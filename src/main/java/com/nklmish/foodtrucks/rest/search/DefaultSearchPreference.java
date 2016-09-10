package com.nklmish.foodtrucks.rest.search;

public class DefaultSearchPreference {

    private final String name;
    private final Object value;

    public DefaultSearchPreference(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
