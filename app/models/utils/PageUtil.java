package models.utils;

import oracle.jdbc.OracleTypes;
import org.hibernate.engine.SessionImplementor;
import play.db.jpa.JPA;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by hfl on 2016-6-12.
 */
public class PageUtil {

    //分页显示
    public static PageBean pagination(PageBean pageBean) throws SQLException {
        Connection connection= JPA.em().unwrap(SessionImplementor.class).connection();
        CallableStatement call = null;
        ResultSet rs=null;
        List modelList = null;
        try {
            call = connection.prepareCall("CALL sp_com_page(?,?,?,?,?,?,?,?,?,?)");
            //4.赋值
            call.setString(1, pageBean.getTableName());  //表名 或者是多表连接查询部分  不可为空（必填）
            call.setString(2, pageBean.getShowColumn());   //--查询结果显示字段 可以为* 不可为空（必填）
            call.setString(3, pageBean.getCondition());    //  --分页查询条件 可以为空
            call.setInt(4, pageBean.getPageSize());       //  --一每页显示记录数 不可为空（必填）
            call.setInt(5, pageBean.getPageNow());       //  --当前页数 不可为空（必填）
            call.setString(6, pageBean.getOrderBy());    //--order by 后排序字段，为空表示不排序  可以多个,号分割
            call.setInt(7, pageBean.getFlag());      //--排序标识 0：正序 1：倒序 可以为空
            call.registerOutParameter(8, OracleTypes.INTEGER);      //--总记录数  输出参数
            call.registerOutParameter(9, OracleTypes.INTEGER);       //--总分页数  输出参数
            call.registerOutParameter(10, OracleTypes.CURSOR);      //--返回的程序包自定义的游标结果记录集
            call.execute();
            pageBean.setTotalcount(call.getInt(8));
            pageBean.setTotalPage(call.getInt(9));
            rs= (ResultSet) call.getObject(10);
            modelList = StringHelper.resultSetToList(rs);
            pageBean.setModelList(modelList);
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
        return pageBean;
    }

}
