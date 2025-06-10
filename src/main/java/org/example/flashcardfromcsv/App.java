package org.example.flashcardfromcsv;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App extends Application {
    private List<FlashCard> flashCards = new ArrayList<>();
    private int currentCardIndex = 0;
    private TextArea questionArea;
    private Button showAnswerButton;
    private TextArea answerArea;
    private Button nextButton;
    private Button restartButton;
    private Label progressLabel;
    private final static int FONT_SIZE = 20;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flashcard App");

        Button loadButton = new Button("Load CSV");
        loadButton.setOnAction(e -> loadCSV(primaryStage));

        questionArea = new TextArea("Question will appear here");
        questionArea.setFont(new Font("Arial", FONT_SIZE));
        questionArea.setWrapText(true);
        questionArea.setEditable(false);
        questionArea.setPrefHeight(100);

        showAnswerButton = new Button("Show Answer");
        showAnswerButton.setOnAction(e -> showAnswer());
        showAnswerButton.setDisable(true);
        showAnswerButton.setFont(new Font("Arial", FONT_SIZE));

        answerArea = new TextArea();
        answerArea.setFont(new Font("Arial", FONT_SIZE));
        answerArea.setWrapText(true);
        answerArea.setEditable(false);
        answerArea.setPrefHeight(250);

        nextButton = new Button("Next");
        nextButton.setOnAction(e -> showNextCard());
        nextButton.setDisable(true);
        nextButton.setFont(new Font("Arial", FONT_SIZE));

        restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restart());
        restartButton.setDisable(true);
        restartButton.setFont(new Font("Arial", FONT_SIZE));

        progressLabel = new Label("Card 1 of 10");
        progressLabel.setFont(new Font("Arial", 14));

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(10));
        topHBox.setAlignment(Pos.CENTER); // Aligns content to the right
        topHBox.getChildren().addAll(loadButton, showAnswerButton, nextButton);

//        VBox layout = new VBox(10);
//        layout.getChildren().addAll(loadButton, questionArea, showAnswerButton, answerArea, nextButton, restartButton);

        BorderPane root = new BorderPane();
        root.setTop(topHBox);

        // Create a VBox for the main content
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(10));
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().addAll(
                questionArea,
                answerArea,
                restartButton
        );

        root.setCenter(mainContent);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadCSV(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            flashCards.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String[] parts = line.split("\\|");
//                    if (parts.length == 2) {
//                        flashCards.add(new FlashCard(parts[0], parts[1]));
//                    }
//                }
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("|")) {
                        content.append("|").append(line);
                    } else {
                        content.append("\n").append(line);
                    }
                }

                String[] lines = content.substring(1).split("\\|");
                for (int i = 2; i + 1 < lines.length; i += 2) {
                    String question = lines[i].trim();
                    String answer = lines[i + 1].trim().replace("\"", "");
                    flashCards.add(new FlashCard(question, answer));
                }

                if (!flashCards.isEmpty()) {
                    Collections.shuffle(flashCards);
                    currentCardIndex = 0;
                    showCard();
                    showAnswerButton.setDisable(false);
                    nextButton.setDisable(false);
                    restartButton.setDisable(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showCard() {
        if (currentCardIndex < flashCards.size()) {
            FlashCard card = flashCards.get(currentCardIndex);
            questionArea.setText(card.getQuestion());
            answerArea.setText("");
        } else {
            questionArea.setText("You've completed all flashcards!");
            answerArea.setText("");
            showAnswerButton.setDisable(true);
            nextButton.setDisable(true);
        }
    }

    private void showAnswer() {
        if (currentCardIndex < flashCards.size()) {
            answerArea.setText(flashCards.get(currentCardIndex).getAnswer());
        }
    }

    private void showNextCard() {
        currentCardIndex++;
        if (currentCardIndex < flashCards.size()) {
            showCard();
        } else {
            questionArea.setText("You've completed all flashcards!");
            answerArea.setText("");
            showAnswerButton.setDisable(true);
            nextButton.setDisable(true);
        }
    }

    private void restart() {
        Collections.shuffle(flashCards);
        currentCardIndex = 0;
        showAnswerButton.setDisable(false);
        nextButton.setDisable(false);
        showCard();
    }

    public static void main(String[] args) {
        launch();
    }
}