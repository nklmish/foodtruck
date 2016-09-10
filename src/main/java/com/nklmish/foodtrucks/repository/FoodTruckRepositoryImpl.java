package com.nklmish.foodtrucks.repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.nklmish.foodtrucks.entity.FoodTruck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FoodTruckRepositoryImpl implements FoodTruckRepository {

    public static final String FOOD_ITEMS_FIELD_NAME = "foodItems";
    private BaseRepo repo;

    private MongoTemplate template;

    @Autowired
    public FoodTruckRepositoryImpl(BaseRepo repo, MongoTemplate template) {
        this.repo = repo;
        this.template = template;
    }

    @Override
    public <S extends FoodTruck> List<S> save(Iterable<S> entites) {
        List<S> savedItems = new ArrayList<>();
        entites.forEach(entity -> savedItems.add(save(entity)));
        return savedItems;
    }

    @Override
    public <S extends FoodTruck> S save(S entity) {
        FoodTruck existing = findByApplicantAndAddress(entity.getApplicant(), entity.getAddress());

        if (existing != null) {
            entity.setId(existing.getId());
        }

        repo.save(entity);

        return entity;
    }

    @Override
    public FoodTruck findOne(String s) {
        return repo.findOne(s);
    }

    @Override
    public boolean exists(String s) {
        return repo.exists(s);
    }

    @Override
    public List<FoodTruck> findAll() {
        return repo.findAll();
    }

    @Override
    public Iterable<FoodTruck> findAll(Iterable<String> strings) {
        return repo.findAll(strings);
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public void delete(String s) {
        repo.delete(s);
    }

    @Override
    public void delete(FoodTruck entity) {
        repo.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends FoodTruck> entities) {
        repo.delete(entities);
    }

    @Override
    public void deleteAll() {
        repo.deleteAll();
    }

    @Override
    public List<FoodTruck> findAll(Sort sort) {
        return repo.findAll(sort);
    }

    @Override
    public Page<FoodTruck> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public <S extends FoodTruck> List<S> insert(Iterable<S> entities) {
        return repo.insert(entities);
    }

    @Override
    public <S extends FoodTruck> S insert(S entity) {
        return repo.insert(entity);
    }

    @Override
    public Page<FoodTruck> findByLocationNear(Point point, Distance distance, Pageable pageable) {
        return repo.findByLocationNear(point, distance, pageable);
    }

    @Override
    public FoodTruck findByApplicant(String applicant) {
        return repo.findByApplicant(applicant);
    }

    @Override
    public FoodTruck findByApplicantAndAddress(String applicant, String address) {
        return repo.findByApplicantAndAddress(applicant, address);
    }

    @Override
    public List<String> findDistinctFoodItems() {
        List distinct = template.getCollection(getCollectionName()).distinct(FOOD_ITEMS_FIELD_NAME);
        List<String> foodItems = new ArrayList<>();
        distinct.stream().map(Object::toString).forEach(o -> foodItems.add((String) o));
        return foodItems;
    }

    @Override
    public List<FoodTruck> findByLocationNear(Point point, Distance distance, List<String> foodItems) {
        DBObject text = getTextDbObject(foodItems);
        DBObject location = getLocationDbObject(point, distance);

        Map<Object, Object> map = new HashMap<>();
        map.put("$text", text);
        map.put("location", location);

        DBCursor cursor = template.getCollection(getCollectionName()).find(new BasicDBObject(map));
        return toFoodTrucks(cursor);
    }

    private List<FoodTruck> toFoodTrucks(DBCursor cursor) {
        List<FoodTruck> foodTrucks = new ArrayList<>();

        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            FoodTruck foo = template.getConverter().read(FoodTruck.class, obj);
            foodTrucks.add(foo);
        }
        return foodTrucks;
    }

    private DBObject getLocationDbObject(Point point, Distance distance) {
        BasicDBList geoData = new BasicDBList();
        BasicDBList coordinates = new BasicDBList();
        coordinates.add(point.getX());
        coordinates.add(point.getY());
        geoData.add(coordinates);
        geoData.add(distance.getNormalizedValue());

        return new BasicDBObject("$geoWithin", new BasicDBObject("$centerSphere", geoData));
    }

    private DBObject getTextDbObject(List<String> foodItems) {
        StringBuilder textPhrases = new StringBuilder();
        foodItems.stream().forEach(textPhrases::append);
        return new BasicDBObject("$search", textPhrases.toString());
    }

    private String getCollectionName() {
        return template.getCollectionName(FoodTruck.class);
    }
}
