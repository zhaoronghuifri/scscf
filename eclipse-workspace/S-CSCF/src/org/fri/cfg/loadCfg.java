package org.fri.cfg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.fri.log.InitLog;
import org.fri.util.utilMethod;

public class loadCfg extends HttpServlet implements HttpSessionListener,HttpSessionAttributeListener {	

	static Properties domain_property = new Properties();
	public static String cfgPath;
	/**
	 * TODO
	 */
	private static final long serialVersionUID = 1L;
	Properties cfg = new Properties();
	static Logger loadCfg = Logger.getRootLogger();//(loadProp.class);

	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    
	 *@ Return   ��
	 *@ Date     ��2015 ����10:36:30 by fri.com.cn
	 *@ Dscpt    ��
	 ********************************************************************* 
	 **/  
	public void load(){
		try {
			cfg.load(new FileInputStream(rootCfg.rootPath+"conf/cscf.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			InitLog.initLog.error("[LoadConfig]: Load SCSCf Config file error.  [ FAILED ]");
			utilMethod.logExceptions(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			InitLog.initLog.error("[LoadConfig]: Load SCSCf Config file error. [ FAILED ]");
			utilMethod.logExceptions(e);
		}		

		if(cfg.containsKey("client_id")
				&&cfg.containsKey("client_secret")
				&&cfg.containsKey("server_id")
				&&cfg.containsKey("client_secret")) {

			rootCfg.client_id        = cfg.getProperty("client_id");
			rootCfg.client_secret    = cfg.getProperty("client_secret");
			rootCfg.server_id        = cfg.getProperty("server_id");
			rootCfg.server_secret    = cfg.getProperty("server_secret");
		}


		/*
		 *   SIP Server
		 */
		if(cfg.containsKey("md5_type"))
			rootCfg.md5_type         = cfg.getProperty("md5_type");
		if(cfg.containsKey("auth"))
			rootCfg.auth             = Boolean.valueOf(cfg.getProperty("auth"));
		if(cfg.containsKey("authRealm"))
			rootCfg.realm             = cfg.getProperty("authRealm");		
		if(cfg.containsKey("sip_host"))
			rootCfg.sip_host       = cfg.getProperty("sip_host");	

		/*
		 *  HSS
		 */
		if(cfg.containsKey("Hsswsdl"))
			rootCfg.Hsswsdl         = cfg.getProperty("Hsswsdl");


		/*
		 *    VMGW
		 */

		if(cfg.containsKey("v_mgw_nat"))
		{
			// with ',' splitter
			rootCfg.mgw_nat = cfg.getProperty("v_mgw_nat").trim();			 
		}
		
		if(cfg.containsKey("v_mgw_nat_addr"))
		{
			// with ',' splitter
			rootCfg.v_mgw_nat_addr = cfg.getProperty("v_mgw_nat_addr").trim();			 
		}
		
		if(cfg.containsKey("v_mgw_addr"))
		{
			rootCfg.mgw_address = cfg.getProperty("v_mgw_addr");			 
		}
		if(cfg.containsKey("v_mgw_record"))
		{
			rootCfg.mgw_record = cfg.getProperty("v_mgw_record").trim();			 
		}

		/*
		 *   PDM and PDM KMC
		 */
		if(cfg.containsKey("pdm_url"))
			rootCfg.pdm_url            = cfg.getProperty("pdm_url");	
		if(cfg.containsKey("pdmkmc_url"))
			rootCfg.pdmkmc_url          = cfg.getProperty("pdmkmc_url");			
		/*
		 *  OMC		
		 */
		if(cfg.containsKey("omc_enable"))
			rootCfg.omc_enable          = cfg.getProperty("omc_enable").trim();	
		if(cfg.containsKey("omc_url"))
			rootCfg.omc_url          = cfg.getProperty("omc_url").trim();	

		/*
		 *    SMS  SETTINGS
		 */		
		if(cfg.containsKey("sms_switch"))
			rootCfg.sms_switch       = cfg.getProperty("sms_switch");
		if(cfg.containsKey("sms_way"))
			rootCfg.sms_way       = cfg.getProperty("sms_way");

		if(cfg.containsKey("sms_vendor"))
			rootCfg.sms_vendor       = cfg.getProperty("sms_vendor");

		if(cfg.containsKey("sms_module_url"))
			rootCfg.sms_module_url			 = cfg.getProperty("sms_module_url");

		/*
		 *    ALTERS_MAIL
		 */
		if(cfg.containsKey("alert_enable"))
			rootCfg.alert_enable			 = cfg.getProperty("alert_enable");
		if(cfg.containsKey("alert_mailsmtp"))
			rootCfg.alert_mailsmtp			 = cfg.getProperty("alert_mailsmtp");
		if(cfg.containsKey("alert_mailfrom"))
			rootCfg.alert_mailfrom			 = cfg.getProperty("alert_mailfrom");
		if(cfg.containsKey("alert_mailto"))
			rootCfg.alert_mailto			 = cfg.getProperty("alert_mailto");
		if(cfg.containsKey("alert_mailcopy"))
			rootCfg.alert_mailcopy			 = cfg.getProperty("alert_mailcopy");
		if(cfg.containsKey("alert_account"))
			rootCfg.alert_account			 = cfg.getProperty("alert_account");
		if(cfg.containsKey("alert_passwd"))
			rootCfg.alert_passwd			 = cfg.getProperty("alert_passwd");
		/*
		 *    SCSCF Version
		 */
		if(cfg.containsKey("version"))
			rootCfg.version			 = cfg.getProperty("version");
		cfg.clear();		
	}




	public void attributeAdded(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		load();
	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		load();
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		load();
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("���һ��session");

	}

}
