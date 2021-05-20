// Server.java
package exp5;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
	static ServerSocket server = null;
	static Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>();  // �洢�ͻ���
	
	public static void main(String[] args) {
		MultiChat multiChat = new MultiChat();  // �½�����ϵͳ����
		try {
			// �ڷ������˶Կͻ��˿����ļ�������߳�
			ServerFileThread serverFileThread = new ServerFileThread();
			serverFileThread.start();
			server = new ServerSocket(8081);  // ���������׽��֣�ֻ�ܽ���һ�Σ�
			// �ȴ����Ӳ�������Ӧ�߳�
			while (true) {
				socket = server.accept();  // �ȴ�����
				list.add(socket);  // ��ӵ�ǰ�ͻ��˵��б�
				// �ڷ������˶Կͻ��˿�����Ӧ���߳�
				ServerReadAndPrint readAndPrint = new ServerReadAndPrint(socket, multiChat);
				readAndPrint.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();  // �����쳣���ӡ���쳣��λ��
		}
	}
}

/**
 *  �������˶�д���߳�
 *  ���ڷ������˶�ȡ�ͻ��˵���Ϣ��������Ϣ���͸����пͻ���
 */
class ServerReadAndPrint extends Thread{
	Socket nowSocket = null;
	MultiChat multiChat = null;
	BufferedReader in =null;
	PrintWriter out = null;
	// ���캯��
	public ServerReadAndPrint(Socket s, MultiChat multiChat) {
		this.multiChat = multiChat;  // ��ȡ��������ϵͳ����
		this.nowSocket = s;  // ��ȡ��ǰ�ͻ���
	}
	
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));  // ������
			// ��ȡ�ͻ�����Ϣ������Ϣ���͸����пͻ���
			while (true) {
				String str = in.readLine();
				// ���͸����пͻ���
				for(Socket socket: Server.list) {
					out = new PrintWriter(socket.getOutputStream());  // ��ÿ���ͻ����½���Ӧ��socket�׽���
					if(socket == nowSocket) {  // ���͸���ǰ�ͻ���
						out.println("(��)" + str);
					}
					else {  // ���͸������ͻ���
						out.println(str);
					}
					out.flush();  // ���out�еĻ���
				}
				// �����Զ��庯�������ͼ�ν���
				multiChat.setTextArea(str);
			}
		} catch (Exception e) {
			Server.list.remove(nowSocket);  // �̹߳رգ��Ƴ���Ӧ�׽���
		}
	}
}
