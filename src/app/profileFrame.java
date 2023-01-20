package app;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import action.loginFrameAction.signUpBtnActionListener;
import action.profileFrameAction;
import factory.serviceDAO;
import action.signUpFrameAction;
import bean.hintBean;
import bean.userBean;
import model.hintComboModel;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;

public class profileFrame extends JPanel{
	private JTextField idField;
	private JPasswordField pswField;
	private JTextField nameField;
	private JTextField hintField;
	private static hintComboModel hintModel;
	private JTextField phoneField;
	
	public profileFrame() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setName("LOGIN_PANEL");
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBounds(6, 6, 438, 332);
		loginPanel.setPreferredSize(new Dimension(500, 500));
		loginPanel.setLayout(null);
		
		/* ID 입력 설정 */		
		JLabel idLabel = new JLabel("ID");
		idLabel.setHorizontalAlignment(SwingConstants.LEFT);
		idLabel.setBounds(20, 30, 80, 30);
				
		idField = new JTextField();
		idField.setBounds(120, 30, 300, 30);
		idField.setColumns(10);
		idField.setName("PROFILE_ID_FIELD");
		idField.setEnabled(false);
		
		/* 이름 입력 설정 */
		JLabel nameLabel = new JLabel("이름");
		nameLabel.setBounds(20, 70, 80, 30);
		
		nameField = new JTextField();
		nameField.setBounds(120, 70, 300, 30);
		nameField.setColumns(10);
		nameField.setName("PROFILE_NAME_FIELD");
		nameField.setEnabled(false);
		
		/* 전화번호 입력 설정 */
		JLabel phoneLabel = new JLabel("전화번호");
		phoneLabel.setBounds(20, 110, 80, 30);
		
		phoneField = new JTextField();
		phoneField.setName("PROFILE_PHONE_FIELD");
		phoneField.setColumns(10);
		phoneField.setBounds(120, 110, 300, 30);		
		phoneField.setEnabled(false);
		
		/* PASSWORD 입력 설정 */
		JLabel pswLabel = new JLabel("PASSWORD");
		pswLabel.setBounds(20, 150, 80, 30);
		
		pswField = new JPasswordField();
		pswField.setBounds(120, 150, 300, 30);
		pswField.setColumns(10);
		pswField.setName("PROFILE_PASSWORD_FIELD");
		pswField.setEnabled(false);
		
		/* 암호 힌트 입력 설정 */
		JLabel hintQLabel = new JLabel("암호 힌트");
		hintQLabel.setBounds(20, 190, 80, 30);
		
		hintModel = new hintComboModel();
		JComboBox<hintComboModel> hintCombo = new JComboBox<hintComboModel>(hintModel);
		setHintInfo();
		hintCombo.setBounds(120, 190, 300, 30);
		hintCombo.setName("PROFILE_HINT_COMBO");
		hintCombo.setEditable(false);
		hintCombo.setEnabled(false);
		
		JLabel hintALabel = new JLabel("힌트 정답");
		hintALabel.setBounds(20, 230, 80, 30);
		
		hintField = new JTextField();
		hintField.setBounds(120, 230, 300, 30);
		hintField.setColumns(10);
		hintField.setName("PROFILE_HINT_FIELD");
		hintField.setEnabled(false);
		
		/* 프로필 공개 설정 */
		JButton profileBtn = new JButton("프로필 공개");
		profileBtn.setBounds(310, 0, 110, 30);
		profileBtn.addMouseListener(new profileFrameAction.profileBtnActionListener());
		profileBtn.setName("PROFILE_OPEN_BTN");
		profileBtn.setVisible(false);
		profileBtn.setBackground(Color.white);
		
		/* 변경 버튼 설정 */
		JButton changeBtn = new JButton("변경");
		changeBtn.setBounds(310, 270, 110, 30);
		changeBtn.addMouseListener(new profileFrameAction.changeBtnActionListener());
		changeBtn.setName("PROFILE_CHANGE_BTN");
		changeBtn.setVisible(false);
		changeBtn.setBackground(Color.white);
		
		/* 이전 버튼 설정 */
		JButton listBtn = new JButton("프로필 목록");
		listBtn.setBounds(10, 0, 110, 30);
		listBtn.addMouseListener(new profileFrameAction.listBtnActionListener());
		listBtn.setName("PROFILE_LIST_BTN");
		listBtn.setBackground(Color.white);
		
		/* 로그아웃 버튼 설정 */
		JButton logoutBtn = new JButton("로그아웃");
		logoutBtn.setName("PROFILE_LOGOUT_BTN");
		logoutBtn.setBounds(10, 270, 110, 30);
		logoutBtn.addMouseListener(new profileFrameAction.logoutBtnActionListener());
		logoutBtn.setVisible(false);
		logoutBtn.setBackground(Color.white);
		
		/* 회원탈퇴 버튼 설정 */
		JButton signOutBtn = new JButton("회원탈퇴");
		signOutBtn.setName("PROFILE_SIGNOUT_BTN");
		signOutBtn.setBounds(200, 270, 110, 30);
		signOutBtn.addMouseListener(new profileFrameAction.signOutBtnActionListener());
		signOutBtn.setVisible(false);
		signOutBtn.setBackground(Color.white);
		
		/* 즐겨찾기 음악 버튼 설정 */
		JButton favoriteBtn = new JButton("즐겨찾기 음악");
		favoriteBtn.setBounds(120, 1, 117, 29);
		favoriteBtn.addMouseListener(new profileFrameAction.favoriteBtnActionListener());
		favoriteBtn.setBackground(Color.white);
		
		
		loginPanel.add(idLabel);
		loginPanel.add(idField);
		loginPanel.add(pswLabel);
		loginPanel.add(pswField);
		loginPanel.add(nameLabel);
		loginPanel.add(nameField);
		loginPanel.add(phoneLabel);
		loginPanel.add(phoneField);
		loginPanel.add(hintQLabel);
		loginPanel.add(hintCombo);
		loginPanel.add(hintALabel);
		loginPanel.add(hintField);
		loginPanel.add(profileBtn);
		loginPanel.add(logoutBtn);
		loginPanel.add(favoriteBtn);
		
		loginPanel.add(signOutBtn);
		loginPanel.add(listBtn);
		loginPanel.add(changeBtn);
		this.add(loginPanel);
		
		
		
	}
	
	/* 힌트 목록을 DB로 부터 받아와서 Combobox에 세팅합니다 */
	public static void setHintInfo() {
		hintModel.removeAllElements();
		serviceDAO dao = new serviceDAO();
		HashMap<Integer, String> findMap = dao.getFindQuestion();
		Iterator<Integer> it = findMap.keySet().iterator();
		
		while (it.hasNext()) {
			hintBean data = new hintBean();
			int key = it.next();
			data.setHintKey(key);
			data.setQuestion(findMap.get((Integer) key));
			hintModel.addElement(data);
		}
	}
	
	public static int getSelectedHint(int index) {
		hintBean data = hintModel.getElementDataAt(index);
		return data.getHintKey();
		
	}
	
	/* 프로파일 정보를 설정합니다 */
	public static void setProfile(int personId) {
		setHintInfo();
		serviceDAO dao = new serviceDAO();
		boolean profileYn = dao.getProfileYn(personId);				// 프로필 공개 여부
		
		if(profileYn || (personId == dao.getPersonId())) {			// 프로필이 공개된 경우 또는 본인 프로필인 경우
			/* 프로필 정보를 설정합니다 */
			userBean bean = dao.getUserInfo(personId);
			/* 설정할 컴포넌트를 불러옵니다 */
			JTextField idField = (JTextField) mainFrame.getComponentByName("PROFILE_ID_FIELD");
			JTextField nameField = (JTextField) mainFrame.getComponentByName("PROFILE_NAME_FIELD");
			JTextField hintField = (JTextField) mainFrame.getComponentByName("PROFILE_HINT_FIELD");
			JTextField phoneField = (JTextField) mainFrame.getComponentByName("PROFILE_PHONE_FIELD");
			JComboBox<hintComboModel> hintCombo = (JComboBox<hintComboModel>) mainFrame.getComponentByName("PROFILE_HINT_COMBO");
			
			idField.setText(bean.getUserId());
			nameField.setText(bean.getUserName());
			hintField.setText(bean.getHintAns());
			phoneField.setText(bean.getPhoneNumber());
			hintCombo.setSelectedIndex(bean.getHintNum());
			
			
			JButton logoutBtn = (JButton) mainFrame.getComponentByName("PROFILE_LOGOUT_BTN");
			JButton changeBtn = (JButton) mainFrame.getComponentByName("PROFILE_CHANGE_BTN");
			JButton signOutBtn = (JButton) mainFrame.getComponentByName("PROFILE_SIGNOUT_BTN");
			JButton profileBtn = (JButton) mainFrame.getComponentByName("PROFILE_OPEN_BTN");
			
			/* 프로필 공개 여부 설정 */
			if(dao.getProfileYn(personId)) {
				profileBtn.setText("프로필 비공개");
			} else {
				profileBtn.setText("프로필 공개");
			}
			
			if(personId == dao.getPersonId()) {								// 본인 프로필이 맞는 경우
				profileBtn.setVisible(true);
				changeBtn.setVisible(true);
				logoutBtn.setVisible(true);
				signOutBtn.setVisible(true);
			} else {																		// 본인 프로필이 아닌 경우
				profileBtn.setVisible(false);
				changeBtn.setVisible(false);
				logoutBtn.setVisible(false);
				signOutBtn.setVisible(false);
			}
		} else {																		// 프로필이 비공개 된 경우 
			optionFrame.alertMessageDialog("프로필이 공개되지 않은 사용자입니다.");
			// TODO Profile 리스트로 돌어갑니다 */
		}	
	}
}
