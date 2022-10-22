import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;
import java.io.*;

public class Connection {
    public static Socket socket;
    public static BufferedReader socketInput;
    public static PrintWriter socketOutput;

    public static void connect() {
		// 주소와 포트를 저장하는 변수들. 기본값은 다음과 같음.
        String addr = "127.0.0.1";
        int port = 35353;

        try {
			// server_data.dat에서 서버 정보 불러오기.
            File f = new File("server_data.dat");
            if (f.exists()) {
                Scanner sc = new Scanner(f);
                addr = sc.nextLine();
                port = Integer.parseInt(sc.nextLine());
                sc.close();
            } else {
				// 만약 파일이 존재하지 않는다면, 기본값으로 load.
                Window.msgBox("Configuration file not found.\nConnecting to default address " 
                    + addr + ":" + port);
            }

			// 소켓 정의하고 열기. I/O 스트림도 소켓에서 받아서 생성함.
			// PrintWriter의 autoFlush를 true로 설정하여 자동으로 메시지가 전송되도록 함.
            socket = new Socket(addr, port);
            socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			// handshake 부분. 
            sendMessage("HI");
            String handshakeResult = getMessage();
            if (handshakeResult == null) {
				// 입력 버퍼가 빈 상태로 리턴되었다는 것은 서버가 연결을 강제로 종료했다는 것.
				// 따라서 연결이 끊겼다는 메시지 출력
                throw new Exception("Connection lost while handshake");
            } else if (!(handshakeResult.equals("HELLO"))) {
				// 잘못된 메시지를 보냈다는 것은 잘못된 프로토콜을 따르고 있다는 것.
				// 따라서 잘못된 프로토콜 쓴다고 메시지 출력.
                throw new Exception("Wrong protocol");
            } 

        } catch (Exception e) {
			// 서버와 연결하며 생긴 예외를 출력후 종료.
            showException(e, "connecting to");
        }
    }

	// 연결을 종료하는 함수.
    public static void disconnect() {
        try {
            sendMessage("BYE");
        } catch (Exception e) {
			// 종료 메시지 전송중 오류 발생. 출력 후 종료
            showException(e, "disconnecting from");
        }
    }

	// Window.msgBox()를 사용하여 오류 내용과 오류 발생이전 실행하던 동작 출력.
	// 출력후에는 프로그램을 코드1로 종료함.
    public static void showException(Exception e, String verb) {
        Window.msgBox("Excpetion while " + verb + " server\n" + e);
        System.exit(1);
    }

	// 서버에 메시지를 전송하는 함수.
    public static void sendMessage(String msg) throws IOException {
        socketOutput.println(msg);
    }
	// 소켓의 입력(수신) 버퍼에서 값을 가져오는 함수.
    public static String getMessage() throws IOException {
        String result = socketInput.readLine();
        return result;
    }
}
