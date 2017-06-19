package models.admin;
import play.db.jpa.Model;
import javax.persistence.*;
import java.util.Date;

/**
 * 库存表
 */
@Entity
@Table(name="rec_Stock")
public class Stock extends Model {

    @Column(name = "stockcode")
    public String stockcode;      // 001=入库 002=上架、003=下架、004=出库、005=退货、006=其他

    @Column(name = "affirmstockcode")
    public String affirmstockcode; // 只对应001=入库：001=待审核 002=审核中；003=审核通过；004=审核驳回

    @Column(name = "codeno")
    public String codeno;         //卖方+仓库+商品下的批号（可能不同于实际的批号）规则###

    @Column(name = "initnum")
    public Double initnum;        //初始数量，一批次的入库、上架、下架、出库、退货或其他数量

    @Column(name = "affirmnum")
    public Double affirmnum;      //确认数量，（后台）仓库管理人员确认

    @Column(name = "pricenotax")
    public Double pricenotax;     //未税单价， 上架，未税单价，例如：100元

    @Column(name = "pricehastax")
    public Double pricehastax;    //含税单价 ，上架，例如：117元

    @Column(name = "amountnotax")
    public Double amountnotax;    //未税金额， 未税单价 * 数量，例如：2件共200元

    @Column(name = "taxmoney")
    public Double taxmoney;       //税额， 未税单价 * 税率 * 数量，例如：2件共34元

    @Column(name = "finalmoney")
    public Double finalmoney;     //价税合计， 含税单价 * 数量，例如：2件共234元

    @Column(name = "stockuserid")
    public Integer stockuserid;   // 仓储管理员：确认数量

    @Column(name = "frontuserid")
    public Integer frontuserid;    //卖方业务员， 每个业务员查看自己入库的各批次，以计算业绩

    @Column(name = "operationid")
    public Integer operationid;    //后台操作人ID

    @Column(name = "affirmtime")
    public Date affirmtime;        // 审核时间

    @Column(name = "operationip")
    public String operationip;     //操作IP地址

    @Column(name = "createtime")
    public Date createtime;        //创建时间

    @Column(name = "statuscode")
    public String statuscode;      //001=有效 002=无效

    @Column(name = "memo")
    public String memo;            //备注

    //外键关系的列
    @ManyToOne
    @JoinColumn(name="companyid")
    public Company company;     //卖家公司

    @ManyToOne
    @JoinColumn(name="storageid")
    public Storage storage;     // 仓库

    @ManyToOne
    @JoinColumn(name="goodid")
    public Good good;  //商品ID


}
