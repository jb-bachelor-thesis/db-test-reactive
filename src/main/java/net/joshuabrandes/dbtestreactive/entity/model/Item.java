package net.joshuabrandes.dbtestreactive.entity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("items")
public class Item {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("category")
    private String category;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("price")
    private Double price;

    @Column("status")
    private String status;

    public Item() {
    }

    public Item(String name, String category, LocalDateTime createdAt, Double price, String status) {
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
