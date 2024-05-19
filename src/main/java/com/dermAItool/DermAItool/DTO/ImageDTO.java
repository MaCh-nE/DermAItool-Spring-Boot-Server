package com.dermAItool.DermAItool.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private Long imageId;
    private Long userId;
    private String extension;
    private String path;
    private String prediction;
    private String realDiagnosis;
    private Date submissionDate;

}
