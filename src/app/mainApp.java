package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

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
                	//String fName = "resource/fonts/NanumGothic.ttf";
                	//URL url = mainApp.class.getResource("/resource/NanumGothic.ttf");
                	//File file = new File(url.toURI());
        			Font originFont = Font.createFont(Font.TRUETYPE_FONT, getResourceAsFile("NanumGothic.ttf"));
                	Font newDefaultFont = originFont.deriveFont(12f);
                	//Font newDefaultFont = new Font("굴림", Font.PLAIN, 12);
                    UIManager.put(defaultKey, newDefaultFont);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static File getResourceAsFile(String resourcePath) {
		try {
			InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
			if(in == null) {
				return null;
			}
			
			File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
			tempFile.deleteOnExit();
			
			try (FileOutputStream out = new FileOutputStream(tempFile)){
				//copy stream
				byte[] buffer = new byte[1024];
				int bytesRead;
				while((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			} 
			return tempFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		changeAllSwingComponentDefaultFont();
		mainFrame mainFrm = new mainFrame();
	}

}
