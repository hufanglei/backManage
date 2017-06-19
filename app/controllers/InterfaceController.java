package controllers;

import models.admin.Contract;
import models.admin.Frontuser;
import models.admin.SeekBuy;
import models.utils.ConvertNum;
import models.utils.StringHelper;
import models.utils.WebCrawlers;
import models.utils.WordController;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.i18n.Messages;
import play.mvc.Controller;

import javax.persistence.Query;
import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: zjw
 * DateTime: 2017/2/6 0006 14:01
 */
public class InterfaceController extends Controller {

    public static void click(long seekbuyid, long buyerId, long sellerId, Double totalMoney) throws ServletException, IOException, SQLException {
        SeekBuy seekbuy = SeekBuy.findById(seekbuyid);

        //新增合同-买方
//        Contract contract_buyer = new Contract();
        Contract contract_buyer = Contract.find("from Contract where seekbuy.id = ? and contractstatuscode = ?", seekbuyid, Messages.get("CONTRACTSTATUS_INIT_BUYER")).first();
        if(contract_buyer == null)
            contract_buyer = new Contract();
        String buyerContractNo = StringHelper.getMobileContractNo();
        contract_buyer.seekbuy = seekbuy;
        contract_buyer.frontuser = Frontuser.findById(buyerId);
        contract_buyer.contractno = buyerContractNo;
        contract_buyer.contractstatuscode = Messages.get("CONTRACTSTATUS_INIT_BUYER");
        contract_buyer.createtime = new Date();
        contract_buyer.amount = totalMoney;
        //保存合同word地址
        contract_buyer.wordurl = Messages.get("CONTRACT_WORDURL_BUYER") + buyerContractNo + Messages.get("SUFFIX_WORD");
        contract_buyer.save();

        //新增合同-卖方
//        Contract contract_seller = new Contract();
        Contract contract_seller = Contract.find("from Contract where seekbuy.id = ? and contractstatuscode = ?", seekbuyid, Messages.get("CONTRACTSTATUS_INIT_SELLER")).first();
        if(contract_seller == null)
            contract_seller = new Contract();
        String sellerContractNo = StringHelper.getMobileContractNo();
        contract_seller.seekbuy = seekbuy;
        contract_seller.frontuser = Frontuser.findById(sellerId);
        contract_seller.contractno = sellerContractNo;
        contract_seller.contractstatuscode = Messages.get("CONTRACTSTATUS_INIT_SELLER");
        contract_seller.createtime = new Date();
        contract_seller.amount = totalMoney;
        contract_seller.save();

        seekbuy.orderstatuscode = Messages.get("ORDERSTATUS_WAITPAY");//修改合同状态
        seekbuy.save();

        word(seekbuyid, buyerContractNo);

        renderJSON("0000");
    }

    public static void word(long codeno, String contractno_buyer) throws ServletException, IOException, SQLException {
        String html = WebCrawlers.getMobileContractHtml(codeno);
        WordController.word(html, contractno_buyer);
    }

    public static void showMobileContract(long codeno) {
        //查询买家相关信息和合同号
        Query query_buyer = Model.em().createNativeQuery("SELECT DISTINCT RC.name companyName,sb.CREATETIME,c.CONTRACTNO,c.createTime contractTime,\n" +
                "sb.TOTALPRICE totalMoney ,c.CONTRACTNO,RC.ADDRESS,RC.PHONE,RC.TAXNO\n" +
                "FROM SEEKBUY sb,CONTRACT c,REC_FRONTUSER rf,REC_COMPANY rc\n" +
                "WHERE sb.id=c.SEEKBUYID AND sb.BUYUSERID=RF.id  AND RF.COMPANYID=RC.id\n" +
                "AND c.CONTRACTSTATUSCODE LIKE '001%'\n" +
                "AND sb.id=?\n");
        query_buyer.setParameter(1, codeno);
        query_buyer.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map obj = (Map) query_buyer.getResultList().get(0);

        //查询卖方相关信息
        Query query_seller = Model.em().createNativeQuery("SELECT sb.address storage,RC.name companyName,RC.ADDRESS,RC.PHONE\n" +
                "FROM SEEKBUY sb,CONTRACT c, REC_FRONTUSER rf, REC_COMPANY rc\n" +
                "WHERE sb.id = ? and sb.id=c.SEEKBUYID\n" +
                "AND c.CONTRACTSTATUSCODE LIKE '002%'\n" +
                "AND c.FRONTUSERID=RF.id AND RF.COMPANYID=RC.id \n");
        query_seller.setParameter(1, codeno);
        query_seller.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map seller = (Map) query_seller.getResultList().get(0);

        //合同提货日期=合同签订日期+3
        obj.put("contractTime", StringHelper.getAddDayDate((Date) obj.get("CONTRACTTIME"), Integer.valueOf(Messages.get("INTERVAL_TIHUO"))));
        //合同总额转换成大写
        obj.put("totalMoney", ConvertNum.NumToChinese(((BigDecimal) obj.get("TOTALMONEY")).doubleValue()));

        //查询商品信息
        Query query_orders = Model.em().createNativeQuery(" SELECT sb.MARK,'' PRODUCINGAREA ,\n" +
//                "(SELECT PRODUCINGAREA FROM DIC_GOOD DG WHERE dg.GOODKINDCODE = sb.goodkind AND dg.MARK = sb.MARK) PRODUCINGAREA,\n" +
                "(SELECT name FROM VIEW_DICTYPE where ENG='goodKind' and LEVELCODE=sb.goodkind) goodKind,\n" +
                "sb.num totalNum, sb.price PRICEHASTAX, sb.totalprice totalMoney\n" +
                "FROM SEEKBUY sb\n" +
                "WHERE sb.id=?");
        query_orders.setParameter(1, codeno);
        query_orders.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query_orders.getResultList();

        render(obj, seller, list);
    }

}
