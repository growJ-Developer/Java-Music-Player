package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.Provider.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.favoriteFrame;
import app.loginFrame;
import app.mainFrame;
import app.menuFrame;
import app.optionFrame;
import app.profileFrame;
import app.profileListFrame;
import app.signUpFrame;
import bean.userBean;
import factory.serviceDAO;
import model.hintComboModel;

public class profileFrameAction {
	/* 즐겨찾기 버튼에 대한 액션 */
	public static class favoriteBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {			
			/* ID필드와 이름 필드를 기반으로 사용자 정보를 받아옵니다 */
			JTextField idField = (JTextField) mainFrame.getComponentByName("PROFILE_ID_FIELD");
			JTextField nameField = (JTextField) mainFrame.getComponentByName("PROFILE_NAME_FIELD");
			userBean user = new userBean();
			user.setUserId(idField.getText());
			user.setUserName(nameField.getText());
			
			/* personID를 계산합니다 */
			serviceDAO dao = new serviceDAO();
			int personId = dao.getPersonIdByInfo(user);
			
			/* 프레임 초기화 */
			menuFrameAction.frameClear();
			
			/* personId를 기반으로 playList를 보여줍니다. */			
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			favoriteFrm.setVisible(true);
			/* DB로 부터 Group 로딩을 수행합니다 */
			favoriteFrm.setGroupList(personId);
		}
	}
	
	/* 회원탈퇴에 대한 액션 */
	public static class signOutBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 사용자 정보를 받아와서 검증합니다 */
			String password = optionFrame.getUserMessageDialog("회원 탈퇴", "현재 로그인 계정의 비밀번호를 입력해 주세요. 탈퇴 이후에는 복구가 불가능합니다.", 3);
			
			serviceDAO dao = new serviceDAO();
			if(dao.checkUserPassword(password)) {
				dao.signOutUser();
				dao.logoutUser();
				
				optionFrame.alertMessageDialog("회원 탈퇴되었습니다.");
				
				/* 프레임 클리어를 진행합니다 */
				menuFrameAction.frameClear();
				
				/* 로그인 프레임으로 이동 */
				loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
				loginFrm.setVisible(true);
			} else {
				optionFrame.alertMessageDialog("비밀번호가 일치하지 않습니다.");
			}
		}
	}
	
	/* 로그아웃에 대한 액션 */
	public static class logoutBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			serviceDAO dao = new serviceDAO();
			/* 로그아웃을 진행합니다 */
			dao.logoutUser();
			/* 프레임 클리어를 진행합니다 */
			menuFrameAction.frameClear();
			
			optionFrame.alertMessageDialog("로그아웃 되었습니다.");
			
			/* 로그인 프레임으로 이동 */
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(true);
		}
		
	}
	
	/* 프로필 목록 버튼에 대한 액션 */
	public static class listBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 프레임 클리어를 진행합니다 */
			menuFrameAction.frameClear();
			
			/* 프로필 리스트로 이동 */
			profileListFrame profileListFrm = (profileListFrame) mainFrame.getComponentByName("PROFILE_LIST_FRAME");
			profileListFrm.setVisible(true);
		}
		
	}
	
	/* 변경 버튼에 대한 액션 */
	public static class changeBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JTextField idField = (JTextField) mainFrame.getComponentByName("PROFILE_ID_FIELD");
			JTextField phoneField = (JTextField) mainFrame.getComponentByName("PROFILE_PHONE_FIELD");
			JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("PROFILE_PASSWORD_FIELD");
			JTextField nameField = (JTextField) mainFrame.getComponentByName("PROFILE_NAME_FIELD");
			JTextField hintField = (JTextField) mainFrame.getComponentByName("PROFILE_HINT_FIELD");
			JComboBox<hintComboModel> hintCombo =  (JComboBox<hintComboModel>) mainFrame.getComponentByName("PROFILE_HINT_COMBO");
			
			JButton changeBtn = (JButton) e.getSource();
			if(changeBtn.getText().equals("변경")) {
				/* 사용자 정보를 받아와서 검증합니다 */
				String password = optionFrame.getUserMessageDialog("비밀번호 확인", "현재 로그인 계정의 비밀번호를 입력해 주세요.", 3);
				serviceDAO dao = new serviceDAO();
				if(password != null) {
					if(dao.checkUserPassword(password)) {
						idField.setEnabled(true);
						phoneField.setEnabled(true);
						pswField.setEnabled(true);
						nameField.setEnabled(true);
						hintField.setEnabled(true);
						hintCombo.setEditable(true);
						hintCombo.setEnabled(true);
						changeBtn.setText("적용");
					} else {
						optionFrame.alertMessageDialog("비밀번호가 일치하지 않습니다.");
					}
				}				
			} else if(changeBtn.getText().equals("적용")) {
				userBean user = new userBean();
				user.setUserId(idField.getText());
				user.setUserPassword(String.valueOf(pswField.getPassword()));
				user.setUserName(nameField.getText());
				user.setPhoneNumber(phoneField.getText());
				user.setHintAns(hintField.getText());
				user.setHintNum(profileFrame.getSelectedHint(hintCombo.getSelectedIndex()));
				String checkString = checkUserMap(user);
				if(checkString.length() == 0) {
					/* 유효성 검증에서 통과했으므로, 결과를 반영합니다 */
					serviceDAO dao = new serviceDAO();
					dao.updateUser(user);
					optionFrame.alertMessageDialog("수정이 완료되었습니다.");
					
					/* 프로필 초기화 */
					frameClear();
					/* 프로필 재로딩 */
					profileFrame.setProfile(new serviceDAO().getPersonId());
				} else {
					/* 유효성 검증에서 불통과 헀으므로 메시지를 출력합니다 */
					optionFrame.alertMessageDialog(checkString);
				}
			}
		}
	}
	
	private static String checkUserMap(userBean user) {
		if(checkId(user.getUserId()).length() != 0) {																// ID 검증 
			return checkId(user.getUserId());
		} else if(checkPassword(user.getUserPassword(), user.getUserId()).length() != 0) {		// Password 검증 
			return checkPassword(user.getUserPassword(), user.getUserId());
		} else if(checkName(user.getUserName()).length() != 0) {											// 이름 검증
			return checkName(user.getUserName());
		} else if(checkPhone(user.getPhoneNumber()).length() != 0) {										// 전화번호 검증	
			return checkPhone(user.getPhoneNumber());
		} else if(checkHint(user.getHintAns()).length() != 0) {													// 힌트 검증 
			return checkHint(user.getHintAns());
		}
		
		return "";
	}
	
	/* 프로필 공개 버튼에 대한 액션 */
	public static class profileBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JButton profileBtn = (JButton) e.getSource();
			
			if(profileBtn.getText().equals("프로필 공개")) {
				/* 프로필 공개에 대한 Action */
				serviceDAO dao = new serviceDAO();
				dao.updateProfile("Y");
				profileBtn.setText("프로필 비공개");
				optionFrame.alertMessageDialog("프로필이 공개 처리되었습니다.");
			} else if(profileBtn.getText().equals("프로필 비공개")) {
				/* 프로필 비공개에 대한 Action */
				serviceDAO dao = new serviceDAO();
				dao.updateProfile("N");
				profileBtn.setText("프로필 공개");
				optionFrame.alertMessageDialog("프로필이 비공개 처리되었습니다.");
			}
		}
	}
	
	/* 프레임 초기화 */
	public static void frameClear() {
		JTextField idField = (JTextField) mainFrame.getComponentByName("PROFILE_ID_FIELD");
		JTextField phoneField = (JTextField) mainFrame.getComponentByName("PROFILE_PHONE_FIELD");
		JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("PROFILE_PASSWORD_FIELD");
		JTextField nameField = (JTextField) mainFrame.getComponentByName("PROFILE_NAME_FIELD");
		JTextField hintField = (JTextField) mainFrame.getComponentByName("PROFILE_HINT_FIELD");
		JComboBox<hintComboModel> hintCombo =  (JComboBox<hintComboModel>) mainFrame.getComponentByName("PROFILE_HINT_COMBO");
		
		idField.setText("");
		idField.setEnabled(false);
		phoneField.setText("");
		phoneField.setEnabled(false);
		pswField.setText("");
		pswField.setEnabled(false);
		nameField.setText("");
		nameField.setEnabled(false);
		hintField.setText("");
		hintField.setEnabled(false);
		hintCombo.setSelectedIndex(0);
		hintCombo.setEditable(false);
		hintCombo.setEnabled(false);
	}
	

	/* 아이디 체크 메소드 */
	public static String checkId(String id) {
		// 5~12자 이하의 영문, 특수문 제외
		Pattern idPattern = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$");
		Matcher idMatcher = idPattern.matcher(id);
		
		if(!idMatcher.find()) 			return "ID는 5~12자 범위의, 영문, 특수문자, 숫자, '_' 로만 이루어져야 합니다.";
		return "";
	}
	
	/* 이름 체크 메소드 */
	public static String checkName(String name) {
		// 5~12자 이하의 영문, 특수문 제외
		Pattern namePattern = Pattern.compile("^[a-zA-Z가-힣]{2,20}$");
		Matcher nameMatcher = namePattern.matcher(name);
		
		if(!nameMatcher.find()) 			return "이름은 2~20문자로 공백 없이 이루어져야 합니다.";
		return "";
	}
	
	/* 전화번호 체크 메소드 */
	public static String checkPhone(String phone) {
		Pattern phonePattern = Pattern.compile("^\\d{2,3}-\\d{3,4}-\\d{4}$");
		Matcher nameMatcher = phonePattern.matcher(phone);
				
		if(!nameMatcher.find()) 			return "전화번호는 010-0000-0000과 같은 형태로 입력되어야 합니다.";
		return "";
	}
	
	/* 비밀번호 체크 메소드 */
	public static String checkPassword(String pwd, String id) {
		// 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
		Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
		Matcher passMatcher1 = passPattern1.matcher(pwd);

		if (!passMatcher1.find()) 	return "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.";

		/* 반복된 문자 확인 */
		Pattern passPattern2 = Pattern.compile("(\\w)\\1\\1\\1");
		Matcher passMatcher2 = passPattern2.matcher(pwd);

		if (passMatcher2.find()) 		return "비밀번호에 동일한 문자를 과도하게 연속해서 사용할 수 없습니다.";
		if (pwd.contains(id)) 			return "비밀번호에 ID를 포함할 수 없습니다.";

		/* 특수문자 확인 */
		Pattern passPattern3 = Pattern.compile("\\W");
		Pattern passPattern4 = Pattern.compile("[!@#$%^*+=-]");

		for (int i = 0; i < pwd.length(); i++) {
			String s = String.valueOf(pwd.charAt(i));
			Matcher passMatcher3 = passPattern3.matcher(s);

			if (passMatcher3.find()) {
				Matcher passMatcher4 = passPattern4.matcher(s);
				if (!passMatcher4.find()) {
					return "비밀번호에 특수문자는 !@#$^*+=-만 사용 가능합니다.";
				}
			}
		}

		/* 연속된 문자 확인 */
		int ascSeqCharCnt = 0; // 오름차순 연속 문자 카운트
		int descSeqCharCnt = 0; // 내림차순 연속 문자 카운트
		char char_0, char_1, char_2;
		int diff_0_1, diff_1_2;
		
		for (int i = 0; i < pwd.length() - 2; i++) {
			char_0 = pwd.charAt(i);
			char_1 = pwd.charAt(i + 1);
			char_2 = pwd.charAt(i + 2);
			diff_0_1 = char_0 - char_1;
			diff_1_2 = char_1 - char_2;
			if (diff_0_1 == 1 && diff_1_2 == 1) ascSeqCharCnt += 1;
			if (diff_0_1 == -1 && diff_1_2 == -1) descSeqCharCnt -= 1;
		}
		if (ascSeqCharCnt > 1 || descSeqCharCnt > 1) {
			return "비밀번호에 연속된 문자열을 사용할 수 없습니다.";
		}
		return "";
	}
	
	/* 힌트 체크 메소드 */
	public static String checkHint(String hint) {
		// 5~12자 이하의 영문, 특수문 제외
		Pattern hintPattern = Pattern.compile("^[a-zA-Z가-힣]{2,20}$");
		Matcher hintMatcher = hintPattern.matcher(hint);
		
		if(!hintMatcher.find()) 			return "힌트 답변은 5, 20문자 내로 이루어져야 합니다.";
		return "";
	}
}
