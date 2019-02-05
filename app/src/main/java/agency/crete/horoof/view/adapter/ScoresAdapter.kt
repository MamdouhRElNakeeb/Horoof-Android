package agency.crete.horoof.view.adapter

import agency.crete.horoof.R
import agency.crete.horoof.model.Score
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.leaderboard_item.view.*

class ScoresAdapter(val items : ArrayList<Score>, val context: Context): RecyclerView.Adapter<ScoresAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.leaderboard_item,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val score = items.get(p1)

        when(p1){
            0 -> p0.indexBg.setImageResource(R.drawable.leaderboard1)
            1 -> p0.indexBg.setImageResource(R.drawable.leaderboard2)
            2 -> p0.indexBg.setImageResource(R.drawable.leaderboard3)
        }

        p0.index.text = (p1 + 1).toString()
        p0.scoreTV.text = score.value.toString()
        p0.username.text = score.user.name

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val index = itemView.indexTV
        val indexBg = itemView.indexBgIV
        val scoreTV = itemView.scoreTV
        val username = itemView.nameTV
    }

}