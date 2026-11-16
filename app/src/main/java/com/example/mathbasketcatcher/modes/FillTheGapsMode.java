package com.example.mathbasketcatcher.modes;

// tryb Fill the gaps - dwa działania z luką, użytkownik zaznacza działanie i kulkę z brakującą liczbą (albo przycisk Already used)
public class FillTheGapsMode extends GameMode {

    public static final int ALREADY_USED = 10;   // zaznaczony przycisk zamiast kulki

    public Expression[] equations = new Expression[2];
    public int selectedEquation = -1;   // -1 to nic nie zaznaczone
    public int selectedBall = -1;

    public FillTheGapsMode() {
        shuffle();
        for (int i = 0; i < equations.length; i++) {
            equations[i] = Expression.randomGap(Result.result(used));
        }
    }

    public void chooseEquation(int i) {
        // drugie kliknięcie w to samo odznacza
        selectedEquation = selectedEquation == i ? -1 : i;
        move();
    }

    @Override
    public void choose(int number) {
        selectedBall = selectedBall == number ? -1 : number;
        move();
    }

    @Override
    public void alreadyUsed() {
        choose(ALREADY_USED);
    }

    // ruch dopiero, gdy zaznaczone jest działanie i kulka
    private void move() {
        if (selectedEquation == -1 || selectedBall == -1) {
            return;
        }
        Expression e = equations[selectedEquation];
        int answer = e.answer();
        if (selectedBall == ALREADY_USED) {
            if (used[answer]) {
                // poprawna odpowiedź, losujemy nowe działanie
                lastGood = true;
                lastMove = "✓ " + e.withGap("" + answer);
                newEquation();
            } else {
                // błąd: kliknięty przycisk Already used, a brakująca liczba nie jest zużyta
                errors++;
                lastGood = false;
                lastMove = "✗ Already used";
            }
        } else if (used[selectedBall]) {
            // błąd: kliknięta zużyta liczba, więc odznaczamy ją
            used[selectedBall] = false;
            errors++;
            lastGood = false;
            lastMove = "✗ " + e.withGap("" + selectedBall);
        } else if (selectedBall == answer) {
            // poprawna odpowiedź, zaznaczamy ją jako zużytą i losujemy nowe działanie
            used[answer] = true;
            score++;
            lastGood = true;
            lastMove = "✓ " + e.withGap("" + answer);
            newEquation();
        } else {
            // błąd: kliknięta liczba nie jest odpowiedzią
            errors++;
            lastGood = false;
            lastMove = "✗ " + e.withGap("" + selectedBall);
        }
        selectedEquation = -1;
        selectedBall = -1;
        shuffle();
    }

    // nowe działanie w miejsce rozwiązanego, po ostatniej liczbie już nie losujemy
    private void newEquation() {
        if (!isOver()) {
            equations[selectedEquation] = Expression.randomGap(Result.result(used));
        }
    }
}
