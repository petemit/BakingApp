package com.petemit.example.android.bakingapp;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import android.support.test.espresso.contrib.RecyclerViewActions;


/**
 * Created by Peter on 8/16/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule=
            new ActivityTestRule<>(MainActivity.class);

    @Before public void initResources(){
        Intents.init();
    }

    @Test
    public void ensureDataHasLoaded() {


        //ensure that list is showing the data properly.
        onView(withId(R.id.rv_recipe_list))
                .check(matches(hasDescendant(withText("Brownies"))));//.check(matches(hasDescendant(withText("Brownies"))));
    }

    @Test
    public void ensureCorrectRecipeDetailActivityPops() {

        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1
                        ,click()));

        intended(hasComponent(new ComponentName(getTargetContext(),RecipeDetailActivity.class)));

        onView(withId(R.id.tv_recipe_detail_ingredients_recipe_description))
                .check(matches((withText("Brownies"))));
    }

    @Test
    public void ensureStepFragmentLoads() {

        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1
                        ,click()));

        intended(hasComponent(new ComponentName(getTargetContext(),RecipeDetailActivity.class)));

        onView(withId(R.id.tv_recipe_detail_ingredients_recipe_description))
                .check(matches((withText("Brownies"))));

        //find the view immediately after the ingredients
        onView(withId(R.id.rv_recipe_detail_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click())
                );
        //should be missing previous step button!
        onView(withId(R.id.previous_step_button)).check(matches(not(isDisplayed())));

        //Should have a next button!
        onView(withId(R.id.next_step_button)).check(matches(isDisplayed()));


    }





    @After
    public void releaseResources(){
        Intents.release();
    }
}
