package br.com.rdevs.ecommerce.documentoFiscal.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDocumentoFiscalDTO {

    private Long idCliente;

    private Long idEndereco;

    private Long idFormaPagamento;

    private String nrNumeroCartao;

    private Boolean salvarCartao = false;

    private String nmNomeTitular;

    private List<PostDocumentoFiscalItemDTO> itensDTOPost;

}
