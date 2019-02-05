package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.kittinunf.fuel.core.Parameters
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
        body.put("email", email)
        body.put("password", pass)

        API.LOGIN
            .httpPost()
            .jsonBody(body.toString())
            .responseString{ request, response, result ->

                print("resp: " + response)

                when(result){
                    is Result.Success -> {

                        val resp = JSONObject(result.get())

                        print("result: " + resp)

                        val prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE)
                        val editor = prefs.edit()

                        editor.putString("token", resp.getString("token"))
                        editor.putString("email", email)
                        editor.putBoolean("login", true)
                        editor.apply()

                        startActivity(Intent(this@Login, Home::class.java))
                        finish()
                    }

                    is Result.Failure -> {
                        val err = result.getException()

                        when(response.statusCode){
                            401, 403 -> Toast.makeText(this, JSONObject(result.get()).getString("message"), Toast.LENGTH_SHORT).show()
                            404 -> Toast.makeText(this, "An error occurred, Try again later!", Toast.LENGTH_SHORT).show()
                        }

                        print("err: " + err)
                    }
                }
            }
    }
}