package com.example.mathbasketcatcher;

import java.util.Random;

// tryb Licz i klikaj - wyświetla się działanie i kulki z liczbami - użytkownik klika kulkę z wynikiem działania lub przycisk pomiń jeśli już raz kliknął daną liczbę
public class CalcMode {
    
    public boolean[] used = new boolean[10];
    public int[] order = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public int score = 0;
    public int errors = 0;
    public Expression expression;
    public String lastMove = "";
    public boolean lastGood = true;

    private Random random = new Random();

    public CalcMode() {
        shuffle();
        newExpression();
    }

    public void choose(int number) {
        if (used[number]) {
            // błąd: kliknięta zużyta liczba, więc odznaczamy ją
            used[number] = false;
            errors++;
            lastGood = false;
        } else if (number == expression.result) {
            // kliknięta dobra liczba
            used[number] = true;
            score++;
            lastGood = true;
        } else {
            // błąd: kliknięta zła liczba
            errors++;
            lastGood = false;
        }
        nextMove();
    }

    // przycisk Already used - aby pominąć liczbę, która już była kliknięta
    public void alreadyUsed() {
        if (used[expression.result]) {
            lastGood = true;
        } else {
            errors++;
            lastGood = false;
        }
        nextMove();
    }

    public boolean isOver() {
        for (int i = 0; i < 10; i++) {
            if (!used[i]) {
                return false;
            }
        }
        return true;
    }

    private void nextMove() {
        lastMove = (lastGood ? "✓ " : "✗ ") + expression + " = " + expression.result;
        shuffle();
        if (!isOver()) {
            newExpression();
        }
    }

    // generowanie działania - losowy wynik z określonego przedziału i określonego wystąpienia, działanie dopasowane do wyniku
    private void newExpression() {
        int result = Result.result(used);
        expression = Expression.random(result);
    }

    // tasowanie kulek aby nie dało się zapamiętać ich po położeniu
    private void shuffle() {
        for (int i = 9; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = order[i];
            order[i] = order[j];
            order[j] = temp;
        }
    }
}
