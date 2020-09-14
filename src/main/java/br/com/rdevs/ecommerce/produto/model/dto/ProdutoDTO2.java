package br.com.rdevs.ecommerce.produto.model.dto;

import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueProdutoDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProdutoDTO2 {

    private Long cdProduto;
    private Long idStatusProduto;
    private String nomeFantasia;
    private String nomeFabricante;
    private String dsProduto;
    private String dsUrl;
    private BigDecimal valorUnidade;
    private BigDecimal valorSemDesconto;
    private SubCategoriaProdutoDTO subCategoriaProduto;

    private EstoqueProdutoDTO estoques;
}
