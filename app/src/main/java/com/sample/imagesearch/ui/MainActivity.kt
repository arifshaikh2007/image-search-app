package com.sample.imagesearch.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.sample.imagesearch.R
import dagger.android.AndroidInjection

/**
 * This activity shows search image screen for phones and for tablet in landscape mode it shows
 * search image screen in left section where as image details screen on right section.
 * To show image search Ui, fragment {@link SearchFragment} is used.
 * To show image details screen after selecting image from search section,
 * fragment {@link ImageDetailsFragment} is used
 */
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().run {
                replace(R.id.search_fragment_container, SearchFragment())
                    .commit()
            }

        }

        showImageDetailsFramentIfRequired()
    }

    /**
     * This method check if layout if for tablet by checking appropriate id for tablet.
     * For tablet {@link ImageDetailsFragment} is shown in right section where as left section
     * contains {@link SearchFragment}
     * If existing {@link ImageDetailsFragment} is found then that is used
     * otherwise new {@link ImageDetailsFragment} is created. Existing ImageDetailsFragment is used
     * check to handle case where device rotated from portrait to landscape from ImageDetailsFragment
     * and back to main activity then show previously selected image in right pane on tablet.
     */
    private fun showImageDetailsFramentIfRequired() {
        findViewById<View>(R.id.tablet_fragment_container) ?: return

        var fragment = supportFragmentManager.findFragmentByTag(IMAGE_DETAILS_FRAGMENT_TAG)
        if (fragment == null) {
            fragment = ImageDetailsFragment()
        }

        supportFragmentManager.beginTransaction().run {
            replace(R.id.image_details_fragment_container, fragment, IMAGE_DETAILS_FRAGMENT_TAG)
                .commit()
        }
    }

    companion object {
        const val IMAGE_DETAILS_FRAGMENT_TAG = "image_details_fragment"
    }
}