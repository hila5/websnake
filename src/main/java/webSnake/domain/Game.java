package webSnake.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import webSnake.exception.SnakeOutOfBoundsException;

import java.awt.*;

@Getter
@Setter
@Log4j2
public class Game {

    private int rowSize;
    private int colSize;
    private Point appleLocation;
    private Point snakeLocation;
    private int score;

    public Game(int rows, int cols) {
        log.info("Game constructor started. rows:{}, cols:{} ", rows, cols);

        this.score = 0;
        this.rowSize = rows;
        this.colSize = cols;
        snakeLocation = getRandomPoint();
        setRandomAppleLocation();

        log.info("Game constructor ended. rows:{}, cols:{} ", rows, cols);
    }

    public Game(int rowSize, int colsSize, Point snakeLocation, Point appleLocation, int score) {
        log.info("Game constructor started.");

        this.score = score;
        this.rowSize = rowSize;
        this.colSize = colsSize;
        this.snakeLocation = snakeLocation;
        this.appleLocation = appleLocation;

        log.info("Game constructor ended.");
    }

    private void setRandomAppleLocation() {
        do {
            appleLocation = getRandomPoint();
        } while (snakeLocation.equals(appleLocation));
    }

    public Point getRandomPoint() {
        log.info("getRandomPoint started.");

        int randRow = (int) (Math.random() * rowSize);
        int randCol = (int) (Math.random() * colSize);

        log.info("getRandomPoint ended.");

        return new Point(randRow, randCol);
    }


    public void moveTheSnake(String direction, int newX, int newY) throws SnakeOutOfBoundsException {
        log.info("moveTheSnake started. direction:{}, newX:{}, newY:{}", direction, newX, newY);

        Point newSnakeLocation = new Point(newX, newY);

        if (isLegalMove(newSnakeLocation)) {
            snakeLocation = newSnakeLocation;

            if (snakeLocation.equals(appleLocation)) {
                eatApple();
            }
        } else {
            throw new SnakeOutOfBoundsException("can't move " + direction);
        }

        log.info("moveTheSnake ended. newSnakeLocation:{}, direction:{}", newSnakeLocation, direction);
    }

    private boolean isLegalMove(Point newSnakeLocation) {
        log.debug("isLegalMove. newSnakeLocation:{}", newSnakeLocation);

        return newSnakeLocation.x < rowSize && newSnakeLocation.x >= 0 && newSnakeLocation.y < colSize && newSnakeLocation.y >= 0;
    }

    private void eatApple() {
        log.debug("eatApple started.");

        score++;
        setRandomAppleLocation();

        log.debug("eatApple ended. score:{}", score);
    }

    @Override
    public boolean equals(Object o) {
        log.debug("run equals.");

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;

        return game.score == this.score &&
                game.appleLocation.equals(this.appleLocation) &&
                game.snakeLocation.equals(this.snakeLocation) &&
                game.getRowSize() == this.rowSize &&
                game.getColSize() == this.colSize;
    }

}