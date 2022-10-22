package SocketCalculatorServer;
import java.util.Scanner;
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Socket Calculator Server");
        System.out.println("202135785 Song Seung Hwan");
        System.out.println("** Press enter to terminate\n");

        // Start Server 
        Server.startServerAsync();

        // Detect enter key press
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextLine());
        sc.close();

        // Close sockets
        while (!(Server.connectionList.isEmpty())) {
            Server.connectionList.get(0).disconnect();
        }

        // Exit after all thread terminated
        while (Server.getConnectionThreadCount() != 0) {}
        System.out.println("* All connections closed!");
        System.exit(0);
    }
}
