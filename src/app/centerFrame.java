package app;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import action.loginFrameAction;

public class centerFrame extends JPanel{
	public centerFrame() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setSize(700, 500);
		centerPanel.setPreferredSize(new Dimension(700, 500));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setName("CENTER_INNER_PANEL");
		centerPanel.setLayout(new FlowLayout());
		centerPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		/* 음악 차트 */
		chartFrame chartFrm = new chartFrame();
		chartFrm.setPreferredSize(new Dimension(500, 500));
		chartFrm.setName("CHART_FRAME");
		chartFrm.setVisible(false);
		
		/* 로그인 창 */
		loginFrame loginFrm = new loginFrame();
		loginFrm.setPreferredSize(new Dimension(550, 500));
		loginFrm.setName("LOGIN_FRAME");
		loginFrm.setVisible(true);
		
		/* 회원가입 창 */
		signUpFrame signUpFrm = new signUpFrame();
		signUpFrm.setPreferredSize(new Dimension(550, 500));
		signUpFrm.setName("SIGNUP_FRAME");
		signUpFrm.setVisible(false);
		
		/* ID 찾기 창 */
		idFindFrame idFindFrm = new idFindFrame();
		idFindFrm.setPreferredSize(new Dimension(550, 500));
		idFindFrm.setName("ID_FIND_FRAME");
		idFindFrm.setVisible(false);
		
		/* 비밀번호 찾기 창 */
		findFrame findFrm = new findFrame();
		findFrm.setPreferredSize(new Dimension(550, 500));
		findFrm.setName("FIND_FRAME");
		findFrm.setVisible(false);
		
		/* 프로필 창 */
		profileFrame profileFrm = new profileFrame();
		profileFrm.setPreferredSize(new Dimension(550, 500));
		profileFrm.setName("PROFILE_FRAME");
		profileFrm.setVisible(false);
		
		/* 프로필 리스트 창 */
		profileListFrame profileListFrm = new profileListFrame();
		profileListFrm.setPreferredSize(new Dimension(550, 500));
		profileListFrm.setName("PROFILE_LIST_FRAME");
		profileListFrm.setVisible(false);
		
		/* 즐겨찾기 음악 창 */
		favoriteFrame favoriteFrm = new favoriteFrame();
		favoriteFrm.setPreferredSize(new Dimension(600, 500));
		favoriteFrm.setName("FAVORITE_FRAME");
		favoriteFrm.setVisible(false);
		
		centerPanel.add(chartFrm);
		centerPanel.add(loginFrm);
		centerPanel.add(signUpFrm);
		centerPanel.add(idFindFrm);
		centerPanel.add(findFrm);
		centerPanel.add(profileFrm);
		centerPanel.add(profileListFrm);
		centerPanel.add(favoriteFrm);
		
		this.add(centerPanel);
	}
}
