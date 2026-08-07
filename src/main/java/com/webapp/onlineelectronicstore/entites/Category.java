package com.webapp.onlineelectronicstore.entites;

import com.webapp.onlineelectronicstore.enums.Role;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;


    //other attribute write here....

}
