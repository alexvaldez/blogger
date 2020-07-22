package com.example.blogger;

import com.example.blogger.model.BlogPost;
import com.example.blogger.model.BlogPostRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootTest
@EnableAutoConfiguration
@EnableMongoRepositories(("com.example.blogger.model"))
class BloggerApplicationTests {

    @Autowired
    BlogPostRepository blogPostRepository;

    private BlogPost post1, post2, post3;

    @BeforeEach
    void setup() {
        blogPostRepository.deleteAll();
        post1 = new BlogPost(null, "joebloggs", "My first post", "Look at me, I can blog!", new Date(), null);
        post2 = new BlogPost(null, "joebloggs", "My second post", "Now we're cooking!", new Date(), null);
        post3 = new BlogPost(null, "drumpf", "Covfefe", "Tweet tweet", new Date(), null);
    }

    @Test
    void testInsertOne() {
        // given

        // when
        BlogPost blogPost = blogPostRepository.save(post1).block();

        //then
	assertNotNull(blogPost.getId());
	assertEquals("joebloggs", blogPost.getUser());
	assertEquals("My first post", blogPost.getTitle());
	assertEquals("Look at me, I can blog!", blogPost.getContent());
    }

    @Test
    void testFindByUser() {
        // given
        blogPostRepository.save(post1).block();
        blogPostRepository.save(post2).block();
        blogPostRepository.save(post3).block();

        // when
        List<BlogPost> posts = blogPostRepository.findByUser("joebloggs")
                                                 .collectList()
                                                 .block();

        // then
	assertEquals(2 , posts.size());
    }
}
