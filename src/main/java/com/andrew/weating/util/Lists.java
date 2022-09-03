package com.andrew.weating.util;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

public class Lists {

    public static <T> List<T> merge(List<T> a, List<T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(toList());
    }
}
