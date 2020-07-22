package com.example.blogger;

import com.example.blogger.model.BlogPost;
import com.example.blogger.model.BlogPostRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
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
import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnableAutoConfiguration
@AutoConfigureWebTestClient
@EnableMongoRepositories(("com.example.blogger.model"))
class BloggerApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(BloggerApplicationTests.class);

    private BlogPost post1, post2, post3;
    @Autowired
    BlogPostRepository blogPostRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        blogPostRepository.deleteAll().block();
        post1 = new BlogPost(null, "joebloggs", "My first post", "Look at me, I can blog!", new Date(), null);
        post2 = new BlogPost(null, "joebloggs", "My second post", "Now we're cooking!", new Date(), null);
        post3 = new BlogPost(null, "drumpf", "Covfefe", "Tweet tweet", new Date(), null);
        blogPostRepository.saveAll(Flux.just(post1, post2, post3)).blockLast();
    }

    @Test
    void testGetAll() {
        byte[] response = webTestClient.get()
                .uri("/blogPosts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .returnResult()
                .getResponseBody();

        logger.info(new String(response));
    }

    @Test
    void testPostOne() {
        String post = "{\"user\":\"devincow\"," +
                "\"title\":\"How now, brown cow\"," +
                "\"content\":\"Mr. Brown can moo, can you?\"," +
                "\"date\":\"2020-07-22\"}";

        byte[] response = webTestClient.post()
                .uri("/blogPosts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .returnResult()
                .getResponseBody();

        logger.info(new String(response));
    }
    // [{"id":"5f188c7a000ffc7553742bcb","user":"joebloggs","title":"My second post","content":"Now we're cooking!","date":"2020-07-22T18:59:06.735+00:00","comments":null},{"id":"5f188c7a000ffc7553742bca","user":"joebloggs","title":"My first post","content":"Look at me, I can blog!","date":"2020-07-22T18:59:06.734+00:00","comments":null},{"id":"5f188c7a000ffc7553742bcc","user":"drumpf","title":"Covfefe","content":"Tweet tweet","date":"2020-07-22T18:59:06.735+00:00","comments":null}]

}
