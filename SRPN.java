import java.util.Stack;
import java.util.Random;
/**
 * Program class for an SRPN calculator.
 */

public class SRPN {
    static final int STACK_SIZE = 23;

    private Stack numbers;
    private boolean isCommenting;

    public SRPN(){
      numbers = new Stack();
      isCommenting = false;
    }

    /**
      * processCommand is the entry method for an SRPN to handle the calculation
      * this will go through each characters in the string to
      *    + detect either positive or negative integers. Then these integers will be stored
             to the stack.
      *    + detect operators. Whenever it see an operator, it will be evaluated immediately
      *    + detect `#` commenting sign. When see this sign, the rest of the instruction
      *      will be ignored until it see another `#` sign.
    */
    public void processCommand(String s){
      int lastIntegerIndex = 0;
      char c;
      String number;
      for (int i = 0; i < s.length(); i++){
        c = s.charAt(i);
        if(isCommenting) {
          if (c == '#') { isCommenting = false; }
        } else {
          if (isOperator(c) && !isMinusSign(c, i, s)) {
            if(isLastCharIsInteger(s, i)) { storingNumeric(s.substring(lastIntegerIndex, i)); }
            lastIntegerIndex = i + 1;
            processCalculation(c);
          } else if (isInteger(c) && i == s.length() - 1) {
            storingNumeric(s.substring(lastIntegerIndex, i+1));
          } else if(c == ' ') {
            if(isLastCharIsInteger(s, i)) { storingNumeric(s.substring(lastIntegerIndex, i)); }
            lastIntegerIndex = i+1;
          } else if (isInteger(c) || c == '-'){
          } else if (c == '#') {
            isCommenting = true;
          } else {
            System.out.println("Unrecognised operator or operand \"" + c + "\".");
          }
        }
      }
    }

    private void storingNumeric(String s) {
      try {
        if (numbers.size() >= SRPN.STACK_SIZE){
          System.out.println("Stack overflow.");
        } else {
          long i = Long.parseLong(s);
          numbers.push(i);
        }
      } catch (NumberFormatException e1) {}
      return;
    }

    private boolean isOperator(char c){
      return isCaculatingOperator(c) || c == 'r' || c == 'd' || c == '=';
    }

    private boolean isCaculatingOperator(char c) {
      return c == '-' || c == '+' || c == '*' || c == '/' || c == '%' || c == '^';
    }

    private boolean isInteger(char c){
      return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }

    private boolean isMinusSign(char c, int idx, String s) {
      return c == '-' && idx + 1 < s.length() && isInteger(s.charAt(idx+1));
    }

    private boolean isLastCharIsInteger(String s, int idx) {
      return idx >= 1 && isInteger(s.charAt(idx-1));
    }

    private long handleSaturation(long n){
      if(n > Integer.MAX_VALUE) { return (long) Integer.MAX_VALUE; }
      else if(n < Integer.MIN_VALUE) { return (long) Integer.MIN_VALUE; }
      else { return n; }
    }

    private void processCalculation(char op){
      if(isCaculatingOperator(op) && numbers.size() <= 1) {
        System.out.println("Stack underflow.");
        return;
      }

      if(isOperator(op)) {
        if(isCaculatingOperator(op)) {
          Long b = (long) numbers.pop();
          Long a = (long) numbers.pop();

          switch(op) {
          case '+':
            numbers.push(handleSaturation(a + b));
            break;
          case '-':
            numbers.push(handleSaturation(a - b));
            break;
          case '*':
            numbers.push(handleSaturation(a * b));
            break;
          case '/':
            numbers.push(handleSaturation(a / b));
            break;
          case '%':
            numbers.push(handleSaturation(a % b));
            break;
          case '^':
            numbers.push(handleSaturation((long) Math.pow(a, b)));
            break;
          }
        } else {
          switch(op) {
          case '=':
            Long n = (long) numbers.peek();
            System.out.println(n);
            break;
          case 'd':
            for (Object item : numbers){
              Long x = (long) item;
              System.out.println(x);
            }
            break;
          case 'r':
            Random ran = new Random();
            long x = ran.nextInt(999999999);
            storingNumeric(String.valueOf(handleSaturation(x)));
            break;
          }
        }
      }
      return;
    }
}
