package br.com.rdevs.ecommerce.produto.model.dto;

import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueDTO;
import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueProdutoDTO;
import br.com.rdevs.ecommerce.estoque.model.dto.FilialDTO;
import br.com.rdevs.ecommerce.estoque.model.entity.TbFilial;
import br.com.rdevs.ecommerce.produto.model.entity.TbCategoriaProduto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProdutoDTO {

    private Long cdProduto;
    private Long idStatusProduto;
    private String nomeFantasia;
    private String nomeFabricante;
    private String dsProduto;
    private BigDecimal valorUnidade;


    private CategoriaProdutoDTO categoriaProduto;
    private SubCategoriaProdutoDTO subCategoriaProduto;
//    private TipoProdutoDTO tipoProduto;

    private List<ProdutoImagemDTO> imagens;
//    private List<EstoqueProdutoDTO> estoques;
    private EstoqueProdutoDTO estoques;

}
