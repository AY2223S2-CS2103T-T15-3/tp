package vimification.internal.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;


/**
 * Stores mapping of prefixes to their respective arguments. Each key may be associated with
 * multiple argument values. Values for a given key are stored in a list, and the insertion ordering
 * is maintained. Keys are unique, but the list of argument values may contain duplicate argument
 * values, i.e. the same argument value can be inserted multiple times for the same prefix.
 */
public class ArgumentMultimap {

    private final Set<ArgumentFlag> allowedFlags;

    /**
     * Flags mapped to their respective arguments
     **/
    private final Map<ArgumentFlag, Set<String>> args;

    public ArgumentMultimap(ArgumentFlag... allowedFlags) {
        this.allowedFlags = Set.of(allowedFlags);
        this.args = new HashMap<>();
    }

    private void throwIfNotAllowed(ArgumentFlag flag) {
        if (!allowedFlags.contains(flag)) {
            throw new ParserException("Invalid flag for this command");
        }
    }


    /**
     * Associates the specified argument value with {@code prefix} key in this map. If the map
     * previously contained a mapping for the key, the new value is appended to the list of existing
     * values.
     *
     * @param prefix Prefix key with which the specified argument value is to be associated
     * @param argValue Argument value to be associated with the specified prefix key
     */
    public void put(ArgumentFlag flag, String value) {
        throwIfNotAllowed(flag);
        Set<String> argValues = args.computeIfAbsent(flag, k -> new HashSet<>());
        if (!argValues.add(value)) {
            throw new ParserException("Duplicated argument");
        }
        if (argValues.size() > flag.getMaxCount()) {
            throw new ParserException("Number of argument exceeded limit");
        }
    }

    public Set<String> get(ArgumentFlag flag) {
        throwIfNotAllowed(flag);
        Set<String> result = args.get(flag);
        return result == null ? Set.of() : result;
    }

    public Set<String> remove(ArgumentFlag flag) {
        throwIfNotAllowed(flag);
        Set<String> result = args.remove(flag);
        return result == null ? Set.of() : result;
    }

    public Optional<String> getFirst(ArgumentFlag flag) {
        throwIfNotAllowed(flag);
        return args.getOrDefault(flag, Set.of())
                .stream()
                .findFirst();
    }
}
