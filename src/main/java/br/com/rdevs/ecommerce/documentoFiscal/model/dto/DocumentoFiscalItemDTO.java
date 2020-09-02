package br.com.rdevs.ecommerce.documentoFiscal.model.dto;

import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoFiscal;
import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
public class DocumentoFiscalItemDTO {

    private Long nrItemDocumento;

    private Long cdProduto;

    private Long qtItem;

    private BigDecimal vlItem;

    private BigDecimal pcIcms;

    private BigDecimal vlIcms;
}
