package khuong.com.smartorder_domain2.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${kitchen.queue.orders}")
    private String ordersQueue;

    @Value("${kitchen.exchange.orders}")
    private String ordersExchange;

    @Value("${kitchen.routing-key.orders}")
    private String ordersRoutingKey;

    @Bean
    Queue ordersQueue() {
        return new Queue(ordersQueue, true);
    }

    @Bean
    DirectExchange ordersExchange() {
        return new DirectExchange(ordersExchange);
    }

    @Bean
    Binding orderBinding() {
        return BindingBuilder.bind(ordersQueue())
                .to(ordersExchange())
                .with(ordersRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}