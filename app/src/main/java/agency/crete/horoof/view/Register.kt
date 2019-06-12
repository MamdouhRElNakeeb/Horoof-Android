package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.register_activity.*
import org.json.JSONObject
import kotlin.math.log

class Register: AppCompatActivity() {

    val contest = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val nameET = findViewById<EditText>(R.id.nameET)
        val emailET = findViewById<EditText>(R.id.emailET)
        val passET = findViewById<EditText>(R.id.passET)

        registerBtn.setOnClickListener {
            register(nameET.text.toString().trim(), emailET.text.toString().trim(), passET.text.toString().trim())
        }

        hasAccountBtn.setOnClickListener {
            finish()
        }
    }


    fun register(name: String, email: String, pass: String) {

        val body = JSONObject()
        body.put("firstName", name)
        body.put("login", email)
        body.put("email", email)
        body.put("password", pass)
        body.put("activated", true)

        API.REGISTER
            .httpPost()
            .jsonBody(body.toString())
            .also { println(it) }
            .responseString{ _, response, _ ->

                println("ressss $response")

                val respBody = response.body().asString("application/json")

                when(response.statusCode) {
                    201 -> {
                        // Registration is successful
                        login(email, pass)
                    }
                    400, 401, 403 -> {
                        val err =  JSONObject(respBody).getString("title")
                        println("errrrr $err")
                        Toast.makeText(this@Register, err, Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        Toast.makeText(contest, "An error occurred, Try again later!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun login(email: String, pass: String){

        val body = JSONObject()
        body.put("username", email)
        body.put("password", pass)

        API.LOGIN
            .httpPost()
            .jsonBody(body.toString())
            .responseString{ _, response, _ ->

                println("loginResp $response")
                val respBody = response.body().asString("application/json")

                when(response.statusCode) {
                    200 -> {
                        // Login is successful
                        val prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE)
                        val editor = prefs.edit()

                        val resp = JSONObject(respBody)
                        editor.putString("token", resp.getString("id_token"))
                        editor.putString("email", email)
                        editor.putBoolean("login", true)
                        editor.apply()

                        startActivity(Intent(this@Register, Home::class.java))
                        finish()
                    }
                    400, 401, 403 -> {
                        val err =  JSONObject(respBody).getString("title")
                        println("errrrr $err")
                        Toast.makeText(this@Register, err, Toast.LENGTH_SHORT).show()

                    }
                    404 -> {
                        Toast.makeText(this@Register, "An error occurred, Try again later!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}