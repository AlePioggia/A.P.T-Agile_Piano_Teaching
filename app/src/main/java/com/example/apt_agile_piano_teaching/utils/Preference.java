package com.example.apt_agile_piano_teaching.utils;

public enum Preference {
    EMAIL_PREFERENCE("emailPreference"),
    IMAGE_TEMPLATE_PREFERENCE("imageTemplatePreference"),
    IMAGE_TEMPLATE_NOT_NULl("imageTemplateNotNull");

    private final String preference;

    Preference(final String preference) {
        this.preference = preference;
    }

    @Override
    public String toString() {
        return this.preference;
    }
}
