package models.searchModel;

/**
 * 合同查询model
 */
public class SearchContract {
    //订单号码
    private String orderno;
    //起始金额
    private Double moneyFrom;
    //截止金额
    private Double moneyTo;
    //公司名称
    private String companyName;
    //下单日期
    private String orderTime;

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Double getMoneyFrom() {
        return moneyFrom;
    }

    public void setMoneyFrom(Double moneyFrom) {
        this.moneyFrom = moneyFrom;
    }

    public Double getMoneyTo() {
        return moneyTo;
    }

    public void setMoneyTo(Double moneyTo) {
        this.moneyTo = moneyTo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
