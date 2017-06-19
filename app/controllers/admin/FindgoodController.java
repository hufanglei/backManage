package controllers.admin;

import controllers.Application;
import models.admin.Findgood;
import models.searchModel.SearchFindgood;
import models.templates.FindgoodFormat;
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
 * 帮您找货Controller
 */
@With(Application.class)
public class FindgoodController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //首页
    public static void pageIndex(SearchFindgood searchFindgood , int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ?  1 : pageIndex;//当前页
        PageBean pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("rec_findgood");
        pageBean.setShowColumn("id , title , content,linkman,phone,sup,dealcode,operationid,operationip,to_char(begintime ,'yyyy-mm-dd HH24:mi:ss') begintime,memo ");
        pageBean.setOrderBy("begintime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchFindgood != null){
            builderSQL.append(" 1 = 1 ");
            //联系人
            if(StringHelper.isTrimNotNull(searchFindgood.getLinkman())){
                builderSQL.append("and  linkman = '"+ searchFindgood.getLinkman() + "'");
            }
            //手机号
            if(StringHelper.isTrimNotNull(searchFindgood.getPhone())){
                builderSQL.append("and  phone = '"+ searchFindgood.getPhone() + "'");
            }
            //受理状态
            if(StringHelper.isTrimNotNull(searchFindgood.getDealcode())){
                builderSQL.append("and  dealcode = '"+ searchFindgood.getDealcode() + "'");
            }
            //成交供应商
            if(StringHelper.isTrimNotNull(searchFindgood.getSup())){
                builderSQL.append("and  sup = '"+ searchFindgood.getSup() + "'");
            }
            //开始时间
            if(StringHelper.isTrimNotNull(searchFindgood.getBegintime())){
                builderSQL.append("and  to_char(begintime , 'yyyy-mm-dd') =  '"+ searchFindgood.getBegintime() + "'");
            }
            //截止时间
            if(StringHelper.isTrimNotNull(searchFindgood.getEndtime())){
                builderSQL.append("and  to_char(endtime , 'yyyy-mm-dd') = '"+ searchFindgood.getEndtime() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }

        //获取受理（进展）状态
        List dealcodeList = FindgoodFormat.dealcodeType("deal");
        //获取联系人
        List linkmanList = FindgoodFormat.linkmanList();
        //获取手机号
        List phoneList = FindgoodFormat.phoneList();
        //获取成交供应商
        List supList = FindgoodFormat.supList();

        pageBean =  PageUtil.pagination(pageBean);
        render(pageBean , pageIndex ,searchFindgood,dealcodeList,linkmanList,phoneList,supList);
    }

    //编辑页面
    public static void update(Long id) throws SQLException {
        Findgood findgood = Findgood.findById(id);
        RootParamNode rootParamNode = ParamNode.convert(params.all());
        findgood.edit(rootParamNode,"findgood");
        validation.valid(findgood);
        if(validation.hasErrors()) {
            edit(id);
        } else{
            findgood.save(); // 强制保存
        }
        pageIndex(null, 0);
    }

    //编辑页面
    public static void edit(Long findgoodId) {
        if(findgoodId==null){
            return;
        }
        Findgood findgood = Findgood.findById(findgoodId);
        //获取受理（进展）状态
        List dealcodeList = FindgoodFormat.dealcodeType("deal");
        render(findgood ,dealcodeList);
    }
}
