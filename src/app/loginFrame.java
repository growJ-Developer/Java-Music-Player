package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import action.loginFrameAction;
import java.awt.FlowLayout;

public class loginFrame extends JPanel{
	private JTextField idField;
	private JPasswordField pswField;
	
	public loginFrame() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setName("LOGIN_PANEL");
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBounds(6, 6, 438, 288);
		loginPanel.setPreferredSize(new Dimension(500, 500));
		loginPanel.setLayout(null);
		
		/* ID 입력 설정 */		
		JLabel idLabel = new JLabel("ID");
		idLabel.setHorizontalAlignment(SwingConstants.LEFT);
		idLabel.setBounds(20, 50, 80, 30);
		
		idField = new JTextField();
		idField.setBounds(120, 50, 300, 30);
		idField.setColumns(10);
		idField.setName("LOGIN_ID_FIELD");
		
		/* PASSWORD 입력 설정 */
		JLabel pswLabel = new JLabel("PASSWORD");
		pswLabel.setBounds(20, 90, 80, 30);
		
		pswField = new JPasswordField();
		pswField.setBounds(120, 90, 300, 30);
		pswField.setColumns(10);
		pswField.setName("LOGIN_PASSWORD_FIELD");
		
		/* 로그인 버튼 설정 */
		JButton loginBtn = new JButton("로그인");
		loginBtn.setBounds(220, 130, 110, 30);
		loginBtn.addMouseListener(new loginFrameAction.loginBtnActionListener());
		loginBtn.setBackground(Color.white);
		
		/* 회원가입 버튼 설정 */
		JButton signUpBtn = new JButton("회원가입");
		signUpBtn.setBounds(100, 130, 110, 30);
		signUpBtn.addMouseListener(new loginFrameAction.signUpBtnActionListener());
		signUpBtn.setBackground(Color.white);
		
		/* 패스워드 찾기 설정 */
		JButton findBtn = new JButton("비밀번호찾기");
		findBtn.setBounds(310, 10, 110, 30);
		findBtn.addMouseListener(new loginFrameAction.findBtnActionListener());
		findBtn.setBackground(Color.white);
		
		/* ID 찾기 설정 */
		JButton idFindBtn = new JButton("ID 찾기");
		idFindBtn.setBounds(200, 10, 110, 30);
		idFindBtn.addMouseListener(new loginFrameAction.idFindBtnActionListener());
		idFindBtn.setBackground(Color.white);
		
		loginPanel.add(idLabel);
		loginPanel.add(idField);
		loginPanel.add(pswLabel);
		loginPanel.add(pswField);
		loginPanel.add(signUpBtn);
		loginPanel.add(loginBtn);		
		loginPanel.add(idFindBtn);
		loginPanel.add(findBtn);
		this.add(loginPanel);
	}
}
