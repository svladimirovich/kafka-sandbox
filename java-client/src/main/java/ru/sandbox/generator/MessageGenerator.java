package ru.sandbox.generator;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

public class MessageGenerator {

    private Lorem lorem;


    public MessageGenerator() {
        this.lorem = LoremIpsum.getInstance();
    }

    public IGeneratedMessage generateMessage() {
        IGeneratedMessage message = new IGeneratedMessage() {
            private String text;
            private String author;

            public void setText(String text) {
                this.text = text;
            }
            public void setAuthorLogin(String login) {
                this.author = login;
            }
            public String getText() {
                return text;
            }
            public String getAuthorLogin() {
                return author;
            }
        };

        message.setText("");
        message.setAuthorLogin("");

        return message;
    }
}
