import java.io.*;
import java.net.*;
import java.util.*;

public class RootDns extends Server {

    public RootDns(int port) {
        super(port, "Root DNS");
    }

    // TLD 주소 저장소. key: 도메인, value: tld 서버 주소
    private static Map<String, Address> TLDAdresses = new HashMap<String, Address>();
    static {
        TLDAdresses.put("com", new Address("localhost", 59002));
        TLDAdresses.put("net", new Address("localhost", 59003));
        TLDAdresses.put("org", new Address("localhost", 59004));
    }

    @Override
    public void onReceive(String[] received, InetAddress ip, int port) throws IOException {

        // 명령어와 인수 변수 선언
        int idx = Integer.parseInt(received[0]);
        String cmd = received[1];
        String arg = received[2];

        if (cmd.equals("request")) {

            // 도메인 파싱
            String dotstr = null;
            boolean parseFail = false;
            try {
                dotstr = arg.substring(arg.indexOf(".")+1, arg.length());
            } catch (IndexOutOfBoundsException e) {
                parseFail = true;
            }

            if (parseFail || !(TLDAdresses.containsKey(dotstr))) {
                // 없으므로 nxdomain
                sendTo(new Address(ip, port), idx, "nxdomain", arg);
            } else {
                // TLD 주소 보내기
                sendTo(new Address(ip, port), idx, "another", TLDAdresses.get(dotstr).toString());
            }
        } else {
            
        }
    }
}