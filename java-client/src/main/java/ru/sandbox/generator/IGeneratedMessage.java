package ru.sandbox.generator;

public interface IGeneratedMessage {
    String getAuthorLogin();
    String getTitle();
    String getText();
    String toJsonString();
}
