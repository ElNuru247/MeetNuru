package xyz.ismailnurudeen.meetnuru

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class AppUtils(val activity:Activity) {

    fun hasInternetConnection(): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            return (activeNetwork.type == ConnectivityManager.TYPE_WIFI) || (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
        }
        return false
    }

    fun toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(activity, msg, length).show()
    }
    fun checkPermission(permission: String, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return if (ContextCompat.checkSelfPermission(activity,
                            permission) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    Toast.makeText(activity, "We need this permission for the app to work properly", Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
                }
                Log.v(TAG, "Permission is revoked")
                false
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            return true
        }
    }
    fun readJSONFromAsset(filename: String): JSONObject? {
        var filename = filename
        var json: String? = null
        if (!filename.endsWith(".json")) filename = "$filename.json"
        try {
            val inputStream = activity.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return JSONObject(json)
    }
}