package net.joshuabrandes.dbtestreactive.boundary.router;

import net.joshuabrandes.dbtestreactive.boundary.handler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ItemRouter {

    private final ItemHandler itemHandler;

    public ItemRouter(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Bean
    RouterFunction<ServerResponse> getCategories() {
        return route(GET("/v1/categories"), itemHandler::getCategories);
    }

    @Bean
    RouterFunction<ServerResponse> getExpensiveItemsByCategory(RouterFunction<ServerResponse> getExpensiveItemsByCategory) {
        return route(GET("/v1/items/expensive-by-category"), itemHandler::getExpensiveItemsByCategory);
    }
}
