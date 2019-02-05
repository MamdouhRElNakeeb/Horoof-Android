package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.register_activity.*
import org.json.JSONObject
import kotlin.math.log

class Register: AppCompatActivity() {

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
        body.put("name", name)
        body.put("email", email)
        body.put("password", pass)

        API.REGISTER
            .httpPost()
            .jsonBody(body.toString())
            .responseString{ request, response, result ->

                print("resp: " + response)

                when(result){
                    is Result.Success -> {

                        val resp = JSONObject(result.get())

                        print("resp: " + resp)

                        login(email, pass)
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

    fun login(email: String, pass: String){

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

                        print("resp: " + resp)

                        val prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE)
                        val editor = prefs.edit()

                        editor.putString("token", resp.getString("token"))
                        editor.putString("email", email)
                        editor.putBoolean("login", true)
                        editor.apply()

                        startActivity(Intent(this, Home::class.java))
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