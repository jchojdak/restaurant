package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Photo;
import com.jchojdak.restaurant.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements IPhotoService {

    private final PhotoRepository photoRepository;
    @Override
    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePhotoById(Long id) {
        photoRepository.deleteById(id);
    }
}
