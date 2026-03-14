package webSnake.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import webSnake.entity.PointEmbed;

import java.awt.*;

@Mapper(componentModel = "spring")
public interface PointEmbedMapper {

    default PointEmbed map(Point point) {
        return point == null ? null : new PointEmbed(point);
    }

    default Point map(PointEmbed pointEmbed) {
        return pointEmbed == null ? null : pointEmbed.toPoint();
    }

    default void updateFromPoint(Point source, @MappingTarget PointEmbed target) {
        if (target == null) {
            target = new PointEmbed(source);
        } else {
            target.set(source);
        }
    }

}