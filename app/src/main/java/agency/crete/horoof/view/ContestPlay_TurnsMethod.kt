package agency.crete.horoof.view

import agency.crete.horoof.R
import agency.crete.horoof.helper.API
import agency.crete.horoof.helper.LocaleManager
import agency.crete.horoof.model.Answer
import agency.crete.horoof.model.Question
import agency.crete.horoof.model.User
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import org.json.JSONObject
import pl.droidsonroids.gif.GifDrawable
import java.util.ArrayList
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.contest_play_activity.*
import org.java_websocket.client.WebSocketClient
import ua.naiksoftware.stomp.Stomp


class ContestPlay_TurnsMethod: AppCompatActivity() {

    private var stompClient: StompClient? = null

    var items: ArrayList<Question> = ArrayList()

    private var gifDrawable: GifDrawable? = null

    var compID = 0
    var player1: User? = null
    var player2: User? = null
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

        val token = getSharedPreferences("User", Context.MODE_PRIVATE).getString("token", "")
        Log.d("token", token)

        Log.d("sockUrl", "${API.TRACKER}?access_token=$token")
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "${API.TRACKER}?access_token=$token")
        joinCompetition()

        answer1Btn.setOnClickListener {
            evaluateAnswers(0)
        }

        answer2Btn.setOnClickListener {
            evaluateAnswers(1)
        }

        answer3Btn.setOnClickListener {
            evaluateAnswers(2)
        }

        answer4Btn.setOnClickListener {
            evaluateAnswers(3)
        }
    }

    private fun joinCompetition() {

        val token = getSharedPreferences("User", Context.MODE_PRIVATE).getString("token", "")

        Fuel.post(API.COMPETITIONS)
            .authentication()
            .bearer(token!!)
            .jsonBody("{}")
            .responseString{ request, response, result ->

                print("resp: $response")

                when(result){
                    is Result.Success -> {

                        val resp = JSONObject(result.get())

                        print("resp: ${result.get()}")


                        compID = resp.getInt("id")
                        Log.d("compID", compID.toString())

                        val questions = resp.getJSONArray("questions")

                        for (i in 0 until questions.length()){

                            val obj = questions.getJSONObject(i).getJSONObject("question")

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

                        if (resp.getString("status").equals("STARTED")) {
                            val user = resp.getJSONObject("player1")
                            this.player1 = User(user.getInt("id"),
                                user.getString("firstName"),
                                user.getString("email"))

//                            player1NameTV.text = this.player1!!.name
                            subscribeToCompetition(compID)
                        }
                        else if (resp.getString("status").equals("RUNNING")){

                            val user1 = resp.getJSONObject("player2")
                            this.player1 = User(user1.getInt("id"),
                                user1.getString("firstName"),
                                user1.getString("email"))

                            val user2 = resp.getJSONObject("player1")
                            this.player2 = User(user2.getInt("id"),
                                user2.getString("firstName"),
                                user2.getString("email"))

//                            player1NameTV.text = this.player1!!.name
//                            player2NameTV.text = this.player2!!.name

//                            changeQuestion(resp.getInt("questionIndex"))

                            subscribeToCompetition(compID)
                        }
                    }

                    is Result.Failure -> {
                        val err = result.getException()

                        when(response.statusCode){
                            401, 403 -> Toast.makeText(this, JSONObject(result.get()).getString("message"), Toast.LENGTH_SHORT).show()
                            404 -> Toast.makeText(this, "An error occurred, Try again later!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun subscribeToCompetition(compId: Int) {

        stompClient!!.withClientHeartbeat(1000).withServerHeartbeat(1000)
        stompClient!!.connect()

        stompClient!!.lifecycle()
            .subscribe {
                lifecycle ->

                when(lifecycle.type){
                    LifecycleEvent.Type.OPENED -> {
                        Log.d("stompLifeCycle", "opened")
                        connectSocket(compId)
                    }

                    LifecycleEvent.Type.ERROR ->
                        Log.d("stompLifeCycle", lifecycle.exception.toString())

                    LifecycleEvent.Type.CLOSED ->
                        Log.d("stompLifeCycle", "closed")

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT ->
                        Log.d("stompLifeCycle", "failed")

                }
            }

//        compositeDisposable!!.add(dispLifecycle)

        Log.d("testaa", "/app/competition/$compId/subscription")

    }

    @SuppressLint("CheckResult")
    private fun connectSocket(compId: Int) {

        stompClient!!.send("/app/competition/$compId/subscription")
            .subscribe(
                {
                    Log.d("Subscription", "send to subscription")
                },
                {
                        throwable -> Log.e("Subscription", "Error on subscribe topic", throwable)
                }
            )

        stompClient!!.topic("/topic/competition/$compId/questions")
            .subscribe (
                {
                        topicMessage -> Log.d("Questions", topicMessage.payload)

                    val jsonObj = JSONObject(topicMessage.payload)

                    if (jsonObj.getString("status") == "RUNNING"){

                        val user = jsonObj.getJSONObject("player2")

                        if (player2 == null) {
                            this.player2 = User(
                                user.getInt("id"),
                                user.getString("firstName"),
                                user.getString("email")
                            )

//                            player2NameTV.text = this.player2!!.name
                        }

                        this.questionIndex = jsonObj.getInt("questionIndex")


                        runOnUiThread {
                            changeQuestion(this.questionIndex)
                        }
                    }
                }
                ,
                {
                        throwable -> Log.e("Questions", "Error on subscribe topic", throwable)
                }
            )

        stompClient!!.topic("/topic/competition/$compId/next")
            .subscribe({
                    topicMessage ->
                Log.d("Next", "Received " + topicMessage.payload)

                val jsonObj = JSONObject(topicMessage.payload)

                if (jsonObj.getString("status") == "RUNNING"){

                    val user = jsonObj.getJSONObject("player2")

                    if (player2 == null) {
                        this.player2 = User(
                            user.getInt("id"),
                            user.getString("firstName"),
                            user.getString("email")
                        )

//                        player2NameTV.text = this.player2!!.name
                    }

                    this.questionIndex = jsonObj.getInt("questionIndex")


                    runOnUiThread {
                        changeQuestion(this.questionIndex)
                    }
                }

            }, {
                    throwable -> Log.e("Next", "Error on subscribe topic", throwable)
            })

        stompClient!!.topic("/topic/competition/$compId/ended")
            .subscribe({
                    topicMessage ->
                Log.d("Ended", "Received " + topicMessage.payload)

            }, {
                    throwable -> Log.e("Ended", "Error on subscribe topic", throwable)
            })
    }

    private fun changeQuestion(index: Int){

        questionTV.text = items[index].content
        answer1TV.text = items[index].answers[0].content
        answer2TV.text = items[index].answers[1].content
        answer3TV.text = items[index].answers[2].content
        answer4TV.text = items[index].answers[3].content

        resetAnswers()
    }

    @SuppressLint("CheckResult")
    private fun evaluateAnswers(chosenAnswer: Int) {

        Log.d("answerUrl", "/app/competition/$compID/question/${items.get(questionIndex).id}")
        Log.d("answerObj", "{id: ${items[questionIndex].answers[chosenAnswer].id}}")

        stompClient!!
            .send("/app/competition/$compID/question/${items.get(questionIndex).id}/answer/${items[questionIndex].answers[chosenAnswer].id}/${player1!!.id}")
            .subscribe(
                {
                    Log.d("answerSend", "send answer")
                },
                {
                        throwable -> Log.e("answerSend", "Error on send answer", throwable)
                }
            )

        when {
            items[questionIndex].answers[0].status -> answer1Bg.setImageResource(R.drawable.frame_gbg)
            items[questionIndex].answers[1].status -> answer2Bg.setImageResource(R.drawable.frame_gbg)
            items[questionIndex].answers[2].status -> answer3Bg.setImageResource(R.drawable.frame_gbg)
            items[questionIndex].answers[3].status -> answer4Bg.setImageResource(R.drawable.frame_gbg)
        }

        when(chosenAnswer){

            0 -> {
                if (!items[questionIndex].answers[0].status) {
                    answer1Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
//                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
//                    showGIF(R.raw.correct)
                    score++
                }

                answer1Icn.setImageResource(R.drawable.radio_btn_on)
            }

            1 -> {
                if (!items[questionIndex].answers[1].status){
                    answer2Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
//                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
//                    showGIF(R.raw.correct)
                    score++
                }

                answer2Icn.setImageResource(R.drawable.radio_btn_on)
            }

            2 -> {
                if (!items[questionIndex].answers[2].status){
                    answer3Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
//                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
//                    showGIF(R.raw.correct)
                    score++
                }

                answer3Icn.setImageResource(R.drawable.radio_btn_on)
            }

            3 -> {
                if (!items[questionIndex].answers[3].status){
                    answer4Bg.setImageResource(R.drawable.frame_rbg)
                    resultMsgTV.setText(R.string.wrong_answer)
//                    showGIF(R.raw.wrong)
                }
                else{
                    resultMsgTV.setText(R.string.correct_answer)
//                    showGIF(R.raw.correct)
                    score++
                }

                answer4Icn.setImageResource(R.drawable.radio_btn_on)
            }

        }

    }

    private fun resetAnswers(){
        answer1Bg.setImageResource(R.drawable.frame_ybg)
        answer2Bg.setImageResource(R.drawable.frame_ybg)
        answer3Bg.setImageResource(R.drawable.frame_ybg)
        answer4Bg.setImageResource(R.drawable.frame_ybg)

        answer1Icn.setImageResource(R.drawable.radio_btn_off)
        answer2Icn.setImageResource(R.drawable.radio_btn_off)
        answer3Icn.setImageResource(R.drawable.radio_btn_off)
        answer4Icn.setImageResource(R.drawable.radio_btn_off)
    }

    override fun onDestroy() {
        super.onDestroy()
        stompClient!!.disconnect()
    }
}