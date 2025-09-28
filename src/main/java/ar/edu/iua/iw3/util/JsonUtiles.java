package ar.edu.iua.iw3.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public final class JsonUtiles {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ObjectMapper getObjectMapper(Class clazz, StdSerializer ser, String dateFormat) {
		ObjectMapper mapper = new ObjectMapper();
		String defaultFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
		if (dateFormat != null)
			defaultFormat = dateFormat;
		SimpleDateFormat df = new SimpleDateFormat(defaultFormat, Locale.getDefault());
		SimpleModule module = new SimpleModule();
		if (ser != null) {
			module.addSerializer(clazz, ser);
		}
		mapper.setDateFormat(df);
		mapper.registerModule(module);
		return mapper;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ObjectMapper getObjectMapper(Class clazz, StdDeserializer deser, String dateFormat) {
		ObjectMapper mapper = new ObjectMapper();
		String defaultFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
		if (dateFormat != null)
			defaultFormat = dateFormat;
		SimpleDateFormat df = new SimpleDateFormat(defaultFormat, Locale.getDefault());
		SimpleModule module = new SimpleModule();
		if (deser != null) {
			module.addDeserializer(clazz, deser);
		}
		mapper.setDateFormat(df);
		mapper.registerModule(module);
		return mapper;
	}

	/**
	 * Obtiene una cadena con la siguiente lógica:
	 * 1) Busca en cada uno de los atributos definidos en el arreglo "attrs",
	 * el primero que encuentra será el valor retornado.
	 * 2) Si no se encuentra ninguno de los atributos del punto 1), se
	 * retorna "defaultValue".
	 * Ejemplo: supongamos que "node" represente: {"code":"c1, "codigo":"c11",
	 * "stock":true}
	 * getString(node, String[]{"codigo","cod"},"-1") retorna: "cl1"
	 * getString(node, String[]{"cod_prod","c_prod"},"-1") retorna: "-1"
	 * 
	 * @param node
	 * @param attrs
	 * @param defaultValue
	 * @return
	 */

	public static String getString(JsonNode node, String[] attrs, String defaultValue) {
		String r = null;
		for (String attr : attrs) {
			if (node.get(attr) != null) {
				r = node.get(attr).asText();
				break;
			}
		}
		if (r == null)
			r = defaultValue;
		return r;
	}

	public static double getDouble(JsonNode node, String[] attrs, double defaultValue) {
		Double r = null;
		for (String attr : attrs) {
			if (node.get(attr) != null && node.get(attr).isDouble()) {
				r = node.get(attr).asDouble();
				break;
			}
		}
		if (r == null)
			r = defaultValue;
		return r;
	}

	public static boolean getBoolean(JsonNode node, String[] attrs, boolean defaultValue) {
		Boolean r = null;
		for (String attr : attrs) {
			if (node.get(attr) != null && node.get(attr).isBoolean()) {
				r = node.get(attr).asBoolean();
				break;
			}
		}
		if (r == null)
			r = defaultValue;
		return r;
	}

	public static Date getDate(JsonNode node, String[] attrs, Date defaultValue) throws ParseException {
		Date r = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


		for (String attr : attrs) {
			if (node.has(attr) && node.get(attr).isTextual()) {
				r = sdf.parse(node.get(attr).asText());

				break;
			}
		}

		if (r == null)
			r = defaultValue;

		return r;
	}
	public static String[] getArrayComponent(JsonNode node, String[] attrs, String[] defaultValue) {
    String[] r = null;
    for (String attr : attrs) {
        if (node.get(attr) != null && node.get(attr).isArray()) {
            JsonNode arrayNode = node.get(attr);
            r = new String[arrayNode.size()];
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode compNode = arrayNode.get(i);
                if (compNode.has("component")) {
                    r[i] = compNode.get("component").asText();
                } else {
                    r[i] = ""; // o null, según cómo quieras manejarlo
                }
            }
            break;
        }
    }
    if (r == null) r = defaultValue;
    return r;
}


}
