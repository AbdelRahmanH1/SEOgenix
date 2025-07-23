package com.system.seogenix.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document(collection = "articles")
@Getter
@Setter
public class Article {

    @Id
    private String id;

    @NotBlank
    @Size(min=5)
    private String title;
    private List<String> tags;
    private Date createdAt = new Date();
}
