package com.gapsi.catalog.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ErrorResponse", description = "Respuesta de error estándar")
public record ErrorResponse(
        @Schema(description = "Momento en que ocurrió el error", example = "2026-04-18T10:30:00Z")
        Instant timestamp,

        @Schema(description = "Código de estado HTTP", example = "400")
        int status,

        @Schema(description = "Descripción corta del error HTTP", example = "Bad Request")
        String error,

        @Schema(description = "Mensaje descriptivo del error", example = "Validation failed")
        String message,

        @Schema(description = "Path del endpoint que generó el error", example = "/api/v1/articles/ART0000001")
        String path,

        @Schema(description = "Lista de errores de validación (opcional)")
        List<FieldError> errors
) {
    public ErrorResponse(int status, String error, String message, String path) {
        this(Instant.now(), status, error, message, path, null);
    }

    public ErrorResponse(int status, String error, String message, String path, List<FieldError> errors) {
        this(Instant.now(), status, error, message, path, errors);
    }

    @Schema(name = "FieldError", description = "Detalle de error de validación por campo")
    public record FieldError(
            @Schema(description = "Nombre del campo con error", example = "description")
            String field,

            @Schema(description = "Mensaje de error del campo", example = "must not be blank")
            String message
    ) {
    }
}
