package vimification.internal.parser;

import java.util.logging.Logger;

import vimification.commons.core.LogsCenter;
import vimification.internal.command.Command;
import vimification.internal.command.logic.LogicCommand;
import vimification.internal.command.view.ViewCommand;
import vimification.model.MacroMap;

public class VimificationParser {

    private static final Logger LOGGER = LogsCenter.getLogger(VimificationParser.class);

    private static final ApplicativeParser<String> PREPROCESSOR = ApplicativeParser
            .string(":")
            .optional()
            .takeNext(ApplicativeParser.untilEof().map(String::strip));

    private static final CommandParser<LogicCommand> LOGIC_COMMAND_PARSER =
            AddCommandParser.getInstance()
                    .<LogicCommand>cast()
                    .or(DeleteCommandParser.getInstance())
                    .or(InsertCommandParser.getInstance())
                    .or(EditCommandParser.getInstance())
                    .or(UndoCommandParser.getInstance());

    private static final CommandParser<ViewCommand> VIEW_COMMAND_PARSER =
            SearchCommandParser.getInstance()
                    .<ViewCommand>cast()
                    .or(RefreshCommandParser.getInstance());

    private static final CommandParser<Command> COMMAND_PARSER =
            LOGIC_COMMAND_PARSER
                    .<Command>cast()
                    .or(VIEW_COMMAND_PARSER)
                    .updateInternalParser(parser -> parser.throwIfFail("Unknown command"));

    private MacroMap macroMap;

    private final ApplicativeParser<String> macroPreprocessor =
            PREPROCESSOR.map(input -> macroMap.get(input).orElse(input));

    private VimificationParser(MacroMap macroMap) {
        this.macroMap = macroMap;
    }

    public static VimificationParser getInstance(MacroMap macroMap) {
        if (macroMap == null) {
            macroMap = new MacroMap();
        }
        return new VimificationParser(macroMap);
    }

    /**
     * Parses the user input and return its corresponding command.
     *
     * @param userInput
     * @return
     */
    public Command parse(String input) {
        LOGGER.info(input);
        String preprocessedInput = macroPreprocessor.parse(input);
        return COMMAND_PARSER.parse(preprocessedInput);
    }
}
