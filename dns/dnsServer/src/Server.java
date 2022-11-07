import java.net.*;
import java.io.*;
import java.util.*;

// local, root, TLD 서버 클래스의 부모 클래스.
public abstract class Server {

    // 서버 포트.
    // 같은 포트로 한개의 컴퓨터에서 세개의 서버를 돌리는 것은 
    // 프로토콜 상의 오버헤드 때문에, 세개의 서버가 각기
    // 다른 포트를 사용하도록 구성함.
    private int port = -1; 

    // 공용 UDP 소켓.
    private static DatagramSocket ds;

    // 콘솔 상에서 세개의 다른 서버를 구분하기 위한 서버이름.
    private String name = "";

    // 객체 생성시 반드시 포트와 서버 이름이 주어져있어야함.
    public Server(int port, String name) {
        this.port = port;
        this.name = name;
    }

    // 콘솔 출력 함수.
    public void log(String msg) {
        System.out.print("[" + name + "] ");
        System.out.println(msg);
    }

    // 서버 시작하기. 강제 종료하지 않는이상 종료되지 않는 함수.
    public void startServer() {
        log("Started on port " + port);

        try {
            // 소켓 선언
            ds = new DatagramSocket(this.port);
            final byte[] bf = new byte[512];
            DatagramPacket dp;

            while (true) {

                // 버퍼 초기화
                for (int i=0; i<bf.length; i++)
                    bf[i] = 0;

                // 패킷 기다리기
                dp = new DatagramPacket(bf, bf.length);
                ds.receive(dp);

                // 쓰레드 시작
                new Thread(new rcv(dp, bf)).start();
                
            }
        } catch (BindException e) {
            // 이미 사용중인 주소.
            log("Address already in use!");
            System.exit(1);
        } catch (IOException e) {
            // IOException 발생.
            log("exception " + e);
            e.printStackTrace();
        } finally {
            // 끝나면 소켓 닫기
            if (!ds.isClosed())
                ds.close();
        }

    }

    // startServer에서 UDP패킷을 감지했을때 이 함수를 호출함.
    // 세개의 서버별로 다른 일을 하므로 abstract로 선언.
    public abstract void onReceive(String[] received, InetAddress senderIp, int senderPort) throws IOException;

    // UDP 패킷 전송 함수.
    public void sendTo(Address addr, int idx, String cmd, String arg) throws IOException {
        // 전송할 String 만들기
        StringBuilder buf = new StringBuilder("tmindns;");
        buf.append(idx + ";").append(cmd + ";").append(arg);

        // String byte array로 변환
        byte[] encoded = buf.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(encoded, encoded.length, addr.getIP(), addr.getPort());
        ds.send(packet);

        log("[send] to: " + addr + ", content: " + buf);
    }

    // 패킷 받았을때 대응할 쓰레드를 위한 클래스
    private class rcv implements Runnable {
        private DatagramPacket dp;
        private byte[] bf;

        // 생성자. 수신된 패킷과 버퍼를 넘겨받음
        public rcv(DatagramPacket dp, byte[] bf) {

            // Remove zeros from buffer
            byte[] data = new byte[dp.getLength()];
            System.arraycopy(dp.getData(), dp.getOffset(), data, 0, dp.getLength());

            // Assign data
            this.dp = dp;
            this.bf = data;
        }

        @Override
        public void run() {
            // 내용 String으로 변환
            String rcv = new String(bf);

            // 콘솔로 내용과 소스ip 출력
            log("[rcv] from: " + dp.getAddress() + ", content: " + rcv);

            // 받은 string을 프로토콜에 따라 파싱
            String[] parsed = rcv.split(";");

            if (parsed == null || parsed.length != 4 || !(parsed[0].equals("tmindns"))) {
                // 프로토콜을 따르지 않으므로 무시 
                log("Wrong protocol, ignoring.");
                return;
            }

            // onReceive 호출
            try {
                onReceive(Arrays.copyOfRange(parsed, 1, parsed.length), dp.getAddress(), dp.getPort());
            } catch (IOException e) {
                log("exception: " + e);
            }
        }
    }
}