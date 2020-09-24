package br.com.rdevs.ecommerce.produto.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ProdutoDTO2 {

    private BigInteger cdProduto;
    private BigInteger idStatusProduto;
    private String nomeFantasia;
    private String nomeFabricante;
    private String dsProduto;
    private String dsUrl;
    private BigDecimal valorUnidade;
    private BigDecimal valorSemDesconto;
    private BigInteger idSubCategoria;
    private String dsSubCategoria;
    private BigInteger cdCategoria;
    private String dsCategoriaProduto;
    private BigInteger qtEstoque;
    private BigInteger qtEmpenho;
    private BigInteger cdEstoque;

}
