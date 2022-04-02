package com.douzone.final_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisServiceImpl implements com.douzone.final_backend.service.RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void getRedisStringValue(String key) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        System.out.println("Redis key : " + key);
        System.out.println("Redis value : " + stringValueOperations.get(key));

    }

    @Override
    public void setRedisStringValue(String key, String value) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set(key, value);
    }

    @Override
    public void setRedisHashValue(String key, Map<String, String> value) {
        HashOperations<String, Object, Object> redis = stringRedisTemplate.opsForHash();
        redis.putAll(key, value);
    }


    @Override
    public void setRedisGeoValue(String sNumber, double lat, double lon) {
        GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();

        Point coordinate = new Point(lat, lon);
        Map<String, Point> pointMap = new HashMap<>();
        pointMap.put(sNumber, coordinate);
        geoOperations.add("Location", pointMap);
    }

    @Override
    public void getRedisGeoValue(String key, String sNumber, double lat, double lon) {
        GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();
        Point coordinate = new Point(lat, lon);
        Circle circle = new Circle(coordinate, 10000);
        GeoResults<RedisGeoCommands.GeoLocation<String>> res =  geoOperations.radius(key, circle);
        System.out.println(res);
    }


}