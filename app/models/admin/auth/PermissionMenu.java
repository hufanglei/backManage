package models.admin.auth;

public class PermissionMenu {
	
	public Long rootId;
	public String rootName;
	public Long subId;
	public String subName;
	public String curName;
	public String permission;
	
	public PermissionMenu(Long rootId, String rootName, Long subId, String subName, String curName, String permission){
		this.rootId=rootId;
		this.rootName=rootName;
		this.subId=subId;
		this.subName=subName;
		this.curName=curName;
		this.permission=permission;
	}
	
	public void setRootId(Long rootId){
		this.rootId=rootId;
	}
	public void setRootName(String rootName){
		this.rootName=rootName;
	}
	public void setSubId(Long subId){
		this.subId=subId;
	}
	public void setSubName(String subName){
		this.subName=subName;
	}
	public void setCurName(String curName){
		this.curName=curName;
	}
	public void setPermission(String permission){
		this.permission=permission;
	}
	
	public Long getRootId(){
		return this.rootId;
	}
	public String getRootName(){
		return this.rootName;
	}
	public Long getSubId(){
		return this.subId;
	}
	public String getSubName(){
		return this.subName;
	}
	public String getCurName(){
		return this.curName;
	}
	public String getPermission(){
		return this.permission;
	}
	
}
