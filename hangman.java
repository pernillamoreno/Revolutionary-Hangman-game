package com.example.revolutionary_hangman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Hangman extends Application {
    private char pressedKey;

    static int amountPlayers;
    static int gameMode;
    static int disabledRounds;
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    private int turn = 0;

    private ArrayList<Integer> order = new ArrayList<>();
    Random random = new Random();


    private ArrayList<Player> playersList = new ArrayList<>();
    private Group playersDrawing = new Group();

    private Stage stage = new Stage();
    int playerIndex = 0;

    // Settings Group
    private Group settingsGroup = createSettingsGroup();

    // Choose word group
    private Group chooseWordGroup = createChooseWordGroup();

    //Scene for settings
    private Scene settingScene = new Scene(settingsGroup, WIDTH, HEIGHT);

    //Scene for creating word
    private Scene chooseWordScene = new Scene(chooseWordGroup, WIDTH, HEIGHT);

    // Scene for playing
    private Scene playScene = new Scene(playersDrawing, WIDTH, HEIGHT);
    private static char forbiddenVowel;
    public static char getForbiddenVowel() {
        return forbiddenVowel;
    }


    @Override
    public void start(Stage startStage) throws IOException {

        stage = startStage;



        createSettingsGroup();
        createChooseWordGroup();
        createPlaySceneGroup();


        chooseWordScene.setFill(Color.WHITE);
        chooseWordScene = sceneSetKeyPress(chooseWordScene);

        playScene.setFill(Color.WHITE);
        playScene = sceneSetKeyPress(playScene);

        chooseWordScene.setFill(Color.WHITE);
        chooseWordScene = sceneSetKeyPress(chooseWordScene);


        run();

        // Stage
        stage.setTitle("Hangman!");
        stage.setScene(settingScene);
        stage.show();
    }

    public void run() {
        // Timeline is the runs every 0.2 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), ev -> {

            updateDrawings();
            playScene.setRoot(playersDrawing);

        }));
        // Runs the timeline forever
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch();
    }

    public Scene sceneSetKeyPress(Scene scene) {
        //Records the key press and stores it in pressedKey
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            pressedKey = key.getText().charAt(0);
        });

        return scene;
    }

    public ArrayList<Integer> randomiseWhoToGuess(int players) {
        // Store the order
        ArrayList<Integer> order = new ArrayList<>();
        Boolean isDone = false;
        while (!isDone) {
            int randomNumber = random.nextInt(players);
            boolean foundNumber = false;
            // Checks if the random number already exist
            for (int i = 0; i < order.size(); i++) {
                if (order.get(i) == randomNumber || randomNumber == order.size()+1) {
                    foundNumber = true;
                }
            }

            // If the number doesn't exist, add it to the order list
            if (!foundNumber) {
                order.add(randomNumber);
            }
            // If the list is full
            if (order.size() == players) {
                isDone = true;
            }
        }

        return order;

    }

    public char randomizeForbiddenVowel() {
        Random random = new Random();
        char[] forbiddenVowels = {'a', 'e', 'i', 'o', 'u', 'y', 'å', 'ä', 'ö'};
        // Randomize which index of the character that will be forbidden.
        int forbiddenVowelIndex = random.nextInt(forbiddenVowels.length - 1);
        System.out.println("Banned vowel is: " + forbiddenVowels[forbiddenVowelIndex]);
        return forbiddenVowels[forbiddenVowelIndex];
    }

    public ArrayList<Player> createPlayers(int width, int height, int players) {
        ArrayList<Player> playersList = new ArrayList<>();
        int newXPosition = 0;
        int playerWidth = width / players;

        // Creates the number of players and add their new x position
        for (int i = 0; i < players; i++) {
            Player player = new Player(newXPosition, 0, playerWidth, height);
            player.createDrawing();
            playersDrawing.getChildren().addAll(player.getDrawing());
            playersList.add(player);
            newXPosition += playerWidth;
        }

        return playersList;
    }

    public Group createSettingsGroup() {
        Group settingsGroup = new Group();

        RadioButton buttonAmountPlayers2 = new RadioButton("Amount Players  2");
        RadioButton buttonAmountPlayers3 = new RadioButton("Amount Players  3");
        RadioButton buttonAmountPlayers4 = new RadioButton("Amount Players  4");
        buttonAmountPlayers2.setLayoutX(250);
        buttonAmountPlayers2.setLayoutY(110);
        buttonAmountPlayers3.setLayoutX(250);
        buttonAmountPlayers3.setLayoutY(130);
        buttonAmountPlayers4.setLayoutX(250);
        buttonAmountPlayers4.setLayoutY(150);
        buttonAmountPlayers2.setOnAction(e -> playersList = createPlayers(WIDTH, HEIGHT, 2));
        buttonAmountPlayers3.setOnAction(e -> playersList = createPlayers(WIDTH, HEIGHT, 3));
        buttonAmountPlayers4.setOnAction(e -> playersList = createPlayers(WIDTH, HEIGHT, 4));

        ToggleGroup amountButtons = new ToggleGroup();
        buttonAmountPlayers2.setToggleGroup(amountButtons);
        buttonAmountPlayers3.setToggleGroup(amountButtons);
        buttonAmountPlayers4.setToggleGroup(amountButtons);

        RadioButton buttonKey0 = new RadioButton("Selection Mode");
        RadioButton buttonKey1 = new RadioButton("Random Mode");
        RadioButton buttonKey2 = new RadioButton("Queue Mode");
        buttonKey0.setLayoutX(250);
        buttonKey0.setLayoutY(180);
        buttonKey1.setLayoutX(250);
        buttonKey1.setLayoutY(200);
        buttonKey2.setLayoutX(250);
        buttonKey2.setLayoutY(220);
        buttonKey0.setOnAction(e -> gameMode = 0);
        buttonKey1.setOnAction(e -> gameMode = 1);
        buttonKey2.setOnAction(e -> gameMode = 2);

        ToggleGroup modeButtons = new ToggleGroup();
        buttonKey0.setToggleGroup(modeButtons);
        buttonKey1.setToggleGroup(modeButtons);
        buttonKey2.setToggleGroup(modeButtons);

        RadioButton buttonSetDisabledRounds1 = new RadioButton("Random vowel will be banned for 1 round");
        RadioButton buttonSetDisabledRounds2 = new RadioButton("Random vowel will be banned for 2 rounds");
        RadioButton buttonSetDisabledRounds3 = new RadioButton("Random vowel will be banned for 3 rounds");
        buttonSetDisabledRounds1.setLayoutX(550);
        buttonSetDisabledRounds1.setLayoutY(110);
        buttonSetDisabledRounds2.setLayoutX(550);
        buttonSetDisabledRounds2.setLayoutY(130);
        buttonSetDisabledRounds3.setLayoutX(550);
        buttonSetDisabledRounds3.setLayoutY(150);
        buttonSetDisabledRounds1.setOnAction(e -> disabledRounds = 0);
        buttonSetDisabledRounds2.setOnAction(e -> disabledRounds = 1);
        buttonSetDisabledRounds3.setOnAction(e -> disabledRounds = 2);

        ToggleGroup bannedVowelBut = new ToggleGroup();
        buttonSetDisabledRounds1.setToggleGroup(bannedVowelBut);
        buttonSetDisabledRounds2.setToggleGroup(bannedVowelBut);
        buttonSetDisabledRounds3.setToggleGroup(bannedVowelBut);

        settingsGroup.getChildren().add(buttonKey0);
        settingsGroup.getChildren().add(buttonKey1);
        settingsGroup.getChildren().add(buttonKey2);
        settingsGroup.getChildren().add(buttonSetDisabledRounds1);
        settingsGroup.getChildren().add(buttonSetDisabledRounds2);
        settingsGroup.getChildren().add(buttonSetDisabledRounds3);
        settingsGroup.getChildren().add(buttonAmountPlayers2);
        settingsGroup.getChildren().add(buttonAmountPlayers3);
        settingsGroup.getChildren().add(buttonAmountPlayers4);

        Button choseWordButton = new Button("Chose Word");
        choseWordButton.setLayoutX(550);
        choseWordButton.setLayoutY(190);
        choseWordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(chooseWordScene);
            }
        });
        settingsGroup.getChildren().add(choseWordButton);

        Button startButton = new Button("Start");
        startButton.setLayoutX(550);
        startButton.setLayoutY(240);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stage.setScene(playScene);
            }
        });
        settingsGroup.getChildren().add(startButton);

        return settingsGroup;
    }

    public void updateDrawings(){
        playersDrawing = new Group();

        for (Player player:playersList) {
            player.updateText();
            playersDrawing.getChildren().addAll(player.getDrawing());
        }

        //Update the
        playersDrawing.getChildren().addAll(createPlaySceneGroup());
    }


    public Group createChooseWordGroup() {

        Group chooseWordGroup = new Group();
        TextField chooseWord = new TextField();
        chooseWord.setLayoutX(550);
        chooseWord.setLayoutY(90);

        chooseWordGroup.getChildren().add(chooseWord);

        Button enterWordButton = new Button("Enter your word");
        enterWordButton.setLayoutX(550);
        enterWordButton.setLayoutY(130);
        enterWordButton.setOnAction(actionEvent -> {
            String word = chooseWord.getText();
            Player player = playersList.get(playerIndex);
            player.setWord(word);
            player.updateText();

            if (playerIndex == playersList.size() - 1) {
                changeToPlay();
            }
            chooseWord.clear();
            playerIndex++;                                  // next player be able to enter word
            randomizeForbiddenVowel();
        });
        chooseWordGroup.getChildren().add(enterWordButton);

        return chooseWordGroup;
    }

    public void changeToPlay() {
        // when the last player entered word, scene is changing to playScene
        stage.setScene(playScene);
    }

    public Group createPlaySceneGroup() {

        Group playSceneGroup = new Group();

        Button nextMatchButton = new Button("Guess");
        nextMatchButton.setLayoutX(500);
        nextMatchButton.setLayoutY(400);
        nextMatchButton.setTextFill(Color.FIREBRICK);
        nextMatchButton.setOnAction(event -> play());


        playSceneGroup.getChildren().add(nextMatchButton);


        return playSceneGroup;


    }

    public void play(){
        // If there is no order
        if (order.isEmpty())
        {
            order = randomiseWhoToGuess(playersList.size());
        }
        //If there is a players turn
        if (turn < playersList.size())
        {
            //Look for the player who gets guessed on and check if it is not the right character
            if (!playersList.get(order.get(turn)).checkForCharacter(pressedKey))
            {
                //Sets the player that guessed wrong to start getting hanged
                playersList.get(turn).wrongWord();
            }
            //If all the players have guessed the button should change text to "Next round"
            if (turn == playersList.size()-1)
            {

            }
            turn++;
        }
        //If it is new round, get a new order and the button should change text to "Guess"
        if (turn == playersList.size())
        {
            order = randomiseWhoToGuess(playersList.size());
            turn = 0;
        }

    }

}
