package br.com.rdevs.ecommerce.produto.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name= "TB_TC_CUPOM")
@Data
public class TbTcCupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CUPOM")
    private Long idCupom;



}
