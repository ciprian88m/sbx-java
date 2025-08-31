package dev.ciprian.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class HttpClientExamples {

    public static void main(String[] args) throws IOException, InterruptedException {
        blockingCall();
        System.out.println("=== Separate blocking from async call");
        asyncCall();
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
}
