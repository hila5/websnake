package webSnake.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserUT {

    @Test
    void constructor_setsUsername() {
        // Act
        User user = new User("hila");

        // Assert
        assertEquals("hila", user.getUsername());
    }

    @Test
    void constructor_setsCreationTime_notNull() {
        // Act
        User user = new User("hila");

        // Assert
        assertNotNull(user.getCreationTime());
    }

}