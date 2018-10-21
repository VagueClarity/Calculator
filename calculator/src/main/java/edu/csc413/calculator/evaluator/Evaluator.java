package edu.csc413.calculator.evaluator;



import edu.csc413.calculator.operators.*;
import jdk.internal.org.objectweb.asm.Opcodes;
import sun.invoke.empty.Empty;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
  private Stack<Operand> operandStack;
  private Stack<Operator> operatorStack;
  private StringTokenizer tokenizer;
  private static final String DELIMITERS = "+-*^/() ";

  public Evaluator() {
    operandStack = new Stack<>();
    operatorStack = new Stack<>();
  }

  public int eval( String expression ) {
      String token;
      int OpCounter = 0;
      // The 3rd argument is true to indicate that the delimiters should be used
      // as tokens, too. But, we'll need to remember to filter out spaces.
      this.tokenizer = new StringTokenizer(expression, DELIMITERS, true);

      // initialize operator stack - necessary with operator priority schema
      // the priority of any operator in the operator stack other than
      // the usual mathematical operators - "+-*/" - should be less than the priority
      // of the usual operators


      while (this.tokenizer.hasMoreTokens()) {
          // filter out spaces
          token = this.tokenizer.nextToken();

          if (!token.equals(" ")) {
              // check if token is an operand
              if ( Operand.check(token) ) {
                  operandStack.push(new Operand(token));

                  // reduces operator counter if it's an operand
//                  if(OpCounter>0) {
//                      OpCounter--;
//                  }
              } else {

                  if(!token.equals(")")) {
                      if (!Operator.check(token)) {
                          System.out.println("*****invalid operator******");
                          throw new RuntimeException("invalid operator");
                      }

                      // TODO Operator is abstract - these two lines will need to be fixed:
                      // The Operator class should contain an instance of a HashMap,
                      // and values will be instances of the Operators.  See Operator class
                      // skeleton for an example.

                      Operator newOperator = Operator.getOperator(token);

                      // Checking repeated operator case such as 4 + + + 4
                      // Adds one to counter for every operator it sees and reduce it by one for every operand it sees
//                      OpCounter++;
//                      if (OpCounter >= 2) {
//                          System.out.println("****Repeated Operators****");
//                          throw new RuntimeException("Expression is Invalid.");
//                      } else {

                          // If operator is not a left parentheses
                          if (newOperator.priority() != 0) {


                              while (!operatorStack.empty() && (operatorStack.peek().priority() >= newOperator.priority())) {


                                  // note that when we eval the expression 1 - 2 we will
                                  // push the 1 then the 2 and then do the subtraction operation
                                  // This means that the first number to be popped is the
                                  // second operand, not the first operand - see the following code
                                  Operator oldOpr = operatorStack.pop();
                                  Operand op2 = operandStack.pop();
                                  Operand op1 = operandStack.pop();
                                  operandStack.push(oldOpr.execute(op1, op2));


                              }
                          }

                          operatorStack.push(newOperator);

//                      }
                  }
                  else{
                      // Evaluate the expression inside the parentheses
                      while(operatorStack.peek().priority() != 0){
                          Operator oldOpr = operatorStack.pop();
                          Operand op2 = operandStack.pop();
                          Operand op1 = operandStack.pop();
                          operandStack.push(oldOpr.execute(op1, op2));
                      }
                      // Pop left parentheses
                      operatorStack.pop();
                  }
              }
          }
      }


      // Control gets here when we've picked up all of the tokens; you must add
      // code to complete the evaluation - consider how the code given here
      // will evaluate the expression 1+2*3
      // When we have no more tokens to scan, the operand stack will contain 1 2
      // and the operator stack will have + * with 2 and * on the top;
      // In order to complete the evaluation we must empty the stacks (except
      // the init operator on the operator stack); that is, we should keep
      // evaluating the operator stack until it only contains the init operator;
      // Suggestion: create a method that takes an operator as argument and
      // then executes the while loop.


      // To finish up the remaining operations in both stacks
      evalStack();

      return operandStack.pop().getValue();
  }

  private void evalStack(){
      while(!operatorStack.empty()) {

          Operator oldOpr = operatorStack.pop();
          Operand op2 = operandStack.pop();
          Operand op1 = operandStack.pop();
          operandStack.push(oldOpr.execute(op1, op2));
      }


  }


}
