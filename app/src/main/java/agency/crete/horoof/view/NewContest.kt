package agency.crete.horoof.view

import agency.crete.horoof.R
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.contest_mode_activity.*

class NewContest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contest_mode_activity)

        horoofMethodRL.setOnClickListener {
            startActivity(Intent(this, Categories::class.java))
        }
    }
}