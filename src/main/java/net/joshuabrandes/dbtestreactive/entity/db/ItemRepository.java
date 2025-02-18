package net.joshuabrandes.dbtestreactive.entity.db;

import net.joshuabrandes.dbtestreactive.entity.model.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {

    Flux<Item> findAllByCreatedAtBefore(LocalDateTime timestamp);

    Flux<Item> findAllByCreatedAtAfterAndPriceGreaterThan(LocalDateTime createdAtAfter, Double priceIsGreaterThan);
}
