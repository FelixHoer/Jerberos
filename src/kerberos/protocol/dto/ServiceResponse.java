package kerberos.protocol.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceResponse {

    private Encrypted timePlusOne;

    public Encrypted getTimePlusOne() {
        return timePlusOne;
    }

    public void setTimePlusOne(Encrypted timePlusOne) {
        this.timePlusOne = timePlusOne;
    }

}
