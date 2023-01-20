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
import factory.serviceDAO;
import action.signUpFrameAction;
import bean.hintBean;
import model.hintComboModel;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;

public class signUpFrame extends JPanel{
	private JTextField idField;
	private JPasswordField pswField;
	private JTextField nameField;
	private JTextField hintField;
	private static hintComboModel hintModel;
	private JTextField phoneField;
	
	public signUpFrame() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setName("LOGIN_PANEL");
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBounds(6, 6, 438, 300);
		loginPanel.setPreferredSize(new Dimension(500, 500));
		loginPanel.setLayout(null);
		
		/* ID 입력 설정 */		
		JLabel idLabel = new JLabel("ID");
		idLabel.setHorizontalAlignment(SwingConstants.LEFT);
		idLabel.setBounds(20, 20, 80, 30);
				
		idField = new JTextField();
		idField.setBounds(120, 20, 300, 30);
		idField.setColumns(10);
		idField.setName("SIGNUP_ID_FIELD");
		
		/* PASSWORD 입력 설정 */
		JLabel pswLabel = new JLabel("PASSWORD");
		pswLabel.setBounds(20, 60, 80, 30);
		
		pswField = new JPasswordField();
		pswField.setBounds(120, 60, 300, 30);
		pswField.setColumns(10);
		pswField.setName("SIGNUP_PASSWORD_FIELD");
		
		/* 이름 입력 설정 */
		JLabel nameLabel = new JLabel("이름");
		nameLabel.setBounds(20, 100, 80, 30);
		
		nameField = new JTextField();
		nameField.setBounds(120, 100, 300, 30);
		nameField.setColumns(10);
		nameField.setName("SIGNUP_NAME_FIELD");
		
		/* 전화번호 입력 설정 */
		JLabel phoneLabel = new JLabel("전화번호");
		phoneLabel.setBounds(20, 140, 80, 30);
		
		phoneField = new JTextField();
		phoneField.setName("SIGNUP_PHONE_FIELD");
		phoneField.setColumns(10);
		phoneField.setBounds(120, 140, 300, 30);		
		
		/* 암호 힌트 입력 설정 */
		JLabel hintQLabel = new JLabel("암호 힌트");
		hintQLabel.setBounds(20, 180, 80, 30);
		
		hintModel = new hintComboModel();
		JComboBox hintCombo = new JComboBox(hintModel);
		setHintInfo();
		hintCombo.setBounds(120, 180, 300, 30);
		hintCombo.setName("SIGNUP_HINT_COMBO");
		
		/* 힌트 정답 입력 설정 */
		JLabel hintALabel = new JLabel("힌트 정답");
		hintALabel.setBounds(20, 220, 80, 30);
		
		hintField = new JTextField();
		hintField.setBounds(120, 220, 300, 30);
		hintField.setColumns(10);
		hintField.setName("SIGNUP_HINT_FIELD");
		
		/* 회원가입 버튼 설정 */
		JButton signUpBtn = new JButton("회원가입");
		signUpBtn.setBounds(220, 260, 110, 30);
		signUpBtn.addMouseListener(new signUpFrameAction.signUpBtnActionListener());
		signUpBtn.setBackground(Color.white);
		
		/* 초기화 버튼 설정 */
		JButton prevBtn = new JButton("이전");
		prevBtn.setBounds(100, 260, 110, 30);
		prevBtn.addMouseListener(new signUpFrameAction.prevBtnActionListener());
		prevBtn.setBackground(Color.white);
		
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
		loginPanel.add(signUpBtn);
		loginPanel.add(prevBtn);
		this.add(loginPanel);
	}
	
	/* 힌트 목록을 DB로 부터 받아와서 Combobox에 세팅합니다 */
	public void setHintInfo() {
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
}
