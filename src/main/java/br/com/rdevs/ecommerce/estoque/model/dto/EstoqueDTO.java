package br.com.rdevs.ecommerce.estoque.model.dto;


import lombok.Data;

import java.math.BigInteger;

@Data
public class EstoqueDTO {

    private BigInteger cdProduto;
    private BigInteger cdCategoria;
    private String dsProduto;
    private Integer qtEstoque;
    private Integer qtEmpenho;
    private BigInteger cdEstoque;





}
