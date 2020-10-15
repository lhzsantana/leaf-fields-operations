package com.leaf.samples.operations.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "address",
    "apiOwnerUsername",
    "climateFieldViewCredentials",
    "cnhiCredentials",
    "email",
    "id",
    "name",
    "phone",
})
public class LeafUser {

    @JsonProperty("address")
    private String address;
    @JsonProperty("apiOwnerUsername")
    private String apiOwnerUsername;
    @JsonProperty("climateFieldViewCredentials")
    private ClimateFieldViewCredentials climateFieldViewCredentials;

    @JsonProperty("email")
    private String email;
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
    @JsonProperty("phone")
    private String phone;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("apiOwnerUsername")
    public String getApiOwnerUsername() {
        return apiOwnerUsername;
    }

    @JsonProperty("apiOwnerUsername")
    public void setApiOwnerUsername(String apiOwnerUsername) {
        this.apiOwnerUsername = apiOwnerUsername;
    }

    @JsonProperty("climateFieldViewCredentials")
    public ClimateFieldViewCredentials getClimateFieldViewCredentials() {
        return climateFieldViewCredentials;
    }

    @JsonProperty("climateFieldViewCredentials")
    public void setClimateFieldViewCredentials(ClimateFieldViewCredentials climateFieldViewCredentials) {
        this.climateFieldViewCredentials = climateFieldViewCredentials;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}