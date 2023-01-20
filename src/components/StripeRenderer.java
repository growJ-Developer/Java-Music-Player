package components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class StripeRenderer extends DefaultListCellRenderer{
	/* 리스트의 색상을 줄무늬로 설정합니다 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if(index % 2 == 0) {
			label.setBackground(new Color(250, 250, 250));
		} else {
			label.setBackground(new Color(255, 255, 255));
		}
		
		if(isSelected) {
			label.setForeground(Color.black);
		}
		
		return label;
	}

}
