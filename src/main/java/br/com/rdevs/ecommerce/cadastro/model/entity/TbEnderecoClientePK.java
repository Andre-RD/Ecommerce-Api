package br.com.rdevs.ecommerce.cadastro.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class TbEnderecoClientePK implements Serializable {
    private Long idCliente;


    private Long idEndereco;
}
