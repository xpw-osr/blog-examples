package com.simplejourney.securityacl.repositories;

import com.simplejourney.securityacl.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoy extends PagingAndSortingRepository<User, Long> {
    User getUserByName(String username);
}
