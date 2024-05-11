package miso4203.mobile.app.vinilos


import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ExpressTest8d {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun express_test_8_d() {
        val materialButton = onView(
            allOf(
                withId(R.id.btn_visitor_login), withText("Visitor"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(R.id.searchView),

                isDisplayed()
            )
        )
        searchAutoComplete.perform(click())
        searchAutoComplete.perform(typeText("something"),closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(R.id.searchView),

                isDisplayed()
            )
        )
        searchAutoComplete2.perform(click())
        searchAutoComplete2.perform(pressKey(KeyEvent.KEYCODE_DEL),
                pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            closeSoftKeyboard())


        val textView = onView(
            allOf(
                withId(R.id.textAlbumName), withText("Poeta del pueblo"),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Poeta del pueblo")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
