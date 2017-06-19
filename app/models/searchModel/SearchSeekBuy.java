package models.searchModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单列表首页查询model
 */
public class SearchSeekBuy {

    private String orderno;//订单号
    private String companyName;//公司名称
    private String createtime;//下单时间
    private String price;//起始金额
    private String totalPrice;//截止金额

    public String getOrderno() {
        return orderno;
    }
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
