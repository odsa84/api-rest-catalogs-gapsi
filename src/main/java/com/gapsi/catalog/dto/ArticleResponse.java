package com.gapsi.catalog.dto;

import java.math.BigDecimal;

public record ArticleResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        String model
) {
}
