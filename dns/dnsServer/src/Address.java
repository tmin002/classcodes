import java.net.*;

// 아이피, 포트를 저장하기 위한 클래스.
public class Address {
    private InetAddress ip;    
    private int port;

    public Address(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    public Address(String ip, int port) {
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException ignored) {}
        this.port = port;
    }

    public InetAddress getIP() {
        return this.ip;
    }
    public int getPort() {
        return this.port;
    }
    
    @Override
    public String toString() {
        return ip.getHostAddress() + ":" + port;
    }
}
