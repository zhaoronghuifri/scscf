package org.fri.sip;


import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.sip.Address;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipApplicationSessionEvent;
import javax.servlet.sip.SipApplicationSessionListener;
import javax.servlet.sip.SipErrorEvent;
import javax.servlet.sip.SipErrorListener;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletContextEvent;
import javax.servlet.sip.SipServletListener;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSession.State;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.TooManyHopsException;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.fri.alerts.OMCMonitor;
import org.fri.alerts.ServiceStatus;
import org.fri.cfg.Const;
import org.fri.cfg.States;
import org.fri.cfg.loadCfg;
import org.fri.cfg.rootCfg;
import org.fri.log.InitLog;
import org.fri.log.InvLog;
import org.fri.mail.EmailAlert;
import org.fri.mail.SendEmail;
import org.fri.pdm.PdmKek;
import org.fri.sms.SMSAlert;
import org.fri.timer.ReadCfg;
import org.fri.util.utilMethod;
import org.fri.ws.CallLogWs;
import org.fri.ws.HssWs;
import org.fri.ws.TekWs;
import org.fri.ws.UeWs;

import gov.nist.javax.sdp.fields.Email;


/** 
 * @ClassName	: CoreSrv 
 * @Description  : TODO(这里用一句话描述这个类的作用) 
 * @author       : ronghui_zhao@163.com
 * @date         : 2015年7月24日 上午8:37:04  
 */
public class CoreSrv extends SipServlet implements SipServletListener, SipErrorListener,TimerListener, SipApplicationSessionListener { 
	/**     
	 *     
	 */      
	public static Logger log = Logger.getRootLogger();//(CoreSrv.class);
	private static final long serialVersionUID = 1L;
	public static SipFactory sf ;

	/**
	 * 
	 */
	private long TIMEOUT_BYE = 30000;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) {
		try {
			super.init(config);
			ServletContext context = getServletContext();
			sf = (SipFactory)context.getAttribute("javax.servlet.sip.SipFactory");				
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			InitLog.initLog.error("[CoreSrv]: Initialize CSCF error. [Cause]:"+e.getLocalizedMessage());			
		}
	}


	/**
	 * SCSCF init
	 */
	public  void initial(){

		InitLog.initLog.info("---------------------------------------");

		/**********************************
		 *       Initialize  Config properties
		 *********************************/
		new loadCfg().load();			

		/**********************************
		 *      Init HSS
		 *********************************/

		HssWs.init(rootCfg.Hsswsdl);

		/************************************
		 *  Initialize other AS cLasses
		 ************************************/

		new PdmKek(rootCfg.pdm_url);			

		/**********************************
		 *       Raise ReadCfg Timer
		 *********************************/
		ReadCfg.start();		

		/**********************************
		 *      Init EmailAlert Timer
		 *********************************/
		EmailAlert.init();

		/**********************************
		 *      OMC Monitor
		 *********************************/
		OMCMonitor.start();

		InitLog.initLog.info("---------------------------------------");

	}


	/** This is called by the container when shutting down the service. */
	public void destroy() {
		try {
			InitLog.initLog.error("[SCSCF]    Server is shutting down -- goodbye!");
		} catch (Throwable e) { // ignore all errors when shutting down.
			e.printStackTrace();
		}
		super.destroy();
	}

	@Override
	public void doRequest(SipServletRequest request)   {
		// TODO: Add logic	
		try {
			// 判断是否为非法信令消息
			if( utilMethod.isAttack(request)){			
				//System.err.println("attack: "+request.toString());
				return;
			}

			// check service status
			if(!ServiceStatus.checkServiceStatus(request))
				return;

			/*
			 *    SIP CALL 
			 */
			//判断会话Session是否有效
			if(request.getSession().isValid()) {					//判断是否为OPTIONS
				if(request.getMethod().equals(Request.REGISTER))			
					doRegister(request);					
				else  if(request.getMethod().equals(Request.BYE))
					doBye(request);	  
				else if(request.getMethod().equals(Request.CANCEL))			
					doCancel(request);
				else if(request.getMethod().equals(Request.ACK))		
					doAck(request);		
				else if(request.getMethod().equals(Request.INVITE)){								
					doInvite(request);	
				}else if(request.getMethod().equals(Request.MESSAGE)){
					doMessage(request);
				}else if(request.getMethod().equals(Request.SUBSCRIBE)){
					request.createResponse(403).send();
				}else if(request.getMethod().equals(Request.UPDATE))		
					doUpdate(request);
				else if(request.getMethod().equals(Request.INFO))	{
					request.createResponse(200).send();
				}if(request.getMethod().equals(Request.OPTIONS))		
				{
					doOptions(request);	
					return;
				}
			}
		}catch (Exception e) {utilMethod.logExceptions(e);}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.sip.SipServlet#doResponse(javax.servlet.sip.SipServletResponse)
	 */
	@Override
	public void doResponse(SipServletResponse response) throws IOException, ServletException {
		if(response.getStatus()<SipServletResponse.SC_OK){
			doProvisionalResponse(response);
		}else if(response.getStatus()==SipServletResponse.SC_OK){
			doSuccessResponse(response);
		}else if(response.getStatus()>=SipServletResponse.SC_OK){
			doErrorResponse(response);
		}
	}

	/* (non-Javadoc)     
	 * @see javax.servlet.sip.SipServlet#doRegister(javax.servlet.sip.SipServletRequest)     
	 */
	@Override
	public void doRegister(SipServletRequest request) {	

		try{
			//解析Contact字段
			String contact = request.getHeader("Contact");
			String id = utilMethod.parseUeID(contact);
			//
			if(null == id)
				return;
			if(id.startsWith("190"))
			{
				Registrar.register_ok(request);
				return;
			}
			if(rootCfg.auth )
				Registrar.processRegister(request);		
			else
				Registrar.register_ok(request);		
		}catch (Exception e){
			utilMethod.logExceptions(e);			
		}
	}

	/* (non-Javadoc)     
	 * @see javax.servlet.sip.SipServlet#doInvite(javax.servlet.sip.SipServletRequest)     
	 */

	public void doInvite(SipServletRequest request) throws IOException, ServletParseException {
		// TODO: Add logic
		try {
			/*
			 *   Response 100trying
			 */
			request.createResponse(Response.TRYING, "Giving a Try").send();
			/*****************************************
			 *          Re-Invite
			 *****************************************/
			if(!request.isInitial()) {
				InvLog.InvLog.info("[CoreSrv]: ------------- [re-INVITE] : [Receive]:"+request);
				B2buaHelper helper = request.getB2buaHelper();
				SipSession peerSession = helper.getLinkedSession(request.getSession());
				SipServletRequest forkedRequest = helper.createRequest(peerSession,request, null);
				forkedRequest.getSession().setAttribute("originalRequest", request);
				forkedRequest.setContentType(request.getContentType());
				forkedRequest.setContent(request.getContent(), request.getContentType());
				forkedRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
				forkedRequest.send();			
				InvLog.InvLog.info("[CoreSrv]: ------------- [re-INVITE] : [Send]:"+forkedRequest);

			}else {

				/*****************************************
				 *         Init-Invite
				 *****************************************/
				String callerID = utilMethod.parseUeID(request.getFrom().getURI().toString());
				String calleeID = utilMethod. parseUeID(request.getRequestURI().toString());

				if(callerID == null || calleeID == null)
				{
					InvLog.InvLog.info("[CoreSrv]: ------------- [Request] : [Callee/Caller UEID_ILLEGAL], caller:"+callerID +"callee:"+calleeID);
					return;
				}

				String callidCaller = request.getCallId();


				if(utilMethod.isUeidIllegal(callerID)) 
				{
					/*
					 *   INVITE 
					 */
					if(request.getMethod().equals(Request.INVITE)){
						try {
							SipServletResponse err = request.createResponse(403,rootCfg.UEID_ILLEGAL);
							err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							err.setHeader(Const.sip_header_SV, rootCfg.version);
							err.send();

							InvLog.InvLog.info("[CoreSrv]: ------------- [ERROR] : [CALLEE UEID_ILLEGAL]. [CALLEE]:"+calleeID);
							if(!utilMethod.isUeidIllegal(callerID))					
								/*
								 *   CallLogWs
								 */
								CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Callee_Iderror);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							utilMethod.logExceptions(e);
						}			
					}
					return;
				}else 
				{

					/*
					 *   Check Callee REGISTER expire time 					
					 */
					// 主叫是否注册
				    boolean callerExpired = utilMethod.checkUeRegisterExpires(callerID);//false 未注册或失效或离线
					boolean calleeExpired = utilMethod.checkUeRegisterExpires(calleeID);
					if(!callerExpired && !calleeExpired) {
						SipServletResponse errresponse = request.createResponse(SipServletResponse.SC_FORBIDDEN,rootCfg.CALLEEORCALLER_EXPIRES);
						errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
						errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
						errresponse.send();
						InvLog.InvLog.info("[CoreSrv]: ------------- [ERROR] : [CALLEE/CALLER_EXPIRES]. "+errresponse);

						/*
						 *   CallLogWs
						 */
						CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Callee_Invalid);

						/*
						 *  send SMS to Reminder Callee
						 */
						SMSAlert.sendCallLogSMSToCallee(callerID, calleeID);   // ------------------SMS
						return;
					}else {
						/*
						 *   callee offline
						 */
						if(!utilMethod.checkUeOnline(calleeID)){
							SipServletResponse errresponse = request.createResponse(SipServletResponse.SC_FORBIDDEN,rootCfg.CALLEE_OFFLINE);
							errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
							errresponse.send();
							InvLog.InvLog.info("[CoreSrv]: -------------[CALLEE UNREGISTERED]. "+errresponse);
							/*
							 *    Record to DB no Recv Call Info
							 */
							/*
							 *   CallLogWs
							 */
							CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Callee_Offline);

							/*
							 *  send SMS to Reminder Callee
							 */
							SMSAlert.sendCallLogSMSToCallee(callerID, calleeID);   // ------------------SMS						

						}else {							

							String agent = request.getHeader(Const.sip_header_UA);	

							/**************************************
							 *        INVITE TO VMGW
							 ************************************/

							if(agent!= null && (agent.contains("Sonicom")||agent.contains("eyeBeam")))							
							{

								SipURI requestURI =sf.createSipURI(calleeID, (rootCfg.mgw_address));//(SipURI) sf.createURI(calleeUri);//

								/********************************
								 * **********  B2BUA **********
								 ******************************/

								Map<String,List<String>> headers = new HashMap<String,List<String>>();
								/*
								 *    FROM /To Header
								 */
								List<String> FromHeaderSet =new ArrayList<String>();
								List<String> ToHeaderSet =new ArrayList<String>();

								String fromUri = "sip:"+callerID+"@"+rootCfg.realm;
								FromHeaderSet.add(fromUri);			
								headers.put("From",FromHeaderSet);	

								String toUri = "sip:"+calleeID+"@"+rootCfg.realm;
								ToHeaderSet.add(toUri);			
								headers.put("To",ToHeaderSet);	

								B2buaHelper bh = request.getB2buaHelper();
								SipServletRequest invite = null;
								try {
									invite = bh.createRequest(request,true,headers);
								} catch (IllegalArgumentException e1) {
									// TODO Auto-generated catch block
									utilMethod.logExceptions(e1);
									InvLog.InvLog.error("[CoreSrv]: ------------- [Error], [B2BUA IllegalArgumentException]: "+e1.getLocalizedMessage());
								} catch (TooManyHopsException e1) {
									// TODO Auto-generated catch block
									utilMethod.logExceptions(e1);
									InvLog.InvLog.error("[CoreSrv]: ------------- [Error], [B2BUA TooManyHopsException]: "+e1.getLocalizedMessage());
								}	



								/**************************************************************************************
								 *    create callee Tek,Set Callee Tek    kek:32  tek:8 AEStek:32 base64:44
								 ******************************************************************************************/
								String vmgwCallid = invite.getCallId();

								String calleeEncTek = null;
								int tekCreateRet = 0;
								try {
									/*
									 * create pure Tek and store
									 */
									tekCreateRet = TekWs.tekInsert(vmgwCallid, callerID, calleeID);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									SipServletResponse errresponse = request.createResponse(503,rootCfg.SERVER_TEK_EXCEPTION);
									errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
									errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
									errresponse.send();
									utilMethod.logExceptions(e);
									InvLog.InvLog.error("[CoreSrv]: ------------- [ERROR] : [SERVER_TEK_CREATE_EXCEPTION]:"+errresponse);

									/*
									 *   CallLogWs
									 */
									CallLogWs.callLogFailed(callidCaller, callerID, calleeID, CallLogWs.Record_Tek_Notfound);	
									return;
								}	
								/*
								 *  CallLogWs  init
								 */
								CallLogWs.callLogInit(callidCaller, callerID, calleeID);
								/*
								 *   check  Ue calling status
								 */
								if( UeWs.getUeStatus(calleeID).equals(States.CALLING))
								{
									try {
										SipServletResponse err = request.createResponse(SipServletResponse.SC_BUSY_HERE,rootCfg.CALLEE_IN_CALLING);
										err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
										err.setHeader(Const.sip_header_SV, rootCfg.version);
										err.send();
										InvLog.InvLog.info("[CoreSrv]: ------------- [ERROR] : [CALLEE_IN_CALLING]:"+err);

										/*
										 *   CallLogWs
										 */
										CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Callee_Incalling);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										utilMethod.logExceptions(e);
									}		
									return;
								}
								/*
								 *   caller == callee
								 */
								if(callerID.equals(calleeID)){
									try {
										SipServletResponse errresponse = request.createResponse(403,rootCfg.CALLER_IS_CALLEE);
										errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
										errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
										errresponse.send();

										InvLog.InvLog.info("[CoreSrv]: ------------- [ERROR] : [CALLER_IS_CALLEE]:"+errresponse);
										/*
										 *   CallLogWs
										 */
										CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Caller_Itself);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										utilMethod.logExceptions(e);
									}
								}else{
									InvLog.InvLog.info("[CoreSrv]:-------- INVITE from Caller, [CallerID]:" +callerID);
									InvLog.InvLog.info(request.toString());
									/*
									 *  Check  Caller  Online
									 */
									try {
										if(!utilMethod.checkUeOnline(callerID)){
											SipServletResponse errresponse = request.createResponse(SipServletResponse.SC_FORBIDDEN,rootCfg.CALLER_UNREGISTER);
											errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
											errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
											errresponse.send();
											InvLog.InvLog.info("[CoreSrv]: ------------- [ERROR] : [CALLER_OFFLINE]:"+errresponse);

											/*
											 *   CallLogWs
											 */
											CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Caller_Unreg);
											return ;								
										}else
										{
											if( 0!=tekCreateRet) {
												SipServletResponse errresponse = request.createResponse(503,rootCfg.SERVER_TEK_EXCEPTION);
												errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
												errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
												errresponse.send();
												InvLog.InvLog.error("[CoreSrv]: ------------- [ERROR] : [SERVER_TEK_EXCEPTION]:"+errresponse);
												/*
												 *   CallLogWs
												 */
												CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Tek_Notfound);	
												return;
											}else {	

												String calleeKek = UeWs.getUeKek(calleeID);		
												//zhaoronghui
												if(calleeKek!= null)
												{																					
													try {			
														calleeEncTek = TekWs.createUeTek(calleeID, vmgwCallid, calleeKek);														
													
													} catch (Exception e) {
														// TODO Auto-generated catch block													
														utilMethod.logExceptions(e);
														InvLog.InvLog.error("[CoreSrv]:------------ [createUsrEncryptTek error], [Cause]:"+e.getLocalizedMessage());
													}
												}else {
													calleeEncTek = null;
													InvLog.InvLog.error("[CoreSrv]:------------Error, [Cause]: calleeKek is null.");
												}

												if(calleeEncTek != null){
													/*
													 * add k to SDP
													 */
													String osdp = new String(request.getRawContent());

													osdp = utilMethod.addKAttr2SDP(osdp, calleeEncTek);	    

													invite.getSession().setAttribute("originalRequest", request);

													/*
													 * Set the Session to be continued
													 */

													request.getApplicationSession().setExpires(0);
													invite.getApplicationSession().setExpires(0);

													invite.setHeader(Const.sip_header_UA, rootCfg.version);
													invite.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());

													if(rootCfg.mgw_record.equalsIgnoreCase("0"))
													{
														/*
														 *  add Record order 1
														 *    X-MP-Record:  1-record else not record
														 */		

														String retcaller = UeWs.getUeRECKey(callerID);
														String retcallee = UeWs.getUeRECKey(calleeID);

														/*
														 *  Special : YES
														 */													
														if((null != retcaller && retcaller.equals("YES"))
																|| (null != retcallee && retcallee.equals("YES")))
														{
															invite.addHeader(Const.sip_header_X_MP_Record, "1");
															request.getSession().setAttribute(Const.sip_header_X_MP_Record, "0");
															osdp = utilMethod.setRecordSDP(osdp);
														}else{
															request.getSession().setAttribute(Const.sip_header_X_MP_Record, "0");
														}
													}

													invite.setRequestURI(requestURI);
													invite.setContentType(request.getContentType());
													invite.setContent(osdp,request.getContentType());
													invite.setContentLength(osdp.length());	
													invite.send();

													/**
													 * update caller UE Calling status
													 *  
													 */
													UeWs.setUeStatus(callerID, States.CALLING);
													InvLog.InvLog.info("[CoreSrv]: ------------ INVITE to VMGW, [VMGW]: "+rootCfg.mgw_address);
													InvLog.InvLog.info(invite);
												}else{
													SipServletResponse errresponse = request.createResponse(503,rootCfg.SERVER_TEK_EXCEPTION);
													errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
													errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
													errresponse.send();

													InvLog.InvLog.error("[CoreSrv]: ------------- [ERROR] : [SERVER_TEK_EXCEPTION]:"+errresponse);

													/*
													 *   CallLogWs
													 */
													CallLogWs.callLogFailed(request.getCallId(), callerID, calleeID, CallLogWs.Record_Tek_Notfound);													return;
												}
											}										
										}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										utilMethod.logExceptions(e);
										return;
										//e.printStackTrace();
									} 
								}		
								/**************************************
								 *        INVITE TO PCSCF
								 ************************************/
							}else{
								try {
									InvLog.InvLog.info("[CoreSrv]:-------- INVITE from VMGW:");
									InvLog.InvLog.info(request);		

									/**********************  FIND ************************/

									String calleeUri = UeWs.getUeAddress(calleeID);
									if(calleeUri == null)
									{
										SipServletResponse errresponse = request.createResponse(404,rootCfg.CALLEE_NOT_FOUND);
										errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
										errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
										errresponse.send();
										InvLog.InvLog.info("[CoreSrv]: ------------- [ERROR] : [CALLEE_URI_NOTFOUND]:"+errresponse);								
										return;
									}

									SipURI  request2Callee =  (SipURI) sf.createURI(calleeUri);  //sf.createSipURI(calleeID,calleeUri);

									/********************************
									 * **********  B2BUA **********
									 ******************************/

									/*
									 *   FROM /TO Header
									 */
									Map<String,List<String>> headers=new HashMap<String,List<String>>();
									List<String> FromHeaderSet =new ArrayList<String>();
									List<String> ToHeaderSet =new ArrayList<String>();

									String fromUri = "sip:"+callerID+"@"+rootCfg.realm;
									FromHeaderSet.add(fromUri);			
									headers.put("From",FromHeaderSet);	

									String toUri = "sip:"+calleeID+"@"+rootCfg.realm;
									ToHeaderSet.add(toUri);			
									headers.put("To",ToHeaderSet);	

									B2buaHelper bh = request.getB2buaHelper();
									SipServletRequest invite = null;
									try {
										invite = bh.createRequest(request,true,headers);
									} catch (IllegalArgumentException e) {
										// TODO Auto-generated catch block
										utilMethod.logExceptions(e);
										InvLog.InvLog.error("[CoreSrv]: ------------- [Error], [B2BUA IllegalArgumentException]: "+e.getLocalizedMessage());
									} catch (TooManyHopsException e) {
										// TODO Auto-generated catch block
										utilMethod.logExceptions(e);
										InvLog.InvLog.error("[CoreSrv]: ------------- [Error], [B2BUA IllegalArgumentException]: "+e.getLocalizedMessage());
									}	

									FromHeaderSet = null;
									headers = null;

									/*
									 *    add route
									 */
									String domain = UeWs.getDomain(calleeID);

									Address route_pcscf = sf.createAddress(domain+";lr");

									invite.pushRoute(route_pcscf);

									/*
									 * add k to SDP
									 */

									String osdp = new String(request.getRawContent());
									/*
									 *  change VMGW SDP IP with outbound IP
									 */
									if(rootCfg.mgw_nat.equals("0"))
										osdp = utilMethod.changeNatIPInSDP(osdp, (rootCfg.v_mgw_nat_addr).trim());

									invite.getSession().setAttribute("originalRequest", request);
									invite.setContentType(request.getContentType());
									invite.setContent(osdp,request.getContentType());
									invite.setContentLength(osdp.length());	

									invite.setRequestURI(request2Callee);
									invite.setHeader(Const.sip_header_UA, rootCfg.version);
									invite.getAddressHeader("Contact").setExpires(7200);	

									if(invite.getHeader("Allow-Events")!= null)
										invite.removeHeader("Allow-Events");
									if(invite.getHeader("Supported")!= null)
										invite.removeHeader("Supported");
									if(invite.getHeader("Allow")!= null)
										invite.removeHeader("Allow");
									if(invite.getHeader("Content-Disposition")!= null)
										invite.removeHeader("Content-Disposition");
									invite.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
									invite.send();

									request.getApplicationSession().setExpires(0);
									invite.getApplicationSession().setExpires(0);

									InvLog.InvLog.info("[CoreSrv]:------------INVITE to PCSCF, [INVITE]:"+invite);

									/**
									 * update callee UE Calling status
									 *  
									 */
									UeWs.setUeStatus(calleeID, States.CALLING);									
									request.getSession().setAttribute("forwardRequestToCallee", invite);									


									//timeout
									TimerService timerService = (TimerService) getServletContext().getAttribute(TIMER_SERVICE);
									timerService.createTimer(request.getApplicationSession(), TIMEOUT_BYE , false, (Serializable)request);	

								} catch (ServletParseException e) {
									// TODO Auto-generated catch block
									utilMethod.logExceptions(e);
								}
							}
						}
					}
				}
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e2);
		}		
	}


	@Override
	protected void doCancel(SipServletRequest request)  {      		
		try {

			String agent = request.getHeader(Const.sip_header_UA);	

			SipSession session = request.getSession();      
			B2buaHelper helper = request.getB2buaHelper();
			SipSession linkedSession = helper.getLinkedSession(session);
			SipServletRequest originalRequest=null;

			String callerID = utilMethod.parseUeID(request.getFrom().toString());
			String calleeID = utilMethod. parseUeID(request.getRequestURI().toString());
			/**
			 * update UE Calling status
			 *  
			 */			
			UeWs.setUeStatus(callerID, States.ON);
			UeWs.setUeStatus(calleeID, States.ON);

			if(linkedSession != null)
				originalRequest= (SipServletRequest)linkedSession.getAttribute("originalRequest");

			if(originalRequest != null)
			{
				if(linkedSession!=null && linkedSession.isValid()) {
					SipServletRequest  linkedRequest =  helper.getLinkedSipServletRequest(originalRequest);

					if(linkedRequest != null && linkedRequest.getSession().isValid()) {
						try
						{
							SipServletRequest  cancelRequest  = linkedRequest.createCancel();
							if(agent!=null && agent.contains("ippbx")){
								InvLog.InvLog.info("[CoreSrv]: ------------ [CANCEL from VMGW] : "+request);
								String toId = utilMethod.parseUeID(request.getTo().getURI().toString());
								String pscscfdomain = UeWs.getDomain(toId);
								String toaddress =null;
								if(null!=pscscfdomain)
								{
									if(!pscscfdomain.contains("@"))
									{
										toaddress = pscscfdomain.replace("sip:", "").trim();
										cancelRequest.setRequestURI(sf.createSipURI(toId,toaddress));
									}
									else {										
										cancelRequest.setRequestURI(sf.createURI(pscscfdomain));
									}

									cancelRequest.setHeader(Const.sip_header_UA, rootCfg.version);
									cancelRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
									cancelRequest.send();
									InvLog.InvLog.info("[CoreSrv]: ------------ [CANCEL to PCSCF]: "+cancelRequest);
								}else{
									cancelRequest.setHeader(Const.sip_header_UA, rootCfg.version);
									cancelRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
									cancelRequest.send();
									InvLog.InvLog.info("[CoreSrv]: ------------ [CANCEL to PCSCF]: "+cancelRequest);
								}
							}else{
								InvLog.InvLog.info("[CoreSrv]: ------------ [CANCEL from PCSCF] :"+request);
								String toId = utilMethod.parseUeID(request.getTo().getURI().toString());
								String toaddress = rootCfg.mgw_address;
								cancelRequest.setRequestURI(sf.createSipURI(toId,toaddress));
								cancelRequest.setHeader(Const.sip_header_UA, rootCfg.version);
								cancelRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
								cancelRequest.send();
								InvLog.InvLog.info("[CoreSrv]: ------------ [CANCEL to VMGW]: "+cancelRequest);
							}						
						}						
						catch (Exception e){
							InvLog.InvLog.error("[CoreSrv]: ------------ [CANCEL],  error Cause:  "+e.getLocalizedMessage());
						}
					}else
						return;
				} 			
			}
			/*
			 *   CallLogWs
			 */
			CallLogWs.callLogAbort(request.getCallId(), Request.CANCEL);
			//TekWs.tekDelete(request.getCallId());
		} catch (Exception e) {
			// TODO Auto-generated catch block	
			utilMethod.logExceptions(e);
			InvLog.InvLog.error("[CoreSrv]: ------------ Error. [CANCEL]. [Cause]: "+e.getLocalizedMessage());				
		}
	}



	@Override
	public void doBye(SipServletRequest request) throws IOException {
		try{
			SipServletRequest forkedRequest = null;
			String agent = request.getHeader(Const.sip_header_UA);	

			/**
			 * update UE Calling status
			 *  
			 */			
			String callerID = utilMethod.parseUeID(request.getFrom().toString());
			String calleeID = utilMethod.parseUeID(request.getTo().toString());
			UeWs.setUeStatus(callerID, States.ON);
			UeWs.setUeStatus(calleeID, States.ON);

			/*
			 * we send the OK directly to the first call leg
			 */
			try{
				SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
				sipServletResponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
				sipServletResponse.setHeader(Const.sip_header_SV, rootCfg.version);
				sipServletResponse.send();
			}catch(Exception e) {utilMethod.logExceptions(e);}

			/**
			 *   exception
			 */

			if(!request.getSession().isValid()
					||!request.getSession().getApplicationSession().isValid()) {
				InvLog.InvLog.error("[CoreSrv]:-------------["+request.getCallId()+"]'s session is invalid already.");

				try{
					SipServletResponse err = request.createResponse(488);
					err.send();
					InvLog.InvLog.info("[CoreSrv]: ------------ [488] :"+err);
				}catch(Exception e) {utilMethod.logExceptions(e);}

			}else {				

				SipSession session = request.getSession();
				B2buaHelper helper = request.getB2buaHelper();		
				SipSession linkedSession = helper.getLinkedSession(session);  

				if((linkedSession != null)
						&& linkedSession.getState() != State.TERMINATED)
				{
					try {
						{
							forkedRequest = linkedSession.createRequest("BYE");
							forkedRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							/*
							 *   BYE from VMGW
							 */

							if(agent!=null && agent.contains("ippbx")){
								InvLog.InvLog.info("[CoreSrv]: ------------ [BYE from VMGW] :"+request);
								String toId = utilMethod.parseUeID(request.getTo().getURI().toString());
								String pscscfdomain = UeWs.getDomain(toId);
								String toaddress =null;
								if(null!=pscscfdomain)
								{/*
									if(!pscscfdomain.contains("@"))
									{
										toaddress = pscscfdomain.replace("sip:", "").trim();
										forkedRequest.setRequestURI(sf.createSipURI(toId,toaddress));
									}
									else {										
										forkedRequest.setRequestURI(sf.createURI(pscscfdomain));
									}*/
									forkedRequest.setHeader(Const.sip_header_UA, rootCfg.version);
									forkedRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
									forkedRequest.send(); 	
									InvLog.InvLog.info("[CoreSrv]: ------------ [BYE to PCSCF]:"+forkedRequest);	
								}else{
									forkedRequest.setHeader(Const.sip_header_UA, rootCfg.version);
									forkedRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
									forkedRequest.send(); 	
									InvLog.InvLog.info("[CoreSrv]: ------------ [BYE to PCSCF]:"+forkedRequest);	
								}

							}else{
								InvLog.InvLog.info("[CoreSrv]: ------------ [BYE from PCSCF] :"+request);
								String toId = utilMethod.parseUeID(request.getTo().getURI().toString());
								String toaddress = rootCfg.mgw_address;
								forkedRequest.setRequestURI(sf.createSipURI(toId,toaddress));
								forkedRequest.setHeader(Const.sip_header_UA, rootCfg.version);
								forkedRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
								forkedRequest.send(); 	
								InvLog.InvLog.info("[CoreSrv]: ------------ [BYE to VMGW]:"+forkedRequest);	
							}	

						}
					}catch (Exception e) {
						utilMethod.logExceptions(e);
					}
				}else {
					InvLog.InvLog.error("[CoreSrv]:-------------Call-ID ["+request.getCallId()+"]'s  B2BUA linked Session is invalidate already.");
				}
			}

			/*
			 *   CallLogWs
			 */
			CallLogWs.callLogUpdate(request.getCallId(),CallLogWs.TERMINATED);	
			if(null != forkedRequest)
				CallLogWs.callLogUpdate(forkedRequest.getCallId(),CallLogWs.TERMINATED);


		} catch(Exception e){
			utilMethod.logExceptions(e);			
		}
	}


	/* (non-Javadoc)     
	 * @see javax.servlet.sip.SipServlet#doMessage(javax.servlet.sip.SipServletRequest)     
	 */
	@Override
	protected void doMessage(SipServletRequest request) {   
		try{
			String calleeID = utilMethod.parseUeID(request.getTo().getURI().toString());
			if(null == calleeID)
				return;

			String calleeDomain = (String) UeWs.getDomain(calleeID);

			if(null == calleeDomain)
			{
				try {
					request.createResponse(404, "CALLEE_DOMAIN_NOT_FOUND").send();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					utilMethod.logExceptions(e);
				}
				return;
			}

			SipURI requestURI = sf.createSipURI(calleeID, calleeDomain);
			B2buaHelper bh = request.getB2buaHelper();
			SipServletRequest message = bh.createRequest(request);	

			String ua = request.getHeader(Const.sip_header_UA);
			if(null != ua)
				message.setHeader(Const.sip_header_UA, ua);

			message.getSession().setAttribute("originalRequest", request);
			message.setContentType(request.getContentType());
			try {
				message.setContent(request.getContent(),request.getContentType());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				utilMethod.logExceptions(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				utilMethod.logExceptions(e);
			}
			message.setContentLength(request.getContentLength());		

			/*
			 * Set the Session to be continued
			 */
			message.setRequestURI(requestURI);
			message.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());

			try {
				message.send();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				utilMethod.logExceptions(e);
			}
		}catch (Exception e){
			utilMethod.logExceptions(e);
		}
	}

	/* (non-Javadoc)     
	 * @see javax.servlet.sip.SipServlet#doOptions(javax.servlet.sip.SipServletRequest)     
	 */

	@Override
	protected void doOptions(SipServletRequest request)   { 
		try {
			if(ServiceStatus.hssStatus !=0)			
				return;
			SipServletResponse res = request.createResponse(200);
			res.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			res.setHeader(Const.sip_header_SV, rootCfg.version);
			res.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//	e.printStackTrace();
		}
	}


	@Override
	protected void doAck(SipServletRequest request) throws IOException  {  
		InvLog.InvLog.info("[CoreSrv]: ------------[Receive ACK] :"+request);
		/*
		 *   CallLogWs
		 */
		CallLogWs.callLogUpdate(request.getCallId(),CallLogWs.CONFIRMED);	

	}	

	@Override
	protected void doInfo(SipServletRequest request) throws IOException  {  
		request.createResponse(200).send();	
	}


	/* (non-Javadoc)     
	 * @see javax.servlet.sip.SipServlet#doOptions(javax.servlet.sip.SipServletRequest)     
	 */
	@Override
	protected void doUpdate(SipServletRequest request) throws IOException  {   
		InvLog.InvLog.info("[CoreSrv]: ------------[Receive UPDATE] :"+request);			

		B2buaHelper helper = request.getB2buaHelper();
		SipSession peerSession = helper.getLinkedSession(request.getSession());
		SipServletRequest update = helper.createRequest(peerSession, request,null);
		update.getSession().setAttribute("originalRequest", request);
		update.setContentType(request.getContentType());
		update.setContent(request.getContent(), request.getContentType());
		update.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
		update.send();
		InvLog.InvLog.info("[CoreSrv]: ------------[Send UPDATE] :"+update);
	}


	@Override
	public void doProvisionalResponse(SipServletResponse response) throws IOException{
		try {
			if(response.getStatus() == 180){
				InvLog.InvLog.info("[CoreSrv]:---------Receive ["+response.getStatus()+"] Response ."+" :" +response);
				SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");

				ServletTimer st = (ServletTimer) originalRequest.getSession().getAttribute("ServletTimer");			
				if(st !=null)
					st.cancel();

				if(originalRequest.getSession().getState() == State.TERMINATED)
					return;

				SipServletResponse ring = originalRequest.createResponse(response.getStatus(),"Ringing A Bell");	
				ring.setHeader(Const.sip_header_SV, rootCfg.version);
				ring.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());				
				ring.send();
				InvLog.InvLog.info("[CoreSrv]:---------Send ["+response.getStatus()+"] Response ]"+" :" +ring);
			}else {
				InvLog.InvLog.info("[CoreSrv]:---------Receive ["+response.getStatus()+"] Response ."+" :" +response);
				SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");
				SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());	
				responseToOriginalRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
				responseToOriginalRequest.setHeader(Const.sip_header_SV, rootCfg.version);
				responseToOriginalRequest.send();
				InvLog.InvLog.info("[CoreSrv]:---------Send ["+response.getStatus()+"] Response ]"+" :" +responseToOriginalRequest);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
			return;
		}
	}

	@Override
	public void doSuccessResponse(SipServletResponse response) {

		/*******************************************************************
		 *                      INVITE
		 ******************************************************************/
		/*
		 *   200 ok from VMGW
		 */

		if(response.getMethod().indexOf("INVITE") != -1) {

			/******************************************************************************************************
			 *   								Re-Invite on dialog	200 ok 
			 *******************************************************************************************************/
			boolean initial = response.getRequest().isInitial();

			if(!initial){

				//   if this is a response to an INVITE we ack it and forward the OK 

				SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");				
				if(originalRequest != null){
					SipServletResponse res = originalRequest.createResponse(response.getStatus(),"In dialog");
					res.setHeader(Const.sip_header_SV, rootCfg.version);
					res.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
					res.setContentType(response.getContentType());					
					res.setContentType(response.getContentType());
					try {
						res.setContent(response.getContent(), response.getContentType());
						res.setContentLength(response.getContentLength());

						if(res.getSession().getState() == State.TERMINATED)
							return;
						res.send();
						InvLog.InvLog.info("[CoreSrv]: ------------[Re-INVITE 200 OK ] :"+res);

					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						utilMethod.logExceptions(e1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						utilMethod.logExceptions(e1);
					}		

					try {
						SipServletRequest ackRequest = response.createAck();
						ackRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
						ackRequest.setHeader(Const.sip_header_UA, rootCfg.version);
						ackRequest.send();
						InvLog.InvLog.info("[CoreSrv]: ------------[Send ACK ] :"+ackRequest);
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						utilMethod.logExceptions(e);					
						return;
					}
				}


				/*
				 *       initial  CALL  200 OK
				 */

			}else{		
				String agent = response.getHeader(Const.sip_header_UA);		

				/******************************************************************************************************
				 *   											200 ok from VMGW
				 *******************************************************************************************************/
				if(agent != null && agent.contains(Const.sip_vmgw_UA)){

					//   if this is a response to an INVITE we ack it and forward the OK 


					SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");
					String callerID =utilMethod.parseUeID(originalRequest.getFrom().toString());
					String calleeID =utilMethod. parseUeID(originalRequest.getTo().toString());
					/*
					 *   1、200OK到达之前，主叫的CANCEL先到达
					 *   2、则发送给被叫BYE
					 */

					if(originalRequest.getSession().getState().equals(SipSession.State.TERMINATED)){

						SipServletRequest bye = response.getSession().createRequest(Request.BYE);
						try {
							InvLog.InvLog.info("[CoreSrv]: ------------[Send Status_Error BYE] :"+bye);	
							bye.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							bye.send();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}	

					/*
					 *   Send ACK
					 */
					SipServletRequest ackRequest = response.createAck();
					try {
						ackRequest.setHeader(Const.sip_header_UA, rootCfg.version);
						ackRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());						
						//			String toId = utilMethod.parseUeID(response.getFrom().getURI().toString());
						//			String toaddress = response.getRemoteAddr()+":"+String.valueOf(response.getRemotePort());
						//			ackRequest.setRequestURI(sf.createSipURI(toId,toaddress));
						ackRequest.send();
						InvLog.InvLog.info("[CoreSrv]: ------------[ACK to VMGW ] :"+ackRequest);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						utilMethod.logExceptions(e);						
					}

					SipServletResponse ok2callee = originalRequest.createResponse(response.getStatus(),"Receiving A Call");
					ackRequest.setHeader(Const.sip_header_SV, rootCfg.version);
					ok2callee.setHeader(Const.sip_header_SV, rootCfg.version);
					String vmgwCallid = response.getCallId();


					InvLog.InvLog.info("[CoreSrv]: -------- [200OK from VMGW], [CalleeID]:" +calleeID);
					InvLog.InvLog.info(response);


					try {
						String okSdp  = new String(response.getRawContent());

						/*
						 *   create  Caller Tek
						 */		
						String caller_sessionkey = UeWs.getUeKek(callerID);
						String callerTek = TekWs.createUeTek(callerID, vmgwCallid, caller_sessionkey);
						//if(callerID.equals(""))
						//zhaoronghui
						{
							//	System.err.println("caller:"+callerID);
							//	System.err.println("caller_sessionkey:"+caller_sessionkey);
							//	System.err.println("caller_tek:"+callerTek);

						}
						if(callerTek != null)
						{
							//add k to SDP			
							okSdp = utilMethod.addKAttr2SDP(okSdp, callerTek);

							/*
							 *     0: REC  1: dont REC
							 */
							if(rootCfg.mgw_record.equalsIgnoreCase("0")){

								// init 0: not record audio
								ok2callee.addHeader(Const.sip_header_X_MP_Record, "0");							

								Object  xmrecord = originalRequest.getSession().getAttribute(Const.sip_header_X_MP_Record);

								if(null != xmrecord && "1".equals(xmrecord.toString()))
								{
									// set 1: record audio
									ok2callee.setHeader(Const.sip_header_X_MP_Record, "1");
									okSdp = utilMethod.setRecordSDP(okSdp);
								}
							}


							/*
							 *  change VMGW SDP IP with outbound IP
							 */
							if(rootCfg.mgw_nat.equals("0"))
								okSdp = utilMethod.changeNatIPInSDP(okSdp, (rootCfg.v_mgw_nat_addr).trim());

							ok2callee.setContentType(response.getContentType())	;
							ok2callee.setContent(okSdp, response.getContentType());			
							ok2callee.setContentLength(okSdp.length());
							ok2callee.setHeader(Const.sip_header_SV, rootCfg.version);
							ok2callee.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							if(ok2callee.getSession().getState().equals(SipSession.State.TERMINATED))
								return;
							ok2callee.send();									
							InvLog.InvLog.info("[CoreSrv]: --------- [200OK to PCSCF], [CallerID]: " +callerID);
							InvLog.InvLog.info(ok2callee);						
						}else{

							// BYE to Callee, Cause caller Tek error
							InvLog.InvLog.error("[CoreSrv]: [Cause] caller'Tek error. [Caller]:"+callerID);
							SipSession ss = ackRequest.getSession();
							if(ss.isValid()&&ss.getApplicationSession().isValid()) {
								SipServletRequest bye = ackRequest.getSession().createRequest("BYE");
								bye.setRequestURI(ackRequest.getRequestURI());
								bye.setHeader(Const.sip_header_UA, rootCfg.version);
								bye.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
								bye.send();
								InvLog.InvLog.error("[CoreSrv]: [BYE to PCSCF]. [Caller]:"+callerID);
							}
							return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						InvLog.InvLog.error("[CoreSrv]: Error occurred. [Cause]: " +e.getLocalizedMessage());						
						utilMethod.logExceptions(e);					
					}

					/******************************************************************************************************
					 *   											200 ok from PCSCF
					 *******************************************************************************************************/

				}else{
					SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");
					if(originalRequest != null){
						String calleeID = utilMethod. parseUeID(originalRequest.getTo().toString());

						InvLog.InvLog.info("[CoreSrv]: -------- [200OK from PCSCF]:"+calleeID);
						InvLog.InvLog.info(response);

						/*
						 *   1、200OK到达之前，主叫的CANCEL先到达
						 *   2、则发送给被叫BYE
						 */

						if(originalRequest.getSession().getState().equals(SipSession.State.TERMINATED)){
							try {
								SipServletRequest bye = response.getSession().createRequest(Request.BYE);
								bye.setHeader(Const.sip_header_UA, rootCfg.version);
								bye.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
								bye.send();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							return;
						}	

						/*
						 *   ACK确认消息
						 */
						SipServletRequest ackRequest = response.createAck();	
						try {
							//	InvLog.InvLog.info("[CoreSrv]: ------------[Send a ACK] :");	
							//			String toId = utilMethod.parseUeID(response.getFrom().getURI().toString());
							//			String toaddress = response.getRemoteAddr()+":"+String.valueOf(response.getRemotePort());
							//			ackRequest.setRequestURI(sf.createSipURI(toId,toaddress));
							ackRequest.setHeader(Const.sip_header_UA, rootCfg.version);
							ackRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							ackRequest.send();
							InvLog.InvLog.info("[CoreSrv]: ------------[ACK to PCSCF] :"+ackRequest);	
						} catch (IOException e) {
							// TODO Auto-generated catch block
							utilMethod.logExceptions(e);
							return;
						}

						/*
						 *    200 Ok to VMGW
						 */

						try {
							SipServletResponse ok2vmgw = originalRequest.createResponse(response.getStatus(),"Receiving A Call");
							ok2vmgw.setHeader(Const.sip_header_SV, rootCfg.version);
							ok2vmgw.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());

							ok2vmgw.setContentType(response.getContentType())	;
							ok2vmgw.setContent(response.getContent(), response.getContentType());
							ok2vmgw.setContentLength(response.getContentLength());

							if(ok2vmgw.getSession().getState().equals(SipSession.State.TERMINATED))
								return;
							ok2vmgw.send();
							InvLog.InvLog.info("[CoreSrv]: ------------[200OK to VMGW] :"+ok2vmgw);	
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							utilMethod.logExceptions(e);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							utilMethod.logExceptions(e);
						}		
					}
				}				
			}

			/*******************************************************************
			 *                     BYE
			 ******************************************************************/

		}else if(response.getMethod().indexOf("BYE") != -1) {
			SipSession sipSession = response.getSession(false);
			if(sipSession != null && sipSession.isValid()) {
				sipSession.invalidate();
			}
			SipApplicationSession sipApplicationSession = response.getApplicationSession(false);
			if(sipApplicationSession != null && sipApplicationSession.isValid()) {
				sipApplicationSession.invalidate();
			}       
			return;

			/*******************************************************************
			 *                    MESSAGE
			 ******************************************************************/
		}else if(response.getMethod().indexOf("MESSAGE") != -1) {
			SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");

			if(originalRequest!= null) {
				SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
				try {
					responseToOriginalRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
					responseToOriginalRequest.setHeader(Const.sip_header_SV, rootCfg.version);				
					responseToOriginalRequest.send();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					utilMethod.logExceptions(e);
				}
			}else {
				/*
				 *   push MESSAGE reponse
				 */
				//String UeId = utilMethod.parseUsrID(response.getTo().getURI().toString());
				//PushCallInfo.deleteCallNoRecvInfo(UeId);
			}
		}else if(response.getMethod().indexOf(Request.UPDATE)!=-1) {

			InvLog.InvLog.info("[CoreSrv]: ------------[UPDATE 200 OK ] :"+response);

			SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");				
			if(originalRequest != null){
				SipServletResponse res = originalRequest.createResponse(response.getStatus(),"On dialog");
				res.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
				res.setHeader(Const.sip_header_SV, rootCfg.version);

				res.setContentType(response.getContentType());			
				try {
					res.setContent(response.getContent(), response.getContentType());
					res.setContentLength(response.getContentLength());
					InvLog.InvLog.info("[CoreSrv]: ------------[UPDATE 200 OK ] :"+res);
					res.send();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					utilMethod.logExceptions(e1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					utilMethod.logExceptions(e1);
				}				

			}
		}
	}

	/* (non-Javadoc)     
	 * @see javax.servlet.sip.SipServlet#doErrorResponse(javax.servlet.sip.SipServletResponse)     
	 */
	@Override
	public void doErrorResponse(SipServletResponse response){
		try {			
			SipServletRequest originalRequest = (SipServletRequest) response.getSession().getAttribute("originalRequest");
			InvLog.InvLog.info("[CoreSrv]:---------------Receive ["+response.getStatus()+"] Response:");
			InvLog.InvLog.info(response);

			String callerID = utilMethod.parseUeID(response.getFrom().toString());
			String calleeID = utilMethod. parseUeID(response.getTo().toString());


			/**
			 * update UE Calling status
			 *  
			 */			
			UeWs.setUeStatus(callerID, States.ON);
			UeWs.setUeStatus(calleeID, States.ON);		

			/**
			 *    Deal 
			 */
			SipServletResponse responseToOriginalRequest = null;
			String  orgCallid = null;
			int status = response.getStatus();

			/*
			 *       Time out
			 */
			if(status == 408){			
				if(response.getMethod().equals(Request.INVITE)){
					InvLog.InvLog.info("[CoreSrv]:---------------Receive ["+response.getStatus()+"] Response:" + response);
					/*
					 *   CallLogWs
					 */
					CallLogWs.callLogAbort(response.getCallId(), String.valueOf(status));


					/*
					 *  send SMS to Reminder Callee
					 */
					SMSAlert.sendCallLogSMSToCallee(callerID, calleeID);   // ------------------SMS
				}				
			}else if(status == 487 ||status == 481){
				/*
				 * 487(CANCEL) / 481 Discasted
				 */
			}else {						
				/*
				 * other error response
				 */
				if(originalRequest != null 
						&& originalRequest.getApplicationSession().isValid()
						&& originalRequest.getSession().isValid())
				{	
					try {
						if(originalRequest.getSession().getState() != SipSession.State.TERMINATED)
						{
							orgCallid = originalRequest.getCallId();
							responseToOriginalRequest = originalRequest.createResponse(status,response.getReasonPhrase());
							responseToOriginalRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
							responseToOriginalRequest.setHeader(Const.sip_header_SV, rootCfg.version);
							responseToOriginalRequest.send();
							InvLog.InvLog.info("[CoreSrv]:---------------["+response.getStatus()+" response] :" +responseToOriginalRequest);
							InvLog.InvLog.info(responseToOriginalRequest);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						utilMethod.logExceptions(e);
					} 

					
					
					/**
					 *   DELETE TEK INFO
					 */
					if(	response.getHeader(Const.sip_header_UA)!=null 
							&& response.getHeader(Const.sip_header_UA).equals(Const.sip_vmgw_UA)
							&&response.getMethod().equals(Request.INVITE)
							){

						if(null!=orgCallid)
						{
							if(originalRequest != null && status != 487){				
								if(status!= 408	&& response.getMethod().equalsIgnoreCase("INVITE"))
								{
									String ua = response.getHeader(Const.sip_header_UA);
									if(ua!=null && ua.contains(Const.sip_vmgw_UA))
									{
										/*
										 *   CallLogWs
										 */
										CallLogWs.callLogAbort(originalRequest.getCallId(), String.valueOf(status));					
									}
								}
							}						
						}
					}					
				}else{
					InvLog.InvLog.info("[CoreSrv]: --------------[Error]: originalRequest is null/originalRequest.getApplicationSession() is invalid.");
					return;  
				}
			}
			
		}catch (Exception e) {
			utilMethod.logExceptions(e);
		}
	}

	/* (non-Javadoc)     
	 * @see javax.servlet.sip.SipServletListener#servletInitialized(javax.servlet.sip.SipServletContextEvent)     
	 */
	public void servletInitialized(SipServletContextEvent sce) {

		//System.err.println("----------------------scscf------------------");
		// TODO Auto-generated method stub
		ServletContext sc = sce.getServletContext();

		/**
		 *  初始化程序使用的目录路径
		 */
		rootCfg.rootPath = sc.getRealPath("").split("webapps")[0];		
		loadCfg.cfgPath = sc.getRealPath("");

		/**
		 *  初始化Log4j的配置文件
		 */
		PropertyConfigurator.configure(sc.getRealPath("")+"/log4j.properties");

		/**
		 *  加载sip.xml资源
		 */
		//rootCfg.open_lbc         = Boolean.valueOf(sc.getInitParameter("open_lbc"));
		//rootCfg.version			 = sc.getInitParameter("version");		
		//rootCfg.Hsswsdl 			 = sc.getInitParameter("wsdl");

		/**
		 *  初始化应用服务
		 */
		initial();					
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.sip.TimerListener#timeout(javax.servlet.sip.ServletTimer)
	 */
	public void timeout(ServletTimer timer) {
		try {	
			SipServletRequest request = (SipServletRequest) timer.getInfo();				
			/*
			 *   response  488 to vmgw
			 */
			if(	!(request.getSession().getState() == (State.TERMINATED)) && request.getSession().isValid()){
				try {
					SipServletResponse errresponse = request.createResponse(488,"INVITE_TIMEOUT");
					errresponse.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
					errresponse.setHeader(Const.sip_header_SV, rootCfg.version);
					errresponse.send();
					InvLog.InvLog.info("[CoreSrv]: ------------- [ERROR] : [INVITE_TIMEOUT]:"+errresponse);

					/*
					 *  send cancel to callee
					 */
					SipServletRequest invitecallee = (SipServletRequest) request.getSession().getAttribute("forwardRequestToCallee");
					SipServletRequest canceltocalleetimeout = invitecallee.createCancel();
					InvLog.InvLog.info("[CoreSrv]: ------------- [CANCEL] : [CANCEL TO CALLEE]:"+canceltocalleetimeout);
					canceltocalleetimeout.send();	
				} catch (IOException e) {}		
			}
			return;
		} catch (Exception e) {}		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.sip.SipErrorListener#noAckReceived(javax.servlet.sip.SipErrorEvent)
	 */
	public void noAckReceived(SipErrorEvent err) {
		// TODO Auto-generated method stub
		SipServletRequest request = err.getRequest();
		try {
			SipSession sess = request.getSession();
			/*
			 *    CallLog
			 */
			HssWs.callLogAbort(request.getCallId(), "NO ACK");

			if(sess.getState() == (State.CONFIRMED)) {

				SipServletRequest bye = sess.createRequest("BYE");
				bye.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
				bye.send();

				//we forward the BYE
				SipSession session = request.getSession();
				B2buaHelper helper = request.getB2buaHelper();
				SipSession linkedSession = helper.getLinkedSession(session);  

				if(linkedSession != null
						&& linkedSession.getState() == (State.CONFIRMED))
				{
					SipServletRequest forkedRequest = linkedSession.createRequest("BYE");
					forkedRequest.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
					forkedRequest.send();   
					InvLog.InvLog.info("[CoreSrv]: -------------[noAckReceived], [BYE linkedRequest] : \n"+forkedRequest);
				}				
			}else {
				InvLog.InvLog.info("[CoreSrv]: -------------[noAckReceived]. [Session] Terminated. [Request]:\n"+request);
			}

		} catch (IOException e) {utilMethod.logExceptions(e);} 
	}

	public void noPrackReceived(SipErrorEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void sessionCreated(SipApplicationSessionEvent ss) {
		// TODO Auto-generated method stub
		//System.err.println("------------------sessionCreated---\n");
		SipApplicationSession session = ss.getApplicationSession();	
		InitLog.initLog.info("[CoreSrv]: -------------[sessionCreated]. [Session] Created. [session]:"+session.getId());
	}

	@Override
	public void sessionDestroyed(SipApplicationSessionEvent as) {
		// TODO Auto-generated method stub
		//as.getApplicationSession().getSipSession(SIP_FACTORY).
		//System.err.println("------------------sessionDestroyed---");
		SipApplicationSession session = as.getApplicationSession();
		/*	try{
			session..invalidate();
		}catch(Exception e){

		}*/
		InitLog.initLog.info("[CoreSrv]: -------------[sessionDestroyed]. [Session] Terminated. [session]:"+session.getId());
	}

	@Override
	public void sessionExpired(SipApplicationSessionEvent ss) {
		// TODO Auto-generated method stub
		//System.err.println("------------------sessionExpired---\n");
		SipApplicationSession session = ss.getApplicationSession();
		/*if(session.isValid())
			session.invalidate();*/
		InitLog.initLog.info("[CoreSrv]: -------------[sessionExpired]. [Session] Expired. [session]:"+session.getId());
	}

	@Override
	public void sessionReadyToInvalidate(SipApplicationSessionEvent arg0) {
		// TODO Auto-generated method stub
		//System.err.println("------------------sessionReadyToInvalidate---\n");
	}
}
