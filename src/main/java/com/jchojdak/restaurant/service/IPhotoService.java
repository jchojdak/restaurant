package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Photo;

public interface IPhotoService {
    Photo savePhoto(Photo photo);

    Photo getPhotoById(Long id);

    void deletePhotoById(Long id);
}
