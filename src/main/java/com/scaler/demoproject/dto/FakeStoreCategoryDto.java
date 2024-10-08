package com.scaler.demoproject.dto;

import com.scaler.demoproject.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FakeStoreCategoryDto {
    private Category[] categories;
}
