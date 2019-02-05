package agency.crete.horoof.view

import agency.crete.horoof.R
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*
import kotlin.concurrent.schedule

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE)

        Timer().schedule(2000){

            if (prefs.getBoolean("login", false)) {
                val intent = Intent(this@Splash, Home::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@Splash, Login::class.java)
                startActivity(intent)
            }

            finish()
        }
    }
}
