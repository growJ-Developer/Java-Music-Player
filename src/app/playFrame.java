package app;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.text.html.ImageView;

import org.graalvm.compiler.nodes.java.NewMultiArrayNode;

import action.playFrameAction;
import bean.musicInfoBean;
import factory.iconManager;
import factory.mp3Player;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.FlowLayout;

public class playFrame extends JPanel{
	private int isCycle = 0;									// 반복 재생 여부를 설정합니다. (0 : 한곡만, 1 : 전체재생, 2 : 재생후 처음부터)
	
	public playFrame() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		setContolPanel();
		this.setBackground(Color.WHITE);
	}	
	
	/* 재생 컨트롤 바를 생성합니다 */
	public void setContolPanel() {
		/* 아이콘을 생성합니다. */
		IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(Color.WHITE);
		
		/* 사이클 버튼을 설정합니다 */
		JButton cycleBtn = new JButton();
		cycleBtn.setBounds(150, 20, 30, 30);
		cycleBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LOOKS_ONE, 20, new Color(128, 128, 128)));
		cycleBtn.setBorderPainted(false);
		cycleBtn.setHorizontalAlignment(SwingConstants.CENTER);
		cycleBtn.addMouseListener(new playFrameAction.cycleBtnAction());
		
		/* 이전 곡 재생 버튼을 설정합니다 */
		JButton prevBtn = new JButton();
		prevBtn.setBounds(10, 60, 40, 40);
		prevBtn.addMouseListener(new playFrameAction.prevBtnAction());
		prevBtn.setBorderPainted(false);
		prevBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SKIP_PREVIOUS, 30, new Color(128, 128, 128)));
		prevBtn.setHorizontalAlignment(SwingConstants.CENTER);
		
		/* 다음 곡 재생 버튼을 설정합니다 */
		JButton nextBtn = new JButton();
		nextBtn.setBounds(100, 60, 40, 40);
		nextBtn.addMouseListener(new playFrameAction.nextBtnAction());
		nextBtn.setBorderPainted(false);
		nextBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SKIP_NEXT, 30, new Color(128, 128, 128)));
		nextBtn.setHorizontalAlignment(SwingConstants.CENTER);
		
		/* 재생/정지 버튼을 설정합니다. */
		JButton toogleBtn = new JButton();
		toogleBtn.setBounds(50, 55, 50, 50);
		toogleBtn.addMouseListener(new playFrameAction.toggleBtnAction());
		toogleBtn.setBorderPainted(false);
		toogleBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PLAY_CIRCLE_OUTLINE, 40, new Color(94, 94, 94)));
		toogleBtn.setHorizontalAlignment(SwingConstants.CENTER);
		toogleBtn.setName("TOGGLE_BTN");
		
		
		/* 볼륨 컨트롤을 위한 슬라이더 */
		JSlider volumeSlider = new JSlider(0, 0, 100, 0);
		volumeSlider.setBounds(30, 10, 90, 20);
		volumeSlider.setValue(100);
		volumeSlider.addMouseListener(new playFrameAction.volumeAction());
		volumeSlider.setName("VOLUME_CONTROL");
		
		/* 재생 Timeline을 위한 슬라이더 */
		JSlider timeLine = new JSlider(0, 0, 100, 0);
		timeLine.setBounds(376, 70, 400, 20);
		timeLine.setName("TIME_LINE");
		timeLine.addMouseListener(new playFrameAction.timeLineAction());
		
		/* TimeLine에 따른 시간 표시 */
		JLabel timeSec = new JLabel("00:00");
		timeSec.setBounds(330, 70, 50, 20);
		timeSec.setName("TIME_DISPLAY");
		JLabel timeMaxSec = new JLabel("00:00");
		timeMaxSec.setBounds(780, 70, 50, 20);
		timeMaxSec.setName("TIME_MAX_DISPLAY");
		
		/* 노래 제목 */
		JLabel nameLabel = new JLabel();
		nameLabel.setBounds(330, 20, 481, 20);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setName("MUSIC_NAME_PANE");
		
		/* 가수명 */
		JLabel artistLabel = new JLabel();
		artistLabel.setBounds(395, 40, 350, 20);
		artistLabel.setName("ARTIST_NAME_PANE");
		artistLabel.setHorizontalAlignment(JLabel.CENTER);
		
		/* 앨범 커버 표시 */
		JLabel artWorkPanel = new JLabel();
		artWorkPanel.setBounds(200, 0, 110, 110);
		artWorkPanel.setPreferredSize(new Dimension(100, 80));
		artWorkPanel.setName("ART_WORK_PANEL");
		artWorkPanel.setBackground(Color.WHITE);
		/* 앨범 커버 기본 이미지 사용 */
		try {
			//URL url = mainApp.class.getResource("/resource/blank_artwork.png");
			//ImageIcon imageIcon = new ImageIcon(url);
			//File file = new File("resource/images/blank_artwork.png");
			//File file = new File(url.toURI());
			Image originImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("blank_artwork.png"));
			//Image originImg = ImageIO.read(file);
			Image changedImg = originImg.getScaledInstance(artWorkPanel.getWidth(), artWorkPanel.getHeight(), Image.SCALE_SMOOTH);
			artWorkPanel.setIcon(new ImageIcon(changedImg));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		/* 셔플 버튼 */
		JButton randomBtn = new JButton();
		randomBtn.setBounds(150, 60, 30, 30);
		randomBtn.setBorderPainted(false);
		randomBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SHUFFLE, 20, new Color(128, 128, 128)));
		randomBtn.setVerticalAlignment(SwingConstants.CENTER);
		randomBtn.setHorizontalAlignment(SwingConstants.CENTER);
		randomBtn.addMouseListener(new playFrameAction.randomBtnAction());
		
		/* 음소거 버튼 */
		JButton muteBtn = new JButton();
		muteBtn.setBounds(10, 10, 20, 20);
		muteBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VOLUME_MUTE, 20, new Color(128, 128, 128)));
		muteBtn.setBorderPainted(false);
		muteBtn.setHorizontalAlignment(SwingConstants.CENTER);
		muteBtn.addMouseListener(new playFrameAction.muteAction());
		
		/* 스피커(불륨 최대) 버튼 */
		JButton speakerBtn = new JButton();
		speakerBtn.setBounds(120, 10, 20, 20);
		speakerBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VOLUME_UP, 20, new Color(128, 128, 128)));
		speakerBtn.setBorderPainted(false);
		speakerBtn.setHorizontalAlignment(SwingConstants.CENTER);
		speakerBtn.addMouseListener(new playFrameAction.speakerAction());
		
		
		controlPanel.setLayout(null);
		controlPanel.setPreferredSize(new Dimension(1024, 110));
		
		controlPanel.add(cycleBtn);
		controlPanel.add(randomBtn);
		controlPanel.add(muteBtn);
		controlPanel.add(volumeSlider);
		controlPanel.add(speakerBtn);
		controlPanel.add(timeMaxSec);
		controlPanel.add(timeLine);
		controlPanel.add(timeSec);
		controlPanel.add(prevBtn);
		controlPanel.add(nextBtn);
		controlPanel.add(toogleBtn);
		controlPanel.add(nameLabel);
		controlPanel.add(artistLabel);
		controlPanel.add(artWorkPanel);
		
		this.add(controlPanel);
	}
	
	/* 사이클 설정 여부를 반환합니다 */
	public int getCycleInfo() {
		return isCycle;
	}
	
	public void setCycleInfo(int cycle) {
		this.isCycle = cycle;
	}
	
	/* 현재 재생 상황을 출력합니다. */
	public void printNowPlaying() {
		
	}
	
	/* 다음 음악을 재생합니다.(이전에 음악이 재생중인 상태에서만 사용합니다) */
	public void nextPlay() {
		/* PLAY_LIST를 받아옵니다 */
		listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
		if(listFrm.getListSize() != 0) {
			musicInfoBean data = listFrm.getList(0);
			File file = new File(data.getFilePath());
			
			switch (isCycle) {
			case 0:									// 한곡만 재생일 경우
				/* 아무 동작을 수행하지 않습니다 */
				break;
			case 1:									// 전체 재생일 경우 
				mp3Player.getInstance().play(file);
				listFrm.removeList(0, true);
				break;
			case 2:									// 반복 재생일 경우, 재생을 설정 한 뒤, 맨 뒤 목록에 다시 추가합니다.
				mp3Player.getInstance().play(file);
				listFrm.removeList(0, true);
				listFrm.addList(data);
			case 3:									// 한곡 반복 재생인 경우, 최상의 PlayList의 곡을 다시 재생합니다. 
				mp3Player.getInstance().play(file);
			default:
				break;
			}
		}
	}
}


