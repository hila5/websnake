package webSnake.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import webSnake.mapper.GameMapper;
import webSnake.mapper.PointEmbedMapper;
import webSnake.mapper.UserMapper;

@Configuration
public class MapperConfig {

    @Bean
    public PointEmbedMapper pointEmbedMapper() {
        return Mappers.getMapper(PointEmbedMapper.class);
    }

    @Bean
    public GameMapper gameMapper() {
        return Mappers.getMapper(GameMapper.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

}