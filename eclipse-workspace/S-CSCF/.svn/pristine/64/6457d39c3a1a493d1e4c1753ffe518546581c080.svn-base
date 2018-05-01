/**
 * 
 */
package org.fri.alerts;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Andy
 *
 */
public class PortUtils {

	/*** 
	 *  true:already in using  false:not using  
	 * @param port 
	 */  
	public static int isLoclePortUsing(int port){  
		int flag = -1;  
		try {  
			flag = isPortUsing("127.0.0.1", port);  
		} catch (Exception e) {  
		}  
		return flag;  
	}  
	/*** 
	 *  true:already in using  false:not using  
	 * @param host 
	 * @param port 
	 * @throws IOException 
	 */  
	public static int isPortUsing(String host,int port) throws IOException{  
		int flag = -1;  
		InetAddress theAddress = InetAddress.getByName(host);
		Socket socket = null;
		try {  
			socket = new Socket(theAddress,port);  
			flag = 0;  
			socket.close();
		} 
		catch (IOException e) { socket.close(); }  
		return flag;  
	}  
}
