package com.example.quizapp.constants;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static String categoryId;
    public static String categoryName;
    public static User currentUser;
    public static List<Question> questions = new ArrayList<>();
    public static final String STR_PUSH = "pushNotification";
}
