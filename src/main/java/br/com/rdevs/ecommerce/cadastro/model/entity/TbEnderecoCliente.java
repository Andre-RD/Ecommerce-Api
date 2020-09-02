package br.com.rdevs.ecommerce.cadastro.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name="TB_ENDERECO_CLIENTE")
@Entity
public class TbEnderecoCliente implements Serializable {

    @Id
    @Column(name = "ID_CLIENTE")
    private Long idCliente;

    @JsonIgnore
    @Column(name = "ID_ENDERECO")
    private Long idEndereco;


}
