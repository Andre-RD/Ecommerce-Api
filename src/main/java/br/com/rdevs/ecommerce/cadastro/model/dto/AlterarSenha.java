package br.com.rdevs.ecommerce.cadastro.model.dto;

import lombok.Data;

@Data
public class AlterarSenha {

    private Long idCliente;
    private String senhaAtual;
    private String novaSenha;
    private String confirmarSenha;

}
