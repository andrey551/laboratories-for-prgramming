package com.example.lab8_cli.utility;

import com.example.lab8_cli.StatsBundle.*;

import java.io.Serializable;
import java.util.ListResourceBundle;
import java.util.Locale;

public enum Languages implements Serializable {
    RUSSIAN("Русский") {
        @Override
        public ListResourceBundle getResources() {
            return new StatsBundle_ru_RU();
        }

        @Override
        public Locale getLocale() {
            return new Locale("ru");
        }
    },
    SPANISH("Español") {
        @Override
        public ListResourceBundle getResources() {
            return new StatsBundle_es_EC();
        }

        @Override
        public Locale getLocale() {
            return new Locale("es", "EC");
        }
    },
    HUNGARIAN("Magyar") {
        @Override
        public ListResourceBundle getResources() {
            return new StatsBundle_hu_HU();
        }

        @Override
        public Locale getLocale() {
            return new Locale("hu", "HU");
        }
    },
    GERMANY("Germany") {
        @Override
        public ListResourceBundle getResources() {
            return new StatsBundle_de_DE();
        }

        @Override
        public Locale getLocale() {
            return new Locale("de", "DE");
        }
    },
    VIETNAM("Việt Nam") {
        @Override
        public ListResourceBundle getResources() {
            return new StatsBundle_vi_VN();
        }
        @Override
        public Locale getLocale() {
            return new Locale("vi", "VI");
        }
    };

    public static final long serialVersionUID = 42L;

    private final String name;

    Languages(String name) {
        this.name = name;
    }

    public abstract ListResourceBundle getResources();

    public abstract Locale getLocale();

    public String getName() {
        return name;
    }

    public static Languages getEnum(String value) {
        for (Languages language : values()) {
            if (language.getName().equals(value)) return language;
        }
        return null;
    }
}
