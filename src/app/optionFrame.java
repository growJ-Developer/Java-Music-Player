package app;

import javax.swing.JOptionPane;

public class optionFrame {
	public static void alertMessageDialog(String message) {
		alertMessageDialog("알림", message, 1);
	}
	public static void alertMessageDialog(String message, int type) {
		alertMessageDialog("알림", message, type);
	}
	public static void alertMessageDialog(String title, String message) {
	 	alertMessageDialog(title, message, 1);
	}
	
	/* 알림창 Dialog를 출력합니다 */
	public static void alertMessageDialog(String title, String message, int type) {
	    /*
		-ERROR_MESSAGE = 0;
	    -INFORMATION_MESSAGE = 1;
	    -WARNING_MESSAGE = 2;
	    -QUESTION_MESSAGE = 3;
	    -PLAIN_MESSAGE = -1;
	    */
		centerFrame centerFrm = (centerFrame) mainFrame.getComponentByName("CENTER_PANEL");
		JOptionPane.showMessageDialog(centerFrm, message, title, type);	
	}
	
	public static String getUserMessageDialog(String title, String message, int type) {
		String result = JOptionPane.showInputDialog(null, message, title, type);
		
		return result;
	}
}
