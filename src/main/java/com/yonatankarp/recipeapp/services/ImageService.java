package com.yonatankarp.recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageFile(final Long recipeId, final MultipartFile file);
}
