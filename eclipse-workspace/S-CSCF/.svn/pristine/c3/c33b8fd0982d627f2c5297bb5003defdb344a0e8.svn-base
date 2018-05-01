package org.fri.timer;


import java.util.Timer;
import java.util.TimerTask;

import org.fri.cfg.loadCfg;
import org.fri.log.InitLog;
import org.fri.util.utilMethod;

public class ReadCfg {

	private static Timer timer = new Timer();
	private static  int interval = 60 ;
	static loadCfg lp = new loadCfg();

	public static void start(){
		try {
			timer.schedule(new TimerTask(){
				public void run(){
					/*
					 * 
					 */
					lp.load();
				}}
			,interval*1000
			,interval*1000);
			InitLog.initLog.info("[LoadConfig]: Load SCSCf Config file and init parameters. [ OK ]");
		}catch (Exception e) {
			utilMethod.logExceptions(e);
			InitLog.initLog.error("[LoadConfig]: Load SCSCf Config file error. [ FAILED ]");		}
	}	
}
