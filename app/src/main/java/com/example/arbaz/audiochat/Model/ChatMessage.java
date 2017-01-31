package com.example.arbaz.audiochat.Model;

/**
 * Created by arbaz on 5/10/16.
 */
public class ChatMessage {
    public boolean right;
    public String message;

    public ChatMessage(boolean right, String message) {
        this.right = right;
        this.message = message;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
