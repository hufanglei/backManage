package controllers.admin;

import controllers.Application;
import models.admin.Authority;
import models.admin.Role;
import models.admin.RoleAuthority;
import models.templates.RoleFormat;
import models.utils.PageBean;
import models.utils.PageUtil;
import play.Logger;
import play.data.binding.ParamNode;
import play.data.binding.RootParamNode;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;
import java.sql.SQLException;
import java.util.List;

/**
 * 角色管理Controller
 */
@With(Application.class)
public class RoleController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //首页
    public static void pageIndex(int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ?  1 : pageIndex;//当前页
        PageBean pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("role");
        pageBean.setShowColumn("*");
        pageBean =  PageUtil.pagination(pageBean);
        render(pageBean ,pageIndex);
    }

    //跳转到新增页面
    public static void add(){
        render();
    }

    //新增 角色管理
    public static void save(Role role) throws SQLException {
        //版本
        role.version = Long.valueOf(Messages.get("ACCOUNT_VERSION"));
        //有效状态
        role.enable = Integer.valueOf(Messages.get("ACCOUNT_ENABLE"));
        role.save();
        pageIndex(0);
    }

    //编辑页面
    public static void update(Long id) throws SQLException {
        Role role = Role.findById(id);
        RootParamNode rootParamNode = ParamNode.convert(params.all());
        role.edit(rootParamNode,"role");
        validation.valid(role);
        if(validation.hasErrors()) {
            edit(id);
        } else{
            role.save(); // 强制保存
        }
        pageIndex(0);
    }

    //编辑页面
    public static void edit(Long roleId){
        if(roleId==null){
            return;
        }
        Role role = Role.findById(roleId);
        render(role);
    }

    //启用
    public static void enabled(Long roleId) throws SQLException {
        if(roleId==null){
            return;
        }
        Role role = Role.findById(roleId);
        role.enable = Integer.valueOf(Messages.get("ACCOUNT_ENABLE"));   //设置为启用状态
        role.save();
        pageIndex(0);
    }

    //禁用
    public static void disable(Long roleId) throws SQLException {
        if(roleId==null){
            return;
        }
        Role role = Role.findById(roleId);
        role.enable = Integer.valueOf(Messages.get("ACCOUNT_DISENABLE"));  //设置为禁用状态
        role.save();
        pageIndex(0);
    }

    //删除
    public static void delete(Long roleId) throws SQLException {
        Role role =  Role.findById(roleId);
        if (role != null) {
            role.delete();
        } else {
            Logger.info("用户为空，不能删除");
        }
        pageIndex(0);
    }

    //绑定权限
    public static void bind(Long roleId){
        if(roleId==null){
            return;
        }
        Role role = Role.findById(roleId);
        List authorityList = RoleFormat.roleList(roleId);
        render(role,authorityList);
    }

    //绑定权限提交
    public static void bindcom() throws SQLException {
        //从页面获取值
        String authid = params.get("auth");
        String roleid = params.get("role");

        String[] strArray = null;
        strArray = authid.split(",");
        for(int i = 0 ;i < strArray.length;i ++){
            //new对象
            RoleAuthority roAuth = new RoleAuthority();

            //权限ID
            Long z = Long.valueOf(strArray[i]);
            roAuth.authority = Authority.findById(z);
            //角色ID
            roAuth.role = Role.findById(Long.valueOf(roleid));
            //查看数据库中是否有这条数据
            List roleAuth = RoleFormat.authroList(z,Long.valueOf(roleid));
            if(!roleAuth.isEmpty()) {
                bind(Long.valueOf(roleid));
            } else {
                roAuth.save();//保存
            }
        }
        pageIndex(0);
    }
}