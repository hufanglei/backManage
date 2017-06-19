package models.admin.auth;

public class AccountAuth {
	
	private Long id;
	private String name;
	private String username;
	private AccountRole accountRole;
	
	public AccountAuth(Long id, String name, String username){
		this.id=id;
		this.name=name;
		this.username=username;
	}
	
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setUsername(String username){
		this.username=username;
	}
	public void setAccountRole(AccountRole accountRole){
		this.accountRole=accountRole;
	}
	
	public Long getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getUsername(){
		return this.username;
	}
	public AccountRole getAccountRole(){
		return this.accountRole;
	}
	
}
