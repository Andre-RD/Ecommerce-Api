package br.com.rdevs.ecommerce.produto.model.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CategoriaProdutoDTO {

    private BigInteger idCategoriaProduto;

    private String dsCategoriaProduto;

//    private SubCategoriaProdutoDTO subCategoriaProduto;

//    private List<TbProduto> produtos;
}
