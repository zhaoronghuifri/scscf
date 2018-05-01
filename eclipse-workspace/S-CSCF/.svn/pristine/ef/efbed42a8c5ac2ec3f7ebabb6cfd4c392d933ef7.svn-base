package org.fri.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * @see PingTest java ping���߲���
 * @author Herman.Xiong
 * @date 2014��5��9�� 14:43:01
 */
public class PingTest {

	// ping��ip
	private static final String HERMAN_IP = "112.126.77.54";

	/**
	 * @see ����һ ��õ� PING ����(Runtime)ֱ�ӵ��÷����������ping����
	 * @author Herman.Xiong
	 * @date 2014��5��9�� 14:46:33
	 * @version V2.0
	 * @since jdk 1.6 , tomcat 6.0
	 * @return void
	 */
	public static int  test0() {
		// ��ȡ��ǰ��������н�̶���
		Runtime runtime = Runtime.getRuntime();
		// �������������
		Process process = null;
		// ��������Ϣ
		String line = null;
		// ������
		InputStream is = null;
		// �ֽ���
		InputStreamReader isr = null;
		// ������
		BufferedReader br = null;
		// ���
		boolean res = false;
		try {
			// ִ��PING����
			process = runtime.exec("ping " + HERMAN_IP);
			// ʵ��������
			is = process.getInputStream();
			// ��������ת�����ֽ���
			isr = new InputStreamReader(is);
			// ���ֽ��ж�ȡ�ı�
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if (line.contains("TTL")) {
					res = true;
				}
			}
			is.close();
			isr.close();
			br.close();
			if (res) {
				//System.out.println("ping ͨ  ...");
				return 0;
			} else {
				//System.out.println("ping ��ͨ...");
				return -1;
			}
		} catch (IOException e) {
			e.printStackTrace();
			runtime.exit(1);
			return -1;
		}
	}

	/**
	 * @see JDK1.5 PING���·����������� ����Ϊ ��PING����˿�Ϊ7 �������վ��رղ���Ҫ�Ķ˿ڷ�ֹ����
	 * @author Herman.Xiong
	 * @date 2014��5��9�� 15:00:49
	 * @version V2.0
	 * @since jdk 1.6, tomcat 6.0
	 * @return
	 */
	public static boolean pingTest(String remoteHost) {
		InetAddress address;
		try {
			address = InetAddress.getByName(remoteHost);
			//System.out.println("Name: " + address.getHostName());
			//System.out.println("Addr: " + address.getHostAddress());
			boolean isReache = address.isReachable(2000); // �Ƿ���ͨ��
			return isReache;
			//System.out.println("Reach: " + isReache);
			// ����true��false
			//System.out.println(address.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		//test0();
		System.out.println(pingTest("210.12.165.100"));
	}
}
