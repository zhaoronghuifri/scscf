package org.fri.sms;

public class SMSFactory {
	public static SMSService getSMSService(String serviceName)
	{
		if (serviceName.equalsIgnoreCase("Baiwu")) 
			return new BaiwuSMS();
		else if (serviceName.equalsIgnoreCase("Unicom")) 
			return new UnicomSMS();

		return new BaiwuSMS(); // 默认
	}
}
