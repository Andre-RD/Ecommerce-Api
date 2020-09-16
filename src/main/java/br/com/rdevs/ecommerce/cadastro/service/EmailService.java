package br.com.rdevs.ecommerce.cadastro.service;


import br.com.rdevs.ecommerce.cadastro.model.dto.ClienteLoja;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EmailService {

    @Autowired
    private CadastroRepository cadastroRepository;

    public String esqueciSenha(String esqueciSenha) {

        if (esqueciSenha.matches("[0-9]+")) {
            TbCliente clienteEntity = cadastroRepository.findByNrCpf(esqueciSenha);
            if (clienteEntity != null) {
                String email = clienteEntity.getDsEmail();

                return email;
            }
            else {
                return null;
            }
        } else {
            TbCliente clienteEntity = cadastroRepository.findByDsEmail(esqueciSenha).get(0);
            if (clienteEntity != null) {
                String email = clienteEntity.getDsEmail();

                return email;
            }
            else {
                return null;
            }
        }

    }

    public String clienteLoja(ClienteLoja clienteLoja){
       TbCliente cliente = cadastroRepository.findByNrCpf(clienteLoja.getNrCpf());
       if (cliente!=null){
           String email = clienteLoja.getNmEmail();

           return email;
       }else {
           return null;
       }
    }


}
