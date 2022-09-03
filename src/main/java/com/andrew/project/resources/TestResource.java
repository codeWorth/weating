package com.andrew.project.resources;

import com.andrew.project.session.SessionFromHeader;
import com.andrew.project.session.SessionInfo;
import com.andrew.project.session.SessionToken;
import com.andrew.project.session.SessionTokenResolver;
import com.andrew.project.ws.WebSocketManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestResource {

    @GetMapping(value = "test/user/random", produces = APPLICATION_JSON_VALUE)
    public String helloWorld() {
        return "Hello world!";
    }
}
