package models.searchModel;

/**
 * 帮您找货首页查询
 */
public class SearchFindgood {
    //联系人
    private String linkman;
    //手机号
    private String phone;
    //受理状态
    private String dealcode;
    //成交供应商
    private String sup;
    //开始时间
    private String begintime;
    //截止时间
    private String endtime;

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDealcode() {
        return dealcode;
    }

    public void setDealcode(String dealcode) {
        this.dealcode = dealcode;
    }

    public String getSup() {
        return sup;
    }

    public void setSup(String sup) {
        this.sup = sup;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
