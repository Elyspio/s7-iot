package com.example.iot_project.models;

public class Code {
    final Integer id;
    final String label;
    final String code;

    public Code(Integer id, String label, String code) {
        this.id = id;
        this.label = label;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }

    static public class DbMap {
        public static int brightness = 0;
        public static int Temperature = 1;
    }

}
