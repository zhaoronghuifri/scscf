package org.fri.sms.module;

public class TestSMS {

	public TestSMS() {
		// TODO Auto-generated constructor stub
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 获取service
		SmsService service = new SmsServiceImpService().getSmsServiceImpPort();
		{			
			int result = service.sendmsg("18611068603", "你好");  
			System.err.println("SMS: " +result );			
		}
	}

}
