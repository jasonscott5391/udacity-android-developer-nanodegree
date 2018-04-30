package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        ImageView imageView = findViewById(R.id.sandwich_image);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(imageView);

        if (sandwich.getAlsoKnownAs() != null
                && !sandwich.getAlsoKnownAs().isEmpty()) {
            StringBuilder akaStringBuilder = new StringBuilder();
            for (String aka : sandwich.getAlsoKnownAs()) {
                akaStringBuilder.append(String.format("%s, ", aka));
            }
            akaStringBuilder.replace(akaStringBuilder.lastIndexOf(","), akaStringBuilder.length(), "");
            ((TextView) findViewById(R.id.sandwich_aka)).setText(akaStringBuilder.toString());
        } else {
            findViewById(R.id.sandwich_aka_label).setVisibility(View.INVISIBLE);
        }

        if (sandwich.getPlaceOfOrigin() != null
                && !sandwich.getPlaceOfOrigin().isEmpty()) {
            ((TextView) findViewById(R.id.sandwich_origin)).setText(sandwich.getPlaceOfOrigin());
        } else {
            findViewById(R.id.sandwich_origin_label).setVisibility(View.INVISIBLE);
        }

        ((TextView) findViewById(R.id.sandwich_description)).setText(sandwich.getDescription());

        StringBuilder ingredientsStringBuilder = new StringBuilder();
        for (String ingredient : sandwich.getIngredients()) {
            ingredientsStringBuilder.append(String.format("\t* %s\n", ingredient));
        }
        ingredientsStringBuilder.replace(ingredientsStringBuilder.lastIndexOf("\n"), ingredientsStringBuilder.length(), "");
        ((TextView) findViewById(R.id.sandwich_ingredients)).setText(ingredientsStringBuilder.toString());
    }
}
