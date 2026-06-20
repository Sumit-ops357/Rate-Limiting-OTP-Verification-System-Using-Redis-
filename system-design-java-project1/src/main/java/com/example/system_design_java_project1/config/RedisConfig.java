package com.example.system_design_java_project1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

//    @Bean
//   public RedisConnectionFactory redisConnectionFactory()
//   {
//       return new LettuceConnectionFactory();
//   }

   @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<String, String> template= new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

       StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

       //This tells redis template that before storing key ,use stringRedisSerializer

       template.setKeySerializer(stringRedisSerializer);
       template.setValueSerializer(stringRedisSerializer);
       template.setHashKeySerializer(stringRedisSerializer);
       template.setHashValueSerializer(stringRedisSerializer);

       //This tells spring that the template is ready and u can initialize it
       template.afterPropertiesSet();
       return template;
   }
}
