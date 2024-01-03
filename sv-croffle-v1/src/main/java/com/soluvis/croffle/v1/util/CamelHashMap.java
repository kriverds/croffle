package com.soluvis.croffle.v1.util;

import java.util.LinkedHashMap;

import com.google.common.base.CaseFormat;

public class CamelHashMap extends LinkedHashMap<Object, Object> {

	private static final long serialVersionUID = 1L;

	@Override
    public Object put(Object key, Object value) {
        return super.put(toLowerCamel((String) key), value);
    }

    private static String toLowerCamel(String key) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
    }
}