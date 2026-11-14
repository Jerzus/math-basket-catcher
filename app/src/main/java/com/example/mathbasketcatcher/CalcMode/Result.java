package com.example.mathbasketcatcher;

import java.util.Random;

public class Result {

    private static final Random random = new Random();

    // losowy wynik 0-9, used to liczby już kliknięte
    public static int result(boolean[] used) {
        int free = 0;
        for (int i = 0; i < 10; i++) {
            if (!used[i]) {
                free++;
            }
        }
        // szansa na wolną liczbę: 100% przy 10 wolnych, 70% kiedy została jedna (poziom łatwy) TODO: dodać poziomy trudności
        double chance = 0.7 + 0.3 * (free - 1) / 9.0;
        boolean wantFree = random.nextDouble() < chance;
        int result = random.nextInt(10);
        while (used[result] == wantFree) {
            result = random.nextInt(10);
        }
        return result;
    }
}
