import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Stack;

/**
 * A class representing an abstract arithmetic expression
 */
public abstract class Expression
{
   public static String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
   public static Expression exp;
   public static Stack <Expression> operands = new Stack<Expression>();
   public static Stack <String> operators = new Stack<String>();


   /**
    * Creates a tree from an expression in postfix notation
    * @param postfix an array of Strings representing a postfix arithmetic expression
    * @return a new Expression that represents postfix
    */
   public static Expression expressionFromPostfix(String[] postfix)
   {
      //Creates stack to hold in all items in the array
      
      //Based on the length of the array it will do something to each item in the array
      for(int i = 0; i < postfix.length; i++){
         //If it is not an operator it will send it through the integer or variable expression methods
         if((!postfix[i].equals("/") && !postfix[i].equals("+") && !postfix[i].equals("-") && !postfix[i].equals("*"))){
            if(alpha.contains(postfix[i])){
               VariableOperand var = new VariableOperand(postfix[i]);
               operands.push(var);
            }
            else{
               IntegerOperand num = new IntegerOperand(Integer.parseInt(postfix[i]));
               operands.push(num);
            }
         }
         else{
            //Obtains the leaves to the the operator
            Expression right = operands.pop();
            Expression left = operands.pop();
            String symbol = postfix[i];
            operands.push(helper(symbol, left, right)); 
         }
      }
      return operands.pop();
   }

   /**
    * Creates a tree from an expression in infix notation
    * @param infix an array of Strings representing a infix arithmetic expression
    * @return a new Expression that represents infix
    */
   public static Expression expressionFromInfix(String[] infix)
   {  
      //Allows us to compare precedence when needed
      HashMap <String, Integer> precedence = new HashMap <String, Integer>();
      precedence.put("+", 1);
      precedence.put("-", 1);
      precedence.put("/", 2);
      precedence.put("*", 2);
      precedence.put("(", 0);
      precedence.put(")", 0);

      for(int i = 0; i < infix.length; i++){
         //If it is not an operator it will send it through the integer or variable expression methods
         if((!infix[i].equals("/") && !infix[i].equals("+") && !infix[i].equals("-") && !infix[i].equals("*") && !infix[i].equals(")") && !infix[i].equals("("))){
            if(alpha.contains(infix[i])){
               VariableOperand var = new VariableOperand(infix[i]);
               operands.push(var);
            }
            else{
               IntegerOperand num = new IntegerOperand(Integer.parseInt(infix[i]));
               operands.push(num);
            }
         }
         //will pop and create new expressions until it hits a (
         else if(infix[i].equals(")")){
            while(!operators.peek().equals("(") && !operands.isEmpty()){
               Expression right = operands.pop();
               Expression left = operands.pop();
               String symbol = operators.pop(); 
               operands.push(helper(symbol, left, right));
            }
            operators.pop();
         }
         else{
            if(operators.isEmpty()){
               operators.push(infix[i]);
            }
            else if(!operators.isEmpty()){
               //If the precedence of the incoming operator is lower it will be added to the stack
               if(precedence.get(infix[i]) >= precedence.get(operators.peek()) || infix[i].equals("(")){
                  operators.push(infix[i]);
               }
               //other wise pop from the stack and create the new one and then add the incoming one
               else if(precedence.get(infix[i]) < precedence.get(operators.peek())){
                  Expression right = operands.pop();
                  Expression left = operands.pop();
                  String symbol = operators.pop();
                  operands.push(helper(symbol, left, right));
                  operators.push(infix[i]);                    
               }                  
            }
         }
      }
      //Pop until the stack is empty to fully complete the expression
      while(!operators.isEmpty()){
         Expression right = operands.pop();
         Expression left = operands.pop();
         String symbol = operators.pop();
         operands.push(helper(symbol, left, right));
      }
      return operands.pop();
   }
   //This method allows us to provide the expression with its adequate operator and cut down redudency              
   public static Expression helper(String symbol, Expression left, Expression right){
      if(symbol.equals("+")){
         exp = new SumExpression(left, right);
      }
      if(symbol.equals("-")){
         exp = new DifferenceExpression(left, right);
         }
      if(symbol.equals("*")){
         exp = new ProductExpression(left, right);
      }
      if(symbol.equals("/")){
        exp = new QuotientExpression(left, right);
      }
      return exp;
   }

   /**
    * @return a String that represents this expression in prefix notation.
    */
   public abstract String toPrefix();

   /**
    * @return a String that represents this expression in infix notation.
    */  
   public abstract String toInfix();

   /**
    * @return a String that represents this expression in postfix notation.
    */  
   public abstract String toPostfix();

   /**
    * @return a String that represents the expression in infix notation
    */
   @Override
   public String toString()
   {
      return toInfix();
   }
   
   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
   public abstract Expression simplify();

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
   public abstract int evaluate(HashMap<String, Integer> assignments);

   /**
    * @return a Set of the variables contained in this expression
    */
   public abstract Set<String> getVariables();

   @Override
   public abstract boolean equals(Object obj);

   /**
    * Prints the expression as a tree in DOT format for visualization
    * @param filename the name of the output file
    */
   public void drawExpression(String filename) throws IOException
   {
      BufferedWriter bw = null;
      FileWriter fw = new FileWriter(filename);
      bw = new BufferedWriter(fw);
      
      bw.write("graph Expression {\n");
      
      drawExprHelper(bw);
      
      bw.write("}\n");
      
      bw.close();
      fw.close();     
   }

   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
   protected abstract void drawExprHelper(BufferedWriter bw) throws IOException;
}

/**
 * A class representing an abstract operand
 */
abstract class Operand extends Expression
{
}

/**
 * A class representing an expression containing only a single integer value
 */
class IntegerOperand extends Operand
{
   protected int operand;

   /**
    * Create the expression
    * @param operand the integer value this expression represents
    */
   public IntegerOperand(int operand)
   {
      this.operand = operand;
   }

   /**
    * @return a String that represents this expression in prefix notation.
    */   
   public String toPrefix()
   {
      //adds the operand
      return " " + operand;
   }

   /**
    * @return a String that represents this expression in postfix notation.
    */  
   public String toPostfix()
   {
      //adds the operand
      return  " " + operand;
   }   

   /**
    * @return a String that represents the expression in infix notation
    */
   public String toInfix()
   {
      //adds the operand
      return  " " + operand;      
   }

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
   public Expression simplify()
   {
      return new IntegerOperand(operand);
   }   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
   public int evaluate(HashMap<String, Integer> assignments)
   {
      return operand;
   }

   /**
    * @return a Set of the variables contained in this expression
    */
   public Set<String> getVariables()
   {
      return new TreeSet<String>();
   }

   /**
    * @param obj and Object to compare to
    * @return true if obj is an IntegerOperand with the same associated value
    */
   @Override
   public boolean equals(Object obj)
   {
      if((obj instanceof IntegerOperand)){
         IntegerOperand other = (IntegerOperand) obj;
         if(this.operand == other.operand){
            return true;
         }
      }
      return false;
   }
  
   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
   protected void drawExprHelper(BufferedWriter bw) throws IOException
   {
      bw.write("\tnode"+hashCode()+"[label="+operand+"];\n");
   }
}

/**
 * A class representing an expression containing only a single variable
 */
class VariableOperand extends Operand
{
   protected String variable;

   /**
    * Create the expression
    * @param variable the variable name contained with this expression
    */
   public VariableOperand(String variable)
   {
      this.variable = variable;
   }

   /**
    * @return a String that represents this expression in prefix notation.
    */   
   public String toPrefix()
   {
      //adds var
      return variable;
   }

   /**
    * @return a String that represents this expression in postfix notation.
    */  
   public String toPostfix()
   {
      //adds var
      return variable;
   }   

   /**
    * @return a String that represents the expression in infix notation
    */
   public String toInfix()
   {
      //adds var
      return variable;      
   }

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
   public Expression simplify()
   {
      return new VariableOperand(variable);
   }   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
   public int evaluate(HashMap<String, Integer> assignments)
   {
      //Obtains var
      return assignments.get(variable);
   }

   /**
    * @return a Set of the variables contained in this expression
    */
   public Set<String> getVariables()
   {
      //adds var to tree 
      TreeSet<String> tree = new TreeSet<String>();
      tree.add(variable);
      return tree;
   }

   /**
    * @param obj and Object to compare to
    * @return true if obj is an IntegerOperand with the same associated value
    */
   @Override
   public boolean equals(Object obj)
   {
      //Checks if 2 expressions are the same 
      if((obj instanceof VariableOperand)){
         VariableOperand other = (VariableOperand) obj;
         if(this.variable.equals(other.variable)){
            return true;
         }
      }
      return false;
   }   

   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
   protected void drawExprHelper(BufferedWriter bw) throws IOException
   {
      bw.write("\tnode"+hashCode()+"[label="+variable+"];\n");
   }   
}

/**
 * A class representing an expression involving an operator
 */
abstract class OperatorExpression extends Expression
{
   protected Expression left;
   protected Expression right;

   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
   public OperatorExpression(Expression left, Expression right)
   {
      this.left = left;
      this.right = right;
   }

   /**
    * @return a string representing the operator
    */
   protected abstract String getOperator();     
   
   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
   protected void drawExprHelper(BufferedWriter bw) throws IOException
   {
      String rootID = "\tnode"+hashCode();
      bw.write(rootID+"[label=\""+getOperator()+"\"];\n");

      bw.write(rootID + " -- node" + left.hashCode() + ";\n");
      bw.write(rootID + " -- node" + right.hashCode() + ";\n");
      left.drawExprHelper(bw);
      right.drawExprHelper(bw);
   }   
}

/**
 * A class representing an expression involving an sum
 */
class SumExpression extends OperatorExpression
{
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
   public SumExpression(Expression left, Expression right)
   {
      super(left, right);
   }

   /**
    * @return a string representing the operand
    */
   protected String getOperator()
   {
      return "+";
   }
   
   /**
    * @return a String that represents this expression in prefix notation.
    */   
   public String toPrefix()
   {
      //Obtains the operater and leaves to create the equation
      return this.getOperator() + left.toPrefix() + right.toPrefix() ;
   }

   /**
    * @return a String that represents this expression in postfix notation.
    */  
   public String toPostfix()
   {
      //Obtains the operater and leaves to create the equation
      return left.toPostfix() + right.toPostfix() + this.getOperator();
   }   

   /**
    * @return a String that represents the expression in infix notation
    */
   public String toInfix()
   {
      //Obtains the operater and leaves to create the equation
      String equation = left.toInfix();
      equation = equation + this.getOperator();
      equation = "(" + equation + right.toInfix() + ")";
      return equation;      
   }

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
   public Expression simplify()
   {
      //Simplifies cases in where the left or the right leaf is a 0 or both are ints
      if(left.simplify().equals(new IntegerOperand(0))){
            return right;
         }
      else if(right.simplify().equals(new IntegerOperand(0))){
         return left;
      }
      else if(this.left.simplify() instanceof IntegerOperand && this.right.simplify() instanceof IntegerOperand ){
         return new IntegerOperand(((IntegerOperand) this.left.simplify()).operand + ((IntegerOperand) this.right.simplify()).operand);
      }
      return new SumExpression(left.simplify(), right.simplify());
   }   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
   public int evaluate(HashMap<String, Integer> assignments)
   {
      //simply evaluates the left and right leaf
      return left.evaluate(assignments) + right.evaluate(assignments);
   }

   /**
    * @return a Set of the variables contained in this expression
    */
   public Set<String> getVariables()
   {
      //Creates the tree, gets all left and right leaves/expressions
      TreeSet<String> tree = new TreeSet<String>();
      Set<String> leftVar = left.getVariables();
      Set<String> rightVar = right.getVariables();
      tree.addAll(leftVar);
      tree.addAll(rightVar);
      return tree;
   }

   /**
    * @param obj and Object to compare to
    * @return true if obj is an IntegerOperand with the same associated value
    */
   @Override
   public boolean equals(Object obj)
   {
      //compares 2 expressions to see if tehy are the same
      if(obj == null)
         return false;

      if(!(obj instanceof SumExpression)){
         return false;
      }
      SumExpression other = (SumExpression) obj;
      if((other.left.equals(this.left) && other.right.equals(this.right)) || (other.left.equals(this.right) && other.right.equals(this.left))){
         return true;
      }
      return false;
   }
      
}

/**
 * A class representing an expression involving an differnce
 */
class DifferenceExpression extends OperatorExpression
{
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
   public DifferenceExpression(Expression left, Expression right)
   {
      super(left, right);
   }

   /**
    * @return a string representing the operand
    */
   protected String getOperator()
   {
      return "-";
   }

   /**
    * @return a String that represents this expression in prefix notation.
    */   
   public String toPrefix()
   {
      //Obtains the operater and leaves to create the equation
      return this.getOperator() + left.toPrefix() + right.toPrefix() ;
   }

   /**
    * @return a String that represents this expression in postfix notation.
    */  
   public String toPostfix()
   {
      //Obtains the operater and leaves to create the equation
      return left.toPostfix() + right.toPostfix() + this.getOperator();
   }   

   /**
    * @return a String that represents the expression in infix notation
    */
   public String toInfix()
   {
      //Obtains the operater and leaves to create the equation
      String equation = left.toInfix();
      equation = equation + this.getOperator();
      equation = "(" + equation + right.toInfix() + ")";
      return equation;   
   }

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
   public Expression simplify()
   {
      //Simplifies cases in where a leaf is 0 or if both leaves are the same number or if they are both numbers
      if(right.simplify().equals(new IntegerOperand(0))){
         return left.simplify();
      }
      else if(left.simplify().equals(right.simplify())){
         return new IntegerOperand(0);
      }
      else if(this.left.simplify() instanceof IntegerOperand && this.right.simplify() instanceof IntegerOperand ){
         return new IntegerOperand(((IntegerOperand) this.left.simplify()).operand - ((IntegerOperand) this.right.simplify()).operand);
      }
      return new DifferenceExpression(left.simplify(), right.simplify());
   }   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
   public int evaluate(HashMap<String, Integer> assignments)
   {
      //evaluates the expression
      return left.evaluate(assignments) - right.evaluate(assignments);
   }

   /**
    * @return a Set of the variables contained in this expression
    */
   public Set<String> getVariables()
   {
      //Obtains all left and right leaves based 
      TreeSet<String> tree = new TreeSet<String>();
      Set<String> leftVar = left.getVariables();
      Set<String> rightVar = right.getVariables();
      tree.addAll(leftVar);
      tree.addAll(rightVar);
      return tree;
   }

   /**
    * @param obj and Object to compare to
    * @return true if obj is an IntegerOperand with the same associated value
    */
   @Override
   public boolean equals(Object obj)
   {
      //Checks if 2 expressions are the same
      if(obj == null){
         return false;
      }
      if(!(obj instanceof DifferenceExpression)){
         return false;
      }
      DifferenceExpression other = (DifferenceExpression) obj;
      if((other.left.equals(this.left) && other.right.equals(this.right)) || (other.left.equals(this.right) && other.right.equals(this.left))){
         return true;
      }
      return false;
   }
   }      


/**
 * A class representing an expression involving a product
 */
class ProductExpression extends OperatorExpression
{
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
   public ProductExpression(Expression left, Expression right)
   {
      super(left, right);
   }

   /**
    * @return a string representing the operand
    */
   protected String getOperator()
   {
      return "*";
   }

   /**
    * @return a String that represents this expression in prefix notation.
    */   
   public String toPrefix()
   {
      //Obtains the operater and leaves to create the equation
      return this.getOperator() + left.toPrefix() + right.toPrefix() ;
   }

   /**
    * @return a String that represents this expression in postfix notation.
    */  
   public String toPostfix()
   {
      //Obtains the operater and leaves to create the equation
      return left.toPostfix() + right.toPostfix() + this.getOperator();
   }   

   /**
    * @return a String that represents the expression in infix notation
    */
   public String toInfix()
   {
      //Obtains the operater and leaves to create the equation
      String equation = left.toInfix();
      equation = equation + this.getOperator();
      equation = "(" + equation + right.toInfix() + ")";
      return equation;    
   }

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
   public Expression simplify()
   {
      //Simplifies expressions in where a leaf is a 1 or a 0 or both are numbers
      if(this.left.simplify().equals(new IntegerOperand(0)) || this.right.simplify().equals(new IntegerOperand(0))){
         return new IntegerOperand(0);
      }
      else if(this.left.simplify().equals(new IntegerOperand(1))){
         return this.right;
      }
      else if(this.right.simplify().equals(new IntegerOperand(1))){
        return this.left;
      }
      else if(this.left.simplify() instanceof IntegerOperand && this.right.simplify() instanceof IntegerOperand){
         return new IntegerOperand(((IntegerOperand) this.left.simplify()).operand * ((IntegerOperand) this.right.simplify()).operand);
      }
      return new ProductExpression(left.simplify(), right.simplify());
   }   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
   public int evaluate(HashMap<String, Integer> assignments)
   {
      //////REPLACE WITH YOUR CODE
      return left.evaluate(assignments) * right.evaluate(assignments);
   }

   /**
    * @return a Set of the variables contained in this expression
    */
   public Set<String> getVariables()
   {
      //Obtaisn leaves for this operand
      TreeSet<String> tree = new TreeSet<String>();
      Set<String> leftVar = left.getVariables();
      Set<String> rightVar = right.getVariables();
      tree.addAll(leftVar);
      tree.addAll(rightVar);
      return tree;
   }

   /**
    * @param obj and Object to compare to
    * @return true if obj is an IntegerOperand with the same associated value
    */
   @Override
   public boolean equals(Object obj)
   {
      //Compares 2 expressions to see if they are the same 
      if(obj == null){
         return false;
      }

      if(!(obj instanceof ProductExpression)){
         return false;
      }
      ProductExpression other = (ProductExpression) obj;
      if((other.left.equals(this.left) && other.right.equals(this.right)) || (other.left.equals(this.right) && other.right.equals(this.left))){
         return true;
      }
      return false;
   }      
}

/**
 * A class representing an expression involving a division
 */
class QuotientExpression extends OperatorExpression
{
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
   public QuotientExpression(Expression left, Expression right)
   {
      super(left, right);
   }

   /**
    * @return a string representing the operand
    */
   protected String getOperator()
   {
      return "/";
   }

   /**
    * @return a String that represents this expression in prefix notation.
    */   
   public String toPrefix()
   {
      //Obtains the operater and leaves to create the equation
      return this.getOperator() + left.toPrefix() + right.toPrefix() ;
   }

   /**
    * @return a String that represents this expression in postfix notation.
    */  
   public String toPostfix()
   {
      //Obtains the operater and leaves to create the equation
      return left.toPostfix() + right.toPostfix() + this.getOperator();
   }   

   /**
    * @return a String that represents the expression in infix notation
    */
   public String toInfix()
   {
      //Obtains the operater and leaves to create the equation
      String equation = left.toInfix();
      equation = equation + this.getOperator();
      equation = "(" + equation + right.toInfix() + ")";
      return equation;    }

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
   public Expression simplify()
   {
      //Simplifies equations in where we are diving 0, or by 1 or both are the seme number or if they are both numbers
      if(this.left.simplify().equals(new IntegerOperand(0))){
         return new IntegerOperand(0);
      }
      else if(this.right.simplify().equals(left)){
         return new IntegerOperand(1);
      }
      else if(this.right.simplify().equals(new IntegerOperand(1))){
         return this.left;
      }
      else if(this.left.simplify() instanceof IntegerOperand && this.right.simplify() instanceof IntegerOperand ){
         return new IntegerOperand(((IntegerOperand) this.left.simplify()).operand / ((IntegerOperand) this.right.simplify()).operand);
      }
      return new QuotientExpression(left.simplify(), right.simplify());
   }   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
   public int evaluate(HashMap<String, Integer> assignments)
   {
         //evaluates the expression
      return left.evaluate(assignments) / right.evaluate(assignments);
   }

   /**
    * @return a Set of the variables contained in this expression
    */
   public Set<String> getVariables()
   {
      //obtians the leaves of the operator
      TreeSet<String> tree = new TreeSet<String>();
      Set<String> leftVar = left.getVariables();
      Set<String> rightVar = right.getVariables();
      tree.addAll(leftVar);
      tree.addAll(rightVar);
      return tree;
   }

   /**
    * @param obj and Object to compare to
    * @return true if obj is an IntegerOperand with the same associated value
    */
   @Override
   public boolean equals(Object obj)
   {
      //compares 2 expressions to see if they are the same
      if(obj == null){
         return false;
      }

      if(!(obj instanceof QuotientExpression)){
         return false;
      }
      QuotientExpression other = (QuotientExpression) obj;
      if((other.left.equals(this.left) && other.right.equals(this.right)) || (other.left.equals(this.right) && other.right.equals(this.left))){
         return true;
      }
      return false;
   }
}

