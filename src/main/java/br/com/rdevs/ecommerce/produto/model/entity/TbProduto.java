package br.com.rdevs.ecommerce.produto.model.entity;

import br.com.rdevs.ecommerce.estoque.model.entity.TbFilial;
import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "TB_PRODUTO")
@Data
public class TbProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_PRODUTO")
    private Long cdProduto;

    @Column(name = "ID_STATUS_PRODUTO")
    private Long idStatusProduto;

    @Column(name = "NM_FANTASIA")
    private String nomeFantasia;

    @Column(name = "NM_FABRICANTE")
    private String nomeFabricante;

    @Column(name = "VL_UNIDADE")
    private BigDecimal valorUnidade;


    @Column(name = "DS_PRODUTO")
    private String dsProduto;

    @OneToMany(mappedBy = "produtoImagem", cascade = CascadeType.ALL)
    private List<TbProdutoImagem> imagens;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ID_TIPO_PRODUTO")
//    @JsonIgnore
//    private TbTipoProduto tipoProduto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA")
    @JsonIgnore
    private TbCategoriaProduto categoriaProduto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUB_CATEGORIA")
    private TbSubCategoriaProduto subCategoriaProduto;

    @OneToMany(mappedBy = "produtoFilial", cascade = CascadeType.ALL)
    private List<TbProdutoFilialEstoque> produtosEstoqueEntity;

}
