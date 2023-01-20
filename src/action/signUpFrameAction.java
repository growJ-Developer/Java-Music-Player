package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.Provider.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.loginFrame;
import app.mainFrame;
import app.optionFrame;
import app.signUpFrame;
import bean.userBean;
import factory.serviceDAO;
import model.hintComboModel;

public class signUpFrameAction {
	
	/* 회원가입 버튼 클릭 시 Action */
	public static class signUpBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JTextField idField = (JTextField) mainFrame.getComponentByName("SIGNUP_ID_FIELD");
			JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("SIGNUP_PASSWORD_FIELD");
			JTextField nameField = (JTextField) mainFrame.getComponentByName("SIGNUP_NAME_FIELD");
			JTextField phoneField = (JTextField) mainFrame.getComponentByName("SIGNUP_PHONE_FIELD");
			JTextField hintField = (JTextField) mainFrame.getComponentByName("SIGNUP_HINT_FIELD");
			JComboBox hintCombo = (JComboBox) mainFrame.getComponentByName("SIGNUP_HINT_COMBO");
			String id = idField.getText();
			String password = String.valueOf(pswField.getPassword());
			String name = nameField.getText();
			String hint = hintField.getText();
			String phone = phoneField.getText();
			int hintNum = hintCombo.getSelectedIndex();
			hintNum = signUpFrame.getSelectedHint(hintNum);
			
			
			if(checkId(id).length() != 0) {																// ID 검증 
				optionFrame.alertMessageDialog(checkId(id));
				return;
			} else if(checkPassword(password, id).length() != 0) {							// Password 검증 
				optionFrame.alertMessageDialog(checkPassword(password, id));
				return;
			} else if(checkName(name).length() != 0) {										// 이름 검증
				optionFrame.alertMessageDialog(checkName(name));
				return;
			} else if(checkPhone(phone).length() != 0) {										// 전화번호 검증	
				optionFrame.alertMessageDialog(checkPhone(phone));
				return;
			} else if(checkHint(hint).length() != 0) {												// 힌트 검증 
				optionFrame.alertMessageDialog(checkHint(hint));
				return;
			}
			
			/* 가입 정보를 bean에 담습니다 */
			userBean user = new userBean();
			user.setUserId(id);
			user.setUserPassword(password);
			user.setUserName(name);
			user.setPhoneNumber(phone);
			user.setHintNum(hintNum);
			user.setHintAns(hint);
			
			serviceDAO dao = new serviceDAO();
			if(dao.checkUser(id)) {																		// 사용중인 아이디 검증
				optionFrame.alertMessageDialog("이미 사용하고 있는 아이디입니다. 다른 아이디를 이용해 주세요.");
				return;
			}
			// 회원가입 진행
			dao.singUpUser(user);
			
			optionFrame.alertMessageDialog("회원가입이 완료되었습니다.");
			
			// 이전 화면(로그인)으로 이동
			signUpFrame signUpFrm = (signUpFrame) mainFrame.getComponentByName("SIGNUP_FRAME");
			signUpFrm.setVisible(false);
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(true);
			
			/* 프레임 초기화 */
			frameClear();
		}
	}
	
	
	/* 이전 버튼 클릭 시 Action */
	public static class prevBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 프레임 초기화 */
			frameClear();
			
			// 이전 화면(로그인)으로 이동
			signUpFrame signUpFrm = (signUpFrame) mainFrame.getComponentByName("SIGNUP_FRAME");
			signUpFrm.setVisible(false);
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(true);
		}
	}
	
	/* 프레임 내 요소들을 초기화합니다 */
	public static void frameClear() {
		JTextField idField = (JTextField) mainFrame.getComponentByName("SIGNUP_ID_FIELD");
		JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("SIGNUP_PASSWORD_FIELD");
		JTextField nameField = (JTextField) mainFrame.getComponentByName("SIGNUP_NAME_FIELD");
		JTextField hintField = (JTextField) mainFrame.getComponentByName("SIGNUP_HINT_FIELD");
		JComboBox hintCombo = (JComboBox) mainFrame.getComponentByName("SIGNUP_HINT_COMBO");
		
		idField.setText("");
		pswField.setText("");
		nameField.setText("");
		hintField.setText("");
		hintCombo.setSelectedIndex(0);
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
