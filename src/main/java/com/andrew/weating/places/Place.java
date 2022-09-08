package com.andrew.weating.places;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.andrew.weating.util.jooq.JsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;
import lombok.With;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Value
public class Place {

    String id;
    @With
    Instant refreshAt;
    @With
    Geometry geometry;

    @Data
    public static class Geometry {
        LatLng location;
        Viewport viewport;

        @Value
        public static class Viewport {
            LatLng northeast;
            LatLng southwest;
        }

        @Value
        public static class LatLng {
            String lat;
            String lng;
        }

        public static class Converter extends JsonConverter<Geometry> {}
    }
}
