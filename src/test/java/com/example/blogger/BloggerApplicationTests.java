package com.example.blogger;

import com.example.blogger.model.BlogPost;
import com.example.blogger.model.BlogPostRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import org.springframework.http.HttpStatus;
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
    void setup() throws ParseException {
        blogPostRepository.deleteAll().block();

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        post1 = new BlogPost(null, "joebloggs", "My first post", "Look at me, I can blog!", format.parse("21-Jul-2020"), null);
        post2 = new BlogPost(null, "joebloggs", "My second post", "Now we're cooking!", format.parse("22-Jul-2020"), null);
        post3 = new BlogPost(null, "drumpf", "Covfefe", "Tweet tweet", format.parse("22-Jul-2020"), null);
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

    @Test
    void testGetByUser() {
        String user = "joebloggs";
        byte[] response = webTestClient.get()
                .uri("/blogPosts/byUser/" + user)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].user").isEqualTo(user)
                .jsonPath("$[1].user").isEqualTo(user)
                .jsonPath("$[0].title").isEqualTo("My second post")
                .jsonPath("$[1].title").isEqualTo("My first post")
                .returnResult()
                .getResponseBody();

        logger.info(new String(response));
    }

    @Test
    void testGetById() {
        byte[] response = webTestClient.get()
                .uri("/blogPosts/" + post3.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.user").isEqualTo(post3.getUser())
                .jsonPath("$.title").isEqualTo(post3.getTitle())
                .jsonPath("$.content").isEqualTo(post3.getContent())
                .returnResult()
                .getResponseBody();

        logger.info(new String(response));
    }

    @Test
    void testUpdate() {
        String post = "{\"user\":\"joebloggs\"," +
                "\"title\":\"How now, brown cow\"," +
                "\"content\":\"Mr. Brown can moo, can you?\"," +
                "\"date\":\"2020-07-22\"}";

        byte[] response = webTestClient.post()
                .uri("/blogPosts/update/" + post1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("How now, brown cow")
                .jsonPath("$.content").isEqualTo("Mr. Brown can moo, can you?")
                .returnResult()
                .getResponseBody();

        logger.info(new String(response));
    }

    @Test
    void testBadUpdate() {
        String post = "{\"user\":\"joebloggs\"," +
                "\"title\":\"How now, brown cow\"," +
                "\"content\":\"Mr. Brown can moo, can you?\"," +
                "\"date\":\"2020-07-22\"}";

        webTestClient.post()
                .uri("/blogPosts/update/badbadbad")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testComment() {
        String comment = "{\"user\":\"ladyg\"," +
                "\"comment\":\"Thank you!\"," +
                "\"date\":\"2020-07-22\"}";

        byte[] response = webTestClient.post()
                .uri("/blogPosts/comment/" + post3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(comment)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.comments.length()").isEqualTo(1)
                .jsonPath("$.comments[0].user").isEqualTo("ladyg")
                .jsonPath("$.comments[0].comment").isEqualTo("Thank you!")
                .returnResult()
                .getResponseBody();

        logger.info(new String(response));
    }
}
