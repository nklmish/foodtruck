package com.nklmish.foodtrucks.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "foodtrucks")
public class FoodTruck {

    @Id
    private String id;
    private String applicant;
    private String address;
    private String daysHours;
    @TextIndexed
    private List<String> foodItems;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;
    private String locationDescription;
    private String schedule;
    private Status status;

    public enum Status {
        APPROVED ("approved"), REQUESTED("requested"),
        SUSPEND("suspend"), EXPIRED("expired"), UNKNOWN("unknown");

        private final String code;

        Status(String code) {
            this.code = code;
        }

        private static final Map<String, Status> CODE_MAP = new HashMap<>();

        static {
            Arrays.stream(Status.values()).forEach(status -> CODE_MAP.put(status.code, status));
        }

        public static Status getByCode(String string) {
            Status status = CODE_MAP.get(string.toLowerCase());
            return status != null ? status : UNKNOWN;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDaysHours() {
        return daysHours;
    }

    public void setDaysHours(String daysHours) {
        this.daysHours = daysHours;
    }

    public List<String> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<String> foodItems) {
        this.foodItems = foodItems;
    }

    @Override
    public String toString() {
        return "FoodTruck{" +
                "id='" + id + '\'' +
                ", location=" + location +
                ", address='" + address + '\'' +
                ", applicant='" + applicant + '\'' +
                ", schedule='" + schedule + '\'' +
                ", status='" + status + '\'' +
                ", daysHours='" + daysHours + '\'' +
                ", foodItems='" + foodItems + '\'' +
                '}';
    }
}
