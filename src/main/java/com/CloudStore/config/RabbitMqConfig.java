package com.CloudStore.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Bean
    public ConnectionFactory connectionRabbitFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionRabbitFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionRabbitFactory());
    }


    @Bean
    public Queue fileQueue() {
        return new Queue("file-queue");
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("file-exchange");
    }

    @Bean
    public Binding deleteFolderBinding(){
        return BindingBuilder.bind(fileQueue()).to(topicExchange()).with("file.delete.folder");
    }

    @Bean
    public Binding deleteFileBinding(){
        return BindingBuilder.bind(fileQueue()).to(topicExchange()).with("file.delete.file");
    }

    @Bean
    public Binding uploadBinding(){
        return BindingBuilder.bind(fileQueue()).to(topicExchange()).with("file.upload");
    }

    @Bean
    public Binding renameBinding(){
        return BindingBuilder.bind(fileQueue()).to(topicExchange()).with("file.rename");
    }

}
