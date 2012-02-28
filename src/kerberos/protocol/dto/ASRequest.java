package kerberos.protocol.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ASRequest {

	private String clientName;
	private String serviceName;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
}
