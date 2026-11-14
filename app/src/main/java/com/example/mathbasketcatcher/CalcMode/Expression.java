package com.example.mathbasketcatcher;

import java.util.Random;

public class Expression {

    public int a;
    public int b;
    public char op;   // '+' albo '-'
    public int result;

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
