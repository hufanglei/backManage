package models.admin.auth;

import java.util.List;

public class AccountRole {
	
	private Long id;
	private String name;
	private List<AuthorityMenu> authorityMenus;
	private List<PermissionMenu> permissionMenus;
	
	public AccountRole(Long id, String name){
		this.id=id;
		this.name=name;
	}
	
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPermissionMenus(List<PermissionMenu> permissionMenus){
		this.permissionMenus=permissionMenus;
	}
	public void setAuthorityMenus(List<AuthorityMenu> authorityMenus){
		this.authorityMenus=authorityMenus;
	}
	
	public Long getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public List<PermissionMenu> getPermissionMenus(){
		return this.permissionMenus;
	}
	public List<AuthorityMenu> getAuthorityMenus(){
		return this.authorityMenus;
	}
	
}
