package org.fri.sms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.fri.cfg.rootCfg;

public class PhoneReminder {
	
	public static String SMS_CONTENT_FISRT = "[PMOS]: ";
	public static String SMS_CONTENT_LAST = "拨打过您的安全电话.";
	public static String SMS_CONTENT_TIME = "在MM月dd日HH时mm分";	
	
	private SMSService smsHandler;
	
	private static final PhoneReminder instance = new PhoneReminder(rootCfg.sms_vendor);
	////////////////////////////单例模式/////////////////////////////////////////
	public static PhoneReminder getInstance() {
		return instance;
	}

	public PhoneReminder(String vendor) {
		smsHandler = SMSFactory.getSMSService(vendor); //Unicom
	}
	
	/**
	 *********************************************************************
	 *@ Method   ：  
	 *@ Para in  ：    @param CallerNum
	 *@ Para in  ：    @param CalleeNum
	 *@ Para in  ：    @return
	 *@ Return   ：
	 *@ Date     ：   【 2015年5月28日 上午11:17:25】 by fri.com.cn
	 *@ Dscpt    ：
	 ********************************************************************* 
	 **/  
	public int sendSMS(String CallerNum, String CalleeNum) {
		if (CallerNum == null || CallerNum == null)
			return -1;

		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat(SMS_CONTENT_TIME);
		String reminderInfo = SMS_CONTENT_FISRT + CallerNum + dateformat.format(date) + SMS_CONTENT_LAST;
		// 发送短信
		if (smsHandler.sendSMS(CalleeNum, reminderInfo) != 0)
			return -4;
		
		return 0;
	}	
	
}
