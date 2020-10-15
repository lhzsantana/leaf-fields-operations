package com.leaf.samples.operations;

import com.google.gson.Gson;
import com.leaf.samples.operations.models.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Main {

    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    String api = "https://api.withleaf.io";
    //CHANGE IT
    String username = "";
    String password = "";

    public static void main(String [] args) throws IOException, InterruptedException {

        Main main = new Main();

        String token = main.authenticate();
        ClimateFieldViewCredentials climateFieldViewCredentials =   main.createCFVCredentials();
        main.createLeafUser(climateFieldViewCredentials);

        for(Operation operation : main.getStandardGeoJSON(token)) {

            Image [] images = main.getImages(token, operation.getId());

            for(Image image : images) {
                System.out.println(image.getUrl());
            }
        }
    }

    private ClimateFieldViewCredentials createCFVCredentials() throws IOException, InterruptedException {

        ClimateFieldViewCredentials climateFieldViewCredentials = new ClimateFieldViewCredentials();
        climateFieldViewCredentials.setApiKey("");
        climateFieldViewCredentials.setRefreshToken("");
        climateFieldViewCredentials.setClientId("");
        climateFieldViewCredentials.setClientSecret("");

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/usermanagement/api/climate-field-view-credentials"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString((new Gson()).toJson(climateFieldViewCredentials))).build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), ClimateFieldViewCredentials.class);
    }

    private String createLeafUser(ClimateFieldViewCredentials climateFieldViewCredentials) throws IOException, InterruptedException {

        LeafUser leafUser = new LeafUser();
        leafUser.setEmail("testeste@test.com");
        leafUser.setClimateFieldViewCredentials(climateFieldViewCredentials);

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/usermanagement/api/users"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString((new Gson()).toJson(leafUser))).build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), Token.class).getId_token();
    }

    private List<Operation> getStandardGeoJSON(String token) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/operations/api/files?page=0&size=3&status=processed"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+token)
            .GET().build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), Operations.class).getOperations();
    }

    private Image[] getImages(String token, String id) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/operations/api/files/"+id+"/images"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+token)
            .GET().build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), Image[].class);
    }

    private String authenticate() throws IOException, InterruptedException {

        Authentication authentication = new Authentication();
        authentication.setUsername(username);
        authentication.setPassword(password);

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/api/authenticate"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString((new Gson()).toJson(authentication))).build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), Token.class).getId_token();
    }
}
