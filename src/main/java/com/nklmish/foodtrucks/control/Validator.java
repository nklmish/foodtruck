package com.nklmish.foodtrucks.control;

public interface Validator<T> {

    boolean validate(T param);

}
