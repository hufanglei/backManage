package models.searchModel;

/**
 * Created by dong-qianqian on 2016/9/27.
 * 申请聚融宝资质首页查询用model
 */
public class SearchApplyjurongbao {
    //用户类型
    private String usertype;
    //用户名
    private String name;
    //加工机械
    private Integer procmach;
    //产出产品
    private String product;
    //创建时间
    private String createtime;

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProcmach() {
        return procmach;
    }

    public void setProcmach(Integer procmach) {
        this.procmach = procmach;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
