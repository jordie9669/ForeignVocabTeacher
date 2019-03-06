package com.example.c00204110.languagevocabteacher;
public class Words {
    String name;
    String words;
    public Words(){
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWords() {
        return words;
    }
    public void setWords(String words) {
        this.words = words;
    }
    public Words(String name, String words) {
        this.name = name;
        this.words = words;
    }
}
