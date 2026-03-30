package webSnake.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserServiceUtilUT {

    @Nested
    class FindByFunctionList {
        @Test
        void findByFunctionList_nullInput_returnsEmpty() {
            //Arrange
            //Act
            Optional<String> result = UserServiceUtil.findByFunctionList(null, List.of());

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        void findByFunctionList_emptyInput_returnsEmpty() {
            //Arrange
            //Act
            Optional<String> result = UserServiceUtil.findByFunctionList("", List.of());

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        void findByFunctionList_trimsInput() {
            //Arrange
            String raw = "  hilaa  ";
            String trimmed = "hilaa";
            Function<String, Optional<String>> function = input -> input.equals(trimmed) ? Optional.of(trimmed) : Optional.empty();

            //Act
            Optional<String> result = UserServiceUtil.findByFunctionList(raw, List.of(function));

            //Assert
            assertTrue(result.isPresent());
            assertEquals(trimmed, result.get());
        }

        @Test
        void findByFunctionList_firstMatchWins() {
            //Arrange
            String first = "1";
            String second = "2";
            String justString = "abc";
            Function<String, Optional<String>> func1 = input -> Optional.of(first);
            Function<String, Optional<String>> func2 = input -> Optional.of(second);

            //Act
            Optional<String> result = UserServiceUtil.findByFunctionList(justString, List.of(func1, func2));

            //Assert
            assertEquals(first, result.get());
        }

        @Test
        void findByFunctionList_secondFunctionMatches() {
            //Arrange
            String second = "2";
            String justString = "abc";
            Function<String, Optional<String>> func1 = input -> Optional.empty();
            Function<String, Optional<String>> func2 = input -> Optional.of(second);

            //Act
            Optional<String> result =
                    UserServiceUtil.findByFunctionList(justString, List.of(func1, func2));

            //Assert
            assertEquals(second, result.get());
        }

        @Test
        void findByFunctionList_noMatch_returnsEmpty() {
            //Arrange
            String justString = "abc";
            Function<String, Optional<String>> func1 = input -> Optional.empty();
            Function<String, Optional<String>> func2 = input -> Optional.empty();

            //Act
            Optional<String> result = UserServiceUtil.findByFunctionList(justString, List.of(func1, func2));

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        void findByFunctionList_emptyFunctionList_returnsEmpty() {
            //Arrange
            String justString = "abc";

            //Act
            Optional<String> result = UserServiceUtil.findByFunctionList(justString, List.of());

            //Assert
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class IsLongParsable {

        @Test
        void isLongParsable_validNumber_returnsTrue() {
            assertTrue(UserServiceUtil.isLongParsable("123"));
        }

        @Test
        void isLongParsable_negativeNumber_returnsTrue() {
            assertTrue(UserServiceUtil.isLongParsable("-123"));
        }

        @Test
        void isLongParsable_largeNumber_returnsTrue() {
            assertTrue(UserServiceUtil.isLongParsable("9223372036854775807")); // Long.MAX_VALUE
        }

        @Test
        void isLongParsable_invalidString_returnsFalse() {
            assertFalse(UserServiceUtil.isLongParsable("abc"));
        }

        @Test
        void isLongParsable_decimal_returnsFalse() {
            assertFalse(UserServiceUtil.isLongParsable("12.5"));
        }

        @Test
        void isLongParsable_emptyString_returnsFalse() {
            assertFalse(UserServiceUtil.isLongParsable(""));
        }

        @Test
        void isLongParsable_null_throwsException() {
            assertFalse(UserServiceUtil.isLongParsable(null));
        }

    }

}