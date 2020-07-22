package com.example.blogger.controller;

import com.example.blogger.model.BlogPost;
import com.example.blogger.model.BlogPostRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/blogPosts")
public class BlogPostController {

    private static final Logger logger = LoggerFactory.getLogger(BlogPostController.class);

    @Autowired
    private BlogPostRepository blogPostRepository;

    @GetMapping("/{id}")
    private Mono<BlogPost> getBlogPostById(@PathVariable String id) {
        return blogPostRepository.findById(id);
    }

    @GetMapping
    private Flux<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    @GetMapping("/byUser/{user}")
    private Flux<BlogPost> getPostsByUser(@PathVariable String user) {
        return blogPostRepository.findByUser(user);
    }

    @PostMapping
    private Mono<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }

    @PostMapping("/update/{id}")
    private Mono<ResponseEntity<BlogPost>> updateBlogPost(@PathVariable String id, @RequestBody BlogPost blogPost) {
        return blogPostRepository.findById(id)
                .flatMap(oldPost -> {
                            oldPost.setTitle(blogPost.getTitle());
                            oldPost.setContent(blogPost.getContent());
                            oldPost.setDate(blogPost.getDate());
                            return blogPostRepository.save(oldPost);
                        })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
