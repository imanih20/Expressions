
import java.util.*;
public class Assembly {
    private String assemblyCode;
    private int temp;
    private boolean isTempUsed;

    public Assembly(String parenthesizedInfix){
        temp=1;
        isTempUsed=false;
        setAssembly(parenthesizedInfix);
    }
    public void setAssembly(String parenthesizedInfix){
        String[] parenthesized=parenthesizedInfix.split(" ");
        String string = "";
        Stack<String> stack=new Stack<>();
        for (int i=0;i<parenthesized.length;i++){
            if (Expression.isParenthesise(parenthesized[i])){
                if (string!=""){
                    stack.push(string);
                    string="";
                }
                stack.push(parenthesized[i]);
            }
            else {
                string+=parenthesized[i]+ " ";
            }
        }
        String[] expression = new  String[stack.size()];
        for (int i=expression.length-1;i>=0;i--){
            expression[i]=stack.pop();
        }
        LinkedList<String> list=new LinkedList<>();
        for (int i=expression.length-1;i>=0;i--){
            if (expression[i].charAt(0)!='('){
                stack.push(expression[i]);
            }
            else {
                while (stack.peek().charAt(0)!=')'){
                    list.addLast(stack.pop());
                }
                stack.pop();
            }
        }
        boolean store;
        string = "";
        assemblyCode="";
        while (!list.isEmpty()){
            string=list.removeFirst();
            String[] next;
            if (!list.isEmpty()) {
                next = list.getFirst().split(" ");
                if (next.length == 3)
                    store = true;
                else if (next.length == 2 && next[0].charAt(0) == '~')
                    store = true;
                else if (next.length == 2 && (next[1].charAt(0) == '^' || next[1].charAt(0) == '/' || next[1].charAt(0) == '-'))
                    store = true;
                else
                    store = false;
            }
            else {
                store=false;
            }
            assemblyCode += expressionToAssembly(string,store);

        }
    }
    private String expressionToAssembly(String exp,boolean store){
        String [] expression = exp.split(" ");
        char operator = 0;
        if (expression.length==3)
            operator=expression[1].charAt(0);
        else if (expression.length==2&&Expression.isOperator(expression[0])){
            operator=expression[0].charAt(0);
        }
        else if (expression.length==2&& Expression.isOperator(expression[1])){
            operator=expression[1].charAt(0);
        }
        else if (expression.length==1){
            operator=expression[0].charAt(0);
        }
        String assemblyCode="";
        switch (operator){
            case '+':
                assemblyCode=add(expression,store);
                break;
            case '-':
                assemblyCode=sub(expression,store);
                break;
            case '/':
                assemblyCode=div(expression, store);
                break;
            case '*':
                assemblyCode=mul(expression,store);
                break;
            case '^':
                assemblyCode=pow(expression,store);
                break;
            case '~':
                assemblyCode=neg(expression,store);
                break;
        }
        return assemblyCode;
    }
    public String getAssemblyCode(){
        return assemblyCode;
    }
    private String add(String[] expression,boolean store){
        String assemblyCode="";
        if (expression.length==3){
            assemblyCode="LDA "+expression[0]+"\n\t\tADD "+expression[2]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[0])){
            assemblyCode="ADD "+expression[1]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[1])){
            assemblyCode="ADD "+expression[0]+"\n\t\t";
        }
        else if (expression.length==1){
            if (!isTempUsed){
                temp--;
            }
            assemblyCode="ADD TEMP" + temp + "\n\t\t";
            isTempUsed=false;
        }
        if (store){
            if (isTempUsed){
                temp++;
            }
            assemblyCode+="STR TEMP"+temp+"\n\t\t";
            isTempUsed=true;
        }
        return assemblyCode;
    }
    private String mul(String[] expression,boolean store){
        String assemblyCode="";
        if (expression.length==3){
            assemblyCode="LDA "+expression[0]+"\n\t\tMUL "+expression[2]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[0])){
            assemblyCode="MUL "+expression[1]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[1])){
            assemblyCode="MUL "+expression[0]+"\n\t\t";
        }
        else if (expression.length==1){
            if (!isTempUsed){
                temp--;
            }
            assemblyCode="MUL TEMP" + temp+"\n\t\t";
            isTempUsed=false;
        }
        if (store){
            if (isTempUsed){
                temp++;
            }
            assemblyCode+="STR TEMP"+temp+"\n\t\t";
            isTempUsed=true;
        }
        return assemblyCode;
    }
    private String sub(String[] expression, boolean store){
        String assemblyCode="";
        if (expression.length==3){
            assemblyCode="LDA "+expression[0]+"\n\t\tSUB "+expression[2]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[0])){
            assemblyCode="SUB "+expression[1]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[1])){
            assemblyCode="LDA "+expression[0]+"\n\t\tSUB TEMP"+temp+"\n\t\t";
            isTempUsed=false;
        }
        else if (expression.length==1){
            if (!isTempUsed){
                temp--;
            }
            assemblyCode="SUB TEMP"+temp+"\n\t\t";
            isTempUsed=false;
        }
        if (store){
            if (isTempUsed){
                temp++;
            }
            assemblyCode+="STR TEMP"+temp+"\n\t\t";
            isTempUsed=true;
        }
        return assemblyCode;
    }
    private String div(String [] expression,boolean store){
        String assemblyCode="";
        if (expression.length==3){
            assemblyCode="LDA "+expression[0]+"\n\t\tDIV "+expression[2]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[0])){
            assemblyCode="DIV "+expression[1]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[1])){
            assemblyCode="LDA "+expression[0]+"\n\t\tDIV TEMP"+temp+"\n\t\t";
            isTempUsed=false;
        }
        else if (expression.length==1){
            if (!isTempUsed){
                temp--;
            }
            assemblyCode="DIV TEMP"+temp+"\n\t\t";
            isTempUsed=false;
        }
        if (store){
            if (isTempUsed){
                temp++;
            }
            assemblyCode+="STR TEMP"+temp+"\n\t\t";
            isTempUsed=true;
        }
        return assemblyCode;
    }
    private String pow(String[] expression,boolean store){
        String assemblyCode="";
        if (expression.length==3){
            assemblyCode="LDA "+expression[0]+"\n\t\tPOW "+expression[2]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[0])){
            assemblyCode="POW "+expression[1]+"\n\t\t";
        }
        else if (expression.length==2&&Expression.isOperator(expression[1])){
            assemblyCode="LDA "+expression[0]+"\n\t\tPOW TEMP"+temp+"\n\t\t";
            isTempUsed=false;
        }
        else if (expression.length==1){
            if (!isTempUsed){
                temp--;
            }
            assemblyCode="POW TEMP"+temp+"\n\t\t";
            isTempUsed=false;
        }
        if (store){
            if (isTempUsed){
                temp++;
            }
            assemblyCode+="STR TEMP"+temp+"\n\t\t";
            isTempUsed=true;
        }
        return assemblyCode;
    }
    private String neg(String[] expression,boolean store){
        String assemblyCode="LDA "+expression[1]+"\n\t\tNEG\n\t\t";
        if (store){
            if (isTempUsed){
                temp++;
            }
            assemblyCode+="STR TEMP"+ temp +"\n\t\t";
            isTempUsed=true;
        }
        return assemblyCode;
    }
}
/*tanzim kardam temp
behine kardan toAssembly
 */