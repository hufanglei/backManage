package controllers.admin;

import controllers.Application;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import java.io.*;


/**
 * Created by Administrator on 2016/7/5.
 */
@With(Application.class)
public class AttachedController extends Controller{

    public void attached( String fileType,
                          String uploadDate,
                          String suffix,
                          String fileName) {
        //根据suffix设置响应ContentType
        //response.setContentType("text/html; charset=UTF-8");

        InputStream is = null;
        OutputStream os = null;
        try {
            // File file = new File("d:/attached/" + fileType + "/" + uploadDate + "/" + fileName + "." + suffix);
            File file = new File(Messages.get("HELP_UPLOADURL_NEWS_CONTENT")+ fileType + "/" + uploadDate + "/" + fileName + "." + suffix);
            is = new FileInputStream(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            //os = new BufferedOutputStream();
            os.write(buffer);
            os.flush();
        } catch (Exception e) {
            //判断suffix
            //图片请求可以在此显示一个默认图片
            //file显示文件已损坏等错误提示...
            renderJSON("读取文件失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    renderJSON("读取文件失败");
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        renderJSON("读取文件失败");
                    }
                }
            }
        }
    }
}
