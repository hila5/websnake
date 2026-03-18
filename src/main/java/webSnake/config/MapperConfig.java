package webSnake.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import webSnake.mapper.GameMapper;
import webSnake.mapper.PointEmbedMapper;

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

}