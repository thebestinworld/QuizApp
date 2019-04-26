package com.example.quizapp.model;

public class QuestionScore {

    private String questionScore;
    private String user;
    private String score;
    private String categoryId;
    private String categoryName;

    public QuestionScore() {
    }

    public QuestionScore(String questionScore, String user, String score, String categoryId, String categoryName) {
        this.questionScore = questionScore;
        this.user = user;
        this.score = score;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(String questionScore) {
        this.questionScore = questionScore;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
