package models.admin;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 仓库表
 */
@Entity
@Table(name = "dic_Storage")
public class Storage extends Model {

    @Column(name = "memo")
    public String memo;        //备注

    @Column(name = "statuscode")
    public String statuscode;  //  001=有效 002=无效

    @Column(name = "createtime")
    public Date createtime;    //创建时间

    @Column(name = "operationip")
    public String operationip;  //操作IP地址

    @Column(name = "operationid")
    public Integer operationid;  //操作人ID

    @Column(name = "phone")
    public String phone;         //固话

    @Column(name = "mobphone")
    public String mobphone;      //手机

    @Column(name = "linkman")
    public String linkman;        //联系人，仓储人员不是前台用户

    @Column(name = "postal")
    public String postal;         // 邮编

    @Column(name = "address")
    public String address;        //详细地址

    @Column(name = "countycode")
    public String countycode;     //县 具体位置可能没有县

    @Column(name = "citycode")
    public String citycode;       //市

    @Column(name = "provcode")
    public String provcode;       //省

    @Column(name = "name")
    public String name;           //名称

    @Column(name = "codeno")
    public String codeno;         //编号

    //获取所有的仓库名
    public static List getAllNames(){
        Query query = Model.em().createNativeQuery("SELECT DISTINCT name FROM DIC_STORAGE");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return query.getResultList();

    }

}
