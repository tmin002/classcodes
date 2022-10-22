package SocketCalculatorServer;

// 계산 시에만 발생하는 예외를 다른 예외로부터 구분하기 위해
// 만든 Exception 클래스
public class CalculateException extends Exception {
    public CalculateException(String errorMessage) {
        super(errorMessage);
    }
}
