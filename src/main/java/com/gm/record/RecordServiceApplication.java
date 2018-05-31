package com.gm.record;

import com.gm.record.loader.RecordDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;
import org.stagemonitor.core.Stagemonitor;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static org.stagemonitor.web.servlet.initializer.ServletContainerInitializerUtil.registerStagemonitorServletContainerInitializers;

@SpringBootApplication
@EnableAutoConfiguration
public class RecordServiceApplication implements CommandLineRunner {

    @Autowired
    private RecordDataLoader loader;

    public static void main(String[] args) {
        Stagemonitor.init();
        SpringApplication.run(RecordServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        loader.loadRecordsFromCsv();
    }

    @Component
    public static class StagemonitorInitializer implements ServletContextInitializer {

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            // necessary for spring boot 2.0.0 until stagemonitor supports it natively
            registerStagemonitorServletContainerInitializers(servletContext);
        }
    }
}
