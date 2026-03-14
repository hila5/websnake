package webSnake.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "USER_SEQ",
            allocationSize = 1)
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CREATION_TIME")
    private String creationTime;

    @Column(name = "LAST_PLAYED")
    private String lastPlayed;

    @Column(name = "CURRENT_GAME_ID")
    private Long currentGameId;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<GameEntity> games = new HashSet<>();

}