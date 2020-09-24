package br.com.rdevs.ecommerce.cadastro.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;


@Data
@NoArgsConstructor
public class TbEnderecoClientePK implements Serializable {
    private BigInteger idCliente;


    private BigInteger idEndereco;
}
