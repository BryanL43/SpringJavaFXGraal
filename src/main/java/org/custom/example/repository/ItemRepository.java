package org.custom.example.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.ListCrudRepository;

import org.custom.example.entity.Item;

public interface ItemRepository extends ListCrudRepository<@NonNull Item, @NonNull Long> {}
