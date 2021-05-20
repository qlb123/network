// ClientFileThread.java
package exp5;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientFileThread extends Thread{
	private Socket socket = null;
	private JFrame chatViewJFrame = null;
	static String userName = null;
	static PrintWriter out = null;  // ��ͨ��Ϣ�ķ��ͣ�Server.java������ֵ��
	static DataInputStream fileIn = null;
	static DataOutputStream fileOut = null;
	static DataInputStream fileReader = null;
	static DataOutputStream fileWriter = null;
	
	public ClientFileThread(String userName, JFrame chatViewJFrame, PrintWriter out) {
		ClientFileThread.userName = userName;
		this.chatViewJFrame = chatViewJFrame;
		ClientFileThread.out = out;
	}
	
	// �ͻ��˽����ļ�
	public void run() {
		try {
			InetAddress addr = InetAddress.getByName(null);  // ��ȡ������ַ
			socket = new Socket(addr, 8090);  // �ͻ����׽���
			fileIn = new DataInputStream(socket.getInputStream());  // ������
			fileOut = new DataOutputStream(socket.getOutputStream());  // �����
			// �����ļ�
			while(true) {
				String textName = fileIn.readUTF();
				long totleLength = fileIn.readLong();
				int result = JOptionPane.showConfirmDialog(chatViewJFrame, "�Ƿ���ܣ�", "��ʾ",
														   JOptionPane.YES_NO_OPTION);
				int length = -1;
				byte[] buff = new byte[1024];
				long curLength = 0;
				// ��ʾ��ѡ������0Ϊȷ����1λȡ��
				if(result == 0){
//					out.println("��" + userName + "ѡ���˽����ļ�����");
//					out.flush();
					File userFile = new File("C:\\Users\\����\\Desktop\\�����ļ�\\" + userName);
					if(!userFile.exists()) {  // �½���ǰ�û����ļ���
						userFile.mkdir();
					}
					File file = new File("C:\\Users\\����\\Desktop\\�����ļ�\\" + userName + "\\"+ textName);
					fileWriter = new DataOutputStream(new FileOutputStream(file));
					while((length = fileIn.read(buff)) > 0) {  // ���ļ�д������
						fileWriter.write(buff, 0, length);
						fileWriter.flush();
						curLength += length;
//						out.println("�����ս���:" + curLength/totleLength*100 + "%��");
//						out.flush();
						if(curLength == totleLength) {  // ǿ�ƽ���
							break;
						}
					}
					out.println("��" + userName + "�������ļ�����");
					out.flush();
					// ��ʾ�ļ���ŵ�ַ
					JOptionPane.showMessageDialog(chatViewJFrame, "�ļ���ŵ�ַ��\n" +
							"C:\\Users\\����\\Desktop\\�����ļ�\\" +
							userName + "\\" + textName, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				}
				else {  // �������ļ�
					while((length = fileIn.read(buff)) > 0) {
						curLength += length;
						if(curLength == totleLength) {  // ǿ�ƽ���
							break;
						}
					}
				}
				fileWriter.close();
			}
		} catch (Exception e) {}
	}
	
	// �ͻ��˷����ļ�
	static void outFileToServer(String path) {
		try {
			File file = new File(path);
			fileReader = new DataInputStream(new FileInputStream(file));
			fileOut.writeUTF(file.getName());  // �����ļ�����
			fileOut.flush();
			fileOut.writeLong(file.length());  // �����ļ�����
			fileOut.flush();
			int length = -1;
			byte[] buff = new byte[1024];
			while ((length = fileReader.read(buff)) > 0) {  // ��������
				
				fileOut.write(buff, 0, length);
				fileOut.flush();
			}
			
			out.println("��" + userName + "�ѳɹ������ļ�����");
			out.flush();
		} catch (Exception e) {}
	}
}
