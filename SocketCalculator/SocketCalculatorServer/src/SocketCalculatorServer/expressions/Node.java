package SocketCalculatorServer.expressions;

public class Node {
    public double number;
    public char operator;
    public Node right = null;
    public Node left = null;

	// 각 노드는 숫자노드 또는 연산자 노드임.
	// 숫자노드일 경우 left와 right가 null이며,
	// 연산자노드일 경우 left와 right가 null이 아님.
    public Node(double number, char operator) {
        this.number = number;
        this.operator = operator;
    }
}
