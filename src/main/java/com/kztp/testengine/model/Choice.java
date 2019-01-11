package com.kztp.testengine.model;

public class Choice {
    private String text;

    public Choice() {
    }

    public Choice(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Choice))return false;
        return ((Choice) obj).text.equals(text);
    }
}
