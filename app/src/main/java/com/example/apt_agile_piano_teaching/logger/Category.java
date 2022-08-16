package com.example.apt_agile_piano_teaching.logger;

public enum Category {
    LESSON("lezione"),
    USER("utente");

    private final String category;

    Category(final String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return this.category;
    }
}
