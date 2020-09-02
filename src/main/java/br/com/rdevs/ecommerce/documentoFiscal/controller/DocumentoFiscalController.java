package br.com.rdevs.ecommerce.documentoFiscal.controller;

import br.com.rdevs.ecommerce.documentoFiscal.model.dto.PostDocumentoFiscalDTO;
import br.com.rdevs.ecommerce.documentoFiscal.service.DocumentoFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DocumentoFiscalController {
    @Autowired
    DocumentoFiscalService documentoFiscalService;

    @GetMapping("/documentoFiscal")
    public ResponseEntity buscarNotas(){
        return ResponseEntity.ok().body(documentoFiscalService.listaDocumentos());
    }

    @GetMapping("/documentoFiscal/{idCliete}")
    public ResponseEntity buscarNotas(@PathVariable("idCliete") Long idCliete){
        return ResponseEntity.ok().body(documentoFiscalService.listarPorIdCliente(idCliete));
    }

    @PostMapping("/adicionaNota")
    public ResponseEntity inserirNotaF(@RequestBody PostDocumentoFiscalDTO nfDto){
        return ResponseEntity.ok().body(documentoFiscalService.inserirItem(nfDto));
    }


}
