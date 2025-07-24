package com.system.seogenix.controllers;


import com.system.seogenix.dtos.TitleRequest;
import com.system.seogenix.services.ArticleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("generate-tags")
    public ResponseEntity<List<String>> generateTags(@Valid @RequestBody TitleRequest request){
        var response = articleService.getTagsByTitle(request.title());
        return ResponseEntity.ok(response);
    }
}
