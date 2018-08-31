package timely.balancer.connection;

import timely.balancer.BalancerConfiguration;

public class TimelyBalancedHost {

    private String host;
    private int tcpPort;
    private int httpPort;
    private int wsPort;
    private int udpPort;
    private boolean isUp = true;
    private BalancerConfiguration config;
    private int failures = 0;
    private int successes = 0;

    public TimelyBalancedHost() {

    }

    public TimelyBalancedHost(String host, int tcpPort, int httpPort, int wsPort, int udpPort) {
        this.host = host;
        this.tcpPort = tcpPort;
        this.httpPort = httpPort;
        this.wsPort = wsPort;
        this.udpPort = udpPort;
    }

    public TimelyBalancedHost(String host, int basePort) {
        this.host = host;
        this.tcpPort = basePort;
        this.httpPort = basePort + 1;
        this.wsPort = basePort + 2;
        this.udpPort = basePort + 3;
    }

    static public TimelyBalancedHost of(String host, int tcpPort, int httpPort, int wsPort, int udpPort) {
        return new TimelyBalancedHost(host, tcpPort, httpPort, wsPort, udpPort);
    }

    static public TimelyBalancedHost of(String host, int basePort) {
        return new TimelyBalancedHost(host, basePort);
    }

    public void setConfig(BalancerConfiguration config) {
        this.config = config;
    }

    synchronized public boolean isUp() {
        return isUp;
    }

    public void reportSuccess() {
        int serverSuccessesBeforeUp = config.getServerSuccessesBeforeUp();
        synchronized (this) {
            if (!isUp) {
                if (++successes >= serverSuccessesBeforeUp) {
                    isUp = true;
                    successes = 0;
                }
            }
        }
    }

    public void reportFailure() {
        int serverFailuresBeforeDown = config.getServerFailuresBeforeDown();
        synchronized (this) {
            if (isUp) {
                if (++failures >= serverFailuresBeforeDown) {
                    isUp = false;
                    failures = 0;
                }
            }
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getWsPort() {
        return wsPort;
    }

    public void setWsPort(int wsPort) {
        this.wsPort = wsPort;
    }
}
