package calculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    static Map<String, Integer> userVariables = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here

        String userInput = "";
        userInput = scanner.nextLine();
        while(!userInput.equals("/exit")){
            ActionProcessor(userInput);
            userInput = scanner.nextLine();
        }
        System.out.println("Bye!");
    }

    public static void ActionProcessor(String userInput){
        if(userInput.length() == 0){
            return;
        } else if(userInput.equals("/help")){
            System.out.println("The program calculates the sum of numbers");
            return;
        } else if(userInput.charAt(0) == '/'){
            System.out.println("Unknown command");
            return;
        }else if(userVariables.containsKey(userInput)){
            System.out.println(userVariables.get(userInput));
            return;
        }
        else if(!userInput.contains("=") && userInput.matches("[a-zA-Z]+")){
            System.out.println("Unknown variable");
            return;
        }

        if(userInput.contains("=")){
            String[] splitInput = userInput.split("=");
            for(int i = 0; i < splitInput.length; i++){
                splitInput[i] = splitInput[i].trim();
            }
            if(splitInput.length > 2){
                System.out.println("Invalid assignment");
                return;
            }
            userVariable(splitInput);
        }else{
            String[] splitInput = userInput.split(" ");
            for(int i = 0; i < splitInput.length; i++ ){
                if(userVariables.containsKey(splitInput[i])){
                    splitInput[i] = String.valueOf(userVariables.get(splitInput[i]));
                }
            }
            System.out.println(calculate(splitInput));
        }

    }

    public static Character determineOperator(String operator){
        if(operator.length() % 2 == 0 && operator.charAt(0) == '-'){
            return '+';
        }else{
            return operator.charAt(0);
        }
    }
    public static Integer calculate(String[] requestedCalculation){
        int num = Integer.parseInt(requestedCalculation[0]);
        char operator = ' ';
        for(int i = 1; i < requestedCalculation.length; i++){
            if(requestedCalculation[i].contains("+")){
                operator = determineOperator(requestedCalculation[i]);
                continue;
            }
            else if(requestedCalculation[i].contains("-")) {
                operator = determineOperator(requestedCalculation[i]);
                continue;
            } else {
                if(operator == '+'){
                    num += Integer.parseInt(requestedCalculation[i]);
                }else if(operator == '-'){
                    num -= Integer.parseInt(requestedCalculation[i]);
                }
            }
        }
        return num;
    }

    public static void userVariable(String[] userDeclaration){
        if(!validateIdentifier(userDeclaration[0]))
        {
            System.out.println("Invalid identifier");
            return;
        }else if(!userVariables.containsKey(userDeclaration[1]) && userDeclaration[1].matches(".*[a-z].*"))
        {
            System.out.println("Invalid assignment");
            return;
        }
        if(userDeclaration.length > 2) {
            throw new IllegalArgumentException("Array too long");
        }
        if(userVariables.containsKey(userDeclaration[1])){
            userVariables.put(userDeclaration[0], userVariables.get(userDeclaration[1]));
        }else{
            userVariables.put(userDeclaration[0], Integer.parseInt(userDeclaration[1]));
        }
    }

    public static boolean validateIdentifier(String identifier){
        return identifier.matches("[a-zA-Z]+");
    }
}
