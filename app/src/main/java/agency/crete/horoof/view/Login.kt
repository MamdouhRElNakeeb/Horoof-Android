package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.login_activity.*
import org.json.JSONObject

class Login: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        newUserBtn.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }

        loginBtn.setOnClickListener {
            login()
        }
    }


    fun login(){

        val email = emailET.text.toString().trim()
        val pass = passET.text.toString().trim()

        val body = JSONObject()
        body.put("username", email)
        body.put("password", pass)

        API.LOGIN
            .httpPost()
            .jsonBody(body.toString())
            .responseString{ request, response, result ->

                println("loginResp $response")
                val respBody = response.body().asString("application/json")
                println(respBody)

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

                        startActivity(Intent(this@Login, Home::class.java))
                        finish()
                    }
                    400, 401, 403 -> {
                        val err =  JSONObject(respBody).getString("title")
                        println("errrrr $err")
                        Toast.makeText(this@Login, err, Toast.LENGTH_SHORT).show()

                    }
                    404 -> {
                        Toast.makeText(this@Login, "An error occurred, Try again later!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}