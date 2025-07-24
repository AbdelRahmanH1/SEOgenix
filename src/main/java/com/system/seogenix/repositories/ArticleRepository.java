package com.system.seogenix.repositories;

import com.system.seogenix.entities.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {
    Article findByTitle(String title);
}
