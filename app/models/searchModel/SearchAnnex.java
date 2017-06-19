package models.searchModel;

/**
 * 三证审核首页查询model
 */
public class SearchAnnex {
    //登录名
    private String login;
    //姓名
    private String name;
    //附件类型
    private String annexcode;
    //操作人
    private Long operationid;
    //审核状态
    private String statuscode;
    //创建时间
    private String createtime;

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

    public String getAnnexcode() {
        return annexcode;
    }

    public void setAnnexcode(String annexcode) {
        this.annexcode = annexcode;
    }

    public Long getOperationid() {
        return operationid;
    }

    public void setOperationid(Long operationid) {
        this.operationid = operationid;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
