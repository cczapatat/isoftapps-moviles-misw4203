package miso4203.mobile.app.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginListArtistsFilterSelectOneAndReturnTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginListArtistsFilterSelectOneAndReturnTest() {
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

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_artist), withContentDescription("Artists"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(R.id.searchView),

                isDisplayed()
            )
        )
        searchAutoComplete.perform(click())
        searchAutoComplete.perform(typeText("rub"),closeSoftKeyboard())

        val recyclerView = onView(
            allOf(
                withId(R.id.artistRv),
                childAtPosition(
                    withClassName(`is`("android.widget.FrameLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val button = onView(
            allOf(
                withId(R.id.btnBackArt), withText("X"),
                withParent(withParent(withId(R.id.detail_title_art))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnBackArt), withText("X"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.detail_title_art),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val searchView = onView(
            allOf(
                withId(R.id.searchView),
                withParent(withParent(withId(R.id.card_title))),
                isDisplayed()
            )
        )
        searchView.check(matches(isDisplayed()))
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
