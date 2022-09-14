package calculator;

import javax.swing.text.StyleContext;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    static Map<String, Integer> userVariables = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here

        String userInput = "";
        userInput = scanner.nextLine();
        while (!userInput.equals("/exit")) {
            ActionProcessor(userInput);
            userInput = scanner.nextLine();
        }
        System.out.println("Bye!");
    }

    public static void ActionProcessor(String userInput) {
        if(userInput.equals("")){
            return;
        }
        if(!validInput(userInput)){
            return;
        }
        if(userInput.charAt(0) == '/'){
            commandHandler(userInput);
        }else if (userInput.contains("=")){
            variableAssignment(userInput);
        } else{
            if(userInput.matches(".*[a-zA-Z]+.*")){
                userInput = variableProcessor(userInput);
            }
            processEquation(userInput);
        }
    }

    public static String determineSubstraction(String subtractionIndicator){
        if(!subtractionIndicator.contains("-")){
            return subtractionIndicator;
        }
        String[] splitIndicator = subtractionIndicator.split("[^-]");
        if(splitIndicator[1].length() % 2 == 0){
            return subtractionIndicator.replace(splitIndicator[1], "+");
        }else{
            return subtractionIndicator.replace(splitIndicator[1], "-");
        }
    }


    public static String mathProcessor(String toBeProcessed){
        if(toBeProcessed.contains("--")){
            toBeProcessed = determineSubstraction(toBeProcessed);
        }
        //2+3
        String[] splitEquation = toBeProcessed.split("[+\\-/*]");
        if (splitEquation.length ==1){
            return toBeProcessed;
        }
        int result = 0;
        if(toBeProcessed.contains("+")){
            result = Integer.parseInt(splitEquation[0]) + Integer.parseInt(splitEquation[1]);
        } else if (toBeProcessed.contains("-")) {
            result = Integer.parseInt(splitEquation[0]) - Integer.parseInt(splitEquation[1]);
        } else if (toBeProcessed.contains("*")) {
            result = Integer.parseInt(splitEquation[0]) * Integer.parseInt(splitEquation[1]);
        } else if (toBeProcessed.contains("/")) {
            result = Integer.parseInt(splitEquation[0]) / Integer.parseInt(splitEquation[1]);
        }
        return String.valueOf(result);
    }

    public static boolean hasBrackets(String userInput){
        return userInput.contains("(");
    }
    public static void processEquation(String userInput){
        //"\\++"
        while (hasBrackets(userInput)){
            int rightBracketIndex = userInput.indexOf(")");
            int leftBracketIndex = userInput.lastIndexOf("(", rightBracketIndex);
            String tobeProcessed = userInput.substring(leftBracketIndex, rightBracketIndex+1);
            System.out.println(tobeProcessed);
            String result = mathProcessor(tobeProcessed.substring(1, tobeProcessed.length()-1));
            userInput = userInput.replace(tobeProcessed, result);
        }
        userInput = mathProcessor(userInput);
        System.out.println(userInput);
    }
    public static void variableAssignment(String userInput){
        userInput = userInput.replace(" ", "");
        String[] variableSetter = userInput.split("=");
        if(variableSetter.length > 2) {
            System.out.println("Invalid variable");
        }else if(variableSetter[1].matches("[a-zA-Z]")){
            System.out.println("Invalid assignment");
        }else{
            userVariables.put(variableSetter[0], Integer.parseInt(variableSetter[1]));
        }
    }

    public static String variableProcessor(String userInput){
        String[] splitEquation = userInput.split("[+\\-/*()]");
        for(int i = 0; i < splitEquation.length; i++){
            if (userVariables.containsKey(splitEquation[i])){
                int value = userVariables.get(splitEquation[i]);
                userInput = userInput.replace(splitEquation[i], String.valueOf(value));
            }
        }
        return userInput;
    }
    public static boolean validInput(String userInput){
        if(userInput.charAt(0) == '/'){
            if(userInput.contains("exit")
                    || userInput.contains("help")
            ){
                return true;
            }else{
                System.out.println("Unknown command");
                return false;
            }
        } else if ((userInput.contains("(") && !userInput.contains(")"))) {
            System.out.println("Invalid expression");
            return false;
        } else if((userInput.contains("(") && userInput.contains(")"))){
            int leftBracket = patternOccurrence("\\(", userInput);
            int rightBracket = patternOccurrence("\\)", userInput);
            if(leftBracket != rightBracket){
                System.out.println("Invalid expression");
                return false;
            }
        }
        return true;
    }

    public static int patternOccurrence(String regex, String inputString){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);
        int counter = 0;
        while (matcher.find()){
            counter++;
        }
        return counter;
    }

    public static void commandHandler(String userInput){
        if(userInput.equals("/help")){
            System.out.println("The program calculates the sum of numbers");
        }
    }
}

