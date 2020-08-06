package com.example.braintrainerappkotlin.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import com.example.braintrainerappkotlin.R
import kotlinx.android.synthetic.main.activity_guess.*
import kotlin.properties.Delegates
import kotlin.random.Random

private const val MAX_NUMBER = 20
private const val AMOUNT_OF_TIME_FOR_QUESTION = 30000

class GuessActivity : AppCompatActivity() {

    private var score: String = ""
    private var sumOfTwoNumbers: Int = 0
    private var points: Int = 0
    private var questionsAnswered: Int = 0
    lateinit var timer: CountDownTimer

    val displayMetrics = DisplayMetrics()


    var width by Delegates.notNull<Int>()
    var height by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels
    }

    fun onStart(view: View) {
        goButton.visibility = View.INVISIBLE
        answerGridLayout.visibility = View.VISIBLE

        initNewQuestion()
    }

    fun onOptionClicked(view: View) {
        val clickedButton: Button = view as Button
        timer.cancel()
        if(questionsAnswered >= 19) {


            if (clickedButton.text == sumOfTwoNumbers.toString()) {
                points++
            }

            questionsAnswered++

            actualizePoints()
            initEndGameScreen()

        } else {
            if (clickedButton.text == sumOfTwoNumbers.toString()) {
                points++
            }
            questionsAnswered++
            initNewQuestion()
        }


    }

    @SuppressLint("SetTextI18n")
    fun initNewQuestion() {
        val twoNumbers: List<Int> = List(2) { Random.nextInt(0, MAX_NUMBER) }
        sumOfTwoNumbers = twoNumbers[0] + twoNumbers[1]
        val threeFakeAnswers: List<Int> = List(3) { Random.nextInt(0, MAX_NUMBER * 2) }

        val allAnswersNotShuffled = listOf<Int>(sumOfTwoNumbers, threeFakeAnswers[0], threeFakeAnswers[1], threeFakeAnswers[2])

        val allAnswersShuffled = allAnswersNotShuffled.shuffled()

        leftTopButton.text = allAnswersShuffled[0].toString()
        rightTopButton.text = allAnswersShuffled[1].toString()
        leftBottomButton.text = allAnswersShuffled[2].toString()
        rightBottomButton.text = allAnswersShuffled[3].toString()

        calculationTextView.text = "${twoNumbers[0]} + ${twoNumbers[1]}"

        actualizePoints()

        timer = object : CountDownTimer(AMOUNT_OF_TIME_FOR_QUESTION.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateSeconds((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                questionsAnswered++
                initNewQuestion()
            }
        }
        timer.start()

    }

    private fun actualizePoints() {
        score = "$points / $questionsAnswered"
        chancesTextView.text = score.toString()
    }

    fun updateSeconds(seconds: Int) {
        var secondsString: String = seconds.toString()

        if (seconds <= 9) {
            secondsString = "0$seconds"
        }

        timeTextView.text = secondsString

    }

    private fun initEndGameScreen() {
        playAgainButton.visibility = View.VISIBLE
        answerGridLayout.visibility = View.INVISIBLE
        calculationTextView.visibility = View.INVISIBLE
        timeTextView.visibility = View.INVISIBLE



        chancesTextView.animate().translationXBy((-width / 2 + 200).toFloat()).translationYBy((height / 3).toFloat()).scaleXBy(2F).scaleYBy(2F)
    }

    fun onPlayAgain(view: View) {
        playAgainButton.visibility = View.INVISIBLE
        answerGridLayout.visibility = View.VISIBLE
        calculationTextView.visibility = View.VISIBLE
        timeTextView.visibility = View.VISIBLE

        chancesTextView.animate().translationXBy((width / 2 - 200).toFloat()).translationYBy(-(height / 3).toFloat()).scaleXBy(-2F).scaleYBy(-2F)

        score = "0/0"
        questionsAnswered = 0
        points = 0
        actualizePoints()
    }

}