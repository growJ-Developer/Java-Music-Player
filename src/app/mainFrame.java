package app;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import bean.*;

/* 메인 프레임 */
public class mainFrame extends JFrame{
	/* 재생한 기록의 PlayList를 저장합니다, 최대 50개의 데이터만 저장합니다 */
	public static ArrayList<musicInfoBean> prevList = new ArrayList<musicInfoBean>(50);
	/* 모든 Component에 대해 HashMap으로 저장합니다. Name이 지정되있을 경우 불러올 수 있는 Key로 사용이 가능합니다 */
	private static HashMap componentMap;
	
	public mainFrame() {
		setTitle("J Music Player");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			// 닫기 동작 시 프로그램 전체를 종료합니다.
		Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		setNorthPanel(container);
		setCenterPanel(container);
		setWestPanel(container);
		setEastPanel(container);
				
		createComponentMap();
		setSize(1024, 512);														// 기본 사이즈를 1024 X 512로 설정합니다.
		setVisible(true);
	}
	
	/* 상단 패널에 대해 설정합니다. */
	public void setNorthPanel(Container container) {
		playFrame playFrm = new playFrame();
		playFrm.setName("PLAY_PANEL");
		container.add(playFrm, BorderLayout.NORTH);
	}
	
	/* 중앙 패널에 대해 설정합니다. */
	public void setCenterPanel(Container container) {
		
	}
	
	/* 좌측 패널에 대해 설정합니다. */
	public void setWestPanel(Container container) {
		JPanel westPanel = new JPanel();
		
		/* 제어 버튼을 생성합니다. */
		JButton chartBtn = new JButton("Music Chart");				// Music Chart 버튼 
		JButton favoriteBtn = new JButton("Favorite Music");		// Favorite Music 버튼
		JButton usersBtn = new JButton("Users");						// Users 버튼
		
		/* 각 버튼에 대한 Action을 지정합니다. 버튼 클릭시, MainFrame의 Center에 알맞는 창을 출력합니다. */
		
		
		/* 버튼을 패널에 추가합니다. */
		westPanel.add(chartBtn);
		westPanel.add(favoriteBtn);
		westPanel.add(usersBtn);
		
		/* West Control Panel을 conatiner에 추가합니다 */
		container.add(westPanel, BorderLayout.WEST);
	}
	
	/* 우측 패널에 대해 설정합니다. */
	public void setEastPanel(Container container) {
		listFrame listFrm = new listFrame();
		listFrm.setName("LIST_PANEL");
		container.add(listFrm, BorderLayout.EAST);
	}
	
	/* 플레이리스트에 대한 테스트 데이터를 불러옵니다 */
	public ArrayList<musicInfoBean> getTestList(){
		ArrayList<musicInfoBean> testList = new ArrayList<musicInfoBean>();		
		
		for(int i = 0; i < 10; i++) {
			musicInfoBean bean = new musicInfoBean();
			bean.setFilePath("/Users/addps5012/Library/Mobile Documents/com~apple~CloudDocs/devOps/eclipse_java/JMusicPlayer/musicList/1.mp3");
			bean.setMusicName("test");
			testList.add(bean);
		}
		return testList;
	}
	
	public static Dimension getMainFrameSize() {
		return null;
	}
	
	private void createComponentMap() {
        componentMap = new HashMap<String,Component>();
        Component[] components = this.getContentPane().getComponents();
        for (int i=0; i < components.length; i++) {
                componentMap.put(components[i].getName(), components[i]);
                Component[] childComponents = ((Container) components[i]).getComponents();
                if(childComponents.length != 0) {
                	addComponentMap(childComponents);
                }
        }
	}
	
	private void addComponentMap(Component[] components) {
		for (int i=0; i < components.length; i++) {
            componentMap.put(components[i].getName(), components[i]);
            Component[] childComponents = ((Container) components[i]).getComponents();
            if(childComponents.length != 0) {
            	addComponentMap(childComponents);
            }
		}
	}

	public static Component getComponentByName(String name) {
	        if (componentMap.containsKey(name)) {
	                return (Component) componentMap.get(name);
	        }
	        else return null;
	}
}