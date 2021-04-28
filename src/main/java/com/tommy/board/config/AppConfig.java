package com.tommy.board.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;

@Configuration
@EnableJpaRepositories(basePackages = "com.tommy.board.repository")
public class AppConfig {
    @PersistenceContext
    private EntityManager entityManager;
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .withConnectTimeout(Duration.ofSeconds(3))
                .withSocketTimeout(Duration.ofSeconds(3))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
