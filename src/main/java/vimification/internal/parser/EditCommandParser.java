package vimification.internal.parser;

import vimification.commons.core.Index;
import vimification.internal.command.logic.EditCommand;
import vimification.internal.command.logic.EditRequest;

public class EditCommandParser implements CommandParser<EditCommand> {

    private static final ApplicativeParser<EditCommand> COMMAND_PARSER =
            CommandParserUtil.ONE_BASED_INDEX_PARSER
                    .flatMap(EditCommandParser::parseArguments)
                    .dropNext(ApplicativeParser.skipWhitespaces())
                    .dropNext(ApplicativeParser.eof());;

    private static final ApplicativeParser<ApplicativeParser<EditCommand>> INTERNAL_PARSER =
            ApplicativeParser
                    .string("e")
                    .takeNext(ApplicativeParser.skipWhitespaces1())
                    .constMap(COMMAND_PARSER);

    private static final EditCommandParser INSTANCE = new EditCommandParser();

    private EditCommandParser() {}

    private static ApplicativeParser<EditCommand> parseArguments(Index index) {
        EditRequest request = new EditRequest();
        ArgumentCounter counter = new ArgumentCounter(
                CommandParserUtil.TITLE_FLAG,
                CommandParserUtil.LABEL_FLAG.withMaxCount(Integer.MAX_VALUE),
                CommandParserUtil.STATUS_FLAG,
                CommandParserUtil.PRIORITY_FLAG,
                CommandParserUtil.DEADLINE_FLAG);

        ApplicativeParser<Void> flagParser = ApplicativeParser.choice(
                CommandParserUtil.TITLE_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.TITLE_PARSER)
                        .consume(request::setEditedTitle),
                CommandParserUtil.LABEL_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.LABEL_PARSER)
                        .dropNext(ApplicativeParser.skipWhitespaces1())
                        .combine(CommandParserUtil.LABEL_PARSER, Pair::of)
                        .consume(pair -> request.getEditedLabels().add(pair)),
                CommandParserUtil.STATUS_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.STATUS_PARSER)
                        .consume(request::setEditedStatus),
                CommandParserUtil.PRIORITY_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.PRIORITY_PARSER)
                        .consume(request::setEditedPriority),
                CommandParserUtil.DEADLINE_FLAG_PARSER
                        .consume(counter::add)
                        .takeNext(ApplicativeParser.skipWhitespaces1())
                        .takeNext(CommandParserUtil.DEADLINE_PARSER)
                        .consume(request::setEditedDeadline));

        return ApplicativeParser
                .skipWhitespaces1()
                .takeNext(flagParser) // must have at least 1 flag
                .constMap(new EditCommand(index, request));
    }

    public static EditCommandParser getInstance() {
        return INSTANCE;
    }

    @Override
    public ApplicativeParser<ApplicativeParser<EditCommand>> getInternalParser() {
        return INTERNAL_PARSER;
    }

}
