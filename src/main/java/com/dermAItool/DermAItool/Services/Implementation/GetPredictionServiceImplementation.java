package com.dermAItool.DermAItool.Services.Implementation;

import com.dermAItool.DermAItool.DTO.ImageDTO;
import com.dermAItool.DermAItool.DTOtoJPA_Mapper.ImageMapper;
import com.dermAItool.DermAItool.Exceptions.EntityNotFoundException;
import com.dermAItool.DermAItool.JPA_Entities.Images;
import com.dermAItool.DermAItool.Repository.ImageRepo;
import com.dermAItool.DermAItool.Repository.UserRepo;
import com.dermAItool.DermAItool.Services.GetPredictionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class GetPredictionServiceImplementation implements GetPredictionService {

    private UserRepo userRepo;
    private ImageRepo imageRepo;

    private static final String flaskServerUrl = "http://127.0.0.1:5000";

    // First method to call the Flask server API for a prediction ;
    // -> returns a String
    @Override
    public String callFlaskServerForPrediction(Long imageId) {
        // Fetch for image and extract path ;
        Images image = imageRepo.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image with given imageID" + imageId + "not found."));
        String path = image.getPath() + image.getExtension();

        // REST Template for sending the request (a POST request since we will be sending the path via a JSON body) to
        // the Flask server's REST API ;
        RestTemplate restTemplate = new RestTemplate();
        // Request Headers ;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Body and JSON Format : {"path": "givenImagePath"} ;
        // Map : dict. like data structure, key-value pairs ;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("path", path);
        // Actual request (body + headers) ;
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        String predictUrl = flaskServerUrl + "/predict";
        return restTemplate.postForObject(predictUrl, requestEntity, String.class);
    }

    // First method to call the Flask server API for a prediction probability dictionary ;
    // -> returns a List of Strings ("Lesion: probability")
    @Override
    public List<String> callFlaskServerForPredictionProbabilities(Long imageId) {
        Images image = imageRepo.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image with given imageID" + imageId + "not found."));
        String path = image.getPath() + image.getExtension();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Body and JSON Format : {"path": "givenImagePath"} ;
        // Map : dict. like data structure, key-value pairs ;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("path", path);
        // Actual request (body + headers) ;
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        String predictUrl = flaskServerUrl + "/predictions";

        String Result = restTemplate.postForObject(predictUrl, requestEntity, String.class);

        String[] parts = Result.replaceAll("[\\[\\]\"]", "").split(",");

        // Remove escape sequences (\n...) ;
        List<String> cleanedList = new ArrayList<>();
        for (int i = 0 ; i<parts.length ; i++) {
            String cleanedPart = parts[i].trim();                                      // Remove leading and trailing whitespace ;
            String finalPart = cleanedPart.replace("\n", "")      // Removing literal "\n" and "\n " ;
                                          .replace("{ ", "")
                                          .replace("}","");
            if (!finalPart.isEmpty()) {                                            // Ignore empty strings ;
                cleanedList.add(finalPart);
            }
        }
        return cleanedList;
    }

    @Override
    public ImageDTO savePrediction(Long imageId, String prediction) {
        Images image = imageRepo.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image with given imageID" + imageId + "not found."));
        image.setPrediction(prediction);
        Images savedImage = imageRepo.save(image);
        return ImageMapper.toImageDTO(savedImage);
    }

    @Override
    public ImageDTO saveRealDiagnosis(Long imageId, String realDiagnosis) {
        Images image = imageRepo.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image with given imageID" + imageId + "not found."));
        image.setRealDiagnosis(realDiagnosis);
        Images savedImage = imageRepo.save(image);
        return ImageMapper.toImageDTO(savedImage);
    }


    @Override
    public String callFlaskServerForGradCAM(Long imageId, String colormap, String alpha) {
        Images image = imageRepo.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image with given imageID" + imageId + "not found."));
        String path = image.getPath() + image.getExtension();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Body and JSON Format : {"path": "givenImagePath"} ;
        // Map : dict. like data structure, key-value pairs ;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("path", path);
        requestBody.put("imageId", imageId.toString());
        requestBody.put("colormap", colormap);
        requestBody.put("alpha", alpha);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        String predictUrl = flaskServerUrl + "/gradCAM";

        return restTemplate.postForObject(predictUrl, requestEntity, String.class);
    }
}
