package com.example.android.androidmarki.ui.home.trivia.game

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.R
import com.example.android.androidmarki.ui.base.BaseViewModel

class TriviaGameViewModel : BaseViewModel() {
    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All TriviaGameQuestions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the TriviaGameQuestions in code...)
    private val triviaGameQuestions: MutableList<TriviaGameQuestion> = mutableListOf(
        TriviaGameQuestion(
            text = "What is Android Jetpack?",
            answers = listOf("all of these", "tools", "documentation", "libraries")
        ),
        TriviaGameQuestion(
            text = "Base class for Layout?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")
        ),
        TriviaGameQuestion(
            text = "Layout for complex Screens?",
            answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")
        ),
        TriviaGameQuestion(
            text = "Pushing structured data into a Layout?",
            answers = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick")
        ),
        TriviaGameQuestion(
            text = "Inflate layout in fragments?",
            answers = listOf(
                "onCreateView",
                "onActivityCreated",
                "onCreateLayout",
                "onInflateLayout"
            )
        ),
        TriviaGameQuestion(
            text = "Build system for Android?",
            answers = listOf("Gradle", "Graddle", "Grodle", "Groyle")
        ),
        TriviaGameQuestion(
            text = "Android vector format?",
            answers = listOf(
                "VectorDrawable",
                "AndroidVectorDrawable",
                "DrawableVector",
                "AndroidVector"
            )
        ),
        TriviaGameQuestion(
            text = "Android Navigation Component?",
            answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")
        ),
        TriviaGameQuestion(
            text = "Registers app with launcher?",
            answers = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher")
        ),
        TriviaGameQuestion(
            text = "Mark a layout for Data Binding?",
            answers = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>")
        )
    )
    private val _currentQuestion = MutableLiveData<TriviaGameQuestion>()
    val currentQuestion:LiveData<TriviaGameQuestion> = _currentQuestion
    private val _answers = MutableLiveData<MutableList<String>>()
    val answers:LiveData<MutableList<String>> = _answers
    private val _questionIndex = MutableLiveData(0)
    val questionIndex: LiveData<Int> = _questionIndex
    val numQuestions = ((triviaGameQuestions.size + 1) / 2).coerceAtMost(3)
    val checkedId = MutableLiveData<Int>()
    private val _gameUIData: MutableLiveData<GameUIData> = MutableLiveData()
    val gameUIData: LiveData<GameUIData> = _gameUIData

    init {
        randomizeQuestions()
    }

    private fun randomizeQuestions() {
        triviaGameQuestions.shuffle()
        setQuestion()
    }

    private fun setQuestion() {
        _currentQuestion.value = triviaGameQuestions[_questionIndex.value!!]
        // randomize the answers into a copy of the array
        _answers.value = _currentQuestion.value!!.answers.toMutableList()

        // and shuffle them
        _answers.value!!.shuffle()
    }

    fun submit() {
        val answerIndex = when (checkedId.value) {
            R.id.firstAnswerRadioButton -> 0
            R.id.secondAnswerRadioButton -> 1
            R.id.thirdAnswerRadioButton -> 2
            R.id.fourthAnswerRadioButton -> 3
            else -> -1
        }
        if (answerIndex != -1) {
            if (_answers.value!![answerIndex] == _currentQuestion.value!!.answers[0]) {
                _questionIndex.value = _questionIndex.value?.plus(1)
                if (_questionIndex.value!! < numQuestions) {
                    setQuestion()
                } else {
                    // We've won!  Navigate to the gameWonFragment.
                    _gameUIData.value = GameUIData(isWon = true)
                }
            } else {
                // Game over! A wrong answer sends us to the gameOverFragment.
                _gameUIData.value = GameUIData(isLost = true)
            }
        } else {
            _gameUIData.value = GameUIData(error = R.string.invalid_choice)
        }
    }
}