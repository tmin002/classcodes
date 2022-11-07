import java.io.IOException;
import java.net.*;
import java.util.*;

public class TLDDns extends Server {

    private Map<String, String> domainMap;

    public TLDDns(int port, String extensionName, Map<String, String> domainMap) {
        super(port, "TLD DNS");

        // App.java에서 받아온 TLD 도메인 맵을 저장
        this.domainMap = domainMap;

        // 어떤 extension TLD인지 출력.
        log("extension name: " + extensionName);
    }

    @Override
    public void onReceive(String[] received, InetAddress ip, int port) throws IOException {

        // 명령어와 인수 변수 선언
        int idx = Integer.parseInt(received[0]);
        String cmd = received[1];
        String arg = received[2];

        if (cmd.equals("request")) {
            if (!(domainMap.containsKey(arg))) {
                // 없으므로 nxdomain
                sendTo(new Address(ip, port), idx, "nxdomain", arg);
            } else {
                // IP 주소 보내기
                sendTo(new Address(ip, port), idx, "found", domainMap.get(arg));
            }
        }
    }
}