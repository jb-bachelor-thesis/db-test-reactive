package net.joshuabrandes.dbtestreactive.boundary.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemHandler {
    public Mono<ServerResponse> getCategories(ServerRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Mono<ServerResponse> getExpensiveItemsByCategory(ServerRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
