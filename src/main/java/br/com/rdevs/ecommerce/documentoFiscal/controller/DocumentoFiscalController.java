package br.com.rdevs.ecommerce.documentoFiscal.controller;

import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.PostDocumentoFiscalDTO;
import br.com.rdevs.ecommerce.documentoFiscal.service.DocumentoFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

@RestController
public class DocumentoFiscalController {
    @Autowired
    DocumentoFiscalService documentoFiscalService;

    @Autowired
    CadastroRepository cadastroRepository;

    @GetMapping("/documentoFiscalId/{idDocumentoFiscal}")
    public ResponseEntity buscarPorIDnota(@PathVariable("idDocumentoFiscal") Long idDocumentoFiscal){
        return ResponseEntity.ok().body(documentoFiscalService.listaDocumentosPorID(idDocumentoFiscal));
    }

    @GetMapping("/documentoFiscal/{idCliete}")
    public ResponseEntity buscarNotas(@PathVariable("idCliete") Long idCliete){
        return ResponseEntity.ok().body(documentoFiscalService.listarPorIdCliente(idCliete));
    }

    @PostMapping("/adicionaNota")
    public ResponseEntity inserirNotaF(@RequestBody PostDocumentoFiscalDTO nfDto){
        String email = cadastroRepository.findByIdCliente(nfDto.getIdCliente()).getDsEmail();
        SimpleMailMessage message = new SimpleMailMessage();

        return ResponseEntity.ok().body(documentoFiscalService.inserirItem(nfDto));
    }

    @PostMapping("/enviaEmail/{idDocumentoFiscal}")
    public ResponseEntity enviaEmail(@PathVariable("idDocumentoFiscal") Long idDocumentoFiscal){
        return ResponseEntity.ok().body(documentoFiscalService.listaDocumentosPorID(idDocumentoFiscal));
    }




}
