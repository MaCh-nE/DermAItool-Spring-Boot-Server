package com.dermAItool.DermAItool.JPA_Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity  // JPA class mapping ;
@Table(name = "images")  // Concerned table ;
@Builder   // For image building ;
public class Images {
    @Id   // Primary key ;
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // How its generated ;
    @Column(name = "imageId")   // Associated column ;
    private Long imageId;

    @ManyToOne   //   Foreign key, many images could belong to the same user ;
    @JoinColumn(name = "userId", referencedColumnName = "id")   // Foreign key source ;
    private User userId;

    @Column(name = "extension")
    private String extension;

    @Column(name = "path")
    private String path;

    @Column(name = "prediction")
    private String prediction;

    @Column(name = "realDiagnosis")
    private String realDiagnosis;

    @Column(name = "submissionDate")
    private Date submissionDate;


}
