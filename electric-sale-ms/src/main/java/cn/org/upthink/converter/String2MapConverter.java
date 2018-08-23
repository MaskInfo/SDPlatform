package cn.org.upthink.converter;

import cn.org.upthink.persistence.mybatis.util.StringUtils;
import cn.org.upthink.util.Jacksons;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

/**
 * 键值对string 转换为 map形式json
 */
public class String2MapConverter extends JsonSerializer {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String val = (String) value;
        if(StringUtils.isEmpty(val)){
            val = "{}";
        }
        gen.writeObject(Jacksons.getMapper().readValue(val,Map.class));
    }
}
