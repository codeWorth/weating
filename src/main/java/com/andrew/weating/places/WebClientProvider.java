package com.andrew.weating.places;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.SneakyThrows;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebClientProvider {

    private static final int REQUESTS_PER_SECOND = 100;
    private static final int MILLIS_PER_REQUEST = 1000 / REQUESTS_PER_SECOND;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private static WebClientProvider provider = null;

    private final WebClient client;
    private final Deque<CompletableFuture<WebClient>> requests;

    public static WebClientProvider getProvider() {
        if (provider == null) {
            provider = new WebClientProvider();
        }
        return provider;
    }

    @SneakyThrows(value = IOException.class)
    private static WebClient makeClient() {
        SslContext context = SslContextBuilder.forClient().build();
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().secure(t -> t.sslContext(context))))
                .baseUrl(BASE_URL)
                .build();
    }

    private WebClientProvider() {
        this.client = makeClient();
        this.requests = new ConcurrentLinkedDeque<>();
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::handleRequest, 0, MILLIS_PER_REQUEST + 1, MILLISECONDS);
    }

    public Future<WebClient> request() {
        CompletableFuture<WebClient> future = new CompletableFuture<>();
        requests.addLast(future);
        return future;
    }

    private void handleRequest() {
        if (!requests.isEmpty()) {
            requests.pop().complete(client);
        }
    }
}
