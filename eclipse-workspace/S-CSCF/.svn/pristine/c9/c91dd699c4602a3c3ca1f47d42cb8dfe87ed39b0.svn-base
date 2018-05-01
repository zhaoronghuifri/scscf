package org.fri.sms;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class VerificationCodeManager {
	/////////////////////////////////////////
	public class CodeTime{
		public String code = "";
		public long time = 0;
	}
	//////////////清除Map功能Task//////////////////////////////	
	public class codeCleanTimerTask extends TimerTask{
		public void run() {
			VerificationCodeManager codeClean = VerificationCodeManager.getInstance();
			codeClean.cleanInvalidCode();
		}
	}
	///////////////////////类变量////////////////////////////
	private static long	TIME_ONE_DAY = 24 * 60 * 60 * 1000;//每天的ms数
	private static int	PERIOD_CLEAN_DAYS = 1;// 间隔为1天
	private static int	PERIOD_CLEAN_HOUR = 3;// 每天的3点开始
	private static long TIME_INTERVAL_INVALID = 300 * 1000; // ms = 5分钟
	private static long TIME_INTERVAL_RESEND = 30 * 1000; //ms
	private static String SMS_CONTENT = "（动态验证码，5分钟后失效），欢迎您使用警务专用手机安全模式！";
	
	private Map<String, CodeTime> codeMap;
	private Map<String, CodeTime> cidCodeMap;
	//private SMSHandler smsHandler;
	private SMSService smsHandler;
	
	private static final VerificationCodeManager instance = new VerificationCodeManager();
	////////////////////////////单例模式/////////////////////////////////////////
	public static VerificationCodeManager getInstance() {
		return instance;
	}

	private VerificationCodeManager() {
		codeMap = new ConcurrentHashMap<String, CodeTime>();
		cidCodeMap = new ConcurrentHashMap<String, CodeTime>();
		smsHandler = SMSFactory.getSMSService("Unicom");
	}
	
	
	public int sendMessage(String phoneNum, String smsContent) {
		if (phoneNum == null)
			return -1;
		// 发送短信
		if (smsHandler.sendSMS(phoneNum, smsContent) != 0)
			return -4;
		return 0;
	}
	
	///////////////////////类函数////////////////////////////
	/*
	 * 函数功能：发送短信验证码
	 * 返回值说明:
	 * 0	发送验证码成功
	 * -1	参数错误为null
	 * -2	向同一个号码发送短信过快
	 * -3	向同一个加密卡发送短信过快
	 * -4	发送短信失败
	 */
	public int sendCode(String phoneNum, String cid) {
		if (phoneNum == null || cid == null)
			return -1;
		// 判断该手机号是否刚发送过验证码
		CodeTime codeTimePair = codeMap.get(phoneNum);
		long curTime = System.currentTimeMillis();
		if (codeTimePair != null) 
		{
			if ((curTime - codeTimePair.time) < TIME_INTERVAL_RESEND)
				return -2;
		}
		else
		{
			codeTimePair = new CodeTime();
		}
		
		// 判断该卡是否刚刚发送过验证码
		CodeTime cidPair = cidCodeMap.get(cid);
		if (cidPair != null)
		{
			if ((curTime - cidPair.time) < TIME_INTERVAL_RESEND)
				return -3;
		}
		
		// 生成验证码
		Random random = new Random();
		String verificationCode = "";
		for (int i = 0; i < 8; i++) {
			String rand = String.valueOf(random.nextInt(10));
			verificationCode += rand;
		}
		// 发送短信
		if (smsHandler.sendSMS(phoneNum, verificationCode + SMS_CONTENT) != 0)
			return -4;
		//保存短信验证码
		codeTimePair.code = verificationCode;
		codeTimePair.time = curTime;
		codeMap.put(phoneNum, codeTimePair);
		cidCodeMap.put(cid, codeTimePair);
		return 0;
	}
	/*
	 * 函数功能：验证短信验证码是否有效
	 * 返回值说明:
	 * 0	验证码验证成功
	 * -1	参数错误为null
	 * -2	该号码发的验证码不存在、不正确或已经过期
	 */
	public int verifyCode(String phoneNum, String verificationCode) {
		if (phoneNum == null || verificationCode == null)
			return -1;
		CodeTime codeTimePair = codeMap.get(phoneNum);
		if (codeTimePair == null)
			return -2;
		if (!codeTimePair.code.equals(verificationCode))
			return -2;
		if ((System.currentTimeMillis() - codeTimePair.time) > TIME_INTERVAL_INVALID)
			return -2;
		
		return 0;
	}
	/*
	 * 函数功能：清除失效短信验证码
	 * 返回值说明:
	 * 0	成功
	 */	
	public int cleanInvalidCode()
	{
		//System.out.println("cleanInvalidCode");
		long curTime = System.currentTimeMillis();
		Iterator<Entry<String, CodeTime>> it = codeMap.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, CodeTime> entry = it.next();
            //System.out.println("have " + entry.getKey());
            CodeTime codeTimePair = entry.getValue();
            if ((curTime - codeTimePair.time) > TIME_INTERVAL_INVALID){  
            	//System.out.println("remove " + entry.getKey());
                it.remove();
            }
        }
        
		Iterator<Entry<String, CodeTime>> itCid = cidCodeMap.entrySet().iterator();
        while (itCid.hasNext())
        {
            Entry<String, CodeTime> entry = itCid.next();
            CodeTime codeTimePair = entry.getValue();
            if ((curTime - codeTimePair.time) > TIME_INTERVAL_INVALID){  
            	itCid.remove();
            }
        }
		return 0;
	}
	/*
	 * 函数功能：周期清理过期短信验证码
	 * 返回值说明:
	 * 0	成功
	 */
	public int cleanbyPeriod() {
		return cleanbyPeriod(PERIOD_CLEAN_DAYS, PERIOD_CLEAN_HOUR);
	}
	
	public int cleanbyPeriod(int periodDays, int startHour) {
		Calendar calendar = Calendar.getInstance();
		// 定制每日2:00执行方法
		calendar.set(Calendar.HOUR_OF_DAY, startHour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime(); // 第一次执行定时任务的时间
		// 如果第一次执行定时任务的时间 小于 当前的时间
		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		if (date.before(new Date())) {
			Calendar startDT = Calendar.getInstance();
			startDT.setTime(date);
			startDT.add(Calendar.DAY_OF_MONTH, periodDays);
			date = startDT.getTime();
		}
		Timer timer = new Timer();
		codeCleanTimerTask task = new codeCleanTimerTask();
		// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, date, TIME_ONE_DAY * periodDays);
		return 0;
	}
	/*
	 * 函数功能：获取短信验证码
	 * 返回值说明:
	 * 0	成功
	 */		
	public String getCode(String phoneNum) {
		CodeTime codeTimePair = codeMap.get(phoneNum);
		if (codeTimePair == null) 
			return null;
		return codeTimePair.code;
	}
}


