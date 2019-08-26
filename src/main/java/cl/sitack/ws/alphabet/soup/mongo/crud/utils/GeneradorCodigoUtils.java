package cl.sitack.ws.alphabet.soup.mongo.crud.utils;

public class GeneradorCodigoUtils {

    public GeneradorCodigoUtils() {
    }

    public static String createSetter(String fieldName, String fieldType) {
        StringBuffer setter = new StringBuffer();
        setter.append("\tpublic void").append(" set");
        setter.append(getFieldName(fieldName));
        setter.append("(" + fieldType + " " + fieldName + ") {");
        setter.append("\n\t\t this." + fieldName + " = " + fieldName + ";");
        setter.append("\n\t}\n");
        return setter.toString();
    }

    public static String createGetter(String fieldName, String fieldType) {
        StringBuffer getter = new StringBuffer();
        getter.append("\tpublic " + fieldType).append((fieldType.equals("boolean") ? "  is" : " get") + getFieldName(fieldName) + "(){");
        getter.append("\n\t\treturn " + fieldName + ";");
        getter.append("\n\t}\n");
        return getter.toString();
    }

    public static String getFieldName(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
    }

}
