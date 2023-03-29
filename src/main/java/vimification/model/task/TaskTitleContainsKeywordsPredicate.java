package vimification.model.task;

import java.util.List;
import java.util.function.Predicate;

import vimification.commons.util.StringUtil;

/**
 * Tests that a {@code Task}'s {@code Description} matches any of the keywords given.
 */
public class TaskTitleContainsKeywordsPredicate implements Predicate<Task> {
    private final List<String> keywords;

    public TaskTitleContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Task task) {
        String title = task.getTitle();
        return keywords
                .stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(title, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskTitleContainsKeywordsPredicate
                        // nulls
                        && keywords.equals(
                                ((TaskTitleContainsKeywordsPredicate) other).keywords)); // state
        // check
    }

}
