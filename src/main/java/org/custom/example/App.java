package org.custom.example;

import java.util.Objects;
import java.util.Random;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import org.custom.example.entity.Item;
import org.custom.example.repository.ItemRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class App extends Application {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static ConfigurableApplicationContext context;

    private static final String ASSETS_DIR = "/assets/";
    private static final String APP_ICON_PATH = Objects.requireNonNull(
        App.class.getResource(ASSETS_DIR + "icons/app-icon.png")
    ).toExternalForm();

    @Override
    public void init() throws Exception {
        super.init();
    }

    public static void main(String[] args) {
        context = new SpringApplicationBuilder(App.class)
            .headless(false)
            .web(WebApplicationType.NONE)
            .run(args);

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        var scene = new Scene(createTestPane(), 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(
            getClass().getResource(ASSETS_DIR + "index.css")).toExternalForm());

        stage.setScene(scene);

        stage.setTitle(context.getEnvironment().getProperty("spring.application.name", "Application"));
        stage.getIcons().add(new Image(APP_ICON_PATH));
        stage.setOnCloseRequest(t -> Platform.exit());
        stage.setMaxWidth(1280);
        stage.setMaxHeight(900);
        stage.show();
        stage.requestFocus();
    }

    @Override
    public void stop() {
        if (context != null) {
            context.close();
        }
        Platform.exit();
    }

    private Pane createTestPane() {
        var root = new VBox();
        root.getStyleClass().add("main");
        root.setAlignment(Pos.CENTER);

        Label outputLabel = new Label("Click the button");
        Button button = new Button("Get Random Item");

        ItemRepository repo = context.getBean(ItemRepository.class);

        button.setOnAction(e -> {
            button.setDisable(true);

            Task<Item> task = new Task<>() {
                @Override
                protected Item call() {
                    var items = repo.findAll();
                    if (items.isEmpty()) {
                        return null;
                    }

                    int index = new Random().nextInt(items.size());
                    return items.get(index);
                }
            };

            task.setOnSucceeded(event -> {
                Item item = task.getValue();
                outputLabel.setText(
                    item != null ? item.getItemDescription() : "No items found"
                );
                button.setDisable(false);
            });

            task.setOnFailed(event -> {
                logger.error("Error fetching item", task.getException());
                outputLabel.setText("Error!");
                button.setDisable(false);
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

        root.getChildren().addAll(
            new FontIcon(MaterialDesignS.SCHOOL),
            button,
            outputLabel
        );

        return root;
    }
}
