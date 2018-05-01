/**
 * ScscfWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.fri.ws.api;

public class ScscfWSServiceLocator extends org.apache.axis.client.Service implements org.fri.ws.api.ScscfWSService {

    public ScscfWSServiceLocator() {
    }


    public ScscfWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ScscfWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ScscfWS
    private java.lang.String ScscfWS_address = "http://localhost:8080/HssServer/services/ScscfWS";

    public java.lang.String getScscfWSAddress() {
        return ScscfWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ScscfWSWSDDServiceName = "ScscfWS";

    public java.lang.String getScscfWSWSDDServiceName() {
        return ScscfWSWSDDServiceName;
    }

    public void setScscfWSWSDDServiceName(java.lang.String name) {
        ScscfWSWSDDServiceName = name;
    }

    public org.fri.ws.api.ScscfWS getScscfWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ScscfWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getScscfWS(endpoint);
    }

    public org.fri.ws.api.ScscfWS getScscfWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.fri.ws.api.ScscfWSSoapBindingStub _stub = new org.fri.ws.api.ScscfWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getScscfWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setScscfWSEndpointAddress(java.lang.String address) {
        ScscfWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.fri.ws.api.ScscfWS.class.isAssignableFrom(serviceEndpointInterface)) {
                org.fri.ws.api.ScscfWSSoapBindingStub _stub = new org.fri.ws.api.ScscfWSSoapBindingStub(new java.net.URL(ScscfWS_address), this);
                _stub.setPortName(getScscfWSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ScscfWS".equals(inputPortName)) {
            return getScscfWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.fri.org", "ScscfWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.fri.org", "ScscfWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ScscfWS".equals(portName)) {
            setScscfWSEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
