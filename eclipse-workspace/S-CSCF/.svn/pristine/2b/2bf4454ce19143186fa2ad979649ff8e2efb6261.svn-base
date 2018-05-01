package org.fri.pdm;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.fri.cfg.rootCfg;
import org.fri.kmc.VtekServiceImplServiceStub;
import org.fri.kmc.VtekServiceImplServiceStub.GetVtek;
import org.fri.util.utilMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PdmTek {
	/**
	 * @param callid
	 * @param caller
	 * @param callee
	 */
	public static String getCallTek(String callid,String caller,String callee){
		try {
			JSONObject tekJson = JSONObject.fromObject(getTek(callid,caller,callee));
			if(0 == (tekJson.getInt("resultcode")))
			{
				JSONObject tek = JSONObject.fromObject(tekJson.get("tek"));			
				return tek.getString(callee);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @param callid
	 * @param caller
	 * @param callee
	 * @return
	 */
	public static String getTek(String callid,String caller,String callee){
		try {
			//InitLog.initLog.info("[SELECT] TekInfo from PDM DB.  ["+true+"], [callid] :"+callid);
			VtekServiceImplServiceStub stub = new VtekServiceImplServiceStub();
			VtekServiceImplServiceStub.GetVtekE getVTekE = new VtekServiceImplServiceStub.GetVtekE();
			GetVtek vtek = new GetVtek();

			JSONArray phonenumJArr = new JSONArray();
			JSONObject phonenum = new JSONObject();
			phonenum.put("phonenum", caller);
			phonenumJArr.add(phonenum);
			JSONObject phonenum2 = new JSONObject();
			phonenum2.put("phonenum", callee);
			phonenumJArr.add(phonenum2);		


			JSONObject request = new JSONObject();
			request.put("callid", callid);
			request.put("phonenums", phonenumJArr);

			//InitLog.initLog.info("[SELECT] TekInfo from PDM DB.  [request] :"+request);

			vtek.setRequest(request.toString());
			getVTekE.setGetVtek(vtek);

			String result = stub.getVtek(getVTekE).getGetVtekResponse().get_return();
			//System.out.println(result);
			return result;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
		}
		return null;
	}

	public static void main(String args[]) throws InterruptedException{
		rootCfg.pdmkmc_url="http://123.57.18.235:9088/PDMKmc/service/VtekService?wsdl";
		for(int index =0;index<1000;index++)
		{
			Thread.sleep(100);
			System.out.println(getCallTek("dwedwefwefwef","18911070385","18611068603"));
		}
	}
}
