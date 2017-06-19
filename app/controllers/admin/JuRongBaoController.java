package controllers.admin;

import controllers.Application;
import models.admin.Applyjurongbao;
import models.searchModel.SearchApplyjurongbao;
import models.templates.JurongbaoFormat;
import models.utils.PageBean;
import models.utils.PageUtil;
import models.utils.StringHelper;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import java.sql.SQLException;
import java.util.List;

/**
 * 申请聚融宝资质Controller
 */
@With(Application.class)
public class JuRongBaoController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //首页
    public static void pageIndex(SearchApplyjurongbao searchApplyjurongbao , int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ?  1 : pageIndex;//当前页
        PageBean pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("applyjurongbao");
        pageBean.setShowColumn("id ,frontuserid ,usertype,name,procmach ,product ,mobphone ,provcode ,citycode,countrycode,address ,to_char(createtime ,'yyyy-mm-dd HH24:mi:ss') createtime ");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchApplyjurongbao != null){
            builderSQL.append(" 1 = 1 ");
            //用户类型
            if(StringHelper.isTrimNotNull(searchApplyjurongbao.getUsertype())){
                builderSQL.append("and usertype = '"+ searchApplyjurongbao.getUsertype() + "'");
            }
            //用户名
            if(StringHelper.isTrimNotNull(searchApplyjurongbao.getName())){
                builderSQL.append("and name = '"+ searchApplyjurongbao.getName() + "'");
            }
            //加工机械
            if(StringHelper.isTrimNotNull(String.valueOf(searchApplyjurongbao.getProcmach()))){
                builderSQL.append("and procmach = '"+ searchApplyjurongbao.getProcmach() + "'");
            }
            //产出产品
            if(StringHelper.isTrimNotNull(searchApplyjurongbao.getProduct())){
                builderSQL.append("and product = '"+ searchApplyjurongbao.getProduct() + "'");
            }
            //创建时间
            if(StringHelper.isTrimNotNull(searchApplyjurongbao.getCreatetime())){
                builderSQL.append("and to_char(createtime , 'yyyy-mm-dd') =  '"+ searchApplyjurongbao.getCreatetime() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }

        //获取用户类型
        List usertypeList = JurongbaoFormat.userType("userType");
        pageBean =  PageUtil.pagination(pageBean);
        render(pageBean ,pageIndex ,searchApplyjurongbao,usertypeList);
    }

    //标题超链接
    public static void detail(Long id){
        //获取聚融宝
        Applyjurongbao applyjurongbao = Applyjurongbao.findById(id);
        //获取常用料
//        Commproduct compro = Commproduct.findById();
        List comproList = JurongbaoFormat.comProduct(id);

        render(applyjurongbao,comproList);
    }
}
