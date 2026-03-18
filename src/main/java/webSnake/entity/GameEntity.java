package webSnake.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "GAMES")
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "GameOwner",
        attributeNodes = @NamedAttributeNode("owner"))
public class GameEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_seq")
    @SequenceGenerator(
            name = "game_seq",
            sequenceName = "GAME_SEQ",
            allocationSize = 1)
    @Column(name = "GAME_ID", nullable = false, updatable = false)
    private Long gameId; // עילה, חעלד, שעי, נעתן, עדם, עמית

    @Column(name = "ROW_SIZE")
    private int rowSize;

    @Column(name = "COL_SIZE")
    private int colSize;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "SNAKE_X")),
            @AttributeOverride(name = "y", column = @Column(name = "SNAKE_Y"))
    })
    private PointEmbed snakeLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "APPLE_X")),
            @AttributeOverride(name = "y", column = @Column(name = "APPLE_Y"))
    })
    private PointEmbed appleLocation;

    @Column(name = "SCORE")
    private int score;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false,
            foreignKey = @ForeignKey(name = "fk_game_user"))
    @JsonBackReference
    private UserEntity owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        if (gameId != null ? !gameId.equals(that.gameId) : that.gameId != null) return false;
        if (colSize != that.colSize) return false;
        if (rowSize != that.rowSize) return false;
        if (snakeLocation != null ? !snakeLocation.toPoint().equals(that.snakeLocation.toPoint()) : that.snakeLocation != null)
            return false;
        if (appleLocation != null ? !appleLocation.toPoint().equals(that.appleLocation.toPoint()) : that.appleLocation != null)
            return false;
        return score == that.score;
    }

}