package br.com.rdevs.ecommerce.pagamentopedido.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class PostPagamentoPedidoDTO {

    private Long idPedido;
    private Long idDocumentoFiscal;
    private Long idTipoPagameto;
    private Long nrNumeroCartao;
    private String nmNomeTitular;

}
