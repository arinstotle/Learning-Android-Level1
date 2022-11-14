package com.example.myapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

private const val EXTRA_ANSWER_IS_TRUE = "com.example.myapp.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.myapp.answer_shown"
const val EXTRA_HINTS = "com.example.myapp.hints"
private const val TAG = "CheatActivity"
private const val KEY_ANSWERED = "ifAnswered"

class CheatActivity : AppCompatActivity() {

    private lateinit var showAnswerButton: Button
    private lateinit var answerTextView: TextView
    private lateinit var versionTextView: TextView
    private lateinit var hintsTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        val quizViewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        ViewModelProvider(this, quizViewModelFactory)[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        versionTextView = findViewById(R.id.version_text_view)
        hintsTextView = findViewById(R.id.hints_text_view)

        versionTextView.text = "Version SDK " + Build.VERSION.SDK_INT
        versionTextView.visibility = View.VISIBLE

        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, quizViewModel.isAnswerShown)
            putExtra(EXTRA_HINTS, quizViewModel.numberOfHints)
        }
        setResult(Activity.RESULT_OK, data)

        quizViewModel.answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        quizViewModel.numberOfHints = intent.getIntExtra(EXTRA_HINTS, 0)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {

            if (quizViewModel.numberOfHints < 3) {
                val answerText = when {
                quizViewModel.answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
                answerTextView.setText(answerText)
                answerTextView.visibility = View.VISIBLE;
                quizViewModel.isAnswerShown = true;
                quizViewModel.numberOfHints++
                hintsTextView.text = "Number of clues left: " + (3 - quizViewModel.numberOfHints)
                hintsTextView.visibility = View.VISIBLE
            }
            else {
                hintsTextView.text = "No more hints!"
                hintsTextView.visibility = View.VISIBLE
            }
            setAnswerShownResult();
        }
    }

    private fun setAnswerShownResult()
    {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, quizViewModel.isAnswerShown)
            putExtra(EXTRA_HINTS, quizViewModel.numberOfHints)
        }
        setResult(Activity.RESULT_OK, data)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")

        savedInstanceState.putBoolean(KEY_ANSWERED, quizViewModel.isAnswerShown)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, hintsOf : Int): Intent
        {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_HINTS, hintsOf)
            }
        }
    }
}
