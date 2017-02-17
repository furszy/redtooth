package iop_sdk.profile_server.model;

import iop_sdk.global.HardCodedConstans;

/**
 * Created by mati on 05/02/17.
 */

public class ProfServerData {

    private String host;
    private int pPort = 16987;
    private int custPort;
    private int nonCustPort;

    private byte[] protocolVersion = HardCodedConstans.PROTOCOL_VERSION;

    public ProfServerData(String host) {
        this.host = host;
    }

    public ProfServerData(String host, int pPort) {
        this.host = host;
        this.pPort = pPort;
    }

    public ProfServerData(String host, int pPort, int custPort, int nonCustPort) {
        this.host = host;
        this.pPort = pPort;
        this.custPort = custPort;
        this.nonCustPort = nonCustPort;
    }

    public String getHost() {
        return host;
    }

    public int getpPort() {
        return pPort;
    }

    public int getCustPort() {
        return custPort;
    }

    public int getNonCustPort() {
        return nonCustPort;
    }

    public byte[] getProtocolVersion() {
        return protocolVersion;
    }

    public void setCustPort(int custPort) {
        this.custPort = custPort;
    }

    public void setNonCustPort(int nonCustPort) {
        this.nonCustPort = nonCustPort;
    }
}
