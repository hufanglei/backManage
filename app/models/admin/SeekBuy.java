package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 手机订单列表
 */
@Entity
@Table(name = "seekbuy")
public class SeekBuy extends GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seekbuy")
    @SequenceGenerator(initialValue = 1, name = "seekbuy", schema = "RJDS", sequenceName = "SEQ_SEEKBUY", allocationSize = 1)
    @Column(name = "ID")
    public Long id;

    @Column(name = "codeno")
    public String codeno;//订单号

    @Column(name = "buyuserid")
    public Long buyuserid;//买方ID

    @Column(name = "name")
    public String name;//姓名

    @Column(name = "mobphone")
    public String mobphone;//联系手机

    @Column(name = "email")
    public String email;//email

    @Column(name = "address")
    public String address;//交货地点

    @Column(name = "goodkind")
    public String goodkind;//品种

    @Column(name = "mark")
    public String mark;//牌号

    @Column(name = "price")
    public BigDecimal price;//期望价格

    @Column(name = "num")
    public BigDecimal num;//数量

    @Column(name = "pick")
    public String pick;//配送

    @Column(name = "createtime")
    public Date createtime;//创建时间

    @Column(name = "ordertime")
    public Date ordertime;//订货时间

    @Column(name = "statuscode")
    public String statuscode;//有效状态

    @Column(name = "memo")
    public String memo;//买方说明 备注

    @Column(name = "paycode")
    public String paycode;// 例如：002=线下支付，002001=现金、002002=电汇等

    @Column(name = "orderstatuscode")
    public String orderstatuscode; //001=已下单，002=商务支付，003=线下支付，002001=已付款，002002=已收款

    @Column(name = "voiceurl")
    public String voiceurl;//语音地址

    @Column(name = "totalprice")
    public BigDecimal totalprice;//总价

    @Column(name = "storageid")
    public Long storageid;

}
