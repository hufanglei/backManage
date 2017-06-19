package models.searchModel;

/**
 * 公司列表首页查询model 用户
 */
public class SearchFrontUser {
    //登录名
    private String login;
    //姓名
    private String name;
    //手机号
    private String mobphone;
    //认证状态
    private String affirmregcode;
    //有效状态
    private String statuscode;
    //公司名称
    private String company;
    //新增用户周期-天
    private String cycle;

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobphone() {
        return mobphone;
    }

    public void setMobphone(String mobphone) {
        this.mobphone = mobphone;
    }

    public String getAffirmregcode() {
        return affirmregcode;
    }

    public void setAffirmregcode(String affirmregcode) {
        this.affirmregcode = affirmregcode;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
