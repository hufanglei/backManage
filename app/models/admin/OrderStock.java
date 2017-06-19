package models.admin;
import play.db.jpa.Model;
import javax.persistence.*;
import java.util.Date;

/**
 *订单明细
 */
@Entity
@Table(name="rec_Order_Stock")
public class OrderStock extends Model{

    @Column(name = "memo")
    public String memo;          //备注

    @Column(name = "statuscode")
    public String statuscode;    //001=有效 002=无效

    @Column(name = "createtime")
    public Date createtime;      //创建时间

    @Column(name = "operationip")
    public String operationip;   //操作IP地址

    @Column(name = "operationid")
    public Integer operationid;  //操作人ID

    //外键关系的列
    @ManyToOne
    @JoinColumn(name="orderid")
    public Order order;      //订单

    @ManyToOne
    @JoinColumn(name="stockid")
    public Stock stock;      //库存


}
