package webSnake.domain;

import lombok.extern.log4j.Log4j2;
import webSnake.exception.SnakeOutOfBoundsException;

@Log4j2
public enum MoveSnakeEnum {

    UP {
        @Override
        public void move(Game game) throws SnakeOutOfBoundsException {
            log.debug("move started. direction:{}", this.name());

            game.moveTheSnake(this.name(), game.getSnakeLocation().x - STEP_SIZE, game.getSnakeLocation().y);

            log.debug("move ended. direction:{}", this.name());
        }
    },
    DOWN {
        @Override
        public void move(Game game) throws SnakeOutOfBoundsException {
            log.debug("move started. direction:{}", this.name());

            game.moveTheSnake(this.name(), game.getSnakeLocation().x + STEP_SIZE, game.getSnakeLocation().y);

            log.debug("move ended. direction:{}", this.name());
        }
    },
    LEFT {
        @Override
        public void move(Game game) throws SnakeOutOfBoundsException {
            log.debug("move started. direction:{}", this.name());

            game.moveTheSnake(this.name(), game.getSnakeLocation().x, game.getSnakeLocation().y - STEP_SIZE);

            log.debug("move ended. direction:{}", this.name());
        }
    },
    RIGHT {
        @Override
        public void move(Game game) throws SnakeOutOfBoundsException {
            log.debug("move started. direction:{}", this.name());

            game.moveTheSnake(this.name(), game.getSnakeLocation().x, game.getSnakeLocation().y + STEP_SIZE);

            log.debug("move ended. direction:{}", this.name());
        }
    };

    public abstract void move(Game game) throws SnakeOutOfBoundsException;

    final int STEP_SIZE = 1;

}