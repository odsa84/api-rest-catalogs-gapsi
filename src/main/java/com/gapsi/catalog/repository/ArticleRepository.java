package com.gapsi.catalog.repository;

import com.gapsi.catalog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
