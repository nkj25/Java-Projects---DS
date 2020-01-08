
package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) throws IllegalArgumentException {
		
		/* IMPLEMENT THIS METHOD */
		DigitNode EndNode = null;
	    int initial = 0;
	    BigInteger result = new BigInteger() ;

	    char posneg;
	    int length1 = integer.length();
	    posneg = integer.charAt(0);

	    if (posneg == '+'){
	    initial = 1; 
	    result.negative = false; 
	    }
	    
	    if (posneg == '-'){
	    	initial = 0; 
	    	result.negative = true; 
	    }
	    int x;
	    int i;
	    for (i = initial; i <= length1-1; i++){
	    	x = integer.charAt(i) - '0' ; 
	    	if (x > 0) break; 
	    }
	          
	    initial = i;
	    
	    for (i = length1 - 1; i >= initial; i--){ 	 
	    	if(!Character.isDigit (integer.charAt(i) ) ) {
	    		throw new IllegalArgumentException("Illegal Format");
	        		// break;
	    }
	    
	    DigitNode Node2 = new DigitNode((integer.charAt(i) - '0'), null) ;
	    	if (result.front==null) {
	    		result.front  = Node2;
	        }else {
	        	EndNode.next = Node2; 
	        }
	     
	    	EndNode = Node2;
	        //result.numDigits++;
	    }
	    
	    return result;

	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		
		BigInteger resultAdd = new BigInteger();
		
        DigitNode thisPtr = first.front;
        DigitNode otherPtr = second.front;
        
        int FirstValue;
		int SecondValue;
		String StringResult = "";
        int length1;
        length1 = first.numDigits; 
    	String firstNum = "";
    	String secondNum = "";
        int firstInt = 0;
        int secondInt = 0;

        int carry = 0;
        
        if(first == null || second.front == null) return first;
        else if(first.front == null) return second;
        
        
        // both positive or both negative numbers
        if((first.negative == true && second.negative == true) || (first.negative == false && second.negative == false)){
        	 
        	// resultAdd.negative = first.negative;
            resultAdd.front = new DigitNode(0, null);
        	
        		while(thisPtr != null || otherPtr != null){
                 	FirstValue = 0; 
                 	SecondValue = 0;
                 	if(thisPtr != null){ 
                 		FirstValue = thisPtr.digit;
                 		thisPtr = thisPtr.next;
                 	}
                 	
                 	else FirstValue=0;
                 	
                 	if(otherPtr != null) { 
                 		SecondValue = otherPtr.digit;
                 		otherPtr = otherPtr.next; 
                 	}
                 	else SecondValue=0;
                 	
                 	length1 = (FirstValue + SecondValue + carry);
                 	StringResult  = String.valueOf(length1%10) + StringResult;
                 	
                 	carry = (length1 / 10);
                 	
                 	if(length1 >= 10){
                 		length1 = length1 % 10;
                 	}
        		}
        	carry = (length1 / 10);
             	
            if(length1 >= 10) length1 = length1 % 10;
            

        } 
        
        //adding one positive and one negative
        else if (((first.negative == true) && (second.negative == false)) || ((second.negative == true) && (first.negative == false))){
        	
    	    //determining 2 numbers to compare magnitude and length
        	while (thisPtr != null) {
    	    	firstNum = thisPtr.digit + firstNum;
    	    	thisPtr = thisPtr.next;
    	    }
    	    
        	while (otherPtr != null) {
    	    	secondNum = otherPtr.digit + secondNum;
    	    	otherPtr = otherPtr.next;
    	    }
        	
        	firstInt = Integer.parseInt(firstNum);
        	secondInt = Integer.parseInt(secondNum);
        	
        	// reset pointers
        	thisPtr = first.front;
        	otherPtr = second.front;
        	
        	int borrow = 0;
        	
        	
        	while(thisPtr != null || otherPtr != null){
             	
        		FirstValue = 0; 
             	SecondValue = 0;
             	
             	if (firstInt > secondInt) {
             		
             		if(thisPtr != null){ 
             			FirstValue = thisPtr.digit;
             			thisPtr = thisPtr.next;
             		}
             		else FirstValue=0;
             	
             		if(otherPtr != null) { 
             			SecondValue = otherPtr.digit;
             			otherPtr = otherPtr.next; 
             		}
             		else SecondValue=0;
             		
             		if (FirstValue < 1 && borrow > 0) FirstValue = 9;
             		else FirstValue = FirstValue - borrow;
             	
             		if (FirstValue >= SecondValue) {
             			if (FirstValue > 8 && borrow > 0) borrow = 1;
             			else borrow = 0;
             			length1 = (FirstValue - SecondValue);
             			StringResult  = String.valueOf(length1) + StringResult;
             		}
             		else if (FirstValue < SecondValue) {
             			length1 = ((FirstValue + 10) - SecondValue);
             			borrow = 1;
             			StringResult  = String.valueOf(length1) + StringResult;
             		}
	
             	}
             	
             	else if (secondInt > firstInt) {
             		
             		if(thisPtr != null){ 
             			FirstValue = thisPtr.digit;
             			thisPtr = thisPtr.next;
             		}
             		else FirstValue=0;
             	
             		if(otherPtr != null) { 
             			SecondValue = otherPtr.digit;
             			otherPtr = otherPtr.next; 
             		}
             		else SecondValue=0;
             		
             		if (SecondValue < 1 && borrow > 0) SecondValue = 9;
             		else SecondValue = SecondValue - borrow;
             	
             		if (SecondValue >= FirstValue) {
             			if (SecondValue > 8 && borrow > 0) borrow = 1;
             			else borrow = 0;
             			length1 = (SecondValue - FirstValue);
             			StringResult  = String.valueOf(length1) + StringResult;
             		}
             		else if (SecondValue < FirstValue) {
             			length1 = ((SecondValue + 10) - FirstValue);
             			borrow = 1;
             			StringResult  = String.valueOf(length1) + StringResult;
             		}
             		
             	}
           }
    
        }
                                                                   	           
        if (carry > 0 ) StringResult = carry + StringResult;
        
        BigInteger resultDone = BigInteger.parse(StringResult); //initialize result BigInteger class
        
        if (first.negative && second.negative) {
        	resultDone.negative = true;
        } else if (!first.negative && !second.negative) {
        	resultDone.negative = false;
		} else if ((secondInt > firstInt) && (second.negative)) {
     		resultDone.negative = true;
     	} else if ((firstInt > secondInt) && (first.negative)) {
     		resultDone.negative = true;
     	} else {
     		resultDone.negative = false;
     	}
        
        return resultDone;
        }
        	
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		
		int FirstValue; 
		int SecondValue; 
		String StringResult= "";
		int extra = 0;
		int turns = 0;
		int length1;
		
		DigitNode thisPtr = first.front;
		DigitNode otherPtr = second.front;
		
		BigInteger thisBigInteger = new BigInteger();
		

		while(otherPtr != null){
         	
			while (thisPtr != null) {
         		
				FirstValue = 0; 
         		SecondValue = 0;
         	
         		FirstValue = thisPtr.digit;
         	
         		if (otherPtr != null) SecondValue = otherPtr.digit;
         		else SecondValue = 0;
         		
         		length1 = ((FirstValue * SecondValue) + extra);
         		StringResult  = String.valueOf(length1%10) + StringResult;
         		extra = (length1 / 10);
         		
         		thisPtr = thisPtr.next;
         				
			}
			
			if (extra > 0 ) StringResult = extra + StringResult;
			extra = 0;
			
			BigInteger prevBigInteger = BigInteger.parse(StringResult);
     		StringResult = "";
	        
         	turns++;
			otherPtr = otherPtr.next;
			thisPtr = first.front;
			
			for (int i = 1; i <= turns; i++) {
				StringResult = "0" + StringResult;
			}
			
			thisBigInteger = BigInteger.add(thisBigInteger, prevBigInteger);
		}
		
		
		
		if (first.negative && second.negative) {
			thisBigInteger.negative = false;
        } else if (!first.negative && !second.negative) {
        	thisBigInteger.negative = false;
        } else if ((!first.negative && second.negative) || (first.negative && !second.negative)) {
        	thisBigInteger.negative = true;
        }
		
		return thisBigInteger;	
	}	
			
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
