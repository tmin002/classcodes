import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Connection {

    private static DatagramSocket ds;
    private static InetAddress addr;
    private static int port = 59000; // 기본값

    public static void init() {
		// 주소를 저장하는 변수. 기본값은 다음과 같음.
        String address = "127.0.0.1";

        try {
			// server_data.dat에서 서버 정보 불러오기.
            File f = new File("server_data.dat");
            if (f.exists()) {
                Scanner sc = new Scanner(f);
                address = sc.nextLine();
                port = Integer.parseInt(sc.nextLine());
                sc.close();
            } else {
				// 만약 파일이 존재하지 않는다면, 기본값으로 load.
                Window.msgBox("Configuration file not found.\nConnecting to default address " 
                    + addr + ":" + port);
            }

            ds = new DatagramSocket();

            // addr (주소저장 global 변수)에 저장
            addr = InetAddress.getByName(address);

        } catch (Exception e) {
			// 서버와 연결하며 생긴 예외를 출력후 종료.
            showException(e, "connecting to");
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
        byte[] encoded = msg.getBytes();
        DatagramPacket dp = new DatagramPacket(encoded, encoded.length, addr, port);
        ds.send(dp);
    }
	// 소켓의 입력(수신) 버퍼에서 값을 가져오는 함수.
    public static String getMessage() throws IOException {

        // Receive data
        byte[] bf = new byte[512];
        DatagramPacket dp = new DatagramPacket(bf, bf.length);
        ds.receive(dp);

        // Remove zeros from buffer
        byte[] data = new byte[dp.getLength()];
        System.arraycopy(dp.getData(), dp.getOffset(), data, 0, dp.getLength());

        // Return converted string
        return new String(data);
    }
}