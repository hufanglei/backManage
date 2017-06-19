package models.admin;

import models.utils.PageBean;
import models.utils.StringHelper;
import oracle.jdbc.OracleTypes;
import org.hibernate.engine.SessionImplementor;
import play.db.jpa.JPA;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 测试表
 */
@Entity
@Table(name = "test")
public class Test extends Model {
    /**
     * 姓名
     */
    @Column(name = "name")
    public String name;

    /**
     * 公司
     */
    @Column(name = "company")
    public String company;


    @Column(name = "createTime")
    public Date createTime;

    /**
     * 图片路径
     */
    @Column(name = "imgurl")
    public String imgurl;




}
