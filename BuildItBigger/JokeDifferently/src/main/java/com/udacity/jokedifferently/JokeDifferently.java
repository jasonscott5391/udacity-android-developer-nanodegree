package com.udacity.jokedifferently;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokeDifferently {

    private static final String CHUCK_JOKES_PATH = "chuck-jokes.txt";
    private static final List<String> CHUCK_JOKES = new ArrayList<>();

    static {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(JokeDifferently.class.getClassLoader().getResourceAsStream(CHUCK_JOKES_PATH)))) {

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
