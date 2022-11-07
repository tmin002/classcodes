import java.io.*;
import java.net.*;
import java.util.*;

public class LocalDns extends Server {

    // DNS 캐시. key: 도메인, value: ip
    private HashMap<String, String> dnsCache = new HashMap<String, String>();

    // 현재 처리중인 request 
    private HashMap<Integer, String> currentRequests = new HashMap<Integer, String>();

    // root 서버 주소
    private static Address rootDnsAddress = new Address("localhost", 59001);

    // LocalDns 생성자
    public LocalDns(int port) {
        super(port, "Local DNS");
    }


    // 클라이언트 고유번호 기본값.
    private int connectionCount = 0;

    // 클라이언트 리스트. 
    Map<Integer, Address> connectionList = new HashMap<Integer, Address>();

    // 여러 쓰레드에서 쓰기 작업시 충돌발생 우려.
    // 따라서 이 두 함수들은 synchornized로 선언하여 세마포어 형태로 동작하도록 함.
    private synchronized int addConnectionCount() {
        return connectionCount++;
    }
    private synchronized int subConnectionCount() {
        return connectionCount--;
    }

    // 고유번호 생성 및 클라이언트 리스트에 넣기
    private int addConnection(InetAddress ip, int port) {
        int idx = addConnectionCount();
        connectionList.put(idx, new Address(ip, port));
        return idx;
    }

    @Override
    public void onReceive(String[] received, InetAddress ip, int port) throws IOException {

        if (received.length != 3) {
            // Wrong format, ignore
            return;
        }

        // 명령어와 인수 변수 선언
        String cmd = received[1];
        String arg = received[2];

        int idx;
        if (cmd.equals("request")) {

            // 캐시 뒤지기
            if (dnsCache.containsKey(arg)) {
                // 찾았으므로 보내고 종료
                sendTo(new Address(ip, port), -1, "found", dnsCache.get(arg));
                return;
            }

            // 접속 목록에 추가하고 root dns에 요청
            idx = addConnection(ip, port);
            sendTo(rootDnsAddress, idx, "request", arg);
            currentRequests.put(idx, arg);

        } else if (cmd.equals("found") || cmd.equals("notfound")) {

            // 찾음. 
            idx = Integer.parseInt(received[0]);
            sendTo(connectionList.remove(idx), -1, cmd, arg);
            subConnectionCount();

            // dnsCache에 추가
            if (cmd.equals("found")) {
                dnsCache.put(currentRequests.remove(idx), arg);
            }

        } else if (cmd.equals("another")) {

            // 다른 서버에 물어보기
            idx = Integer.parseInt(received[0]);
            String[] parsed = arg.split(":");
            sendTo(new Address(parsed[0], Integer.parseInt(parsed[1])), idx, "request", currentRequests.get(idx));

        } else {
            // Wrong format, ignore
            return;
        }
        
    }
}