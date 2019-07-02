package com.github.riverxik;

import com.github.riverxik.meowbot.Bot;
import com.github.riverxik.meowbot.commands.Lexer;
import com.github.riverxik.meowbot.commands.Parser;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        //new Bot();
        while(true) {
            //Lexer lexer = new Lexer("!hi irinafox 12 1.3");
            Lexer lexer = new Lexer(new Scanner(System.in).nextLine());
            lexer.tokenize();
            //lexer.showAllTokens();
            Parser parser = new Parser(lexer.getTokenList());
            parser.start(false);
            System.out.println(parser.stackValues.get(0));
        }
    }
}
