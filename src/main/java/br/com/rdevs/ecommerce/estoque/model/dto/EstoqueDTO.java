package br.com.rdevs.ecommerce.estoque.model.dto;


import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
public class EstoqueDTO {

    private Long cdProduto;
    private Long cdCategoria;
    private String dsProduto;
    private Long qtEstoque;
    private Long qtEmpenho;
    private Long cdEstoque;





}
