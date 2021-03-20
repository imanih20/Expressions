import java.util.*;

public class Expression {
    private String[] infix;
    private String[] postfix;
    private String prefix;
    private String expression;
    private BinaryTree<String> treeExpression;
    private String parenthesizedInfix;
    private String assemblyCode;
    private static byte[][] prioritize= { //to declare prioritaize of each operator in stack
            {-1,2,0,0,0,0,0,0},
            {-1,1,1,1,1,1,1,1},
            {-1,1,0,1,0,1,0,0},
            {-1,1,0,0,0,0,0,0},
            {-1,1,1,1,1,1,0,1},
            {-1,1,0,0,0,0,0,0},
            {-1,1,1,1,1,1,0,1},
            {-1,1,0,1,0,1,0,0}
    };
    public Expression(String expression){   
        setExpression(expression);
        try {
            setInfix();
            setPostfix();
            setTree();
            setPrefix();
            setParenthesizedInfix();
            setAssemblyCode();
        }
        catch (Exception e){}
    }
    public boolean isInfix(String expression){
        expression=removeSpaces(expression);
        char[] chars=expression.toCharArray();
        int openParenthesizedNum=0;
        int closeParenthesizedNum=0;
        for (int i=0;i<chars.length;i++){
            if (chars[i]=='(')
                openParenthesizedNum++;
            else if (chars[i]==')')
                closeParenthesizedNum++;
            if (!isOperand(chars[i])&&!isOperator(chars[i])&&!isParenthesise(chars[i])) {
                System.out.println("just (a-z) or (A-Z) or (0-9) or '.' for operands accepted");
                return false;
            }
            if (i==0&&isOperator(chars[i])&&chars[i]!='~') {
                System.out.println("first variable cannot be an operator!");
                return false;
            }
            else if (isOperator(chars[i])&&isOperator(chars[i+1])&&i+1<chars.length&&chars[i+1]!='~') {
                System.out.println(" after an operator you cannot add another operator except '~'!");
                return false;
            }
            else if (chars[i]=='('&&i>0&&!isOperator(chars[i-1])) {
                System.out.println("before '(' just an operator accepted!");
                return false;
            }
            else if (chars[i]=='('&&i+1<chars.length&&chars[i+1]!='~'&&isOperator(chars[i+1])) {
                System.out.println("after '(' you can't add an operator!");
                return false;
            }
            else if (chars[i]==')'&&i>0&&isOperator(chars[i-1])) {
                System.out.println("after an operator you can't add ')' !");
                return false;
            }
            else if (chars[i]==')'&&i+1<chars.length&&!isOperator(chars[i+1])) {
                System.out.println("after ')' you can just add an operator!");
                return false;
            }
            else if (chars[i]=='('&&i+1<chars.length&&chars[i+1]==')') {
                System.out.println("between '(' and ')' just an expression accepted");
                return false;
            }
            else if (i==chars.length-1&&isOperator(chars[i])) {
                System.out.println("last variable must be an operand!");
                return false;
            }
        }
        if (openParenthesizedNum!=closeParenthesizedNum) {
            System.out.println("One parenthesized is missing!");
            return false;
        }
        return true;
    }
    private boolean isOperand(char character){
        if ((int) character == 46||((int) character > 47&&(int) character < 58)||((int) character > 64&&(int) character < 91)) return true;
        if ((int) character > 96) if ((int) character < 123) return true;
        return false;
    }
    public static String removeSpaces(String s){
        String[] strings=s.split(" ");
        StringBuilder sBuilder = new StringBuilder();
        for (String string : strings) {
            sBuilder.append(string);
        }
        return sBuilder.toString();
    }
   private void setExpression(String expression){
        expression=removeSpaces(expression);
        char[] chars=expression.toCharArray();
        this.expression="";
        for (int i=0;i<chars.length;i++){
            if ((i==0&&(isOperator(chars[i])||isParenthesise(chars[i])))||chars[i]=='~'){
                this.expression+=chars[i]+" ";
            }
            else if (isOperator(chars[i])){
                this.expression+=" "+chars[i]+" ";
            }
            else if (chars[i]=='('){
                this.expression+=chars[i]+" ";
            }
            else if (chars[i]==')'){
                this.expression+=" "+chars[i];
            }
            else {
                this.expression+=chars[i];
            }
        }
    }
    public static boolean isParenthesise(String string){
        return isParenthesise(string.charAt(0));
    }
    private static boolean isParenthesise(char character){
        if((int)character==40||(int)character==41){
            return true;
        }
        return false;
    }
    public static boolean isOperator(char character){
        if (character=='+'||character=='-'||character=='*'||character=='/'||character=='^'||character=='~'){
            return true;
        }
        return false;
    }
    public static boolean isOperator(String string){
        return isOperator(string.charAt(0));
    }
    private int numParenthesize(String []infix){
        int num=0;
        for (int i=0;i<infix.length;i++){
            if (isParenthesise(infix[i])){
                num++;
            }
        }
        return num;
    }
    private void setInfix(){
        infix=expression.split(" ");
        for (int i=0;i<infix.length;i++){
            infix[i]=infix[i].trim();
        }
    }
    public void setPostfix(){
        String postfix="";
        Stack<String> stack = new Stack<>();
        for (int i=0;i<infix.length;i++){
            if (!isParenthesise(infix[i])&&!isOperator(infix[i])){
                postfix+=infix[i]+" ";
            }
            else if (stack.isEmpty()){
                stack.push(infix[i]);
            }else {
                if (infix[i].charAt(0) == '(') {
                    stack.push(infix[i]);
                } else if (infix[i].charAt(0) == ')') {
                    while (!stack.isEmpty() && stack.peek().charAt(0) !='(') {
                        postfix+=stack.pop()+" ";
                    }
                    if (!stack.isEmpty() && stack.peek().charAt(0) == '(') {
                        stack.pop();
                    }
                } else if (isOperator(infix[i])) {
                    while (!stack.isEmpty()) {
                        int j = (infix[i].codePointAt(0) % 40) % 10;
                        int k = (stack.peek().codePointAt(0) % 40) % 10;
                        if (prioritize[j][k] == 0) {
                            postfix+=stack.pop()+" ";
                        } else {
                            break;
                        }
                    }
                    stack.push(infix[i]);
                }
            }
        }
        while (!stack.isEmpty()){
            postfix+=stack.pop()+" ";
        }
        this.postfix=postfix.split(" ");
    }
    public void setTree(){
        BinaryTree<String> tree=new BinaryTree<>(postfix[postfix.length-1],postfix.length);
        String s=null;
        int pointer=tree.root.position;
        for (int i=postfix.length-2;i>=0;i--) {
            s += pointer + " ";
            if (isOperator(postfix[i]) && tree.getRight(pointer) == 0) {
                tree.setRight(pointer, postfix[i]);
                pointer = tree.getRight(pointer);
            } else if (isOperator(postfix[i]) && tree.getRight(pointer) != 0 && tree.getLeft(pointer) == 0) {
                if ( tree.find(tree.root, pointer).data.charAt(0) == '~') {
                    pointer = tree.getParent(pointer);
                    i++;
                    continue;
                }
                tree.setLeft(pointer, postfix[i]);
                pointer = tree.getLeft(pointer);
            } else if (!isOperator(postfix[i]) && tree.getRight(pointer) == 0) {
                tree.setRight(pointer, postfix[i]);
            } else if (!isOperator(postfix[i]) && tree.getRight(pointer) != 0 && tree.getLeft(pointer) == 0) {
                if (tree.find(tree.root, pointer).data.charAt(0) == '~') {
                    pointer = tree.getParent(pointer);
                    i++;
                    continue;
                }
                tree.setLeft(pointer, postfix[i]);
                pointer = tree.getParent(pointer);
            } else if (tree.getLeft(pointer) != 0 && tree.getRight(pointer) != 0) {
                pointer = tree.getParent(pointer);
                i++;
            }
            s += pointer;
        }
        treeExpression=tree;
    }
    private void setPrefix(){
        treeExpression.setPreOrder(treeExpression.root);
        prefix=treeExpression.getPreOrder();
    }
    private void setAssemblyCode(){
        Assembly assembly=new Assembly(parenthesizedInfix);
        assemblyCode=assembly.getAssemblyCode();
    }
    private void setParenthesizedInfix(){
        treeExpression.setInorder(treeExpression.root);
        parenthesizedInfix=treeExpression.getInorder();
    }
    public String [] getInfix(){
        return infix;
    }
    public String [] getPostfix(){
        return postfix;
    }
    public String getPrefix(){
        return prefix;
    }
    public String getAssemblyCode(){
        return assemblyCode;
    }
    public String getParenthesizedInfix(){
        return parenthesizedInfix;
    }
}
