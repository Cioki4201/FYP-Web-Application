package com.paul.fyp.models.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CoverIDsDTO {
    private String[] coverIDs;

    public CoverIDsDTO(String[] coverIDs) {
        this.coverIDs = coverIDs;
    }

    public String[] getCoverIDs() {
        return coverIDs;
    }

    public void setCoverIDs(String[] coverIDs) {
        this.coverIDs = coverIDs;
    }
}
