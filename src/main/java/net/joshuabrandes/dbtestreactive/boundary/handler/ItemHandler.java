package net.joshuabrandes.dbtestreactive.boundary.handler;

import net.joshuabrandes.dbtestreactive.boundary.model.CategoryDTO;
import net.joshuabrandes.dbtestreactive.control.mapper.ItemMapper;
import net.joshuabrandes.dbtestreactive.control.service.ItemService;
import net.joshuabrandes.dbtestreactive.entity.model.Item;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class ItemHandler {

    // you may want to change this depending on your dataset
    private static final LocalDateTime REFERENCE_TIME = LocalDate.now().atStartOfDay();
    private static final Random RANDOM = new Random();

    private final ItemService itemService;

    public ItemHandler(ItemService itemService) {
        this.itemService = itemService;
    }

    public Mono<ServerResponse> getCategories(ServerRequest request) {
        return itemService.getAllItemsAfterAndPriceGreaterThan(REFERENCE_TIME.minusYears(3), 500.0)
                .filter(item -> !item.getStatus().equalsIgnoreCase("discontinued"))
                .map(item -> {
                    item.setPrice(calculateAdjustedPrice(item));
                    return item;
                })
                .collectList()
                .flatMap(items -> itemService.saveAllItems(items).collectList())
                .flatMap(items -> Mono.just(items.stream()
                        .collect(Collectors.groupingBy(
                                Item::getCategory,
                                Collectors.averagingDouble(Item::getPrice)
                        ))))
                .flatMap(map -> Mono.just(map.entrySet().stream()
                        .map(entry -> new CategoryDTO(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())))
                .flatMap(list -> ServerResponse.ok().bodyValue(list));
    }

    private double calculateAdjustedPrice(Item item) {
        var adjustedPrice = item.getPrice();

        if (item.getCreatedAt().isBefore(REFERENCE_TIME.minusMonths(6))) {
            adjustedPrice *= 0.9;
        }
        if (item.getStatus().equalsIgnoreCase("preorder")) {
            adjustedPrice = item.getPrice() * 1.1;
        }

        if (adjustedPrice > 5000.0) {
            adjustedPrice = 1000.0 + (adjustedPrice % 500.0);
        }

        return Math.max(501.0, adjustedPrice);
    }


    public Mono<ServerResponse> getExpensiveItemsByCategory(ServerRequest request) {
        return itemService.getAllItems()
                .groupBy(Item::getCategory)
                .flatMap(groupedFlux -> groupedFlux
                        .collectSortedList(Comparator.comparing(Item::getPrice).reversed())
                        .map(sortedList -> {
                            var mostExpensive = sortedList.getFirst();
                            return Tuples.of(groupedFlux.key(), ItemMapper.mapToItemDTO(mostExpensive));
                        })
                )
                .collectMap(Tuple2::getT1, Tuple2::getT2)
                .flatMap(map -> ServerResponse.ok().bodyValue(map));
    }

    private static double getRandomDouble() {
        return getRandomDouble(1);
    }

    private static double getRandomDouble(double modifier) {
        return RANDOM.nextDouble() * modifier;
    }
}
