package ru.shtrm.serviceman.addquestion;

import android.os.Build;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.shtrm.serviceman.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Tests the components of {@link AddQuestionActivity} layout.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddQuestionScreenTest {

    private String validQuestionNumber;
    private String invalidQuestionNumber;

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<AddQuestionActivity> mAddQuestionActivityTestRule
            = new ActivityTestRule<>(AddQuestionActivity.class);

    @Before
    public void grantCameraPermission() {
        // In M+, trying to call a number will trigger a runtime dialog. Make sure
        // the permission is granted before running this test.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.CAMERA");
        }
    }

    @Test
    public void test_AddQuestionScreenDisplayed() {
        // Check that the toolbar title was correct.
        onView(withText(R.string.activity_add_question))
                .check(matches(withParent(withId(R.id.toolbar))));
    }

    @Test
    public void clickOnFab_ShowErrorTip() {
        // Click the floating action button without inputting anything.
        onView(withId(R.id.fab)).perform(click());

        // Check that the snack bar was displayed.
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.wrong_title)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void typeValidNumber_ShowHomeScreen() {
        onView(withId(R.id.editTextTitle))
                .check(matches(isCompletelyDisplayed()));

        // Type the valid number.
        onView(withId(R.id.editTextTitle))
                .perform(typeText(validQuestionNumber), closeSoftKeyboard());

        // Click the floating action button.
        onView(withId(R.id.fab)).perform(click());

        // Check that the Question name edit text was filled automatically.
        String name = mAddQuestionActivityTestRule.getActivity().getString(R.string.question_title)
                + validQuestionNumber.substring(0, 4);
        onView(withId(R.id.editTextName))
                .check(matches(withText(name)));

    }

    @Test
    public void typeInvalidNumber_ShowErrorTip() {
        onView(withId(R.id.editTextTitle))
                .check(matches(isCompletelyDisplayed()));

        // Type the valid number.
        onView(withId(R.id.editTextTitle))
                .perform(typeText(invalidQuestionNumber), closeSoftKeyboard());

        // Click the floating action button.
        onView(withId(R.id.fab)).perform(click());

        // Check that the Question name edit text was filled automatically.
        String name = mAddQuestionActivityTestRule.getActivity().getString(R.string.question_title)
                + invalidQuestionNumber.substring(0, 4);
        onView(withId(R.id.editTextName))
                .check(matches(withText(name)));

        // Check that the snack bar with error message was displayed.
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.wrong_title)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void typeEmptyNumber_ShowErrorTip() {
        onView(withId(R.id.editTextTitle))
                .check(matches(isCompletelyDisplayed()));

        // Type empty.
        onView(withId(R.id.editTextTitle))
                .perform(typeText(""), closeSoftKeyboard());

        // Click the floating action button.
        onView(withId(R.id.fab)).perform(click());

        // Check that the Question name edit text was filled automatically.
        onView(withId(R.id.editTextName))
                .check(matches(withText("")));

        // Check that the snack bar with error message was displayed.
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.wrong_title)))
                .check(matches(isDisplayed()));
    }

}
