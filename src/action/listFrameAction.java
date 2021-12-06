package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.listFrame;
import app.mainFrame;
import app.playFrame;
import bean.musicInfoBean;
import factory.mp3Player;
import model.playListModel;

public class listFrameAction {
	/* 플레이리스트에 디스크로부터 파일을 선택하여 부여하는 기능을 수행합니다. */
	public static class playListFromDiskListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Media Fules(mp3, wav)", "mp3", "wav");
			chooser.setFileFilter(filter);
			chooser.setMultiSelectionEnabled(true);
			chooser.showOpenDialog((JButton)e.getSource());
			File[] files = chooser.getSelectedFiles();
			
			listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
			listFrm.addList(files);
		}
	}
	
	/* playList에 대한 MouseEvent를 수행합니다 */
	public static class playListActionListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			JList<playListModel> list = (JList<playListModel>)e.getSource();
			/* 더블클릭을 진행했을 경우, 해당 위치를 받아 음악 재생을 진행합니다. */
			if (e.getClickCount() == 2) {
				int location = list.locationToIndex(e.getPoint());						// 선택 요소 항목 계산
				
				listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
				if(location <= listFrm.getListSize()) {
					musicInfoBean data = listFrm.getList(location);
					/* 선택한 음악을 재생합니다 */
					mp3Player.getInstance().play(data.getFilePath());		
					
					/* 사이클과 무관하게 음악 재생 시, 재생을 요청한 앞의 list를 모두 뒤로 이동시킵니다. */
					for(int i = 0; i <= location; i++) {
						musicInfoBean listData = listFrm.getList(i);
						listFrm.removeList(i, false);
						listFrm.addList(listData);
					}
				}
			}
		}
	}
}
