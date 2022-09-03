package com.andrew.weating.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RoomResource {

    @GetMapping(value = "room", produces = APPLICATION_JSON_VALUE)
    public RoomView newRoom() {
        return new RoomView(UUID.randomUUID().toString());
    }

    @Value
    static class RoomView {
        String roomId;
    }
}
