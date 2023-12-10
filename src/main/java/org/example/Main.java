package org.example;

import java.io.IOException;
import java.net.HttpURLConnection;

public class Main {public static void main(String[] args) throws IOException, InterruptedException {

    InteractionWithUser interactionWithUser = new InteractionWithUser("https://jsonplaceholder.typicode.com/users");

    User user = new User();
    user.setId(10);
    user.setName("John");
    user.setUsername("fear");
    user.setEmail("bar235@gmail.com");
    user.setPhone("1-548-458-4589");
    user.setWebsite("www.mysite.com");

    System.out.println(interactionWithUser.createUser(user));
    System.out.println(interactionWithUser.updateUser(user, 10));
    interactionWithUser.deleteUser(5);
    interactionWithUser.getAllUsers();
    System.out.println(interactionWithUser.getUserByID(5));
    System.out.println(interactionWithUser.getUserByUsername("Kamren"));
    interactionWithUser.lastCommentsToFile(7);
    interactionWithUser.openedTasks(5);
}
}