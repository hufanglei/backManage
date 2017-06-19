package models.searchModel;

/**
 * 订单首页查询model
 */
public class SearchOrder {
    private String orderno;
    private String companyName;
    private String createtime;
    private String finalmoneyStart;
    private String finalmoneyEnd;

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

    public String getFinalmoneyStart() {
        return finalmoneyStart;
    }

    public void setFinalmoneyStart(String finalmoneyStart) {
        this.finalmoneyStart = finalmoneyStart;
    }

    public String getFinalmoneyEnd() {
        return finalmoneyEnd;
    }

    public void setFinalmoneyEnd(String finalmoneyEnd) {
        this.finalmoneyEnd = finalmoneyEnd;
    }
}
