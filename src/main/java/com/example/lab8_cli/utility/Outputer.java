package com.example.lab8_cli.utility;

import com.example.lab8_cli.controller.tool.ObservableResourceFactory;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.example.lab8_cli.App.BUNDLE;

public class Outputer {
    private static ObservableResourceFactory resourceFactory;

    private static String tryResource(String str, String arg) {
        try{
            if(haveResourceFactory()) throw new NullPointerException();
            if(arg == null) return resourceFactory.getResources().getString(str);
            else {
                MessageFormat messageFormat = new MessageFormat(resourceFactory.getResources().getString(str));
                Object[] args  = {arg};
                return messageFormat.format(args);
            }
        } catch(MissingResourceException| NullPointerException e) {
            return str;
        }
    }

    public static void print(String toOut, String arg) {
        System.out.println(tryResource(toOut, arg));
    }

    public static void print(String toOut) {
        print(toOut, null);
    }

    public static void println(String toOut, String arg) {
        System.out.println(tryResource(toOut, arg));
    }

    public static void println(String toOut) {
        println(toOut, null);
    }

    public static void println() {
        System.out.println();
    }

    public static void printError(String toOut, String arg) {
        System.out.println("Error: " + tryResource(toOut,arg));
    }

    public static void printError(String toOut) {
        printError(toOut, null);
    }

    public static void setResourceFactory(ObservableResourceFactory resourceFactory) {
        Outputer.resourceFactory = resourceFactory;
    }

    public static boolean haveResourceFactory() {
        return resourceFactory == null;
    }
}
