package exercises;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exercises.Classes.User;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class exercise3 {
    private static final String SITE_URL= "https://jsonplaceholder.typicode.com";
    @SneakyThrows
    public static void getTodos(){
        HttpClient client = Demo.client;//костиль)
        List<User> users  = Demo.getUsers();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        for (User user : users) {
            String uri = String.format(SITE_URL + "/users/%s/todos",user.getId());

            HttpRequest todosRequest = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();

            HttpResponse<String> todosResponse = client.send(todosRequest, HttpResponse.BodyHandlers.ofString());

            if(todosResponse.statusCode() == HttpURLConnection.HTTP_OK){


                List<TodoItem> list = objectMapper.readValue(todosResponse.body(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, TodoItem.class));

                System.out.println(String.format("Todos for user %s (ID %s)",user.getName(),user.getId()));

                list.stream()
                        .filter(element -> element.getCompleted())
                        .forEach(element -> System.out.println(element.getTitle()+"\n"));

                System.out.println("-".repeat(100));
            }
            else{
                System.out.println("ERROR! status code = " + todosResponse.statusCode());
            }
        }
    }
}

@Data
class TodoItem{
    private int userId;
    private int id;
    private String title;
    private boolean completed;

    public boolean getCompleted() {
        return  completed;
    }
}
