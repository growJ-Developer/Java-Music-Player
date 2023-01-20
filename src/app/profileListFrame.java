package app;

import javax.swing.JPanel;

import bean.musicInfoBean;
import bean.userBean;
import components.StripeRenderer;
import factory.serviceDAO;
import factory.similarityUtil;
import model.playListModel;
import model.userListModel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;

import action.profileListFrameAction;

public class profileListFrame extends JPanel{
	private static userListModel userModel;
	private static userListModel searchModel;
	private JTextField searchField;
	private static JList<playListModel> userList;
	
	public profileListFrame() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel chartPanel = new JPanel();
		chartPanel.setBounds(0, 0, 450, 579);
		chartPanel.setBackground(Color.WHITE);
		chartPanel.setLayout(null);
		
		userModel = new userListModel();
		
		/* 사용자 JList */
		userList = new JList<playListModel>(userModel);
		userList.setBounds(5, 35, 438, 548);
		userList.setFixedCellHeight(25);
		userList.setFixedCellWidth(480);
		userList.setCellRenderer(new StripeRenderer());
		userList.setName("PROFILE_USER_LIST");
		userList.addMouseListener(new profileListFrameAction.userListActionListener());
		setUserList();
		
		/* 사용자 검색 Field */
		searchField = new JTextField();
		searchField.setBounds(0, 5, 450, 30);
		searchField.setColumns(10);
		searchField.addKeyListener(new profileListFrameAction.searchFieldActionListener());
		searchField.setName("PROFILE_SEARCH_FIELD");
		
		chartPanel.add(userList);
		chartPanel.add(searchField);
		
		this.add(chartPanel);
	}
	
	/* model의 Filter를 설정합니다 */
	public static void setModelFilter(String text) {
		/* searchModel 의 모든 요소를 제거합니다 */
		searchModel = new userListModel();
		
		/* 검색 결과를 기반으로 JList의 목록을 설정합니다 */
		if(text.equals("")) {
			userList.setModel(userModel);
		} else {
			/* userModel에서 유사한 항목들을 찾아서 searchModel에 추가합니다 */
			LinkedList<userBean> elem = userModel.getElementDataAll();
			Iterator<userBean> it = elem.iterator();
			while(it.hasNext()) {
				userBean bean = it.next();
				similarityUtil smt = new similarityUtil();
				double comId = smt.similarity(text, bean.getUserId());			// 사용자 ID와의 유사도를 측정합니다.
				double comName = smt.similarity(text, bean.getUserName());	// 사용자 이름을 기반으로 유사도를 측정합니다 
				if(comId >= 0.5 || comName >= 0.5) {
					searchModel.addElement(bean);
				}
			}
			
			userList.setModel(searchModel);
		}
	}
	
	/* 공개된 프로필 사용자 정보를 DB로 부터 불러와서 Model에 설정합니다 */
	public static void setUserList() {
		removeAllUserList();
		
		serviceDAO dao = new serviceDAO();
		ArrayList<userBean> users = dao.getUserList();
		
		Iterator<userBean> it = users.iterator();
		while(it.hasNext()) {
			userModel.addElement(it.next());
		}
	}
	
	/* 사용자 리스트 모두를 삭제합니다 */
	public static void removeAllUserList() {
		userModel.removeAllElements();
	}
	
	/* 선택한 사용자의 정보를 반환합니다 */
	public static userBean getUserElement(int index) {
		userListModel findModel = (userListModel) userList.getModel();
		return findModel.getElementDataAt(index);
	}
	

}
