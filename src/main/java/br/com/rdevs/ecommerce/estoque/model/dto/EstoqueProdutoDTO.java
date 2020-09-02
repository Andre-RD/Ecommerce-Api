package br.com.rdevs.ecommerce.estoque.model.dto;

import lombok.Data;

@Data
public class EstoqueProdutoDTO {

    private Long cdFilial;
//    private String nmFilial;
    private Long qtEstoque;
    private Long qtEmpenho;

}
