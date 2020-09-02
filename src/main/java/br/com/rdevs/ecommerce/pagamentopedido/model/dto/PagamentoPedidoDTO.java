package br.com.rdevs.ecommerce.pagamentopedido.model.dto;

import br.com.rdevs.ecommerce.pedido.model.dto.PedidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PagamentoPedidoDTO {

    //    private Long idDocumentoFiscal;
    private Long idPagamentoPedido;

    private TipoPagamentoDTO tipoPagamentoDTO;

    private Long idTipoPagamento;

    private PedidoDTO pedidoPagamento;

    private Long idPedido;

    private Long nrNumeroCartao;

    private String nmNomeTitular;



}
