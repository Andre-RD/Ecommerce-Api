package br.com.rdevs.ecommerce.estoque.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilialDTO {

    private BigInteger cdFilial;
    private String nmFilial;
    private List<EstoqueDTO> estoque;



}
