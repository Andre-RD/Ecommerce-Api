package br.com.rdevs.ecommerce.produto.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TcCupomDTO {

    private Long idCupom;

    private String nmCliente;

    private Date dtFinalCupom;

    private List<TcCupomItemDTO> itensCupom;



}
