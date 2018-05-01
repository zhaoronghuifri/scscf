package org.fri.sms;

public class SMSTest {
	public static void main(String[] args) throws Exception {
		//////////////////////////////////////////////////////////////////////
		////////Unicom 正常test///////////////	
		PhoneReminder reminder = PhoneReminder.getInstance();	
		long start = System.currentTimeMillis();
	//	while(System.currentTimeMillis() - start<60*1000)
		{
		//	Thread.sleep(3000);
			reminder.sendSMS("18611068603", "18911070385");
		}

		/*VerificationCodeManager test = VerificationCodeManager.getInstance();
		test.sendCode("13691437845", "FRI0000001");*/

		////////正常test///////////////
		//SMSHandler smsHandler = new SMSHandler();
		//smsHandler.getResponse();
		///////////////////////////////////////////////////////////////////////
		//VerificationCodeManager test = VerificationCodeManager.getInstance();

		//if (test.sendMessage("18511380975", "尊敬的用户您好，现在邀请您开启安全模式，实现对信息的全方位保护！") != 0)
		//if (test.sendMessage("13691284737", "尊敬的用户您好，现在邀请您开启安全模式，实现对信息的全方位保护！") != 0)	
		//if (test.sendMessage("18519070300", "尊敬的用户您好，现在邀请您开启安全模式，实现对信息的全方位保护吧！") != 0)
		//if (test.sendMessage("18911876374", "尊敬的用户您好，现在邀请您开启安全模式，实现对信息的全方位保护！") != 0)
		//if (test.sendMessage("13146469129", "尊敬的用户您好，现在邀请您开启安全模式，实现对信息的全方位保护！") != 0)
		//if (test.sendMessage("13691437845", "尊敬的用户您好，现在邀请您开启安全模式，实现对信息的全方位保护！") != 0)
		//if (test.sendMessage("18501920506", "尊敬的用户您好，现在邀请您开启安全模式，实现对信息的全方位保护！") != 0)
		//{
		//	System.out.println("1-2. test error, 向正确号码发送短信错误. num = " + "18601101089");
		//	return;
		//}

		//String tmpNum = "13146469129";
		//		String tmpNum7 = "18601101089";
		//		String tmpNum1 = "15311438629";
		//		String tmpNum2 = "13146468671";
		//		String tmpNum10 = "13691437845";
		//		String tmpNum11 = "13811091903";
		//		String tmpNum3 = "13691502027";
		//		String tmpNum5 = "13651164338";
		//		String tmpNum4 = "18513019392";
		//		String tmpNum6 = "18618415631";
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) != 0)
		//			System.out.println("1-1. test ok, 号码尚未发送code时，code验证错误. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("1-1. test error, 号码尚未发送code时，code验证ok. num = " + tmpNum);
		//			return;
		//		}
		//		
		//if (test.sendCode(tmpNum, "FRI0000001") != 0)
		//{
		//	System.out.println("1-2. test error, 向正确号码发送code，code插入错误. num = " + tmpNum);
		//	return;
		//}
		//else
		//	System.out.println("1-2. test ok, 向正确号码发送code，code插入ok. num = " + tmpNum);
		//======================================================================================

		//		if (test.sendCode(tmpNum, "FRI0000001") != 0)
		//			System.out.println("1-2-1. test ok, 向正确号码短时间重复发送code，code插入错误. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("1-2-1. test error, 向正确号码短时间重复发送code，code插入ok. num = " + tmpNum);
		//			return;
		//		}
		//		
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) != 0)
		//		{
		//			System.out.println("1-3. test error, 正确号码已经发送code后，code验证错误. num = " + tmpNum);
		//			return;
		//		}
		//		else
		//			System.out.println("1-3. test ok, 正确号码已经发送code后，code验证ok. num = " + tmpNum);
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) != 0)
		//		{
		//			System.out.println("1-4. test error, 正确号码已经发送code后，第二次认证code，code验证错误. num = " + tmpNum);
		//			return;
		//		}
		//		else
		//			System.out.println("1-4. test ok, 正确号码已经发送code后，第二次认证code，code验证ok. num = " + tmpNum);
		//		
		//		//////测试周期清除///////////
		//		//test.cleanbyPeriod();
		//		Thread.sleep(TIME_INTERVAL_RESEND*2/3);		
		//		if (test.sendCode(tmpNum2, "FRI0000001") != 0)
		//			System.out.println("2-2-1. test ok, 向正确号码2，加密卡1等待一段时间重复发送code，code插入错误. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("2-2-1. test error, 向正确号码2，加密卡1时间等待一段时间重复发送code，code插入ok. num = " + tmpNum);
		//			return;
		//		}
		//		Thread.sleep(TIME_INTERVAL_RESEND*2/3);		
		//		if (test.sendCode(tmpNum2, "FRI0000001") == 0)
		//			System.out.println("3-2-1. test ok, 向正确号码2，加密卡1 等待二段时间重复发送code，code插入ok. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("3-2-1. test error, 向正确号码2，加密卡1等待二段时间重复发送code，code插入错误. num = " + tmpNum);
		//			return;
		//		}
		//		if (test.sendCode(tmpNum, "FRI0000002") == 0)
		//			System.out.println("4-2-1. test ok, 向正确号码，加密卡2等待二段时间发送code，code插入ok. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("4-2-1. test error, 向正确号码，加密卡2等待二段时间发送code，code插入错误. num = " + tmpNum);
		//			return;
		//		}
		//		
		//////////////////////////////////////////////////
		/////// 功能测试！！！
		////////////////////////////////////////////////////////////////////				
		//		if (test.sendCode(tmpNum, "FRI0000001") != 0)
		//		{
		//			System.out.println("2-1. test error, 向正确号码发送code，第二次，code插入错误. num = " + tmpNum);
		//			return;
		//		}
		//		else
		//			System.out.println("2-1. test ok, 向正确号码发送code，第二次，code插入ok. num = " + tmpNum);
		//		
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) != 0)
		//		{
		//			System.out.println("2-2. test error, 正确号码已经第二次发送code后，认证code，code验证错误. num = " + tmpNum);
		//			return;
		//		}
		//		else
		//			System.out.println("2-2. test ok, 正确号码已经第二次发送code后，认证code，code验证ok. num = " + tmpNum);
		/////////////////////////////////////////////////////		
		//		tmpNum = "131";
		//		if (test.sendCode(tmpNum, "FRI0000001") != 0)
		//			System.out.println("3-1. test ok, 向错误号码发送code，code插入错误. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("3-1. test error, 向错误号码发送code，code插入ok. num = " + tmpNum);
		//			return;
		//		}
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) != 0)
		//			System.out.println("3-2. test ok, 向错误号码发送code后，code验证错误. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("3-2. test error,  向错误号码发送code后，code验证ok. num = " + tmpNum);
		//			return;
		//		}
		////////////////////////////////////////////////////////		
		////////////clean test		
		//////////////////////////////////////////
		//		for (int i = 0; i <100; i++)
		//		{
		//			String tmp = String.valueOf(i);
		//			String num = tmp;
		//			for (int j = 0; j < (8 - tmp.length()); j++)
		//				num = "0" + num;
		//			num = "13" + num;
		//			if (test.sendCode(num, "FRI0000001") != 0)
		//				System.out.println("code发送错误. num = " + num);		
		//		}
		//		///////////////////////////////////////////////////////////////////////////////		
		//		Thread.sleep(TIME_INTERVAL*2/3);
		//		test.cleanInvalidCode();
		//		
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) == 0)
		//			System.out.println("4-1. test ok, 向正确号码发送code, 等待一段时间后，code验证ok. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("4-1. test error,  向错误号码发送code后，等待一段时间后，code验证错误. num = " + tmpNum);
		//			return;
		//		}		
		//		tmpNum = "13146469128";
		//		if (test.sendCode(tmpNum, "FRI0000001") == 0)
		//			System.out.println("5-1. test ok, 向正确号码2发送code，code插入ok. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("5-1. test error, 向正确号码2发送code，code插入错误. num = " + tmpNum);
		//			return;
		//		}
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) == 0)
		//			System.out.println("6-2. test ok, 向正确号码2发送code后，code验证ok. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("6-2. test error,  向正确号码2发送code后，code验证错误. num = " + tmpNum);
		//			return;
		//		}
		//		
		//		Thread.sleep(TIME_INTERVAL*2/3);
		//		test.cleanInvalidCode();
		//		
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) == 0)
		//			System.out.println("7-2. test ok, 向正确号码2发送code后，等待一段时间后，code验证ok. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("7-2. test error,  向正确号码2发送code后，等待一段时间后，code验证错误. num = " + tmpNum);
		//			return;
		//		}
		//		
		//		tmpNum = "13146469129";
		//		if (test.verifyCode(tmpNum, test.getCode(tmpNum)) != 0)
		//			System.out.println("7-2. test ok, 向正确号码发送code后，等待超时后，code验证错误. num = " + tmpNum);
		//		else
		//		{
		//			System.out.println("7-2. test error,  向正确号码发送code后，等待超时后，code验证ok. num = " + tmpNum);
		//			return;
		//		}
		///////////////////////////////////////////////////////////////////////
		////////批量测试，慎重
		/////////////////////////////////////////////////////////////////////		
		//		long startTime=System.currentTimeMillis();
		//		for (int i = 0; i <1; i++)
		//		{
		//			String tmp = String.valueOf(i);
		//			String num = tmp;
		//			for (int j = 0; j < (8 - tmp.length()); j++)
		//				num = "0" + num;
		//			num = "13" + num;
		//			if (test.sendCode(num, "FRI0000001") != 0)
		//				System.out.println("code发送错误. num = " + num);		
		//		}
		//		long endTime=System.currentTimeMillis();
		//		
		//		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		//		
		//		
		//		long startTime1=System.currentTimeMillis();
		//		for (int i = 0; i <100000; i++)
		//		{
		//			String tmp = String.valueOf(i);
		//			String num = tmp;
		//			for (int j = 0; j < (8 - tmp.length()); j++)
		//				num = "0" + num;
		//			num = "13" + num;
		//			if (test.verifyCode(num, test.getCode(num)) != 0)
		//				System.out.println("code验证错误. num = " + num);		
		//		}
		//		long endTime1=System.currentTimeMillis();
		//		System.out.println("程序运行时间： "+(endTime1-startTime1)+"ms");
		//		
		//		Thread.sleep(3000);		
		//		for (int i = 0; i <10; i++)
		//		{
		//			String tmp = String.valueOf(i);
		//			String num = tmp;
		//			for (int j = 0; j < (8 - tmp.length()); j++)
		//				num = "0" + num;
		//			num = "13" + num;
		//			if (test.verifyCode(num, test.getCode(num)) != 0)
		//				System.out.println("code验证错误. num = " + num);
		//			else
		//				System.out.println("code验证成功. num = " + num);
		//		}
		//		
		//		Thread.sleep(3000);		
		//		for (int i = 0; i <10; i++)
		//		{
		//			String tmp = String.valueOf(i);
		//			String num = tmp;
		//			for (int j = 0; j < (8 - tmp.length()); j++)
		//				num = "0" + num;
		//			num = "13" + num;
		//			if (test.verifyCode(num, test.getCode(num)) != 0)
		//				System.out.println("code验证错误. num = " + num);
		//			else
		//				System.out.println("code验证成功. num = " + num);
		//		}
		//		
		//		Thread.sleep(3000);		
		//		for (int i = 0; i <10; i++)
		//		{
		//			String tmp = String.valueOf(i);
		//			String num = tmp;
		//			for (int j = 0; j < (8 - tmp.length()); j++)
		//				num = "0" + num;
		//			num = "13" + num;
		//			if (test.verifyCode(num, test.getCode(num)) != 0)
		//				System.out.println("code验证错误. num = " + num);
		//			else
		//				System.out.println("code验证成功. num = " + num);
		//		}
	}
}
