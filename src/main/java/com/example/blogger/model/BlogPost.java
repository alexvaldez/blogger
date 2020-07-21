package com.example.blogger.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPost {

    @Id
    private String id;

    private String user;

    private String title;

    private String content;

    private Date date;

    private List<BlogComment> comments;
}
