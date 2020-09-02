package br.com.rdevs.ecommerce.cadastro.service;

import br.com.rdevs.ecommerce.cadastro.model.dto.CartaoCreditoDTO;
import br.com.rdevs.ecommerce.cadastro.model.dto.ClienteDTO;
import br.com.rdevs.ecommerce.cadastro.model.dto.EnderecoDTO;
import br.com.rdevs.ecommerce.cadastro.model.dto.Login;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCartaoCredito;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEndereco;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEnderecoCliente;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.cadastro.repository.CartaoRepository;
import br.com.rdevs.ecommerce.cadastro.repository.EnderecoClienteRepository;
import br.com.rdevs.ecommerce.cadastro.repository.EnderecoRepository;
import br.com.rdevs.ecommerce.cadastro.service.bo.CadastroBO;
import br.com.rdevs.ecommerce.cadastro.service.bo.CartaoCreditoBO;
import br.com.rdevs.ecommerce.cadastro.service.bo.EnderecoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private CadastroRepository cadastroRepository;
    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private EnderecoClienteRepository enderecoClienteRepository;
    @Autowired
    private CadastroBO cadastroBO;
    @Autowired
    private EnderecoBO enderecoBO ;
    @Autowired
    private CartaoCreditoBO cartaoCreditoBO;


    public List<ClienteDTO> buscarTodas(){
        List<ClienteDTO> listDTO = new ArrayList<>();
        List<TbCliente> listEntity = cadastroRepository.findAll();

        for(TbCliente clienteEntity : listEntity){
            ClienteDTO clienteDTO = cadastroBO.parseToDTO(clienteEntity);
            List<EnderecoDTO> enderecoDTOS = new ArrayList<>();
            for (TbEndereco enderecoEntity: clienteEntity.getEnderecos()){
                EnderecoDTO dto = enderecoBO.parseToDTO(enderecoEntity);
                enderecoDTOS.add(dto);
            }
            clienteDTO.setEnderecos(enderecoDTOS);
            List<CartaoCreditoDTO> cartaoCreditoDTOS = new ArrayList<>();
            for (TbCartaoCredito cartaoCreditoEntity: clienteEntity.getCartoesCredito()){
                CartaoCreditoDTO dtoCard = cartaoCreditoBO.parseToDTO(cartaoCreditoEntity);
                cartaoCreditoDTOS.add(dtoCard);
            }
            clienteDTO.setCartoesCreditoDTO(cartaoCreditoDTOS);
            listDTO.add(clienteDTO);
        }

        return listDTO;
    }

    public TbCliente buscarPorCpf(String cpf){
        return cadastroRepository.findByNrCpf(cpf);
    }

    public ClienteDTO buscarPorId(Long idCliente){
        TbCliente clienteEntity=cadastroRepository.findByIdCliente(idCliente);
        ClienteDTO clienteDTO = cadastroBO.parseToDTO(clienteEntity);

        List<EnderecoDTO> enderecoDTOS = new ArrayList<>();
        for (TbEndereco enderecoEntity: clienteEntity.getEnderecos()){
            EnderecoDTO dto = enderecoBO.parseToDTO(enderecoEntity);
            enderecoDTOS.add(dto);
        }
        clienteDTO.setEnderecos(enderecoDTOS);

        List<CartaoCreditoDTO> cartaoCreditoDTOS = new ArrayList<>();
        for (TbCartaoCredito cartaoCreditoEntity: clienteEntity.getCartoesCredito()){
            CartaoCreditoDTO dtoCard = cartaoCreditoBO.parseToDTO(cartaoCreditoEntity);
            cartaoCreditoDTOS.add(dtoCard);
        }
        clienteDTO.setCartoesCreditoDTO(cartaoCreditoDTOS);

        return clienteDTO;
    }

    public TbCliente inserir (ClienteDTO clienteDTO){
        TbCliente clienteEntity = cadastroBO.parseToEntity(clienteDTO, null);

        return cadastroRepository.save(clienteEntity);
    }

    public TbCliente atualizar(ClienteDTO clienteDTO){
        TbCliente clienteEntity = cadastroRepository.getOne(clienteDTO.getIdCliente());
        if(clienteEntity!= null) {
            clienteEntity = cadastroBO.parseToEntity(clienteDTO,null);
            List<TbEndereco> enderecosEntitys = new ArrayList<>();
            for (EnderecoDTO enderecoDTO: clienteDTO.getEnderecos()){
                TbEndereco enderecoEntity = enderecoBO.parseToEntity(enderecoDTO,null);
                enderecosEntitys.add(enderecoEntity);
                enderecoRepository.save(enderecoEntity);
            }
            clienteEntity.setEnderecos(enderecosEntitys);

            List<TbCartaoCredito> cartoesCreditoEntitys = new ArrayList<>();

            for (CartaoCreditoDTO cartaoCreditoDTO: clienteDTO.getCartoesCreditoDTO()){
                TbCartaoCredito cartaoCreditoEntity = cartaoCreditoBO.parseToEntity(cartaoCreditoDTO, null);
                cartoesCreditoEntitys.add(cartaoCreditoEntity);
                cartaoCreditoEntity.setClienteCartao(clienteEntity);
                cartaoRepository.save(cartaoCreditoEntity);
            }
            clienteEntity.setCartoesCredito(cartoesCreditoEntitys);

        }
        return cadastroRepository.save(clienteEntity);
    }

    public ClienteDTO loginCadastro(Login login) throws Exception{

        if (login.getLogin().matches("[0-9]+")) {

            TbCliente clienteEntity = cadastroRepository.findByNrCpf(login.getLogin());

            ClienteDTO clienteDTO = cadastroBO.parseToDTO(clienteEntity);
            List<EnderecoDTO> enderecoDTOS = new ArrayList<>();
            for (TbEndereco enderecoEntity: clienteEntity.getEnderecos()){
                EnderecoDTO dto = enderecoBO.parseToDTO(enderecoEntity);
                enderecoDTOS.add(dto);
            }
            clienteDTO.setEnderecos(enderecoDTOS);

            List<CartaoCreditoDTO> cartaoCreditoDTOS = new ArrayList<>();
            for (TbCartaoCredito cartaoCreditoEntity: clienteEntity.getCartoesCredito()){
                CartaoCreditoDTO dtoCard = cartaoCreditoBO.parseToDTO(cartaoCreditoEntity);
                cartaoCreditoDTOS.add(dtoCard);
            }
            clienteDTO.setCartoesCreditoDTO(cartaoCreditoDTOS);

            return clienteDTO;
        } else {
            TbCliente clienteEntity = cadastroRepository.findByDsEmail(login.getLogin());
            ClienteDTO clienteDTO = cadastroBO.parseToDTO(clienteEntity);
            List<EnderecoDTO> enderecoDTOS = new ArrayList<>();
            for (TbEndereco enderecoEntity: clienteEntity.getEnderecos()){
                EnderecoDTO dto = enderecoBO.parseToDTO(enderecoEntity);
                enderecoDTOS.add(dto);
            }
            clienteDTO.setEnderecos(enderecoDTOS);

            List<CartaoCreditoDTO> cartaoCreditoDTOS = new ArrayList<>();
            for (TbCartaoCredito cartaoCreditoEntity: clienteEntity.getCartoesCredito()){
                CartaoCreditoDTO dtoCard = cartaoCreditoBO.parseToDTO(cartaoCreditoEntity);
                cartaoCreditoDTOS.add(dtoCard);
            }
            clienteDTO.setCartoesCreditoDTO(cartaoCreditoDTOS);

            return clienteDTO;
        }
    }

    public TbCliente adicionaEndereco(EnderecoDTO enderecoDTO, Long idCliente){
        TbCliente clienteEntity = cadastroRepository.getOne(idCliente);
        List <TbEnderecoCliente> idClienteEndereco = enderecoClienteRepository.findByIdCliente(idCliente);
        List<TbEndereco> enderecosBanco = new ArrayList<>();

        for(TbEnderecoCliente enderecos:idClienteEndereco){
            enderecosBanco.add(enderecoRepository.getOne(enderecos.getIdEndereco()));
        }

        TbEndereco enderecoNovo = enderecoBO.parseToEntity(enderecoDTO, null);
        enderecoRepository.save(enderecoNovo);
        enderecosBanco.add(enderecoNovo);
        clienteEntity.setEnderecos(enderecosBanco);
        return cadastroRepository.save(clienteEntity);
    }

    public List<TbEnderecoCliente> listarEnderecoCliente(){
        return enderecoClienteRepository.findAll();
    }

    public TbEndereco atualizaEndereco(EnderecoDTO enderecoDTO){
        TbEndereco enderecoNovo = enderecoBO.parseToEntity(enderecoDTO, null);
        return  enderecoRepository.save(enderecoNovo);
    }

    //TODO falta implementar o deletarEndereco
    public EnderecoDTO deletarEndereco(Long idEndereco){
        TbEndereco enderecoEntity = enderecoRepository.getOne(idEndereco);
        EnderecoDTO enderecoDTO = enderecoBO.parseToDTO(enderecoEntity);
        List <TbEnderecoCliente> enderecoClienteList = enderecoClienteRepository.findByIdEndereco(idEndereco);

        for (TbEnderecoCliente enderecoCliente:enderecoClienteList){
            enderecoClienteRepository.delete(enderecoCliente);
        }

        enderecoRepository.delete(enderecoEntity);

        return enderecoDTO;
    }

    public TbCliente adicionaCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO, Long idCliente){
        TbCliente clienteEntity = cadastroRepository.getOne(idCliente);

        List<TbCartaoCredito> cartoesCreditoEntitys = new ArrayList<>();
        for (TbCartaoCredito cartaoCredito: clienteEntity.getCartoesCredito()){
            cartoesCreditoEntitys.add(cartaoCredito);
        }

        TbCartaoCredito cartaoCreditoEntity = cartaoCreditoBO.parseToEntity(cartaoCreditoDTO,null);
        cartaoCreditoEntity.setClienteCartao(clienteEntity);
        cartaoCreditoDTO.setIdCliente(cartaoCreditoEntity.getIdCartaoCredito());
        cartaoRepository.save(cartaoCreditoEntity);
        cartoesCreditoEntitys.add(cartaoCreditoEntity);
        clienteEntity.setCartoesCredito(cartoesCreditoEntitys);

        return cadastroRepository.save(clienteEntity);
    }

    public TbCartaoCredito atualizaCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO,Long idCliente){
        TbCartaoCredito cartaoCreditoEntity = cartaoCreditoBO.parseToEntity(cartaoCreditoDTO,null);
        TbCliente cliente = cadastroRepository.getOne(idCliente);
        cartaoCreditoEntity.setClienteCartao(cliente);
        return cartaoRepository.save(cartaoCreditoEntity);
    }

    public CartaoCreditoDTO deletarCartaoCredito(Long idCartaoCredito){
        TbCartaoCredito cartaoCreditoEntity = cartaoRepository.getOne(idCartaoCredito);
        CartaoCreditoDTO cartaoCreditoDTO = cartaoCreditoBO.parseToDTO(cartaoCreditoEntity);
        cartaoRepository.delete(cartaoCreditoEntity);
        return cartaoCreditoDTO;
    }




}