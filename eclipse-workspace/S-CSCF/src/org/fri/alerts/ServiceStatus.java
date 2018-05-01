/**
 * 
 */
package org.fri.alerts;

import java.io.IOException;

import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.sip.message.Request;

import org.fri.cfg.Const;
import org.fri.cfg.rootCfg;
import org.fri.log.InvLog;
import org.fri.sip.CoreSrv;
import org.fri.util.utilMethod;

/**
 * @author Andy
 *
 */
public class ServiceStatus {
	public static int localStatus = 0;
	public static int hssStatus   = 0 ;
	public static int vmgwStatus  = 0;
	public static int PdmStatus   = 0 ;


	/**
	 * @param request
	 */
	public static boolean checkServiceStatus(SipServletRequest request){

		/*
		 *   check Service Status and response err 
		 */
	
		/*if(request.getMethod().equals(Request.INVITE))
		{
			if(ServiceStatus.hssStatus !=0)
			{
				respondHSSServiceError(request);
				return false;
			}else if(ServiceStatus.vmgwStatus !=0)
			{
				respondVMGWServiceError(request);
				return false;

			}else if(ServiceStatus.PdmStatus !=0)
			{
				respondPDMServiceError(request);
				return false;
			}
		}*/
		
		return true;
	}

	/**
	 * @param request
	 * @return
	 */
	public static void respondSipServiceError(SipServletRequest request){
		try {

			SipServletResponse err = request.createResponse(503,rootCfg.SERVER_SIP_EXCEPTION);
			err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			err.setHeader(Const.sip_header_SV, rootCfg.version);
			err.send();
			CoreSrv.log.info("[CoreSrv]: ------------- [ERROR] : [SERVER_SIP_EXCEPTION_ALERT]. "+err);		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param request
	 * @return
	 */
	public static void respondHSSServiceError(SipServletRequest request){
		try {

			SipServletResponse err = request.createResponse(503,rootCfg.SERVER_HSS_EXCEPTION);
			err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			err.setHeader(Const.sip_header_SV, rootCfg.version);
			err.send();

			CoreSrv.log.info("[CoreSrv]: ------------- [ERROR] : [SERVER_HSS_EXCEPTION_ALERT]. "+err);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param request
	 * @return
	 */
	public static void respondVMGWServiceError(SipServletRequest request){
		try {

			SipServletResponse err = request.createResponse(503,rootCfg.SERVER_VMGW_EXCEPTION);
			err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			err.setHeader(Const.sip_header_SV, rootCfg.version);
			err.send();

			CoreSrv.log.info("[CoreSrv]: ------------- [ERROR] : [SERVER_VMGW_EXCEPTION_ALERT]. "+err);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param request
	 * @return
	 */
	public static void respondPDMServiceError(SipServletRequest request){
		try {

			SipServletResponse err = request.createResponse(503,rootCfg.SERVER_PDM_EXCEPTION);
			err.addHeader(Const.sip_header_DATE, utilMethod.getDateFormat());
			err.setHeader(Const.sip_header_SV, rootCfg.version);
			err.send();

			CoreSrv.log.info("[CoreSrv]: ------------- [ERROR] : [SERVER_PDM_EXCEPTION_ALERT]. "+err);				

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
