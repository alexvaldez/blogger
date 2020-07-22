package com.example.blogger.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BlogPostRepository extends ReactiveMongoRepository<BlogPost, String> {

    public Flux<BlogPost> findByUser(String user, Sort sort);

}
