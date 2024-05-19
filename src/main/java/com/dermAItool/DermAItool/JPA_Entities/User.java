package com.dermAItool.DermAItool.JPA_Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity  // JPA class mapping
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "mail"))  // Concerned table and constraints ;
public class User {
    @Id   // Primary key annotation
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // How it's generated ;
    @Column(name = "id")
    private Long id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "password")
    private String password;
}
