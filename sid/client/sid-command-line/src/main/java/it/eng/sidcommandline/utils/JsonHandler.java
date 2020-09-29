package it.eng.sidcommandline.utils;


import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.logging.Logger;

public class JsonHandler {
    private static final Logger log = Logger.getLogger(JsonHandler.class.getName());
    public static final String UTF_8 = "UTF-8";

    public static String convertToJson(Object obj) throws Exception {
        return convertToJsonByJackson(obj);
    }

    public static Object convertFromJson(String json, Class clazz) throws Exception {
        return convertFromJsonByJackson(json, clazz);
    }

    public static String convertToJsonByJackson(Object obj) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); //This property put data in upper camel case
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    public static Object convertFromJsonByJackson(String json, Class clazz) throws Exception {
        try {
            if (StringUtils.isEmpty(json))
                throw new Exception("Json data is EMPTY");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true); //This property serialize/deserialize not considering the case of fields
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    public static Object convertFromJson(String json, Class clazz, boolean isCollection) throws Exception {
        try {
            if (StringUtils.isEmpty(json))
                throw new Exception("Json data is EMPTY");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true); //This property serialize/deserialize not considering the case of fields
            if (isCollection) {
                //ObjectReader objectReader = mapper.reader().forType(new TypeReference<List<?>>() {
                //});
                // return objectReader.readValue(json);
                return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new Exception(e);
        }
    }



}
