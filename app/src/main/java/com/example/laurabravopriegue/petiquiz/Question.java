package com.example.laurabravopriegue.petiquiz;

/**
 * Created by laurabravopriegue on 31/12/16.
 */

public class Question {

    private String mQuestion;
    private String mExplanation;
    private boolean mAnswerTrue;
    public Integer mUserAnswer;

    public String getTextResId() {
        return mQuestion;
    }
    public String getExplanation() {
        return mExplanation;
    }

    public void setTextResId(String question) {
        mQuestion = question;
    }
    public void setExplanation(String explanation) { mExplanation =  explanation; }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public Question(String question, boolean answerTrue, Integer userAnswer, String explanation) {
        mQuestion = question;
        mAnswerTrue = answerTrue;
        mUserAnswer = userAnswer;
        mExplanation = explanation;
    }
}