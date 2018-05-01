package org.fri.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class RunShell {

	public RunShell() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param shell shell
	 */
	public static void execShell(String shell){
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec(shell);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


/**
	 * ����shell
	 * 
	 * @param shStr
	 *           shell
	 * @return
	 * @throws IOException
	 */
	public static List<String> runShell(String shStr) throws Exception {
		List<String> strList = new ArrayList<String>();
		Process process;
		process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,null);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		process.waitFor();
		while ((line = input.readLine()) != null){
		    strList.add(line);
		}		
		return strList;
	}

	public static void main() {
		List<String> ret = null;
		try {
			ret = runShell("ps -ef|grep catalina");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ret);
	}

}
