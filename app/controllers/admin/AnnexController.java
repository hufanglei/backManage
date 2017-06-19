package controllers.admin;

import controllers.Application;
import models.admin.Account;
import models.admin.Annex;
import models.admin.Frontuser;
import models.searchModel.SearchAnnex;
import models.templates.AnnexFormat;
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
 * 注册审核Controller
 */
@With(Application.class)
public class AnnexController  extends Controller{
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //首页
    public static void pageIndex(PageBean pageBean , SearchAnnex searchAnnex , int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ?  1 : pageIndex;
        pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName(" (select ann.id,ann.guestid ,ann.annexcode,ann.charterurl,ann.operationid,ann.operationip,ann.affirmregcode,ann.createtime, " +
                "fro.login , fro.name ,acc.id accountId from rec_annex ann,account acc,rec_frontuser fro where ann.operationid = acc.id(+) and ann.guestid = fro.id(+))");
        pageBean.setShowColumn(" id,guestid ,annexcode,charterurl,operationid,operationip,affirmregcode,to_char(createtime , 'yyyy-mm-dd HH24:mi:ss') createtime,login , name ,accountId ");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchAnnex != null){
            builderSQL.append(" 1 = 1 ");
            //登录名
            if(StringHelper.isTrimNotNull(searchAnnex.getLogin())){
                builderSQL.append("and  login = '"+ searchAnnex.getLogin() + "'");
            }
            //姓名
            if(StringHelper.isTrimNotNull(searchAnnex.getName())){
                builderSQL.append("and  name = '"+ searchAnnex.getName() + "'");
            }
            //附件类型
            if(StringHelper.isTrimNotNull(searchAnnex.getAnnexcode())){
                builderSQL.append("and  annexcode = '"+ searchAnnex.getAnnexcode() + "'");
            }
            //操作人
            if(searchAnnex.getOperationid() != null){
                builderSQL.append("and  operationid = "+ searchAnnex.getOperationid() );
            }
            //审核状态
            if(StringHelper.isTrimNotNull(searchAnnex.getStatuscode())){
                builderSQL.append("and  affirmregcode =  '"+ searchAnnex.getStatuscode() + "'");
            }
            //创建时间
            if(StringHelper.isTrimNotNull(searchAnnex.getCreatetime())){
                builderSQL.append("and  to_char(createtime , 'yyyy-mm-dd') = '"+ searchAnnex.getCreatetime() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }
        pageBean =  PageUtil.pagination(pageBean);

        //获取所有前台用户列表
        List frontuserList = Frontuser.getAllLoginAndNames();
        //获取所有后台用户列表
        List accountList = Account.getAllNames();
        //获取附件类型
        List annexcodeList = AnnexFormat.registerType("annex");
        //获取审核状态
        List affirmList = AnnexFormat.registerType("affirmReg");

        render(pageBean , pageIndex ,searchAnnex,frontuserList,accountList,annexcodeList,affirmList);
    }

    /**
     *  注册审核-通过
     *  备注：由于企业用户可能上传三证也可能上传一个证（三证合一<PC端可能有三证>），个人用户只有一张身份证,因此只支持单选
     *  企业用户和个人用户暂时先采取两证的策略，即两证审核通过则真正审核通过
     * @param annexId
     * @throws SQLException
     */
    public static void enabled(Long annexId) throws SQLException {
        //附件表审核通过
        Annex annex = Annex.findById(annexId);
        annex.affirmregcode = Messages.get("AFFIRMREGCODE_SUCCESS");
        annex.operationid =  Long.valueOf(session.get("userId"));
        annex.save();

        //统计当前用户一共上传了几个证
        Long count_annex =  Annex.count("frontuser.id = ? and statuscode='001'" , annex.frontuser.id);
        //统计当前用户审核通过的证数是否等于2
        Long count_success =  Annex.count("frontuser.id = ? and affirmregcode=?" , annex.frontuser.id,"003");
        if(count_success == count_annex){
            //修改公司表为审核通过
            /*Company company = annex.frontuser.comp;
            company.affirmregcode=Messages.get("AFFIRMREGCODE_SUCCESS");
            company.save();*/

            //修改用户为审核通过
            annex.frontuser.affirmregcode = Messages.get("AFFIRMREGCODE_SUCCESS");
            annex.frontuser.save();
        }
        HashMap map = new HashMap();
        map.put("message","ok");
        renderJSON(map);
//        pageIndex(null, null, 0);
    }

    //审核不通过
    public static void disable(Long annexId) throws SQLException {
        //设置附件表为未通过
        Annex annex = Annex.findById(annexId);
        annex.affirmregcode = Messages.get("AFFIRMREGCODE_FAIL");   //附件设置为 审核不通过
        annex.statuscode = Messages.get("STATUSCODE_INVALID");      //附件设置为失效
        annex.operationid =  Long.valueOf(session.get("userId"));
        annex.save();

        /*//设置公司表为审核未通过
        annex.frontuser.comp.affirmregcode=Messages.get("AFFIRMREGCODE_FAIL");
        annex.frontuser.comp.save();*/

        //设置用户状态为审核为通过
        annex.frontuser.affirmregcode = Messages.get("AFFIRMREGCODE_FAIL");
        annex.frontuser.save();

        HashMap map = new HashMap();
        map.put("message","ok");
        renderJSON(map);
//        pageIndex(null, null, 0);
    }
}
