package com.andrew.weating;

import com.andrew.weating.util.Hasher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({
        ApplicationContext.class
})
@SpringBootConfiguration
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        System.out.println(Hasher.hash("asdjkhaskjdahs"));
//        SpringApplication.run(Application.class, args);
    }

}
