package br.com.rdevs.ecommerce.produto.model.dto;


import lombok.Data;


import java.math.BigDecimal;

@Data
public class TcCupomItemDTO {

    private Long idTcCupom;

    private Long idCupomItem;

    private BigDecimal pcDesconto;

    private ProdutoDTO produtoCp;

}
