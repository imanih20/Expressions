
import java.util.Scanner;

public class Main {
    static Scanner scan=new Scanner(System.in);
    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Enter a infix expression:");
        String exp=scan.nextLine();
        Expression expression=new Expression(exp);
        if (!expression.isInfix(exp)){
            System.out.println("please enter a valid infix expression.");
            return;
        }
        System.out.println("Infix :");
        System.out.print("        ");
        String string="";
        for (int i=0;i<expression.getInfix().length;i++){
            string+=expression.getInfix()[i]+" ";
        }
        System.out.println(string);
        System.out.println("Parenthesized infix :");
        System.out.println("        "+expression.getParenthesizedInfix());
        System.out.println("Postfix :");
        System.out.print("        ");
        string="";
        for (int i=0;i<expression.getPostfix().length;i++){
            string+=expression.getPostfix()[i]+" ";
        }
        System.out.println(string);
        System.out.println("Prefix :");
        System.out.println("        "+expression.getPrefix());
        System.out.println("Assembly code:");
        System.out.println("        "+expression.getAssemblyCode());
    }
}
