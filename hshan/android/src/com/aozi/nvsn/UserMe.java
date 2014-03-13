package com.aozi.nvsn;

public class UserMe {
	private final static UserMe INSTANCE = new UserMe();
	
	private String user_id = "";
	private boolean isLogined = false;
	
	private UserMe() {}
	
	public static UserMe getInstance() {
		return INSTANCE;
	}

	
	public void setInfo(JSONObjectBuilder result) {
		user_id = result.getString("user_id");
		isLogined = true;
	}
	
	public String getUserId() {
		return user_id;
	}
	
	public boolean isLogined() {
		return isLogined;
	}
}
