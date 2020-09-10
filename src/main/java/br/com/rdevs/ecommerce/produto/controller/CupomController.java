package br.com.rdevs.ecommerce.produto.controller;

import br.com.rdevs.ecommerce.produto.service.CupomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CupomController {

    @Autowired
    CupomService cupomService;

    @GetMapping(value = "/cupomDesconto/{idCliente}")
    public ResponseEntity cupomPorCliente(@PathVariable("idCliente") Long idCliente){
        return ResponseEntity.ok().body(cupomService.buscarCuponsIdCliente(idCliente));
    }

    @RequestMapping(value = "/cupomDesconto/{idCliente}/{cdProduto}",method = RequestMethod.GET)
    public ResponseEntity cupomPorClienteProduto(@PathVariable("idCliente") Long idCliente,@PathVariable("cdProduto") Long cdProduto){
        return ResponseEntity.ok().body(cupomService.buscarPorClienteEProduto(idCliente , cdProduto));
    }



}
