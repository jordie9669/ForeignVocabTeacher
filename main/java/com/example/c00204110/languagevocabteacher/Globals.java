package com.example.c00204110.languagevocabteacher;
public class Globals {
        private static Globals instance;
        // Global variable
        private String data;
        private String selectedSector;
        private String selectedWord;
        private String mySelectedWord;
        private String translation;
        private String description;
        private String imageUri;
    private String selectedTranslations;
        // Restrict the constructor from being instantiated
        private Globals(){}
        public void setData(String d){
            this.data=d;
        }
        public void setSelectedSector(String s){
            this.selectedSector = s;
        }
        public void setSelectedWord(String w){this.selectedWord=w;}
        public void setMySelectedWord(String x){this.mySelectedWord=x;}
        public String getData(){
            return this.data;
        }
        public String getSelectedSector(){
            return this.selectedSector;
        }
        public String getSelectedWord(){return  this.selectedWord;}
        public String getMySelectedWord(){return  this.mySelectedWord;}
        public String getTranslation() {
        return translation;
    }
        public void setTranslation(String translation) {
        this.translation = translation;
    }
        public String getDescription() {
        return description;
    }
    public String getSelectedTranslations() {
        return selectedTranslations;
    }
    public void setSelectedTranslations(String selectedTranslations) {
        this.selectedTranslations = selectedTranslations;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageUri() {
        return imageUri;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    public static synchronized Globals getInstance(){
            if(instance==null){
                instance=new Globals();
            }
            return instance;
        }
    }



