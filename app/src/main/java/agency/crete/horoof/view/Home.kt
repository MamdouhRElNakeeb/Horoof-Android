package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.LocaleManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.home_activity.*

class Home : AppCompatActivity() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)


        newCompBtn.setOnClickListener {
            startActivity(Intent(this@Home, NewContest::class.java))
        }
        bestResultsBtn.setOnClickListener {
            startActivity(Intent(this@Home, Leaderboard::class.java))
        }

        settingsBtn.setOnClickListener {
            startActivity(Intent(this@Home, Settings::class.java))
        }

        helpBtn.setOnClickListener {
            startActivity(Intent(this@Home, Help::class.java))
        }
    }
}
