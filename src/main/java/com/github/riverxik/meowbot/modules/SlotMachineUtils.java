package com.github.riverxik.meowbot.modules;

public class SlotMachineUtils {
    /**
     * Kappa Kappa _                - x5
     * Kappa Kappa Kappa            - x10
     * PogChamp PogChamp _          - x4
     * PogChamp PogChamp PogChamp   - x8
     * 4Head 4Head _                - x3
     * 4Head 4Head 4Head            - x6
     * <3 <3 _                      - x2
     * <3 <3 <3                     - x4
     * CSB CSB CSB                  - x1
     * */
    private static final int sum = 1;
    private static final String[] emotes = {"Kappa", "PogChamp", "4Head", "<3", "CoolStoryBob"};

    public static int getWinValue(int[] roll, int bet) {
        int first = roll[0];
        int second = roll[1];
        int third = roll[2];

        if ((first == second) && (first == third)) {
            // x x x
            return 2 * first * bet;
        } else if ((first == second)) {
            // x x y
            return first * bet;
        } else if (second == third) {
            // y x x
            return second * bet;
        } else {
            return 0;
        }
    }

    public static int[] getRandomRoll() {
        int first = ((int) (Math.random() * 4) + sum);
        int second = ((int) (Math.random() * 4) + sum);
        int third = ((int) (Math.random() * 4) + sum);
        return new int[] {first, second, third};
    }

    public static String rollToString(int[] roll) {
        return String.format("|%d|%d|%d|", roll[0], roll[1], roll[2]);
    }

    public static String rollToSmileString(int[] roll) {
        return String.format("%s | %s | %s", emotes[roll[0]], emotes[roll[1]], emotes[roll[2]]);
    }
}
