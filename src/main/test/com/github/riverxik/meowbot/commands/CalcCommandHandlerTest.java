package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.commands.fsa.Lexer;
import com.github.riverxik.meowbot.commands.fsa.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CalcCommandHandlerTest {

    @Test
    public void testExecute() throws Exception {
        String message = "!calc (2 + 2)";
        Object[] commandParts = parseCommand(message);
        assert(commandParts[1] != null);
        assertEquals(4, commandParts[1]);
    }

    private static Object[] parseCommand(String message) {
        try {
            Lexer lexer = new Lexer(message);
            lexer.tokenize(false);
            Parser parser = new Parser(lexer.getTokenList());
            parser.start(false);

            int sizeOfStackValues = parser.stackValues.size();

            Object[] params = new Object[sizeOfStackValues];
            for (int i = 0; i < sizeOfStackValues; i++) {
                params[i] = parser.stackValues.get(i);
            }
            return params;
        } catch (RuntimeException e) {
            return new Object[] {"error", e.getMessage()};
        }
    }
}