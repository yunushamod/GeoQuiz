package com.yunushamod.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {
    private var cheatAnswer = false
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        cheatAnswer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        showAnswerButton.setOnClickListener{
            val answerText = when{
                cheatAnswer -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerForResult(cheatAnswer)
        }
    }

    private fun setAnswerForResult(cheatAnswer: Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_IS_SHOWN, true)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object{
        private const val EXTRA_ANSWER_IS_TRUE = "com.yunushamod.android.geoquiz.answer_is_true"
        const val EXTRA_ANSWER_IS_SHOWN = "com.yunushamod.android.geoquiz.answer_is_shown"
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent{
            return Intent(packageContext, CheatActivity::class.java)
                .apply{
                    putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                }
        }
    }
}