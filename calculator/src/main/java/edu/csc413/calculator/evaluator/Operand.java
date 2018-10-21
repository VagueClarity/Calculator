package edu.csc413.calculator.evaluator;
/**
 * Operand class used to represent an operand
 * in a valid mathmetical expression.
 */
public class Operand {

    private String token;
  /**
  * construct operand from string token.
  */  
  public Operand( String token ) {
    this.token = token;
  }
  /**
   * construct operand from integer
   */
  public Operand( int value ) {
    token = ""+ value;
  }
  /**
   * return value of operand
   */
  public int getValue() {

      check(token);

      return Integer.parseInt(token);
  }
  /**
   * Check to see if given token is a valid
   * operand.
   */
  public static boolean check( String token ) {

    try {
      Integer.parseInt(token);
    }
    catch(NumberFormatException e){

      return false;
    }
    return true;
  }
}
