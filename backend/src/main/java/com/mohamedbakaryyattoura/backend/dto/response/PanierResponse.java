package com.mohamedbakaryyattoura.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PanierResponse {
    private Long id;
    private LocalDateTime dateMiseAJour;
    private List<PanierItemResponse> items;
    private BigDecimal total;
    private int nombreArticles;
}
