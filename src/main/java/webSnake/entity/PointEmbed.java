package webSnake.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.awt.*;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PointEmbed implements Serializable {

    @Column(name = "X", nullable = false)
    private int x;

    @Column(name = "Y", nullable = false)
    private int y;

    public PointEmbed(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point toPoint() {
        return new Point(x, y);
    }

    public void set(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

}