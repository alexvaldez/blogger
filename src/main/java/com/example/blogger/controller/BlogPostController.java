package com.example.blogger.controller;

import com.example.blogger.model.BlogPost;
import com.example.blogger.model.BlogPostRepository;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/update")
    private Mono<BlogPost> updateBlogPost(@RequestBody BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }
}