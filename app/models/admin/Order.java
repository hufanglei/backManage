package models.admin;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 订单表
 */
@Entity
@Table(name="rec_order")
public class Order extends Model {
    @Column(name = "memo")
    public String memo;    //备注

    @Column(name = "srccode")
    public String srccode; // 购物车编号 002=单品上架单编号

    @Column(name = "createtime")
    public Date createtime;   //创建时间

    @Column(name = "operationip")
    public String operationip;  //操作IP地址

    @Column(name = "operationid")
    public Integer operationid;  // 操作人ID

    @Column(name = "paycode")
    public String paycode;        // 例如：002=线下支付，002001=现金、002002=电汇等

    @Column(name = "collectionid")
    public Integer collectionid;   //收款表ID

    @Column(name = "orderstatuscode")
    public String orderstatuscode;   // 001=已下单，002=商务支付，003=线下支付，002001=已付款，002002=已收款

    @Column(name = "orderno")
    public String orderno;         // 订单编号，规则未知

    //外键关系的列
    @ManyToOne
    @JoinColumn(name = "guestid")
    public Frontuser frontUser;     //客户

    //根据合同类型获取所有订单号
    public static List getAllOrderNosByGuestCode(String guestCode){
        Query query = Model.em().createNativeQuery("SELECT RO.ORDERNO\n" +
                "FROM REC_ORDER ro,CONTRACT c,REC_FRONTUSER rf\n" +
                "WHERE RO.id=c.ORDERID AND c.FRONTUSERID=RF.id\n" +
                "AND RF.GUESTCODE=?\n" +
                "ORDER BY c.CREATETIME DESC");
        query.setParameter(1,guestCode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList();
    }


}
