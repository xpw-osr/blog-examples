package com.simplejourney.securityacl.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * [postgresql bigserial or sequence using jpa](https://dba.stackexchange.com/questions/62939/postgresql-bigserial-or-sequence-using-jpa)
 * MUST add
 * ```java
 * @GeneratedValue(strategy=GenerationType.IDENTITY)
 * ```
 * to 'id' field
 */

@Data
@Entity
@Table(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private long createDate;

    @Column(name = "author_id")
    private long authorId;
}
