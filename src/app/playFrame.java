package app;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import action.playFrameAction;
import bean.musicInfoBean;
import factory.mp3Player;

public class playFrame extends JPanel{
	private int isCycle = 0;									// 반복 재생 여부를 설정합니다. (0 : 한곡만, 1 : 전체재생, 2 : 재생후 처음부터)
	
	public playFrame() {
		setContolPanel();
		this.setBackground(Color.red);
	}	
	
	/* 재생 컨트롤 바를 생성합니다 */
	public void setContolPanel() {
		JPanel controlPanel = new JPanel();
		
		JButton cycleBtn = new JButton("0");
		cycleBtn.addMouseListener(new playFrameAction.cycleBtnAction());
		
		JButton prevBtn = new JButton("<<");
		prevBtn.addMouseListener(new playFrameAction.prevBtnAction());
		JButton nextBtn = new JButton(">>");
		nextBtn.addMouseListener(new playFrameAction.nextBtnAction());
		
		/* 정지 버튼을 설정합니다. */
		JButton toogleBtn = new JButton(">");
		toogleBtn.addMouseListener(new playFrameAction.toggleBtnAction());
		
		/* 볼륨 컨트롤을 위한 슬라이더 */
		JSlider volumeSlider = new JSlider(0, 0, 100, 0);
		volumeSlider.setValue(100);
		volumeSlider.addMouseListener(new playFrameAction.volumeAction());
		volumeSlider.setName("VOLUME_CONTROL");
		
		/* 재생 Timeline을 위한 슬라이더 */
		JSlider timeLine = new JSlider(0, 0, 100, 0);
		timeLine.setName("TIME_LINE");
		timeLine.addMouseListener(new playFrameAction.timeLineAction());
		
		/* TimeLine에 따른 시간 표시 */
		JLabel timeSec = new JLabel("00:00");
		timeSec.setName("TIME_DISPLAY");
		JLabel timeMaxSec = new JLabel("00:00");
		timeMaxSec.setName("TIME_MAX_DISPLAY");
		
		/* 노래 제목 */
		JLabel nameLabel = new JLabel();
		nameLabel.setName("MUSIC_NAME_PANE");
		
		/* 가수명 */
		JLabel artistLabel = new JLabel();
		artistLabel.setName("ARTIST_NAME_PANE");
		
		/* 앨범 커버 표시 */
		JLabel artWorkPanel = new JLabel();
		artWorkPanel.setPreferredSize(new Dimension(100, 80));
		artWorkPanel.setName("ART_WORK_PANEL");
		artWorkPanel.setBackground(Color.LIGHT_GRAY);
		
		/* 가사 표시(스크롤 제공) */
		JLabel lyricsLabel = new JLabel();
		lyricsLabel.setName("LYRICS_PANEL");
		JScrollPane lyricsPane = new JScrollPane(lyricsLabel);
		lyricsPane.setBackground(Color.lightGray);
		lyricsPane.setPreferredSize(new Dimension(350, 100));
				
		controlPanel.add(cycleBtn);
		controlPanel.add(volumeSlider);
		controlPanel.add(timeMaxSec);
		controlPanel.add(timeLine);
		controlPanel.add(timeSec);
		controlPanel.add(prevBtn);
		controlPanel.add(nextBtn);
		controlPanel.add(toogleBtn);
		controlPanel.add(nameLabel);
		controlPanel.add(artistLabel);
		controlPanel.add(artWorkPanel);
		controlPanel.add(lyricsPane);
		
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


