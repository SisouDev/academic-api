package com.institution.management.academic_api.domain.model.entities.user;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.Role;

import java.util.HashSet;
import java.util.Set;

public class User {
    private Long id;
    private String login;
    private String passwordHash;
    private boolean isActive;
    private Person person;
    private Set<Role> roles = new HashSet<>();
}
