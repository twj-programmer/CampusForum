package com.forum.controller;

import com.forum.entity.RestBean;
import com.forum.service.ImageService;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ObjectController {

    @Resource
    ImageService imageService;

    @GetMapping("/images/avatar/**")
    public void imageFetch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.fetchImage(request, response);
    }

    private void fetchImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = request.getServletPath().substring(7);
        ServletOutputStream stream = response.getOutputStream();
        if (imagePath.length() <= 13) {
            response.setStatus(404);
            stream.println(RestBean.failure(404, "图片不存在").toString());
        } else {
            try {
                imageService.fetchImageFormMinio(stream, imagePath);
                response.setHeader("Cache-Control", "max-age=2592000");
            } catch (ErrorResponseException e) {
                if (e.response().code() == 404) {
                    response.setStatus(404);
                    stream.println(RestBean.failure(404, "图片不存在").toString());
                } else {
                    log.error("从Minio获取图片异常", e);
                }
            }
        }
    }
}
