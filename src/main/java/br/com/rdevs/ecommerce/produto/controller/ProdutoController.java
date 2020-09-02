package br.com.rdevs.ecommerce.produto.controller;

import br.com.rdevs.ecommerce.produto.service.ProdutoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @ApiOperation(value = "Listar todos os produtos")
    @GetMapping("/produtos")
    public ResponseEntity<Object> listarTodos() {
        return ResponseEntity.ok().body(service.listarTodos());
    }

    @ApiOperation(value = "Buscar Produto Por Nome")
    @GetMapping("/produtos/nomeFantasia/{nomeFantasia}")
    public ResponseEntity buscarPorProduto(@PathVariable("nomeFantasia") String nomeFantasia) {
        return ResponseEntity.ok().body(service.buscarPorNome(nomeFantasia));
    }

    @ApiOperation(value = "Buscar Produto Por fabricante")
    @GetMapping("/produtos/fabricante/{nomeFabricante}")
    public ResponseEntity buscarPorFabricante(@PathVariable("nomeFabricante") String nomeFabricante) {
        return ResponseEntity.ok().body(service.buscarPorFabricante(nomeFabricante));
    }

    @ApiOperation(value = "Buscar Produto Por idcategoria")
    @GetMapping("/produtos/categoria/{idCategoriaProduto}")
    public ResponseEntity buscarPorCategoria(@PathVariable("idCategoriaProduto") Long idCategoriaProduto) {
        return ResponseEntity.ok().body(service.buscarPorCategoria(idCategoriaProduto));
    }

    @ApiOperation(value = "Buscar Produto Por Sub Categoria")
    @GetMapping("/produtos/subcategoria/{idSubCategoria}")
    public ResponseEntity buscarPorSubCategoria(@PathVariable("idSubCategoria") Long idSubCategoria) {
        return ResponseEntity.ok().body(service.buscarPorSubCategoria(idSubCategoria));
    }

    @ApiOperation(value = "Buscar Produto Por cdProduto")
    @GetMapping("/produtos/codigo/{cdProduto}")
    public ResponseEntity buscarPorCdProduto(@PathVariable("cdProduto") Long cdProduto) {
        return ResponseEntity.ok().body(service.buscarPorCdProduto(cdProduto));
    }

    @ApiOperation(value = "Buscar Produto Por categoria e subcategoria")
    @RequestMapping(value = "/produtos/categoria/{idCategoriaProduto}/{idSubCategoria}",method = RequestMethod.GET)
    public ResponseEntity buscarPorCdProduto(@PathVariable("idCategoriaProduto")Long idCategoriaProduto,@PathVariable("idSubCategoria") Long idSubCategoria) {
        return ResponseEntity.ok().body(service.buscaPorCategoriaESubCategoria(idCategoriaProduto,idSubCategoria));
    }

    @GetMapping("/fabricantes/{idSubCategoria}")
    public ResponseEntity nomesFabrincates(@PathVariable("idSubCategoria") Long idSubCategoria){
        return ResponseEntity.ok().body(service.fabricantesPorSubCategoria(idSubCategoria));
    }












}
