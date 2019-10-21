package com.github.riverxik.meowbot.modules;

import org.junit.Test;

public class SlotMachineUtilsTest {

    @Test
    public void testGetWinValue() throws Exception {
        int maxRolls = 1000;
        int bet = 5;
        int gain = 0;
        int numOfWins = 0;
        for (int i = 0; i < maxRolls; i++) {
            int[] roll = SlotMachineUtils.getRandomRoll();
            //System.out.println(SlotMachineUtils.rollToString(roll));
            int tmp = SlotMachineUtils.getWinValue(roll, bet);
            //System.out.println("Win: " + tmp);
            gain += tmp;
            if (tmp != 0) numOfWins++;
        }
        double percent = (Double.valueOf(numOfWins) / Double.valueOf(maxRolls)) * 100.0;
        int user = (gain - (bet*maxRolls));
        System.out.println("__________________");
        System.out.println("Money spend: " + bet*maxRolls);
        System.out.println("Money gain: " + gain);
        System.out.println("");
        System.out.println("User Money: " + user);
        System.out.println("Percent win: " + percent + "%");
    }
}