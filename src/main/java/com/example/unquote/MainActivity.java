package com.example.unquote;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity<displaySize> extends AppCompatActivity {


    private static final String TAG = "SIZE_OF_ARRAY";
    int currentQuestionIndex;
    int totalCorrect;
    int totalQuestions;
    ArrayList<Question> questions;

    // TODO 3-A: Declare View member variables
    ImageView questionImageView;
    TextView questionTextView;
    TextView questionsRemainingTextView;
    Button answer0Button;
    Button answer1Button;
    Button answer2Button;
    Button answer3Button;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO 2-G: Show app icon in ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_unquote_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);

        setContentView(R.layout.activity_main);

        // TODO 3-B: assign View member variables
        questionImageView = findViewById(R.id.iv_main_question_image);
        questionsRemainingTextView = findViewById(R.id.tv_main_questions_remaining_count);
        questionTextView = findViewById(R.id.tv_main_question_title);
        answer0Button = findViewById(R.id.btn_main_answer_0);
        answer1Button = findViewById(R.id.btn_main_answer_1);
        answer2Button = findViewById(R.id.btn_main_answer_2);
        answer3Button = findViewById(R.id.btn_main_answer_3);
        submitButton = findViewById(R.id.btn_main_submit_answer);

        // TODO 4-E: set onClickListener for each answer Button
        answer0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnswerSelected(0);
            }
        });
        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnswerSelected(1);
            }
        });
        answer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnswerSelected(2);
            }
        });
        answer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnswerSelected(3);
            }
        });

        // TODO 5-A: set onClickListener for the submit answer Button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnswerSubmission();
            }
        });

        startNewGame();
    }

    // TODO 3-F: displayQuestion(Question question) {...}
    void displayQuestion(Question question){
        questionTextView.setText(question.questionText);
        questionImageView.setImageResource(question.imageId);
        answer0Button.setText(question.answer0);
        answer1Button.setText(question.answer1);
        answer2Button.setText(question.answer2);
        answer3Button.setText(question.answer3);
    }


    // TODO 3-C: displayQuestionsRemaining(int questionRemaining) {...}
    void displayQuestionsRemaining(int remainingQuestion){
        questionsRemainingTextView.setText(String.format("%d", remainingQuestion));
    }

    // TODO 4-A: onAnswerSelected(int answerSelected) {...}
    void onAnswerSelected(int playerChoice){
        Question currentQuestion = getCurrentQuestion();
        currentQuestion.playerAnswer = playerChoice;
        answer0Button.setText(currentQuestion.answer0);
        answer1Button.setText(currentQuestion.answer1);
        answer2Button.setText(currentQuestion.answer2);
        answer3Button.setText(currentQuestion.answer3);
        switch (playerChoice){
            case 0:
                answer0Button.setText(MessageFormat.format("✔{0}", currentQuestion.answer0));
//                answer3Button.setBackgroundResource(R.color.btn_select);
                break;
            case 1:
                answer1Button.setText(MessageFormat.format("✔{0}", currentQuestion.answer1));
//                answer3Button.setBackgroundResource(R.color.btn_select);
                break;
            case 2:
                answer2Button.setText(MessageFormat.format("✔{0}", currentQuestion.answer2));
//                answer3Button.setBackgroundResource(R.color.btn_select);
                break;
            case 3:
                answer3Button.setText(MessageFormat.format("✔{0}", currentQuestion.answer3));
//                answer3Button.setBackgroundResource(R.color.btn_select);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + playerChoice);
        }
    }

    void onAnswerSubmission() {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion.isCorrect()) {
            totalCorrect = totalCorrect + 1;
        }else if (currentQuestion.playerAnswer == -1){
            return;
        }
        questions.remove(currentQuestion);

        // TODO 3-D.i: Uncomment the line below after implementing displayQuestionsRemaining(int)
         displayQuestionsRemaining(questions.size());

        if (questions.size() == 0) {
            String gameOverMessage = getGameOverMessage(totalCorrect, totalQuestions);

            // TODO 5-D: Show a popup instead
            AlertDialog.Builder gameOverDialogBuilder =
                    new AlertDialog.Builder(MainActivity.this);
            gameOverDialogBuilder.setCancelable(false);
            gameOverDialogBuilder.setTitle("Game Over!");
            gameOverDialogBuilder.setMessage(gameOverMessage);

            gameOverDialogBuilder.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startNewGame();
                }
            });
            gameOverDialogBuilder.setNegativeButton("Quit Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    endGame();
                }
            });
            gameOverDialogBuilder.create().show();

//            System.out.println(gameOverMessage);
        } else {
            chooseNewQuestion();

            // TODO 3-H.i: uncomment after implementing displayQuestion(Question)
             displayQuestion(getCurrentQuestion());
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void endGame() {
        finish();
        System.exit(0);
    }

    void startNewGame() {
        questions = new ArrayList<>();

        // TODO 2-H: Provide actual drawables for each of these questions!
        Question question0 = new Question(R.drawable.img_quote_0, "Pretty good advice,\n" +
                "and perhaps a scientist\n" +
                "did say it… Who\n" +
                "actually did?", "Albert Einstein", "Isaac Newton", "Rita Mae Brown", "Rosalind Franklin", 2);
        Question question1 = new Question(R.drawable.img_quote_1, "Was honest Abe\n" +
                "honestly quoted? Who\n" +
                "authored this pithy bit\n" +
                "of wisdom?", "Edward Stieglitz", "Maya Angelou", "Abraham Lincoln", "Ralph Waldo Emerson", 0);
        Question question2 = new Question(R.drawable.img_quote_2, "Easy advice to read,\n" +
                "difficult advice to\n" +
                "follow — who actually", "Martin Luther King Jr.", "Mother Teresa", "Fred Rogers", "Oprah Winfrey", 1);
        Question question3 = new Question(R.drawable.img_quote_3, "Insanely inspiring,\n" +
                "insanely incorrect\n" +
                "(maybe). Who is the\n" +
                "true source of this\n" +
                "inspiration?", "Nelson Mandela", "Harriet Tubman", "Mahatma Gandhi", "Nicholas Klein", 3);
        Question question4 = new Question(R.drawable.img_quote_4, "A peace worth striving\n" +
                "for — who actually\n" +
                "reminded us of this?", "Malala Yousafzai", "Martin Luther King Jr.", "Liu Xiaobo", "Dalai Lama", 1);
        Question question5 = new Question(R.drawable.img_quote_5, "Unfortunately, true —\n" +
                "but did Marilyn Monroe\n" +
                "convey it or did\n" +
                "someone else?", "Laurel Thatcher Ulrich", "Eleanor Roosevelt", "Marilyn Monroe", "Queen Victoria", 0);
        Question question6 = new Question(R.drawable.img_quote_6, "Here’s the truth, Will Smith did\n" +
                "say this, but in which movie?", "Independence Day", "Bad Boys",
                "Men In Black","The Pursuit of Happyness", 2);
        Question question7 = new Question(R.drawable.img_quote_7, "Which TV funny gal actually\n" +
                "quipped this 1-liner?", "Ellen Degeneres", "Amy Poehler", "Betty White",
                "Tina Fay", 3);
        Question question8 = new Question(R.drawable.img_quote_8, "This mayor won’t get my vote —\n" +
                "but did he actually give this\n" +
                "piece of advice? And if not, who\n" +
                "did?", "Forrest Gump,\n" +
                "Forrest Gump", "Dorry, Finding\n" +
                "Nemo", "Esther Williams", "The Mayor, Jaws", 1);
        Question question9 = new Question(R.drawable.img_quote_9, "Her heart will go on, but whose\n" +
                "heart is it?", "Whitney Houston", "Diana Ross", "Celine Dion",
                "Mariah Carey", 0);
        Question question10 = new Question(R.drawable.img_quote_10, "He’s the king of something\n" +
                "alright — to whom does this\n" +
                "self-titling line belong to?", "Tony Montana,\n" +
                "Scarface", "Joker, The Dark\n" +
                "Knight", "Lex Luthor,\n" +
                "Batman v Superman", "Jack, Titanic", 3);
        Question question11 = new Question(R.drawable.img_quote_11, "Is “Grey” synonymous for\n" +
                "“wise”? If so, maybe Gandalf did\n" +
                "preach this advice. If not, who\n" +
                "did?", "Yoda, Star Wars", "Gandalf The Grey,\n" +
                "Lord of the Rings", "Dumbledore, Harry\n" +
                "Potter", "Uncle Ben, Spider-Man", 0);
        Question question12 = new Question(R.drawable.img_quote_12, "Houston, we have a problem\n" +
                "with this quote — which\n" +
                "space-traveler does this\n" +
                "catch-phrase actually belong to?", "Han Solo, Star Wars", "Captain Kirk, Star\n" +
                "Trek", "Buzz Lightyear, Toy\n" +
                "Story", "Jim Lovell, Apollo 13", 2);



        questions.add(question0);
        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        questions.add(question5);
        questions.add(question6);
        questions.add(question7);
        questions.add(question8);
        questions.add(question9);
        questions.add(question10);
        questions.add(question11);
        questions.add(question12);

        totalCorrect = 0;
        int questionNumToDisplay = 6;
        while(questionNumToDisplay > 0){
            int questionIndexToRemove = generateRandomNumber(questions.size());
            Log.i(TAG, "startNewGame with questions: " + questions.toString());
            for (int i = 0; i < questions.size(); i++) {
                System.out.println(questions.get(i));
                Log.i(TAG, "startNewGame: "+ questions.get(i).toString());
            }
            questions.remove(questionIndexToRemove);
            questionNumToDisplay--;
        }

        totalQuestions = questions.size();

        Question firstQuestion = chooseNewQuestion();

        // TODO 3-D.ii: Uncomment the line below after implementing displayQuestionsRemaining(int)
         displayQuestionsRemaining(questions.size());

        // TODO 3-H.ii: Uncomment after implementing displayQuestion(Question)
         displayQuestion(firstQuestion);
    }

//    int displaySize = 6;
//    while(displaySize > 0){
//        int questionIndexToRemove = generateRandomNumber(questions.size());
//        displaySize--;
//    }

    Question chooseNewQuestion() {
        int newQuestionIndex = generateRandomNumber(questions.size());
        currentQuestionIndex = newQuestionIndex;
        return questions.get(currentQuestionIndex);
    }

    int generateRandomNumber(int max) {
        double randomNumber = Math.random();
        double result = max * randomNumber;
        return (int) result;
    }

    Question getCurrentQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        return currentQuestion;
    }

    String getGameOverMessage(int totalCorrect, int totalQuestions) {
        if (totalCorrect == totalQuestions) {
            return "You got all " + totalQuestions + " right! You won!";
        } else {
            return "You got " + totalCorrect + " right out of " + totalQuestions + ". Better luck next time!";
        }
    }
}

