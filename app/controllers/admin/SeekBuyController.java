package controllers.admin;

import controllers.Application;
import models.admin.Company;
import models.searchModel.SearchSeekBuy;
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
 * 手机列表Controller
 */
@With(Application.class)

public class SeekBuyController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    public static void pageIndex(PageBean pageBean, SearchSeekBuy searchSeekBuy, int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ? 1 : pageIndex;
        pageBean = new PageBean(pageIndex, pageSize, 1);
        pageBean.setTableName("(select sb.id,sb.codeno,sb.totalprice,sb.createtime,sb.price,sb.orderstatuscode,rc.name companyname,rf. name\n" +
                "from seekbuy sb,rec_frontuser rf,rec_company rc \n" +
                "where sb.buyuserid = rf. id \n" +
                "and rf.companyid = rc. id and sb.statuscode = '002')");
        pageBean.setShowColumn("id,codeno,totalprice,to_char(createtime ,'yyyy-mm-dd HH24:mi:ss') createtime,price,orderstatuscode,companyname,name");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if (searchSeekBuy != null) {
            builderSQL.append(" 1 = 1 ");
            //订单编号
            if (StringHelper.isTrimNotNull(searchSeekBuy.getOrderno())) {
                builderSQL.append(" and  codeno = " + searchSeekBuy.getOrderno());
            }
            //公司名称
            if (StringHelper.isTrimNotNull(searchSeekBuy.getCompanyName())) {
                builderSQL.append("and  companyname = '" + searchSeekBuy.getCompanyName() + "'");
            }
            //下单时间
            if(StringHelper.isTrimNotNull(searchSeekBuy.getCreatetime())){
                builderSQL.append("and  to_char(createtime , 'yyyy-mm-dd') =  '"+ searchSeekBuy.getCreatetime() + "'");
            }
            //订单金额(起始金额)
            if(StringHelper.isTrimNotNull(searchSeekBuy.getPrice())){
                builderSQL.append("and totalPrice >=  '"+ searchSeekBuy.getPrice() + "'");
            }
            //订单金额(截止金额)
            if(StringHelper.isTrimNotNull(searchSeekBuy.getTotalPrice())){
                builderSQL.append("and totalPrice <=  '"+ searchSeekBuy.getTotalPrice() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }
        //获取所有公司列表
        List companyList = Company.getAllNames();
        pageBean = PageUtil.pagination(pageBean);
        render(pageBean, pageIndex,companyList, searchSeekBuy);
    }

    /**
     * 订单详情
     */
    public static void detail(Long  seekBuyNo) {
        //查询订单基本信息及买家信息
        Query query = Model.em().createNativeQuery("(select sb.id,sb.codeno,sb.totalprice,sb.createtime,sb.orderstatuscode,SB.MOBPHONE,rc.name companyname,rf.name \n" +
                " from seekbuy sb,rec_frontuser rf,rec_company rc \n" +
                " where sb.buyuserid = rf. id \n" +
                "and rf.companyid = rc. id " +
                "and sb.id =?\n)");
        //query.setParameter(1, Messages.get("STOCKCODE_SELLOUT"));
        query.setParameter(1, seekBuyNo);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object obj = query.getSingleResult();
        //查询卖家相关信息
       Query query_sellerCom = Model.em().createNativeQuery("(SELECT sb.buyuserid,rc. NAME sellerCompanyName,RC.LINKMAN,RC.MOBPHONE,RC.EMAIL \n" +
                "FROM  seekbuy sb,rec_frontuser rf,rec_company rc\n" +
                "WHERE sb.buyuserid = rf. ID AND rf.companyid = rc. ID\n" +
                "and sb.id =?)");
        query_sellerCom.setParameter(1, seekBuyNo);
        query_sellerCom.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object obj_seller = query_sellerCom.getSingleResult();
        //查询商品列表
        Query query_seekBuys = Model.em().createNativeQuery("(select rownum,t.* from(\n" +
                "select sb.buyuserid,sb.mark,sb.address storagename,sb.price,sb.num,sb.totalprice,rc. name sellercompanyname,rc.linkman,rc.mobphone,rc.email,\n" +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='goodKind' AND LEVELCODE=sb.goodKind) goodKind,\n" +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='packType' AND LEVELCODE=sb.statuscode) packType," +
                "(SELECT name FROM VIEW_DICTYPE WHERE eng='packContent' AND LEVELCODE=sb.paycode) PACKCONTENT\n" +
                "from seekbuy sb,rec_frontuser rf,rec_company rc,dic_storage ds\n" +
                "where sb.buyuserid = rf. id and rf.companyid = rc. ID and SB.STORAGEID=DS.id\n" +
                "AND sb.id=?)t)");
        // query_orders.setParameter(1, Messages.get("STOCKCODE_SELLOUT"));
        query_seekBuys.setParameter(1, seekBuyNo);
        query_seekBuys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List seekBuyList = query_seekBuys.getResultList();

        render(obj, obj_seller, seekBuyList);
    }
}


