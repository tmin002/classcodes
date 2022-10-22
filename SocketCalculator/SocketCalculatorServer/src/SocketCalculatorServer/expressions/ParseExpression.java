package SocketCalculatorServer.expressions;
import SocketCalculatorServer.CalculateException;

public class ParseExpression {

	// 수식의 결과를 리턴하는 함수. 예시: "2+4*4/2" -> 10.0
	// 트리를 이용하여 사칙연산의 순서를 구현함.
    public static double calculateExpression(String expr) throws CalculateException {
		// 수식 문자열이 null이면 오류로 처리
        if (expr == null) {
            throw new CalculateException("SYNTAX_WRONG");
        }
		
		// 수식 문자열을 char 배열로 변환
        char[] arr = expr.toCharArray();

		// 현재 인덱스 위치에서 찾은 숫자와 다음 인덱스의 위치를 저장하는 곳
		// 초기값은 0번째에서 숫자를 찾은 결과.
        double[] foundResult = findNumberWord(expr, 0);

		// 다음 인덱스의 위치를 저장하는 변수. 
		// 초기값은 0번째에서 숫자를 찾은 후 숫자 다음의 문자 인덱스
        int i = (int) foundResult[1];

		// 숫자를 노드로 바꿔 저장하는 곳. 
		// 연산자를 가져온 뒤 가져온 숫자를 저장함.
        Node currentNum;

		// 루트 노드를 저장하는 곳. 초기값은 수식에서 첫번째로 나온 숫자.
        Node root = new Node(foundResult[0], '!');

		// 현재 인덱스가 문자열의 끝을 가리킬때까지 반복
        while (i<arr.length) {
			// 연산자 노드.
            Node operator;

			// 만약 숫자 다음 인덱스에 문자가 연산자 문자중 하나라면,
			// 연산자 노드를 정의.
            if (arr[i] == '+' || arr[i] == '-' || arr[i] == '*' || arr[i] == '/') {
                operator = new Node(-1, arr[i]);
            } else {
				// 숫자 다음에 이상한 문자가 온것. 형식 오류 예외 throw.
                throw new CalculateException("SYNTAX_WRONG");
            }

			// 연산자 다음에 오는 숫자 찾기. 인덱스는 숫자 다음으로 설정.
            foundResult = findNumberWord(expr, ++i);
            currentNum = new Node(foundResult[0], '!');
            i = (int) foundResult[1];

            if (operator.operator == '+' || operator.operator == '-' || root.right == null
                || root.operator == '*' || root.operator == '/') {
				// 연산자가 +/-일 경우에는 순서대로 계산하면 되므로,
				// 단순히 root를 받아온 새로운 연산자 노드로 만들고
				// left를 예전 root, right을 받아온 숫자로 정의하면 됨.
				// 
				// 곱하기와 나누기의 경우에도 인접한 곱하기와 나누기들은 순서대로 계산하므로
				// root가 곱하기와 나누기일때도 똑같은 과정 적용.

                Node previousRoot = root;
                root = operator;
                root.left = previousRoot;
                root.right = currentNum;
            } else {
				// root가 곱하기/나누기가 아니며, 연산자가 곱하기/나누기인 상태임.
				// 가장 최근에 나온 곱하기/나누기 덩어리에 붙어 순서대로 계산되도록 해야하므로,
				// 
				// 자식이 곱하기와 나누기만으로 구성된 가장 위쪽에 있는 노드를 찾거나, .......(1)
				// 또는 가장 오른쪽 아래에 있는 노드를 찾고, ..........................(2)
				// 그 노드를 미리 정의한 연산자 노드로 바꾸고 원래 노드는 자식으로 보내야 함.

				// target은 가장 위쪽에 있는 노드의 부모 노드를 찾고 저장하기 위한 변수.
				// 만약 현재 노드의 오른쪽 노드가 곱하기/나누기 노드라면 경우 (1).
				// 만약 현재 노드의 오른쪽 노드가 숫자 노드(leaf node) 라면 경우 (2).
                Node target = root;
                while (target.right.right != null && 
                       !(target.right.operator == '*' ||
                       target.right.operator == '/')) {
                    target = target.right;
                }

				// 부모 노드의 오른쪽을 미리 정의한 연산자 노드로 바꾸고,
				// 연산자 노드의 오른쪽은 입력받은 숫자로 만든 노드,
				// 왼쪽은 원래 노드로 바꾸기.
                operator.left = target.right;
                operator.right = currentNum;
                target.right = operator;
            }
        }

		// 재귀적으로 모든 노드들을 돌며 계산하고, 결과를 리턴함
        return calculateNodes(root);
    }

	// 모든 노드들을 재귀적으로 돌며 계산하는 함수
    private static double calculateNodes(Node node) throws CalculateException {
        if (node.right != null) {
		// 현재 노드가 연산자 노드일떄: 
		// 오른쪽과 왼쪽 노드의 계산 결과를 연산자에 따라 계산후 히턴
            if (node.operator == '+') {
                return calculateNodes(node.left) + calculateNodes(node.right);
            } else if (node.operator == '-') {
                return calculateNodes(node.left) - calculateNodes(node.right);
            } else if (node.operator == '*') {
                return calculateNodes(node.left) * calculateNodes(node.right);
            } else if (node.operator == '/') {
                double right = calculateNodes(node.right);
                if (right == 0) {
					// 만약 0으로 나누는 경우가 있다면
					// 0으로 나눴다는 계산 예외 throw.
                    throw new CalculateException("DIV_ZERO");
                } else {
                    return calculateNodes(node.left) / right;
                }
            } else {
                throw new CalculateException("SYNTAX_WRONG");
            }
        } else {
		// 현재 노드가 숫자 노드 (leaf node)일때:
		// 현재 숫자를 리턴함.
            return node.number;
        }
    }

	// {숫자, 숫자 다음 인덱스} 배열 리턴하는 함수. 수식 문자열과 탐색시작 인덱스가 input.
    private static double[] findNumberWord(String expr, int startFrom) throws CalculateException {
        if (startFrom >= expr.length()) {
			// 수식 문자열의 마지막 문자가 연산자일때, 연산자 다음 숫자를 찾으려고 하기 때문에
			// 계속 실행하게 두면 인덱스 범위 예외가 발생함. 따라서 미리 수식오류 throw.
            throw new CalculateException("SYNTAX_WRONG");
        }

		// 수식 문자열을 char 배열로 바꾼것
        char[] arr = expr.toCharArray();
		// 현재 찾고있는 숫자 문자열의 시작부분 인덱스
        int currentNumWordStart = -1;
		// 현재 찾고있는 숫자 문자열의 끝부분 인덱스
        int currentNumWordEnd = -1;
		// 현재 인덱스를 저장하는 변수. 초기값은 인수로 주어진 탐색시작 인덱스.
        int i = startFrom;

        if (arr[i] == 45) {
			// 첫번째 문자가 '-'일 경우.
            currentNumWordStart = i;
            currentNumWordEnd = i;
            i++;
        }
        for (; i<arr.length; i++) {
            if ((48 <= arr[i] && arr[i] <= 57) || arr[i] == 46) {
			// 현재 문자가 0~9의 문자 중 하나거나, '.' 일때. 즉 "숫자 문자"일때.
                if (currentNumWordStart == -1) {
					// 아직 "숫자 문자"가 한번도 나오지 않은 상태.
					// 따라서 시작과 끝 지점을 정의함.
                    currentNumWordStart = i;
                    currentNumWordEnd = i;
                } else {
					// 인덱스가 숫자의 중간에 위치한 상태. 끝부분만 업데이트 해준다.
                    currentNumWordEnd = i;
                }
            } else if ((arr[i] == '+' || arr[i] == '-' || arr[i] == '/' || arr[i] == '*')
                && currentNumWordEnd != -1) {
					// 인덱스가 가리키는 문자가 연산자 중 하나이고, 
					// 이전에 숫자가 나온적이 있을때의 경우.
					// 문자열을 숫자로 바꾼 결과와 다음 인덱스의 값을 배열에 담아 리턴
                    return new double[] {
                        Double.parseDouble(expr.substring(
                            currentNumWordStart, currentNumWordEnd+1)),
                        currentNumWordEnd+1};
            } else {
				// 숫자가 나온 뒤 연산자가 나오는 포맷을 따르지 않는 경우임.
				// 형식 예외 throw.
                throw new CalculateException ("SYNTAX_WRONG");
            }
        }

        if (currentNumWordEnd != -1) {
			// 이 경우는 연산자를 찾지 못해 for문에서 함수가 끝나지 않았을때의 경우임.
            return new double[] {
            Double.parseDouble(expr.substring(
                currentNumWordStart, currentNumWordEnd+1)),
            currentNumWordEnd+1};
        }

		// 연산자도 찾지 못했고, 숫자도 찾지 못했다면 형식 오류.
        throw new CalculateException("SYNTAX_WRONG");
    }

}
