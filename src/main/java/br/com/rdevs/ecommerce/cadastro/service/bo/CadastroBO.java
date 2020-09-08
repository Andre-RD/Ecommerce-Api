package br.com.rdevs.ecommerce.cadastro.service.bo;

import br.com.rdevs.ecommerce.cadastro.model.dto.ClienteDTO;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class CadastroBO{

    public ClienteDTO parseToDTO(TbCliente clienteEntity){
        ClienteDTO clienteDTO = new ClienteDTO();

        clienteDTO.setIdCliente(clienteEntity.getIdCliente());
        clienteDTO.setNmCliente(clienteEntity.getNmCliente());
        clienteDTO.setNrCpf(clienteEntity.getNrCpf());
        clienteDTO.setDtNasc(clienteEntity.getDtNasc());
        clienteDTO.setDsEmail(clienteEntity.getDsEmail());
        clienteDTO.setDsGenero(clienteEntity.getDsGenero());
        clienteDTO.setNrTelefone1(clienteEntity.getNrTelefone1());
        clienteDTO.setNrTelefone2(clienteEntity.getNrTelefone2());
        clienteDTO.setPwCliente(clienteEntity.getPwCliente());

        return clienteDTO;
    }

    public TbCliente parseToEntity(ClienteDTO clienteDTO,TbCliente clienteEntity){
        if(clienteEntity == null)
            clienteEntity = new TbCliente();

        if(clienteDTO == null)
            return clienteEntity;

        clienteEntity = new TbCliente();
        clienteEntity.setIdCliente(clienteDTO.getIdCliente());
        clienteEntity.setNmCliente(clienteDTO.getNmCliente());
        clienteEntity.setNrCpf(clienteDTO.getNrCpf());
        clienteEntity.setDtNasc(clienteDTO.getDtNasc());
        clienteEntity.setDsEmail(clienteDTO.getDsEmail());
        clienteEntity.setDsGenero(clienteDTO.getDsGenero());
        clienteEntity.setNrTelefone1(clienteDTO.getNrTelefone1());
        clienteEntity.setNrTelefone2(clienteDTO.getNrTelefone2());

        clienteEntity.setPwCliente(Base64.getEncoder().encodeToString(clienteDTO.getPwCliente().getBytes()));

        //TODO Criar validação por data para clientes acima de 60 anos serem setados como 2

        clienteEntity.setIdCategoriaCliente(1L);

        return clienteEntity;
    }
}
