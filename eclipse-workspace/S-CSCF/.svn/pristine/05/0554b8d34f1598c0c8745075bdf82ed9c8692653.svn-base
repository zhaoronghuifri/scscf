package org.fri.ws.api;

public class ScscfWSProxy implements org.fri.ws.api.ScscfWS {
  private String _endpoint = null;
  private org.fri.ws.api.ScscfWS scscfWS = null;
  
  public ScscfWSProxy() {
    _initScscfWSProxy();
  }
  
  public ScscfWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initScscfWSProxy();
  }
  
  private void _initScscfWSProxy() {
    try {
      scscfWS = (new org.fri.ws.api.ScscfWSServiceLocator()).getScscfWS();
      if (scscfWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)scscfWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)scscfWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (scscfWS != null)
      ((javax.xml.rpc.Stub)scscfWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.fri.ws.api.ScscfWS getScscfWS() {
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS;
  }
  
  public void callLogInit(java.lang.String callid, java.lang.String caller, java.lang.String callee) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    scscfWS.callLogInit(callid, caller, callee);
  }
  
  public int callLogUpdate(java.lang.String callid, java.lang.String sipState) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.callLogUpdate(callid, sipState);
  }
  
  public void callLogAbort(java.lang.String callid, java.lang.String phrase) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    scscfWS.callLogAbort(callid, phrase);
  }
  
  public java.lang.String recKeySelect(java.lang.String ueid) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.recKeySelect(ueid);
  }
  
  public int ueInsertInfo(java.lang.String ueid, java.util.Calendar time, java.lang.String address, java.lang.String status, java.lang.String special, java.lang.String domain, java.lang.String kek) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.ueInsertInfo(ueid, time, address, status, special, domain, kek);
  }
  
  public int ueDeleteInfo(java.lang.String ueid) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.ueDeleteInfo(ueid);
  }
  
  public int tekInsert(java.lang.String callid, java.lang.String caller, java.lang.String callee, java.lang.String tek) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.tekInsert(callid, caller, callee, tek);
  }
  
  public java.lang.String tekSelect(java.lang.String callid) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.tekSelect(callid);
  }
  
  public int ueUpdateInfo(java.lang.String ueid, java.util.Calendar time, java.lang.String address, java.lang.String status, java.lang.String special, java.lang.String domain, java.lang.String kek) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.ueUpdateInfo(ueid, time, address, status, special, domain, kek);
  }
  
  public int tekDelete(java.lang.String callid) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.tekDelete(callid);
  }
  
  public java.lang.String ueSelectInfo(java.lang.String ueid) throws java.rmi.RemoteException{
    if (scscfWS == null)
      _initScscfWSProxy();
    return scscfWS.ueSelectInfo(ueid);
  }
  
  
}