package controllers.admin;

import controllers.Application;
import models.admin.Contract;
import models.admin.Frontuser;
import models.admin.Order;
import models.searchModel.SearchContract;
import models.templates.CompanyFormat;
import models.utils.*;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.i18n.Messages;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.With;

import javax.persistence.Query;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同Controller
 */
@With(Application.class)
public class ContractController extends Controller {

    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //买方合同列表
    public static void index(PageBean pageBean, SearchContract searchContract) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex, pageSize, 1);
        pageBean.setTableName("(SELECT c.id,RO.ORDERNO,c.CONTRACTNO,c.AMOUNT,c.wordUrl,c.createtime,RC.name COMPANY,a.name,\n" +
                "c.contractStatusCode contractStatus\n" +
                "FROM CONTRACT c,REC_ORDER ro,REC_FRONTUSER rf,account a,REC_COMPANY rc\n" +
                "WHERE c.ORDERID=RO.id AND RO.GUESTID=RF.id AND RO.operationId=a.id AND c.FRONTUSERID=RF.id AND RF.COMPANYID=RC.id\n" +
                "AND RF.GUESTCODE='001')");
        pageBean.setShowColumn("id,orderNo,contractNo,AMOUNT,wordUrl,to_char(CREATETIME,'yyyy-MM-dd hh24:mi:ss')createtime,COMPANY,name,contractStatus");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if (searchContract != null) {
            builderSQL.append(" 1 = 1 ");
            if (StringHelper.isTrimNotNull(searchContract.getOrderno())) {
                builderSQL.append(" and orderno = " + searchContract.getOrderno());
            }
            if (StringHelper.isTrimNotNull(searchContract.getCompanyName())) {
                builderSQL.append(" and company = '" + searchContract.getCompanyName()+"'");
            }
            if (StringHelper.isTrimNotNull(searchContract.getOrderTime())) {
                builderSQL.append(" and to_char(createtime,'yyyy-MM-dd') = '"+searchContract.getOrderTime()+"'");
            }
            if (searchContract.getMoneyFrom() != null && searchContract.getMoneyTo() != null && searchContract.getMoneyFrom() != 0 && searchContract.getMoneyTo() != 0 && searchContract.getMoneyFrom() < searchContract.getMoneyTo()) {
                builderSQL.append(" and amount BETWEEN " + searchContract.getMoneyFrom() + " AND " + searchContract.getMoneyTo());
            }
            pageBean.setCondition(builderSQL.toString());
        }
        pageBean = PageUtil.pagination(pageBean);
        //获取所有公司名称
        List companyList = CompanyFormat.getCompanyListByGuestCode(Messages.get("GUESTCODE_BUYER"));

        render(pageBean, pageIndex, searchContract,companyList);
    }

    //跳转到合同详情页面
    public static void detail(Long id) {
        if (id == null) {
            return;
        } else {
            //买方合同
            Contract contract = Contract.findById(id);
            //卖方合同   卖方和买方合同 订单号一样
            Contract seller_constract = Contract.find("order.id =? and substr(contractstatuscode,0,3) =?", contract.order.id, "002").first();
            //商品信息
            Query query = Model.em().createNativeQuery("select g.mark , g.producer ,st.address, ss.pricehastax ,ss.initnum , ss.finalmoney \n" +
                    "from contract c ,Rec_Order_Stock os ,rec_order o, rec_stock ss ,dic_Storage st, dic_good g\n" +
                    "where c.orderid = o.id and os.orderid = o.id and os.stockid = ss.id and ss.goodid = g.id and ss.storageid = st.id\n" +
                    "and ss.stockcode = '004' and c.id = ?");
            query.setParameter(1, id);
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Object goodObject = query.getResultList().get(0);

            render(contract, seller_constract, goodObject);
        }
        render();
    }


    public static void showContract(long orderNo) {
        //查询买家相关信息和合同号
        Query query_buyer = Model.em().createNativeQuery("SELECT DISTINCT RC.name companyName,RO.CREATETIME,c.CONTRACTNO,c.createTime contractTime,\n" +
                "sum(RS.FINALMONEY) totalMoney ,c.CONTRACTNO,RC.ADDRESS,RC.PHONE,RC.TAXNO\n" +
                "FROM REC_ORDER ro,CONTRACT c,REC_FRONTUSER rf,REC_ORDER_STOCK ros,REC_STOCK rs,REC_COMPANY rc\n" +
                "WHERE RO.id=c.ORDERID AND RO.GUESTID=RF.id AND RO.id=ROS.ORDERID AND ROS.STOCKID=RS.id AND RF.COMPANYID=RC.id\n" +
                "AND c.CONTRACTSTATUSCODE LIKE '001%'\n" +
                "AND RO.id=?\n" +
                "GROUP BY (RC.name,RO.CREATETIME,c.CONTRACTNO,c.createTime,c.contractno,RC.ADDRESS,RC.PHONE,RC.TAXNO)");
        query_buyer.setParameter(1, orderNo);
        query_buyer.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map obj = (Map) query_buyer.getSingleResult();

        //查询卖方相关信息
        Query query_seller = Model.em().createNativeQuery("SELECT DS.name storage,RC.name companyName,RC.ADDRESS,RC.PHONE\n" +
                "FROM(\n" +
                "SELECT min(ROS.STOCKID) stockId\n" +
                "FROM REC_ORDER ro,REC_ORDER_STOCK ros\n" +
                "WHERE RO.id=?)t,REC_STOCK rs,REC_COMPANY rc,DIC_STORAGE ds\n" +
                "WHERE t.stockId=RS.id AND RS.COMPANYID=RC.id AND RS.STORAGEID=DS.id");
        query_seller.setParameter(1, orderNo);
        query_seller.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map seller = (Map) query_seller.getSingleResult();

        //合同提货日期=合同签订日期+3
        obj.put("contractTime", StringHelper.getAddDayDate((Date) obj.get("CONTRACTTIME"), Integer.valueOf(Messages.get("INTERVAL_TIHUO"))));
        //合同总额转换成大写
        obj.put("totalMoney", ConvertNum.NumToChinese(((BigDecimal) obj.get("TOTALMONEY")).doubleValue()));

        //查询商品信息
        Query query_orders = Model.em().createNativeQuery(" SELECT RF.COMPANY,DG.MARK,DG.PRODUCINGAREA,\n" +
                "(SELECT name FROM VIEW_DICTYPE where ENG='goodKind' and LEVELCODE=DG.GOODKINDCODE) goodKind,\n" +
                "sum(CASE WHEN RS.STOCKCODE=? THEN RS.INITNUM END) over(partition by RS.COMPANYID,RS.STORAGEID,RS.PRICEHASTAX,RS.GOODID) totalNum,\n" +
                "RS.PRICEHASTAX,\n" +
                "sum(CASE WHEN RS.STOCKCODE=? THEN RS.FINALMONEY END) over(partition by RS.COMPANYID,RS.STORAGEID,RS.PRICEHASTAX,RS.GOODID) totalMoney\n" +
                "FROM REC_ORDER ro,REC_ORDER_STOCK ros,REC_STOCK rs,REC_FRONTUSER rf,DIC_GOOD dg\n" +
                "WHERE RO.id=ROS.ORDERID AND ROS.STOCKID=RS.id AND RO.GUESTID=RF.id AND RS.GOODID=DG.id\n" +
                "AND RO.id=?");
        query_orders.setParameter(1, Messages.get("STOCKCODE_SELLOUT"));
        query_orders.setParameter(2, Messages.get("STOCKCODE_SELLOUT"));
        query_orders.setParameter(3, orderNo);
        query_orders.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query_orders.getResultList();

        render(obj, seller, list);
    }

    public static void click(long orderNo, long buyerId, long sellerId, Double totalMoney) throws ServletException, IOException, SQLException {
        //新增合同-买方
        Contract contract_buyer = new Contract();
        String buyerContractNo = StringHelper.getContractNo();
        contract_buyer.order = Order.findById(orderNo);
        contract_buyer.frontuser = Frontuser.findById(buyerId);
        contract_buyer.contractno = buyerContractNo;
        contract_buyer.contractstatuscode = Messages.get("CONTRACTSTATUS_INIT_BUYER");
        contract_buyer.createtime = new Date();
        contract_buyer.amount = totalMoney;
        //保存合同word地址
        contract_buyer.wordurl = Messages.get("CONTRACT_WORDURL_BUYER") + buyerContractNo + Messages.get("SUFFIX_WORD");
        contract_buyer.save();

        //新增合同-卖方
        Contract contract_seller = new Contract();
        String sellerContractNo = StringHelper.getContractNo();
        contract_seller.order = Order.findById(orderNo);
        contract_seller.frontuser = Frontuser.findById(sellerId);
        contract_seller.contractno = sellerContractNo;
        contract_seller.contractstatuscode = Messages.get("CONTRACTSTATUS_INIT_SELLER");
        contract_seller.createtime = new Date();
        contract_seller.amount = totalMoney;
        contract_seller.save();
        //contract_seller.wordurl = Messages.get("CONTRACT_WORDURL_SELLER")+sellerContractNo+Messages.get("SUFFIX_WORD");//保存合同word地址
        Order order = Order.findById(orderNo);
        order.operationid = Integer.valueOf(session.get("userId"));// 绑定业务员
        order.orderstatuscode = Messages.get("ORDERSTATUS_WAITPAY");//修改合同状态
        order.save();
        word(orderNo, buyerContractNo);
    }

    public static void word(long orderNo, String contractno_buyer) throws ServletException, IOException, SQLException {
        String html = WebCrawlers.getContractHtml(orderNo);
        WordController.word(html, contractno_buyer);
        index(null, null);
    }

    /* 跳转到 合同管理>卖方合同 */
    public static void sellerContract(PageBean pageBean, SearchContract searchContract) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex, pageSize, 1);
        pageBean.setTableName("(SELECT c.id,RO.ORDERNO,c.CONTRACTNO,c.AMOUNT,c.CREATETIME,RC.name COMPANY,a.name,\n" +
                "c.contractStatusCode contractStatus\n" +
                "FROM CONTRACT c,REC_ORDER ro,REC_FRONTUSER rf,account a,REC_COMPANY rc\n" +
                "WHERE c.ORDERID=RO.id AND RO.operationId=a.id AND c.FRONTUSERID=RF.id and RF.COMPANYID=RC.id\n" +
                "AND RF.GUESTCODE='002')");
        pageBean.setShowColumn("id,ORDERNO,CONTRACTNO,AMOUNT,to_char(CREATETIME,'yyyy-MM-dd hh24:mi:ss') CREATETIME,COMPANY,name,contractStatus");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if (searchContract != null) {
            builderSQL.append(" 1 = 1 ");
            if (StringHelper.isTrimNotNull(searchContract.getOrderno())) {
                builderSQL.append(" and  orderno = " + searchContract.getOrderno());
            }
            if (StringHelper.isTrimNotNull(searchContract.getCompanyName())) {
                builderSQL.append("and  COMPANY = '" + searchContract.getCompanyName() + "'");
            }
            if (StringHelper.isTrimNotNull(searchContract.getOrderTime())) {
                builderSQL.append(" and to_char(createtime,'yyyy-MM-dd') = '" + searchContract.getOrderTime()+"'");
            }
            if (searchContract.getMoneyFrom() != null && searchContract.getMoneyTo() != null && searchContract.getMoneyFrom() != 0 && searchContract.getMoneyTo() != 0 && searchContract.getMoneyFrom() < searchContract.getMoneyTo()) {
                builderSQL.append(" and amount BETWEEN " + searchContract.getMoneyFrom() + " AND " + searchContract.getMoneyTo());
            }
            pageBean.setCondition(builderSQL.toString());
        }
        pageBean = PageUtil.pagination(pageBean);
        //获取所有公司名称
        List companyList = CompanyFormat.getCompanyListByGuestCode(Messages.get("GUESTCODE_SELLER"));

        render(pageBean, pageIndex, searchContract,companyList);
    }


    //跳转到原始合同
    public static void originalContract(Long orderId, Long contractId) {
        render(orderId, contractId);
    }


    //核对合同
    public static void checkContract(Long orderId, Long contractId) {
        Contract contract = Contract.findById(contractId);
        render(orderId, contractId, contract);
    }


    /* 跳转到 合同管理>卖方合同>合同详情 */
    public static void mfContractDetails(Long id) {
        if (id == null) {
            return;
        } else {
            //买方合同
            Contract contract = Contract.findById(id);
            //买方合同   卖方和买方合同 订单号一样
            Contract buyer_constract = Contract.find("order.id =? and substr(contractstatuscode,0,3) =?", contract.order.id, "001").first();
            //商品信息
            Query query = Model.em().createNativeQuery("select g.mark , g.producer ,st.address, ss.pricehastax ,ss.initnum , ss.finalmoney \n" +
                    "from contract c ,Rec_Order_Stock os ,rec_order o, rec_stock ss ,dic_Storage st, dic_good g\n" +
                    "where c.orderid = o.id and os.orderid = o.id and os.stockid = ss.id and ss.goodid = g.id and ss.storageid = st.id\n" +
                    "and ss.stockcode = ? and c.id = ?");
            query.setParameter(1, Messages.get("STOCKCODE_SELLOUT"));
            query.setParameter(2, id);
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Object goodObject = query.getResultList().get(0);
            render(contract, buyer_constract, goodObject);
        }
        render();
    }

    /* 跳转到 合同管理>卖方合同>协助上传 */
    public static void mforiginalContract(long contractId) {
        render(contractId);
    }

    /**
     * 图片上传
     */
    public static void mfUploadImg(long contractId, File fileText) {
//        Files.copy(fileText, new File("F:\\" + fileText.getName()));//本地
        Contract contract = Contract.findById(contractId);
        contract.picurl1 = Messages.get("HELP_UPLOADURL_SELLER") + contract.contractno + Messages.get("SUFFIX_IMG");
        contract.save();
        Files.copy(fileText, new File("/home/backManage/contract/seller/img/" + contract.contractno + Messages.get("SUFFIX_IMG")));//正式服务器

        mforiginalContract(contractId);
    }

    /* 市场管理 */
    public static void marketManagement() {
        render();
    }

    /* 个人中心 */
    public static void personalCenter() {
        render();
    }

    /* 金融中心 */
    public static void financialCenter() {
        render();
    }

    /* 配送管理 */
    public static void distributionManagement() {
        render();
    }

    /* 资讯中心 */
    public static void informationCenter() {
        render();
    }

    /* 数据中心 */
    public static void dataCenter() {
        render();
    }

    /* 客服中心 */
    public static void serviceCenter() {
        render();
    }

    /* 系统配置 */
    public static void systemConfiguration() {
        render();
    }

    /* 客户关系维护 */
    public static void clientRelationsMaintain() {
        render();
    }

    /* 货物保留时间 */
    public static void goodsKeepTime() {
        render();
    }

    /* 增加页面 */
    public static void Increase() {
        render();
    }

    public static void Editor() {
        render();
    }

    /**
     * 修订合同
     */
    public static void editContract(long orderId) {
        showContract(orderId);
    }

}



