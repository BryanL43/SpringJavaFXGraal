package org.custom.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.scene.control.Labeled;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.util.WaitForAsyncUtils.waitFor;

@SpringBootTest
class ApplicationTests extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        if (App.context == null) {
            App.context = new SpringApplicationBuilder(App.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run();
        }

        new App().start(stage);
    }

    @Test
    void testGetRandomItemFlow() throws TimeoutException {
        clickOn("#getRandomItemBtn");

        // Wait until label changes
        waitFor(5, TimeUnit.SECONDS, () -> {
            String text = lookup("#outputLabel")
                .queryAs(Labeled.class)
                .getText();

            return !text.equals("Click the button");
        });

        // Print result
        String text = lookup("#outputLabel")
            .queryAs(Labeled.class)
            .getText();

        System.out.println("Label text: " + text);
    }

    @AfterAll
    static void shutdown() {
        if (App.context != null) {
            App.context.close();
        }
    }
}
