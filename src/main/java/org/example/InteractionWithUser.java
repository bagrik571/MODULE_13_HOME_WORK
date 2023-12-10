package org.example;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;


public class InteractionWithUser {

    private final String URL;
    private final HttpClient CLIENT = HttpClient.newHttpClient();

    public InteractionWithUser(String URL) {
        this.URL = URL;
    }

    //создание нового пользователя
    public User createUser (User user) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user)))
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        return new Gson().fromJson(response.body(), User.class);
    }

    //обновление пользователя
    public User updateUser (User user, int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .header("Content-type", "application/json; charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user)))
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        return new Gson().fromJson(response.body(), User.class);
    }

    //удаление пользователя
    public void deleteUser (int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .DELETE()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    //получение информации обо всех пользователях
    public void getAllUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    //получение информации о пользователе с определенным id
    public User getUserByID(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id))
                .GET()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        return new Gson().fromJson(response.body(), User.class);
    }

    //получение информации о пользователе с опредленным username
    public User getUserByUsername (String username) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "?username=" + username))
                .GET()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        return new Gson().fromJson(new StringBuilder(response.body()).deleteCharAt((response.body().length())-1).deleteCharAt(0).toString(), User.class);
    }

    //все комментарии к последнему посту определенного пользователя в файл
    public void lastCommentsToFile (int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id + "/posts"))
                .GET()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Posts> posts = new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), Posts[].class)));
        int maxId = posts.get(0).getId();

        for (Posts temp : posts) {
            if (temp.getId() > maxId) {
                maxId = temp.getId();
            }
        }

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/" + maxId + "/comments"))
                .GET()
                .build();

        final HttpResponse<String> response2 = CLIENT.send(request2, HttpResponse.BodyHandlers.ofString());
        File resultFile = new File("user-" + id + "-post-" + maxId + "-comments.json");
        try (BufferedWriter toWrite = new BufferedWriter(new FileWriter(resultFile))) {
            toWrite.append(response2.body());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //все открытые задачи для пользователя
    public void openedTasks (int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/" + id + "/todos"))
                .GET()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), Todos[].class))).stream()
                .filter(x -> !x.isCompleted())
                .forEach(x -> System.out.println(x.getTitle()));
    }
}
