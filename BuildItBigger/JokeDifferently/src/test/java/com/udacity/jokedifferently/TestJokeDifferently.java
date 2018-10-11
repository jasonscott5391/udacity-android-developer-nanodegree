package com.udacity.jokedifferently;

import org.junit.Test;

public class TestJokeDifferently {

    @Test
    public void testGetAJoke() {
        String joke = JokeDifferently.getAJoke();
        assert joke != null;
        assert !joke.isEmpty();
    }
}
