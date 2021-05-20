// Client.java
package exp5;

import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Client {
	// ���������½���¼����
	public static void main(String[] args) {
		new Login();
	}
}

/**
 *  ����ͻ��˵Ķ���д���Լ���¼�ͷ��͵ļ���
 *  ֮���԰ѵ�¼�ͷ��͵ļ��������������ΪҪ����һЩ���ݣ�����mySocket,textArea
 */
class ClientReadAndPrint extends Thread{
	static Socket mySocket = null;  // һ��Ҫ����static�������½��߳�ʱ�����
	static JTextField textInput;
	static JTextArea textShow;
	static JFrame chatViewJFrame;
	static BufferedReader in = null;
	static PrintWriter out = null;
	static String userName;
	
	// ���ڽ��մӷ���˷���������Ϣ
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));  // ������
			while (true) {
				String str = in.readLine();  // ��ȡ����˷��͵���Ϣ
				textShow.append(str + '\n');  // ��ӽ�����ͻ��˵��ı�����
				textShow.setCaretPosition(textShow.getDocument().getLength());  // ���ù�������������
			}
		} catch (Exception e) {}
	}
	
	/**********************��¼����(�ڲ���)**********************/
	class LoginListen implements ActionListener{
		JTextField textField;
		JPasswordField pwdField;
		JFrame loginJFrame;  // ��¼���ڱ���
		
		ChatView chatView = null;
		
		public void setJTextField(JTextField textField) {
			this.textField = textField;
		}
		public void setJPasswordField(JPasswordField pwdField) {
			this.pwdField = pwdField;
		}
		public void setJFrame(JFrame jFrame) {
			this.loginJFrame = jFrame;
		}
		public void actionPerformed(ActionEvent event) {
			userName = textField.getText();
			String userPwd = String.valueOf(pwdField.getPassword());  // getPassword�������char����
			if(userName.length() >= 1 && userPwd.equals("123")) {  // ����Ϊ123�����û������ȴ��ڵ���1
				chatView = new ChatView(userName);  // �½����촰��,�������촰�ڵ��û�������̬��
				// �����ͷ���������ϵ
				try {
					InetAddress addr = InetAddress.getByName(null);  // ��ȡ������ַ
					mySocket = new Socket(addr,8081);  // �ͻ����׽���
					loginJFrame.setVisible(false);  // ���ص�¼����
					out = new PrintWriter(mySocket.getOutputStream());  // �����
					out.println("�û���" + userName + "�����������ң�");  // �����û�����������
					out.flush();  // ��ջ�����out�е�����
				} catch (IOException e) {
					e.printStackTrace();
				}
				// �½���ͨ��д�̲߳�����
				ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
				readAndPrint.start();
				// �½��ļ���д�̲߳�����
				ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
				fileThread.start();
			}
			else {
				JOptionPane.showMessageDialog(loginJFrame, "�˺Ż�����������������룡", "��ʾ", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/**********************����������(�ڲ���)**********************/
	class ChatViewListen implements ActionListener{
		public void setJTextField(JTextField text) {
			textInput = text;  // �����ⲿ�࣬��Ϊ�����ط�ҲҪ�õ�
		}
		public void setJTextArea(JTextArea textArea) {
			textShow = textArea;  // �����ⲿ�࣬��Ϊ�����ط�ҲҪ�õ�
		}
		public void setChatViewJf(JFrame jFrame) {
			chatViewJFrame = jFrame;  // �����ⲿ�࣬��Ϊ�����ط�ҲҪ�õ�
			// ���ùر��������ļ���
			chatViewJFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					out.println("�û���" + userName + "���뿪�����ң�");
					out.flush();
					System.exit(0);
				}
			});
		}
		// ����ִ�к���
		public void actionPerformed(ActionEvent event) {
			try {
				String str = textInput.getText();
				// �ı�������Ϊ��
				if("".equals(str)) {
					textInput.grabFocus();  // ���ý��㣨���У�
					// ������Ϣ�Ի��򣨾�����Ϣ��
					JOptionPane.showMessageDialog(chatViewJFrame, "����Ϊ�գ����������룡", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}
				out.println(userName + "˵��" + str);  // ����������
				out.flush();  // ��ջ�����out�е�����
				
				textInput.setText("");  // ����ı���
				textInput.grabFocus();  // ���ý��㣨���У�
//				textInput.requestFocus(true);  // ���ý��㣨���У�
			} catch (Exception e) {}
		}
	}
}
