/*
 * 로그인 한 유저 정보 들어가는 클래스
 * */
package scheduler.main.user;

public class UserInfo {
	private int member_no;
	private String id;
	private String name;
	private String email;
	private String phone;
	private boolean admin;
	private boolean showTrayMessage = false;

	public void setData(int member_no, String id, String name, String email, String phone, boolean admin) {
		this.member_no = member_no;
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.admin = admin;

	}

	public int getMember_no() {
		return member_no;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isAdmin() {
		return admin;
	}

	public boolean isShowTrayMessage() {
		return showTrayMessage;
	}

	public void setShowTrayMessage(boolean showTrayMessage) {
		this.showTrayMessage = showTrayMessage;
	}
	

}
