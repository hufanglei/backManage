package models.admin;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import play.i18n.Messages;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 前台公司表
 */
@Entity
@Table(name="rec_company")
public class Company extends GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rec_company")
    @SequenceGenerator(initialValue = 1, name = "rec_company", schema= "RJDS",sequenceName = "SEQ_REC_COMPANY" ,allocationSize = 1)
    @Column(name = "ID")
    public long id;

    @Column(name = "unitno")
    public String unitno;    //组织机构代码

    @Column(name = "licensecode")
    public String licensecode;   // 营业执照代码

    @Column(name = "taxno")
    public String taxno;            //纳税人识别号

    @Column(name = "name")
    public String name;          //公司名称

    @Column(name = "linkman")
    public String linkman;       //企业法人

    @Column(name = "phone")
    public String phone;          //固定电话

    @Column(name = "mobphone")
    public String mobphone;       //联系手机

    @Column(name = "email")
    public String email;          //email

    @Column(name = "address")
    public String address;        //公司地址

    @Column(name = "affirmregcode")
    public String affirmregcode;    // 001=待审核 002=审核中 003=审核通过 004=审核失败

    @Column(name = "backuserid")
    public Integer backuserid;      //每个后台用户负责若干前台用户

    @Column(name = "agentid")
    public Integer agentid;         //专职经纪人ID

    @Column(name = "operationid")
    public Integer operationid;      //操作人ID

    @Column(name = "operationip")
    public String operationip;       //操作IP地址

    @Column(name = "createtime")
    public Date createtime;          //创建时间

    @Column(name = "statuscode")
    public String statuscode;       //001=有效 002=无效

    @Column(name = "memo")
    public String memo;             //备注

    @Column(name = "certnum")
    public String certnum;             //证件号码(+)

    @Column(name = "certtypecode")
    public String certtypecode;             //证件类型   1=身份证

    //获取所有的公司名
    public static List getAllNames(){
        Query query = Model.em().createNativeQuery("SELECT DISTINCT name FROM REC_COMPANY");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return query.getResultList();

    }

    //获取所有的卖方公司名
    public static List getAllSellerNames(){
        Query query = Model.em().createNativeQuery("SELECT DISTINCT RC.name FROM REC_COMPANY rc,REC_FRONTUSER rf WHERE RC.id=RF.COMPANYID AND RF.GUESTCODE=?");
        query.setParameter(1, Messages.get("GUESTCODE_SELLER"));
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return query.getResultList();

    }
}
