package server.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseOutput {
    private static StringBuilder stringBuilder = new StringBuilder();
    private static List<String> argList = new ArrayList<>();
    public static void append(Object toOut) {
        stringBuilder.append(toOut);
    }

    public static void newline() {
        stringBuilder.append('\n');
    }

    public static void appendln(Object toOut) {
        stringBuilder.append(toOut + "\n");
    }

    public static void appendError(Object toOut) {
        stringBuilder.append("Error: " + toOut);
    }
    public static void appendTable(Object element1, Object element2) {
        stringBuilder.append(String.format("%-37s%-1s%n", element1, element2));
    }

    public static void appendargs(String... args){
        argList.addAll(Arrays.asList(args));
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static  String getAndClear() {
        String res = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());

        return res;
    }
    public static String[] getArgsAndClear() {
        String[] argsAsArray = new String[argList.size()];
        argsAsArray = argList.toArray(argsAsArray);
        argList.clear();
        return argsAsArray;
    }
    public static void clear() {
        stringBuilder.delete(0, stringBuilder.length());
    }
}
