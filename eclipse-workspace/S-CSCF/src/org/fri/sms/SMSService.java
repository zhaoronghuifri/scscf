package org.fri.sms;

public interface SMSService {
	public int getResponse();
	public int sendSMS(String phoneNum, String smsContent);
}
