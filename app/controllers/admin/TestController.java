package controllers.admin;

import models.admin.Test;
import models.utils.Page;
import models.utils.PageBean;
import models.utils.PageUtil;
import models.utils.StringHelper;
import oracle.jdbc.OracleTypes;
import org.hibernate.SQLQuery;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.transform.Transformers;
import play.Logger;
import play.Play;
import play.data.binding.ParamNode;
import play.data.binding.RootParamNode;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.i18n.Messages;
import play.libs.Files;
import play.mvc.Controller;

import javax.persistence.Query;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;

/**
 *测试 Controller
 */
public class TestController extends Controller {
    private static int pageSize = 6;
    private static GenericModel.JPAQuery jpaQuery;

    //遍历
    public static void list(int curPage ,Test searchTest,String createTime){
        // Map<String, String[]> parms = params.all();
        // Logger.info("================== 参数为%s" , new Gson().toJson(parms));
        Logger.info("time==update===== %s", createTime);
        curPage = curPage == 0 ?  1 : curPage;   //当前页
        int beginIndex = (curPage-1)*pageSize;
        jpaQuery = Test.find("order by id asc");

        if(searchTest.name!=null&&!"".equals(searchTest.name)){
            jpaQuery = Test.find("name = ? and to_char(createTime , 'yyyy-mm-dd')  = ?", searchTest.name.trim(),createTime);
            Logger.info("我进入了查询 name 的这个方法==数据个数=%s", jpaQuery.fetch().size());
        }
        if(searchTest.company!=null&&!"".equals(searchTest.company)){
            jpaQuery = Test.find("company = ?", searchTest.company.trim());
            Logger.info("我进入了查询  company  的这个方法==数据个数=%s", jpaQuery.fetch().size());
        }
        if(createTime!=null && !"".equals(createTime)){
            jpaQuery = Test.find("to_char(createTime , 'yyyy-mm-dd')  = ?", createTime);
            Logger.info("我进入了查询  createTime  的这个方法=========数据个数:===%s", jpaQuery.fetch().size());
        }

        int count =  jpaQuery.fetch().size();
        List<Test> testList = jpaQuery.from(beginIndex).fetch(pageSize);
        Page page = new Page(curPage,pageSize ,count);
        render(testList, searchTest ,page, curPage,createTime);
    }

    //分页(利用存储过程)查询
    public static void pageIndex(PageBean pageBean) throws SQLException {
        int pageNow = params.get("pageIndex") == null ? 1 : Integer.valueOf(params.get("pageIndex"));//当前页
       // String condition = "name = '啊啊啊啊啊' and company = '23完全围绕' and id = 40";   //查询条件
        pageBean = new PageBean(pageNow , pageSize , "test" ,"*" ,"","",1);
        pageBean =  PageUtil.pagination(pageBean);
        render(pageBean);
    }


    //跳到新增
    public static void add(Test test){
      render();
    }

    //测试原生sql查询
    public static void query(){
        //Query query = JPA.em().createNativeQuery("select * from test");
        Query query = Model.em().createNativeQuery("select * from test");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List tests = query.getResultList();
        //JPA.em().getDelegate().createSQLQuery("xxxxxx")这种可以成功执行外，其余几种都会出现不同的异常。
        render(tests);
    }


    //测试原生sql单个结果返回
    public static void querySinggle(Long id){
        Query query = Model.em().createNativeQuery("select * from test where id=?");
        query.setParameter(1,id);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object object = query.getResultList().get(0);
        //另一种方式=推荐
//        Object object = query.getSingleResult();
        System.out.println(object.toString());
        render(object);
    }

    public static  void json(){
        render();
    }

    //原生sql+json+测试数据返回
    public  static  void jsonTest(Long id){
        //Query query = Model.em().createNativeQuery("select * from test where id=?");
        Query query = Model.em().createNativeQuery("select m.finalmoney, m.oid from(select s.id sid , s.finalmoney  ,o.id oid from rec_Stock s , rec_order_stock os ,rec_order o  \n" +
                "where s.id = os.stockid and o.id = os.orderid ) m  where m.oid = ?");
        query.setParameter(1,id);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object object = query.getResultList().get(0);
        renderJSON(object);
    }




    //保存
    public static void save(Test test){
        test.createTime = new Date();
        Logger.info("time===add==== %s", test.createTime);
        test.save();
        list(0 ,null,null);
    }

    //进入更新页面
    public static  void edit(Long id){
       Test test = Test.findById(id);
        render(test);
    }

    public static  void update(Long id ,File fileText){
        Test test = Test.findById(id);
        Files.copy(fileText, Play.getFile("public/images/" + fileText.getName()));
        test.imgurl  =  Messages.get("WORD_PREVIEW_URL") + "/public/images/" + fileText.getName();
        test.save();
        RootParamNode rootParamNode = ParamNode.convert(params.all());
        test.edit(rootParamNode,"test");
        validation.valid(test);
        if(validation.hasErrors()) {
            System.out.println("==========11111");
            edit(id);
        } else{
            System.out.println("==========2222222");
            test.save(); // 强制保存
        }
        list(0 , null , null);
    }

    public static  void delete(Long testId){
        Test test =  Test.findById(testId);
        if (test != null) {
            //Test.deletedUser(user);
            test.delete();
        } else {
            Logger.info("用户为空，不能删除");
        }
        list(0 ,null , null);
    }

    //跳转到文件上传页面
    public static  void file(){
        String osName = System.getProperty("os.name");//操作系统名字
        String osLevel = System.getProperty("sun.os.patch.level");//操作系统版本
        int aa = Window.PROPERTIES;
        String host = request.remoteAddress;
        String host2 = request.remoteAddress;
        Object agent = request.headers.get("user-agent");
        System.out.println("osName============"+osName);
        System.out.println("osLevel============"+osLevel);
        System.out.println("host============"+host);
        System.out.println("host2============"+host2);
        System.out.println("user-agent============"+agent);

        render(osName,osLevel,aa,agent);
    }

    //跳转到文件上传
    public static void uploadFile(File imgFile) throws IOException {
//        Files.copy(imgFile, Play.getFile("public/images/" + imgFile.getName()));
        File file = new File("F:\\");
        if(!file.exists()){
            file.mkdirs();
        }
        Files.copy(imgFile, new File(file+imgFile.getName()));

        renderJSON("111111111111111111");

    }

    public static void addTest(Test test) {

        test.save();
        renderText("图片上传成功!");
    }


    //测试  分页存储过程 显示
    public static void procedureTest() throws SQLException {
        Connection connection= JPA.em().unwrap(SessionImplementor.class).connection();
        CallableStatement call = null;
        ResultSet rs=null;
        int  myrows=0;
        int  myPageCount=0;
        List testList=null;
        try {
            call = connection.prepareCall("CALL sp_com_page(?,?,?,?,?,?,?,?,?,?)");
            call.setString(1," test ");
            call.setString(2," * ");
            call.setString(3,"");
            call.setInt(4,10);
            call.setInt(5,1);
            call.setString(6,"id");
            call.setInt(7,1);
            call.registerOutParameter(8, OracleTypes.INTEGER);
            call.registerOutParameter(9, OracleTypes.INTEGER);
            call.registerOutParameter(10, OracleTypes.CURSOR);
            call.execute();
            myrows = call.getInt(8);
            myPageCount = call.getInt(9);
            rs= (ResultSet) call.getObject(10);
            testList = StringHelper.resultSetToList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (rs!=null) {
                rs.close();
            }
            if(call!=null){
                call.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
        render(myrows , myPageCount , testList);
    }



    //测试 jdbc方式 分页存储过程 的调用
    public static void procedureTest2() throws ClassNotFoundException, SQLException {
        Connection ct = null;
        // 1.加载驱动
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //2.得到链接
        ct = DriverManager.getConnection("dbc:oracle:thin:@192.168.0.37:1521:juhuawang", "rjds", "Rjdsjhw001");
        //3.创建CallableStatement
        CallableStatement cs=ct.prepareCall("{call sp_com_page(?,?,?,?,?,?,?,?,?,?)}");
        //4.赋值
        cs.setString(1, "test");  //表名 或者是多表连接查询部分  不可为空（必填）
        cs.setString(2, "*");   //--查询结果显示字段 可以为* 不可为空（必填）
        cs.setString(3, "");    //  --分页查询条件 可以为空
        cs.setInt(4, 5);       //  --一页显示记录数 不可为空（必填）
        cs.setInt(5, 1);       //  --当前页数 不可为空（必填）
        cs.setString(6, "");    //--order by 后排序字段，为空表示不排序  可以多个,号分割
        cs.setInt(7, 1);      //--排序标识 0：正序 1：倒序 可以为空
        cs.registerOutParameter(8, OracleTypes.INTEGER);      //--总记录数  输出参数
        cs.registerOutParameter(9, OracleTypes.INTEGER);       //--总分页数  输出参数
        cs.registerOutParameter(10, OracleTypes.CURSOR);      //--返回的程序包自定义的游标结果记录集
        cs.execute();

        int  myrows = cs.getInt(8);
        int  myPageCount = cs.getInt(9);

        ResultSet rs = (ResultSet)cs.getObject(10);
        List testList = new ArrayList();
        while(rs.next()){
            System.out.println(rs.getString("name") + "===" + rs.getDate("createtime"));
            Map testMap = new HashMap();
            testMap.put("id", rs.getInt("id"));
            testMap.put("name", rs.getString("name"));
            testMap.put("company", rs.getString("company"));
            testMap.put("createtime" ,rs.getDate("createtime"));
            testMap.put("name", rs.getString("name"));
            testMap.put("company", rs.getString("company"));
            testList.add(testMap);
        }
        rs.close();
        cs.close();
        ct.close();
        render(myrows , myPageCount , testList);
    }



    //测试 带参数(输入参数 + 输出参数 )的 存储过程 的调用
    public static void procedureTest1() throws ClassNotFoundException, SQLException {
        Connection ct = null;
        // 1.加载驱动
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //2.得到链接
        ct = DriverManager.getConnection("dbc:oracle:thin:@192.168.0.37:1521:juhuawang", "rjds", "Rjdsjhw001");
        //3.创建CallableStatement
        CallableStatement cs=ct.prepareCall("{call p_test(?,?)}");
        //4.赋值
        cs.setLong(1, 23);
        cs.registerOutParameter(2, OracleTypes.CURSOR);
        cs.execute();

        ResultSet rs=(ResultSet)cs.getObject(2);
        Map map = new HashMap();
        while(rs.next()){
            System.out.println(rs.getString("name") + "==="+ rs.getString("company"));
            map.put("name", rs.getString("name"));
            map.put("company" ,rs.getString("company"));
            map.put("createtime" ,rs.getString("createtime"));
        }
        rs.close();
        cs.close();
        ct.close();
        render(map);
    }

    public static void getIp(){
        StringHelper.getRequestIpAddr(request);
        renderText("111111111111111111");
    }



}
