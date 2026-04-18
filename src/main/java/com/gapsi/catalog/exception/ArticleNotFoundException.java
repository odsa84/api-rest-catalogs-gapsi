package com.gapsi.catalog.exception;

public class ArticleNotFoundException extends RuntimeException {

    private final String articleId;

    public ArticleNotFoundException(String articleId) {
        super("Article not found with id: " + articleId);
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }
}
