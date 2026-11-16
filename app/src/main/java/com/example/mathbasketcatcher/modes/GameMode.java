package com.example.mathbasketcatcher.modes;

import java.util.Random;

public abstract class GameMode {

    public boolean[] used = new boolean[10];
    public int[] order = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public int score = 0;
    public int errors = 0;
    public String lastMove = "";
    public boolean lastGood = true;

    private Random random = new Random();

    public abstract void choose(int number);

    public abstract void alreadyUsed();

    public boolean isOver() {
        for (int i = 0; i < 10; i++) {
            if (!used[i]) {
                return false;
            }
        }
        return true;
    }

    // tasowanie kulek aby nie dało się zapamiętać ich po położeniu
    protected void shuffle() {
        for (int i = 9; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = order[i];
            order[i] = order[j];
            order[j] = temp;
        }
    }
}
