package com.example.myapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_INDEX_ALL = "index_all"
private const val KEY_INDEX_CORRECT = "index_correct"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: Button
    private lateinit var finishButton: Button
    private lateinit var cheatButton: Button

    /*private val questions = listOf(
        //все имеющиеся вопросы в приложении
        Question(R.string.question_un, true),
        Question(R.string.question_australia, true),
        Question(R.string.question_atlas, false),
        Question(R.string.question_dublin, true),
        Question(R.string.question_gaborone, true),
        Question(R.string.question_lena, false),
        Question(R.string.question_mayan, false),
        Question(R.string.question_paris, false),
        Question(R.string.question_sicily, true),
    )

    private var currentIndex = 0; //индекс для генерации вопросов
    private var counterAnswers = 0; //счётчик вопросов, на которые дан ответ
    private var correctAnswers = 0; //счётчик правильных вопросов*/

    private val quizViewModel: QuizViewModel by lazy {
        val quizViewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        ViewModelProvider(this, quizViewModelFactory)[QuizViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val counterAnswers = savedInstanceState?.getInt(KEY_INDEX_ALL, 0) ?: 0
        val correctAnswers = savedInstanceState?.getInt(KEY_INDEX_CORRECT, 0) ?: 0

        quizViewModel.updateCurrentIndex(currentIndex)
        quizViewModel.updateCorrectAnswers(correctAnswers)
        quizViewModel.updateCounterAnswers(counterAnswers)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)
        finishButton = findViewById(R.id.finish_button)
        cheatButton = findViewById(R.id.cheat_button)

        trueButton.setOnClickListener {

            if (!(quizViewModel.currentQuestionIsAnswered)) //если на вопрос нет ответа
            {
                checkAnswer(true) //проверить вопрос
                quizViewModel.isAnswerShown = false
                quizViewModel.updateIsCheater(false)
                quizViewModel.goAnswered() //отметить его отвеченным
                quizViewModel.incrementAnswers() //увеличить число отвеченных вопросов

                if (quizViewModel.getCounterAnswers == quizViewModel.getQuestionsSize) // если на все вопросы есть ответ
                {
                    finishButton.visibility = View.VISIBLE;
                }
            }

        }

        falseButton.setOnClickListener {
            if (!(quizViewModel.currentQuestionIsAnswered)) //если на вопрос нет ответа
            {
                checkAnswer(false)//проверить вопрос
                quizViewModel.isAnswerShown = false
                quizViewModel.updateIsCheater(false)
                quizViewModel.goAnswered()//отметить его отвеченным
                quizViewModel.incrementAnswers() //увеличить число отвеченных вопросов

                if (quizViewModel.getCounterAnswers == quizViewModel.getQuestionsSize) // если на все вопросы есть ответ
                {
                    finishButton.visibility = View.VISIBLE;
                }
            }
        }

        finishButton.setOnClickListener {

            val res : Double = ((quizViewModel.getCorrectAnswers).toDouble() / (quizViewModel.getQuestionsSize).toDouble()) * 100
            val messageRes = "Your result: $res%"
            Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()

            }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {

            quizViewModel.decrementCurrentIndex()

            if (quizViewModel.getCurrentIndex < 0)
            {
                quizViewModel.moveToPrev()
            }

            updateQuestion()
        }

        cheatButton.setOnClickListener { view ->
            //val intent = Intent(this, CheatActivity::class.java) //явный интент
            //явный интент используется для запуска активностей одного приложения. Неявный - когда
            //активность одного приложения запускает активность другого

    val answerIsTrue = quizViewModel.currentQuestionAnswer
            val hintsOf = quizViewModel.numberOfHints
    val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue, hintsOf)

    //startActivityForResult(intent, REQUEST_CODE_CHEAT)

    val option = ActivityOptionsCompat.makeClipRevealAnimation(
        view, 0, 0,
        view.width, view.height
    )

    resultLauncher.launch(intent, option)

        }

        updateQuestion()

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result->
        if (result.resultCode == Activity.RESULT_OK)
        {
            val parameter = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.updateIsCheater(parameter)
            quizViewModel.numberOfHints = result.data?.getIntExtra(EXTRA_HINTS, 0) ?:0
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.getCurrentIndex)
        savedInstanceState.putInt(KEY_INDEX_ALL, quizViewModel.getCounterAnswers)
        savedInstanceState.putInt(KEY_INDEX_CORRECT, quizViewModel.getCorrectAnswers)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    private fun updateQuestion()
    {
        //Log.d(TAG, "Updating question text", Exception())
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer : Boolean)
    {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        var messageResId : Int

        if (quizViewModel.getIsCheater) {
            messageResId = R.string.judgment_toast
        }

        else {
            if (userAnswer == correctAnswer) {
                messageResId = R.string.correct_toast
                quizViewModel.incrementCorrectAnswers()
            } else {
                messageResId = R.string.incorrect_toast
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

}
