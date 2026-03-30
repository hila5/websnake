package webSnake.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserServiceUtil {

    public static <T> Optional<T> findByFunctionList(String rawInput, List<Function<String, Optional<T>>> functions) {
        if (rawInput == null || rawInput.isEmpty()) {
            return Optional.empty();
        }

        String trimmedInput = rawInput.trim();

        for (Function<String, Optional<T>> function : functions) {
            Optional<T> result = function.apply(trimmedInput);

            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    public static boolean isLongParsable(String identifier) {
        try {
            Long.parseLong(identifier);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}