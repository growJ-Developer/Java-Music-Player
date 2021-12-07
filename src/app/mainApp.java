package app;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.sun.tools.javac.launcher.Main;

import java.awt.*;

public class mainApp {
	                                                                     
	/* UI 전체에 대한 폰트를 설정합니다 */
	public static void changeAllSwingComponentDefaultFont() {
        try {
            UIDefaults swingComponentDefaultTable = UIManager.getDefaults();
            Enumeration allDefaultKey = swingComponentDefaultTable.keys();
            while(allDefaultKey.hasMoreElements()) {
                String defaultKey = allDefaultKey.nextElement().toString();
                if(defaultKey.indexOf("font") != -1) {
                	String fName = "resource/fonts/NanumGothic.ttf";
        			Font originFont = Font.createFont(Font.TRUETYPE_FONT, new File(fName));
                	Font newDefaultFont = originFont.deriveFont(12f);
                	//Font newDefaultFont = new Font("굴림", Font.PLAIN, 12);
                    UIManager.put(defaultKey, newDefaultFont);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		System.out.println("test");
		changeAllSwingComponentDefaultFont();
		mainFrame mainFrm = new mainFrame();
	}

}
