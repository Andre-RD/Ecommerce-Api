package br.com.rdevs.ecommerce.documentoFiscal.model.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
public class DocumentoFiscalDTO {

   private Long idCliente;

   private Long nrChaveAcesso;

   private Long nrNF;

   private Long nrSerie;

   private Date dtEmissao;

   private Long idPagamentoPedido;

   private Long nrNumeroCartao;

   private String nmNomeTitular;

   private List<DocumentoFiscalItemDTO> itensDocumento;





}
