package com.system.seogenix.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document(collation = "articles")
@Getter
@Setter
public class Article {

    @Id
    private String id;


    private String title;
    private String content;
    private List<String> tags;
    private Date createdAt = new Date();
}
