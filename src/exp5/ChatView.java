// ChatView.java
package exp5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ChatView {
	String userName;  //�ɿͻ��˵�¼ʱ����
	JTextField text;
	JTextArea textArea;
	ClientReadAndPrint.ChatViewListen listener;
	
	// ���캯��
	public ChatView(String userName) {
		this.userName = userName ;
		init();
	}
	// ��ʼ������
	void init() {
		JFrame jf = new JFrame("�ͻ���");
		jf.setBounds(500,200,400,330);  //��������ʹ�С
		jf.setResizable(false);  // ����Ϊ��������
		
		JPanel jp = new JPanel();
		JLabel lable = new JLabel("�û���" + userName);
		textArea = new JTextArea("***************��¼�ɹ�����ӭ�������������ң�****************\n",12, 35);
		textArea.setEditable(false);  // ����Ϊ�����޸�
		JScrollPane scroll = new JScrollPane(textArea);  // ���ù�����壨װ��textArea��
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // ��ʾ��ֱ��
		jp.add(lable);
		jp.add(scroll);
		
		text = new JTextField(20);
		JButton button = new JButton("����");
		JButton openFileBtn = new JButton("�����ļ�");
		jp.add(text);
		jp.add(button);
		jp.add(openFileBtn);
		
		// ���á����ļ�������
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileOpenDialog(jf);
			}
		});
		
		// ���á����͡�����
		listener = new ClientReadAndPrint().new ChatViewListen();
		listener.setJTextField(text);  // ����PoliceListen��ķ���
		listener.setJTextArea(textArea);
		listener.setChatViewJf(jf);
		text.addActionListener(listener);  // �ı�����Ӽ���
		button.addActionListener(listener);  // ��ť��Ӽ���
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // �������Ͻǹر�ͼ�������
		jf.setVisible(true);  // ���ÿɼ�
	}
	// �����ļ������ú���
	void showFileOpenDialog(JFrame parent) {
		// ����һ��Ĭ�ϵ��ļ�ѡ����
		JFileChooser fileChooser = new JFileChooser();
		// ����Ĭ����ʾ���ļ���
		fileChooser.setCurrentDirectory(new File("C:/Users/Samven/Desktop"));
		// ��ӿ��õ��ļ���������FileNameExtensionFilter �ĵ�һ������������, ��������Ҫ���˵��ļ���չ����
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
        // ����Ĭ��ʹ�õ��ļ���������FileNameExtensionFilter �ĵ�һ������������, ��������Ҫ���˵��ļ���չ�� �ɱ������
        fileChooser.setFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
		// ���ļ�ѡ����߳̽���������֪��ѡ��򱻹رգ�
		int result = fileChooser.showOpenDialog(parent);  // �Ի��򽫻ᾡ����ʾ�ڿ��� parent ������
		// ���ȷ��
		if(result == JFileChooser.APPROVE_OPTION) {
			// ��ȡ·��
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			ClientFileThread.outFileToServer(path);
		}
	}
}