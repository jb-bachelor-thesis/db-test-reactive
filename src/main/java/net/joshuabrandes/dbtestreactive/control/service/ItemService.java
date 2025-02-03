package net.joshuabrandes.dbtestreactive.control.service;

import net.joshuabrandes.dbtestreactive.entity.db.ItemRepository;
import net.joshuabrandes.dbtestreactive.entity.model.Item;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Flux<Item> getAllItemsBeforeTimestamp(LocalDateTime timestamp) {
        return itemRepository.findAllByCreatedAtBefore(timestamp);
    }

    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
