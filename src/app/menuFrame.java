package app;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import action.menuFrameAction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class menuFrame extends JPanel{
	public menuFrame() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		JPanel menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(150, 370));
		menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		/* 즐겨찾기 목록 버튼 */
		JButton favoriteBtn = new JButton("Favorite Music");
		favoriteBtn.setPreferredSize(new Dimension(130, 50));
		favoriteBtn.addMouseListener(new menuFrameAction.favoriteBtnActionListener());
		favoriteBtn.setBackground(Color.white);
		
		/* 사용자 버튼 */
		JButton usersBtn = new JButton("Users");
		usersBtn.setPreferredSize(new Dimension(130, 50));
		usersBtn.addMouseListener(new menuFrameAction.userBtnActionListener());
		usersBtn.setBackground(Color.white);
		
		/* 음악 차트 버튼 */
		JButton chartBtn = new JButton("Music Chart");
		chartBtn.setPreferredSize(new Dimension(130, 50));
		chartBtn.addMouseListener(new menuFrameAction.chartBtnActionListener());
		chartBtn.setBackground(Color.white);
		
		/* 가사 표시(스크롤 제공) */
		JScrollPane lyricsPane = new JScrollPane();
		lyricsPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		lyricsPane.setBounds(330, 8, 485, 50);
		lyricsPane.setBackground(Color.lightGray);
		lyricsPane.setPreferredSize(new Dimension(130, 190));
		lyricsPane.setName("LYRICS_PANEL");
		lyricsPane.setBorder(null);
		
		
		menuPanel.add(favoriteBtn);
		menuPanel.add(usersBtn);
		menuPanel.add(chartBtn);
		menuPanel.add(lyricsPane);
		
		this.add(menuPanel);
		
		
	}
}
