package com.example.fair2share

import android.app.Activity
import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.fair2share.utils.MyViewAction
import com.example.fair2share.utils.RecyclerViewItemCountAssertion
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Rule
import org.junit.Test


@LargeTest
class NormalFlowTest {
    private fun resetSharedPreferences() {
        val sharedPreferences = InstrumentationRegistry.getInstrumentation()
            .targetContext.getSharedPreferences("Fair2Share", Activity.MODE_PRIVATE)
        sharedPreferences.edit().remove("token").remove("profileId").apply()
    }

    @get:Rule
    val activityRule = object : ActivityTestRule<StartUpActivity>(StartUpActivity::class.java) {
        override fun beforeActivityLaunched() {
            resetSharedPreferences()
            super.beforeActivityLaunched()
        }
    }

    @After
    fun server() {
        resetSharedPreferences()
    }


    @Test
    fun test() {
        SystemClock.sleep(1100)
        onView(withId(R.id.loginFragment))
        onView(withId(R.id.edit_login_email)).perform(
            typeText("test@account.be"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.edit_login_password)).perform(
            typeText("testPassword1"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.btn_login_login)).perform(click())
        SystemClock.sleep(2000)

        onView(withText("my activity")).perform(click())
        SystemClock.sleep(1000)
        var itemCount = 0
        onView(withId(R.id.rv_activitytransactions_list)).check { view, noViewFoundException ->
            itemCount = (view as RecyclerView).adapter!!.itemCount
        }
        onView(withId(R.id.fab_activitytransactions_addtransaction)).perform(click())

        onView(withId(R.id.edit_addedittransaction_name)).perform(
            typeText("my test transaction"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.edit_addedittransaction_payment)).perform(
            replaceText("10.00"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.cbo_addedittransaction_paidby)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("new account"))).perform(click())
        onView(withId(R.id.cbo_addedittransaction_paidby)).check(
            matches(
                withSpinnerText(
                    containsString("new account")
                )
            )
        )
        onView(withId(R.id.btn_addedittransaction)).perform(click())

        onView(withId(R.id.rv_mmanagepeopleintransaction_candidates)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ConstraintRowItemViewHolder>(
                0, MyViewAction.clickChildViewWithId(R.id.btn_recycleraddandremovefriend_addfriend)
            )
        )
        onView(withId(R.id.rv_mmanagepeopleintransaction_candidates)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ConstraintRowItemViewHolder>(
                1, MyViewAction.clickChildViewWithId(R.id.btn_recycleraddandremovefriend_addfriend)
            )
        )
        onView(withId(R.id.btn_managepeopleintransaction_confirm)).perform(click())
        SystemClock.sleep(1000)

        onView(withId(R.id.rv_activitytransactions_list)).check(
            RecyclerViewItemCountAssertion(
                itemCount + 1
            )
        )
    }
}

