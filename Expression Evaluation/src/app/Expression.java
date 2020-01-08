package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	delims = "0123456789\t*+-/([])";
    	boolean check = true;
    	
    	expr = expr.replace(" ", "");
    	
    	StringTokenizer firstToken;
    	StringTokenizer secondToken;
    	
    	firstToken = new StringTokenizer (expr, delims, true);
    	secondToken = new StringTokenizer (expr, delims, true);
    	
    	secondToken.nextToken();
    	
    	while (secondToken.hasMoreTokens()) {
    		
    		String secondPoint = secondToken.nextToken();
    		String firstPoint = firstToken.nextToken();
    		
    		if (delims.contains(firstPoint)) {
    			continue;
    		}
    		else if (secondPoint.equals("[")) {
    			int i = 0;
    			while (i<arrays.size()) {
    				if (firstPoint.contentEquals(arrays.get(i).name)) {
    					check = false;
    					
    				}
    				i++;
    			}
    			
    			if (check == true) {
    				arrays.add(new Array(firstPoint));
    			}
    			
    			check = true;
    			continue;
    			
    		}
    		else {
    			int i = 0;
    			while (i < vars.size()) {
    				if (firstPoint.contentEquals(vars.get(i).name)) {
    					check = false;
    				}
    				i++;
    			}
    			
    			if (check == true) {
    				vars.add(new Variable(firstPoint));
    			}
    			
    			check = true;
    			continue;
    				
    			}
    		}
    	String firstPoint = firstToken.nextToken();
    	if (!delims.contains(firstPoint)) {
    		vars.add(new Variable(firstPoint));
    	}
    			
    	
    }

    
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	
    	if (expr.contains("[")) {
            return evaluate(ArrayReplacer(expr, vars, arrays), vars, arrays);
        } else {
            return solveInnerExpr(expr, vars);
        }
    }

    private static float solveInnerExpr(String expr2, ArrayList<Variable> vars2) {
        
        Stack<Character> OperStack = new Stack<>();

        Stack<Float> theoper = new Stack<>();
        
        String digits = "()/*+-";
        
        expr2 = expr2.replaceAll("[ \\t]", "");

        int posEnd = -1;
        int length2 = expr2.length();
        int i = 0;
        
        while (i < length2 + 1) {

            if (i == length2) {
                if (expr2.charAt(i - 1) != ')') 
                {
                    String expName = expr2.substring(posEnd + 1);
                    int posOfVar = vars2.indexOf(new Variable(expName));
                    if (posOfVar >= 0) {
                        Variable temp = vars2.get(posOfVar);
                        theoper.push((float) temp.value);
                        
                    } else {
                        theoper.push(Float.parseFloat(expName));
                    }
                }
                break;
           
            }

            char charViewed = expr2.charAt(i);
            if (digits.contains(String.valueOf(charViewed))) {

                String expName = expr2.substring(posEnd + 1, i);
                if (!expName.equals("")) {
                    int posOfVar = vars2.indexOf(new Variable(expName));
                    if (posOfVar >= 0) {
                        Variable temp = vars2.get(posOfVar);
                        theoper.push((float) temp.value);
                    } else {
                        theoper.push(Float.parseFloat(expName));
                    }
                }

                if (charViewed == ')') {
                    while (OperStack.peek() != '(') {
                    	StacksProcessor(theoper, OperStack);
                    }
                    OperStack.pop();
                } else if (charViewed == '(') {
                    OperStack.push(charViewed);
                } 
                else {
                    while (!OperStack.isEmpty() &&
                    		SignPrecedence(OperStack.peek()) >= SignPrecedence(charViewed)) {
                    	StacksProcessor(theoper, OperStack);
                    }
                    OperStack.push(charViewed);
                }

                posEnd = i;
            }
            i++;
        }

        while (!OperStack.isEmpty()) {
        	StacksProcessor(theoper, OperStack);
        }
        return theoper.peek();
    }

    private static void StacksProcessor(Stack<Float> oper2, Stack<Character> operStack2) {
    	char x = operStack2.pop();
    	float operRight = oper2.pop();
        float operLeft = oper2.pop();

        float result = SolveResult(operLeft, x , operRight);
        oper2.push(result);
    }

    private static int SignPrecedence(char x) {
        switch (x) {
        	case '*':
        	case '/':
        		return 3;
            case '-':
            case '+':
                return 1;
            
        }
        return 0;
    }

    private static float SolveResult(float operLeft, char charOper, float operRight) {
        switch (charOper) {
        	case '*':
        		return operLeft * operRight;    
            case '/':
                return operLeft / operRight;
            case '-':
                return operLeft - operRight;
            case '+':
                return operLeft + operRight;
            
        }
        return 0;
    }

    private static String ArrayReplacer(String expr3, ArrayList<Variable> vars3, ArrayList<Array> arrays3) {
        expr3 = expr3.replaceAll("[ \\t]", "");
        int length = expr3.length();

        int indexLast = -1;
        for (int i = 0; i < length; i++) {
            char charViewed = expr3.charAt(i);
            if (charViewed == '[') {
                String expName = expr3.substring(indexLast + 1, i);
                int indexArr = arrays3.indexOf(new Array(expName));

                int indexClose = 0, count = 1;
                int k = i + 1;
                while (count > 0) {
                    char trial = expr3.charAt(k);
                    if (trial == '[') count++;
                    else if (trial == ']') count--;
                    if (count == 0) {
                    	indexClose = k;
                        break;
                    }
                    k++;
                }

                String innerExpr = expr3.substring(i + 1, indexClose);
                int indexInt = (int) evaluate(innerExpr, vars3, arrays3);
                String result = String.valueOf(arrays3.get(indexArr).values[indexInt]);
                return expr3.replace(expName + "[" + innerExpr + "]", result);
            }
            if (delims.contains(String.valueOf(charViewed))) {
                indexLast = i;
            }
        }
        return expr3;
 
    }
}
