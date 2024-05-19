package com.dermAItool.DermAItool.Services.Implementation;

import com.dermAItool.DermAItool.DTO.ImageDTO;
import com.dermAItool.DermAItool.DTOtoJPA_Mapper.ImageMapper;
import com.dermAItool.DermAItool.Exceptions.EntityNotFoundException;
import com.dermAItool.DermAItool.JPA_Entities.Images;
import com.dermAItool.DermAItool.Repository.ImageRepo;
import com.dermAItool.DermAItool.Repository.UserRepo;
import com.dermAItool.DermAItool.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ImageServiceImplementation implements ImageService {

    private ImageRepo imageRepo;
    private UserRepo userRepo;
    private static final String folderPath = "C:\\Users\\Lenovo\\Desktop\\DermAItool_local_imageRepo";


    @Override
    public ImageDTO uploadImage(Long userId, MultipartFile imageFile) throws IOException {

        Images image = imageRepo.save(Images.builder()
                .userId(userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with given ID" + userId + "not found.")))
                .extension(imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")))
                .path("")
                .prediction("None")
                .realDiagnosis("None")
                .submissionDate(new Date()).build());

        image.setPath(folderPath + "\\" + image.getImageId());

        Images saved = imageRepo.save(image);

        imageFile.transferTo(new File(image.getPath() + image.getExtension()));
        return ImageMapper.toImageDTO(image);
    }


    @Override
    public byte[] downloadImage (Long imageId) throws IOException {
        Images fetchedImage = imageRepo.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image with given imageID" + imageId + "not found."));
        try {
            return Files.readAllBytes(new File(fetchedImage.getPath() + fetchedImage.getExtension() ).toPath());
        } catch (IOException e) {
            throw new IOException("Failed to read image file: " + fetchedImage.getPath(), e);
        }
    }

    @Override
    public void removeImage(Long imageId) {
        Images fetchedImage = imageRepo.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image with given imageID" + imageId + "not found."));
        imageRepo.delete(fetchedImage);
    }

    @Override
    public List<ImageDTO> getAllImages(Long userId) {
        List<Images> allImages = imageRepo.findBySecondaryKey(userId);
        return allImages.stream().map(ImageMapper::toImageDTO).collect(Collectors.toList());
    }

}
