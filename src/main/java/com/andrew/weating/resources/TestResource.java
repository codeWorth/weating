package com.andrew.weating.resources;

import com.andrew.weating.session.SessionFromHeader;
import com.andrew.weating.session.SessionInfo;
import com.andrew.weating.session.SessionToken;
import com.andrew.weating.session.SessionTokenResolver;
import com.andrew.weating.ws.WebSocketManager;
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
