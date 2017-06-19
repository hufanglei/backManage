package controllers.admin;

import controllers.Application;
import models.admin.Company;
import models.admin.Good;
import models.admin.Stock;
import models.admin.Storage;
import models.searchModel.SearchStock;
import models.templates.StockFormat;
import models.utils.PageBean;
import models.utils.PageUtil;
import models.utils.StringHelper;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by peak on 2016/6/22 0022.
 */
@With(Application.class)
public class StockController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));
    /**
     * 库存列表
     */
    public static void list(PageBean pageBean , SearchStock searchStock) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("(SELECT RS.id stockId,DG.MARK,rf.name as username,RC.name companyName,DS.name storageName,RS.STOCKCODE," +
                "TO_CHAR(RS.CREATETIME,'yyyy-MM-dd  hh24:mi:ss')CREATETIME,RS.INITNUM,RS.AFFIRMNUM,RS.AFFIRMSTOCKCODE\n" +
                "FROM REC_STOCK rs,DIC_GOOD dg,REC_COMPANY rc,DIC_STORAGE ds,REC_FRONTUSER rf\n" +
                "WHERE RS.GOODID=DG.id AND RS.COMPANYID=RC.id AND RS.STORAGEID=DS.id\n" +
                "AND RS.STOCKCODE='001' AND rf.companyid=rc.id and RF.id = RS.FRONTUSERID)");
        pageBean.setShowColumn("*");
        pageBean.setOrderBy("createtime");
        pageBean.setFlag(1);
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchStock != null){
            builderSQL.append(" 1 = 1 ");
            //牌号
            if(StringHelper.isTrimNotNull(searchStock.getMark())){
                builderSQL.append(" and  MARK = '"+ searchStock.getMark() + "'");
            }
            //公司名
            if(StringHelper.isTrimNotNull(searchStock.getCompanyName())){
                builderSQL.append("and  companyName = '"+ searchStock.getCompanyName() + "'");
            }
            //仓库名
            if(StringHelper.isTrimNotNull(searchStock.getStorageName())){
                builderSQL.append("and  storageName = '"+ searchStock.getStorageName() + "'");
            }
            //审核状态
            if(StringHelper.isTrimNotNull(searchStock.getAffirmStockCode())){
                builderSQL.append("and  AFFIRMSTOCKCODE = '"+ searchStock.getAffirmStockCode() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }
        pageBean =  PageUtil.pagination(pageBean);

        //获取所有的商品牌号
        List markList = Good.getAllMarks();
        //获取所有的卖家公司名称
        List sellerCompanyNames = Company.getAllSellerNames();
        //获取所有的仓库名称
        List storageNames = Storage.getAllNames();
        //获取所有的审核状态
        List affirmList = StockFormat.getAffirmStockList();

        render(pageBean , pageIndex ,searchStock,markList,sellerCompanyNames,storageNames,affirmList);
    }

    /**
     * 上架审核列表
     */
    public static void arrivalList(PageBean pageBean , SearchStock searchStock) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("(SELECT RS.id stockId,DG.MARK,rf.name as username,RC.name companyName,DS.name storageName,RS.STOCKCODE," +
                "TO_CHAR(RS.createtime,'yyyy-MM-dd hh24:mi:ss')createtime,RS.INITNUM,RS.AFFIRMNUM,RS.AFFIRMSTOCKCODE\n" +
                "FROM REC_STOCK rs,DIC_GOOD dg,REC_COMPANY rc,DIC_STORAGE ds,REC_FRONTUSER rf\n" +
                "WHERE RS.GOODID=DG.id AND RS.COMPANYID=RC.id AND RS.STORAGEID=DS.id\n" +
                "AND RS.STOCKCODE='002' AND rf.companyid=rc.id " +
                "and RF.id = RS.FRONTUSERID" +
                ")");
        pageBean.setShowColumn("*");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchStock != null){
            builderSQL.append(" 1 = 1 ");
            //牌号
            if(StringHelper.isTrimNotNull(searchStock.getMark())){
                builderSQL.append(" and  MARK = '"+ searchStock.getMark() + "'");
            }
            //公司名
            if(StringHelper.isTrimNotNull(searchStock.getCompanyName())){
                builderSQL.append("and  companyName = '"+ searchStock.getCompanyName() + "'");
            }
            //仓库名
            if(StringHelper.isTrimNotNull(searchStock.getStorageName())){
                builderSQL.append("and  storageName = '"+ searchStock.getStorageName() + "'");
            }
            //审核状态
            if(StringHelper.isTrimNotNull(searchStock.getAffirmStockCode())){
                builderSQL.append("and  AFFIRMSTOCKCODE = '"+ searchStock.getAffirmStockCode() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }
        pageBean =  PageUtil.pagination(pageBean);

        //获取所有的商品牌号
        List markList = Good.getAllMarks();
        //获取所有的卖家公司名称
        List sellerCompanyNames = Company.getAllSellerNames();
        //获取所有的仓库名称
        List storageNames = Storage.getAllNames();
        //获取所有的审核状态
        List affirmList = StockFormat.getAffirmStockList();

        render(pageBean , pageIndex ,searchStock,markList,sellerCompanyNames,storageNames,affirmList);
    }

    /**
     * 上架确认数量-通过
     */
    public static void affirmPass(long stockId,String stockCode,Double affirmNum) {
        Stock st = Stock.findById(stockId);
        st.affirmnum = affirmNum;
        st.affirmstockcode = Messages.get("AFFIRMSTOCKCODE_CONFIRM");
        st.save();
        // 入库
//        if(stockCode.equals("001")){
//            list(null,null);
//        } else if (stockCode.equals("002")){
//            arrivalList(null,null);
//        }
        HashMap map = new HashMap();
        map.put("message","ok");
         renderJSON(map);
    }

    /**
     * 上架确认数量-驳回
     */
    public static void affirmFail(long stockId,String stockCode,Double affirmNum) {
        Stock st = Stock.findById(stockId);
        st.affirmnum = affirmNum;
        st.affirmstockcode = "003";
        st.save();
        HashMap map = new HashMap();
        map.put("message","ok");
        renderJSON(map);
    }

}
