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
import java.util.stream.Collectors;

@Component
public class ItemHandler {

    // you may want to change this depending on your dataset
    private static final LocalDateTime REFERENCE_TIME = LocalDate.now().atStartOfDay();

    private final ItemService itemService;

    public ItemHandler(ItemService itemService) {
        this.itemService = itemService;
    }

    public Mono<ServerResponse> getCategories(ServerRequest request) {
        return itemService.getAllItemsAfterAndPriceGreaterThan(REFERENCE_TIME.minusYears(3), 500.0)
                .filter(item -> !item.getStatus().equalsIgnoreCase("discontinued"))
                .sort((a, b) -> {
                    var categoryComparison = a.getCategory().compareTo(b.getCategory());
                    if (categoryComparison != 0) return categoryComparison;

                    int statusComparison = b.getStatus().compareTo(a.getStatus());
                    if (statusComparison != 0) return statusComparison;

                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .flatMap(item -> {
                    var adjustedPrice = item.getPrice();
                    if (item.getCreatedAt().isBefore(REFERENCE_TIME.minusMonths(6))) {
                        adjustedPrice *= 0.9;
                    }
                    if (item.getStatus().equalsIgnoreCase("preorder")) {
                        adjustedPrice = item.getPrice() * 1.1;
                    }

                    item.setPrice(adjustedPrice);

                    return itemService.saveItem(item);
                })
                .collect(Collectors.groupingBy(Item::getCategory, Collectors.averagingDouble(Item::getPrice)))
                .flatMapMany(map -> Flux.fromIterable(map.entrySet()))
                .map(entry -> new CategoryDTO(entry.getKey(), entry.getValue()))
                .collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(list));
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
}
