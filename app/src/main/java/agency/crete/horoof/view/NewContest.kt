package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import agency.crete.horoof.helper.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.contest_mode_activity.*
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient


class NewContest: AppCompatActivity() {

    private val mStompClient: StompClient? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contest_mode_activity)

        horoofMethodRL.setOnClickListener {
            startActivity(Intent(this, Categories::class.java))
        }

        turnsMethodRL.setOnClickListener { val intent = Intent(this@NewContest, ContestPlay_TurnsMethod::class.java)
            startActivity(intent)
        }

    }

}