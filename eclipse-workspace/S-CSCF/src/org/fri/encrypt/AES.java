/**     
***************************************************************
* @ Doc name  :   AES.java     
* @ Include in:   org.fri.encrypt  
* @ Right by  :   copyright(c) 2014,Rights Reserved  
* @ Commpany  :   The first Research Institute
* @ Author    :    rhzhao 
* @ Version   :   v1.0  
* @ Date      :   2014-7-25 ����5:33:53  
***************************************************************
 */
package org.fri.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.fri.log.InitLog;
import org.fri.util.utilMethod;

/**  
 ***************************************************************   
 *@ Class          :   AES                                
 *@ Description    :                                              
 *@ Author         :                                              
 *@ Version        :   v1.0                                       
 *@ Date           :   2014-7-25     
 ******************************************************************  
 **/
public class AES {
	public static Logger alog = Logger.getRootLogger();//(AES.class);
	/**     
	 * create a instance  AES.          
	 */
	public AES() {
		// TODO Auto-generated constructor stub
		InitLog.initLog.info("[AES]: AES Initialized.");
	}
	
	  /** 
	 * 
	 *  
	 * @param content 
	 * @param password  
	 * @return 
	 */  
	public static byte[] encrypt(byte[] content, byte[] password) {  
	        try {             
	                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                kgen.init(128, new SecureRandom(password));  
	                SecretKey secretKey = kgen.generateKey();  
	                byte[] enCodeFormat = secretKey.getEncoded();  
	                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
	                Cipher cipher = Cipher.getInstance("AES");  
	                byte[] byteContent =  content;// content.getBytes("ISO-8859-1");  //ISO-8859-1 
	                cipher.init(Cipher.ENCRYPT_MODE, key);   
	                byte[] result = cipher.doFinal(byteContent);  
	                return result; 
	        } catch (NoSuchAlgorithmException e) {  
	        	utilMethod.logExceptions(e);  
	        } catch (NoSuchPaddingException e) {  
	        	utilMethod.logExceptions(e);  
	        } catch (InvalidKeyException e) {  
	        	utilMethod.logExceptions(e); 
	        } catch (IllegalBlockSizeException e) {  
	        	utilMethod.logExceptions(e);  
	        } catch (BadPaddingException e) {  
	        	utilMethod.logExceptions(e);  
	        }  
	        return null;  
	}  
	
	
	/** 
     * ���� 
     * 
     * @param content ��Ҫ���ܵ����� 
     * @param password  �������� 
     * @return 
     */  
    public static byte[] encrypt2(byte[] content, byte[] password) {  
            try {  
                    SecretKeySpec key = new SecretKeySpec(password, "AES");  
                    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");  
                    byte[] byteContent = content;  
                    cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��   
                    byte[] result = cipher.doFinal(byteContent);  
                    return result; // ����   
            } catch (NoSuchAlgorithmException e) {  
            	utilMethod.logExceptions(e);
            } catch (NoSuchPaddingException e) {  
            	utilMethod.logExceptions(e);
            } catch (InvalidKeyException e) {  
            	utilMethod.logExceptions(e);
            } catch (IllegalBlockSizeException e) {  
            	utilMethod.logExceptions(e);
            } catch (BadPaddingException e) {  
            	utilMethod.logExceptions(e); 
            }  
            return null;  
    }  
	
	/**
	 * @param content  
	 * @param password 
	 * @return 
	 */  
	public static byte[] decrypt(byte[] content, String password) {  
	        try {  
	                 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                 kgen.init(128, new SecureRandom(password.getBytes()));  
	                 SecretKey secretKey = kgen.generateKey();  
	                 byte[] enCodeFormat = secretKey.getEncoded();  
	                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
	                 Cipher cipher = Cipher.getInstance("AES");
	                cipher.init(Cipher.DECRYPT_MODE, key);// 
	                byte[] result = cipher.doFinal(content);  
	                return result; // 
	        } catch (NoSuchAlgorithmException e) {  
	        	utilMethod.logExceptions(e);
	        } catch (NoSuchPaddingException e) {  
	        	utilMethod.logExceptions(e);
	        } catch (InvalidKeyException e) {  
	        	utilMethod.logExceptions(e);
	        } catch (IllegalBlockSizeException e) {  
	        	utilMethod.logExceptions(e);
	        } catch (BadPaddingException e) {  
	        	utilMethod.logExceptions(e);
	        }  
	        return null;  
	}  

	

	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    @param buf
	 *@ Para in  ��    @return
	 *@ Return   ��
	 *@ Date     ��2014-10-17 ����11:30:58 by zhaoronghui
	 *@ Dscpt    ��
	 ********************************************************************* 
	 **/  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  	      
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  	              
	        }  	   
	        return sb.toString();  
	}  
	
	
	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    @param hexStr
	 *@ Para in  ��    @return
	 *@ Return   ��
	 *@ Date     ��2014-10-17 ����11:30:55 by zhaoronghui
	 *@ Dscpt    ��
	 ********************************************************************* 
	 **/  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}  

	
	
	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    @param content
	 *@ Para in  ��    @param password
	 *@ Para in  ��    @return
	 *@ Para in  ��    @throws UnsupportedEncodingException
	 *@ Return   ��
	 *@ Date     ��2014-10-17 ����8:35:13 by zhaoronghui
	 *@ Dscpt    ��
	 ********************************************************************* 
	 **/  
	public static byte[] AESEncrypt(byte[] content,byte[] password) throws UnsupportedEncodingException{	
		
	//	System.out.println("content length:"+content.length);
	//	System.out.println("password length:"+password.length);
		byte[] encryptResult = encrypt2(content, password); 
		//System.out.println("encryptResult.length:" + encryptResult.length);
		//String encryptResultStr =  parseByte2HexStr(encryptResult);//new String(encryptResult,"ISO-8859-1");
		//System.out.println("encryptResult.length:" + encryptResult.length);
		//System.out.println("encryptResultStr.length:" + encryptResultStr.length());  
		return encryptResult;
	}
	
	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    @param content
	 *@ Para in  ��    @param password
	 *@ Para in  ��    @return
	 *@ Para in  ��    @throws UnsupportedEncodingException
	 *@ Return   ��
	 *@ Date     ��2014-10-20 ����11:08:49 by zhaoronghui
	 *@ Dscpt    ��
	 ********************************************************************* 
	 **/  
	public static byte[] AESEncrypt4CheckBit(byte[] content,byte[] password) throws UnsupportedEncodingException{	
		//����   
		//System.out.println("����ǰ��" + content);  
		byte[] encryptResult = encrypt2(content, password); 
		//System.out.println("encryptResult.length:" + encryptResult.length);
		byte[] temp = new byte[6];
		/*
		 *  ex 6 bits
		 */
		for(int i=0;i<6;i++)
			temp[i] = encryptResult[i];		
		
		return temp;
	}
	
	
	public static void main(String args[]) throws UnsupportedEncodingException{
		//System.out.println(AESEncrypt("1234567812345678".getBytes(),"1234567812345678".getBytes()));
		  String str = "Hello world!";
		  // stringתbyte
		  byte[] bs = str.getBytes();
		  System.out.println(bs.length);
		  System.out.println(Arrays.toString(bs));
		  
		  // byteתstring
		  String str2 = new String(bs);
		  System.out.println(str2.length());
	}
	
	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    @param encryptResultStr
	 *@ Para in  ��    @param password
	 *@ Para in  ��    @return
	 *@ Return   ��
	 *@ Date     ��2014-10-20 ����7:57:26 by zhaoronghui
	 *@ Dscpt    ��
	 ********************************************************************* 
	 **/  
	public static String AESDecrypt(String encryptResultStr,String password){
		//����   
		byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);  
		byte[] decryptResult = decrypt(decryptFrom,password);  
		//System.out.println("���ܺ�" + new String(decryptResult));
		return new String(decryptResult);
	}

}
