package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.listFrame;
import app.mainFrame;
import app.optionFrame;
import app.optionFrame.*;
import app.playFrame;
import bean.musicInfoBean;
import factory.mp3Player;

public class playFrameAction {
	/* 이전 곡 재생 버튼 클릭 시 */ 
	public static class prevBtnAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* PLAY_LIST를 받아옵니다 */
			listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
			/* PREV_PLAY_LIST를 받아옵니다 */
			ArrayList<musicInfoBean> prevList = mainFrame.prevList;
			if(prevList.size() > 1) {
				musicInfoBean prevData = prevList.get(prevList.size() - 2);
				/* 리스트의 최상위에 추가한 뒤에 prevList에서 삭제합니다 */
				listFrm.addListAt(0, prevData);		
				prevList.remove(prevList.size() - 1);
				mp3Player.getInstance().play(new File(prevData.getFilePath()));
			}
		}
	}
	
	/* 다음 곡 재생 버튼 클릭 시 */
	public static class nextBtnAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* PLAY_LIST를 받아옵니다 */
			listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
			/* PLAY_PANEL을 받아옵니다 */
			playFrame playFrm = (playFrame) mainFrame.getComponentByName("PLAY_PANEL");
			
			if(listFrm.getListSize() == 0) {
				optionFrame.alertMessageDialog("재생 목록이 없습니다.");
			} else {
				musicInfoBean data = listFrm.getList(0);
				File file = new File(data.getFilePath());
				
				/* 사이클 설정에 따라서 재생을 설정합니다 */
				int cycle = playFrm.getCycleInfo();
				switch (cycle) {
				case 3:	// 전체 곡 반복 재생인 경우, 다음으로 목록을 넘깁니다. 
					mp3Player.getInstance().play(file);
					listFrm.removeList(0, true);
					listFrm.addList(data);
					break;
				default:	// 반복 재생이 아니거나, 한곡만 반복 재생인 경우 재생한 List를 삭제합니다.
					mp3Player.getInstance().play(file);
					listFrm.removeList(0, true);
					break;
				}
				playFrm.nextPlay();
			}
		}
	}
	
	/* 랜덤 재생 버튼 클릭 시 */
	public static class randomBtnAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			LinkedList<musicInfoBean> musicList = new LinkedList<musicInfoBean>();
			/* PLAY_LIST를 받아옵니다 */
			listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
			for (int i = 0; i < listFrm.getListSize(); i++) {
				musicList.add(listFrm.getList(i));
				listFrm.removeList(i, false);
			}
			Collections.shuffle(musicList);
			
			for(musicInfoBean data : musicList) {
				listFrm.addList(data);
			}
		}
	}	
	
	/* 사이클 버튼 클릭 Action */
	public static class cycleBtnAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JButton cycleBtn = (JButton) e.getSource();
			playFrame playFrm = (playFrame) mainFrame.getComponentByName("PLAY_PANEL");
			int cycle = playFrm.getCycleInfo();
			
			switch (cycle) {
			case 0:
				playFrm.setCycleInfo(1);
				cycleBtn.setText("1");
				break;
			case 1:
				playFrm.setCycleInfo(2);
				cycleBtn.setText("2");
				break;
			case 2:
				playFrm.setCycleInfo(3);
				cycleBtn.setText("3");
				break;
			case 3:
				playFrm.setCycleInfo(0);
				cycleBtn.setText("0");
				break;
			default:
				break;
			}
		}
	}

	/* 토글(재생) 버튼 Action */
	public static class toggleBtnAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JButton target = (JButton) e.getSource();
			mp3Player.getInstance().toggle();
			if (mp3Player.getInstance().getAllBytes() == null) {
				/* 재생 중인 음악이 없을 경우 PlayList에서 첫번째 항목을 받아서 재생합니다 */
				listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
				playFrame playFrm = (playFrame) mainFrame.getComponentByName("PLAY_PANEL");
				if(listFrm.getListSize() == 0) {
					optionFrame.alertMessageDialog("재생 목록이 없습니다.");
				} else {
					musicInfoBean data = listFrm.getList(0);
					int cycle = playFrm.getCycleInfo();
					switch (cycle) {
					case 1:
						File file = new File(data.getFilePath());
						mp3Player.getInstance().play(file);
						listFrm.removeList(0, true);
						break;
					default:
						playFrm.nextPlay();
						break;
					}
				}
			}
			if(mp3Player.getInstance().getIsRunning()) {
				target.setText("||");
			} else {
				target.setText(">");
			}
		}
	}
	
	/* timeLine Action */
	public static class timeLineAction extends MouseAdapter{
		/* 마우스 드래그가 행해지는 시점에 설정 변경 */
		@Override
		public void mousePressed(MouseEvent e) {
			JSlider timeLine = (JSlider) e.getSource();
			mp3Player.getInstance().setIsDragging(true);
			mp3Player.getInstance().setCurrentSeconds(timeLine.getValue());
		}
		/* 마우스가 떼어진 시점에 재생 위치 설정 */
		@Override
		public void mouseReleased(MouseEvent e) {
			JSlider timeLine = (JSlider) e.getSource();
			mp3Player.getInstance().setCurrentSeconds(timeLine.getValue());
			mp3Player.getInstance().setIsDragging(false);
		}
	}
	
	/* 볼륨 컨트롤에 대한 Action */
	public static class volumeAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JSlider volumeControl = (JSlider) e.getSource();
			mp3Player.getInstance().setVolumeDragging(true);
			mp3Player.getInstance().setVolume(volumeControl.getValue());
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			JSlider volumeControl = (JSlider) e.getSource();
			mp3Player.getInstance().setVolume(volumeControl.getValue());
			mp3Player.getInstance().setVolumeDragging(false);
		}
	}
	
	/* 음소버 버튼에 대한 Action */
	public static class muteAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			mp3Player.getInstance().setVolume(0);
		}
	}
	
	/* 음량 최대 버튼에 대한 Action */
	public static class speakerAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			mp3Player.getInstance().setVolume(100);
		}
	}
}
