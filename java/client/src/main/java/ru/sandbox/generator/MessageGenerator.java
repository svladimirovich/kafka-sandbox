package ru.sandbox.generator;

import com.google.gson.Gson;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

public class MessageGenerator {

    private class WritableMessage implements IGeneratedMessage {
        private String text;
        private String author;
        private String title;

        public void setAuthorLogin(String login) {
            this.author = login;
        }
        public String getAuthorLogin() {
            return author;
        }
        public void setTitle(String title) { this.title = title; }
        public String getTitle() { return this.title; }
        public void setText(String text) { this.text = text; }
        public String getText() { return text; }

        @Override
        public String toJsonString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    private Lorem lorem;
    private NicknameGenerator login;


    public MessageGenerator() {
        this.lorem = LoremIpsum.getInstance();
        this.login = new NicknameGenerator();
    }

    public IGeneratedMessage generateMessage() {
        WritableMessage message = new WritableMessage();

        message.setAuthorLogin(login.generateNickname());
        message.setTitle(lorem.getTitle(1, 5));
        message.setText(lorem.getWords(2, 15));


        return message;
    }
}
