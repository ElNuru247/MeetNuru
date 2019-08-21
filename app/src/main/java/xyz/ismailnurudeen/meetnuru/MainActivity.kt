package xyz.ismailnurudeen.meetnuru

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_no_internet.*
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    //val TAG = "MainActivity"
    private lateinit var utils: AppUtils
    private lateinit var quote: JSONObject

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        utils = AppUtils(this)

        val wbSettings = main_wbview.settings
        wbSettings.javaScriptEnabled = true

        loading_icon.startAnim()
        loading_icon.setBarColor(ContextCompat.getColor(this, R.color.colorAccent))
        showQuote()
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                showQuote()
                handler.postDelayed(this, 5500)
            }

        }, 5500)
        loadWebPage()

        retry_btn.setOnClickListener {
            no_internet_view.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
            showQuote()
            loadWebPage()
        }
        val popUpMenu = PopupMenu(this, btn_more_info)
        popUpMenu.menuInflater.inflate(R.menu.offline_menu, popUpMenu.menu)
        popUpMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.offline_view -> {
                    startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                }
                R.id.share_contact -> {
                    val shareTitle = "Share Contact With...?"
                    val shareIntent = ShareCompat.IntentBuilder.from(this)
                    shareIntent.setChooserTitle(shareTitle)
                            .setType("text/plain")
                            .setText(getString(R.string.share_contact_text))
                    shareIntent.startChooser()
                }
            }
            true
        }
        btn_more_info.setOnClickListener {
            popUpMenu.show()
        }
    }

    private fun loadWebPage() {
        if (utils.hasInternetConnection()) {
            main_wbview.loadUrl("http://ismailnurudeen.xyz")
            main_wbview.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    main_wbview.visibility = View.VISIBLE
                    loading_view.visibility = View.GONE
                    no_internet_view.visibility = View.GONE
                }

                @Suppress("OverridingDeprecatedMember", "DEPRECATION")
                override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
//                    main_wbview.loadUrl("about:blank")
                    main_wbview.visibility = View.GONE
                    loading_view.visibility = View.GONE
                    no_internet_view.visibility = View.VISIBLE

                    utils.toast("$description", Toast.LENGTH_LONG)
                    super.onReceivedError(view, errorCode, description, failingUrl)
                }
            }
        } else {
            main_wbview.visibility = View.GONE
            loading_view.visibility = View.GONE
            no_internet_view.visibility = View.VISIBLE
            utils.toast("No internet connection!!!", Toast.LENGTH_LONG)
        }
    }

    fun offlineOptionClick(v: View) {
        when (v.id) {
            R.id.btn_call -> {
                contact(0)
            }
            R.id.btn_sms -> {
                contact(1)
            }
            R.id.btn_email -> {
                contact(2)
            }
        }
    }

    private fun contact(type: Int) {
        val phoneNum = "+2348163471885"
        val email = "ibrightstar247@gmail.com"
        if (type == 0) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNum"))
            if (utils.checkPermission(Manifest.permission.CALL_PHONE, 102)) startActivity(intent)
        } else if (type == 1) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.type = "vnd.android-dir/mms-sms"
            intent.putExtra("address", phoneNum)
     		startActivity(intent)
        } else if (type == 2) {
            val intent = Intent(Intent.ACTION_SEND, Uri.parse("mailto:"))
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, email)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Ismail Nurudeen")
            startActivity(Intent.createChooser(intent, "Choose email app..."))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            when (requestCode) {
                101 -> {
                }
                102 -> contact(0)
                103 -> contact(1)
            }
        }
    }

    private fun showQuote() {
        val quotesFile = utils.readJSONFromAsset("inspirational_quotes")
        val quotes = quotesFile!!.getJSONArray("quotes")
        val randNum = Random().nextInt(quotes.length())
        quote = quotes.getJSONObject(randNum)
        val quote_anim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        val author_anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)

        loading_quote_id.text = "\"${quote.getString("quote")}\""
        loading_quote_author.text = "- ${quote.getString("author")}"
        loading_quote_id.startAnimation(quote_anim)
        loading_quote_author.startAnimation(author_anim)

    }

    override fun onBackPressed() {
        if (main_wbview.canGoBack() && main_wbview.url != "http://ismailnurudeen.xyz/") {
            main_wbview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
