package com.udacity.jokedifferently;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokeDifferently {

    private static final String CHUCK_JOKES_PATH = "src/main/res/chuck-jokes.txt";
    private static final List<String> CHUCK_JOKES = new ArrayList<>();

    static {
        Path chuckJokesPath = Paths.get(CHUCK_JOKES_PATH);
        try (BufferedReader bufferedReader = Files.newBufferedReader(chuckJokesPath)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                CHUCK_JOKES.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static String getAJoke() {
        return CHUCK_JOKES.get(new Random().nextInt(CHUCK_JOKES.size()));
    }
}
