package org.fri.alerts;

import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.fri.cfg.rootCfg;
import org.fri.ws.HssWs;

public class OMCMonitor {

	final static Timer timer = new Timer();
	final static  int interval = 60 ;	

	/**
	 * 
	 */
	public static void start(){	
		try {
			timer.schedule(new TimerTask(){
				public void run(){	
					/*  ONLY SEND REPORT TO OMC WHEN omc enable*/
					if(rootCfg.omc_enable.equalsIgnoreCase("0")){	
						/*
						 *  perodic check HSS service
						 */
						if(0 != checkHssService())
						{
							if(0!= checkHssService())											
							{
								ServiceStatus.hssStatus = -1;
								Report.setReportContent(Report.LEVEL_FATAL, Report.TYPE_NETWORK, "488", "SCSCF-HSS-EXCEPTION");
							//	System.err.println("-------ServiceStatus.hssStatus = -1");

							}								
						}else
						{
							//System.err.println("-------ServiceStatus.hssStatus = 0");
							ServiceStatus.hssStatus = 0;
						}

						/*
						 *  perodic check PDM service
						 */
						if(!checkPdm()){
							if(!checkPdm()){
								ServiceStatus.PdmStatus = -1;
								Report.setReportContent(Report.LEVEL_FATAL, Report.TYPE_NETWORK, "488", "SCSCF-ACC-EXCEPTION");
							//	System.err.println("-------ServiceStatus.PdmStatus = -1");
							}
						}else
						{
							ServiceStatus.PdmStatus = 0;
							//System.err.println("-------ServiceStatus.PdmStatus = 0");
						}

						/*
						 *  perodic check VMGW service
						 */
						if(!checkVmgw()){	
							if(!checkVmgw()){
								ServiceStatus.vmgwStatus = -1;
								Report.setReportContent(Report.LEVEL_FATAL, Report.TYPE_NETWORK, "488", "SCSCF-VMGW-EXCEPTION");	
							//	System.err.println("-------ServiceStatus.vmgwStatus =-1");

							}
						}else
						{
							ServiceStatus.vmgwStatus = 0;
						//	System.err.println("-------ServiceStatus.vmgwStatus =0");

						}
						/*
						 *    send report 
						 */
						Report.sendReport();
					}
				}},180000,interval*1000);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}



	/*
	 *   PDM  https 
	 */

	public static boolean checkPdm()  {

		String remote  = null;
		if(rootCfg.pdm_url.contains("https://"))
			remote = rootCfg.pdm_url.replace("https://", "").replace("/auth", "").trim();
		if(rootCfg.pdm_url.contains("http://"))
			remote = rootCfg.pdm_url.replace("http://", "").replace("/auth", "").trim();
		String addr = null;		
		int port = 0;
		if(remote.contains(":"))
		{
			addr = remote.split(":")[0].trim();
			port = Integer.parseInt(remote.split(":")[1].trim());
		}else
			addr = remote.trim();

		//return PingTest.pingTest(addr);

			
		try{
			//https://123.56.91.170:5982/auth

			Socket socket = new Socket(addr,port);
			socket.setSoTimeout(3000);
			socket.close();
			return true;
		}catch(Exception e){
			//若出现异常，则判定端口未开启
			return false;
		}
	}

	/*
	 *   VMGW
	 */
	public static boolean checkVmgw(){
      return true;
		//return PingTest.pingTest(rootCfg.mgw_address);
		/*try{
			Socket socket = new Socket((rootCfg.mgw_address),5060);
			socket.setSoTimeout(3000);
			socket.close();
			return true;
		}catch(Exception e){
			//若出现异常，则判定端口未开启
			e.printStackTrace();
			return false;
		}*/
	}

	public static void main(String args[]){
		System.out.println(checkVmgw());
	}

	/*
	 *   HSS webservice
	 */
	public static int checkHssService(){
		//String wsdl = "http://192.168.0.72:8080/HssServer/services/ScscfWS";
		HssWs.init(rootCfg.Hsswsdl);			
		int ret1= HssWs.ueInsert("heartbeat", new Date(),"sip:127.0.0.1","OFF","NO","sip:localhost","sdfghjkdfhjkfghj");
		int ret2 =HssWs.ueDelete("heartbeat");
		if(ret1!=0 ||ret2 !=0)
			return -1;
		return 0;
	}


}
