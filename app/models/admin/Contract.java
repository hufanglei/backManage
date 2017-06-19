package models.admin;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 合同
 */
@Entity
@Table(name="contract")
public class Contract extends Model {


    @Column(name = "memo")
    public String memo; //备注

    @Column(name = "createtime")
    public Date createtime; //创建时间 买方合同或卖方合同的初始生成时间，不是签约时间

    @Column(name = "operationid")
    public Integer operationid;  //操作人ID：  与买方或卖方签约的平台工作人员，一般是平台合同人员

    @Column(name = "picurl2")
    public String picurl2; // 图片2: 买方合同：买方回传图片，卖方合同：平台回传图片（两个章）

    @Column(name = "picurl1")
    public String picurl1;  //图片1: 买方合同：平台图片（盖一章），卖方合同：卖方盖一章的图片

    @Column(name = "wordurl")
    public String wordurl;   // word文档 :一个商品可以对应多个url

    @Column(name = "contractstatuscode")
    public String contractstatuscode;  // 合同状态Code
    // "001001=初始，001002=平台已盖章，001003=需平台修订，001004=平台已修订，001005=买方已盖章，001006=需协助上传，001007=平台已校对（买方合同生效）
   // 002001=初始，002002=需协助上传，002003=卖方已提交，002004=已回传盖章，002005=卖方已校对（卖方合同生效）

    @Column(name = "contractno")
    public String contractno;         //合同号: RJDZLSW-20160320-001

    @Column(name="amount")
    public Double amount;     //合同金额

    //外键关系的列
    @ManyToOne
    @JoinColumn(name = "frontuserid")
    public Frontuser frontuser;      //买卖方ID:  一个订单对应一个买方与一个卖方

    @ManyToOne
    @JoinColumn(name = "orderid")
    public Order order;          //订单号

    @ManyToOne
    @JoinColumn(name = "seekbuyid")
    public SeekBuy seekbuy;          //手机订单号

}
