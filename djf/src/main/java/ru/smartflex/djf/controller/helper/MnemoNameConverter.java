package ru.smartflex.djf.controller.helper;

public class MnemoNameConverter {

    private static final String STRING_MNEMO = "string";
    private static final String STRING_CLAZZ = "java.lang.String";

    static final String STACK_MNEMO_DELETE = "stackDelete";
    static final String STACK_MNEMO_SAVE = "stackSave";
    private static final String STACK_CLAZZ = "java.util.Deque";

    private static final String OBJECT_MNEMO = "object";
    static final String OBJECT_MNEMO_SOURCE = "objSource";
    private static final String OBJECT_CLAZZ = "java.lang.Object";

    private static final String BOOLEAN_MNEMO = "boolean";
    private static final String BOOLEAN_CLAZZ = "java.lang.Boolean";

    private static final String INTEGER_MNEMO = "int";
    private static final String INTEGER_CLAZZ = "java.lang.Integer";

    private MnemoNameConverter() {
    }

    static String convert(String mnemo) {
        String fullClassName = null;
        if (mnemo != null) {
            if (mnemo.equals(STRING_MNEMO)) {
                fullClassName = STRING_CLAZZ;
            } else if (mnemo.equals(STACK_MNEMO_DELETE)) {
                fullClassName = STACK_CLAZZ;
            } else if (mnemo.equals(STACK_MNEMO_SAVE)) {
                fullClassName = STACK_CLAZZ;
            } else if (mnemo.equals(OBJECT_MNEMO)) {
                fullClassName = OBJECT_CLAZZ;
            } else if (mnemo.equals(OBJECT_MNEMO_SOURCE)) {
                fullClassName = OBJECT_CLAZZ;
            } else if (mnemo.equals(BOOLEAN_MNEMO)) {
                fullClassName = BOOLEAN_CLAZZ;
            } else if (mnemo.equals(INTEGER_MNEMO)) {
                fullClassName = INTEGER_CLAZZ;
            } else {
                fullClassName = mnemo;
            }
        }
        return fullClassName;
    }
}
