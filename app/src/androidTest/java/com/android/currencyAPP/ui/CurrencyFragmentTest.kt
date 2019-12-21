package com.android.currencyAPP

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.android.currencyAPP.ui.MainActivity
import com.android.currencyAPP.ui.RecyclerViewMatcher
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class CurrencyFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testReceiverValueInWhenBase() {
        onView(withId(R.id.nestedScrollView)).check(matches(isDisplayed()))
        onView(withId(R.id.firstCurrencyEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.firstCurrencyEditText)).perform(typeText("500"))
        onView(withId(R.id.firstCurrencyPicker)).perform(click())
        onView(withText("EUR")).perform(click())
        onView(withId(R.id.secondCurrencyPicker)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), containsString("USD"))).perform(click())
        onView(withId(R.id.secondValueText)).check(matches(withText("+554,85")))

    }

    @Test
    // need to disable anim in fragment or use idleResource
    fun testConversion() {
        onView(withId(R.id.firstCurrencyEditText)).perform(click()).perform(typeText("1"))
        onView(withId(R.id.secondCurrencyPicker)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), containsString("EUR"))).perform(click())
        onView(withId(R.id.secondCurrencyPicker)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), containsString("USD"))).perform(click())
        onView(withId(R.id.exchangeButton)).perform(click())
    }

    @Test
    fun recyclerViewTest() {
        onView(
            RecyclerViewMatcher(R.id.latestConversionsList).atPositionOnView(
                0,
                R.id.firstPrice
            )
        ).check(
            matches(withText("12,00"))
        )
        onView(
            RecyclerViewMatcher(R.id.latestConversionsList).atPositionOnView(
                0,
                R.id.secondPrice
            )
        ).check(
            matches(withText("19,31"))
        )
    }
}