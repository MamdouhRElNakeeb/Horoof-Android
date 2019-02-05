package agency.crete.horoof.view.adapter

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import agency.crete.horoof.model.Category
import agency.crete.horoof.view.ContestPlay
import agency.crete.horoof.view.NewContest
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_item.view.*

class CategoriesAdapter(val items : ArrayList<Category>, val context: Context): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.category_item,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val category = items[p1]

        p0.name.text = category.name
        Picasso.get().load(API.SERVER + "/" + category.img).placeholder(R.drawable.category_icn).into(p0.img)

        p0.itemView.setOnClickListener {
            val intent = Intent(context, ContestPlay::class.java)
            intent.putExtra("category", category.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.categoryNameTV
        val img = itemView.categoryIV
    }

}