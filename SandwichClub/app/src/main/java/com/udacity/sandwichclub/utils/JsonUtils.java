package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {


    public static Sandwich parseSandwichJson(String sandwichJson) {
        final String NAME = "name";
        final String MAIN_NAME = "mainName";
        final String ALSO_KNOWN_AS = "alsoKnownAs";
        final String PLACE_OF_ORIGIN = "placeOfOrigin";
        final String DESCRIPTION = "description";
        final String IMAGE = "image";
        final String INGREDIENTS = "ingredients";

        Sandwich sandwich = new Sandwich();

        try {
            JSONObject sandwichJsonObject = new JSONObject(sandwichJson);

            JSONObject nameJsonObject = sandwichJsonObject.getJSONObject(NAME);
            sandwich.setMainName(nameJsonObject.getString(MAIN_NAME));

            List<String> akaList = new ArrayList<>();
            JSONArray akaJsonArray = nameJsonObject.getJSONArray(ALSO_KNOWN_AS);
            for (int i = 0; i < akaJsonArray.length(); i++) {
                akaList.add(akaJsonArray.getString(i));
            }
            sandwich.setAlsoKnownAs(akaList);

            sandwich.setPlaceOfOrigin(sandwichJsonObject.getString(PLACE_OF_ORIGIN));
            sandwich.setDescription(sandwichJsonObject.getString(DESCRIPTION));
            sandwich.setImage(sandwichJsonObject.getString(IMAGE));

            List<String> ingredientsList = new ArrayList<>();
            JSONArray ingredientJsonArray = sandwichJsonObject.getJSONArray(INGREDIENTS);
            for (int i = 0; i < ingredientJsonArray.length(); i++) {
                ingredientsList.add(ingredientJsonArray.getString(i));
            }
            sandwich.setIngredients(ingredientsList);

        } catch (JSONException e) {
            e.printStackTrace();
            sandwich = null;
        }

        return sandwich;
    }
}
