package com.gapsi.catalog.dto;

import jakarta.validation.constraints.Size;

public record ArticleUpdateRequest(
        @Size(min = 1, max = 200, message = "description must be between 1 and 200 characters")
        String description,

        @Size(min = 1, max = 10, message = "model must be between 1 and 10 characters")
        String model
) {
    public boolean isEmpty() {
        return description == null && model == null;
    }
}
