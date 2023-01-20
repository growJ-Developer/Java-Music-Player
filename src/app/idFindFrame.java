package app;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import action.findFrameAction;
import action.idFindFrameAction;
import action.loginFrameAction.signUpBtnActionListener;
import bean.hintBean;
import factory.serviceDAO;
import model.hintComboModel;

public class idFindFrame extends JPanel{
	private JTextField nameField;
	private JTextField phoneField;
	
	public idFindFrame() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setName("LOGIN_PANEL");
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBounds(6, 6, 436, 288);
		loginPanel.setPreferredSize(new Dimension(500, 500));
		loginPanel.setLayout(null);
		
		/* 이름 입력 설정 */		
		JLabel nameLabel = new JLabel("이름");
		nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nameLabel.setBounds(20, 20, 80, 30);
				
		nameField = new JTextField();
		nameField.setName("IDFIND_NAME_FIELD");
		nameField.setBounds(120, 20, 300, 30);
		nameField.setColumns(10);
		
		/* 전화번호 입력 설정 */
		JLabel phoneLabel = new JLabel("전화번호");
		phoneLabel.setBounds(20, 60, 80, 30);
		
		phoneField = new JTextField();
		phoneField.setName("IDFIND_PHONE_FIELD");
		phoneField.setColumns(10);
		phoneField.setBounds(120, 60, 300, 30);		
		
		/* 찾기 버튼 설정 */
		JButton signUpBtn = new JButton("찾기");
		signUpBtn.setBounds(220, 100, 110, 30);
		signUpBtn.addMouseListener(new idFindFrameAction.findBtnActionListener());
		
		/* 이전 버튼 설정 */
		JButton prevBtn = new JButton("이전");
		prevBtn.setBounds(100, 100, 110, 30);
		prevBtn.addMouseListener(new idFindFrameAction.prevBtnActionListener());
		
		
		loginPanel.add(phoneLabel);
		loginPanel.add(phoneField);
		loginPanel.add(nameLabel);
		loginPanel.add(nameField);
		loginPanel.add(signUpBtn);
		loginPanel.add(prevBtn);
		this.add(loginPanel);
	}	
}
