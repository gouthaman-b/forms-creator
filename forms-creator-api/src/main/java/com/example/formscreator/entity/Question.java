package com.example.formscreator.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Digits(fraction = 0, integer = 3, message = "Incorrect Number Format For OrderNumber")
    @Positive(message = "OrderNumber Must Be Positive")
    @Column(nullable = false)
    private Long orderNumber;

    @NotBlank(message = "Title Cannot Be Empty")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description Cannot Be Empty")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Type Cannot Be Empty")
    @Column(nullable = false)
    private String type;

    @NotNull(message = "Required Not Provided")
    @Column(nullable = false)
    private boolean required;

    private Long max;

    private Long min;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false, updatable = false)
    private Form form;

    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade=CascadeType.ALL, orphanRemoval = true)
    private  List<Response> responses;

    @OneToMany(mappedBy = "question", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;
}
