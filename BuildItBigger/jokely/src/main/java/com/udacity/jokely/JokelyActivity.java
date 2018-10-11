package com.udacity.jokely;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokelyActivity extends AppCompatActivity {

    public static final String EXTRA_JOKE_KEY = "joke";

    private TextView mJokeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokely);

        mJokeTextView = findViewById(R.id.joke_text_view);
        Intent intent = getIntent();
        Bundle extras;
        if (intent != null
                && (extras = intent.getExtras()) != null) {
            String joke = extras.getString(EXTRA_JOKE_KEY, "No jokes today...");
            mJokeTextView.setText(joke);
        }
    }
}
