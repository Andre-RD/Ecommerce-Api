package br.com.rdevs.ecommerce.cadastro.model.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class ClienteDTO {

    private Long idCliente;
    private String nmCliente;
    private String nrCpf;
    private String dsEmail;
    private Date dtNasc;
    private String dsGenero;
    private String nrTelefone1;
    private String nrTelefone2;

    private String pwCliente;
    private String confirmarSenha;

    private String categoriaCliente;


    private List<EnderecoDTO> enderecos;

    private  List<CartaoCreditoDTO> cartoesCreditoDTO;

}
