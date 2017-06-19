package controllers.admin;

import controllers.Application;
import models.admin.Company;
import models.admin.Order;
import models.searchModel.SearchOrder;
import models.utils.PageBean;
import models.utils.PageUtil;
import models.utils.StringHelper;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;

/**
 * 订单Controller
 */
@With(Application.class)
public class OrderController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    public static void pageIndex(PageBean pageBean, SearchOrder searchOrder, int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ? 1 : pageIndex;
        pageBean = new PageBean(pageIndex, pageSize, 1);
        pageBean.setTableName("(select od.id,od.orderno,od.createtime,od.companyName ,od.frontuserName,od.orderstatuscode ,a.name backUserName,od.finalmoney from account a \n" +
                " right join \n" +
                "(select o.id, o.orderno ,o.createtime ,c.name companyName ,f.name frontuserName , o.operationid, o.orderstatuscode, s.finalmoney \n" +
                "from rec_order o ,rec_company c , rec_frontuser f ,rec_Stock s , rec_order_stock os\n" +
                "where o.guestid = f.id and c.id = f.companyid \n" +
                "and s.id = os.stockid and o.id = os.orderid \n"+
                ") od\n" +
                "on  od.operationid = a.id)");
        pageBean.setShowColumn("id,orderno,to_char(createtime ,'yyyy-mm-dd HH24:mi:ss') createtime,companyName,frontuserName,backUserName,orderstatuscode,finalmoney");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if (searchOrder != null) {
            builderSQL.append(" 1 = 1 ");
            //订单编号
            if (StringHelper.isTrimNotNull(searchOrder.getOrderno())) {
                builderSQL.append(" and  orderno = " + searchOrder.getOrderno());
            }
            //公司名称
            if (StringHelper.isTrimNotNull(searchOrder.getCompanyName())) {
                builderSQL.append("and  companyName = '" + searchOrder.getCompanyName() + "'");
            }
            //下单时间
            if(StringHelper.isTrimNotNull(searchOrder.getCreatetime())){
                builderSQL.append("and  to_char(createtime , 'yyyy-mm-dd') =  '"+ searchOrder.getCreatetime() + "'");
            }
            //订单金额(起始金额)
            if(StringHelper.isTrimNotNull(searchOrder.getFinalmoneyStart())){
                builderSQL.append("and finalmoney >=  '"+ searchOrder.getFinalmoneyStart() + "'");
            }
            //订单金额(截止金额)
            if(StringHelper.isTrimNotNull(searchOrder.getFinalmoneyEnd())){
                builderSQL.append("and finalmoney <=  '"+ searchOrder.getFinalmoneyEnd() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }
        //获取所有公司列表
        List companyList = Company.getAllNames();
        pageBean = PageUtil.pagination(pageBean);
        render(pageBean, pageIndex,companyList, searchOrder);
    }

    /**
     * 订单详情
     */
    public static void detail(Long orderNo) {
        //查询订单基本信息及买家信息
        Query query = Model.em().createNativeQuery("SELECT RO.id orderId,RO.guestId,RO.orderstatuscode, RO.ORDERNO,\n" +
                "SUM(CASE WHEN RS.STOCKCODE=? THEN RS.PRICEHASTAX * RS.INITNUM END) over(partition BY RS.COMPANYID,RS.STORAGEID,RS.GOODID,RS.PRICEHASTAX) totalPrice,\n" +
                "to_char(RO.CREATETIME,'yyyy-MM-DD hh24:mi:ss')CREATETIME,RC.LINKMAN,RC.\"NAME\" CompanyName,RF.\"NAME\" frontUserName,RF.MOBPHONE,RF.EMAIL\n" +
                "FROM REC_ORDER ro,REC_ORDER_STOCK ros,REC_STOCK rs,REC_FRONTUSER rf,REC_COMPANY rc\n" +
                "WHERE RO.id=ROS.ORDERID AND ROS.STOCKID=RS.id AND RO.GUESTID=RF.id AND RF.COMPANYID=rc.id\n" +
                "AND RO.id=?");
        query.setParameter(1, Messages.get("STOCKCODE_SELLOUT"));
        query.setParameter(2, orderNo);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object obj = query.getSingleResult();

        //查询卖家相关信息
        Query query_sellerCom = Model.em().createNativeQuery("SELECT RC.name sellerCompanyName,RC.LINKMAN linkMan,RS.FRONTUSERID sellerId,RC.MOBPHONE,RC.EMAIL\n" +
                "FROM REC_ORDER ro,REC_ORDER_STOCK ros,REC_STOCK rs,REC_COMPANY rc\n" +
                "WHERE RO.id=ROS.ORDERID AND ROS.STOCKID=RS.id AND RS.COMPANYID = RC.id\n" +
                "AND RO.id=?");
        query_sellerCom.setParameter(1, orderNo);
        query_sellerCom.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object obj_seller = query_sellerCom.getSingleResult();

        //查询商品列表
        Query query_orders = Model.em().createNativeQuery("SELECT ROWNUM,t.* FROM(\n" +
                "SELECT RC.name sellerComName,RC.LINKMAN,RC.MOBPHONE,DG.MARK,DG.producingArea,DG.PRODUCER,RS.PRICEHASTAX,ds.name storageName,\n" +
                "sum(CASE WHEN RS.STOCKCODE=? THEN RS.INITNUM END) over(partition BY RS.STORAGEID,RS.PRICEHASTAX,RS.GOODID) totalNum,\n" +
                "RS.FINALMONEY,\n" +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='goodKind' AND LEVELCODE=dg.goodKindCode) goodKind,\n" +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='package' AND LEVELCODE=dg.packageCode) package,\n" +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='mea' AND LEVELCODE=dg.meaCode) mea,\n" +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='packType' AND LEVELCODE=dg.PACKTYPECODE) packType,\n" +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='packContent' AND LEVELCODE=dg.PACKCONTENT) PACKCONTENT\n" +
                "FROM REC_ORDER ro,REC_ORDER_STOCK ros,REC_STOCK rs,DIC_GOOD dg,REC_COMPANY rc,DIC_STORAGE ds\n" +
                "where RO.id=ROS.ORDERID AND ROS.STOCKID=RS.id AND RS.COMPANYID=RC.id AND RS.GOODID=DG.id AND RS.STORAGEID=DS.id\n" +
                "AND RO.id=?)t");
        query_orders.setParameter(1, Messages.get("STOCKCODE_SELLOUT"));
        query_orders.setParameter(2, orderNo);
        query_orders.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List orderList = query_orders.getResultList();

        render(obj, obj_seller, orderList);
    }

    /**
     * 取消订单
     */
    public static void cancelOrder(Long orderNo) throws SQLException {
        Order order = Order.findById(orderNo);
        order.orderstatuscode = Messages.get("ORDERSTATUS_CANCLE");
        order.save();

        pageIndex(null,null,1);
    }
}
