package br.com.rdevs.ecommerce.produto.service;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueProdutoDTO;
import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import br.com.rdevs.ecommerce.estoque.repository.EstoqueRepository;
import br.com.rdevs.ecommerce.pedido.model.entity.TbStatusPedido;
import br.com.rdevs.ecommerce.produto.model.dto.*;

import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import br.com.rdevs.ecommerce.produto.model.entity.TbProdutoImagem;
import br.com.rdevs.ecommerce.produto.model.entity.TbTcCupom;
import br.com.rdevs.ecommerce.produto.model.entity.TbTcCupomItem;
import br.com.rdevs.ecommerce.produto.repository.CupomItemRepository;
import br.com.rdevs.ecommerce.produto.repository.CupomRepository;
import br.com.rdevs.ecommerce.produto.repository.ProdutoPageRepository;
import br.com.rdevs.ecommerce.produto.repository.ProdutoRepository;
import br.com.rdevs.ecommerce.produto.service.bo.ProdutoBo;
import br.com.rdevs.ecommerce.produto.service.bo.ProdutoImagemBo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoPageRepository pageRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private CupomItemRepository cupomItemRepository;

    @Autowired
    private ProdutoBo produtoBo;

    @Autowired
    private ProdutoImagemBo produtoImagemBo;

    @PersistenceContext
    private EntityManager manager;


    public List<ListaFabricantes> fabricantesPorSubCategoria(String idSubcategoria) {
        List<ListaFabricantes> listaFabricantes = new ArrayList<>();
        List<TbProduto> produtosConvert = new ArrayList<>();
        if (idSubcategoria.matches("[0-9]+")){
            Long produto = Long.parseLong(idSubcategoria);
            produtosConvert = produtoRepository.findBySubCategoriaProdutoIdSubCategoria(BigInteger.valueOf(produto));
        }else {
            produtosConvert = produtoRepository.findByNomeFantasiaContaining(idSubcategoria);
        }


        for (TbProduto produto : produtosConvert) {
            if (produto.getDsProduto() != null) {
                ListaFabricantes fabricante = new ListaFabricantes();
                fabricante.setNomeFabricante(produto.getNomeFabricante());

                listaFabricantes.add(fabricante);
            }
        }
        List<ListaFabricantes> listaFabricantes1 = listaFabricantes.stream().distinct().collect(Collectors.toList());
        return listaFabricantes1;
    }


    public List<ProdutoDTO> listarTodos(BigInteger idSubCategoria){
        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                "AND TSCP.ID_SUB_CATEGORIA = "+idSubCategoria+" "+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }
            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }

        return map.values().stream().collect(Collectors.toList());
    }









    public List<ProdutoDTO> buscarPorCdProduto(BigInteger cdProduto, BigInteger idCliente) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        if (idCliente == null) {
            idCliente = BigInteger.valueOf(1l);
        }

        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                "AND P.CD_PRODUTO = "+cdProduto+" "+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        Double pcDensconto = 1D;
        Double valorConvertido = null;
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }

            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }


        return map.values().stream().collect(Collectors.toList());
    }

    public List<ProdutoDTO> buscarPorFabricante(String nomeFabricante) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity = produtoRepository.findByNomeFabricante(nomeFabricante);
        for (TbProduto prod : listaEntity) {
            if (prod.getDsProduto() != null) {
                ProdutoDTO dto = produtoBo.parseToDTO(prod);
                CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
                SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();

                subCategoriaProduto.setIdSubCategoria(prod.getSubCategoriaProduto().getIdSubCategoria());
                subCategoriaProduto.setDsSubCategoria(prod.getSubCategoriaProduto().getDsSubCategoria());
                catdto.setIdCategoriaProduto(prod.getCategoriaProduto().getIdCategoriaProduto());
                catdto.setDsCategoriaProduto(prod.getCategoriaProduto().getDsCategoriaProduto());

                dto.setSubCategoriaProduto(subCategoriaProduto);
                dto.setCategoriaProduto(catdto);

                List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
                for (TbProdutoImagem produtoImagemEntity : prod.getImagens()) {
                    ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);

                    imagemsProdutodto.add(imagemDTO);
                }
                dto.setImagens(imagemsProdutodto);


                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), BigInteger.valueOf(4L));
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
                dto.setEstoques(estoqueProdutoDTO);


                listaDTO.add(dto);
            }
        }

        return listaDTO;
    }

//    public List<ProdutoDTO> buscarPorCategoriaPage(BigInteger idCategoriaProduto, BigInteger page) {
//        List<ProdutoDTO> listaDTO = new ArrayList<>();
//        Pageable firstPageWithTwoElements = PageRequest.of(Math.toIntExact(page), 6);
//        Page<TbProduto> listaEntity = pageRepository.findByCategoriaProdutoIdCategoriaProduto(idCategoriaProduto, firstPageWithTwoElements);
//        for (TbProduto prod : listaEntity) {
//            if (prod.getDsProduto() != null) {
//                if (prod.getDsProduto() != null) {
//                    ProdutoDTO dto = produtoBo.parseToDTO(prod);
//                    CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
//                    SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();
//
//                    subCategoriaProduto.setIdSubCategoria(prod.getSubCategoriaProduto().getIdSubCategoria());
//                    subCategoriaProduto.setDsSubCategoria(prod.getSubCategoriaProduto().getDsSubCategoria());
//                    catdto.setIdCategoriaProduto(prod.getCategoriaProduto().getIdCategoriaProduto());
//                    catdto.setDsCategoriaProduto(prod.getCategoriaProduto().getDsCategoriaProduto());
//
//                    dto.setSubCategoriaProduto(subCategoriaProduto);
//                    dto.setCategoriaProduto(catdto);
//
//                    List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
//                    for (TbProdutoImagem produtoImagemEntity : prod.getImagens()) {
//                        ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);
//
//                        imagemsProdutodto.add(imagemDTO);
//                    }
//                    dto.setImagens(imagemsProdutodto);
//
//                    TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
//                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
//                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
//                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
//                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
//                    dto.setEstoques(estoqueProdutoDTO);
//
//
//                    listaDTO.add(dto);
//                }
//            }
//        }
//
//        return listaDTO;
//
//    }

    public List<ProdutoDTO> buscarPorCategoria(BigInteger idCategoriaProduto) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity = produtoRepository.findByCategoriaProdutoIdCategoriaProduto(idCategoriaProduto);
        for (TbProduto prod : listaEntity) {
            if (prod.getDsProduto() != null) {
                if (prod.getDsProduto() != null) {
                    ProdutoDTO dto = produtoBo.parseToDTO(prod);
                    CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
                    SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();

                    subCategoriaProduto.setIdSubCategoria(prod.getSubCategoriaProduto().getIdSubCategoria());
                    subCategoriaProduto.setDsSubCategoria(prod.getSubCategoriaProduto().getDsSubCategoria());
                    catdto.setIdCategoriaProduto(prod.getCategoriaProduto().getIdCategoriaProduto());
                    catdto.setDsCategoriaProduto(prod.getCategoriaProduto().getDsCategoriaProduto());

                    dto.setSubCategoriaProduto(subCategoriaProduto);
                    dto.setCategoriaProduto(catdto);

                    List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
                    for (TbProdutoImagem produtoImagemEntity : prod.getImagens()) {
                        ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);

                        imagemsProdutodto.add(imagemDTO);
                    }
                    dto.setImagens(imagemsProdutodto);

                    TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), BigInteger.valueOf(4L));
                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
                    dto.setEstoques(estoqueProdutoDTO);


                    listaDTO.add(dto);
                }
            }
        }

        return listaDTO;

    }

//    public List<ProdutoDTO> buscarPorSubCategoriaPage(BigInteger idSubCategoriaProduto, BigInteger page) {
//        List<ProdutoDTO> listaDTO = new ArrayList<>();
//        Pageable firstPageWithTwoElements = PageRequest.of(Math.toIntExact(page), 6);
//
//        Page<TbProduto> listaEntity = pageRepository.findBySubCategoriaProdutoIdSubCategoria(idSubCategoriaProduto, firstPageWithTwoElements);
//
//        for (TbProduto prod : listaEntity) {
//            if (prod.getDsProduto() != null) {
//
//                ProdutoDTO dto = produtoBo.parseToDTO(prod);
//
//                CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
//                SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();
//
//                subCategoriaProduto.setIdSubCategoria(prod.getSubCategoriaProduto().getIdSubCategoria());
//                subCategoriaProduto.setDsSubCategoria(prod.getSubCategoriaProduto().getDsSubCategoria());
//                catdto.setIdCategoriaProduto(prod.getCategoriaProduto().getIdCategoriaProduto());
//                catdto.setDsCategoriaProduto(prod.getCategoriaProduto().getDsCategoriaProduto());
//
//                dto.setSubCategoriaProduto(subCategoriaProduto);
//                dto.setCategoriaProduto(catdto);
//
//                List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
//                for (TbProdutoImagem produtoImagemEntity : prod.getImagens()) {
//                    ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);
//
//                    imagemsProdutodto.add(imagemDTO);
//                }
//                dto.setImagens(imagemsProdutodto);
//
//                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
//                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
//                estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
//                estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
//                estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
//                dto.setEstoques(estoqueProdutoDTO);
//
//
//                listaDTO.add(dto);
//
//            }
//        }
//
//        return listaDTO;
//
//    }

    public List<ProdutoDTO> buscarPorSubCategoria(BigInteger idSubCategoriaProduto, BigInteger idCliente) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        if (idCliente == null) {
            idCliente = BigInteger.valueOf(1l);
        }

//        Query query2 = manager.createNativeQuery("SELECT\n" +
//                "tc.ID_CLIENTE,\n" +
//                "tcc.ID_CATEGORIA_CLIENTE,\n" +
//                "tcc.PC_DESCONTO_ECOMMERCE\n" +
//                "FROM TB_CLIENTE as tc, TB_CATEGORIA_CLIENTE as tcc\n" +
//                "WHERE tc.ID_CATEGORIA_CLIENTE = tcc.ID_CATEGORIA_CLIENTE\n" +
//                "AND tc.ID_CLIENTE = "+idCliente+" ");
//
//
//        List<Object []> cliente = query2.getResultList();



//        TbTcCupom tcCupom = cupomRepository.findByClienteIdCliente(idCliente);

        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                "AND TSCP.ID_SUB_CATEGORIA = "+idSubCategoriaProduto+" "+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        Double pcDensconto = 1D;
        Double valorConvertido = null;
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }

//            TbTcCupomItem tcCupomItem = cupomItemRepository.findByTcCupomClienteIdClienteAndProdutoCpCdProduto(idCliente, (BigInteger) produto[0]);
//
//            if (tcCupom == null || tcCupomItem == null) {
//                pcDensconto -= cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
//            } else if (tcCupomItem.getPcDesconto().doubleValue() < cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue()) {
//                pcDensconto -= cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
//            } else if (tcCupomItem.getPcDesconto().doubleValue() > cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue()) {
//                pcDensconto -= tcCupomItem.getPcDesconto().doubleValue();
//            } else {
//                pcDensconto -= cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
//            }
//
//            valorConvertido = produtoDTO.getValorUnidade().doubleValue() * pcDensconto;
//            produtoDTO.setValorUnidade(BigDecimal.valueOf(valorConvertido).setScale(2, RoundingMode.HALF_EVEN));

            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }


        return map.values().stream().collect(Collectors.toList());

    }

    public List<ProdutoDTO> buscarPorSubCategoria2(BigInteger idSubCategoriaProduto, BigInteger idCliente) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        if (idCliente == null) {
            idCliente = BigInteger.valueOf(1l);
        }
        TbCliente cliente = cadastroRepository.findByIdCliente(idCliente);
        TbTcCupom tcCupom = cupomRepository.findByClienteIdCliente(idCliente);

        List<TbProduto> listaEntity = produtoRepository.findBySubCategoriaProdutoIdSubCategoria(idSubCategoriaProduto);


        for (TbProduto prod : listaEntity) {
            Double pcDensconto = 1D;
            Double valorConvertido = null;
            if (prod.getDsProduto() != null) {

                TbTcCupomItem tcCupomItem = cupomItemRepository.findByTcCupomClienteIdClienteAndProdutoCpCdProduto(idCliente, prod.getCdProduto());
                ProdutoDTO dto = produtoBo.parseToDTO(prod);


                if (tcCupom == null || tcCupomItem == null) {
                    pcDensconto -= cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
                } else if (tcCupomItem.getPcDesconto().doubleValue() < cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue()) {
                    pcDensconto -= cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
                } else if (tcCupomItem.getPcDesconto().doubleValue() > cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue()) {
                    pcDensconto -= tcCupomItem.getPcDesconto().doubleValue();
                } else {
                    pcDensconto -= cliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
                }


                valorConvertido = dto.getValorUnidade().doubleValue() * pcDensconto;
                dto.setValorUnidade(BigDecimal.valueOf(valorConvertido).setScale(2, RoundingMode.HALF_EVEN));

                CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
                SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();

                subCategoriaProduto.setIdSubCategoria(prod.getSubCategoriaProduto().getIdSubCategoria());
                subCategoriaProduto.setDsSubCategoria(prod.getSubCategoriaProduto().getDsSubCategoria());
                catdto.setIdCategoriaProduto(prod.getCategoriaProduto().getIdCategoriaProduto());
                catdto.setDsCategoriaProduto(prod.getCategoriaProduto().getDsCategoriaProduto());

                dto.setSubCategoriaProduto(subCategoriaProduto);
                dto.setCategoriaProduto(catdto);

                List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
                for (TbProdutoImagem produtoImagemEntity : prod.getImagens()) {
                    ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);

                    imagemsProdutodto.add(imagemDTO);
                }
                dto.setImagens(imagemsProdutodto);

                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), BigInteger.valueOf(4L));
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
                dto.setEstoques(estoqueProdutoDTO);


                listaDTO.add(dto);


            }
        }

        return listaDTO;

    }

    public List<ProdutoDTO> produtosPromo() {
        Long[] produtosPromo = {};
        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                "AND P.CD_PRODUTO IN (76, 69, 46, 51, 80, 82, 79, 70)"+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }
            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }

        return map.values().stream().collect(Collectors.toList());
    }

    public List<ProdutoDTO> produtosDestaquesSemana() {
        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                "AND P.CD_PRODUTO IN (80, 81, 67, 69, 80, 82, 79, 55)"+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }
            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }

        return map.values().stream().collect(Collectors.toList());
    }

    public List<ProdutoDTO> produtosPopulares() {
        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                "AND P.CD_PRODUTO IN (77, 80, 89, 51, 48, 49, 50, 71)"+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }
            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }

        return map.values().stream().collect(Collectors.toList());
    }


    public List<ProdutoDTO> buscarPorSubCategoriaComFiltro(BigInteger idSubCategoriaProduto, String nomeFabricante, Integer idPreco){

        String filtro = "AND TSCP.ID_SUB_CATEGORIA = "+ idSubCategoriaProduto + " \n" ;


        if (nomeFabricante != null && idPreco == null ){
            filtro+= "AND P.NM_FABRICANTE LIKE '%"+ nomeFabricante +"%' \n";
        }else if (nomeFabricante == null && idPreco != null ){
            if (idPreco == 1){
                filtro+= "AND P.VL_UNIDADE < 10 \n";
            }else if(idPreco == 2){
                filtro+= "AND P.VL_UNIDADE >= 10 \n" +
                        "AND P.VL_UNIDADE < 20 ";
            }else if(idPreco == 3){
                filtro+= "AND P.VL_UNIDADE >= 20 \n" +
                        "AND P.VL_UNIDADE < 30 ";
            }else if(idPreco == 4){
                filtro+= "AND P.VL_UNIDADE >= 30 \n" +
                        "AND P.VL_UNIDADE < 40 ";
            }else if(idPreco == 5){
                filtro+= "AND P.VL_UNIDADE >= 40 \n" +
                        "AND P.VL_UNIDADE < 50 ";
//            }else if(idPreco == 6){
//                filtro+= "AND P.VL_UNIDADE >= 50";
            }else {
                filtro+= "AND P.VL_UNIDADE >= 50";
            }
        }else if(nomeFabricante != null && idPreco != null) {
            filtro+= "AND P.NM_FABRICANTE LIKE '%"+ nomeFabricante +"%' \n";
            if (idPreco == 1){
                filtro+= "AND P.VL_UNIDADE < 10 \n";
            }else if(idPreco == 2){
                filtro+= "AND P.VL_UNIDADE >= 10 \n" +
                        "AND P.VL_UNIDADE < 20 ";
            }else if(idPreco == 3){
                filtro+= "AND P.VL_UNIDADE >= 20 \n" +
                        "AND P.VL_UNIDADE < 30 ";
            }else if(idPreco == 4){
                filtro+= "AND P.VL_UNIDADE >= 30 \n" +
                        "AND P.VL_UNIDADE < 40 ";
            }else if(idPreco == 5){
                filtro+= "AND P.VL_UNIDADE >= 40 \n" +
                        "AND P.VL_UNIDADE < 50 ";
//            }else if(idPreco == 6){
//                filtro+= "AND P.VL_UNIDADE >= 50 ";
            }else {
                filtro+= "AND P.VL_UNIDADE >= 50 ";
            }
        } else {
           filtro+=" ";
        }



        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                filtro+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }
            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }

        return map.values().stream().collect(Collectors.toList());

    }



    public List<ProdutoDTO> buscarPorNome(String nomeFantasia, String nomeFabricante, Integer idPreco) {

        String filtro =  "AND P.NM_FANTASIA like '%"+nomeFantasia+"%' " ;

        if (nomeFabricante != null && idPreco == null ){
            filtro+= "AND P.NM_FABRICANTE LIKE '%"+ nomeFabricante +"%' \n";
        }else if (nomeFabricante == null && idPreco != null ){
            if (idPreco == 1){
                filtro+= "AND P.VL_UNIDADE < 10 \n";
            }else if(idPreco == 2){
                filtro+= "AND P.VL_UNIDADE >= 10 \n" +
                        "AND P.VL_UNIDADE < 20 ";
            }else if(idPreco == 3){
                filtro+= "AND P.VL_UNIDADE >= 20 \n" +
                        "AND P.VL_UNIDADE < 30 ";
            }else if(idPreco == 4){
                filtro+= "AND P.VL_UNIDADE >= 30 \n" +
                        "AND P.VL_UNIDADE < 40 ";
            }else if(idPreco == 5){
                filtro+= "AND P.VL_UNIDADE >= 40 \n" +
                        "AND P.VL_UNIDADE < 50 ";
            }else{
                filtro+= "AND P.VL_UNIDADE >= 50";
            }
        }else if(nomeFabricante != null && idPreco != null) {
            filtro+= "AND P.NM_FABRICANTE LIKE '%"+ nomeFabricante +"%' \n";
            if (idPreco == 1){
                filtro+= "AND P.VL_UNIDADE < 10 \n";
            }else if(idPreco == 2){
                filtro+= "AND P.VL_UNIDADE >= 10 \n" +
                        "AND P.VL_UNIDADE < 20 ";
            }else if(idPreco == 3){
                filtro+= "AND P.VL_UNIDADE >= 20 \n" +
                        "AND P.VL_UNIDADE < 30 ";
            }else if(idPreco == 4){
                filtro+= "AND P.VL_UNIDADE >= 30 \n" +
                        "AND P.VL_UNIDADE < 40 ";
            }else if(idPreco == 5){
                filtro+= "AND P.VL_UNIDADE >= 40 \n" +
                        "AND P.VL_UNIDADE < 50 ";
            }else{
                filtro+= "AND P.VL_UNIDADE >= 50 \n";
            }
        } else {
            filtro+=" ";
        }



        Map<BigInteger, ProdutoDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("select\n" +
                "P.CD_PRODUTO,\n" + //0
                "P.NM_FANTASIA,\n" +//1
                "P.NM_FABRICANTE,\n" +//2
                "P.VL_UNIDADE,\n" +//3
                "P.DS_PRODUTO,\n" +//4
                "TPI.ID_IMAGEM,\n" +//5
                "TPI.DS_URL,\n" +//6
                "TCP.ID_CATEGORIA,\n" +//7
                "TCP.DS_CATEGORIA,\n" +//8
                "TSCP.ID_SUB_CATEGORIA,\n" +//9
                "TSCP.DS_SUB_CATEGORIA,\n" +//10
                "TSP.ID_STATUS_PRODUTO,\n" +//11
                "TSP.DS_STATUS_PRODUTO,\n" +//12
                "TPFE.CD_FILIAL,\n" +//13
                "TPFE.QT_ESTOQUE,\n" +//14
                "TPFE.QT_EMPENHO,\n" +//15
                "TPFE.QT_BASE,\n" +//16
                "TPFE.CD_ESTOQUE\n" +//17
                "\n" +
                "from TB_PRODUTO P, TB_PRODUTO_IMAGEM TPI, TB_CATEGORIA_PRODUTO TCP,TB_SUB_CATEGORIA_PRODUTO TSCP, TB_STATUS_PRODUTO TSP, TB_PRODUTO_FILIAL_ESTOQUE TPFE\n" +
                "where P.CD_PRODUTO = TPI.CD_PRODUTO\n" +
                "AND P.ID_CATEGORIA = TCP.ID_CATEGORIA\n" +
                "AND P.ID_SUB_CATEGORIA = TSCP.ID_SUB_CATEGORIA\n" +
                "AND P.ID_STATUS_PRODUTO= TSP.ID_STATUS_PRODUTO\n" +
                "AND TPFE.CD_PRODUTO = P.CD_PRODUTO\n" +
                "AND TPFE.CD_FILIAL=4\n" +
                filtro+
                "ORDER BY P.CD_PRODUTO");

        List<Object []> listEntity = query.getResultList();
        Double pcDensconto = 1D;
        Double valorConvertido = null;
        for (Object [] produto: listEntity){
            Integer codigo = ((BigInteger) produto [0]).intValue();
            ProdutoDTO produtoDTO = null;
            if(!map.containsKey(codigo)){
                produtoDTO = new ProdutoDTO();
                produtoDTO.setCdProduto((BigInteger) produto [0]);
                produtoDTO.setNomeFantasia((String) produto [1]);
                produtoDTO.setNomeFabricante((String) produto [2]);
                produtoDTO.setValorUnidade((BigDecimal) produto [3]);
                produtoDTO.setDsProduto((String) produto [4]);

                //tbProdutoImagem
                ProdutoImagemDTO produtoImagemDTO = new ProdutoImagemDTO();
                produtoImagemDTO.setCdProduto((BigInteger) produto[0]);
                produtoImagemDTO.setIdImagem((BigInteger) produto[5]);
                produtoImagemDTO.setDsUrl((String) produto[6]);

                List<ProdutoImagemDTO> produtosImagemsDTOs = new ArrayList<>();
                produtosImagemsDTOs.add(produtoImagemDTO);
                produtoDTO.setImagens(produtosImagemsDTOs);

                //tbCategoria
                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
                categoriaProdutoDTO.setIdCategoriaProduto((BigInteger) produto [7]);
                categoriaProdutoDTO.setDsCategoriaProduto((String) produto [8]);
                produtoDTO.setCategoriaProduto(categoriaProdutoDTO);

                //tbSubCategoria
                SubCategoriaProdutoDTO subCategoriaProdutoDTO = new SubCategoriaProdutoDTO();
                subCategoriaProdutoDTO.setIdSubCategoria((BigInteger) produto [9]);
                subCategoriaProdutoDTO.setDsSubCategoria((String) produto [10]);
                produtoDTO.setSubCategoriaProduto(subCategoriaProdutoDTO);

                produtoDTO.setIdStatusProduto((BigInteger) produto [11]);

                //tbEstoque
                EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                estoqueProdutoDTO.setCdFilial((BigInteger) produto [13]);
                estoqueProdutoDTO.setQtEstoque((Integer) produto [14]);
                estoqueProdutoDTO.setQtEmpenho((Integer) produto [15]);
                produtoDTO.setEstoques(estoqueProdutoDTO);

            }

            if (produto[4]!=null) {
                map.put(produtoDTO.getCdProduto(), produtoDTO);
            }

        }


        return map.values().stream().collect(Collectors.toList());
    }






















}
