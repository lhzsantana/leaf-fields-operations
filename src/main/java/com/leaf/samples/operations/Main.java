package com.leaf.samples.operations;

import com.google.gson.Gson;
import com.leaf.samples.operations.models.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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

        ClimateFieldViewCredentials climateFieldViewCredentials  = main.authenticateWithCFV();
        climateFieldViewCredentials =   main.createCFVCredentials(climateFieldViewCredentials, token);
        LeafUser leafUser = main.createLeafUser(token, climateFieldViewCredentials);
        String leafUserId=leafUser.getId();

        List<String> ids = new ArrayList<>();

        for(Operation operation : main.getOperations(token, leafUserId)) {
            ids.add(operation.getId());
        }

        main.mergeOperations(token, ids);

        for(Operation operation : main.getOperations(token, leafUserId)) {

            Image [] images = main.getImages(token, operation.getId());

            for(Image image : images) {
                System.out.println(image.getUrl());
            }
        }

    }

    private ClimateFieldViewCredentials authenticateWithCFV(){

        ClimateFieldViewCredentials climateFieldViewCredentials = new ClimateFieldViewCredentials();
        climateFieldViewCredentials.setApiKey("");
        climateFieldViewCredentials.setRefreshToken("");
        climateFieldViewCredentials.setClientId("");
        climateFieldViewCredentials.setClientSecret("");

        return climateFieldViewCredentials;
    }

    private ClimateFieldViewCredentials createCFVCredentials(ClimateFieldViewCredentials climateFieldViewCredentials, String token) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/usermanagement/api/climate-field-view-credentials"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+token)
            .POST(HttpRequest.BodyPublishers.ofString((new Gson()).toJson(climateFieldViewCredentials))).build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), ClimateFieldViewCredentials.class);
    }

    private LeafUser createLeafUser(String token, ClimateFieldViewCredentials climateFieldViewCredentials) throws IOException, InterruptedException {

        LeafUser leafUser = new LeafUser();
        leafUser.setEmail("testeste@test.com");
        leafUser.setName("name");
        leafUser.setClimateFieldViewCredentials(climateFieldViewCredentials);

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/usermanagement/api/users"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+token)
            .POST(HttpRequest.BodyPublishers.ofString((new Gson()).toJson(leafUser))).build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), LeafUser.class);
    }

    private List<Operation> getOperations(String token, String leafUserId) throws IOException, InterruptedException {

        Operations operations;

        do{
            Thread.sleep(100000);

            HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/operations/api/files?operationType=harvested&provider=ClimateFieldView&leafUserId="+leafUserId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token)
                .GET().build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

             operations = (new Gson()).fromJson(response.body(), Operations.class);

        }while (operations.getOperations().size()==0);

        return operations.getOperations();
    }

    private Operation mergeOperations(String token, List<String> ids) throws IOException, InterruptedException {

        Merge merge = new Merge();
        merge.setIds(ids);

        HttpRequest request = HttpRequest.newBuilder(URI.create(api+"/services/operations/api/files/merge"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+token)
            .POST(HttpRequest.BodyPublishers.ofString((new Gson()).toJson(merge))).build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        return (new Gson()).fromJson(response.body(), Operation.class);
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
