package com.example.apt_agile_piano_teaching.logger;

public enum Action {
    INSERT("ha inserito"),
    DELETE("ha eliminato"),
    UPDATE("ha modificato");

    private final String action;

    Action(final String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return this.action;
    }
}
