package exercises;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exercises.Classes.User;
import lombok.Data;
import lombok.SneakyThrows;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class exercise2 {
    private static final String SITE_URL= "https://jsonplaceholder.typicode.com";
    @SneakyThrows
    public static void readAndWriteComment(){
        HttpClient client = Demo.client;
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<User> users = Demo.getUsers();

        for (User user : users) {
            URI uri = new URI(SITE_URL + "/users/"+user.getId()+"/posts");

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();

            HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());

            List<Post> posts = objectMapper.readValue(responsePost.body(),objectMapper.getTypeFactory()
                    .constructCollectionType(List.class,Post.class));

            Long lastPostIndex = posts.stream()
                    .map(element -> element.getId())
                    .sorted()
                    .toList()
                    .getLast();

            HttpRequest commentsRequest = HttpRequest.newBuilder(new URI(SITE_URL + "/posts/" + lastPostIndex + "/comments"))
                    .GET()
                    .build();

            HttpResponse<String> commentsResponse = client.send(commentsRequest, HttpResponse.BodyHandlers.ofString());

            jsonWriter(user.getId(),lastPostIndex,commentsResponse.body());
        }
    }

    private static void jsonWriter(Long userId,Long postNumber,String comennts){
        try (FileOutputStream fos = new FileOutputStream("user-"+userId+"-post-"+postNumber+"-comments.json");
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));){
            bufferedWriter.write(comennts);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
@Data
class Post{
    private Long userId;
    private Long id;
    private String title;
    private String body;
}

@Data
class Comment{
    private String postId;
    private Long id;
    private String name;
    private String email;
    private String body;
}
