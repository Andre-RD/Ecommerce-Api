package br.com.rdevs.ecommerce.documentoFiscal.model.dto;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbEndereco;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
public class DocumentoFiscalDTO {

   private String titulo = "RaiaDrogasil SA";

   private String nrCpf;

   private BigInteger idNF;

   private Long nrChaveAcesso;

   private String dsEmail;

   private Long nrNF;

   private Date dtEmissao;

   private BigInteger nrPedido;

   private BigInteger idFormaPagamento;

   private String formaPagamento;

   private String nrNumeroCartao;

   private TbEndereco endereco;

   private String nmNomeTitular;

   private BigDecimal valorTotalNota;

   private Long QtItens;

   private List<DocumentoFiscalItemDTO> itensDocumento;





}
