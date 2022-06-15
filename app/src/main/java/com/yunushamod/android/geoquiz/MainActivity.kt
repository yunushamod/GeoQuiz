package com.yunushamod.android.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private val quizViewModel: QuizViewModel by lazy{
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        quizViewModel.currentIndex = savedInstanceState?.getInt(CURRENT_INDEX) ?: 0
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        trueButton.setOnClickListener{
            showAnswer(checkAnswer(true))
        }
        falseButton.setOnClickListener{
            showAnswer(checkAnswer(false))
        }
        cheatButton.setOnClickListener{
            // Start CheatActivity
            val currentAnswer = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, currentAnswer)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val options = ActivityOptions.makeClipRevealAnimation(it, 0, 0, it.width, it.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            }
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
            quizViewModel.isCheater = false
        }
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater = data?.getBooleanExtra(CheatActivity.EXTRA_ANSWER_IS_SHOWN, false) ?: false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_INDEX, quizViewModel.currentIndex)
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun showAnswer(answer: Boolean){
        val textRes = when{
            quizViewModel.isCheater -> R.string.judgement_toast
            answer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, getString(textRes), Toast.LENGTH_SHORT).show()
    }

    private fun checkAnswer(userAnswer: Boolean): Boolean{
        val currentAnswer = quizViewModel.currentQuestionAnswer
        return userAnswer == currentAnswer
    }

    companion object{
        private const val TAG = "MainActivity"
        private const val CURRENT_INDEX = "CurrentIndex"
        private const val REQUEST_CODE_CHEAT = 0
    }
}