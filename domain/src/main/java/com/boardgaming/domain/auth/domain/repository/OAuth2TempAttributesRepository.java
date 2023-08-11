package com.boardgaming.domain.auth.domain.repository;

import com.boardgaming.domain.auth.domain.OAuth2TempAttributes;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2TempAttributesRepository extends CrudRepository<OAuth2TempAttributes, String> {
}
