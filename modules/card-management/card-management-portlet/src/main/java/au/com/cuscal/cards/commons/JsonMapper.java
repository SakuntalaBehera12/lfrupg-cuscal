package au.com.cuscal.cards.commons;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.slf4j.Logger;

public class JsonMapper {

	public static <T> T read(String objStr, Class<T> valueType)
		throws IOException {

		T obj = null;

		if (isNotBlank(objStr)) {
			obj = objectMapper.readValue(objStr, valueType);
		}

		return obj;
	}

	public static String toJson(Object object) throws IOException {
		String objStr = null;

		try {
			if (object != null) {
				objStr = objectMapper.writeValueAsString(object);
			}
		}
		catch (JsonProcessingException e) {
			log.warn("Failed to covert object to json []", object);
		}

		return objStr;
	}

	private static final Logger log = getLogger(JsonMapper.class);
	private static ObjectMapper objectMapper = new ObjectMapper();

}