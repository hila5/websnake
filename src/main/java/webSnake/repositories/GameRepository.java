package webSnake.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webSnake.service.UserScoreRank;
import webSnake.entity.GameEntity;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    List<GameEntity> findByOwnerUserId(Long userId);

    @Query("SELECT SUM(g.score) FROM GameEntity g WHERE g.owner.userId = :userId")
    Long sumScoresByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT t.userId," +
            " t.totalScore," +
            " t.userRank " +
            "FROM (" +
            "        SELECT u.USER_ID           AS userId," +
            "        NVL(SUM(g.SCORE),0) AS totalScore," +
            "DENSE_RANK() OVER (ORDER BY NVL(SUM(g.SCORE),0) DESC) AS userRank " +
            "FROM USERS u " +
            "LEFT JOIN GAMES g ON g.USER_ID = u.USER_ID " +
            "GROUP BY u.USER_ID " +
            ") t " +
            "WHERE t.userId = :userId ", nativeQuery = true)
    UserScoreRank findRankForUser(@Param("userId") Long userId);

}