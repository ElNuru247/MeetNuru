package xyz.ismailnurudeen.meetnuru

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val aboutPage = AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_me))
                .setImage(R.drawable.nurudeen_text)
                .addGroup("Show your support")
                .addPlayStore(packageName, "Rate me on Play Store ")
                .addGroup("Let's Connect")
                .addEmail(getString(R.string.my_email), "Send me an Email")
                .addWebsite(getString(R.string.my_website), "View my website")
                .addFacebook(getString(R.string.my_fb_id), "Like me on Facebook")
                .addTwitter(getString(R.string.my_twitter_id), "Follow me on Twitter")
                .addItem(getCopyRightsElement())
                .create()
        setContentView(aboutPage)
        // setupToolbar()
    }

    fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About (Offline)"
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
    }

    fun getCopyRightsElement(): Element {
        val copyRightsElement = Element()
        val copyrights = "${getString(R.string.copy_right)} ${Calendar.getInstance().get(Calendar.YEAR)}"
        copyRightsElement.setTitle(copyrights)
        copyRightsElement.setIconDrawable(R.drawable.ic_copyright_black_24dp)
        copyRightsElement.setIconTint(R.color.about_item_icon_color)
        copyRightsElement.setIconNightTint(android.R.color.white)
        copyRightsElement.gravity = Gravity.CENTER
        copyRightsElement.setOnClickListener {
            Toast.makeText(this@AboutActivity, copyrights, Toast.LENGTH_SHORT).show()
        }
        return copyRightsElement
    }

    fun simulateDayNight(currentSetting: Int) {
        val DAY = 0
        val NIGHT = 1
        val FOLLOW_SYSTEM = 3

        val currentNightMode = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES)
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
