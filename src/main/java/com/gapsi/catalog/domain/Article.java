package com.gapsi.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private String id;

    @Column(name = "name", length = 20, nullable = false, updatable = false)
    private String name;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Column(name = "price", precision = 10, scale = 2, nullable = false, updatable = false)
    private BigDecimal price;

    @Column(name = "model", length = 10, nullable = false)
    private String model;

    protected Article() {
    }

    public Article(String id, String name, String description, BigDecimal price, String model) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getModel() {
        return model;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateModel(String model) {
        this.model = model;
    }

    public void update(String description, String model) {
        if (description == null && model == null) {
            throw new IllegalArgumentException("At least one field must be provided");
        }

        String newDescription = description != null ? description : this.description;
        String newModel = model != null ? model : this.model;

        if (newDescription.equals(this.description) && newModel.equals(this.model)) {
            return;
        }

        if (description != null) {
            if (description.isBlank() || description.length() > 200) {
                throw new IllegalArgumentException("Invalid description");
            }
            this.description = description;
        }

        if (model != null) {
            if (model.isBlank() || model.length() > 10) {
                throw new IllegalArgumentException("Invalid model");
            }
            this.model = model;
        }
    }
}
