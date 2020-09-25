package br.com.rdevs.ecommerce.documentoFiscal.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@NoArgsConstructor
public class TbDocumentoItemID implements Serializable {

    private BigInteger idDocumentoFiscal;
    private BigInteger nrItemDocumento;

}
