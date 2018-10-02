package com.udacity.bakingapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.udacity.bakingapp.viewmodel.IngredientListViewModel;
import com.udacity.bakingapp.viewmodel.IngredientListViewModelFactory;
import com.udacity.bakingapp.viewmodel.StepListViewModel;
import com.udacity.bakingapp.viewmodel.StepListViewModelFactory;

import java.util.ArrayList;

import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_ID;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_NAME;


public class RecipeStepsFragment extends Fragment implements StepAdapter.StepClickHandler, View.OnClickListener {

    protected static final String TAG = RecipeStepsFragment.class.getSimpleName();
    public static final String INTENT_KEY_STEP_ID = "step_id";
    private static final String INGREDIENTS_VISIBILITY_KEY = "ingredients_visible";

    private ImageButton mExpandimageButton;
    private RecyclerView mIngredientsRecyclerView;
    private RecyclerView mStepsRecyclerView;
    private LinearLayoutManager mIngredientsLayoutManager;
    private LinearLayoutManager mStepsLayoutManager;
    private IngredientAdapter mIngredientAdapter;
    private StepAdapter mStepAdapter;
    private IngredientListViewModel mIngredientListViewModel;
    private StepListViewModel mStepListViewModel;
    private Long mRecipeId;
    private String mRecipeName;
    private OnRecipeStepSelectedListener mOnRecipeStepSelectedListener;

    public RecipeStepsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getContext();

        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        mIngredientsRecyclerView = view.findViewById(R.id.recipe_ingredients_recycler_view);
        mIngredientsLayoutManager = new LinearLayoutManager(context);
        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);

        mIngredientAdapter = new IngredientAdapter(context, new ArrayList<>());
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

        mStepsRecyclerView = view.findViewById(R.id.recipe_steps_recycler_view);
        mStepsLayoutManager = new LinearLayoutManager(context);
        mStepsRecyclerView.setLayoutManager(mStepsLayoutManager);

        mStepAdapter = new StepAdapter(context, this, new ArrayList<>());
        mStepsRecyclerView.setAdapter(mStepAdapter);

        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = getActivity().getIntent().getExtras();
        }

        mRecipeId = bundle.getLong(INTENT_KEY_RECIPE_ID, -1L);
        mRecipeName = bundle.getString(INTENT_KEY_RECIPE_NAME, getString(R.string.app_name));

        IngredientListViewModelFactory ingredientListViewModelFactory = new IngredientListViewModelFactory(context, mRecipeId);
        mIngredientListViewModel = ViewModelProviders.of(this, ingredientListViewModelFactory).get(IngredientListViewModel.class);
        mIngredientListViewModel.getIngredientList().observe(this, ingredientList -> mIngredientAdapter.swapIngredients(ingredientList));

        StepListViewModelFactory stepListViewModelFactory = new StepListViewModelFactory(context, mRecipeId);
        mStepListViewModel = ViewModelProviders.of(this, stepListViewModelFactory).get(StepListViewModel.class);
        mStepListViewModel.getStepList().observe(this, stepList -> mStepAdapter.swapSteps(stepList));

        mExpandimageButton = view.findViewById(R.id.recipe_ingredients_expand);
        mExpandimageButton.setOnClickListener(this);

        int visibility;
        if (savedInstanceState != null) {
            visibility = savedInstanceState.getInt(INGREDIENTS_VISIBILITY_KEY, View.GONE);
        } else {
            visibility = View.GONE;
        }

        mIngredientsRecyclerView.setVisibility(visibility);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INGREDIENTS_VISIBILITY_KEY, mIngredientsRecyclerView.getVisibility());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailActivity) {
            setOnRecipeStepSelectedListener((RecipeDetailActivity) context);
        }
    }

    @Override
    public void onClickStep(long stepId) {
        Log.d(TAG, String.format("onClickStep - recipeId:%s, stepId:%s, mOnRecipeStepSelectedListener:%s", mRecipeId, stepId, mOnRecipeStepSelectedListener));
        if (mOnRecipeStepSelectedListener != null) {
            mOnRecipeStepSelectedListener.onRecipeStepSelected(mRecipeId, mRecipeName, stepId);
        }
    }

    @Override
    public void onClick(View v) {
        int visibility;
        int imageResourceId;
        if (mIngredientsRecyclerView.getVisibility() == View.GONE) {
            visibility = View.VISIBLE;
            imageResourceId = R.mipmap.ic_expand_less;
        } else {
            visibility = View.GONE;
            imageResourceId = R.mipmap.ic_expand_more;
        }

        mExpandimageButton.setImageResource(imageResourceId);
        mIngredientsRecyclerView.setVisibility(visibility);
    }

    public interface OnRecipeStepSelectedListener {
        void onRecipeStepSelected(long recipeId, String bundle, long stepId);
    }

    public void setOnRecipeStepSelectedListener(OnRecipeStepSelectedListener onRecipeStepSelectedListener) {
        this.mOnRecipeStepSelectedListener = onRecipeStepSelectedListener;
    }
}
