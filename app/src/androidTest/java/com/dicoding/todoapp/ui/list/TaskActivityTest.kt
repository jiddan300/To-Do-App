package com.dicoding.todoapp.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.add.AddTaskActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(TaskActivity::class.java)

    @Test
    fun simulateAddTaskButton(){
        Intents.init()

        onView(withId(R.id.fab_add)).perform(click())
        intended(hasComponent(AddTaskActivity::class.java.name))
    }
}