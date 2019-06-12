package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.settings_activity.*

class Settings : AppCompatActivity() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE)
        val editor = prefs.edit()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            musicSB.setProgress(prefs.getInt("music", 100), true)
            sfxSB.setProgress(prefs.getInt("sfx", 100), true)

            if (LocaleManager.getLanguage(this) == "ar")
                langToArabic()
            else
                langToEnglish()
        }

        musicSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                editor.putInt("music", i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

        sfxSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                editor.putInt("sfx", i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

        arabicBtn.setOnClickListener {
            langToArabic()

            editor.putString("lang", "ar")
            LocaleManager.setNewLocale(this, "ar")
        }

        englishBtn.setOnClickListener {
            langToEnglish()

            editor.putString("lang", "en")
            LocaleManager.setNewLocale(this, "en")
        }

        logoutBtn.setOnClickListener {
            getSharedPreferences("User", Context.MODE_PRIVATE).edit().remove("login").apply()
            startActivity(Intent(this@Settings, Login::class.java))
            finish()
        }

        doneBtn.setOnClickListener{
            editor.apply()
            finish()
        }
    }

    private fun langToArabic(){
        arabicBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_btn_on, 0, 0, 0)
        englishBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_btn_off, 0, 0, 0)
    }

    private fun langToEnglish(){
        englishBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_btn_on, 0, 0, 0)
        arabicBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_btn_off, 0, 0, 0)
    }
}
