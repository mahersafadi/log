package core.util;
/**
 * @author Maher Safadi
 *
 */
public class User {
	private long userId;
	private String userName;
	//user roles
	public User(){
		
	}
	public User(long uId, String name){
		userId = uId;
		userName = name;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	private static User sysUser = null;
	public static User getSystemUser(){
		if(sysUser == null){
			sysUser = new User(-1, "SysUser");
		}
		return sysUser;
	}
}
