// MultiChat.java
package exp5;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MultiChat {
	JTextArea textArea;
	
	// �������ı����������Ϣ
	void setTextArea(String str) {
		textArea.append(str+'\n');
		textArea.setCaretPosition(textArea.getDocument().getLength());  // ���ù�������������
	}
	
	// ���캯��
	public MultiChat() {
		init();
	}
	
	void init() {
		JFrame jf = new JFrame("��������");
		jf.setBounds(500,100,450,500);  // ���ô�������ʹ�С
		jf.setResizable(false);  // ����Ϊ��������
		
		JPanel jp = new JPanel();  // �½�����
		JLabel lable = new JLabel("==��ӭ��������==");
		textArea = new JTextArea(23, 38);  // �½��ı��������ó���
		textArea.setEditable(false);  // ����Ϊ�����޸�
		JScrollPane scroll = new JScrollPane(textArea);  // ���ù�����壨װ��textArea��
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  // ��ʾ��ֱ��
		jp.add(lable);
		jp.add(scroll);
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ���ùر�ͼ������
		jf.setVisible(true);  // ���ÿɼ�
	}
}