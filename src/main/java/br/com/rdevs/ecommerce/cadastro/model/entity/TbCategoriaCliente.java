package br.com.rdevs.ecommerce.cadastro.model.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_CATEGORIA_CLIENTE")
@Data
public class TbCategoriaCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORIA_CLIENTE")
    private Long idCategoriaCliente;

    @Column(name = "DS_CATEGORIA_CLIENTE")
    private String dsCategoriaCliente;

    @Column(name = "PC_DESCONTO_ECOMMERCE")
    private BigDecimal pcDescontoEcommerce;

}
