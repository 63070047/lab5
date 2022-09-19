package com.example.lab5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Word implements Serializable {
     public ArrayList<String> goodWords = new ArrayList<>(Arrays.asList("happy", "enjoy", "life"));
     public ArrayList<String> badWords = new ArrayList<>(Arrays.asList("fuck", "olo"));
}
