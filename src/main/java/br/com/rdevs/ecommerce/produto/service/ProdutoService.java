package br.com.rdevs.ecommerce.produto.service;

import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueProdutoDTO;
import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import br.com.rdevs.ecommerce.estoque.repository.EstoqueRepository;
import br.com.rdevs.ecommerce.produto.model.dto.*;

import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import br.com.rdevs.ecommerce.produto.model.entity.TbProdutoImagem;
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

import java.util.ArrayList;
import java.util.List;
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
    private ProdutoBo produtoBo;

    @Autowired
    private ProdutoImagemBo produtoImagemBo;


    @PersistenceContext
    private EntityManager em;

    public List<ProdutoDTO> listarTodos() {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity = produtoRepository.findAll();

        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {
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


                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.getOne(4L);
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

    public List<ProdutoDTO> buscarPorNome(String nomeFantasia) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity = produtoRepository.findByNomeFantasia(nomeFantasia);

        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {
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


                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
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

    public List<ProdutoDTO> buscarPorCdProduto(Long cdProduto){
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        TbProduto prod = produtoRepository.findByCdProduto(cdProduto);
            if(prod.getDsProduto()!=null) {
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

//                List<TbProdutoFilialEstoque> listEstoques = prod.getProdutosEstoqueEntity();

                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
                dto.setEstoques(estoqueProdutoDTO);

//                for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
//                    if ()
//                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
//                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
//                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
//                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
//
//
//                    estoquesProdutosDTO.add(estoqueProdutoDTO);
//                }




                listaDTO.add(dto);
            }


        return listaDTO;
    }

    public List<ProdutoDTO> buscarPorFabricante(String nomeFabricante) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity = produtoRepository.findByNomeFabricante(nomeFabricante);
        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {
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


                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
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

    public List<ProdutoDTO> buscarPorCategoriaPage(Long idCategoriaProduto, Long page){
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        Pageable firstPageWithTwoElements = PageRequest.of(Math.toIntExact(page), 6);
        Page<TbProduto> listaEntity = pageRepository.findByCategoriaProdutoIdCategoriaProduto(idCategoriaProduto,firstPageWithTwoElements);
        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {
                if(prod.getDsProduto()!=null) {
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

                    TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
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

    public List<ProdutoDTO> buscarPorCategoria(Long idCategoriaProduto){
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity = produtoRepository.findByCategoriaProdutoIdCategoriaProduto(idCategoriaProduto);
        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {
                if(prod.getDsProduto()!=null) {
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

                    TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
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

    public List<ProdutoDTO> buscarPorSubCategoriaPage(Long idSubCategoriaProduto, Long page){
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        Pageable firstPageWithTwoElements = PageRequest.of(Math.toIntExact(page), 6);

        Page<TbProduto> listaEntity = pageRepository.findBySubCategoriaProdutoIdSubCategoria(idSubCategoriaProduto,firstPageWithTwoElements);

        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {

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

                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
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

    public List<ProdutoDTO> buscarPorSubCategoria(Long idSubCategoriaProduto){
        List<ProdutoDTO> listaDTO = new ArrayList<>();

        List<TbProduto> listaEntity = produtoRepository.findBySubCategoriaProdutoIdSubCategoria(idSubCategoriaProduto);

        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {

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

                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
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

    public List<ProdutoDTO> buscaPorCategoriaESubCategoria(Long idCategoriaProduto, Long idSubCategoriaProduto){

        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity =  produtoRepository.findByCategoriaProdutoIdCategoriaProdutoAndSubCategoriaProdutoIdSubCategoria(idCategoriaProduto,idSubCategoriaProduto);
        for (TbProduto prod : listaEntity) {
            if(prod.getDsProduto()!=null) {
                if(prod.getDsProduto()!=null) {
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

                    TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(),4L);
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

    public List<ListaFabricantes> fabricantesPorSubCategoria(Long idSubcategoria){
        List<ListaFabricantes> listaFabricantes = new ArrayList<>();
        List<TbProduto> produtos = produtoRepository.findBySubCategoriaProdutoIdSubCategoria(idSubcategoria);
        for (TbProduto produto: produtos){
            ListaFabricantes fabricante = new ListaFabricantes();
            fabricante.setNomeFabricante(produto.getNomeFabricante());

            listaFabricantes.add(fabricante);
        }
        List<ListaFabricantes> listaFabricantes1 = listaFabricantes.stream().distinct().collect(Collectors.toList());
        return listaFabricantes1;
    }

    public Page<TbProduto> buscarPaginas(Long page){

        Pageable firstPageWithTwoElements = PageRequest.of(Math.toIntExact(page), 2);
        Page<TbProduto> listaEntity = pageRepository.findAll(firstPageWithTwoElements);


        return listaEntity;
    }


}
