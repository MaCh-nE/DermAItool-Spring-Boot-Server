package com.dermAItool.DermAItool.Services;

import com.dermAItool.DermAItool.DTO.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    ImageDTO uploadImage(Long userId, MultipartFile imageFile) throws IOException;
    byte[] downloadImage(Long imageId) throws IOException;
    void removeImage(Long imageId);
    List<ImageDTO> getAllImages(Long userId);
}
