package com.example.blogger.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@EnableMongoRepositories(("com.example.blogger.model"))
public class BlogPostRepositoryTests {

    private static final Logger logger = LoggerFactory.getLogger(BlogPostRepositoryTests.class);

    @Autowired
    BlogPostRepository blogPostRepository;

    private BlogPost post1, post2, post3;

    @BeforeEach
    void setup() {
        post1 = new BlogPost(null, "joebloggs", "My first post", "Look at me, I can blog!", new Date(), null);
        post2 = new BlogPost(null, "joebloggs", "My second post", "Now we're cooking!", new Date(), null);
        post3 = new BlogPost(null, "drumpf", "Covfefe", "Tweet tweet", new Date(), null);
    }

    @AfterEach
    void tearDown() {
        blogPostRepository.deleteAll().block();
    }

    @Test
    void testInsertOne() {
        // given

        // when
        BlogPost blogPost = blogPostRepository.save(post1).block();

        //then
        logger.info(String.valueOf(blogPost));
        assertNotNull(blogPost.getId());
        assertEquals("joebloggs", blogPost.getUser());
        assertEquals("My first post", blogPost.getTitle());
        assertEquals("Look at me, I can blog!", blogPost.getContent());
    }

    @Test
    void testFindByUser() {
        // given
        blogPostRepository.saveAll(Flux.just(post1, post2, post3))
                          .blockLast();

        // when
        List<BlogPost> posts = blogPostRepository.findByUser("joebloggs")
                                                 .collectList()
                                                 .block();

        // then
        logger.info(String.valueOf(posts));
        assertEquals(2 , posts.size());
    }
}
