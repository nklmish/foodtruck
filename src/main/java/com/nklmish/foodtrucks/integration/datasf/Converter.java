package com.nklmish.foodtrucks.integration.datasf;

import java.util.ArrayList;
import java.util.List;

public interface Converter<Output, Input> {

    Output convert(Input input);

    default List<Output> convert(List<Input> inputs) {
        final List<Output> outputs = new ArrayList<>();

        inputs.forEach(dto -> outputs.add(convert(dto)));

        return outputs;
    }

}
