package br.com.rdevs.ecommerce.documentoFiscal.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class DocumentoFiscalItemDTO {

    private BigInteger nrItemDocumento;

    private BigInteger cdProduto;

    private Integer qtItem;

    private String nmProduto;

    private BigDecimal vlItemUnitario;

    private BigDecimal vlTotalItem;

    private BigDecimal pcIcms;

    private BigDecimal vlIcms;
}
