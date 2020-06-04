package com.simplejourney.securityacl.repositories;

import com.simplejourney.securityacl.entities.Group;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {
}
