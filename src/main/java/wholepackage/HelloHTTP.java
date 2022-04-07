package wholepackage;

/**
 * @author Emma LU
 * @create 2022-03-30-2:52 pm
 */

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloHTTP {
    public static void postRequest() {
        try {
            Post post = new Post(10, "My title", "My body text\nOf this post");
            Gson gson = new Gson();

            // transfer java to json
            String postJSON = gson.toJson(post);

            HttpRequest request = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/posts"))
                    .POST(HttpRequest.BodyPublishers.ofString(postJSON))
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static void getRequest() {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://api.spacetraders.io/game/status"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());

            Gson gson = new Gson();
            Post post = gson.fromJson(response.body(), Post.class);
            System.out.println(post + "--------------");

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static void CreateUser(){
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://api.spacetraders.io/users/zhuyi8/claim"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());

            String responseBody = response.body();
            Gson gson = new Gson();
            UserPost userPost = gson.fromJson(responseBody, UserPost.class);
            System.out.println(userPost.getToken());

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static void main(String[] args) {
//        getRequest();
        CreateUser();
    }

}
