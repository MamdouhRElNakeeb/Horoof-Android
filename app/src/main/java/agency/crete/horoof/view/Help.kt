package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.LocaleManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Help : AppCompatActivity() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_activity)

    }
}
