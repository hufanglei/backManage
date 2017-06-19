package controllers.admin;

import controllers.Application;
import models.admin.Account;
import models.admin.Company;
import models.admin.Frontuser;
import models.searchModel.SearchCompany;
import models.templates.FrontUserFormat;
import models.utils.PageBean;
import models.utils.PageUtil;
import models.utils.StringHelper;
import play.data.binding.ParamNode;
import play.data.binding.RootParamNode;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by peak on 2016/6/15 0015.
 */
@With(Application.class)
public class CompanyController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));
    /**
     * 前台公司列表
     */
    public static void index(PageBean pageBean , SearchCompany searchCompany) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("(rec_company)");
        pageBean.setShowColumn("*");
        pageBean.setOrderBy("CREATETIME");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchCompany != null){
            builderSQL.append(" 1 = 1 ");
            if(StringHelper.isTrimNotNull(searchCompany.getName())){
                builderSQL.append("and  name = '"+ searchCompany.getName() + "'");
            }
            if(StringHelper.isTrimNotNull(searchCompany.getGuestcode())){
                builderSQL.append("and  guestCode = '"+ searchCompany.getGuestcode() + "'");
            }
            if(StringHelper.isTrimNotNull(searchCompany.getLinkman())){
                builderSQL.append("and  linkMan = '"+ searchCompany.getLinkman() + "'");
            }
            if(StringHelper.isTrimNotNull(searchCompany.getUnitno())){
                builderSQL.append("and  unitNo = '"+ searchCompany.getUnitno() + "'");
            }
            if(StringHelper.isTrimNotNull(searchCompany.getTaxno())){
                builderSQL.append("and  taxNo = '"+ searchCompany.getTaxno() + "'");
            }
            if(StringHelper.isTrimNotNull(searchCompany.getAffirmregcode())){
                builderSQL.append("and  affirmregcode = '"+ searchCompany.getAffirmregcode() + "'");
            }

            pageBean.setCondition(builderSQL.toString());
        }
        pageBean =  PageUtil.pagination(pageBean);

        //获取所有的公司名
//        List companyNames = Company.getAllNames();//公司列表，公司名不可能重复，前台新增公司时要进行唯一性检查
        //获取所有的注册类型
        List regTypeList = FrontUserFormat.registerType("guest");
        //获取所有的审核状态
        List affirmList = FrontUserFormat.registerType("affirmReg");
        //统计所有的买家公司
        long count_buyer = Frontuser.count("guestcode=?", Messages.get("GUESTCODE_BUYER"));
        //统计所有的卖家公司
        long count_seller = Frontuser.count("guestcode=?",Messages.get("GUESTCODE_SELLER"));
        //统计审核成功的公司数量
        long count_affirmSuccess = Company.count("affirmregcode=?",Messages.get("AFFIRMREGCODE_SUCCESS"));

        render(pageBean, pageIndex, searchCompany, regTypeList, affirmList,count_buyer,count_seller,count_affirmSuccess);
    }

    /**
     * 编辑-页面
     */
    public static void toEditCompany(long companyId){
        Company company = Company.findById(companyId);
        List registerTypeList = FrontUserFormat.registerType("guest"); //获得所有注册类型
        List certTypeList = FrontUserFormat.certType(); //获得证件类型(1,52,73)
        List affirmRegTypes = FrontUserFormat.registerType("affirmReg"); //认证状态(code= "affirmReg")
        List<Account>  accounts = Account.findAll(); //获得所有后台用户（和业务员关联）
        List statusTypes = FrontUserFormat.registerType("status"); //获得所有有效状态(code="status")
        String name = session.get("name");
        render(company,registerTypeList,certTypeList,affirmRegTypes,accounts,statusTypes,name);
    }

    /**
     * 编辑-保存
     */
    public static void editCompany(Long companyId) throws SQLException {

        Company company = Company.findById(companyId);

        RootParamNode rootParamNode = ParamNode.convert(params.all());
        company.edit(rootParamNode, "company");
        validation.valid(company);
        if(validation.hasErrors()) {
            toEditCompany(companyId);
        } else{
            company.save(); // 强制保存
        }

        index(null,null);
    }
}
