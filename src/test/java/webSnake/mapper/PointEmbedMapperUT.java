package webSnake.mapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import webSnake.entity.PointEmbed;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class PointEmbedMapperUT {

    private final PointEmbedMapper mapper = Mappers.getMapper(PointEmbedMapper.class);

    @Nested
    class MapPointToPointEmbed {

        @Test
        void map_success() {
            // Arrange
            Point point = new Point(1, 2);

            // Act
            PointEmbed result = mapper.map(point);

            // Assert
            assertNotNull(result);
            assertEquals(point.x, result.getX());
            assertEquals(point.y, result.getY());
        }

        @Test
        void map_nullPoint_returnsNull() {
            // Act
            PointEmbed result = mapper.map((Point) null);

            // Assert
            assertNull(result);
        }

    }

    @Nested
    class MapPointEmbedToPoint {

        @Test
        void map_success() {
            // Arrange
            PointEmbed pointEmbed = new PointEmbed(new Point(1, 2));

            // Act
            Point result = mapper.map(pointEmbed);

            // Assert
            assertNotNull(result);
            assertEquals(pointEmbed.getX(), result.x);
            assertEquals(pointEmbed.getY(), result.y);
        }

        @Test
        void map_nullPointEmbed_returnsNull() {
            // Act
            Point result = mapper.map((PointEmbed) null);

            // Assert
            assertNull(result);
        }

    }

}