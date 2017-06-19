package controllers.admin;

import controllers.Application;
import models.utils.PageBean;
import models.utils.PageUtil;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import java.sql.SQLException;

/**
 * 后台账户Controller
 */
@With(Application.class)
public class AccountController extends Controller {

    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //首页
    public static void pageIndex(int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ?  1 : pageIndex;//当前页
        PageBean pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("account");
        pageBean.setShowColumn("*");
        pageBean =  PageUtil.pagination(pageBean);
        render(pageBean ,pageIndex);
    }
}
