package exercises;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exercises.Classes.Address;
import exercises.Classes.Company;
import exercises.Classes.Geo;
import exercises.Classes.User;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Demo {
    public static final String SITE_URL = "https://jsonplaceholder.typicode.com";
    public static final HttpClient client = HttpClient.newHttpClient();
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        postUser();
        System.out.println("_".repeat(100));

        updateUser(2);
        System.out.println("_".repeat(100));

        deleteUser(2);
        System.out.println("_".repeat(100));

        getAllInfo();
        System.out.println("_".repeat(100));

        List<User> users = getUsers();
        System.out.println("users = " + users);
        System.out.println("users.size() = " + users.size());
        System.out.println("_".repeat(100));

        getInfoByID(3);
        System.out.println("_".repeat(100));

        getInfoByUsername("Samantha");
        System.out.println("_".repeat(100));

        exercise2.readAndWriteComment();
        System.out.println("_".repeat(100));

        exercise3.getTodos();
        System.out.println("_".repeat(100));

    }

    @SneakyThrows
    private static void postUser(){

        Geo geo = new Geo();
        geo.setLat("1234");
        geo.setLng("5678");

        Address address = new Address();
        address.setCity("Dnipro");
        address.setStreet("8 shev");
        address.setSuite("123sf");
        address.setZipcode("4900");
        address.setGeo(geo);

        Company company = new Company();
        company.setName("Cracra");
        company.setCatchPhrase("Hahaha");
        company.setBs("2344567");

        User newUser = new User();

        newUser.setName("Dima");
        newUser.setUsername("Rest");
        newUser.setEmail("someemeil@gmail.com");
        newUser.setPhone("38099234723");
        newUser.setAddress(address);
        newUser.setCompany(company);
        newUser.setWebsite("somewebsite.com");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String userRequestJson = objectMapper.writeValueAsString(newUser);

        HttpRequest newUserRequest = HttpRequest.newBuilder(new URI(SITE_URL + "/users"))
                .POST(HttpRequest.BodyPublishers.ofString(userRequestJson))
                .header("accept", "application/json")
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> newUserResponse = client.send(newUserRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("status code newUserResponse = " + newUserResponse.statusCode());
    }
    @SneakyThrows
    private static void updateUser(int id){

        Geo geo = new Geo();
        geo.setLat("1234");
        geo.setLng("5678");

        Address address = new Address();
        address.setCity("Dnipro");
        address.setStreet("8 shev");
        address.setSuite("123sf");
        address.setZipcode("4900");
        address.setGeo(geo);

        Company company = new Company();
        company.setName("Cracra");
        company.setCatchPhrase("Hahaha");
        company.setBs("2344567");

        User newUser = new User();

        newUser.setName("Helen");
        newUser.setUsername("Rest");
        newUser.setEmail("someemeil@gmail.com");
        newUser.setPhone("38099234723");
        newUser.setAddress(address);
        newUser.setCompany(company);
        newUser.setWebsite("somewebsite.com");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String newUserJson = objectMapper.writeValueAsString(newUser);

        URI uri = new URI(SITE_URL+"/users/"+id);

        HttpRequest updateRequest = HttpRequest.newBuilder(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(newUserJson))
                .header("accept", "application/json")
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> response = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("status code update = " + response.statusCode());
        System.out.println(response.body());
    }
    @SneakyThrows
    private static void deleteUser(int id){
        URI uri = new URI(SITE_URL + "/users/" + id);

        HttpRequest request = HttpRequest.newBuilder(uri)
                .DELETE()
                .header("accept", "application/json")
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("delete status code = " + response.statusCode());

    }

    @SneakyThrows
    public static void getAllInfo(){
        HttpRequest request = HttpRequest.newBuilder(new URI(SITE_URL+"/users"))
                    .GET()
                    .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("response status = " + response.statusCode());
        System.out.println(response.body());

    }
    @SneakyThrows
    public static List<User> getUsers(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpRequest getUsersRequest = HttpRequest.newBuilder(new URI(SITE_URL + "/users"))
                .GET()
                .build();

        HttpResponse<String> usersResponses= client.send(getUsersRequest, HttpResponse.BodyHandlers.ofString());

        if(usersResponses.statusCode() == HttpURLConnection.HTTP_OK)
        {

            return objectMapper.readValue(usersResponses.body(), objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, User.class));
        }else {
            System.out.println("ERROR! statusCode = " + usersResponses.statusCode());
        }

        return null;
    }

    @SneakyThrows
    public static void getInfoByID(int id){
        String uri = String.format(SITE_URL+"/users/%s",id);
        HttpRequest userRequest = HttpRequest.newBuilder(new URI(uri))
                .GET()
                .build();

        HttpResponse<String> userByID = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
        if(userByID.statusCode() == HttpURLConnection.HTTP_OK){
            System.out.println(userByID.body());
        }else {
            System.out.println("ERROR! status code = " + userByID.statusCode());
        }
    }

    @SneakyThrows
    private static void getInfoByUsername(String userName) {
        String uri = String.format(SITE_URL + "/users?username=%s",userName);

        HttpRequest userRequest = HttpRequest.newBuilder(new URI(uri))
                .GET()
                .build();

        HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
        if(userResponse.statusCode() == HttpURLConnection.HTTP_OK){
            System.out.println(userResponse.body());
        }
        else {
            System.out.println("ERROR! status code = " + userResponse.statusCode());
        }
    }
}
