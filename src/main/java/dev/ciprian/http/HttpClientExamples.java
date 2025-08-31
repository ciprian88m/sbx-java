package dev.ciprian.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;

public class HttpClientExamples {

    public static void main(String[] args) throws IOException, InterruptedException {
        blockingCall();
        System.out.println("\n=== Separate blocking from async call");
        asyncCall();
        System.out.println("\n=== Separate async from structured concurrency call");
        structuredConcurrencyCall();
        System.out.println("\n=== Separate structured concurrency call from call with joiner");
        structuredConcurrencyWithJoinerCall();
    }

    private static void blockingCall() throws IOException, InterruptedException {
        var client = HttpClient.newBuilder().build();
        var uri = URI.create("https://rickandmortyapi.com/api/character/1");

        var request = HttpRequest.newBuilder(uri).build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("response status code = " + response.statusCode());

        var name = response.body().substring(15, 29);
        System.out.println("name = " + name);
    }

    private static void asyncCall() {
        var client = HttpClient.newBuilder().build();
        var firstUri = URI.create("https://rickandmortyapi.com/api/character/1");
        var secondUri = URI.create("https://rickandmortyapi.com/api/character/2");

        var firstRequest = HttpRequest.newBuilder(firstUri).build();
        var secondRequest = HttpRequest.newBuilder(secondUri).build();
        var bodyHandler = HttpResponse.BodyHandlers.ofString();

        CompletableFuture.allOf(
                        client.sendAsync(firstRequest, bodyHandler).thenAccept(response -> {
                            System.out.println("first response status code = " + response.statusCode());
                            var name = response.body().substring(15, 29);
                            System.out.println("first character name = " + name);
                        }),
                        client.sendAsync(secondRequest, bodyHandler).thenAccept(response -> {
                            System.out.println("second response status code = " + response.statusCode());
                            var name = response.body().substring(15, 28);
                            System.out.println("second character name = " + name);
                        }))
                .join();
    }

    private static void structuredConcurrencyCall() {
        var client = HttpClient.newBuilder().build();
        var firstUri = URI.create("https://rickandmortyapi.com/api/character/1");
        var secondUri = URI.create("https://rickandmortyapi.com/api/character/2");

        var firstRequest = HttpRequest.newBuilder(firstUri).build();
        var secondRequest = HttpRequest.newBuilder(secondUri).build();
        var bodyHandler = HttpResponse.BodyHandlers.ofString();

        try (var scope = StructuredTaskScope.open()) {
            var firstTask = scope.fork(() -> client.send(firstRequest, bodyHandler));
            var secondTask = scope.fork(() -> client.send(secondRequest, bodyHandler));

            scope.join();

            var firstResponse = firstTask.get();
            var secondResponse = secondTask.get();

            System.out.println("first response status code = " + firstResponse.statusCode());
            var firstCharacterName = firstResponse.body().substring(15, 29);
            System.out.println("first character name = " + firstCharacterName);

            System.out.println("second response status code = " + secondResponse.statusCode());
            var secondCharacterName = secondResponse.body().substring(15, 28);
            System.out.println("second character name = " + secondCharacterName);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void structuredConcurrencyWithJoinerCall() {
        var client = HttpClient.newBuilder().build();
        var firstUri = URI.create("https://rickandmortyapi.com/api/character/1");
        var secondUri = URI.create("https://rickandmortyapi.com/api/character/2");

        var firstRequest = HttpRequest.newBuilder(firstUri).build();
        var secondRequest = HttpRequest.newBuilder(secondUri).build();
        var bodyHandler = HttpResponse.BodyHandlers.ofString();

        try (var scope = StructuredTaskScope.open(Joiner.<HttpResponse<String>>anySuccessfulResultOrThrow())) {
            scope.fork(() -> client.send(firstRequest, bodyHandler));
            scope.fork(() -> client.send(secondRequest, bodyHandler));

            var response = scope.join();

            System.out.println("response status code = " + response.statusCode());
            var characterName = response.body().substring(15, 29);
            System.out.println("character name = " + characterName);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
