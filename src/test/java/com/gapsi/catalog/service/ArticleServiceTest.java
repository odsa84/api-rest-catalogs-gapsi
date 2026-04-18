package com.gapsi.catalog.service;

import com.gapsi.catalog.domain.Article;
import com.gapsi.catalog.dto.ArticleUpdateRequest;
import com.gapsi.catalog.exception.ArticleNotFoundException;
import com.gapsi.catalog.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article(
                "ART0000001",
                "Laptop Gaming",
                "Original description",
                new BigDecimal("1299.99"),
                "XG-2024"
        );
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("should return article when exists")
        void shouldReturnArticleWhenExists() {
            when(articleRepository.findById("ART0000001")).thenReturn(Optional.of(article));

            Article result = articleService.findById("ART0000001");

            assertNotNull(result);
            assertEquals("ART0000001", result.getId());
            assertEquals("Laptop Gaming", result.getName());
            verify(articleRepository).findById("ART0000001");
        }

        @Test
        @DisplayName("should throw ArticleNotFoundException when not exists")
        void shouldThrowExceptionWhenNotExists() {
            when(articleRepository.findById("ART9999999")).thenReturn(Optional.empty());

            ArticleNotFoundException exception = assertThrows(
                    ArticleNotFoundException.class,
                    () -> articleService.findById("ART9999999")
            );

            assertEquals("Article not found with id: ART9999999", exception.getMessage());
            assertEquals("ART9999999", exception.getArticleId());
            verify(articleRepository).findById("ART9999999");
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("should update article successfully")
        void shouldUpdateArticleSuccessfully() {
            when(articleRepository.findById("ART0000001")).thenReturn(Optional.of(article));
            when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ArticleUpdateRequest request = new ArticleUpdateRequest("New description", "NEW-MODEL");

            Article result = articleService.update("ART0000001", request);

            assertNotNull(result);
            assertEquals("New description", result.getDescription());
            assertEquals("NEW-MODEL", result.getModel());
            verify(articleRepository).findById("ART0000001");
            verify(articleRepository).save(article);
            verifyNoMoreInteractions(articleRepository);
        }

        @Test
        @DisplayName("should update only description when model is null")
        void shouldUpdateOnlyDescription() {
            when(articleRepository.findById("ART0000001")).thenReturn(Optional.of(article));
            when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ArticleUpdateRequest request = new ArticleUpdateRequest("New description", null);

            Article result = articleService.update("ART0000001", request);

            assertEquals("New description", result.getDescription());
            assertEquals("XG-2024", result.getModel());
        }

        @Test
        @DisplayName("should update only model when description is null")
        void shouldUpdateOnlyModel() {
            when(articleRepository.findById("ART0000001")).thenReturn(Optional.of(article));
            when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ArticleUpdateRequest request = new ArticleUpdateRequest(null, "NEW-MODEL");

            Article result = articleService.update("ART0000001", request);

            assertEquals("Original description", result.getDescription());
            assertEquals("NEW-MODEL", result.getModel());
        }

        @Test
        @DisplayName("should throw ArticleNotFoundException when article not exists")
        void shouldThrowExceptionWhenArticleNotExists() {
            when(articleRepository.findById("ART9999999")).thenReturn(Optional.empty());

            ArticleUpdateRequest request = new ArticleUpdateRequest("New description", "NEW-MODEL");

            assertThrows(
                    ArticleNotFoundException.class,
                    () -> articleService.update("ART9999999", request)
            );

            verify(articleRepository).findById("ART9999999");
            verify(articleRepository, never()).save(any());
        }

        @Test
        @DisplayName("should propagate IllegalArgumentException from domain")
        void shouldPropagateIllegalArgumentException() {
            when(articleRepository.findById("ART0000001")).thenReturn(Optional.of(article));

            ArticleUpdateRequest request = new ArticleUpdateRequest(null, null);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> articleService.update("ART0000001", request)
            );

            verify(articleRepository, never()).save(any());
        }

        @Test
        @DisplayName("should preserve immutable fields after update")
        void shouldPreserveImmutableFields() {
            when(articleRepository.findById("ART0000001")).thenReturn(Optional.of(article));
            when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ArticleUpdateRequest request = new ArticleUpdateRequest("New description", "NEW-MODEL");

            Article result = articleService.update("ART0000001", request);

            assertEquals("ART0000001", result.getId());
            assertEquals("Laptop Gaming", result.getName());
            assertEquals(new BigDecimal("1299.99"), result.getPrice());
        }
    }
}
