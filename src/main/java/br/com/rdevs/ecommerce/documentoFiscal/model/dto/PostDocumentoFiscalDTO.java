package br.com.rdevs.ecommerce.documentoFiscal.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDocumentoFiscalDTO {

    private Long idCliente;

    private Long idEndereco;

    private Long idFormaPagamento;

    private Long nrNumeroCartao;

    private String nmNomeTitular;

    private List<PostDocumentoFiscalItemDTO> itensDTOPost;


}
