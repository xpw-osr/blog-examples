package com.simplejourney.securityacl.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_group")
public class UserGroup {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "group_id")
    private Group group;
}
