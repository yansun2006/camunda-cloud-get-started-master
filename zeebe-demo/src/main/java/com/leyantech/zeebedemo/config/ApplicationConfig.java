package com.leyantech.zeebedemo.config;

import com.leyantech.zeebedemo.common.base.LocalDateTimeDeserializer;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author yan.sun, yan.sun@leyantech.com
 * @date 2020/1/9.
 */
@Configuration
//@EnableTransactionManagement(proxyTargetClass = true)
public class ApplicationConfig implements ApplicationContextAware {
  private static final Logger LOGGER = LogManager.getLogger(ApplicationConfig.class);

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ApplicationConfig.applicationContext = applicationContext;
  }

  /**
   * LocalDateTime 序列化器
   */
  @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
  private String pattern;
  @Bean
  public LocalDateTimeSerializer localDateTimeSerializer() {
    return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * 配置 LocalDateTime 序列化器 和 反序列化器
   * @return
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
      builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
    };
  }

  @Bean(name = "jobWorkerThreadPool")
  public ExecutorService getPoolExecutor() {
    return Executors.newCachedThreadPool();
  }

//  @Bean
//  public FilterRegistrationBean loginFilterRegistration() {
//    FilterRegistrationBean registration = new FilterRegistrationBean();
//    registration.setFilter(new LoginFilter());
//    // 不能以412开头
//    registration.addUrlPatterns("/*");
//    registration.setName("loginFilter");
//    registration.setOrder(1);
//    return registration;
//  }

//  @Bean
//  public OssConfig ossConfig() {
//    JSONObject json = JSON.parseObject(ApolloConfig.getInstance().getFileOSSConfig());
//    OssConfig ossConfig = new OssConfig();
//    ossConfig.setAccessKeyId(json.getString("accessKeyId"));
//    ossConfig.setAccessKeySecret(json.getString("accessKeySecret"));
//    ossConfig.setBucketName(json.getString("bucketName"));
//    ossConfig.setEndpoint(json.getString("endpoint"));
//    ossConfig.setCredentialsProvider(new DefaultCredentialProvider(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret()));
//    return ossConfig;
//  }
//
//  @Bean
//  public OSSClient ossclient() {
//    OssConfig ossConfig = ossConfig();
//    ClientConfiguration clientConfiguration = new ClientConfiguration();
//    return new OSSClient(ossConfig.getEndpoint(), ossConfig.getCredentialsProvider(), clientConfiguration);
//  }


//  /**
//   * 【redis】connection
//   * @return
//   */
//  @Bean
//  public LettuceConnectionFactory redisConnectionFactory() {
//    Config apolloConfig = ApolloConfig.getConfig();
//
//    String host = apolloConfig.getProperty("redis.host", "127.0.0.1");
//    Integer port = apolloConfig.getIntProperty("redis.port", 6379);
//    String password = apolloConfig.getProperty("redis.pwd", null);
//
//    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//    config.setHostName(host);
//    config.setPort(port);
//    config.setPassword(password);
//
//    return new LettuceConnectionFactory(config);
//  }
//
//  /**
//   * 【redis】其他人不应该使用redisTemplate，应该使用 RedisService 类
//   * @param redisConnectionFactory
//   * @return
//   */
//  @Bean(name = "redisTemplate_Private")
//  public RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//    RedisTemplate redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//    RedisSerializer<String> keyRedisSerializer = RedisSerializer.string();
//    RedisSerializer<Object> valueRedisSerializer = RedisSerializer.java();//RedisSerializer.json();
//
//    redisTemplate.setKeySerializer(keyRedisSerializer);
//    redisTemplate.setHashKeySerializer(keyRedisSerializer);
//    redisTemplate.setStringSerializer(keyRedisSerializer);
//
//    redisTemplate.setValueSerializer(valueRedisSerializer);
//    redisTemplate.setHashValueSerializer(valueRedisSerializer);
//    redisTemplate.setDefaultSerializer(valueRedisSerializer);
//
//    redisTemplate.afterPropertiesSet();
//    return redisTemplate;
//  }
}
