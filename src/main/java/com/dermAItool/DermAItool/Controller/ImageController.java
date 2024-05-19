package com.dermAItool.DermAItool.Controller;


import com.dermAItool.DermAItool.DTO.ImageDTO;
import com.dermAItool.DermAItool.Services.GetPredictionService;
import com.dermAItool.DermAItool.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("API/Users/{id}/Images")   // No image flow without a user connection already ! ;
public class ImageController {

    // No autowired needed, an AllArgs constructor is already in place ;
    private ImageService imageService;
    private GetPredictionService predictionService;

    @PostMapping
    public ResponseEntity<ImageDTO> uploadImage(@PathVariable("id") Long userId,
                                                @RequestParam("image") MultipartFile imageFile) throws IOException {
        ImageDTO imgDTO = imageService.uploadImage(userId, imageFile);
        return new ResponseEntity<>(imgDTO, HttpStatus.CREATED);
    }

    @GetMapping("{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable("imageId") Long imageId) throws IOException {
        byte[] imageData = imageService.downloadImage(imageId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages(@PathVariable("id") Long userId) {
        List<ImageDTO> allImages = imageService.getAllImages(userId);
        return ResponseEntity.ok(allImages);
    }

    @DeleteMapping("{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable("imageId") Long imageId) {
        imageService.removeImage(imageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Image with ID: " + imageId + " deleted.");
    }

    // Prediction GET (POST for the FLASK) mappers ;
    // 1st : Prediction Class ;
    @GetMapping("{imageId}/predict")
    public ResponseEntity<String> Prediction(@PathVariable("imageId") Long imageId) {
        return ResponseEntity.ok(predictionService.callFlaskServerForPrediction(imageId));
    }

    // 2nd : List of Prediction probabilities per class ;
    @GetMapping("{imageId}/predictions")
    public ResponseEntity<List<String>>  PredictionProbabilities(@PathVariable("imageId") Long imageId) {
        return ResponseEntity.ok(predictionService.callFlaskServerForPredictionProbabilities(imageId));
    }

    // Saving prediction to DB ;
    @PutMapping("{imageId}/predict/save")
    public ResponseEntity<ImageDTO> savePrediction(@PathVariable("imageId") Long imageId,
                                                   @RequestBody Map<String, String> prediction) {
        return ResponseEntity.ok(predictionService.savePrediction(imageId, prediction.get("prediction")));
    }

    // Saving real-diagnosis to DB ;
    @PutMapping("{imageId}/diagnosis/save")
    public ResponseEntity<ImageDTO> saveDiagnosis(@PathVariable("imageId") Long imageId,
                                                  @RequestBody Map<String, String> realDiagnosis) {
        return ResponseEntity.ok(predictionService.saveRealDiagnosis(imageId, realDiagnosis.get("realDiagnosis")));
    }

    // 3rd : Saving GRAD-CAM result into the GRAD-CAM repo for certain uploaded image ;
    @PostMapping("{imageId}/gradCAM")
    public ResponseEntity<String> GradCAM(@PathVariable("imageId") Long imageId,
                                          @RequestBody Map<String, String> imageData) {
        String colormap = imageData.get("colormap");
        String alpha = imageData.get("alpha");
        return ResponseEntity.ok(predictionService.callFlaskServerForGradCAM(imageId, colormap, alpha));
    }

}