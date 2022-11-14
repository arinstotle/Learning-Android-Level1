package com.example.myapp

import android.content.Intent
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    private val questions = listOf(
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
    private var correctAnswers = 0; //счётчик правильных вопросов
    private var isCheater = false
     var answerIsTrue = false
     var isAnswerShown = false
     var numberOfHints = 0

    fun updateIsCheater(other : Boolean)
    {
        isCheater = other
    }

    val getIsCheater: Boolean
        get() = isCheater

    val currentQuestionAnswer: Boolean
        get() = questions[currentIndex].answer

    val currentQuestionText: Int
        get() = questions[currentIndex].textResId

    val currentQuestionIsAnswered: Boolean
        get() = questions[currentIndex].getAnswered()

    val getCounterAnswers: Int
        get() = counterAnswers

    val getCorrectAnswers: Int
        get() = correctAnswers

    val getQuestionsSize: Int
        get() = questions.size

    val getCurrentIndex: Int
        get() = currentIndex

    fun moveToNext()
    {
        currentIndex = (currentIndex + 1) % questions.size
    }

    fun moveToPrev()
    {
        currentIndex = questions.size - 1
    }

    fun goAnswered()
    {
        questions[currentIndex].answered()
    }

    fun incrementAnswers()
    {
        counterAnswers++
    }

    fun incrementCorrectAnswers()
    {
        correctAnswers++
    }

    fun decrementCurrentIndex()
    {
        currentIndex -= 1;
    }

    fun updateCurrentIndex(other : Int)
    {
        currentIndex = other
    }

    fun updateCounterAnswers(other : Int)
    {
        counterAnswers = other
    }

    fun updateCorrectAnswers(other : Int)
    {
        correctAnswers = other
    }
}
