package com.example.demo.model;

import lombok.Data;
import javax.persistence.Id;

@Data
public class Sum {
    @Id
    private String category;
    private Integer sum;

    public Sum(String category, Integer sum) {
        this.category = category;
        this.sum = sum;
    }
}
