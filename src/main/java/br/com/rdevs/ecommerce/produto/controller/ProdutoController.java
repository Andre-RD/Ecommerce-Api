package br.com.rdevs.ecommerce.produto.controller;

import br.com.rdevs.ecommerce.cadastro.model.dto.ResultData;
import br.com.rdevs.ecommerce.produto.service.ProdutoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoService service;


    @ApiOperation(value = "Buscar Produto Por fabricante")
    @GetMapping("/produtos/fabricante/{nomeFabricante}")
    public ResponseEntity buscarPorFabricante(@PathVariable("nomeFabricante") String nomeFabricante) {
        return ResponseEntity.ok().body(service.buscarPorFabricante(nomeFabricante));
    }

//    @ApiOperation(value = "Buscar Produto Por idcategoria com paginação")
//    @GetMapping("/produtos/categoriaP/{idCategoriaProduto}")
//    public ResponseEntity buscarPorCategoriaPage(@PathVariable("idCategoriaProduto") Long idCategoriaProduto, Long page) {
//        return ResponseEntity.ok().body(service.buscarPorCategoriaPage(idCategoriaProduto, page));
//    }

    @ApiOperation(value = "Buscar Produto Por idcategoria")
    @GetMapping("/produtos/categoria/{idCategoriaProduto}")
    public ResponseEntity buscarPorCategoria(@PathVariable("idCategoriaProduto") BigInteger idCategoriaProduto) {
        return ResponseEntity.ok().body(service.buscarPorCategoria(idCategoriaProduto));
    }


//    @ApiOperation(value = "Buscar Produto Por Sub Categoria com paginação")
//    @RequestMapping(value = "/produtos/subcategoriaP/{idSubCategoria}",method = RequestMethod.GET)
//    public ResponseEntity buscarPorSubCategoriaPage(@PathVariable("idSubCategoria") Long idSubCategoria, Long page){
//
//        return ResponseEntity.ok().body(service.buscarPorSubCategoriaPage(idSubCategoria,page));
//    }



    @ApiOperation(value = "Buscar Produtos dos cards de Promoção")
    @GetMapping("/produtos/produtoPromo")
    public ResponseEntity produtosPromo() {
        return ResponseEntity.ok().body(service.produtosPromo());
    }

    @ApiOperation(value = "Buscar Produtos dos cards de Destaque da semana")
    @GetMapping("/produtos/destaqueSemana")
    public ResponseEntity produtosDestaquesdaSemana() {
        return ResponseEntity.ok().body(service.produtosDestaquesSemana());
    }



    @ApiOperation(value = "Buscar Produtos dos cards de populares")
    @GetMapping("/produtos/populares")
    public ResponseEntity produtosPopulares() {
        return ResponseEntity.ok().body(service.produtosPopulares());
    }




//    @ApiOperation(value = "Buscar Produto Por Sub Categoria")
//    @RequestMapping(value = "/produtos/subcategoria/{idSubCategoria}",method = RequestMethod.GET)
//    public ResponseEntity buscarPorSubCategoria(@PathVariable("idSubCategoria") BigInteger idSubCategoria,@RequestParam(value = "idCliente", required=false) BigInteger idCliente){
//        if (idCliente==null){
//            idCliente= BigInteger.valueOf(1L);
//            return ResponseEntity.ok().body(service.buscarPorSubCategoria(idSubCategoria, idCliente));
//        }
//        else {
//            return ResponseEntity.ok().body(service.buscarPorSubCategoria(idSubCategoria, idCliente));
//        }
//    }

    @ApiOperation(value = "Buscar Produto Por Sub Categoria2")
    @RequestMapping(value = "/produtos/subcategoria2/{idSubCategoria}",method = RequestMethod.GET)
    public ResponseEntity buscarPorSubCategoria2(@PathVariable("idSubCategoria") BigInteger idSubCategoria,@RequestParam(value = "idCliente", required=false) BigInteger idCliente){
        if (idCliente==null){
            idCliente= BigInteger.valueOf(1L);
            return ResponseEntity.ok().body(service.buscarPorSubCategoria2(idSubCategoria, idCliente));
        }
        else {
            return ResponseEntity.ok().body(service.buscarPorSubCategoria2(idSubCategoria, idCliente));
        }

    }




    @GetMapping( "/fabricantes/{idSubCategoria}")
    public ResponseEntity nomesFabrincates(@PathVariable("idSubCategoria") String idSubCategoria){
        return ResponseEntity.ok().body(service.fabricantesPorSubCategoria(idSubCategoria));
    }



    @ApiOperation(value = "Buscar Produto Por cdProduto")
    @RequestMapping(value = "/produtos/codigo/{cdProduto}",method = RequestMethod.GET)
    public ResponseEntity buscarPorCdProduto(@PathVariable("cdProduto") BigInteger cdProduto, @RequestParam(value = "idCliente", required=false) BigInteger idCliente){
        if (idCliente==null){
            idCliente= BigInteger.valueOf(1L);
            return ResponseEntity.ok().body(service.buscarPorCdProduto(cdProduto,idCliente));
        }
        else {
            return ResponseEntity.ok().body(service.buscarPorCdProduto(cdProduto,idCliente));
        }
    }

//    @ApiOperation(value = "Buscar Produto Por categoria e subcategoria")
//    @RequestMapping(value = "/produtos/categoria/{idCategoriaProduto}/{idSubCategoria}",method = RequestMethod.GET)
//    public ResponseEntity buscarPorCdProduto(@PathVariable("idCategoriaProduto")Long idCategoriaProduto, @PathVariable("idSubCategoria") Long idSubCategoria) {
//        return ResponseEntity.ok().body(service.buscaPorCategoriaESubCategoria(idCategoriaProduto,idSubCategoria));
//    }
    @GetMapping(value = "/produtos/ListarTudos/{idSubCategoria}")
    public ResponseEntity buscarTodos(@PathVariable("idSubCategoria") BigInteger idSubCategoria){
        return ResponseEntity.ok().body(service.listarTodos(idSubCategoria));
    }



    @ApiOperation(value = "Buscar Produto Por Sub Categoria")
    @RequestMapping(value = "/produtos/subcategoria/{idSubCategoria}",method = RequestMethod.GET)
    public ResponseEntity buscarPorSubCategoriaFiltro(@PathVariable("idSubCategoria") BigInteger idSubCategoria,@RequestParam(value = "nomeFabricante", required=false) String nomeFabricante,@RequestParam(value = "idPreco", required=false) Integer idPreco ){
        String comparativo = "0";
        if (nomeFabricante.equals(comparativo) && idPreco == 0){
            nomeFabricante="";
            idPreco=null;
            return ResponseEntity.ok().body(service.buscarPorSubCategoriaComFiltro(idSubCategoria, nomeFabricante,idPreco));
        }else if (idPreco == 0){
            idPreco=null;
            return ResponseEntity.ok().body(service.buscarPorSubCategoriaComFiltro(idSubCategoria, nomeFabricante,idPreco));
        }else if (nomeFabricante.equals(comparativo) ){
            nomeFabricante="";
            return ResponseEntity.ok().body(service.buscarPorSubCategoriaComFiltro(idSubCategoria, nomeFabricante,idPreco));
        }else {
            return ResponseEntity.ok().body(service.buscarPorSubCategoriaComFiltro(idSubCategoria, nomeFabricante,idPreco));
        }
    }



    @ApiOperation(value = "Buscar Produto Por Nome")
    @GetMapping("/produtos/nomeFantasia/{nomeFantasia}")
    public ResponseEntity buscarPorProduto(@PathVariable("nomeFantasia") String nomeFantasia,@RequestParam(value = "nomeFabricante", required=false) String nomeFabricante,@RequestParam(value = "idPreco", required=false) Integer idPreco ) {
        ResultData resultData = null;
        String comparativo = "0";

        if (nomeFabricante.equals(comparativo) && idPreco == 0){
            nomeFabricante="";
            idPreco=null;
            return ResponseEntity.ok().body(service.buscarPorNome(nomeFantasia,nomeFabricante,idPreco));
        }else if (idPreco == 0){
            idPreco=null;
            return ResponseEntity.ok().body(service.buscarPorNome(nomeFantasia,nomeFabricante,idPreco));
        }else if (nomeFabricante.equals(comparativo) ){
            nomeFabricante="";
            return ResponseEntity.ok().body(service.buscarPorNome(nomeFantasia,nomeFabricante,idPreco));
        }else {
            return ResponseEntity.ok().body(service.buscarPorNome(nomeFantasia,nomeFabricante,idPreco));
        }


    }








}
