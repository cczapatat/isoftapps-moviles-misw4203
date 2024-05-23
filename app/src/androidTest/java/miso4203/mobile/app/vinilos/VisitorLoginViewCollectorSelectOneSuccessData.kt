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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class VisitorLoginViewCollectorSelectOneSuccessData {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun collectorDetailTestSuccess() {
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
                withId(R.id.navigation_collector), withContentDescription("Collectors"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.collectorsRv),
                childAtPosition(
                    withClassName(`is`("android.widget.FrameLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val textView = onView(
            allOf(
                withId(R.id.textCollectorAlbumGenre), withText("Salsa"),
                withParent(
                    allOf(
                        withId(R.id.collector_album_title),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.collector_album_image),
                withContentDescription("Collector Albums's cover"),
                withParent(
                    allOf(
                        withId(R.id.containerImageArtist),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.collectorAlbumsRv),
                withParent(withParent(withId(R.id.collector_detail_layout))),
                isDisplayed()
            )
        )
        recyclerView2.check(matches(isDisplayed()))
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
