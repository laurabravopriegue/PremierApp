package com.example.laurabravopriegue.petiquiz;

/**
 * Created by laurabravopriegue on 31/12/16.
 */

public class Question {

    private String mQuestion;
    private boolean mAnswerTrue;
    public Boolean mUserAnswer;

    public String getTextResId() {
        return mQuestion;
    }

    public void setTextResId(String question) {
        mQuestion = question;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public Question(String question, boolean answerTrue, Boolean userAnswer) {
        mQuestion = question;
        mAnswerTrue = answerTrue;
        mUserAnswer = userAnswer;
    }
}