package com.andrew.weating.util.jooq;

import org.jooq.Converter;

public class RatingConverter implements Converter<Integer, Double> {
    private static final double RATING_SCALE = 10;

    @Override
    public Double from(Integer rating) {
        return rating.doubleValue() / RATING_SCALE;
    }

    @Override
    public Integer to(Double rating) {
        return (int) Math.floor(rating * RATING_SCALE);
    }

    @Override
    public Class<Integer> fromType() {
        return Integer.class;
    }

    @Override
    public Class<Double> toType() {
        return Double.class;
    }
}
