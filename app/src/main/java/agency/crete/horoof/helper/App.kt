package agency.crete.horoof.helper

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Environment
import android.util.Log

import java.io.File

import android.provider.ContactsContract.Directory.PACKAGE_NAME

class App : Application() {


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate() {
        super.onCreate()

        setDefaultFont(this, "MONOSPACE", "GE_SS_Two_Bold.otf")
    }

    fun createPackageFolder(): String {
        val extStorageDirectory = Environment
            .getExternalStorageDirectory().toString()
        val folder = File(extStorageDirectory, "/Android/data/$PACKAGE_NAME")

        if (!folder.exists()) {
            folder.mkdir()
            Log.e("Created", "Folder Created At :" + folder.path.toString())
            createFolder()
        } else {
            Log.e("exist", "Folder Created At :" + folder.path.toString())
            createFolder()
        }
        return folder.path.toString()
    }

    private fun createFolder(): String {

        val extStorageDirectory = Environment
            .getExternalStorageDirectory().toString()
        val folder = File(extStorageDirectory, "/Android/data/$PACKAGE_NAME/UserGuide")
        if (!folder.exists()) {
            folder.mkdir()
            Log.e("CreateFolder_function", "Folder Created At :" + folder.path.toString())
        } else {
            Log.e("CreateFolder_function", "Folder Created At :" + folder.path.toString())
        }
        return folder.path.toString()
    }

    private fun checkExist(filePath: String): Boolean {

        val file = File(filePath)

        Log.e("checkExist_Function", file.path.toString())

        if (file.exists()) {
            Log.e("checkExist_Function", "true")
            return true
        }

        return false
    }

    fun path(file_name: String): String {

        val file =
            File(Environment.getExternalStorageDirectory().toString() + "/Android/data/" + PACKAGE_NAME + "/UserGuide/" + file_name)

        Log.e("path_function", file.path.toString())

        return file.path.toString()
    }

    fun checkEmpty(string: String): Boolean {
        return if (string.length != 0) true else false
    }


    fun setDefaultFont(context: Context, staticTypefaceFieldName: String, fontAssetName: String) {
        val regular = Typeface.createFromAsset(
            context.assets,
            fontAssetName
        )
        replaceFont(staticTypefaceFieldName, regular)
    }

    protected fun replaceFont(staticTypefaceFieldName: String, newTypeface: Typeface) {

        try {
            val staticField = Typeface::class.java
                .getDeclaredField(staticTypefaceFieldName)
            staticField.isAccessible = true

            staticField.set(null, newTypeface)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

}
