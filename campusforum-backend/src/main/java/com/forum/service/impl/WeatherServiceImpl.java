package com.forum.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.forum.entity.vo.response.WeatherVO;
import com.forum.service.WeatherService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;


@Service
public class WeatherServiceImpl implements WeatherService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RestTemplate restTemplate;

    @Value("${spring.weather.key}")
    private String WEATHER_API_KEY;

    @Override
    public WeatherVO fetchWeather(double longitude, double latitude) {
        return fetchFromCache(longitude, latitude);
    }

    private WeatherVO fetchFromCache(double longitude, double latitude) {
        byte[] data = restTemplate.getForObject("https://geoapi.qweather.com/v2/city/lookup?location=" +
                longitude + "," + latitude + "&key=" + WEATHER_API_KEY, byte[].class);
        JSONObject geo = decompressStringToJSON(data);
        if (geo == null) {
            return null;
        }
        JSONObject location = geo.getJSONArray("location").getJSONObject(0);
        int id = location.getInteger("id");
        String key = "weather:" + id;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache != null) {
            return JSON.parseObject(cache, WeatherVO.class);
        }
        WeatherVO weatherVO = fetchFromAPI(id, location);
        if (weatherVO == null) {
            return null;
        }
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(weatherVO), 1, TimeUnit.HOURS);
        return weatherVO;
    }

    private WeatherVO fetchFromAPI(int id, JSONObject location) {
        WeatherVO weatherVO = new WeatherVO();
        weatherVO.setLocation(location);
        JSONObject now = decompressStringToJSON(restTemplate.getForObject("https://devapi.qweather.com/v7/weather/now?location=" +
                id + "&key=" + WEATHER_API_KEY, byte[].class));
        if (now == null) {
            return null;
        }
        weatherVO.setNow(now.getJSONObject("now"));
        JSONObject hourly = decompressStringToJSON(restTemplate.getForObject("https://devapi.qweather.com/v7/weather/24h?location=" +
                id + "&key=" + WEATHER_API_KEY, byte[].class));
        if (hourly == null) {
            return null;
        }
        weatherVO.setHourly(new JSONArray(hourly.getJSONArray("hourly").stream().limit(5).toList()));
        return weatherVO;
    }

    private JSONObject decompressStringToJSON(byte[] data) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(data));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
            gzipInputStream.close();
            byteArrayOutputStream.close();
            return JSONObject.parseObject(byteArrayOutputStream.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
