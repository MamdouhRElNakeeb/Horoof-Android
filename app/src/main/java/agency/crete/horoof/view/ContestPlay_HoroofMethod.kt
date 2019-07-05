package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import agency.crete.horoof.helper.LocaleManager
import agency.crete.horoof.model.Answer
import agency.crete.horoof.model.Question
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.contest_play_activity.*
import org.json.JSONArray
import org.json.JSONObject
import pl.droidsonroids.gif.GifDrawable
import java.util.*

class ContestPlay_HoroofMethod: AppCompatActivity() {

    var items: ArrayList<Question> = ArrayList()

    private var gifDrawable: GifDrawable? = null

    var categoryID = 0
    var questionIndex = 0

    var page = 0

    var score = 0

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contest_play_activity)

        if (intent.hasExtra("category"))
            categoryID = intent.getIntExtra("category", 0)

        if (categoryID == 0)
            finish()

        getQuestions()

        answer1Btn.setOnClickListener {
            evaluateAnswers(1)
        }

        answer2Btn.setOnClickListener {
            evaluateAnswers(2)
        }

        answer3Btn.setOnClickListener {
            evaluateAnswers(3)
        }

        answer4Btn.setOnClickListener {
            evaluateAnswers(4)
        }

        nextBtn.setOnClickListener {
            if (questionIndex != items.size - 1) {
                resultLL.visibility = GONE
                questionIndex++
                changeQuestion(questionIndex)
            }
            else {
                if (repeatBtn.isEnabled){
                    resultMsgTV.text = "${resources.getString(R.string.score)}: $score / ${items.size}"
                    repeatBtn.isEnabled = false
                    repeatBtn.alpha = 0.5F
                    showGIF(R.raw.done)
                    Log.d("Score", score.toString())
                }
                else {
                    finish()
                }

            }
        }

        repeatBtn.setOnClickListener {
            resultLL.visibility = GONE
            changeQuestion(questionIndex)
        }
    }

    private fun getQuestions(){

        val token = getSharedPreferences("User", Context.MODE_PRIVATE).getString("token", "")

        Fuel.get("${API.CATEGORY}/$categoryID/questions", listOf("page" to page, "offset" to 10))
            .authentication()
            .bearer(token)
            .responseString{ request, response, result ->

                print("resp: $response")

                when(result){
                    is Result.Success -> {

                        val resp = JSONArray(result.get())

                        print("resp: $resp")

                        for (i in 0..(resp.length() - 1)){

                            val obj = resp.getJSONObject(i)

                            val answersObj = obj.getJSONArray("answers")
                            val list: ArrayList<Answer> = ArrayList()
                            list.add(
                                Answer(
                                    answersObj.getJSONObject(0).getInt("id"),
                                    answersObj.getJSONObject(0).getString("answer"),
                                    answersObj.getJSONObject(0).getBoolean("correct")
                                )
                            )
                            list.add(
                                Answer(
                                    answersObj.getJSONObject(1).getInt("id"),
                                    answersObj.getJSONObject(1).getString("answer"),
                                    answersObj.getJSONObject(1).getBoolean("correct")
                                )
                            )
                            list.add(
                                Answer(
                                    answersObj.getJSONObject(2).getInt("id"),
                                    answersObj.getJSONObject(2).getString("answer"),
                                    answersObj.getJSONObject(2).getBoolean("correct")
                                )
                            )
                            list.add(
                                Answer(
                                    answersObj.getJSONObject(3).getInt("id"),
                                    answersObj.getJSONObject(3).getString("answer"),
                                    answersObj.getJSONObject(3).getBoolean("correct")
                                )
                            )

                            items.add(
                                Question(
                                    obj.getInt("id"),
                                    obj.getString("name"),
                                    list
                                )
                            )

                        }

                        runOnUiThread {
                            changeQuestion(questionIndex)
                        }

                    }

                    is Result.Failure -> {
                        val err = result.getException()

                        when(response.statusCode){
                            401, 403 -> Toast.makeText(this, JSONObject(result.get()).getString("message"), Toast.LENGTH_SHORT).show()
                            404 -> Toast.makeText(this, "An error occurred, Try again later!", Toast.LENGTH_SHORT).show()
                        }

                        print("err: $err")
                    }
                }
            }
    }

    private fun changeQuestion(index: Int){

        questionTV.text = items[index].content
        answer1TV.text = items[index].answers[0].content
        answer2TV.text = items[index].answers[1].content
        answer3TV.text = items[index].answers[2].content
        answer4TV.text = items[index].answers[3].content

        resetAnswers()
    }

    private fun evaluateAnswers(choosenAnswer: Int) {

        when {
            items[questionIndex].answers[0].status -> answer1Bg.setImageResource(R.drawable.frame_gbg)
            items[questionIndex].answers[1].status -> answer2Bg.setImageResource(R.drawable.frame_gbg)
            items[questionIndex].answers[2].status -> answer3Bg.setImageResource(R.drawable.frame_gbg)
            items[questionIndex].answers[3].status -> answer4Bg.setImageResource(R.drawable.frame_gbg)
        }

        when(choosenAnswer){

            1 -> {
                if (!items[questionIndex].answers[0].status) {
                    answer1Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
                    showGIF(R.raw.correct)
                    score++
                }

                answer1Icn.setImageResource(R.drawable.radio_btn_on)
            }

            2 -> {
                if (!items[questionIndex].answers[1].status){
                    answer2Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
                    showGIF(R.raw.correct)
                    score++
                }

                answer2Icn.setImageResource(R.drawable.radio_btn_on)
            }

            3 -> {
                if (!items[questionIndex].answers[2].status){
                    answer3Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
                    showGIF(R.raw.correct)
                    score++
                }

                answer3Icn.setImageResource(R.drawable.radio_btn_on)
            }

            4 -> {
                if (!items[questionIndex].answers[3].status){
                    answer4Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
                    showGIF(R.raw.correct)
                    score++
                }

                answer4Icn.setImageResource(R.drawable.radio_btn_on)
            }

        }

    }

    fun resetAnswers(){
        answer1Bg.setImageResource(R.drawable.frame_ybg)
        answer2Bg.setImageResource(R.drawable.frame_ybg)
        answer3Bg.setImageResource(R.drawable.frame_ybg)
        answer4Bg.setImageResource(R.drawable.frame_ybg)

        answer1Icn.setImageResource(R.drawable.radio_btn_off)
        answer2Icn.setImageResource(R.drawable.radio_btn_off)
        answer3Icn.setImageResource(R.drawable.radio_btn_off)
        answer4Icn.setImageResource(R.drawable.radio_btn_off)
    }

    fun showGIF(gif: Int){
        resultLL.visibility = VISIBLE
        gifDrawable = GifDrawable(resources, gif)
        animation.setImageDrawable(gifDrawable)
    }

}