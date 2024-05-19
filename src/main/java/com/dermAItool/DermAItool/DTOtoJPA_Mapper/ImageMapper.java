package com.dermAItool.DermAItool.DTOtoJPA_Mapper;

import com.dermAItool.DermAItool.DTO.ImageDTO;
import com.dermAItool.DermAItool.JPA_Entities.Images;
import com.dermAItool.DermAItool.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component   // Spring component : Spring manages it ! ;
public class ImageMapper {
    private static UserRepo userRepo;

    @Autowired   // Annotation to tell Spring to fetch arguments (repo which is a Bean) on his own (Dependency injection) ;
    public static void setRepo(UserRepo userRepository) {
        userRepo = userRepository;
    }

    public static Images toImage(ImageDTO imageDTO) {
        return new Images(
                imageDTO.getImageId(),
                userRepo.findById(imageDTO.getUserId()).orElse(null),
                imageDTO.getExtension(),
                imageDTO.getPath(),
                imageDTO.getPrediction(),
                imageDTO.getRealDiagnosis(),
                imageDTO.getSubmissionDate()
        );
    }

    public static ImageDTO toImageDTO(Images image) {
        return new ImageDTO(
                image.getImageId(),
                image.getUserId().getId(),
                image.getExtension(),
                image.getPath(),
                image.getPrediction(),
                image.getRealDiagnosis(),
                image.getSubmissionDate()
        );
    }
}
