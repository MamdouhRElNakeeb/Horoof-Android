package agency.crete.horoof.view

import agency.crete.horoof.view.adapter.CategoriesAdapter
import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import agency.crete.horoof.helper.LocaleManager
import agency.crete.horoof.model.Category
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.categories_activity.*
import org.json.JSONArray
import org.json.JSONObject

class Categories : AppCompatActivity() {

    var items: ArrayList<Category> = ArrayList()

    var page = 0

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_activity)

        categoriesRV.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        categoriesRV.adapter = CategoriesAdapter(items, this)

        categoriesRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getCategories()
                }
            }
        })

        getCategories()

    }

    fun getCategories() {

        val token = getSharedPreferences("User", Context.MODE_PRIVATE).getString("token", "")
        Log.d("token", token)

        API.CATEGORIES
            .httpGet(listOf("page" to page, "offset" to 10))
            .authentication()
            .bearer(token)
            .responseString{ request, response, result ->

                print("resp: " + response)

                when(result){
                    is Result.Success -> {

                        val resp = JSONArray(result.get())

                        print("resp: " + resp)

                        for (i in 0..(resp.length() - 1)){

                            val obj = resp.getJSONObject(i)
                            items.add(
                                Category(
                                    obj.getInt("id"),
                                    obj.getString("name"),
                                    obj.getString("description")
                                )
                            )
                        }

                        runOnUiThread {
                            categoriesRV.adapter!!.notifyDataSetChanged()
                        }

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
