package com.fresh.coding.attachapijavains3.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean isStock;
    private Category category;
}
