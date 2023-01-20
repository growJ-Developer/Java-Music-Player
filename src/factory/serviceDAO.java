package factory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.tools.javac.code.Attribute.Array;

import bean.musicInfoBean;
import bean.userBean;

public class serviceDAO {
	/* 사용자 고유 번호(초기값 -1) */
	private static int personId = -1;
	
	public serviceDAO() {
		
	}
	/* 패스워드를 암호화합니다 */
	public String encryptPassword(String password){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			return bytesToHex(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;				
	}
	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
	
	/* 플레이 기록을 기록합니다 */
	public void recordPlayInfo(musicInfoBean data) {
		replaceMusicInfoBeanForQuery(data);
		String statement = "INSERT INTO PLAY_LOG(PERSON_ID, MUSIC_NAME, ARTIST, PLAY_DATE) VALUES ("
				+ personId + ", "
				+ "'" + data.getMusicName() + "', "
				+ "'" + data.getArtist() + "', "
				+ "date(datetime('now', '+9 hours')))";
		
		dbFactory factory = new dbFactory();
		factory.insertQuery(statement);
	}

	/* 사용자 정보에 따른 회원가입을 진행합니다 */
	public void singUpUser(userBean user) {
		replaceUserBeanForQuery(user);
		String statement = "INSERT INTO USERS(ID, PASSWORD, USER_NAME, PHONE_NUMBER, FIND_PK, FIND_ANSWER) VALUES("
				+ "'" + user.getUserId() + "', "												// ID
				+ "'" + encryptPassword(user.getUserPassword()) + "', "			//	PASSWORD
				+ "'" + user.getUserName() + "', "	
				+ "'" + user.getPhoneNumber() + "', "
				+ "'" + user.getHintNum() + "', "	
				+ "'" + user.getHintAns() + "')"	;
		
		dbFactory factory = new dbFactory();
		factory.insertQuery(statement);
	}
	
	/* 음악 차트를 DB로 부터 불러옵니다 */
	public ArrayList<musicInfoBean> getChartList(){
		ArrayList<musicInfoBean> chartList = new ArrayList<musicInfoBean>();
		String statement = "SELECT MUSIC_NAME, ARTIST, COUNT(*) as PLAY_CNT FROM PLAY_LOG GROUP BY MUSIC_NAME, ARTIST ORDER BY PLAY_CNT DESC";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			musicInfoBean bean = new musicInfoBean();
			bean.setMusicName((String)mData.get("MUSIC_NAME"));
			bean.setArtist((String)mData.get("ARTIST"));
			bean.setPlayCnt((Integer)mData.get("PLAY_CNT"));
			chartList.add(bean);
		}
		
		return chartList;
	}
	
	/* 공개된 프로필 사용자 정보를 불러옵니다 */
	public  ArrayList<userBean> getUserList(){
		ArrayList<userBean> userList = new ArrayList<userBean>();
		String statement = "SELECT PERSON_ID, ID, USER_NAME, PHONE_NUMBER FROM USERS WHERE OPEN_YN = 'Y'";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			userBean bean = new userBean();
			bean.setPersonId((Integer)mData.get("PERSON_ID"));
			bean.setUserName((String)mData.get("USER_NAME"));
			bean.setUserId((String)mData.get("ID"));
			bean.setPhoneNumber((String)mData.get("PHONE_NUMBER"));
			
			userList.add(bean);
		}
		
		return userList;
	}
	
	/* 질문 정보를 불러옵니다 */
	public HashMap<Integer, String> getFindQuestion(){
		HashMap<Integer, String> findMap = new HashMap<Integer, String>();
		
		String statement = "SELECT PK, QUESTION FROM ANSWER_QUESTION WHERE USE_YN = 'Y'";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			Integer key = (Integer) mData.get("PK");
			String question = (String) mData.get("QUESTION");
			
			findMap.put(key, question);
		}
		
		return findMap;
	}
	
	/* 사용자 ID의 체크를 확인합니다 */
	public boolean checkUser(String userId) {
		String statement = "SELECT * FROM USERS WHERE ID = '" + userId + "'";
		
		dbFactory factory = new dbFactory();
		
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		if(data.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/* 사용자 ID를 찾습니다 */
	public ArrayList<String> findUserId(userBean user) {
		replaceUserBeanForQuery(user);
		String statement = "SELECT ID FROM USERS WHERE USER_NAME = '" + user.getUserName() + "' AND PHONE_NUMBER = '" + user.getPhoneNumber() + "'";
		ArrayList<String> findIds = new ArrayList<String>();
		
		dbFactory factory = new dbFactory();
		
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			findIds.add((String)mData.get("ID"));
		}
		
		return findIds;
	}
	
	/* 로그인 되어있는 계정에 대한 설정을 변경합니다 */
	public void updateUser(userBean user) {
		replaceUserBeanForQuery(user);
		String id = user.getUserId();
		String password = encryptPassword(user.getUserPassword());
		String name = user.getUserName();
		String phone = user.getPhoneNumber();
		int hintNum = user.getHintNum();
		String hintAns = user.getHintAns();
		
		String statement = "UPDATE USERS SET"
				+ " ID = '" + id + "',"
				+ " PASSWORD = '" + password + "',"
				+ " USER_NAME = '" + name + "',"
				+ " PHONE_NUMBER = '" + phone + "',"
				+ " FIND_PK = " + hintNum + ","
				+ " FIND_ANSWER = '" + hintAns + "'"
				+ " WHERE PERSON_ID = " + personId;
		
		dbFactory factory = new dbFactory();
		
		factory.updateQuery(statement);
	}
	
	/* 사용자 정보를 받아옵니다 */
	public userBean getUserInfo(int personId) {
		String statement = "SELECT PERSON_ID, ID, USER_NAME, PHONE_NUMBER, FIND_PK, FIND_ANSWER, OPEN_YN FROM USERS WHERE PERSON_ID = " + personId;
		
		userBean bean = new userBean();
		
		dbFactory factory = new dbFactory();
		factory.getQueryResult(statement);
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			bean.setUserId((String)mData.get("ID"));
			bean.setUserName((String)mData.get("USER_NAME"));
			bean.setPhoneNumber((String)mData.get("PHONE_NUMBER"));
			bean.setHintNum((int)mData.get("FIND_PK"));
			bean.setHintAns((String)mData.get("FIND_ANSWER"));
		}
		
		return bean;		
	}
	
	/* 프로필 공개 / 비공개를 설정합니다 */
	public void updateProfile(String openYn) {
		String statement = "UPDATE USERS SET OPEN_YN = '" + openYn + "' WHERE PERSON_ID = " + personId;
		
		dbFactory factory = new dbFactory();
		factory.updateQuery(statement);
	}
	
	/* 프로필 공개 / 비공개 여부를 반환합니다 */
	public boolean getProfileYn(int personId) {
		String statement = "SELECT OPEN_YN FROM USERS WHERE OPEN_YN = 'Y' AND PERSON_ID = " + personId;
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		if(data.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/* 로그인 되 있는 계정의 PersonId를 반환합니다 */
	public int getPersonId() {
		return personId;
	}
	
	/* 로그인 되어있는 계정에 대한 비밀번호를 검증합니다 */
	public boolean checkUserPassword(String password) {
		String statement = "SELECT PERSON_ID FROM USERS WHERE PERSON_ID = " + personId + " AND PASSWORD = '" + encryptPassword(password) +"'";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		if(data.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/* 패스워드 찾기 진행에 대한 결과를 반환합니다 */
	public boolean findPasswordCheck(userBean user) {
		replaceUserBeanForQuery(user);
		String userId = user.getUserId();
		String phone = user.getPhoneNumber();
		int hintNum = user.getHintNum();
		String hintAns = user.getHintAns();
		
		String statement = "SELECT PERSON_ID FROM USERS WHERE ID = '" + userId + "' AND PHONE_NUMBER = '" + phone + "' AND FIND_PK = " + hintNum + " AND FIND_ANSWER = '" + hintAns + "'";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		if(data.size() > 0){
			return true;
		} else {
			return false;
		}
	}
	
	/* 회원 탈퇴를 진행합니다 */
	public void signOutUser() {
		String statement = "DELETE FROM USERS WHERE PERSON_ID = " + personId;
		
		dbFactory factory = new dbFactory();
		factory.deleteQuery(statement);
	}
	
	/* 로그아웃을 진행합니다 */
	public void logoutUser() {
		this.personId = -1;
	}
	
	/* 신규 패스워드를 설정합니다 */
	public void setNewPassword(userBean user) {
		replaceUserBeanForQuery(user);
		String userId = user.getUserId();
		String password = encryptPassword(user.getUserPassword());
		
		String statement = "UPDATE USERS SET PASSWORD = '" + password +"' WHERE ID = '" + userId +"'";
		
		dbFactory factory = new dbFactory();
		factory.updateQuery(statement);		
	}
	
	/* ID 로그인을 진행합니다. */
	public void loginUser(userBean user) {
		replaceUserBeanForQuery(user);
		String userId = user.getUserId();
		String password = encryptPassword(user.getUserPassword());
		String statement = "SELECT PERSON_ID FROM USERS WHERE ID = '" + userId + "' AND PASSWORD = '" + password + "'";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			this.personId = (Integer) mData.get("PERSON_ID");
		}
	}
	
	/* 로그인 여부를 확인합니다 */
	public static boolean isLogin() {
		return personId >= 0;
	}
	
	/* 음악 그룹 리스트를 불러옵니다 */
	public static ArrayList<HashMap<Integer, String>> getGroupList(int personId){
		ArrayList<HashMap<Integer, String>> result = new ArrayList<HashMap<Integer,String>>();
		String statement = "SELECT * FROM FAVORITE_GROUP WHERE PERSON_ID = " + personId;
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			HashMap<Integer, String> groupData = new HashMap<Integer, String>();
			groupData.put((int)mData.get("GROUP_PK"), (String)mData.get("GROUP_NAME"));
			result.add(groupData);
		}
		
		return result;
	}
	
	/* 그룹 번호에 따른 음악 리스트를 불러옵니다 */
	public static ArrayList<musicInfoBean> getGroupMusic(int groupIndex){
		ArrayList<musicInfoBean> result = new ArrayList<musicInfoBean>();
		String statement = "SELECT * FROM FAVORITES WHERE GROUP_ID = " + groupIndex + " ORDER BY MUSIC_INDEX ASC";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			musicInfoBean bean = new musicInfoBean();
			HashMap<String, Object> mData = it.next();
			bean.setMusicName((String)mData.get("MUSIC_NAME"));
			bean.setArtist((String)mData.get("ARTIST"));
			bean.setFilePath((String)mData.get("FILE_PATH"));
			result.add(bean);
		}
		
		return result;
	}
	
	/* 그룹 사용자 번호에 따른 음악 리스트 전체를 불러옵니다. */
	public static ArrayList<musicInfoBean> getGroupMusicSearch(int personId, String searchText){
		ArrayList<musicInfoBean> result = new ArrayList<musicInfoBean>();
		String statement = "SELECT * FROM FAVORITE_GROUP FG, FAVORITES F WHERE FG.PERSON_ID = " + personId + " AND FG.GROUP_PK = F.GROUP_ID AND F.MUSIC_NAME LIKE '%" + searchText + "%'";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			musicInfoBean bean = new musicInfoBean();
			HashMap<String, Object> mData = it.next();
			bean.setMusicName((String)mData.get("MUSIC_NAME"));
			bean.setArtist((String)mData.get("ARTIST"));
			bean.setFilePath((String)mData.get("FILE_PATH"));
			result.add(bean);
		}
		
		return result;
	}
	
	/* 그룹을 추가합니다. */
	public static void addGroupList(String groupName) {
		String statement = "INSERT INTO FAVORITE_GROUP (GROUP_NAME, PERSON_ID) VALUES("
				+ "'" + groupName + "', "
				+ "" + personId + ")";
		
		dbFactory factory = new dbFactory();
		factory.insertQuery(statement);
	}
	
	/* 그룹명으로 해당 그룹이 이미 사용자에게 있는지 검사합니다 */
	public static boolean checkGroup(String groupName) {
		String statement = "SELECT * FROM FAVORITE_GROUP WHERE GROUP_NAME = '" + groupName + "' AND PERSON_ID = " + personId;
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data  = factory.getQueryResult(statement);
		if(data.size() > 0) {
			return true;
		}
		return false;
	}
	
	/* 그룹의 명칭을 변경합니다 */
	public static void updateGroupName(int groupId, String groupName) {
		String statement = "UPDATE FAVORITE_GROUP SET GROUP_NAME = '" + groupName + "' WHERE GROUP_PK = " + groupId;
		
		dbFactory factory = new dbFactory();
		factory.updateQuery(statement);
	}
	
	/* 선택한 그룹을 삭제합니다 */
	public static void delGroup(int groupIndex) {
		String statement = "DELETE FROM FAVORITE_GROUP WHERE GROUP_PK = " + groupIndex;
		String statement1 = "DELETE FROM FAVORITES WHERE GROUP_ID = " + groupIndex;
		
		dbFactory factory = new dbFactory();
		factory.deleteQuery(statement);
	}
	
	/* 선택한 그룹에 음악을 추가합니다. */
	public static void addMusicAtGroup(musicInfoBean data, int groupIndex) {
		replaceMusicInfoBeanForQuery(data);
		String statement = "INSERT INTO FAVORITES (GROUP_ID, MUSIC_NAME, ARTIST, FILE_PATH) VALUES ("
				+ "" + groupIndex + ", "
				+ "'" + data.getMusicName() + "', "
				+ "'" + data.getArtist() + "', "
				+ "'" + data.getFilePath() + "')";
		
		dbFactory factory = new dbFactory();
		factory.insertQuery(statement);
	}
	
	/* 선택한 그룹에서 음악을 삭제합니다 */
	public static void delMusicAtGroup(musicInfoBean data, int groupIndex) {
		replaceMusicInfoBeanForQuery(data);
		String statement = "DELETE FROM FAVORITES WHERE MUSIC_NAME = '" + data.getMusicName() + "' AND ARTIST = '" + data.getArtist() + "' AND GROUP_ID = " + groupIndex;
		
		dbFactory factory = new dbFactory();
		factory.deleteQuery(statement);		
	}
	
	/* 사용자 정보를 기반으로 personId를 불러옵니다. */
	public static int getPersonIdByInfo(userBean user) {
		int getId = -1;
		replaceUserBeanForQuery(user);
		String statement = "SELECT PERSON_ID FROM USERS WHERE ID = '" + user.getUserId() + "' AND USER_NAME = '" + user.getUserName() + "'";
		
		dbFactory factory = new dbFactory();
		ArrayList<HashMap<String, Object>> data = factory.getQueryResult(statement);
		
		Iterator<HashMap<String, Object>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<String, Object> mData = it.next();
			getId = (Integer) mData.get("PERSON_ID");
		}
		
		return getId;		
	}
	
	/* Query문에 수행하기 위한 목적으로 musicInfoBean의 텍스트를 수정합니다 */
	public static void replaceMusicInfoBeanForQuery(musicInfoBean data) {
		if(data.getMusicName() != null)			data.setMusicName(data.getMusicName().replaceAll("\'", "\'\'"));
		if(data.getArtist() != null)					data.setArtist(data.getArtist().replaceAll("\'", "\'\'"));		
		if(data.getFileName() != null)				data.setFileName(data.getFileName().replaceAll("\'", "\'\'"));
		if(data.getFilePath() != null)				data.setFilePath(data.getFilePath().replaceAll("\'", "\'\'"));
		if(data.getLyrics() != null)					data.setLyrics(data.getLyrics().replaceAll("\'", "\'\'"));
	}
	
	/* Query문제 수행하기 위한 목적으로 userBean의 텍스트를 수정합니다 */
	public static void replaceUserBeanForQuery(userBean data) {
		if(data.getHintAns() != null) 				data.setHintAns(data.getHintAns().replaceAll("\'", "\'\'"));
		if(data.getPhoneNumber() != null)		data.setPhoneNumber(data.getPhoneNumber().replaceAll("\'", "\'\'"));
		if(data.getUserId() != null)				data.setUserId(data.getUserId().replaceAll("\'", "\'\'"));
		if(data.getUserName() != null)			data.setUserName(data.getUserName().replaceAll("\'", "\'\'"));
	}
}
