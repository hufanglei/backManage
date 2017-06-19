package controllers.admin;

import controllers.Application;
import models.admin.Phoneversion;
import models.utils.PageBean;
import models.utils.PageUtil;
import models.utils.StringHelper;
import play.data.binding.ParamNode;
import play.data.binding.RootParamNode;
import play.i18n.Messages;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.With;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

/**
 * 数据中心controller
 * User: zjw
 * DateTime: 2016/12/2 0002 13:42
 */
@With(Application.class)
public class DataCenterController extends Controller {
    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    public static void index() {
        render();
    }

    /**
     * 帮助反馈
     *
     * @param pageBean
     * @param title
     * @throws SQLException
     */
    public static void feedbackIndex(PageBean pageBean, String title) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex, pageSize, 1);
        pageBean.setTableName("(rec_feedback)");
        pageBean.setShowColumn("*");
        pageBean.setOrderBy("CREATETIME");

        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if (title != null) {
            builderSQL.append(" 1 = 1 ");
            //标题
            if (StringHelper.isTrimNotNull(title)) {
                builderSQL.append("and  title = '" + title + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }

        pageBean = PageUtil.pagination(pageBean);
        render(pageBean, pageIndex, title);
    }

    /**
     * 签到
     *
     * @param pageBean
     * @throws SQLException
     */
    public static void checkinIndex(PageBean pageBean) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex, pageSize, 1);
        pageBean.setTableName("(rec_checkin)");
        pageBean.setShowColumn("*");
        pageBean.setOrderBy("CREATETIME");


        pageBean = PageUtil.pagination(pageBean);
        render(pageBean, pageIndex);
    }


    /**
     * 手机版本管理
     *
     * @param pageBean
     * @throws SQLException
     */
    public static void phoneversionIndex(PageBean pageBean) throws SQLException {
        int pageIndex = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
        pageBean = new PageBean(pageIndex, pageSize, 1);
        pageBean.setTableName("(phoneversion)");
        pageBean.setShowColumn("*");
        pageBean.setOrderBy("CREATETIME");


        pageBean = PageUtil.pagination(pageBean);
        render(pageBean, pageIndex);
    }

    //跳转到新增页面
    public static void addPhoneVersion() {
        render();
    }

    public static void editPhoneversion(Long id) {
        if (id == null) {
            return;
        }
        Phoneversion phoneversion = Phoneversion.findById(id);
        render(phoneversion);
    }

    public static void deletePhoneversion(Long id) throws SQLException {
        if (id == null) {
            return;
        }
        Phoneversion phoneversion = Phoneversion.findById(id);
        phoneversion.delete();

        PageBean pageBean = new PageBean(1, pageSize, 1);
        phoneversionIndex(pageBean);
    }

    //新增 手机版本管理
    public static void savePhoneversion(Phoneversion phoneversion, File fileText) throws SQLException {
        phoneversion.createtime = new Date();
        phoneversion.statuscode = Messages.get("STATUSCODE_VALID");
        if (fileText != null) {
            phoneversion.path = Messages.get("HELP_UPLOADURL_PHONEVERSION") + StringHelper.getStringByNow() + "/" + fileText.getName();
            File file = new File(Messages.get("HELP_UPLOADURL_PHONEVERSION_CONTENT") + StringHelper.getStringByNow());
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            Files.copy(fileText, new File(file + "/" + fileText.getName()));
        }
        phoneversion.save();

        PageBean pageBean = new PageBean(1, pageSize, 1);
        phoneversionIndex(pageBean);
    }

    //编辑页面
    public static void updatePhoneversion(Long id, File fileText) throws SQLException {
        Phoneversion phoneversion = Phoneversion.findById(id);
        if (fileText != null) {
            phoneversion.path = Messages.get("HELP_UPLOADURL_PHONEVERSION") + StringHelper.getStringByNow() + "/" + fileText.getName();
            File file = new File(Messages.get("HELP_UPLOADURL_PHONEVERSION_CONTENT") + StringHelper.getStringByNow());
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            Files.copy(fileText, new File(file + "/" + fileText.getName()));
        }
        RootParamNode rootParamNode = ParamNode.convert(params.all());
        phoneversion.edit(rootParamNode, "phoneversion");
        validation.valid(phoneversion);
        if (validation.hasErrors()) {
            editPhoneversion(id);
        } else {
            phoneversion.save(); // 强制保存
        }
        PageBean pageBean = new PageBean(1, pageSize, 1);
        phoneversionIndex(pageBean);
    }

    //正常
    public static void enabledPhoneversion(Long id) throws SQLException {
        if (id == null) {
            return;
        }
        Phoneversion phoneversion = Phoneversion.findById(id);
        phoneversion.statuscode = Messages.get("STATUSCODE_VALID");   //设置为 显示状态
        phoneversion.save();
        PageBean pageBean = new PageBean(1, pageSize, 1);
        phoneversionIndex(pageBean);
    }

    //禁用
    public static void disablePhoneversion(Long id) throws SQLException {
        if (id == null) {
            return;
        }
        Phoneversion phoneversion = Phoneversion.findById(id);
        phoneversion.statuscode = Messages.get("STATUSCODE_INVALID");  //设置为隐藏状态
        phoneversion.save();
        PageBean pageBean = new PageBean(1, pageSize, 1);
        phoneversionIndex(pageBean);

    }

}
