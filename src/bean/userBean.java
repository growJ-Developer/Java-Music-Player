package bean;

public class userBean {
	private String userId;					// ID
	private String userPassword;			// PASSWORD
	private String userName;				// NAME
	private String phoneNumber;			// PHONE_NUMBER
	private int personId;					// PERSON_ID
	private String hintAns;					// HINT_ANSWER 
	private int hintNum;						// HINT_INDEX
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public String getHintAns() {
		return hintAns;
	}
	public void setHintAns(String hintAns) {
		this.hintAns = hintAns;
	}
	public int getHintNum() {
		return hintNum;
	}
	public void setHintNum(int hintNum) {
		this.hintNum = hintNum;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
