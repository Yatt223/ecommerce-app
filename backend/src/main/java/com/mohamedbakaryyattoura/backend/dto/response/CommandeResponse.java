package com.mohamedbakaryyattoura.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommandeResponse {
    private Long id;
    private String numero;
    private String statut;
    private BigDecimal total;
    private LocalDateTime dateCommande;
    private LocalDateTime datePaiement;
    private LocalDateTime dateExpedition;
    private List<CommandeItemResponse> items;
    private String userEmail;

}
