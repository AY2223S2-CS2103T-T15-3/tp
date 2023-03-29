package vimification.internal.parser;

import vimification.internal.command.logic.AddCommand;
import vimification.model.task.Task;

public class AddCommandParser implements CommandParser<AddCommand> {

    private static final ApplicativeParser<AddCommand> COMMAND_PARSER =
            CommandParserUtil.STRING_PARSER
                    .map(Task::new)
                    .flatMap(AddCommandParser::parseArguments)
                    .dropNext(ApplicativeParser.skipWhitespaces())
                    .dropNext(ApplicativeParser.eof());

    private static final ApplicativeParser<ApplicativeParser<AddCommand>> INTERNAL_PARSER =
            ApplicativeParser
                    .string("a")
                    .takeNext(ApplicativeParser.skipWhitespaces1())
                    .constMap(COMMAND_PARSER);

    private static final AddCommandParser INSTANCE = new AddCommandParser();

    private AddCommandParser() {}

    private static ApplicativeParser<AddCommand> parseArguments(Task task) {
        ArgumentCounter counter = new ArgumentCounter(
                CommandParserUtil.LABEL_FLAG.withMaxCount(Integer.MAX_VALUE),
                CommandParserUtil.PRIORITY_FLAG,
                CommandParserUtil.DEADLINE_FLAG);

        ApplicativeParser<Void> flagParser = ApplicativeParser.choice(
                CommandParserUtil.LABEL_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.LABEL_PARSER)
                        .consume(task::addLabel),
                CommandParserUtil.PRIORITY_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.PRIORITY_PARSER)
                        .consume(task::setPriority),
                CommandParserUtil.DEADLINE_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.DEADLINE_PARSER)
                        .consume(task::setDeadline));

        return ApplicativeParser
                .skipWhitespaces1()
                .takeNext(flagParser.sepBy1(ApplicativeParser.skipWhitespaces1()))
                .optional()
                .constMap(new AddCommand(task));
    }

    public static AddCommandParser getInstance() {
        return INSTANCE;
    }

    @Override
    public ApplicativeParser<ApplicativeParser<AddCommand>> getInternalParser() {
        return INTERNAL_PARSER;
    }
}
