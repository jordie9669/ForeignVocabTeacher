package com.example.c00204110.languagevocabteacher;
public class MyWords {
    String name;
    String translation;
    String description;
    String myWords;
    public MyWords(){
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMyWords() {
        return myWords;
    }
    public void setMyWords(String myWords) {
        this.myWords = myWords;
    }
    public MyWords(String name, String myWords, String description, String translation) {
        this.name = name;
        this.myWords = myWords;
        this.translation = translation;
        this.description = description;
    }
}
