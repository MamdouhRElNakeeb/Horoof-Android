package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.view.adapter.ScoresAdapter
import agency.crete.horoof.model.Score
import agency.crete.horoof.model.User
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.leaderboard_activity.*

class Leaderboard : AppCompatActivity() {

    var scores: ArrayList<Score> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard_activity)

        scores.add(Score(User(0, "Mamdouh", "nakeeb"), 1000))
        scores.add(Score(User(1, "Reda", "nakeeb"), 2134))
        scores.add(Score(User(2, "Mohamed", "nakeeb"), 1241))
        scores.add(Score(User(3, "Abdel Khalek", "nakeeb"), 1290))
        scores.add(Score(User(0, "Nakeeb", "nakeeb"), 9081))
        scores.add(Score(User(0, "Mamdouh", "nakeeb"), 8761))
        scores.add(Score(User(0, "Mamdouh", "nakeeb"), 7651))
        scores.add(Score(User(0, "Mamdouh", "nakeeb"), 7641))
        scores.add(Score(User(0, "Mamdouh", "nakeeb"), 1241))
        scores.add(Score(User(0, "Mamdouh", "nakeeb"), 4224))
        scores.add(Score(User(0, "Mamdouh", "nakeeb"), 1412))

        scores.sortWith(object: Comparator<Score>{
            override fun compare(p1: Score, p2: Score): Int = when {
                p1.value < p2.value -> 1
                p1.value == p2.value -> 0
                else -> -1
            }
        })
//        scores.sortedWith(compareBy({ it.value }))

        scoresRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        scoresRV.adapter = ScoresAdapter(scores, this)

    }
}
