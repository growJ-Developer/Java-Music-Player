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
import action.loginFrameAction.signUpBtnActionListener;
import bean.hintBean;
import factory.serviceDAO;
import model.hintComboModel;

public class findFrame extends JPanel{
	private JTextField idField;
	private JPasswordField pswField;
	private JTextField hintField;
	private JTextField phoneField;
	private static hintComboModel hintModel;
	
	public findFrame() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setName("LOGIN_PANEL");
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBounds(6, 6, 438, 345);
		loginPanel.setPreferredSize(new Dimension(500, 500));
		loginPanel.setLayout(null);
		
		/* ID 입력 설정 */		
		JLabel idLabel = new JLabel("ID");
		idLabel.setHorizontalAlignment(SwingConstants.LEFT);
		idLabel.setBounds(20, 20, 80, 30);
				
		idField = new JTextField();
		idField.setName("FIND_ID_FIELD");
		idField.setBounds(120, 20, 300, 30);
		idField.setColumns(10);
		
		/* 전화번호 입력 설정 */
		JLabel phoneLabel = new JLabel("전화번호");
		phoneLabel.setBounds(20, 60, 80, 30);
		
		phoneField = new JTextField();
		phoneField.setName("FIND_PHONE_FIELD");
		phoneField.setColumns(10);
		phoneField.setBounds(120, 60, 300, 30);		
		
		/* 암호 힌트 입력 설정 */
		JLabel hintQLabel = new JLabel("암호 힌트");
		hintQLabel.setBounds(20, 100, 80, 30);
		
		hintModel = new hintComboModel();
		JComboBox hintCombo = new JComboBox(hintModel);
		setHintInfo();
		hintCombo.setBounds(120, 100, 300, 30);
		hintCombo.setName("FIND_HINT_COMBO");
		
		JLabel hintALabel = new JLabel("힌트 정답");
		hintALabel.setBounds(20, 140, 80, 30);
		
		hintField = new JTextField();
		hintField.setBounds(120, 140, 300, 30);
		hintField.setColumns(10);
		hintField.setName("FIND_HINT_FIELD");
		
		/* 찾기 버튼 설정 */
		JButton signUpBtn = new JButton("찾기");
		signUpBtn.setBounds(220, 180, 110, 30);
		signUpBtn.addMouseListener(new findFrameAction.findBtnActionListener());
		
		/* 이전 버튼 설정 */
		JButton prevBtn = new JButton("이전");
		prevBtn.setBounds(100, 180, 110, 30);
		prevBtn.addMouseListener(new findFrameAction.prevBtnActionListener());
		
				
		/* PASSWORD 입력 설정 */
		JLabel pswLabel = new JLabel("새 비밀번호");
		pswLabel.setBounds(20, 220, 80, 30);
		pswLabel.setName("FIND_PSW_LABEL");
		pswLabel.setVisible(false);
		
		pswField = new JPasswordField();
		pswField.setBounds(120, 220, 300, 30);
		pswField.setColumns(10);
		pswField.setName("FIND_PSW_FIELD");
		pswField.setVisible(false);
		
		/* 변경 버튼 설정 */
		JButton applyBtn = new JButton("변경");
		applyBtn.setBounds(160, 260, 117, 29);
		applyBtn.addMouseListener(new findFrameAction.applyBtnActionListener());
		applyBtn.setName("FIND_APPLY_BTN");
		applyBtn.setVisible(false);
		
		loginPanel.add(phoneLabel);
		loginPanel.add(phoneField);
		loginPanel.add(idLabel);
		loginPanel.add(idField);
		loginPanel.add(pswLabel);
		loginPanel.add(pswField);
		loginPanel.add(hintQLabel);
		loginPanel.add(hintCombo);
		loginPanel.add(hintALabel);
		loginPanel.add(hintField);
		loginPanel.add(signUpBtn);
		loginPanel.add(prevBtn);
		loginPanel.add(applyBtn);
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
