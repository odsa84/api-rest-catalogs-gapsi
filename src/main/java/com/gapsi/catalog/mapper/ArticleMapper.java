package com.gapsi.catalog.mapper;

import com.gapsi.catalog.domain.Article;
import com.gapsi.catalog.dto.ArticleResponse;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getName(),
                article.getDescription(),
                article.getPrice(),
                article.getModel()
        );
    }
}
