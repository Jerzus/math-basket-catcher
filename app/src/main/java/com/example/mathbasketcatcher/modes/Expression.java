package com.example.mathbasketcatcher.modes;

import java.util.Random;

public class Expression {

    public int a;
    public int b;
    public char op;   // '+' albo '-'
    public int result;
    public boolean gapLeft;   // w Fill the gaps: luka po lewej albo po prawej stronie znaku

    public Expression(int a, char op, int b) {
        this.a = a;
        this.op = op;
        this.b = b;
        if (op == '+') {
            result = a + b;
        } else {
            result = a - b;
        }
    }

    @Override
    public String toString() {
        // ujemna liczba po prawej w nawiasie
        String right = b < 0 ? "(" + b + ")" : "" + b;
        return a + " " + op + " " + right;
    }

    // brakująca liczba w działaniu z luką
    public int answer() {
        return gapLeft ? a : b;
    }

    // działanie z tekstem w miejscu luki, np. "? + 4 = 9" albo "7 + 4 = 9"
    public String withGap(String text) {
        String left = "" + a;
        String right = b < 0 ? "(" + b + ")" : "" + b;
        if (gapLeft) {
            left = text;
        } else {
            right = text;
        }
        return left + " " + op + " " + right + " = " + result;
    }

    // działanie z luką: brakująca liczba answer 0-9, druga liczba other od -16 do 16, wynik dowolny
    public static Expression randomGap(int answer) {
        Random random = new Random();
        int other = random.nextInt(33) - 16;
        char op = random.nextBoolean() ? '+' : '-';
        Expression e;
        if (random.nextBoolean()) {
            e = new Expression(answer, op, other);
            e.gapLeft = true;
        } else {
            e = new Expression(other, op, answer);
        }
        return e;
    }

    // losujemy liczby od -9 do 9, aż wyjdzie podany wynik
    public static Expression random(int result) {
        Random random = new Random();
        while (true) {
            int a = random.nextInt(19) - 9;
            int b = random.nextInt(19) - 9;
            char op = random.nextBoolean() ? '+' : '-';
            Expression e = new Expression(a, op, b);
            if (e.result == result) {
                return e;
            }
        }
    }
}
