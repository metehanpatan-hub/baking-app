package com.patan.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.patan.bakingapp.databinding.FragmentRecipeInfoBinding;

import java.util.Objects;

public class RecipeInfoFragment extends Fragment {
    private FragmentRecipeInfoBinding binding;
    private Recipe recipe;
    private StepsAdapter stepsAdapter;

    private final String CURRENT_TAB = "current_tab";
    private final String CURRENT_STEP = "current_step";

    OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepSelected(int position);
    }

    public RecipeInfoFragment(){}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_info, container, false);
        final View rootView = binding.getRoot();

        if (recipe == null){
            recipe =((RecipeInfoActivity) Objects.requireNonNull(getActivity())).getRecipe();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvIngredients.setLayoutManager(layoutManager);
        binding.rvIngredients.setHasFixedSize(true);


        LinearLayoutManager layoutManagerSteps = new LinearLayoutManager(getContext());
        binding.rvSteps.setLayoutManager(layoutManagerSteps);
        binding.rvSteps.setHasFixedSize(true);

        binding.tabHost.setup();
        addTab(getString(R.string.tab_title_ingredients), R.id.tab1);
        addTab(getString(R.string.tab_title_steps), R.id.tab2);

        initViews();

        // save the currently selected tab, so we can restore it in case of a phone rotation
        if (savedInstanceState != null) {
            binding.tabHost.setCurrentTab(savedInstanceState.getInt(CURRENT_TAB));

            stepsAdapter.selectedPosition = savedInstanceState.getInt(CURRENT_STEP);
            stepsAdapter.notifyDataSetChanged();
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnStepClickListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement OnStepClickListener");
        }
    }

    private void initViews() {
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients());
        binding.rvIngredients.setAdapter(ingredientsAdapter);

        stepsAdapter = new StepsAdapter(recipe.getSteps(), (int clickedItemIndex) -> {
            mCallback.onStepSelected(clickedItemIndex);
        });
        binding.rvSteps.setAdapter(stepsAdapter);
    }

    private void addTab(String tabName, int contentId) {
        TabHost.TabSpec spec = binding.tabHost.newTabSpec(tabName);
        spec.setContent(contentId);
        spec.setIndicator(tabName);
        binding.tabHost.addTab(spec);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_TAB, binding.tabHost.getCurrentTab());
        outState.putInt(CURRENT_STEP, stepsAdapter.selectedPosition);
    }
}
