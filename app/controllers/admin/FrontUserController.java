package controllers.admin;

import controllers.Application;
import models.admin.Account;
import models.admin.Company;
import models.admin.Frontuser;
import models.searchModel.SearchFrontUser;
import models.templates.FrontUserFormat;
import models.utils.PageBean;
import models.utils.PageUtil;
import models.utils.StringHelper;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.data.binding.ParamNode;
import play.data.binding.RootParamNode;
import play.db.jpa.Model;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import javax.persistence.Query;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 前台用户Controller
 */
@With(Application.class)
public class FrontUserController  extends Controller{
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //首页
    public static void pageIndex(SearchFrontUser searchFrontUser , int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ?  1 : pageIndex;//当前页
        PageBean pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("rec_frontuser");
        pageBean.setShowColumn("id , login , guestcode , company , name , mobphone , affirmregcode , statuscode , to_char(createtime ,'yyyy-mm-dd HH24:mi:ss') createtime");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchFrontUser != null){
            builderSQL.append(" 1 = 1 ");
            //登录名
            if(StringHelper.isTrimNotNull(searchFrontUser.getLogin())){
                builderSQL.append("and  login = '"+ searchFrontUser.getLogin() + "'");
            }
            //姓名
            if(StringHelper.isTrimNotNull(searchFrontUser.getName())){
                builderSQL.append("and  name = '"+ searchFrontUser.getName() + "'");
            }
            //手机号
            if(StringHelper.isTrimNotNull(searchFrontUser.getMobphone())){
                builderSQL.append("and  mobphone = '"+ searchFrontUser.getMobphone() + "'");
            }
            //认证状态
            if(StringHelper.isTrimNotNull(searchFrontUser.getAffirmregcode())){
                builderSQL.append("and  affirmregcode = '"+ searchFrontUser.getAffirmregcode() + "'");
            }
            //有效状态
            if(StringHelper.isTrimNotNull(searchFrontUser.getStatuscode())){
                builderSQL.append("and  statuscode = '"+ searchFrontUser.getStatuscode() + "'");
            }
            //公司名称
            if(StringHelper.isTrimNotNull(searchFrontUser.getCompany())){
                builderSQL.append("and  company = '"+ searchFrontUser.getCompany() + "'");
            }
            //新增用户周期
            if(StringHelper.isTrimNotNull(searchFrontUser.getCycle())){
                builderSQL.append("and SYSDATE - createtime <= '"+Integer.valueOf(searchFrontUser.getCycle()) + "'");
            }

            pageBean.setCondition(builderSQL.toString());
        }

        //获取所有的查询参数数值
        Query query = Model.em().createNativeQuery("select login , company , name , mobphone , affirmregcode , statuscode from rec_frontuser");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List searchUserData = query.getResultList();
        //获取所有的审核状态
        List affirmList = FrontUserFormat.registerType("affirmReg");
        //获取有效状态
        List statusList = FrontUserFormat.registerType("status");
        //获取所有的公司名称，防止重复
        List companyNames = Company.getAllNames();

        pageBean =  PageUtil.pagination(pageBean);
        render(searchUserData,pageBean , pageIndex ,searchFrontUser,affirmList,statusList,companyNames);
    }


    //跳转到新增页面
    public static void add(){
        render();
    }

    public static void detail(){
        render();
    }


    //跳转到详情页面
    public static void edit(Long frontuserId){
        if(frontuserId==null){
            return;
        }
        Frontuser frontuser = Frontuser.findById(frontuserId);
        List registerTypes = FrontUserFormat.registerType("guest"); //获得所有注册类型
        List affirmRegTypes = FrontUserFormat.registerType("affirmReg"); //认证状态(code= "affirmReg")
        List statusTypes = FrontUserFormat.registerType("status"); //获得所有有效状态(code="status")
        List<Account>  accounts = Account.findAll(); //获得所有后台用户（和业务员关联）
        List<Company> companies = Company.findAll();   //获得所有公司（绑定公司用）
        render(frontuser ,registerTypes ,affirmRegTypes , statusTypes ,accounts ,companies);
    }

    //跳转到详情页面
    public static void update(Long id,String password) throws SQLException, NoSuchAlgorithmException {
        Frontuser frontuser = Frontuser.findById(id);
        if(frontuser==null){
            return;
        }

        RootParamNode rootParamNode = ParamNode.convert(params.all());
        frontuser.edit(rootParamNode,"frontuser");
        validation.valid(frontuser);
        if(validation.hasErrors()) {
            edit(id);
        } else{
            if(!frontuser.password.equals(password))
                frontuser.password = StringHelper.md5(password);
            frontuser.save(); // 强制保存
        }
        pageIndex(null, 0);
    }


    //新增 前台用户
    public static  void save(Frontuser frontuser) throws SQLException {
        frontuser.createtime = new Date();
        frontuser.save();
        pageIndex(null ,0);
    }


    //启用
    public static void enabled(Long frontuserId) throws SQLException {
         if(frontuserId==null){
             return;
         }
        Frontuser frontuser = Frontuser.findById(frontuserId);
        frontuser.statuscode = "001";   //设置为 启用状态
        frontuser.save();
        pageIndex(null , 0);
    }

    //禁用
    public static void disable(Long frontuserId) throws SQLException {
        if(frontuserId==null){
            return;
        }
        Frontuser frontuser = Frontuser.findById(frontuserId);
        frontuser.statuscode = "002";  //设置为禁用状态
        frontuser.save();
        pageIndex(null, 0);

    }

    /**
     * 新增公司-页面
     */
    public static void toAddCompany(Long frontuserId,boolean flag){
        Frontuser frontuser = Frontuser.findById(frontuserId);
        Company company = frontuser.comp;
        List registerTypeList = FrontUserFormat.registerType("guest"); //获得所有注册类型
        List affirmRegTypes = FrontUserFormat.registerType("affirmReg"); //认证状态(code= "affirmReg")
        List<Account>  accounts = Account.findAll(); //获得所有后台用户（和业务员关联）
        List statusTypes = FrontUserFormat.registerType("status"); //获得所有有效状态(code="status")
        render(frontuser,company, frontuserId,registerTypeList,affirmRegTypes,accounts,statusTypes,flag);
    }

    /**
     * 新增公司-保存
     */
    public static void addCompany(Long frontuserId,Company company){
        company.createtime = new Date();
        //操作IP地址
        company.operationip = StringHelper.getRequestIpAddr(request);
        //操作人ID
        company.operationid = Integer.valueOf(session.get("userId"));
        company.save();

        //用户绑定公司
        Frontuser frontuser = Frontuser.findById(frontuserId);
        frontuser.comp = company;
        frontuser.save();

        boolean flag = true;
        toAddCompany(frontuserId,flag);
    }

    /**
     * 供应商资质申请列表
     */
    public static void applySeller(SearchFrontUser searchFrontUser,int pageIndex) throws SQLException {
        //获取所有的供应商申请数据
        pageIndex = pageIndex == 0 ?  1 : pageIndex;//当前页
        PageBean pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("rec_frontuser");
        pageBean.setShowColumn("id,usertypecode,name,company,mobphone,email,address,affirmregcode,createtime");
        pageBean.setCondition("affirmregcode in('005','006','007')");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchFrontUser != null){
            builderSQL.append(" 1 = 1 ");
            //姓名
            if(StringHelper.isTrimNotNull(searchFrontUser.getName())){
                builderSQL.append("and  name = '"+ searchFrontUser.getName() + "'");
            }
            //卖家公司
            if(StringHelper.isTrimNotNull(searchFrontUser.getCompany())){
                builderSQL.append("and  company = '"+ searchFrontUser.getCompany() + "'");
            }
            //手机号
            if(StringHelper.isTrimNotNull(searchFrontUser.getMobphone())){
                builderSQL.append("and  mobphone = '"+ searchFrontUser.getMobphone() + "'");
            }
            //审核状态
            if(StringHelper.isTrimNotNull(searchFrontUser.getAffirmregcode())){
                builderSQL.append("and  affirmregcode = '"+ searchFrontUser.getAffirmregcode() + "'");
            }

            pageBean.setCondition(builderSQL.toString());
        }

        //获取所有已申请、审核成功、审核失败用户信息
        List<Frontuser> applyUsers = Frontuser.find("affirmregcode in (?,?,?)","005","006","007").fetch();

        pageBean =  PageUtil.pagination(pageBean);
        render(pageBean , pageIndex ,searchFrontUser,applyUsers);
    }

    /**
     * 供应商资质申请-通过
     */
    public static void passApplySeller (long frontUserId) throws SQLException {
        Frontuser frontuser = Frontuser.findById(frontUserId);
        frontuser.affirmregcode = "006";
        frontuser.save();

        HashMap map = new HashMap();
        map.put("message","ok");
        renderJSON(map);
    }

    /**
     * 供应商资质申请-驳回
     */
    public static void rejectApplySeller (long frontUserId) throws SQLException {
        Frontuser frontuser = Frontuser.findById(frontUserId);
        frontuser.affirmregcode = "007";
        frontuser.save();
        HashMap map = new HashMap();
        map.put("message","ok");
        renderJSON(map);
    }
}
