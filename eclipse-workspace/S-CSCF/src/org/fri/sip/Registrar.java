package org.fri.sip;


import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

import org.apache.log4j.Logger;
import org.fri.cfg.Const;
import org.fri.cfg.rootCfg;
import org.fri.encrypt.Md532;
import org.fri.log.RegLog;
import org.fri.pdm.PdmKek;
import org.fri.pdm.PdmTek;
import org.fri.util.utilMethod;
import org.fri.ws.UeWs;

import net.sf.json.JSONObject;


/**
 * @author rhzhaoVSrv
 *
 */



public class Registrar extends SipServlet{	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *********************************************************************
	 *@throws Exception 
	 * @ Method  ��  
	 *@ Para in  ��    
	 *@ Return   ��
	 *@ Date     ��2014 2014-7-25 ����2:12:02  
	 *@ Othor    ��
	 ********************************************************************* 
	 */  
	public static Logger hss = Logger.getLogger(Registrar.class);


	public static void register_ok(SipServletRequest request) throws ServletParseException, IOException{
		RegLog.RegLog.info("[Registrar]: ----------------- REGISTER :");
		RegLog.RegLog.info(request);

		/*************************************************************************************
		 *   Usr Kek    = H( unq(username-value) ":" unq(realm-value) ":" passwd )":" unq(nonce-value) ":" unq(cnonce-value)
		 *  ******************************************************************************** */

		//AVVnyngSHvBktOkuN0KqAtuMT+Qr8VMqSEj8Wxzo5R4=
		String kek = "6ccdbac81fa21e31b95a2fe10ed81bbc";

		/*************************************************************************************
		 *   Usr Kek   = H( unq(username-value) ":" unq(realm-value) ":" passwd )":" unq(nonce-value) ":" unq(cnonce-value)
		 *  ******************************************************************************** */

		String contactStr = request.getHeader("Contact");

		if(contactStr == null)
			return;

		String ueid = utilMethod.parseUeID(contactStr);		

		/*
		 *     ----------------------------  deregister  --------------------------------
		 * 
		 */
		if(contactStr.contains("*")  
				|| request.getExpires() == 0
				||(request.getHeader("Expires") == null && contactStr.contains("expires=0"))){
			RegLog.RegLog.info("[Registrar]: -----------------  REGISTER (expire:0):");
			RegLog.RegLog.info(request);
			SipServletResponse ok = request.createResponse( SipServletResponse.SC_OK,"PMOS/VoIP/CSCF HSS Removed");	
			ok.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			ok.setHeader(Const.sip_header_SV, rootCfg.version);

			ok.send();
			RegLog.RegLog.info("[Registrar]: -----------------  200 OK  (expire:0):");
			RegLog.RegLog.info(ok);

			/*
			 *  UPDATE UE STATUS IN HSS
			 */						


			Address UsrDomain = request.getAddressHeader("Path");
			String domain="";

			if(UsrDomain!=null){
				domain = UsrDomain.getURI().toString();	
				if(domain.contains(";"))
					domain = domain.split(";")[0];			
			}else											
				domain = contactStr;
			int i = UeWs.ueInsert(ueid, new Date(),contactStr,	"OFF","NO",domain,"defalut");

			if(0 == i)
			{
				hss.info("["+ueid+"]-["+contactStr+"] -removed");		
			}else {
				hss.error("["+ueid+"]-["+contactStr+"] -removed");	
			}
			return;
		}
		/*
		 *     ----------------------------  register  --------------------------------
		 * 
		 */		

		if(contactStr.contains("<") && contactStr.contains(";"))
			contactStr = contactStr.split("<")[1].split(";")[0];		

		Address UsrDomain = request.getAddressHeader("Path"); 


		int i = 0;
		if(UsrDomain == null)
		{

			JSONObject PdmRes = PdmKek.CoreSrv2PDM("18911070385");							
			/*
			 * result_code = 0 ; HTTPS 200OK token!=null	
			 */
			if(PdmRes != null
					&& PdmRes.containsKey("result_code")
					&& PdmRes.getInt("result_code") == 0){						

				String UsrToken = PdmRes.getString("access_token"); 
				PdmKek.hlog.info("[Token]: <---TOKEN: [" +UsrToken+"]");
			}


			SipServletResponse ok = request.createResponse(200, "HSS/SAVED BUT NO PATH HEADER");	
			ok.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			ok.setHeader(Const.sip_header_SV, rootCfg.version);
			ok.send();

			RegLog.RegLog.info("[Registrar]: -----------------  200 OK  (expire:3600):");
			RegLog.RegLog.info(ok);


			/*
			 *     Insert to MySql DB
			 */
			i = UeWs.ueInsert(	ueid, new Date(),contactStr,"ON","NO",contactStr,kek);

			hss.info("["+ueid+"]-["+contactStr+"]");

		}else{
			String domain = UsrDomain.getURI().toString();	
			/*
			 *     Insert to MySql DB
			 */
			i = UeWs.ueInsert(ueid, new Date(),	contactStr,	"ON","NO",domain.toString(),kek);

		}


		if(0 == i)
		{
			hss.info("["+ueid+"]-["+contactStr+"]");
			/*
			 * create 200OK sip Resp
			 */
			SipServletResponse okResponse = request.createResponse( SipServletResponse.SC_OK,"PMOS/VoIP/CSCF HSS Saved");	
			okResponse.setAddressHeader("Contact", CoreSrv.sf.createAddress("sip:"+rootCfg.sip_host));
			okResponse.setHeader(Const.sip_header_SV, rootCfg.version);
			okResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			okResponse.send();			
			RegLog.RegLog.info("[Registrar]: ----------------- 200 OK :");
			RegLog.RegLog.info(okResponse);
		}else {
			hss.error("["+ueid+"]-["+contactStr+"]");	
			/*
			 * create 200OK sip Resp
			 */
			SipServletResponse err = request.createResponse( SipServletResponse.SC_SERVICE_UNAVAILABLE,"HSS Disconnected or Error");
			err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			err.setHeader(Const.sip_header_SV, rootCfg.version);

			err.setAddressHeader("Contact", CoreSrv.sf.createAddress("sip:"+rootCfg.sip_host));
			err.send();			
			RegLog.RegLog.info("[Registrar]: ----------------- 503 [HSS Disconnected or Error] :");
			RegLog.RegLog.info(err);
		}	
	}

	/**
	 * @param request
	 */
	public static void processRegister(SipServletRequest request)   {	
		try{
			String AuthInfo = request.getHeader("Authorization");	
			String contactStr = request.getAddressHeader("Contact").toString();
			{
				if(AuthInfo == null)	{
					{				
						try {
							RegLog.RegLog.info("[Registrar]: ----------------- 1st REGISTER:");
							RegLog.RegLog.info(request);
							//SipServletResponse.SC_UNAUTHORIZED
							SipServletResponse authResponse = request.createResponse(SipServletResponse.SC_UNAUTHORIZED);					
							authResponse.addHeader("WWW-Authenticate", createAuthHeader(rootCfg.realm ));	//			
							RegLog.RegLog.info("[Registrar]: ----------------- 401  UNAUTHORIZED:");							
							authResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							authResponse.setHeader(Const.sip_header_SV, rootCfg.version);

							authResponse.send();
							RegLog.RegLog.info(authResponse);

						} catch (IOException e) {
							// TODO Auto-generated catch block   SipServletResponse.SC_OK	
							utilMethod.logExceptions(e);
							SipServletResponse authResponse = request.createResponse(SipServletResponse.SC_SERVICE_UNAVAILABLE);
							authResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							authResponse.setHeader(Const.sip_header_SV, rootCfg.version);
							authResponse.send();
						}
					}
				}else{	

					String authStr = utilMethod.getAuthParams(AuthInfo);
					if(authStr == null) {
						try {
							RegLog.RegLog.info("[Registrar]: ----------------- 1st REGISTER:");
							RegLog.RegLog.info(request);
							//SipServletResponse.SC_UNAUTHORIZED
							SipServletResponse authResponse = request.createResponse(SipServletResponse.SC_UNAUTHORIZED);					
							authResponse.addHeader("WWW-Authenticate",  createAuthHeader(rootCfg.realm ));	//			
							RegLog.RegLog.info("[Registrar]: ----------------- 401  UNAUTHORIZED:");
							authResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							authResponse.setHeader(Const.sip_header_SV, rootCfg.version);
							authResponse.send();
							RegLog.RegLog.info(authResponse);							
							return;
						} catch (IOException e) {
							// TODO Auto-generated catch block   SipServletResponse.SC_OK						
							utilMethod.logExceptions(e);
							SipServletResponse authResponse = request.createResponse(SipServletResponse.SC_SERVICE_UNAVAILABLE);
							authResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							authResponse.setHeader(Const.sip_header_SV, rootCfg.version);
							authResponse.send();
							return;
						}
					}else {
						String ueid = utilMethod.parseUeID(contactStr);		
						if(ueid == null)
							ueid = utilMethod.parseUeID(request.getFrom().getURI().toString());
						/**********************************************
						 *                deregister 注销
						 ********************************************/
						if(contactStr.contains("*")  
								|| request.getExpires() == 0
								||(request.getHeader("Expires") == null && contactStr.contains("expires=0"))){
							RegLog.RegLog.info("[Registrar]: -----------------  REGISTER (expire:0):");
							RegLog.RegLog.info(request);
							SipServletResponse ok = request.createResponse( SipServletResponse.SC_OK,"PMOS/VoIP/CSCF HSS Removed");	
							ok.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							ok.setHeader(Const.sip_header_SV, rootCfg.version);

							ok.send();
							RegLog.RegLog.info("[Registrar]: -----------------  200 OK  (expire:0):");
							RegLog.RegLog.info(ok);

							/*
							 *  UPDATE UE STATUS IN HSS
							 */							

							Address UsrDomain = request.getAddressHeader("Path");
							String domain="";
							if(UsrDomain!=null){			
								domain = UsrDomain.getURI().toString();	
								if(domain.contains(";"))
									domain = domain.split(";")[0];			
							}else											
								domain = contactStr;		
							// special  yes no push
							int i = UeWs.ueInsert(ueid, new Date(),contactStr,	"OFF","NO",domain,"defalut");
							if(0 == i)
							{
								hss.info("["+ueid+"]-["+contactStr+"] -removed");		
							}else {
								hss.error("["+ueid+"]-["+contactStr+"] -removed");	
							}	
							return;
						}else{	
							/**********************************************
							 *                register
							 ********************************************/
							RegLog.RegLog.info("[Registrar]: ----------------- 2scd  REGISTER:");
							RegLog.RegLog.info(request);		
							String usrUriStr = ueid+ "@127.0.0.1";
							/*
							 *  if contact str is not *
							 */
							usrUriStr = utilMethod.getUsrContact(contactStr);	
							/**
							 *  Https to PDM 
							 */

							authStr= authStr.split("@")[0];
							AuthInfo = utilMethod.getAuthParams(AuthInfo).split("@")[1];							
							JSONObject PdmRes = PdmKek.CoreSrv2PDM(AuthInfo.split(";")[0]);							
							/*
							 * result_code = 0 ; HTTPS 200OK token!=null	
							 */
							if(PdmRes != null
									&& PdmRes.containsKey("result_code")
									&& PdmRes.getInt("result_code") == 0){						

								String UsrToken = PdmRes.getString("access_token"); 
								PdmKek.hlog.info("[Token]: <---TOKEN: [" +UsrToken+"]");

								if(UsrToken != null){	

									/* 200   OK  */

									if(ChkAuthInfo(request, UsrToken))
									{			

										/*
										 * create 200OK sip Resp
										 */
										SipServletResponse okResponse = request.createResponse( SipServletResponse.SC_OK,"PMOS/VoIP/CSCF HSS Saved");	
										String AuthInfoHeader = create200OKAuthInfoHeader(AuthInfo);
										okResponse.addHeader("Authorization-info", AuthInfoHeader);
										okResponse.setAddressHeader("Contact", CoreSrv.sf.createAddress("sip:"+rootCfg.sip_host));
										okResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
										okResponse.setHeader(Const.sip_header_SV, rootCfg.version);

										okResponse.send();			
										RegLog.RegLog.info("[Registrar]: ----------------- 200 OK :");
										RegLog.RegLog.info(okResponse);


										/*session kek*/
										/*************************************************************************************
										 *   Usr Kek    = H( unq(username-value) ":" unq(realm-value) ":" passwd )":" unq(nonce-value) ":" unq(cnonce-value)
										 *  ******************************************************************************** */
										String kek = UeWs.createUeKek(UsrToken, AuthInfo.split(";")[0]);
										/*************************************************************************************
										 *   Usr Kek   = H( unq(username-value) ":" unq(realm-value) ":" passwd )":" unq(nonce-value) ":" unq(cnonce-value)
										 *  ******************************************************************************** */

										if(contactStr.contains("<") && contactStr.contains(";"))
											contactStr = contactStr.split("<")[1].split(";")[0];		

										Address UsrDomain = request.getAddressHeader("Path");
										String domain="";

										if(UsrDomain!=null){									

											domain = UsrDomain.getURI().toString();		
											if(domain.contains(";"))
												domain = domain.split(";")[0];			
										}else											
											domain = contactStr;	

										//  PUSH 筛选 							
										// special  yes no push
										int i  = 0;
										if(request.getHeader("PushToken") != null)
											i= UeWs.ueInsert(ueid, new Date(),	contactStr,	"ON","PUSH",domain.toString(),kek);
										else 
											i= UeWs.ueInsert(ueid, new Date(),	contactStr,	"ON","ON",domain.toString(),kek);
										if(0 == i)
										{
											hss.info("["+ueid+"]-["+usrUriStr+"]");		
										}else {
											hss.error("["+ueid+"]-["+usrUriStr+"]");	
										}
									}
									else{							
										/*
										 *     Authentication is failed
										 */
										SipServletResponse authResponse = request.createResponse(SipServletResponse.SC_UNAUTHORIZED,rootCfg.AUTH_FAILED);
										authResponse.addHeader("WWW-Authenticate", createAuthHeader(rootCfg.realm ));		
										authResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
										authResponse.setHeader(Const.sip_header_SV, rootCfg.version);
										authResponse.send();		
										RegLog.RegLog.info("[Registrar]: ----------------- 2scd 401  UNAUTHORIZED:");
										RegLog.RegLog.info(authResponse);
									}
								}else{  
									/*
									 * UsrToken is null
									 */
									SipServletResponse authResponse = request.createResponse(SipServletResponse.SC_UNAUTHORIZED,rootCfg.UE_TOKEN_GET_EXCEPTION);
									authResponse.addHeader("WWW-Authenticate", createAuthHeader(rootCfg.realm ));	
									authResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
									authResponse.setHeader(Const.sip_header_SV, rootCfg.version);
									authResponse.send();	
									RegLog.RegLog.info("[Registrar]: ----------------- 401  UE_TOKEN_PDM_GET_EXCEPTION:");
									RegLog.RegLog.info(authResponse);

									if(request.getSession() != null && request.getSession().isValid()) {
										request.getSession().invalidate();
									}  
									if(request.getApplicationSession() != null && request.getApplicationSession().isValid()) {
										request.getApplicationSession().invalidate();
									}    
								}
							}else{
								/*
								 * result_code = 0 ; HTTPS 200OK token  ==  null	
								 */

								SipServletResponse err = request.createResponse(SipServletResponse.SC_UNAUTHORIZED,rootCfg.UE_TOKEN_GET_EXCEPTION);
								err.addHeader("WWW-Authenticate", createAuthHeader(rootCfg.realm ));	
								err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
								err.setHeader(Const.sip_header_SV, rootCfg.version);
								err.send();

								RegLog.RegLog.info("[Registrar]: ----------------- 401  UE_TOKEN_GET_EXCEPTION:");
								RegLog.RegLog.info(err);

								if(request.getSession() != null && request.getSession().isValid()) {
									request.getSession().invalidate();
								}  
								/*
								 *  
								 */
								if(request.getApplicationSession() != null && request.getApplicationSession().isValid()) {
									request.getApplicationSession().invalidate();
								}    						
							}
						}						
					}
				}
			}
		}catch (Exception e) {
			utilMethod.logExceptions(e);
		}
	}

	/**
	 *********************************************************************
	 *@throws Exception 
	 * @ Method   ��  
	 *@ Para in  ��    
	 *@ Return   ��
	 *@ Date     ��2014 2014-7-25 ����2:12:23  
	 *@ Othor    ��
	 ********************************************************************* 
	 */  
	public static String createAuthHeader(String realm) {
		String headerInfo = "Digest realm="+"\""+realm+"\"";

		headerInfo+=",nonce="+"\""+radomStr()+"\"";

		headerInfo+=",opaque="+"\""+radomStr()+"\"";

		if(rootCfg.md5_type.equals("MD5-sess"))
			headerInfo+=",algorithm=MD5-sess";
		else if(rootCfg.md5_type.equals("MD5"))
			headerInfo+=",algorithm=MD5";

		headerInfo+=",stale=false";
		headerInfo+=",qop=auth";//

		return headerInfo;		
	}

	/**
	 *********************************************************************
	 *@throws Exception 
	 * @ Method   ��  
	 *@ Para in  ��   username+":"+realm+":token:"+nonce+":"+cnonce+":"+nc+":"+qop+";"+url;	
	 *@ Return   ��
	 *@ Date     ��2014 2014-7-25 ����2:12:23  
	 *@ Othor    ��
	 ********************************************************************* 
	 */  
	public static String create200OKAuthInfoHeader(String AuthInfo) throws Exception{

		String headerInfo [] = AuthInfo.split(";");
		String newStr = headerInfo[0];		

		String A1 =newStr.split(":")[0]+":"+newStr.split("%")[1]+":"+newStr.split("%")[2];  		
		String A2 = ":"+headerInfo[1];	
		String HA1 =  Md532.GetMD5Code(A1);
		String HA2 =  Md532.GetMD5Code(A2);		
		//HA2 =  Coder.MD5Encrypt(HA1+":"+nonce+":"+nc+":"+cnonce+":"+qop+":"+ Coder.MD5Encrypt(":"+uri))
		AuthInfo =headerInfo[0];
		//System.err.println(AuthInfo.split(":")[6]);
		//System.err.println(HA1+":"+AuthInfo.split(":")[3]+":"+AuthInfo.split(":")[5]+":"+AuthInfo.split(":")[4]+":"+AuthInfo.split(":")[6]+":"+HA2);
		//System.err.println("createAuthInfoHeader:"+Registrar.checkResponse(clientRes, AuthInfo, UsrToken));
		String retStr = "";
		String respauth =   Md532.GetMD5Code(HA1+":"+AuthInfo.split("%")[3]+":"+AuthInfo.split("%")[5]+":"+AuthInfo.split("%")[4]+":"+AuthInfo.split("%")[6]+":"+HA2);		
		retStr+="rspauth="+"\""+respauth+"\"";
		//retStr+=",qop="+"\""+"auth"+"\"";
		return retStr;		
	}



	/**
	 * @param request
	 * @param pswd
	 * @return
	 * @throws Exception
	 */
	public static boolean ChkAuthInfo(SipServletRequest request,String pswd) throws Exception{
		//	logger.info("ChkAuthInfo()");
		try{
			String msg = request.getHeader("Authorization");		
			//	RegLog.RegLog.info("----------------- REGISTER Authorization:"+msg);
			String AuthInfoStr = msg.substring(7);

			/*
			 *  --------------------------------------------- MD5-Sess ----------------------------------------------------
			 */

			if(AuthInfoStr.contains("MD5-sess")){
				String []substr = AuthInfoStr.split(",");
				String username = null ;
				String realm = null;
				String nonce = null;
				String nc = null;
				String cnonce = null;
				String response = null;
				String uri = null;
				String qop = null;
				for(int i =0;i<substr.length;i++){			
					int indexequ = substr[i].indexOf("=");			

					if(substr[i].substring(0, indexequ).contains("username"))
					{
						username = substr[i].substring(indexequ+2,substr[i].length()-1);
						//System.err.println(username);				
					}else if (substr[i].substring(0, indexequ).contains("realm"))
					{
						realm = substr[i].substring(indexequ+2,substr[i].length()-1);
						//System.err.println(realm);				
					}else if(substr[i].substring(0, indexequ).contains("nonce")){
						if(substr[i].substring(0, indexequ).contains("cnonce"))
						{
							cnonce = substr[i].substring(indexequ+2,substr[i].length()-1);
							//System.err.println(cnonce);
						}else {
							nonce = substr[i].substring(indexequ+2,substr[i].length()-1);
							//System.err.println(nonce);
						}							
					}else if(substr[i].substring(0, indexequ).contains("nc")){
						if(substr[i].contains("\""))
						{					
							nc = substr[i].substring(indexequ+2,substr[i].length()-1);
							//System.err.println(nc );				
						}else 
						{
							nc = substr[i].substring(indexequ+1,substr[i].length());					
						}					
					}else if(substr[i].substring(0, indexequ).contains("response")){
						response = substr[i].substring(indexequ+2,substr[i].length()-1);
						//System.err.println(response );				
					}else if(substr[i].substring(0, indexequ).contains("uri")){
						uri = substr[i].substring(indexequ+2,substr[i].length()-1);
						//System.err.println(uri );
					}else if (substr[i].substring(0, indexequ).contains("qop")){
						if(substr[i].contains("\"")){
							qop = substr[i].substring(indexequ+2,substr[i].length()-1);									
						}else {
							qop = substr[i].substring(indexequ+1,substr[i].length());									
						}
						//System.err.println(qop );
					}else if(substr[i].substring(0, indexequ).contains("opaque")){
					}	
					continue;
				}		
				//System.err.println(msg);
				String A1 =username+":"+realm+":"+pswd;  
				A1= Md532.GetMD5Code(A1)+":"+nonce+":"+cnonce;
				String A2 = "REGISTER"+":"+uri;	
				String HA1 = Md532.GetMD5Code(A1);
				String HA2 = Md532.GetMD5Code(A2);		
				//	RegLog.RegLog.info("Md532.GetMD5Code(A1)+nonce+cnonce:"+A1 );
				//	RegLog.RegLog.info("-------------------------------A2:" +A2);
				////	RegLog.RegLog.info("------------------------------HA1:"+HA1 );
				//	RegLog.RegLog.info("------------------------------HA2:"+HA2 );
				//	RegLog.RegLog.info("inStr>>"+HA1+":"+nonce+":"+nc+":"+cnonce+":"+qop+":"+HA2 );
				String responseCheck =Md532.GetMD5Code(HA1+":"+nonce+":"+nc+":"+cnonce+":"+qop+":"+HA2);
				//	RegLog.RegLog.info("----------------- CSCF response :"+responseCheck);
				//	re.info("response:"+response);
				//	re.info("responseCheck:"+responseCheck);
				@SuppressWarnings("unused")
				String respauth = Md532.GetMD5Code(HA1+":"+nonce+":"+nc+":"+cnonce+":"+qop+":"+  Md532.GetMD5Code(":"+uri));
				//re.info("respauth:"+respauth);
				if(responseCheck.equalsIgnoreCase(response))		
				{
					A1 = "";
					A2 = "";
					responseCheck = "";
					return true;
				}else{
					A1 = "";
					A2 = "";
					responseCheck = "";
					return false;
				}
				/*
				 *  --------------------------------------------- MD5 ----------------------------------------------------
				 */
			}else{
				return true;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			utilMethod.logExceptions(e);
			return false;
		}
	}


	/**
	 *********************************************************************
	 *@throws Exception 
	 * @ Method   ��  
	 *@ Para in  ��    
	 *@ Return   ��
	 *@ Date     ��2014 2014-7-25 ����2:12:21  
	 *@ Othor    ��
	 ********************************************************************* 
	 */  
	public static String radomStr() { //length��ʾ����ַ�ĳ���

		Random rdm = new Random(System.currentTimeMillis());				
		String Rd = Integer.toString((Math.abs(rdm.nextInt())));
		String base = "abcdefghijklmnopqrstuvwxyz"+Rd;   //����ַ�Ӵ�������ȡ
		Random random = new Random();   
		StringBuffer sb = new StringBuffer();   
		for (int i = 0; i < 32; i++) {   
			int number = random.nextInt(base.length());   
			sb.append(base.charAt(number));   
		}   
		return(sb.toString());   
	} 

}
