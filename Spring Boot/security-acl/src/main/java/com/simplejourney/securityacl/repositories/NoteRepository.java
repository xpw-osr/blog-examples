package com.simplejourney.securityacl.repositories;

import com.simplejourney.securityacl.entities.Note;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {
}
