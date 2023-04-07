package vimification.internal.parser;

import vimification.internal.command.ui.RefreshCommand;

public class RefreshCommandParser implements CommandParser<RefreshCommand> {

    private static final ApplicativeParser<ApplicativeParser<RefreshCommand>> INTERNAL_PARSER =
            ApplicativeParser
                    .string("refresh")
                    .constMap(ApplicativeParser.of(new RefreshCommand()));

    private static final RefreshCommandParser INSTANCE = new RefreshCommandParser();

    public static RefreshCommandParser getInstance() {
        return INSTANCE;
    }

    @Override
    public ApplicativeParser<ApplicativeParser<RefreshCommand>> getInternalParser() {
        return INTERNAL_PARSER;
    }
}
