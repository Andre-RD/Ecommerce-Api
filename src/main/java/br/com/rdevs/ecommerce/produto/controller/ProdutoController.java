package br.com.rdevs.ecommerce.produto.controller;

import br.com.rdevs.ecommerce.produto.model.dto.ProdutoDTO;
import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import br.com.rdevs.ecommerce.produto.service.ProdutoService;
import io.swagger.annotations.ApiOperation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.awt.print.Pageable;
import java.util.List;

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

    @ApiOperation(value = "Buscar Produto Por idcategoria com paginação")
    @GetMapping("/produtos/categoriaP/{idCategoriaProduto}")
    public ResponseEntity buscarPorCategoriaPage(@PathVariable("idCategoriaProduto") Long idCategoriaProduto, Long page) {
        return ResponseEntity.ok().body(service.buscarPorCategoriaPage(idCategoriaProduto, page));
    }

    @ApiOperation(value = "Buscar Produto Por idcategoria")
    @GetMapping("/produtos/categoria/{idCategoriaProduto}")
    public ResponseEntity buscarPorCategoria(@PathVariable("idCategoriaProduto") Long idCategoriaProduto) {
        return ResponseEntity.ok().body(service.buscarPorCategoria(idCategoriaProduto));
    }


    @ApiOperation(value = "Buscar Produto Por Sub Categoria com paginação")
    @RequestMapping(value = "/produtos/subcategoriaP/{idSubCategoria}",method = RequestMethod.GET)
    public ResponseEntity buscarPorSubCategoriaPage(@PathVariable("idSubCategoria") Long idSubCategoria, Long page){

        return ResponseEntity.ok().body(service.buscarPorSubCategoriaPage(idSubCategoria,page));
    }

    @ApiOperation(value = "Buscar Produto Por Sub Categoria")
    @GetMapping("/produtos/subcategoria/{idSubCategoria}")
    public ResponseEntity buscarPorSubCategoria(@PathVariable("idSubCategoria") Long idSubCategoria){
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


    @GetMapping(value = "/fabricantes/{idSubCategoria}")
    public ResponseEntity nomesFabrincates(@PathVariable("idSubCategoria") Long idSubCategoria){

        return ResponseEntity.ok().body(service.fabricantesPorSubCategoria(idSubCategoria));
    }

    @GetMapping(value = "/produto/page/{pagina}")
    public ResponseEntity buscarProdutoPagina(@PathVariable("pagina") Long pagina){
        return ResponseEntity.ok().body(service.buscarPaginas(pagina));
    }












}
