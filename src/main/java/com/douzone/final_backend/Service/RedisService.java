package com.douzone.final_backend.service;

import java.util.Map;

public interface RedisService {
    void getRedisStringValue(String key);

    void setRedisStringValue(String key, String value);

    void setRedisHashValue(String key, Map<String, String> value);

    void setRedisGeoValue(String sNumber, double lat, double lon);

    void getRedisGeoValue(String key, String sNumber, double lat, double lon);
}
