package controllers.admin;

import com.alibaba.fastjson.JSONObject;
import controllers.Application;
import models.utils.StringHelper;
import play.i18n.Messages;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.With;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;


/**
 * 资讯内容图片上传
 */
@With(Application.class)
public class KindEditorController extends Controller {

    public static void uploadFile(File imgFile) {

        //文件保存目录路径
//        String savePath =  Play.applicationPath.toString()+Play.configuration.getProperty("newsImg.savePath", "false");
        String savePath = Messages.get("HELP_UPLOADURL_NEWS_CONTENT");
        //文件保存目录URL
//       String saveUrl  =  Play.configuration.getProperty("newsImg.savePath", "false");
        String saveUrl  =  Messages.get("HELP_UPLOADURL_NEWS");

        //定义允许上传的文件扩展名
        String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp","swf","flv","mp3","wav","wma","wmv","mid","avi","mpg","asf","rm","rmvb","doc","docx","xls","xlsx","ppt","htm","html","xml","sql","txt","zip","rar","gz","bz2","pdf" };

        //最大文件大小
        long maxSize = 1000000;
        if (imgFile != null) {
            //检查目录
            File uploadDir = new File(savePath);
            if(!uploadDir.isDirectory()){
                renderJSON("上传目录不存在。");
                return;
            }

            //检查目录写权限
            if(!uploadDir.canWrite()){
                renderJSON("上传目录没有写权限。");
                return;
            }

            //创建文件夹
            savePath += StringHelper.getStringByNow() + "/";
            saveUrl += StringHelper.getStringByNow() + "/";
            File dirFile = new File(savePath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            //检查文件大小
            if(imgFile.length() > maxSize){
                renderJSON("上传文件大小超过限制。");
                return;
            }

            //检查扩展名
            String fileExt = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1).toLowerCase();
            if(!Arrays.<String>asList(fileTypes).contains(fileExt)){
                renderJSON("上传文件扩展名是不允许的扩展名。");
                return;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
            Files.copy(imgFile,new File(savePath ,newFileName));
            try {
                JSONObject obj = new JSONObject();
                obj.put("error", 0);
                obj.put("url", saveUrl + newFileName);
                renderText(obj.toJSONString().toString());
                return;

            } catch (Exception e) {
                e.printStackTrace();
                renderJSON("上传失败");
                return;
            }
        }else{
            renderJSON("请选择文件。");
            return;
        }
    }
}