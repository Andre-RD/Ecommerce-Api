package br.com.rdevs.ecommerce.produto.model.dto;

import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueProdutoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
public class ProdutoDTO {

    private BigInteger cdProduto;
    private BigInteger idStatusProduto;
    private String nomeFantasia;
    private String nomeFabricante;
    private String dsProduto;

    private BigDecimal valorUnidade;
    private BigDecimal valorSemDesconto;



    private CategoriaProdutoDTO categoriaProduto;
    private SubCategoriaProdutoDTO subCategoriaProduto;

    private List<ProdutoImagemDTO> imagens;
    private EstoqueProdutoDTO estoques;

}
