package com.example.quizapp.model;

public class Ranking {

    private String userName;

    private Long score;

    public Ranking() {
    }

    public Ranking(String userName, Long score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
