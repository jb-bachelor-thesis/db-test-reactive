package net.joshuabrandes.dbtestreactive.control.service;

import net.joshuabrandes.dbtestreactive.entity.db.ItemRepository;
import net.joshuabrandes.dbtestreactive.entity.model.Item;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Flux<Item> getAllItemsBeforeTimestamp(LocalDateTime timestamp) {
        return itemRepository.findAllByCreatedAtBefore(timestamp);
    }

    public Flux<Item> getAllItemsAfterAndPriceGreaterThan(LocalDateTime createdAtAfter, Double priceIsGreaterThan) {
        return itemRepository.findAllByCreatedAtAfterAndPriceGreaterThan(createdAtAfter, priceIsGreaterThan);
    }

    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Mono<Item> saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Flux<Item> saveAllItems(List<Item> items) {
        return itemRepository.saveAll(items);
    }
}
