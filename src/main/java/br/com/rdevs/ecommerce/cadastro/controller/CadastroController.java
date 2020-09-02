package br.com.rdevs.ecommerce.cadastro.controller;

import br.com.rdevs.ecommerce.cadastro.model.dto.CartaoCreditoDTO;
import br.com.rdevs.ecommerce.cadastro.model.dto.ClienteDTO;
import br.com.rdevs.ecommerce.cadastro.model.dto.EnderecoDTO;
import br.com.rdevs.ecommerce.cadastro.model.dto.ResultData;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.cadastro.repository.EnderecoRepository;
import br.com.rdevs.ecommerce.cadastro.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
public class CadastroController {

    @Autowired
    private ClienteService service;
    @Autowired
    private CadastroRepository repository;
    @Autowired
    private EnderecoRepository enderecoRepository;


    @GetMapping("/listaCadastros")
    @RolesAllowed(value = "ADMIN")
    public ResponseEntity buscarCadastros(){
        return ResponseEntity.ok().body(service.buscarTodas());
    }

    @GetMapping("/listarPorCpf/{cpf}")
    @RolesAllowed(value = "ADMIN")
    public ResponseEntity buscarPorCpf(@PathVariable("cpf") String cpf){
        return ResponseEntity.ok().body(service.buscarPorCpf(cpf));
    }

    @GetMapping("/listarPorId/{idCliente}")
    @RolesAllowed(value = "ADMIN")
    public ResponseEntity buscarPorIdCliente(@PathVariable("idCliente") Long idCliente){
        return ResponseEntity.ok().body(service.buscarPorId(idCliente));
    }

    @PostMapping("/novoCadastro")
    @RolesAllowed(value = "ADMIN")
    public ResponseEntity<Object> salvarCadastro(@RequestBody ClienteDTO clienteDTO){
        ResultData resultData = null;
        TbCliente clienteCpf = repository.findByNrCpf(clienteDTO.getNrCpf());
        TbCliente clienteEmail = repository.findByDsEmail(clienteDTO.getDsEmail());
        if (clienteCpf != null) {
            resultData = new ResultData(HttpStatus.BAD_REQUEST.value(), "CPF JÁ CADASTRADO");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultData);
        }
        else if (clienteEmail!= null) {
            resultData = new ResultData(HttpStatus.BAD_REQUEST.value(), "EMAIL JÁ CADASTRADO");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultData);
        }
        else {
            try {
                TbCliente tbCliente = service.inserir(clienteDTO);
                resultData = new ResultData<TbCliente>(HttpStatus.OK.value(),
                        "Cliente registrada com sucesso!", tbCliente);
                return ResponseEntity.status(HttpStatus.CREATED).body(resultData);

            } catch (Exception e) {
                resultData = new ResultData(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Ocorreu um erro ao registrar cliente", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultData);
            }
        }
    }

    @PutMapping("/atualizarCadastro")
    @RolesAllowed(value = "ADMINN")
    public ResponseEntity<Object> atualizarCadastro(@RequestBody ClienteDTO clienteDTO){
        ResultData resultData = null;
            try {
                TbCliente tbCliente = service.atualizar(clienteDTO);
                resultData = new ResultData<TbCliente>(HttpStatus.OK.value(),
                        "Cliente Atualizado com Sucesso!", tbCliente);
                return ResponseEntity.status(HttpStatus.CREATED).body(resultData);

            } catch (Exception e) {
                resultData = new ResultData(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Ocorreu um erro ao atualizar o cliente", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultData);
            }

    }

    @GetMapping("/listaEnderecoCliente")
    @RolesAllowed(value = "ADMIN")
    public ResponseEntity buscarEnderecos(){
        return ResponseEntity.ok().body(service.listarEnderecoCliente());
    }



    @RequestMapping(value = "/novoEndereco/{idCliente}", method = RequestMethod.POST)
    public ResponseEntity<Object> novoEndereco(@RequestBody EnderecoDTO enderecoDTO, @PathVariable("idCliente") Long idCliente){
        return ResponseEntity.ok().body(service.adicionaEndereco(enderecoDTO,idCliente));
    }

    @PutMapping(value = "/atualizaEndereco")
    public ResponseEntity<Object> atualizaEndereco(@RequestBody EnderecoDTO enderecoDTO){
        return ResponseEntity.ok().body(service.atualizaEndereco(enderecoDTO));
    }

//    @DeleteMapping(value = "/deletarEndereco/{idEndereco}")
//    public ResponseEntity<Object> deletarEndereco(@PathVariable("idEndereco") Long idEndereco){
//        return ResponseEntity.ok().body(service.deletarEndereco(idEndereco));
//    }

    @RequestMapping(value = "/novoCartao/{idCliente}", method = RequestMethod.POST)
    public ResponseEntity<Object> novoCartaoCredito(@RequestBody CartaoCreditoDTO cartaoCreditoDTO, @PathVariable("idCliente") Long idCliente) {
        return ResponseEntity.ok().body(service.adicionaCartaoCredito(cartaoCreditoDTO, idCliente));
    }

    @RequestMapping(value = "/atualizaCartaoCredito/{idCliente}", method = RequestMethod.PUT)
    public ResponseEntity<Object> atualizaCartaoCredito(@RequestBody CartaoCreditoDTO cartaoCreditoDTO, Long idCliente){
        return ResponseEntity.ok().body(service.atualizaCartaoCredito(cartaoCreditoDTO, idCliente));
    }

    @DeleteMapping(value = "/deletarCartaoCredito/{idCartaoCredito}")
    public ResponseEntity<Object> deletarCartaoCredito(@PathVariable("idCartaoCredito") Long idCartaoCredito){
        return ResponseEntity.ok().body(service.deletarCartaoCredito(idCartaoCredito));
    }



}
