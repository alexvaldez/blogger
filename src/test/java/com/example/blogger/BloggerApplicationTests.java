package com.example.blogger;

import com.example.blogger.model.BlogPost;
import com.example.blogger.model.BlogPostRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnableAutoConfiguration
@AutoConfigureWebTestClient
@EnableMongoRepositories(("com.example.blogger.model"))
class BloggerApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(BloggerApplicationTests.class);

    @Autowired
    BlogPostRepository blogPostRepository;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testGetAll() {
        webTestClient.get()
                     .uri("/blogPosts")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectBody()
                     .json("[]");
    }
}
