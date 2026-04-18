package com.gapsi.catalog.service;

import com.gapsi.catalog.domain.Article;
import com.gapsi.catalog.dto.ArticleUpdateRequest;
import com.gapsi.catalog.exception.ArticleNotFoundException;
import com.gapsi.catalog.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public Article findById(String id) {
        log.info("Fetching article with id: {}", id);
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Transactional
    public Article update(String id, ArticleUpdateRequest request) {
        log.info("Updating article with id: {}", id);
        Article article = findById(id);

        article.update(request.description(), request.model());

        Article updated = articleRepository.save(article);
        log.info("Article updated successfully: {}", id);
        return updated;
    }
}
