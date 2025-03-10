package com.forum.controller;

import com.forum.entity.RestBean;
import com.forum.entity.vo.response.WeatherVO;
import com.forum.service.WeatherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Resource
    WeatherService weatherService;

    @GetMapping("/weather")
    public RestBean<WeatherVO> weather(double longtitude, double latitude) {
        WeatherVO weather = weatherService.fetchWeather(longtitude, latitude);
        return weather == null ? RestBean.failure(400, "获取天气信息失败") : RestBean.success(weather);
    }
}
