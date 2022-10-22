package SocketCalculatorServer;
import SocketCalculatorServer.expressions.ParseExpression;
import java.io.*;
import java.net.*;

// 한 클라이언트와 소켓과 소켓을 관리하는 쓰레드를 담고있는 객체
public class Connection implements Runnable {

    private Socket client;
    private String addr;
    private BufferedReader in;
    private PrintWriter out;
    private Thread thread;

	// 생성시 소켓 정보를 가져오고, 쓰레드를 시작함
    public Connection(Socket client) {
        this.client = client;
        this.thread = new Thread(this);
        this.addr = client.getInetAddress().getHostAddress();
        thread.start();
    }

	// 클라이언트와의 연결 종료하기
    public void disconnect() {
        System.out.printf("* Closing socket with client %s\n", addr);
        out.close(); // 소켓을 닫으면, run() 부분에서는 SocketException이 발생하여 쓰레드가 종료됨
        Server.connectionList.remove(this); // 연결 리스트에서 나를 제거함
    }

    public void run() {
		// 쓰레드가 시작되었으므로 쓰레드 개수 +1
        Server.addConnectionThreadCount();

        try {
			// 소켓 I/O 스트림 선언
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            // Handshake 부분. 만약 잘못된 응답을 한다면 프로토콜을 따르지 않는 것이므로 연결 종료.
            String clientMessageRaw = in.readLine();
            if (!(clientMessageRaw.equals("HI"))) {
                System.out.println("!! Client is using wrong protocol. Terminating connection.");
                client.close();
                return;
            } else {
                out.println("HELLO");
                    System.out.printf("* Handshake with client %s successful.\n", addr);
            }

			// 연결된 클라이언트로부터의 응답을 기다리기 
            while (true) {
                clientMessageRaw = in.readLine();
                if (clientMessageRaw == null) {
					// 입력 버퍼가 빈 상태로 리턴되는 경우는 클라이언트가 연결을 강제로 종료했을 
					// 때 생김. 따라서 연결 종료
                    System.out.printf("* Client %s terminated connection. Terminating thread.\n", addr);
                    disconnect();
                    break;
                } else if (clientMessageRaw.equals("BYE")) {
					// 클라이언트가 접속 종료를 선언함. 연결 종료
                    System.out.printf("* Client %s asserted disconnection. Terminating thread.\n", addr);
                    disconnect();
                    break;
                }
                try {
					// 클라이언트가 수식을 입력하였고, 계산하여 결과 보내기
                    out.println("OK;" + ParseExpression.calculateExpression(clientMessageRaw.split(";")[1]));
                } catch (CalculateException e) {
					// 계산중 계산 관련 오류가 발생하여 오류 코드를 보냄
                    out.println("ERR;" + e.getMessage());
                } catch (Exception e) {
					// 계산중 알수없는 오류가 발생하여 오류 내용을 보냄
                    out.println("UNKNOWN_ERR;" + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (SocketException e) {
			// 소켓 연결이 끊어짐. 쓰레드 종료.
            System.out.printf("* Socket connection with %s closed, terminating thread.\n", addr);
        } catch (Exception e) {
			// 알수없는 예외가 발생함. 출력하고 쓰레드 종료.
            System.out.println("!! Exception: " + e + ", terminating thread.");
        }

		// 쓰레드가 종료되기 전 반드시 거치는 부분.
		// 쓰레드가 종료되기 때문에 쓰레드 개수 -1.
        Server.subConnectionThreadCount();
    }

}
