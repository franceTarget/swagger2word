package com.servingcloud.synergycloud;

/**
 * Created by dongh on 2018/1/17.
 */

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
public class BackendProvider {

    public static void main(String[] args) {
        SpringApplication.run(BackendProvider.class, args);
    }

}