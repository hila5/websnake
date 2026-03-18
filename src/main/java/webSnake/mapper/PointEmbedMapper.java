package webSnake.mapper;

import org.mapstruct.Mapper;
import webSnake.entity.PointEmbed;

import java.awt.*;

@Mapper()
public interface PointEmbedMapper {

    default PointEmbed map(Point point) {
        return point == null ? null : new PointEmbed(point);
    }

    default Point map(PointEmbed pointEmbed) {
        return pointEmbed == null ? null : pointEmbed.toPoint();
    }

}