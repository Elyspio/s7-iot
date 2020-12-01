package com.example.iot_project.models;

public class Code {
    final Integer id;
    final String label;
    final String code;

    /*
       Function Code
       Constructeur
    */
    public Code(Integer id, String label, String code) {
        this.id = id;
        this.label = label;
        this.code = code;
    }

    /*
        Function getId
        Getter
     */
    public Integer getId() {
        return id;
    }

    /*
        Function getLabel
        Getter
     */
    public String getLabel() {
        return label;
    }

    /*
        Function getCode
        Getter
     */
    public String getCode() {
        return code;
    }

    static public class DbMap {
        public static int brightness = 0;
        public static int Temperature = 1;
    }

}
