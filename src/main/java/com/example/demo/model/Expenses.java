package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "expenses")
@SQLDelete(sql = "UPDATE expenses SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean deleted = Boolean.FALSE;
    private Timestamp date;
    private String name;
    private String category;
    private Integer sum;
    private String comments;
    public Expenses(Long id, boolean deleted, Timestamp date, String name, String category, Integer sum, String comments) {
        this.id = id;
        this.deleted = deleted;
        this.date = date;
        this.name = name;
        this.category = category;
        this.sum = sum;
        this.comments = comments;
    }
}
