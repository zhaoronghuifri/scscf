package org.fri.encrypt;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.fri.util.utilMethod;
/*
 * MD5 �㷨
*/
public class Md532 {
    
    // ȫ������
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public Md532() {
    }

    // ������ʽΪ���ָ��ַ�
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // ������ʽֻΪ����
    @SuppressWarnings("unused")
	private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    // ת���ֽ�����Ϊ16�����ִ�
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     *********************************************************************
     *@ Method   ：  
     *@ Para in  ：    @param strObj
     *@ Para in  ：    @return
     *@ Return   ：
     *@ Date     ：2014-10-21 上午9:51:09 by zhaoronghui
     *@ Dscpt    ：
     ********************************************************************* 
     **/  
    public static String AesMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
           
            resultString =byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
        	utilMethod.logExceptions(ex);
        }
        return resultString;
    }

    
    /**
     *********************************************************************
     *@ Method   ：  
     *@ Para in  ：    @param strObj
     *@ Para in  ：    @return
     *@ Return   ：
     *@ Date     ：2014-10-21 上午9:51:09 by zhaoronghui
     *@ Dscpt    ：
     ********************************************************************* 
     **/  
    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");           
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
        	utilMethod.logExceptions(ex);
        }
        
        return resultString;
    }

    @SuppressWarnings("static-access")
	public static void main(String[] args) {
        Md532 getMD5 = new Md532();
        System.out.println(getMD5.GetMD5Code("zhaoronghui"));
    }
}