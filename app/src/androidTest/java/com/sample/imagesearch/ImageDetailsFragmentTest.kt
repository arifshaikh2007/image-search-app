package com.sample.imagesearch

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sample.data.models.ImageDataModel
import com.sample.imagesearch.ui.ImageDetailsFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageDetailsFragmentTest {
    @Test
    fun testImageDetailsFragmentView() {
        val imageDetailsFragment = ImageDetailsFragment()
        val imageDetails = ImageDataModel("1", "https://imgur.com/adfadf.jpp", "images", "title2")
        val bundle = Bundle()
        bundle.putParcelable("image_details", imageDetails)
        launchFragmentInContainer(bundle) { imageDetailsFragment }

        onView(withId(R.id.selected_image)).check(matches(isDisplayed()))
        onView(withId(R.id.comment_edit_txt)).check(matches(isDisplayed()))
        onView(withId(R.id.submit_comment_btn)).check(matches(isDisplayed()))
    }
}