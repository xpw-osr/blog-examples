package com.simplejourney.securityacl.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    /**
     * When we use OneToMany and ManyToMany to fetch multiple objects and there are multiple layers, we should use 'Set' to store the objects' of Many side, but not 'List'.
     * Otherwise, 'Caused by: org.hibernate.HibernateException: cannot simultaneously fetch multiple bags' exception will be thrown.
     */

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    private Set<Group> groups;
}
