package com.gapsi.catalog.controller;

import com.gapsi.catalog.domain.Article;
import com.gapsi.catalog.dto.ArticleResponse;
import com.gapsi.catalog.dto.ArticleUpdateRequest;
import com.gapsi.catalog.exception.ErrorResponse;
import com.gapsi.catalog.exception.InvalidRequestException;
import com.gapsi.catalog.mapper.ArticleMapper;
import com.gapsi.catalog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articles")
@Validated
@Tag(name = "Articles", description = "Operaciones sobre artículos del catálogo")
public class ArticleController {

    private static final String ID_PATTERN = "^[a-zA-Z0-9]{10}$";
    private static final String ID_VALIDATION_MESSAGE = "Invalid article ID format. Must be exactly 10 alphanumeric characters";

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    public ArticleController(ArticleService articleService, ArticleMapper articleMapper) {
        this.articleService = articleService;
        this.articleMapper = articleMapper;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener artículo por ID", description = "Retorna un artículo específico basado en su identificador único")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artículo encontrado"),
            @ApiResponse(responseCode = "400", description = "ID con formato inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ArticleResponse> getById(
            @Parameter(description = "Identificador único del artículo (exactamente 10 caracteres alfanuméricos)")
            @PathVariable
            @Pattern(regexp = ID_PATTERN, message = ID_VALIDATION_MESSAGE)
            String id) {

        Article article = articleService.findById(id);
        return ResponseEntity.ok(articleMapper.toResponse(article));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar artículo parcialmente", description = "Actualiza los campos permitidos de un artículo (description y/o model)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artículo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ArticleResponse> update(
            @Parameter(description = "Identificador único del artículo (exactamente 10 caracteres alfanuméricos)")
            @PathVariable
            @Pattern(regexp = ID_PATTERN, message = ID_VALIDATION_MESSAGE)
            String id,
            @Valid @RequestBody ArticleUpdateRequest request) {

        if (request.isEmpty()) {
            throw new InvalidRequestException("At least one field must be provided (description or model)");
        }

        Article article = articleService.update(id, request);
        return ResponseEntity.ok(articleMapper.toResponse(article));
    }
}
