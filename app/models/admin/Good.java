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
 * 商品
 */
@Entity
@Table(name="dic_Good")
public class Good extends Model {

    @Column(name = "memo")
    public String memo;             //备注

    @Column(name = "createtime")
    public Date createtime;         //创建时间

    @Column(name = "statuscode")
    public String statuscode;        //001=有效 002=无效

    @Column(name = "operationip")
    public String operationip;        //操作IP地址

    @Column(name = "frontuserid")
    public Integer frontuserid;      //供应商业务员ID 仅指前台供应商业务员，后台人员增加记录为null

    @Column(name = "convertnum")
    public Double convertnum;        //换算数量  1包装=X计量

    @Column(name = "meacode")
    public String meacode;           //计量单位  001=克，002=千克，003=吨

    @Column(name = "packagecode")
    public String packagecode;       //包装单位    001=包，002=袋

    @Column(name = "packcontent")
    public String packcontent;       //包装说明:   001=黑字，002=火炬

    @Column(name = "packtypecode")
    public String packtypecode;      //包装类型 :001=木托，002=塑托，003=散货

    @Column(name = "producer")
    public String producer;          //生产商  例如：伊朗石化

    @Column(name = "producingarea")
    public String producingarea;     //产地  例如：伊朗

    @Column(name = "rate")
    public Double rate;             //税率

    @Column(name = "goodtypecode")
    public String goodtypecode;    //商品分类code

    @Column(name = "maccode")
    public String maccode;        //加工级别code:

    @Column(name = "usecode")
    public String usecode;       // 用途级别CODE: 001=薄膜级，002=电子电器部件

    @Column(name = "goodkindcode")
    public String goodkindcode;  // 品种code : 001=PE，002=PP，003=PVC，其中001001=HDPE，001001001=薄膜

    @Column(name = "mark")
    public String mark;          //牌号

    //获取所有的商品牌号
    public static List getAllMarks(){
        Query query = Model.em().createNativeQuery("SELECT DISTINCT MARK FROM DIC_GOOD");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return query.getResultList();

    }


}
