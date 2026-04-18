package com.gapsi.catalog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

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
    @DisplayName("update() method")
    class UpdateMethod {

        @Test
        @DisplayName("should update both description and model")
        void shouldUpdateBothFields() {
            article.update("New description", "NEW-MODEL");

            assertEquals("New description", article.getDescription());
            assertEquals("NEW-MODEL", article.getModel());
        }

        @Test
        @DisplayName("should update only description when model is null")
        void shouldUpdateOnlyDescription() {
            article.update("New description", null);

            assertEquals("New description", article.getDescription());
            assertEquals("XG-2024", article.getModel());
        }

        @Test
        @DisplayName("should update only model when description is null")
        void shouldUpdateOnlyModel() {
            article.update(null, "NEW-MODEL");

            assertEquals("Original description", article.getDescription());
            assertEquals("NEW-MODEL", article.getModel());
        }

        @Test
        @DisplayName("should throw exception when both fields are null")
        void shouldThrowExceptionWhenBothFieldsAreNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> article.update(null, null)
            );

            assertEquals("At least one field must be provided", exception.getMessage());
        }

        @Test
        @DisplayName("should throw exception when description is blank")
        void shouldThrowExceptionWhenDescriptionIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> article.update("   ", null)
            );

            assertEquals("Invalid description", exception.getMessage());
        }

        @Test
        @DisplayName("should throw exception when description exceeds 200 characters")
        void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {
            String longDescription = "a".repeat(201);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> article.update(longDescription, null)
            );

            assertEquals("Invalid description", exception.getMessage());
        }

        @Test
        @DisplayName("should accept description with exactly 200 characters")
        void shouldAcceptDescriptionWithMaxLength() {
            String maxDescription = "a".repeat(200);

            article.update(maxDescription, null);

            assertEquals(maxDescription, article.getDescription());
        }

        @Test
        @DisplayName("should throw exception when model is blank")
        void shouldThrowExceptionWhenModelIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> article.update(null, "   ")
            );

            assertEquals("Invalid model", exception.getMessage());
        }

        @Test
        @DisplayName("should throw exception when model is empty")
        void shouldThrowExceptionWhenModelIsEmpty() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> article.update(null, "")
            );

            assertEquals("Invalid model", exception.getMessage());
        }

        @Test
        @DisplayName("should throw exception when model exceeds 10 characters")
        void shouldThrowExceptionWhenModelExceedsMaxLength() {
            String longModel = "a".repeat(11);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> article.update(null, longModel)
            );

            assertEquals("Invalid model", exception.getMessage());
        }

        @Test
        @DisplayName("should accept model with exactly 10 characters")
        void shouldAcceptModelWithMaxLength() {
            String maxModel = "a".repeat(10);

            article.update(null, maxModel);

            assertEquals(maxModel, article.getModel());
        }
    }

    @Nested
    @DisplayName("Article immutable fields")
    class ImmutableFields {

        @Test
        @DisplayName("should preserve id after update")
        void shouldPreserveIdAfterUpdate() {
            article.update("New description", "NEW-MODEL");

            assertEquals("ART0000001", article.getId());
        }

        @Test
        @DisplayName("should preserve name after update")
        void shouldPreserveNameAfterUpdate() {
            article.update("New description", "NEW-MODEL");

            assertEquals("Laptop Gaming", article.getName());
        }

        @Test
        @DisplayName("should preserve price after update")
        void shouldPreservePriceAfterUpdate() {
            article.update("New description", "NEW-MODEL");

            assertEquals(new BigDecimal("1299.99"), article.getPrice());
        }
    }
}
