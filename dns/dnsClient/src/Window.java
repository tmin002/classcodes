import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {

    public Window() {
		// JFrame 속성 설정
        super("DNS Client");
        setSize(400,80);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

		// JPanel root 선언. 2행1열의 레이아웃 사용
        JPanel root = new JPanel(new GridLayout(2, 1));

		// 1행에 들어갈 upperPanel 선언, 그리고 upperPanel에 들어갈 입력필드와 버튼 선언
        SpringLayout layout = new SpringLayout();
        JPanel upperPanel = new JPanel(layout);
        JButton button = new JButton("request");
        JTextField field = new JTextField(30);

		// upperPanel에 필드과 버튼 넣기
        upperPanel.add(field);
        upperPanel.add(button);

		// upperPanel의 필드과 버튼 위치 조정
        layout.putConstraint(SpringLayout.WEST, field, 5, SpringLayout.WEST, upperPanel);
        layout.putConstraint(SpringLayout.EAST, field, 5, SpringLayout.WEST, button);
        layout.putConstraint(SpringLayout.EAST, button, 5, SpringLayout.EAST, upperPanel);

		// 2행에 들어갈 JLabel result 선언
        JLabel result = new JLabel("Type domain and click '='. result will appear in here.", SwingConstants.CENTER);

		// upperPanel의 버튼을 클릭했을때 필드에 적힌 수식을 서버로 전송하는 부분
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showResult(result, getResult(field.getText()));
            }
        });
		
		// root의 1행과 2행에 각각 upperPanel과 result 넣기
        root.add(upperPanel);
        root.add(result);

		// JFrame에 root 넣고 창 띄우기
        add(root);
        setVisible(true);
    }

	// 수식 문자열을 서버로 보내고 결과값 가져오기
    private String getResult(String domain) {
        String result = null;
        try {
			// 서버로 수식 전송하기
            Connection.sendMessage("tmindns;;request;" + domain);
            result =  Connection.getMessage();
            if (result == null) {
				// 받아온게 없음. 
                throw new Exception("No data received!");
            }
        } catch (Exception e) {
			// 서버와 통신하다가 예외가 발생했다는 메시지 출력 후 종료.
            Connection.showException(e, "communicating with");
        }
		// 아무 문제가 없었다면 서버로부터 받은 결과값 반환
        return result;
    }

	// JLabel에 결과값을 보여주기 위한 함수
    private void showResult(JLabel label, String result) {
		// 결과 코드와 결과값을 저장하는 곳
        String[] data = result.split(";");

		// JLabel에 보여줄 텍스트
        String text = null;

        if (data[2].equals("found")) {
			// 수식이 문제없이 계산되었다면 결과값 보여주기
            label.setForeground(Color.BLACK);
            text = data[3];
        } else {
			// 수식에 문제가 있었다면 빨간색으로 오류 내용 보여주기
            label.setForeground(Color.RED);

            if (data[2].equals("nxdomain")) {
                text = "Not found";
            } else {
				// 만약 프로토콜에 따르지 않는 이상한 메시지가 왔다면  
				// 받아온 알수없는 오류 메시지를 그냥 보여주기.
                // 데이터가 손실되지 않은 이상 여기에 들어오면 안됨.
                text = data[2];
            }
        }

		// JLabel에 결과 반영
        label.setText(text);
    } 

	// 단순히 메시지박스를 띄우는 함수.
    public static void msgBox(String message) {
        JOptionPane.showMessageDialog(null, message, 
            "SocketCalculatorClient", JOptionPane.ERROR_MESSAGE);
    }

}