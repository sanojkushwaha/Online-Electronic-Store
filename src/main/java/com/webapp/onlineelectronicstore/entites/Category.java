package com.webapp.onlineelectronicstore.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name= "categories")
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "category_title", length = 60, nullable = false)
    private String title;

    @Column(name = "category_desc",length=800)
    private String description;

    private String coverImage;

    //other attribute write here....

}
