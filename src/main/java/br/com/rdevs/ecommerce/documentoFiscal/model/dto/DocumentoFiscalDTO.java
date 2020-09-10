package br.com.rdevs.ecommerce.documentoFiscal.model.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class DocumentoFiscalDTO {

   private String titulo = "RaiaDrogasil SA" + "\n";

   private String nrCpf;

   private Long nrChaveAcesso;

   private Long nrNF;

   private Date dtEmissao;

   private Long nrPedido;

   private String nrNumeroCartao;

   private String nmNomeTitular;

   private BigDecimal valorTotalNota;

   private Long QtItens;

   private List<DocumentoFiscalItemDTO> itensDocumento;





}
