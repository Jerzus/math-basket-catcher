package com.example.mathbasketcatcher.modes;

// tryb Count and click - wyświetla się działanie i kulki z liczbami - użytkownik klika kulkę z wynikiem działania lub przycisk pomiń jeśli już raz kliknął daną liczbę
public class CountAndClickMode extends GameMode {

    public Expression expression;

    public CountAndClickMode() {
        shuffle();
        newExpression();
    }

    @Override
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

    @Override
    public void alreadyUsed() {
        if (used[expression.result]) {
            lastGood = true;
        } else {
            errors++;
            lastGood = false;
        }
        nextMove();
    }

    private void nextMove() {
        lastMove = (lastGood ? "✓ " : "✗ ") + expression + " = " + expression.result;
        shuffle();
        if (!isOver()) {
            newExpression();
        }
    }

    // generowanie działania: losowy wynik z określonego przedziału i określonego wystąpienia, działanie dopasowane do wyniku
    private void newExpression() {
        int result = Result.result(used);
        expression = Expression.random(result);
    }
}
