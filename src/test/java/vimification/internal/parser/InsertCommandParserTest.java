package vimification.internal.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import vimification.internal.command.logic.InsertCommand;

public class InsertCommandParserTest {

    private static final Class<ParserException> EXPECTED_EXCEPTION_CLASS = ParserException.class;
    private static final InsertCommandParser INSTANCE = InsertCommandParser.getInstance();

    @Test
    public void urecognizaleArgs_shouldThrow() {
        String input = "i \"description\" -t stickytag -p 0";
        assertThrows(EXPECTED_EXCEPTION_CLASS, () -> INSTANCE.parse(input));

        String input1 = "i 10 -p 0";
        assertThrows(EXPECTED_EXCEPTION_CLASS, () -> INSTANCE.parse(input1));
    }

    @Test
    public void withArgs_shouldSuccess() {
        String input = "i 2 -l \"cry\" -l \"now\"";
        InsertCommand cmd = INSTANCE.parse(input);
        assertTrue(cmd != null);

        String input1 = "i 10 -d <deadline> -l haskell --label proglang";
        InsertCommand cmd1 = INSTANCE.parse(input1);
        assertTrue(cmd1 != null);

        String input2 = "i 10 --deadline <deadline>";
        InsertCommand cmd2 = INSTANCE.parse(input2);
        assertTrue(cmd2 != null);
    }

    @Test
    public void noArg_shouldThrow() {
        String input = "i 10";
        assertThrows(EXPECTED_EXCEPTION_CLASS, () -> INSTANCE.parse(input));
    }
}
