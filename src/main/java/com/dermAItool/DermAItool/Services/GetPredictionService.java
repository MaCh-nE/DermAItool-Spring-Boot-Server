package com.dermAItool.DermAItool.Services;

import com.dermAItool.DermAItool.DTO.ImageDTO;

import java.util.List;

public interface GetPredictionService {
    public String callFlaskServerForPrediction(Long imageId);
    public List<String> callFlaskServerForPredictionProbabilities(Long imageId);
    public ImageDTO savePrediction(Long imageId, String prediction);
    public ImageDTO saveRealDiagnosis(Long imageId, String realDiagnosis);
    public String callFlaskServerForGradCAM(Long imageId, String colormap, String alpha);
}
