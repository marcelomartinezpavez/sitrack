package cl.sitack.ws.alphabet.soup.mongo.crud.utils;

import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.IdTypeAndIdDocumentIncompatiblesException;
import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.NoDocumentException;
import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.NoFieldIdException;
import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.NoIdValueException;
import org.reflections.Reflections;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MongoCRUDUtils {
    private static final String API_MODEL_PROPERTY_TEMPLATE = "\t@ApiModelProperty( dataType=\"{dataType}\", example = \"{example}\", required = {required})\n";
    private static final String FIELD_TEMPLATE = "\tprivate {dataType} {fieldName};\n";

    public MongoCRUDUtils() {
    }

    public static void generarCodigo() {
        generarCodigo(false);
    }

    public static void generarCodigo(boolean sobreescribir) {
        List<Class> clasesDocument = obtenerClasesDocumentoMongo();
        if (clasesDocument != null) {
            try {
                String plantillaDto = ArchivosUtils.readFullFromJARFile("/templates/Dto.template");
                String plantillaController = ArchivosUtils.readFullFromJARFile("/templates/CRUDController.template");
                String plantillaService = ArchivosUtils.readFullFromJARFile("/templates/Service.template");
                String plantillaRepository = ArchivosUtils.readFullFromJARFile("/templates/Repository.template");
                String plantillaControllerException = ArchivosUtils.readFullFromJARFile("/templates/ExceptionController.template");
                clasesDocument.forEach((claseDocument) -> {
                    try {
                        String tipoIdDocumento = obtenerCampoIdFromDocument(claseDocument).getType().getSimpleName();
                        escribirDto(sobreescribir, claseDocument, plantillaDto);
                        esribirController(sobreescribir, claseDocument, plantillaController, tipoIdDocumento);
                        escribirService(sobreescribir, claseDocument, plantillaService, tipoIdDocumento);
                        escribirRepository(sobreescribir, claseDocument, plantillaRepository, tipoIdDocumento);
                    } catch (NoDocumentException var7) {
                        var7.printStackTrace();
                    } catch (NoFieldIdException var8) {
                        var8.printStackTrace();
                    }

                });
                escribirControllerException(sobreescribir, (Class)clasesDocument.get(0), plantillaControllerException);
            } catch (IOException var7) {
                var7.printStackTrace();
            }

        }
    }

    private static void escribirControllerException(boolean sobreescribir, Class claseDocument, String plantilla) {
        String paqueteController = obtenerPaqueteRaizDeDomain(claseDocument) + ".controller";

        try {
            ArchivosUtils.escribirFullArchivo(plantilla.replaceAll("\\{paqueteController\\}", paqueteController), "src/main/java/" + paqueteController.replaceAll("\\.", "/"), "RestResponseEntityExceptionHandler.java", sobreescribir);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    private static void escribirRepository(boolean sobreescribir, Class claseDocument, String plantilla, String tipoIdDocumento) {
        String fullNameDocument = claseDocument.getName();
        String paqueteRepository = obtenerPaqueteRaizDeDomain(claseDocument) + ".repository";
        String simpleNameDocument = claseDocument.getSimpleName();

        try {
            ArchivosUtils.escribirFullArchivo(plantilla.replaceAll("\\{simpleNameDocument\\}", simpleNameDocument).replaceAll("\\{tipoIdDocumento\\}", tipoIdDocumento).replaceAll("\\{paqueteRepository\\}", paqueteRepository).replaceAll("\\{fullNameDocument\\}", fullNameDocument), "src/main/java/" + paqueteRepository.replaceAll("\\.", "/"), simpleNameDocument + "Repository.java", sobreescribir);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    private static void escribirService(boolean sobreescribir, Class claseDocument, String plantilla, String tipoIdDocumento) {
        String simpleNameDocument = claseDocument.getSimpleName();
        String fullNameDocument = claseDocument.getName();
        String fullNameRepository = obtenerPaqueteRaizDeDomain(claseDocument) + ".repository." + simpleNameDocument + "Repository";
        String paqueteRaiz = obtenerPaqueteRaizDeDomain(claseDocument);
        String subPaqueteDomain = obtenerSubpaquetes(paqueteRaiz + ".domain", claseDocument);
        String paqueteDto = paqueteRaiz + ".dto" + subPaqueteDomain;
        String fullNameDto = paqueteDto + "." + simpleNameDocument + "Dto";
        String paqueteService = obtenerPaqueteRaizDeDomain(claseDocument) + ".service";
        String nombreInstanciaDocumento = simpleNameDocument.substring(0, 1).toLowerCase() + simpleNameDocument.substring(1);

        try {
            ArchivosUtils.escribirFullArchivo(plantilla.replaceAll("\\{simpleNameDocument\\}", simpleNameDocument).replaceAll("\\{tipoIdDocumento\\}", tipoIdDocumento).replaceAll("\\{fullNameRepository\\}", fullNameRepository).replaceAll("\\{paqueteService\\}", paqueteService).replaceAll("\\{fullNameDocument\\}", fullNameDocument).replaceAll("\\{fullNameDto\\}", fullNameDto).replaceAll("\\{nombreInstanciaDocumento\\}", nombreInstanciaDocumento), "src/main/java/" + paqueteService.replaceAll("\\.", "/"), simpleNameDocument + "CRUDServiceImpl.java", sobreescribir);
        } catch (IOException var14) {
            var14.printStackTrace();
        }

    }

    private static void escribirDto(boolean sobreescribir, Class claseDocument, String plantilla) {
        try {
            String simpleNameDocument = claseDocument.getSimpleName();
            String paqueteRaiz = obtenerPaqueteRaizDeDomain(claseDocument);
            String subPaqueteDomain = obtenerSubpaquetes(paqueteRaiz + ".domain", claseDocument);
            String paqueteDto = paqueteRaiz + ".dto" + subPaqueteDomain;
            StringBuilder importsExtrasBuilder = new StringBuilder();
            if (tieneCampoTipo(claseDocument, Date.class)) {
                importsExtrasBuilder.append("import java.util.Date;\n");
            }

            if (tieneCampoTipo(claseDocument, List.class)) {
                importsExtrasBuilder.append("import java.util.List;\n");
            }

            if (tieneCampoTipo(claseDocument, LocalDate.class)) {
                importsExtrasBuilder.append("import java.time.LocalDate;\n");
            }

            if (tieneCampoTipo(claseDocument, LocalDateTime.class)) {
                importsExtrasBuilder.append("import java.time.LocalDateTime;\n");
            }

            if (tieneCampoTipo(claseDocument, BigDecimal.class)) {
                importsExtrasBuilder.append("import java.math.BigDecimal;\n");
            }

            if (tieneCampoTipo(claseDocument, BigInteger.class)) {
                importsExtrasBuilder.append("import java.math.BigInteger;\n");
            }

            String extendss = "";
            if (claseDocument.getSuperclass() != null) {
                Class superClass = claseDocument.getSuperclass();
                Package paquete = superClass.getPackage();
                if (paquete != null) {
                    String paqueteSuper = paquete.getName();
                    if (paqueteSuper.contains(".domain")) {
                        if (!paqueteSuper.equals(claseDocument.getPackage().getName())) {
                            String subPaqueteDomainS = obtenerSubpaquetes(paqueteRaiz + ".domain", superClass);
                            String paqueteDtoS = paqueteRaiz + ".dto" + subPaqueteDomainS;
                            importsExtrasBuilder.append("import " + paqueteDtoS + ".*;\n");
                        }

                        extendss = "extends " + superClass.getSimpleName() + "Dto";
                    }
                }
            }

            ArchivosUtils.escribirFullArchivo(plantilla.replaceAll("\\{simpleNameDocument\\}", simpleNameDocument).replaceAll("\\{paqueteDto\\}", paqueteDto).replaceAll("\\{getterAndSetter\\}", createSetterAndGetterDomainToDto(claseDocument)).replaceAll("\\{campos\\}", crearCamposDelDtoDesdeDomain(claseDocument)).replaceAll("\\{extends\\}", extendss).replaceAll("\\{importsExtras\\}", importsExtrasBuilder.toString()), "src/main/java/" + paqueteDto.replaceAll("\\.", "/"), simpleNameDocument + "Dto.java", sobreescribir);
            List<Field> fields = obtenerAtributosObjetosDelDomain(claseDocument);
            fields.forEach((field) -> {
                Class clase = field.getType();
                if (clase == List.class) {
                    clase = ReflectUtils.obtenerClaseDeFieldList(field);
                }

                if (clase != claseDocument) {
                    escribirDto(sobreescribir, clase, plantilla);
                }

            });
        } catch (Exception var14) {
            var14.printStackTrace();
        }

    }

    private static void esribirController(boolean sobreescribir, Class claseDocument, String plantilla, String tipoIdDocumento) {
        try {
            String simpleNameDocument = claseDocument.getSimpleName();
            String paqueteDocument = claseDocument.getPackage() == null ? null : claseDocument.getPackage().getName();
            String fullNameDocument = claseDocument.getName();
            String paqueteRaiz = obtenerPaqueteRaizDeDomain(claseDocument);
            String subPaqueteDomain = obtenerSubpaquetes(paqueteRaiz + ".domain", claseDocument);
            String paqueteDto = paqueteRaiz + ".dto" + subPaqueteDomain;
            String fullNameDto = paqueteDto + "." + simpleNameDocument + "Dto";

            try {
                Field idField = obtenerCampoIdFromDocument(claseDocument);
                String nombreIdDocumento = idField.getName();
                String paqueteIdDocumento = idField.getType().getPackage() == null ? "" : idField.getType().getPackage().getName();
                String paqueteController = obtenerPaqueteRaizDeDomain(claseDocument) + ".controller";
                String aliasDocumento = obtenerTypeAlias(claseDocument);
                String nombreInstanciaDocumento = simpleNameDocument.substring(0, 1).toLowerCase() + simpleNameDocument.substring(1);
                ArchivosUtils.escribirFullArchivo(plantilla.replaceAll("\\{fullNameDocument\\}", fullNameDocument).replaceAll("\\{simpleNameDocument\\}", simpleNameDocument).replaceAll("\\{nombreIdDocumento\\}", nombreIdDocumento).replaceAll("\\{tipoIdDocumento\\}", tipoIdDocumento).replaceAll("\\{paqueteIdDocumento\\}", paqueteIdDocumento).replaceAll("\\{paqueteController\\}", paqueteController).replaceAll("\\{aliasDocumento\\}", aliasDocumento).replaceAll("\\{nombreInstanciaDocumento\\}", nombreInstanciaDocumento).replaceAll("\\{fullNameDto\\}", fullNameDto).replaceAll("\\{paqueteDocument\\}", paqueteDocument), "src/main/java/" + paqueteController.replaceAll("\\.", "/"), simpleNameDocument + "Controller.java", sobreescribir);
            } catch (IOException var17) {
                var17.printStackTrace();
            } catch (NoDocumentException var18) {
                var18.printStackTrace();
            } catch (NoFieldIdException var19) {
                var19.printStackTrace();
            }
        } catch (Exception var20) {
            var20.printStackTrace();
        }

    }

    public static List<Class> obtenerClasesDocumentoMongo() {
        return obtenerClasesDocumentoMongo("cl.uchile.sti");
    }

    public static List<Class> obtenerClasesDocumentoMongo(String packageBase) {
        List<Class> list = new ArrayList();
        Reflections reflections = new Reflections(packageBase, new Scanner[0]);
        Set<Class<?>> documentClassSet = reflections.getTypesAnnotatedWith(Document.class);
        if (documentClassSet != null) {
            list = (List)documentClassSet.stream().filter((clazz) -> {
                return !clazz.getPackage().getName().contains(".mongo_crud.");
            }).collect(Collectors.toList());
        }

        return (List)list;
    }

    public static String obtenerTypeAlias(Class documentClass) throws NoDocumentException {
        validaAnotacionDocument(documentClass);
        if (documentClass.isAnnotationPresent(TypeAlias.class)) {
            TypeAlias typeAlias = (TypeAlias)documentClass.getAnnotation(TypeAlias.class);
            return typeAlias.value().isEmpty() ? documentClass.getSimpleName().toLowerCase() + "s" : typeAlias.value();
        } else {
            return documentClass.getSimpleName().toLowerCase() + "s";
        }
    }

    public static String obtenerCollectionName(Class documentClass) throws NoDocumentException {
        validaAnotacionDocument(documentClass);
        Document document = (Document)documentClass.getAnnotation(Document.class);
        return document.collection().isEmpty() ? documentClass.getSimpleName() : document.collection();
    }

    public static void validaAnotacionDocument(Class documentClass) throws NoDocumentException {
        if (!documentClass.isAnnotationPresent(Document.class)) {
            throw new NoDocumentException(documentClass);
        }
    }

    public static Field obtenerCampoIdFromDocument(Class documentClass) throws NoDocumentException, NoFieldIdException {
        validaAnotacionDocument(documentClass);
        Field field = ReflectUtils.obtenerFieldConAnotacion(documentClass, Id.class);
        if (field != null) {
            return field;
        } else {
            throw new NoFieldIdException(documentClass);
        }
    }

    public static void validaIdValueFromDocumentObject(Object document) throws NoDocumentException, NoFieldIdException, NoIdValueException {
        validaAnotacionDocument(document.getClass());
        Field fieldId = obtenerCampoIdFromDocument(document.getClass());

        try {
            Object idValue = ReflectUtils.fieldGetValue(fieldId, document);
            if (idValue == null) {
                throw new NoIdValueException(document.getClass());
            }
        } catch (IllegalAccessException var3) {
            throw new NoIdValueException(document.getClass());
        }
    }

    public static Object obtenerIdValueFromDocumentObject(Object document) throws NoDocumentException, NoFieldIdException, NoIdValueException {
        validaAnotacionDocument(document.getClass());
        Field fieldId = obtenerCampoIdFromDocument(document.getClass());

        try {
            Object idValue = ReflectUtils.fieldGetValue(fieldId, document);
            if (idValue == null) {
                throw new NoIdValueException(document.getClass());
            } else {
                return idValue;
            }
        } catch (IllegalAccessException var3) {
            throw new NoIdValueException(document.getClass());
        }
    }

    public static void setearIdValueEnDocument(Object document, Object idValue) throws NoDocumentException, NoFieldIdException, IdTypeAndIdDocumentIncompatiblesException {
        validaAnotacionDocument(document.getClass());
        Field fieldId = obtenerCampoIdFromDocument(document.getClass());

        try {
            ReflectUtils.fieldSetValue(fieldId, idValue, document);
        } catch (IllegalAccessException var4) {
            throw new IdTypeAndIdDocumentIncompatiblesException(document.getClass(), fieldId.getClass());
        }
    }

    private static String obtenerSubpaquetes(String paqueteBase, Class clazz) {
        String paqueteClase = clazz.getPackage().getName();
        return paqueteClase.length() <= paqueteBase.length() ? "" : paqueteClase.substring(paqueteBase.length());
    }

    private static String obtenerPaqueteRaizDeDomain(Class clazz) {
        return clazz.getPackage().getName().substring(0, clazz.getPackage().getName().indexOf(".domain"));
    }

    private static boolean tieneCampoTipo(Class clazz, Class fieldType) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.asList(fields).stream().anyMatch((field) -> {
            return field.getType() == fieldType;
        });
    }

    private static String obtenerTipoDeFieldEnDto(Field field, String paqueteRaiz) {
        String fieldType = field.getType().getSimpleName();
        String paquete = field.getType().getPackage() == null ? "" : field.getType().getPackage().getName();
        if (paquete.contains(paqueteRaiz)) {
            fieldType = fieldType + "Dto";
        }

        if (field.getType() == List.class) {
            Class tipoListaClass = ReflectUtils.obtenerClaseDeFieldList(field);
            String listType = tipoListaClass.getSimpleName();
            if (tipoListaClass.getPackage().getName().contains(paqueteRaiz)) {
                listType = listType + "Dto";
            }

            fieldType = fieldType + "<" + listType + ">";
        }

        return fieldType;
    }

    private static List<Field> obtenerAtributosObjetosDelDomain(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList();
        String paqueteRaiz = obtenerPaqueteRaizDeDomain(clazz);
        Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            Class clase = field.getType();
            if (clase == List.class) {
                clase = ReflectUtils.obtenerClaseDeFieldList(field);
            }

            String paquete = clase.getPackage() == null ? "" : clase.getPackage().getName();
            if (paquete.contains(paqueteRaiz)) {
                list.add(field);
            }
        }

        return list;
    }

    private static String crearCamposDelDtoDesdeDomain(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String paqueteRaiz = obtenerPaqueteRaizDeDomain(clazz);
        StringBuilder camposBuilder = new StringBuilder();
        Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            String fieldName = field.getName();
            String fieldType = obtenerTipoDeFieldEnDto(field, paqueteRaiz);
            camposBuilder.append("\t@ApiModelProperty( dataType=\"{dataType}\", example = \"{example}\", required = {required})\n".replace("{dataType}", fieldType).replace("{example}", "").replace("{required}", "false"));
            camposBuilder.append("\tprivate {dataType} {fieldName};\n".replace("{dataType}", fieldType).replace("{fieldName}", fieldName));
        }

        return camposBuilder.toString();
    }

    private static String createSetterAndGetterDomainToDto(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String paqueteRaiz = obtenerPaqueteRaizDeDomain(clazz);
        StringBuilder gsBuilder = new StringBuilder();
        Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            String fieldName = field.getName();
            String fieldType = obtenerTipoDeFieldEnDto(field, paqueteRaiz);
            gsBuilder.append(GeneradorCodigoUtils.createSetter(fieldName, fieldType));
            gsBuilder.append(GeneradorCodigoUtils.createGetter(fieldName, fieldType));
        }

        return gsBuilder.toString();
    }
}
