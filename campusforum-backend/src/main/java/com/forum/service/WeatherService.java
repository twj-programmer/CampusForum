package com.forum.service;

import com.forum.entity.vo.response.WeatherVO;

public interface WeatherService {
    WeatherVO fetchWeather(double longitude, double latitude); //经纬度
}
