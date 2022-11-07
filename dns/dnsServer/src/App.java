import java.util.*;
import java.io.File;

public class App {

    // 프로그램 argument에 잘못된 값이 들어왔을 경우 종료하는 함수.
    public static void wrongUse(String msg) {
        System.out.println("Incorrect use : " + msg);
        System.exit(1);
    }

    // 프로그램 argument 정보: 
    //
    // java -jar dnsServer.jar <서버 종류> <TLD 도메인리스트 파일 경로>
    // java -jar dnsServer.jar [local/root]
    // java -jar dnsServer.jar tld <tld_domain_list_file_location>
    //
    public static void main(String[] args) throws Exception {

        // arguement 형식이 잘못됨.
        if (args.length < 2) {
            wrongUse("Too few arguments.");
        }

        // Server 객체 만들기
        Server server = null;

        // argument로 전달된 Server port 받아오기
        int argPort = -1;
        try {
            argPort = Integer.parseInt(args[1]); 
        } catch (NumberFormatException e) {
            wrongUse("Port number not numeric.");
        }
        assert argPort != -1;

        // argument로 전달된 서버 종류에 따라 Server 객체에 값 넣기
        String arg = args[0].toLowerCase();
        if (arg.equals("root")) {
            server = new RootDns(argPort);
        } else if (arg.equals("tld")) {

            if (args.length < 3) {
                wrongUse("TLD mode requires at least 3 arguments.");
            }
            if (!(new File(args[2]).exists())) {
                wrongUse("TLD domain list file path wrong.");
            }

            // tld전용 argument (3번째)로부터 읽어오기
            HashMap<String, String> tldMap = new HashMap<String, String>();
            String path = args[2];
            Scanner sc = new Scanner(new File(path));

            String extNameRaw = sc.nextLine();
            String extension = extNameRaw.substring(1, extNameRaw.length()-1);

            while (sc.hasNextLine()) {
                String[] thisLine = sc.nextLine().split(" ");
                tldMap.put(thisLine[0], thisLine[1]);
            }

            server = new TLDDns(argPort, extension, tldMap);

        } else if (arg.equals("local")) {
            server = new LocalDns(argPort);
        } else {
            wrongUse("Incorrect server mode");
        }

        // 서버 시작
        server.startServer();
    }
}
