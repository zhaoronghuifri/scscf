/**
 * ScscfWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.fri.ws.api;

public interface ScscfWS extends java.rmi.Remote {
    public java.lang.String ueSelectInfo(java.lang.String ueid) throws java.rmi.RemoteException;
    public void callLogInit(java.lang.String callid, java.lang.String caller, java.lang.String callee) throws java.rmi.RemoteException;
    public int callLogUpdate(java.lang.String callid, java.lang.String sipState) throws java.rmi.RemoteException;
    public void callLogAbort(java.lang.String callid, java.lang.String phrase) throws java.rmi.RemoteException;
    public java.lang.String recKeySelect(java.lang.String ueid) throws java.rmi.RemoteException;
    public int ueInsertInfo(java.lang.String ueid, java.util.Calendar time, java.lang.String address, java.lang.String status, java.lang.String special, java.lang.String domain, java.lang.String kek) throws java.rmi.RemoteException;
    public int ueDeleteInfo(java.lang.String ueid) throws java.rmi.RemoteException;
    public int tekInsert(java.lang.String callid, java.lang.String caller, java.lang.String callee, java.lang.String tek) throws java.rmi.RemoteException;
    public java.lang.String tekSelect(java.lang.String callid) throws java.rmi.RemoteException;
    public int ueUpdateInfo(java.lang.String ueid, java.util.Calendar time, java.lang.String address, java.lang.String status, java.lang.String special, java.lang.String domain, java.lang.String kek) throws java.rmi.RemoteException;
    public int tekDelete(java.lang.String callid) throws java.rmi.RemoteException;
}
