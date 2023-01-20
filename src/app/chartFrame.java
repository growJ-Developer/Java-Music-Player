package app;

import javax.swing.JPanel;

import bean.musicInfoBean;
import components.StripeRenderer;
import factory.serviceDAO;
import model.playListModel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JList;

public class chartFrame extends JPanel{
	private static playListModel chartModel;
	
	public chartFrame() {
		setBackground(Color.WHITE);
		
		JPanel chartPanel = new JPanel();
		chartPanel.setBackground(Color.WHITE);
		
		chartModel = new playListModel();
		JList<playListModel> chartList = new JList<playListModel>(chartModel);
		chartList.setFixedCellHeight(25);
		chartList.setFixedCellWidth(480);
		chartList.setCellRenderer(new StripeRenderer());
		setChartList();
		
		chartPanel.add(chartList);
		this.add(chartPanel);
	}
	
	/* 음악 차트를 DB로 부터 불러와서 Model에 설정합니다 */
	public static void setChartList() {
		removeAllChartList();
		
		serviceDAO dao = new serviceDAO();
		ArrayList<musicInfoBean> data = dao.getChartList();
		
		Iterator<musicInfoBean> it = data.iterator();
		while(it.hasNext()) {
			musicInfoBean bean = it.next();
			chartModel.addElement(bean);
		}
	}
	
	/* 음악 차트의 모두를 삭제합니다 */
	public static void removeAllChartList() {
		chartModel.removeAllElements();
	}

}
