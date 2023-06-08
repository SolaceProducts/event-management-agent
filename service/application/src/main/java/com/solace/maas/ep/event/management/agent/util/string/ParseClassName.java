package com.solace.maas.ep.event.management.agent.util.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParseClassName {

    public static Class getClass(String className) throws ClassNotFoundException {
        int idx = className.indexOf("<");
        if (idx>-1) {
            String classPart = className.substring(0, idx);

            return Class.forName(classPart);
        } else {
            return Class.forName(className);
        }
    }
    public static Class[] getParameterizedTypes(String className) throws ClassNotFoundException {
        int idx = className.indexOf("<");
        if (idx>-1) {
            List<Class> types = new ArrayList<>();
            String genericsPart = className.substring(idx);
            getGenerics(genericsPart, types);
            Collections.reverse(types);
            return types.toArray(new Class[0]);
        } else {
            return new Class[0];
        }
    }

    private static void getGenerics(String genericsPart, List<Class> types) throws ClassNotFoundException {
        String genericType = genericsPart.substring(genericsPart.indexOf("<")+1);
        int nextOpeningBracket = genericType.indexOf("<");
        String className;
        if (nextOpeningBracket>-1){
            className = genericType.substring(0, nextOpeningBracket);
            getGenerics(genericType.substring(nextOpeningBracket+1), types);
        } else {
            className = genericType.substring(0, genericType.indexOf(">"));
        }
        types.add(Class.forName(className));
    }
}
