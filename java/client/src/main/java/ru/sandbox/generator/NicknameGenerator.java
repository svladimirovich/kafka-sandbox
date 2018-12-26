package ru.sandbox.generator;

import java.util.Arrays;
import java.util.Stack;

public class NicknameGenerator {

    public static void main(String[] args) {
        NicknameGenerator loginGenerator = new NicknameGenerator();
        for(int index = 0; index < 20; index++) {
            System.out.println("Generated login: " + loginGenerator.generateNickname());
        }
    }

    private String[] adjectives = {
            "efficient", "pleasant", "ninja", "consistent", "odd", "former", "automatic", "mighty", "dangerous",
            "dramatic", "used", "sorry", "accurate", "mental", "rare", "sudden", "tiny", "electrical", "responsible",
            "lucky", "unlikely", "significant", "alive", "different", "traditional", "entire", "impossible",
            "substantial", "massive", "embarrassed", "successfully", "decent", "realistic", "expensive", "several",
            "nervous", "reasonable", "practical", "boring", "large", "suspicious", "mad", "friendly", "distinct",
            "popular", "wonderful", "aware", "willing", "difficult", "legal", "lonely", "capable", "sexual", "guilty",
            "useful", "obvious", "similar", "educational", "huge", "foreign", "basic", "political", "famous",
            "southern", "curious", "intelligent", "weak", "cultural", "hungry", "administrative", "existing",
            "serious", "psychological", "angry", "awkward", "illigal", "actual", "informal", "mean", "able",
            "unhappy", "anxious", "inner", "historical", "obviously", "unable", "unfair", "terrible", "civil",
            "helpful", "ugly", "wooden", "healthy", "scared", "various", "poor", "hot", "visible", "united", "federal",
    };

    private String[] maleNicknames =  {
            "Emmitt", "Marc", "Julian", "Shane", "Elroy", "Brendon", "Moshe", "Isaac", "Ike", "Roscoe", "Rayford",
            "Mose", "Morris", "Earle", "Sammy", "Elijah", "Riley", "Rey", "Junior", "Ellis", "Diego", "Mervin",
            "Ambrose", "Odell", "Darin", "Rick", "Neil", "Gerry", "Milton", "Jose", "Lamar", "Allan", "Russ", "Enrique",
            "Bot", "Aldo", "Vito", "Quinn", "Vance", "Zachery", "Chad", "Burl", "Timothy", "Jae", "Josh", "Angelo",
            "Robert", "Rodrigo", "Elton", "Johnnie", "Hugo", "Leon", "Desmond", "Walton", "Theron", "Young", "Lon",
            "Markus", "Merlin", "Cedric", "Irwin", "Blaine", "Dominic", "Jewell", "Lindsay", "Ruben", "Joshua", "Denny",
            "Cliff", "Phil", "Pierre", "Ted", "Dylan", "Walter", "Horacio", "Bertram", "Antone", "Scot", "Renaldo",
            "Lenard", "Colby", "Sam", "Trent", "Ed", "Dustin", "Eddie", "Sammie", "Otis", "Buddy", "Dante", "Refugio",
            "Hong", "Santos", "Keneth", "Mickey", "Avery", "Collin", "Son", "Byron", "Abel",
    };

    private String[] femaleNicknames = {
            "Jenna", "Marisol", "Ronda", "Berta", "Patricia", "Sondra", "Pamela", "Mindy", "Liz", "Selena", "Luisa",
            "Teresa", "Sallie", "Tracie", "Deidre", "Erika", "Ginger", "Kathie", "Roxie", "Jeri", "Marva", "Wilma",
            "Roxanne", "Diana", "Letitia", "Rosella", "Eve", "Natalia", "Dee", "Amy", "Marina", "Florence", "Tiffany",
            "Emilia", "Susanne", "Ivy", "Pam", "Mitzi", "Lakisha", "Jana", "Florine", "Rowena", "Britney", "Imogene",
            "Lorna", "Corina", "Josephine", "Brandy", "Carmela", "Agnes", "Sonja", "Janice", "Ester", "Jane", "Felecia",
            "Cassie", "Dixie", "Eugenia", "Michele", "Jewell", "Joy", "Aisha", "Isabel", "Petra", "Charmaine", "Eileen",
            "Maritza", "Sheryl", "Leanna", "Darla", "Olga", "Allison", "Mayra", "Phoebe", "Loraine", "Lauri", "Bianca",
            "Polly", "Lorie", "Alissa", "Iris", "Annabelle", "Amanda", "Miranda", "Margarita", "Manuela", "Gwendolyn",
            "Miriam", "Lena", "Lilian", "Beryl", "Ingrid", "Eddie", "Guadalupe", "Rosanne", "Tonia", "Sherri", "Gladys",
            "Annie", "Patsy",
    };

    public NicknameGenerator() {
//        Stack<String> adjectives = new Stack<>();
//        adjectives.addAll(Arrays.asList(this.adjectives));
//
//        Stack<String> maleNicknames = new Stack<>();
//        maleNicknames.addAll(Arrays.asList(this.maleNicknames));
//
//        Stack<String> femaleNicknames = new Stack<>();
//        femaleNicknames.addAll(Arrays.asList(this.femaleNicknames));
    }

    public String generateNickname() {
        int adjectiveIndex = (int)Math.floor(Math.random() * 100);
        int nicknameIndex = (int)Math.floor(Math.random() * 100);
        boolean isMale = (Math.random() > 0.5);

        if(isMale)
            return String.format("%s_%s", this.adjectives[adjectiveIndex], this.maleNicknames[nicknameIndex]).toLowerCase();
        else
            return String.format("%s_%s", this.adjectives[adjectiveIndex], this.femaleNicknames[nicknameIndex]).toLowerCase();
    }
}
