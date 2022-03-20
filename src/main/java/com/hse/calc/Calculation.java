package com.hse.calc;

import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Calculation {

    static void greetings() {
        System.out.print("""
                *** КАЛЬКУЛЯТОР ДРОБЕЙ ***
                                            
                Данная программа реализует решение математического выражения с дробями
                                            
                Пожалуйста, следуйте инструкции для корректной работы программы:
                    - используйте цифры для ввода значений числителя и знаменателя дроби
                    - используйте '/' для обозначения черты деления дроби
                    - используйте '+', '-', '*' или ':' для обозначения арифметической операции
                    - для ввода отрицательного значения поставте '-' перед числителем или знаменателем дроби
                    - минимальный ввод для работы программы: две дроби и арифметическая операция между ними
                    - вы можете использовать скобки '(' и ')' для указания порядка действий
                    - количество дробей, скобок и арифметических операций неограничено
                    - вы можете ставить пробелы в выражении где угодно и в любом количестве
                    - для завершения работы введите 'quit'
                    Пример ввода: ((-1/3 * 3/ 4 - 1/2)*2 / 1)  :(- 1/-5 + ( -6/7 ) )
                """);
        System.out.print("\nВведите выражение: ");
    }

    static void startWork() {
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();
        while (true) {
            if (Objects.equals(expression, "quit")) {
                System.out.println("Завершение работы");
                break;
            }
            expression = checkUserInput(expression);
            if (expression == null) {
                System.out.println("""
                        ОШИБКА: Неверный ввод
                                Попробуйте еще раз
                        """);
            } else {
                String answer = getAnswer(expression);
                if (answer == null) {
                    System.out.println("""
                            ОШИБКА: Деление на ноль
                                    Пожалуйста, повторите попытку
                            """);
                } else System.out.println("Ответ: " + answer + "\n");
            }
            System.out.print("Введите следующее выражение: ");
            expression = in.nextLine();
        }
        in.close();
    }

    private static int getPriority(char element){
        if (element == '*' | element == ':')      return 3;
        else if (element == '+' | element == '-') return 2;
        else if (element == '(')                  return 1;
        else if (element == ')')                  return -1;
        else                                      return 0;
    }

    static String checkUserInput(String userInput) {
        while (userInput.contains(" ")) {
            userInput = userInput.replace(" ", "");
        }
        Pattern correctInputPattern = Pattern.compile("((([-]?[(]*([-]?\\d+)/([-]?\\d+)[+*:-][(]*)+|" +
                "([-]?[(]*([-]?d+)/([-]?\\d+)[)]*)+)(([+*:-]?([-]?\\d+)/([-]?\\d+))[)]*[+*:-]?)*)+");
        Matcher correctInputMatcher = correctInputPattern.matcher(userInput);
        if (!(correctInputMatcher.matches())) return null;
        int bracketCount = 0;
        for (int i = 0; i < userInput.length(); i++) {
            if (userInput.charAt(i) == '(' ) {
                bracketCount += 1;
                if (userInput.charAt(i + 1) == '+' | userInput.charAt(i + 1) == '*' |
                        userInput.charAt(i + 1) == ':') return null;
            }
            else if (userInput.charAt(i) == ')') bracketCount -= 1;
            else if ((getPriority(userInput.charAt(i)) == 2 | getPriority(userInput.charAt(i)) == 3)
                    && i + 1 == userInput.length()) return null;
            else if ((getPriority(userInput.charAt(i)) == 2 | getPriority(userInput.charAt(i)) == 3) &&
                    (getPriority(userInput.charAt(i + 1)) == 2 | getPriority(userInput.charAt(i + 1)) == 3)) return null;
        }
        if (bracketCount != 0) return null;
        return userInput;
    }

    static String getAnswer(String expression) {
        StringBuilder expressionRPN = reversePolishNotation(expression);
        Fraction calculatedAnswer;
        try {
            calculatedAnswer = countAnswer(expressionRPN);
        } catch (ArithmeticException e) {
            return null;
        }
        return calculatedAnswer.getValue();
    }

    private static StringBuilder reversePolishNotation(String expression){
        StringBuilder expressionRPN = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            if (getPriority(expression.charAt(i)) == 0) expressionRPN.append(expression.charAt(i));
            else if (getPriority(expression.charAt(i)) == 1) stack.push(expression.charAt(i));
            else if (getPriority(expression.charAt(i)) > 1) {
                if (expression.charAt(i) == '-' && i == 0) {
                    expressionRPN.append(" 0/1 ");
                    stack.push('-');
                    continue;
                } else if (expression.charAt(i) == '-' && expression.charAt(i - 1) == '(') {
                    expressionRPN.append(" 0/1 ");
                    stack.push('-');
                    continue;
                } else if (expression.charAt(i) == '-' && expression.charAt(i - 1) == '/') {
                    expressionRPN.append(expression.charAt(i));
                    continue;
                }
                expressionRPN.append(" ");
                while (!(stack.empty())) {
                    if (getPriority(stack.peek()) >= getPriority(expression.charAt(i)))
                        expressionRPN.append(stack.pop());
                    else break;
                }
                stack.push(expression.charAt(i));
            }
            else if (getPriority(expression.charAt(i)) == -1) {
                expressionRPN.append(" ");
                while (getPriority(stack.peek()) != 1) expressionRPN.append(stack.pop());
                stack.pop();
            }
        }
        while (!(stack.empty())) expressionRPN.append(stack.pop());
        return expressionRPN;
    }

    private static Fraction countAnswer(StringBuilder expressionRPN){
        StringBuilder stringElement;
        Fraction fractionElement;
        Stack<Fraction> stack = new Stack<>();
        for (int i = 0; i < expressionRPN.length(); i++) {
            if (expressionRPN.charAt(i) == ' ') continue;
            if (getPriority(expressionRPN.charAt(i)) == 0 | (expressionRPN.charAt(i) == '-' &&
                    (i == 0 | expressionRPN.charAt(i - 1) == '/' | expressionRPN.charAt(i - 1) == '('))) {
                stringElement = new StringBuilder();
                while (expressionRPN.charAt(i) != ' ' && (getPriority(expressionRPN.charAt(i)) == 0 |
                        (expressionRPN.charAt(i) == '-' && (i == 0 | expressionRPN.charAt(i - 1) == '/' |
                                expressionRPN.charAt(i - 1) == '(')))) {
                    stringElement.append(expressionRPN.charAt(i++));
                    if (i == expressionRPN.length()) break;
                }
                String[] parseElement = stringElement.toString().split("/");
                int elementNumenator = Integer.parseInt(parseElement[0]);
                int elementDenominator = Integer.parseInt(parseElement[1]);
                fractionElement = new Fraction(elementNumenator, elementDenominator);
                stack.push(fractionElement);
            }
            if (getPriority(expressionRPN.charAt(i)) > 1) {
                Fraction second = stack.pop(), first = stack.pop();
                switch (expressionRPN.charAt(i)) {
                    case '+' -> stack.push(Fraction.summation(first, second));
                    case '-' -> stack.push(Fraction.substraction(first, second));
                    case '*' -> stack.push(Fraction.multiplication(first, second));
                    case ':' -> stack.push(Fraction.division(first, second));
                }
            }
        }
        return stack.peek();
    }
}
