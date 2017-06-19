package controllers.admin;

import controllers.Application;
import models.admin.News;
import models.searchModel.SearchNews;
import models.templates.NewsFormat;
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
import java.util.List;

/**
 * 资讯列表Controller
 */
@With(Application.class)
public class NewsController extends Controller {

    private static int pageSize = Integer.valueOf(Messages.get("PAGE_PAGESIZE"));

    //首页
    public static void pageIndex(SearchNews searchNews , int pageIndex) throws SQLException {
        pageIndex = pageIndex == 0 ?  1 : pageIndex;//当前页
        PageBean pageBean = new PageBean(pageIndex , pageSize ,1);
        pageBean.setTableName("rec_news");
        pageBean.setShowColumn("id , title , newscode , author , statuscode , to_char(createtime ,'yyyy-mm-dd HH24:mi:ss') createtime , to_char(updatetime ,'yyyy-mm-dd HH24:mi:ss') updatetime , memo ");
        pageBean.setOrderBy("createtime");
        //根据条件查询
        StringBuilder builderSQL = new StringBuilder();
        if(searchNews != null){
            builderSQL.append(" 1 = 1 ");
            //标题
            if(StringHelper.isTrimNotNull(searchNews.getTitle())){
                builderSQL.append("and  title = '"+ searchNews.getTitle() + "'");
            }
            //分类
            if(StringHelper.isTrimNotNull(searchNews.getNewscode())){
                builderSQL.append("and  newscode = '"+ searchNews.getNewscode() + "'");
            }
            //状态
            if(StringHelper.isTrimNotNull(searchNews.getStatuscode())){
                builderSQL.append("and  statuscode = '"+ searchNews.getStatuscode() + "'");
            }
            //作者
            if(StringHelper.isTrimNotNull(searchNews.getAuthor())){
                builderSQL.append("and  author = '"+ searchNews.getAuthor() + "'");
            }
            //开始时间
            if(StringHelper.isTrimNotNull(searchNews.getCreatetime())){
                builderSQL.append("and  to_char(createtime , 'yyyy-mm-dd') =  '"+ searchNews.getCreatetime() + "'");
            }
            //结束时间
            if(StringHelper.isTrimNotNull(searchNews.getUpdatetime())){
                builderSQL.append("and  to_char(updatetime , 'yyyy-mm-dd') = '"+ searchNews.getUpdatetime() + "'");
            }
            pageBean.setCondition(builderSQL.toString());
        }

        //获取资讯分类
        List newscodeList = NewsFormat.registerType("news");
        //获取有效状态
        List statusList = NewsFormat.registerType("status");
        //获取作者
        List authorList = NewsFormat.authorList();

        pageBean =  PageUtil.pagination(pageBean);
        render(pageBean ,pageIndex ,searchNews ,newscodeList ,statusList,authorList);
    }

    //跳转到新增页面
    public static void add(){
        List newscodeList = NewsFormat.registerType("news"); //获得所有资讯类型
        render(newscodeList);
    }

    //新增 资讯管理
    public static void save(News news,File fileText) throws SQLException {
        news.createtime = new Date();
        if(fileText != null)
        {
            news.titleimage = Messages.get("HELP_UPLOADURL_NEWS") + StringHelper.getStringByNow() +"/"+ fileText.getName();
            File file =new File(Messages.get("HELP_UPLOADURL_NEWS_CONTENT") + StringHelper.getStringByNow());
            //如果文件夹不存在则创建
            if  (!file.exists()  && !file.isDirectory())
            {
                file.mkdirs();
            }
            Files.copy(fileText, new File(file +"/"+ fileText.getName()));
        }
        //操作人ID
        news.operationid = Integer.valueOf(session.get("userId"));
        //操作IP地址
        news.operationip = StringHelper.getRequestIpAddr(request);
        //001=有效  002=无效
        news.statuscode = Messages.get("STATUSCODE_VALID");
        //内容
        news.content = (news.content.replaceAll("src=\"/home/backManage", "src=\"" +"http://"+ request.host+"/home/backManage"));
        news.save();
        pageIndex(null ,0);
    }

    //编辑页面
    public static void update(Long id,File fileText) throws SQLException {
        News news = News.findById(id);
        if(fileText != null) {
            news.titleimage = Messages.get("HELP_UPLOADURL_NEWS") + StringHelper.getStringByNow() +"/"+ fileText.getName();
            //news.titleimage = "E:\\" +fileText.getName();//本地
            //File file =new File("E:\\"+ StringHelper.getStringByNow());
            File file =new File(Messages.get("HELP_UPLOADURL_NEWS_CONTENT") + StringHelper.getStringByNow());
            //如果文件夹不存在则创建
            if  (!file .exists()  && !file .isDirectory())
            {
                file.mkdirs();
            }
            Files.copy(fileText, new File(file +"/"+ fileText.getName()));
        }
        news.updatetime = new Date();
        news.content = (news.content.replaceAll("src=\"/backManage", "src=\"" +"http://"+ request.host+"/home/backManage"));
        RootParamNode rootParamNode = ParamNode.convert(params.all());
        news.edit(rootParamNode,"news");
        validation.valid(news);
        if(validation.hasErrors()) {
            edit(id);
        } else{
            news.save(); // 强制保存
        }
        pageIndex(null, 0);
    }

    //编辑页面
    public static void edit(Long newsId){
        if(newsId==null){
            return;
        }
        News news = News.findById(newsId);
        String name = session.get("name");
        List newscodeList = NewsFormat.registerType("news"); //获得所有类型
        List statusTypes = NewsFormat.registerType("status"); //获得所有有效状态(code="status")
        render(news ,name ,newscodeList , statusTypes);
    }

    //显示
    public static void enabled(Long newsId) throws SQLException {
        if(newsId==null){
            return;
        }
        News news = News.findById(newsId);
        news.statuscode = Messages.get("STATUSCODE_VALID");   //设置为 显示状态
        news.save();
        pageIndex(null , 0);
    }

    //隐藏
    public static void disable(Long newsId) throws SQLException {
        if(newsId==null){
            return;
        }
        News news = News.findById(newsId);
        news.statuscode = Messages.get("STATUSCODE_INVALID");  //设置为隐藏状态
        news.save();
        pageIndex(null, 0);

    }

    //标题超链接
    public static void detail(Long id){
        //获取所有资讯列表
        News news = News.findById(id);
        //获取资讯分类
        List newscodeList = NewsFormat.registerType("news");
        //获取有效状态
        List statusList = NewsFormat.registerType("status");

        render(news,newscodeList,statusList);
    }
}
