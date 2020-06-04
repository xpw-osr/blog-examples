package com.simplejourney.securityacl.repositories;

import com.simplejourney.securityacl.entities.UserGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends PagingAndSortingRepository<UserGroup, Long> {
    List<UserGroup> findUserGroupByUserIsAndGroupIs(String username, String groupName);
}
