package br.com.rdevs.ecommerce.produto.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuatidadeCarrinho {
    private Long cdProduto;
    private Long qtProduto;
    private Long cdFilial;
    private Long qtEstoque;


}
