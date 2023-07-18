package matvey.springtodolist;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class SpringTodoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTodoListApplication.class, args);
    }

}
