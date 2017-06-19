package models.admin.auth;

/**
 * Created by peak on 2016/8/1 0001.
 */
public class AuthAuthority {

    private Long id;

    private String name;//权限名字

    private String url;//用户可访问的url

    private String matchurl;//相关url

    private String itemicon;//图标

    private Long parentid;//父id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMatchurl() {
        return matchurl;
    }

    public void setMatchurl(String matchurl) {
        this.matchurl = matchurl;
    }

    public String getItemicon() {
        return itemicon;
    }

    public void setItemicon(String itemicon) {
        this.itemicon = itemicon;
    }

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

}
