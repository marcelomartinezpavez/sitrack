package cl.sitack.ws.alphabet.soup.mongo.crud.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    public ReflectUtils() {
    }

    public static List<String> findPackageNamesStartingWith(String prefix) {
        List<String> result = new ArrayList();
        Package[] var2 = Package.getPackages();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Package p = var2[var4];
            if (p.getName().startsWith(prefix)) {
                result.add(p.getName());
            }
        }

        return result;
    }

    public static List<String> findPackageNamesEndWith(String sufix) {
        List<String> result = new ArrayList();
        Package[] var2 = Package.getPackages();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Package p = var2[var4];
            if (p.getName().endsWith(sufix)) {
                result.add(p.getName());
            }
        }

        return result;
    }

    public static List<String> findPackageNamesContains(String contenido) {
        List<String> result = new ArrayList();
        Package[] var2 = Package.getPackages();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Package p = var2[var4];
            if (p.getName().contains(contenido)) {
                result.add(p.getName());
            }
        }

        return result;
    }

    public static Field obtenerFieldConAnotacion(Class busquedaClazz, Class anotacionClazz) {
        Field[] fields = busquedaClazz.getDeclaredFields();
        Field[] var3 = fields;
        int var4 = fields.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (field.isAnnotationPresent(anotacionClazz)) {
                return field;
            }
        }

        return null;
    }

    public static Class<?> obtenerClaseDeFieldList(Field fieldList) {
        ParameterizedType listType = (ParameterizedType)fieldList.getGenericType();
        return (Class)listType.getActualTypeArguments()[0];
    }

    public static void fieldSetValue(Field field, Object value, Object objeto) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(objeto, value);
        field.setAccessible(false);
    }

    public static Object fieldGetValue(Field field, Object objeto) throws IllegalAccessException {
        field.setAccessible(true);
        Object o = field.get(objeto);
        field.setAccessible(false);
        return o;
    }

    public static Method obtenerMetodoPorNombre(String nombreMetodo, Class clase) {
        Method method = null;
        Method[] var3;
        int var4;
        int var5;
        Method m;
        if (clase.getDeclaredMethods() != null) {
            var3 = clase.getDeclaredMethods();
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                m = var3[var5];
                if (m.getName().equals(nombreMetodo)) {
                    return m;
                }
            }
        }

        if (clase.getMethods() != null) {
            var3 = clase.getMethods();
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                m = var3[var5];
                if (m.getName().equals(nombreMetodo)) {
                    return m;
                }
            }
        }

        return (Method)method;
    }
}
