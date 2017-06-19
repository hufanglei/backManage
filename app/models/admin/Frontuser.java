package models.admin;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 *前台用户表
 */
@Entity
@Table(name="rec_frontuser")
public class Frontuser extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rec_frontuser")
    @SequenceGenerator(initialValue = 1, name = "rec_frontuser", schema= "RJDS",sequenceName = "SEQ_REC_FRONTUSER" ,allocationSize = 1)
    @Column(name = "ID")
    public long id;


    @Column(name = "memo")
    public String memo;     //备注

    @Column(name = "statuscode")
    public String statuscode;  //001=有效 002=无效

    @Column(name = "createtime")
    public Date createtime;    //创建时间

    @Column(name = "operationip")
    public String operationip;  //操作IP地址

    @Column(name = "operationid")
    public Integer operationid;  // 后台审核人员

    @Column(name = "agentid")
    public Integer agentid;       //专职经纪人ID

    @Column(name = "affirmregcode")
    public String affirmregcode;   //三证审核 001=待审核 002=审核中 003=审核通过 004=审核失败

    @Column(name = "address")
    public String address;         //公司地址

    @Column(name = "email")
    public String email;           // email

    @Column(name = "mobphone")
    public String mobphone;        //联系手机

    @Column(name = "phone")
    public String phone;           //固定电话

    @Column(name = "codeno")
    public String codeno;          //工号

    @Column(name = "name")
    public String name;            //姓名

    @Column(name = "company")
    public String companyName;         // 公司名称

    @Column(name = "unitno")
    public String unitno;          //应该是固定10位，尚不确定

    @Column(name = "guestcode")
    public String guestcode;       //例如：001=买家 002=卖家

    @Column(name = "password")
    public String password;        //密码

    @Column(name = "login")
    public String login;           // 登录名

    //外键关系的列
    @ManyToOne
    @JoinColumn(name="companyid" , nullable = true)
    public Company comp;      // 前台用户公司ID

    @ManyToOne
    @JoinColumn(name = "backuserid" , nullable = true)
    public Account backUser;     //每个后台用户负责若干前台用户

    @Column(name = "usertypecode")
    public String usertypecode;   // 用户类型：公司用户or个人用户

    //获取所有用户的登录名和姓名
    public static List getAllLoginAndNames(){
        Query query = Model.em().createNativeQuery("SELECT LOGIN,name FROM REC_FRONTUSER ORDER BY CREATETIME DESC");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return query.getResultList();

    }


}
