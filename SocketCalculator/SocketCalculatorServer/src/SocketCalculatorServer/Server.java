package SocketCalculatorServer;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

	// 클라이언트와의 연결 소켓과 소켓을 관리하는 쓰레드를 담고 있는
	// Connection 객체들의 리스트
    public static ArrayList<Connection> connectionList = new ArrayList<Connection>();

 	// 서버 실행 포트	
    private final static int port = 9999;

	// 클라이언트와의 연결로 생성된 쓰레드의 개수.
    private static int connectionThreadCount = 0;

	// 서버 소켓을 열고 클라이언트를 기다리는 쓰레드를 생성 후 실행.
	// 실행 후 해당 함수는 종료됨
    public static void startServerAsync() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                operation();
            }
        });
        t.start();
    }

	// 서버 소켓을 열고 클라이언트를 기다리는 함수
    public static void operation() {
        ServerSocket server = null;
  
        try {
			// 서버 소켓 정의 후 열기
            server = new ServerSocket(port);
            server.setReuseAddress(true);
  
            while (true) {
  
  				// 클라이언트 기다리기
                Socket client = server.accept();
				// 클라이언트가 응답함
                System.out.println("* Connection attempt from "
                    + client.getInetAddress().getHostAddress());
  
  				// Connection 객체를 생성함. 객체 생성과 동시에 쓰레드가 만들어짐
                Connection connection = new Connection(client);
				// 만든 객체를 static 리스트에 넣기
                connectionList.add(connection);
            }
        } 
        catch (BindException e) {
			// 다른 프로그램이 이미 포트를 사용중
            System.out.printf("!! Failed to bind to port %d. Address is already in use.\n", port);
            System.exit(1);
        } catch (IOException e) {
			// I/O 오류가 발생함
            System.out.printf("!! IO Error while opening socket: %s\n", e.getMessage());
        }
    }

	// 현재 실행중인 클라이언트와의 통신을 담당하는 쓰레드의 개수
    public static int getConnectionThreadCount() {
        return connectionThreadCount;
    }

	// 쓰레드의 개수에 +1 또는 -1 하는 함수들.
	// 여러개의 서로다른 쓰레드가 connectionThreadCount에 액세스 할시 concurrency 오류가
	// 발생할 여지가 있어 synchronized로 선언하여 세마포어 형태로 동작하도록 함.
    public synchronized static void addConnectionThreadCount() {
        connectionThreadCount++;
    }
    public synchronized static void subConnectionThreadCount() {
        connectionThreadCount--;
    }
}
