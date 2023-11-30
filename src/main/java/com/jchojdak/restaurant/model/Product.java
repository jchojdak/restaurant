package com.jchojdak.restaurant.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cook_time", nullable = false)
    private String cookTime;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "favorite", nullable = false)
    private boolean favorite;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
}
