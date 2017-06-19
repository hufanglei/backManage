package controllers;

import models.admin.Account;
import models.utils.StringHelper;
import play.mvc.Before;
import play.mvc.Controller;

import java.security.NoSuchAlgorithmException;

public class Application extends Controller {


    @Before(unless = {"toLogin", "login", "index", "toRegist", "reg"})
    static void checkLogin() {
        String userId = session.get("userId");
        if (userId == null || userId.equals("")) {
            toLogin();
        }
    }

    @Before
    static void checkAuth(){
//        String actionMethod = request.actionMethod;
//        System.out.println("actionMethod=="+actionMethod);
//        String path = request.path;
//        System.out.println("path=="+path);
//        String querystring = request.querystring;
//        System.out.println("querystring=="+querystring);
//        String domain = request.domain;
//        System.out.println("domain=="+domain);
//        String remoteAddress = request.remoteAddress;
//        System.out.println("remoteAddress=="+remoteAddress);
//        String encoding = request.encoding;
//        System.out.println("encoding=="+encoding);
//        Integer port = request.port;
//        System.out.println("port=="+port);
//        Boolean secure = request.secure;
//        System.out.println("secure=="+secure);
//        String action = request.action;
//        System.out.println("action=="+action);

        String path = request.path;

        System.out.println(2);
        System.out.println(1);
    }

    public static void toLogin() {
        render();
    }

    public static void index() {
        render();
    }

    /**
     * 注册页面
     */
    public static void toRegist() {
        render();
    }


    /**
     * 注册
     */
    public static void reg(Account account) throws NoSuchAlgorithmException {
        validation.required(account.username).message("用户名不能为空");
        validation.required(account.password).message("用户密码不能为空");
        validation.required(account.name).message("姓名不能为空");
        validation.required(account.email).message("邮箱不能为空");
        if (validation.hasErrors()) {
            params.flash();
            validation.keep();
            toRegist();
        } else {
            Account.addAccount(account);
            toLogin();
        }
    }

    /**
     * 进入主页+用户登录
     */
    public static void login(Account account) throws NoSuchAlgorithmException {
        validation.required(account.username).message("用户名不能为空");
        validation.required(account.password).message("用户密码不能为空");
        Account user = null;
        int account_new = 0;
        if (validation.hasErrors()) {
            params.flash();
            validation.keep();
            toLogin();
        } else {
            user = Account.login(account.username, StringHelper.md5(account.password));
            validation.required(user);
            if (validation.hasErrors()) {
                params.flash();
                flash.put("message", "用户名或密码错误");
                toLogin();
            } else {
                //获取当前登录用户的角色
                /*user = Account.getByUserNameAndPwd(account.username, StringHelper.md5(account.password));

                AccountAuth accountAuth = new AccountAuth(user.id, user.name, user.username);
                AccountRole accountRole = new AccountRole(user.role.id, user.role.name);
                List<AuthorityMenu> authorityMenus = new ArrayList<AuthorityMenu>();
                //获取当前用户的所有权限
//                List<Authority> roleAuthorities = account.getRole().getAuthorities();
//                List<HashMap> ids = Authority.getListByRoleId(String.valueOf(user.role.id));

                List<Authority> roleAuthorities = user.role.authorityList;
                Comparator comp = new ComparatorImpl();//自定义的按照对象值排序

                for (Authority authority : roleAuthorities) {
                    //顶级菜单
                    if (authority.parentid == null) {
                        AuthorityMenu authorityMenu = new AuthorityMenu(authority.parentid, authority.name,
                                authority.itemicon, authority.url,authority.position);
                        //子菜单集合
                        List<AuthorityMenu> childrenAuthorityMenus = new ArrayList<AuthorityMenu>();
                        for (Authority subAuthority : roleAuthorities) {
                            if (subAuthority.parentid != null && subAuthority.parentid==(authority.id))
                                childrenAuthorityMenus.add(new AuthorityMenu(
                                        subAuthority.getId(), subAuthority.name, subAuthority.itemicon,
                                        subAuthority.url,subAuthority.position));
                        }
                        Collections.sort(childrenAuthorityMenus, comp);//根据位置排序子菜单
                        authorityMenu.setChildrens(childrenAuthorityMenus);
                        authorityMenus.add(authorityMenu);
                    }

                }
                Collections.sort(authorityMenus, comp);//根据位置排序主菜单
                List<PermissionMenu> permissionMenus = new ArrayList<PermissionMenu>();
                for (Authority authority : roleAuthorities) {
                    List<Authority> parentAuthorities = new ArrayList<Authority>();
                    Authority tempAuthority = authority;
                    while (tempAuthority.parentid != null) {
                        //所有中间的二级父类
                        parentAuthorities.add((Authority)Authority.findById(tempAuthority.parentid));
                        tempAuthority = Authority.findById(tempAuthority.parentid);
                    }
                    if (parentAuthorities.size() >= 2)
                        permissionMenus.add(new PermissionMenu(parentAuthorities
                                .get(parentAuthorities.size() - 1).id,
                                parentAuthorities.get(parentAuthorities.size() - 1).name, parentAuthorities.get(
                                parentAuthorities.size() - 2).id,
                                parentAuthorities.get(parentAuthorities.size() - 2).name, authority.name, authority.matchurl));
                    else if (parentAuthorities.size() == 1)
                        permissionMenus.add(new PermissionMenu(parentAuthorities.get(0).id,
                                parentAuthorities.get(0).name, authority.id, authority.name, authority.name, authority.matchurl));
                    else
                        permissionMenus.add(new PermissionMenu(authority.id, authority.name, null, null, authority.name, authority.matchurl));
                }
                accountRole.setAuthorityMenus(authorityMenus);
                accountRole.setPermissionMenus(permissionMenus);
                accountAuth.setAccountRole(accountRole);*/
                session.put("account",account);

//              Cache.set("account_" + user.id, user, "30mn");
                session.put("userId", user.id);
                session.put("name", user.name);
                session.put("userName", user.username);

                /*List<AuthorityMenu> authList =  accountAuth.getAccountRole().getAuthorityMenus();
                List<PermissionMenu> permissionMenuList =  accountAuth.getAccountRole().getPermissionMenus();
                Cache.add("authList", authList);
                Cache.add("permissionMenuList", permissionMenuList);*/

                index();
            }
        }
    }


    /**
     * 注销
     */
    public static void cancel() {
        session.clear();
        toLogin();
    }
}