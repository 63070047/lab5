package com.example.lab5;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
public class WordPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Word words = new Word();

    @RequestMapping(value = "/addBad/{s}", method = RequestMethod.GET)
    public ArrayList<String> addBadWord(@PathVariable("s") String s){
        words.badWords.add(s);
        return words.badWords;
    }

    @RequestMapping(value = "/delBad/{s}", method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("s") String s){
        words.badWords.removeIf( name -> name.equals(s));
        return words.badWords;
    }

    @RequestMapping(value = "/addGood/{s}", method = RequestMethod.GET)
    public ArrayList<String> addGoodWord(@PathVariable("s") String s){
        words.goodWords.add(s);
        return words.goodWords;
    }

    @RequestMapping(value = "/delGood/{s}", method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("s") String s){
        words.goodWords.removeIf( name -> name.equals(s));
        return words.goodWords;
    }

    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence(){
        return getSentence();
    }

    @RequestMapping(value = "/proof/{s}", method = RequestMethod.GET)
    public String proofSentence(@PathVariable("s") String s){
        boolean isFoundGood = false;
        boolean isFoundBad = false;
        for (int i = 0; i < words.goodWords.size(); i++) {
            if (s.contains(words.goodWords.get(i))){
                isFoundGood = true;
            }
        }
        for (int i = 0; i < words.badWords.size(); i++) {
            if (s.contains(words.badWords.get(i))){
                isFoundBad = true;
            }
        }
        if (isFoundGood && !isFoundBad){
            rabbitTemplate.convertAndSend("Direct","good",s);
            return "Found Good Word";
        } else if (isFoundBad && !isFoundGood) {
            rabbitTemplate.convertAndSend("Direct","bad",s);
            return "Found Bad Word";
        }
        else if (isFoundBad && isFoundGood){
            rabbitTemplate.convertAndSend("Fanout","",s);
            return "Found Bad & Good Word";
        }
        else {
            return "Not Found";
        }
    }
}
