package com.system.seogenix.services;

import com.system.seogenix.entities.Article;
import com.system.seogenix.repositories.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final RedisTagService redisTagService;
    private final AiService aiService;
    private final int MAX_TAGS_NUMBER = 5;

    private List<String> getTagsFromRedis(String title){
        var tags =  redisTagService.getRandomTags(title,MAX_TAGS_NUMBER);
        if(tags != null && !tags.isEmpty()){
            return tags;
        }
        return null;
    }

    private List<String> getTagsFromMongo(String title){
        Article article = articleRepository.findByTitle(title);
        if(article !=null && article.getTags()!=null && !article.getTags().isEmpty()){
            redisTagService.saveTag(title,article.getTags());
            return article.getTags().stream().limit(MAX_TAGS_NUMBER).toList();
        }
        return null;
    }

    private List<String> getTagsFromAi(String title){
        var newTags = aiService.generateResponse(title).block();
        if(newTags == null || newTags.isEmpty()){
            return null;
        }
        redisTagService.saveTag(title,newTags);

        var article = new Article();
        article.setTitle(title);
        article.setTags(newTags);
        articleRepository.save(article);
        return newTags.stream().limit(MAX_TAGS_NUMBER).toList();
    }

    public List<String> getTagsByTitle(String title){

        var redisTags = getTagsFromRedis(title);
        if(redisTags!=null && !redisTags.isEmpty()){
            System.out.println("get data from redis");
            return redisTags;
        }

        var mongoTags = getTagsFromMongo(title);
        if(mongoTags!=null && !mongoTags.isEmpty()){
            System.out.println("get data from mongo");
            return mongoTags;
        }
        var aiTags = getTagsFromAi(title);
        if(aiTags!=null && !aiTags.isEmpty()){
            System.out.println("get data from ai");
            return aiTags;
        }
        throw new RuntimeException("No tags could be found");
    }
}
