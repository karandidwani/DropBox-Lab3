package com.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.entity.UserGroup;

public interface UserGroupRepository extends CrudRepository<GroupT, Long> {

    UserGroup findByGroupId(Integer group_id);
}
