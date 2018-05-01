package org.fri.ws;

import org.fri.encrypt.AES;
import org.fri.encrypt.Coder;
import org.fri.log.InvLog;
import org.fri.util.utilMethod;

public class TekWs {

	/*
	 *  TEK
	 */
	/**
	 * @param ids
	 * @param tek
	 * @return
	 */
	public static int tekInsert(String vmgwCallid,String caller, String callee){		
			return HssWs.tekInsert(vmgwCallid, caller, callee);		
	}
	/**
	 * @param ids
	 * @return
	 */
	public static int tekDelete(String callid){
		return HssWs.tekDelete(callid);
	}

	/**
	 * @param ids
	 * @return
	 */
	public static String tekSelect(String callid){
		return HssWs.tekSelect(callid);	
	}
	
		
	/**
	 * @param phoneNum
	 * @param callid
	 * @param UsrKek
	 * @return
	 */
	public static String createUeTek(String ueid,String vmgwCallid,String kek) {	
		try{	
			if(kek != null){ 
				// Encrypt CallTek by UsrKek
				String OriginalTek = HssWs.tekSelect(vmgwCallid).trim();

				if(OriginalTek!=null) {

					byte[] version = utilMethod.Tek_setVersionByte();
					byte[] time = utilMethod.Tek_setTimeStamp();
					byte[] phonenum = utilMethod.Tek_getPhoneNumIn5bytes(ueid);
					byte[] checkBits = new byte[6];

					/*
					 *    iv ---1
					 */

					byte[] iv =utilMethod.byteMerger(version, time);
					iv = utilMethod.byteMerger(iv,phonenum);
					byte[] iv0 = new byte[6];
					for(int i =0;i<6;i++)
						iv0[i] = 0x00;
					iv = utilMethod.byteMerger(iv,iv0);

					
					byte[] tek = hexStringToBytes(OriginalTek);
					/*
					 *  AES for check byte 6 bits  byte[]
					 */
					checkBits = AES.AESEncrypt4CheckBit(iv,tek);

					iv = new byte[16];

					/*
					 *   iv ----2       xor --> AES
					 */
					iv = utilMethod.byteMerger(version, time);
					//	System.err.println("merge 2 1 length:"+iv.length);
					iv = utilMethod.byteMerger(iv,phonenum);
					//	System.err.println("merge 2 2 length:"+iv.length);
					iv = utilMethod.byteMerger(iv,checkBits);
					//	System.err.println("merge 2 3 length:"+iv.length);
					/*	
					utilMethod.Tek_setVersionByte()+
					utilMethod.Tek_setTimeStamp()+
					utilMethod.Tek_getPhoneNumIn5bytes(phoneNum)+
					utilMethod.Tek_setCRCIn6Bytes(OriginalTek);
					 */

					//	System.out.println("iv:"+iv.length);
					//	System.out.println("OriginalTek:"+OriginalTek.length());		
					//  OriginalTek = utilMethod.Tek_XorTekStr(iv, OriginalTek);			
					//	System.out.println("UsrKey:"+UsrKek);

					byte[] tekByte = utilMethod.Tek_XorTekStr(iv, tek); 
					//	System.out.println("XOR:"+tekByte.length);	

					// System.err.println("tekByte xor length:"+tekByte.length);


					// UsrKek.getBytes("ISO-8859-1")
					//System.err.println("userkey:"+UsrKek);
					byte[] aes = AES.AESEncrypt(tekByte,AES.parseHexStr2Byte(kek));
					//	System.err.println("aes length:"+aes.length);
					//	System.out.println("AES byte 2 Str:"+AES.parseByte2HexStr(aes));
					//	System.out.println("AES.length:"+aes.length);	
					//String retStr = AES.parseByte2HexStr(iv)+AES.parseByte2HexStr(aes);

					iv = utilMethod.byteMerger(iv,aes);
					//	System.err.println("key length:"+iv.length);
					// Coder.Base64Encrypt  0AB060EF344E955064B998BE43BF96F0  AVREg/gSIz9GALnhJlz6kEkoSzXel61RoK9fziaTztQ=
					// System.out.println("Base64:"+e);	48102119106103620712655112-99-115126114
					//  0870CEFCCF5946D581784A74AFEEC579
					//	String de64 = Coder.Base64Decrypt(e);
					//  tlog.info("AES:"+de64);	
					//	String o = AES.AESDecrypt(de64, UsrKek);
					//tlog.info("tek:"+o);		//  01761B3CBC3F422F4960F672E1779F21 MDE3NjFCM0NCQzNGNDIyRjQ5NjBGNjcyRTE3NzlGMjE=
					String e = Coder.Base64Encrypt(iv).trim();
					//String retStr = AES.parseByte2HexStr(iv);//new String(iv,"UTF-8");//
					//tlog.info("sessionkey:"+iv.length);
					//tlog.info("retStr:"+retStr);  
					//tlog.error("Tek base64 String :"+e);
					return e;	
				}
				else return null;
			}else{
				//System.err.println("[TekBasic]: CreateUsrEncryptTek error. user kek is null. [UEID]: "+phoneNum);
				InvLog.InvLog.error("[TekBasic]: CreateUsrEncryptTek error. user kek is null. [UEID]: "+ueid);
				return null;
			}
		}catch (Exception e){

			utilMethod.logExceptions(e);
		}
		return null;
	}

	
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	 
	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	} 

}
