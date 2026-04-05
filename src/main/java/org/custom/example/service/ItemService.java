package org.custom.example.service;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import org.custom.example.entity.Item;
import org.custom.example.repository.ItemRepository;

/**
 * Validates Item entities at the service boundary using Jakarta Validation
 * before saving through the repository.
 */
@Validated
@Service
public class ItemService {
    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public Item save(@Valid Item item) {
        return repo.save(item);
    }
}

/*
Usage example:
ItemService itemService = context.getBean(ItemService.class);
itemService.save(new Item(1L, "", "123ABC", "New Item", 0, false, null, 0, 0));
*/
