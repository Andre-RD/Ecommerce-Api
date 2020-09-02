package br.com.rdevs.ecommerce.produto.service;

import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueProdutoDTO;
import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import br.com.rdevs.ecommerce.produto.model.dto.*;

import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import br.com.rdevs.ecommerce.produto.model.entity.TbProdutoImagem;
import br.com.rdevs.ecommerce.produto.repository.ProdutoRepository;
import br.com.rdevs.ecommerce.produto.service.bo.ProdutoBo;
import br.com.rdevs.ecommerce.produto.service.bo.ProdutoImagemBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;


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


                List<EstoqueProdutoDTO> estoquesProdutosDTO = new ArrayList<>();

                for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());


                    estoquesProdutosDTO.add(estoqueProdutoDTO);
                }

                dto.setEstoques(estoquesProdutosDTO);


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


                List<EstoqueProdutoDTO> estoquesProdutosDTO = new ArrayList<>();

                for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());


                    estoquesProdutosDTO.add(estoqueProdutoDTO);
                }

                dto.setEstoques(estoquesProdutosDTO);


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


                List<EstoqueProdutoDTO> estoquesProdutosDTO = new ArrayList<>();

                for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());


                    estoquesProdutosDTO.add(estoqueProdutoDTO);
                }

                dto.setEstoques(estoquesProdutosDTO);


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


                List<EstoqueProdutoDTO> estoquesProdutosDTO = new ArrayList<>();

                for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
                    EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                    estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                    estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                    estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());


                    estoquesProdutosDTO.add(estoqueProdutoDTO);
                }

                dto.setEstoques(estoquesProdutosDTO);


                listaDTO.add(dto);
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

                    List<EstoqueProdutoDTO> estoquesProdutosDTO = new ArrayList<>();
                    for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
                        EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                        estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                        estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                        estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());


                        estoquesProdutosDTO.add(estoqueProdutoDTO);
                    }
                    dto.setEstoques(estoquesProdutosDTO);

                    listaDTO.add(dto);
                }
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

                    List<EstoqueProdutoDTO> estoquesProdutosDTO = new ArrayList<>();
                    for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
                        EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                        estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                        estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                        estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());


                        estoquesProdutosDTO.add(estoqueProdutoDTO);
                    }
                    dto.setEstoques(estoquesProdutosDTO);

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

                    List<EstoqueProdutoDTO> estoquesProdutosDTO = new ArrayList<>();
                    for (TbProdutoFilialEstoque produtoEstoqueEntity : prod.getProdutosEstoqueEntity()) {
                        EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
                        estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
                        estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
                        estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());


                        estoquesProdutosDTO.add(estoqueProdutoDTO);
                    }
                    dto.setEstoques(estoquesProdutosDTO);

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
            if(!listaFabricantes.contains(produto.getNomeFabricante())){
                fabricante.setNomeFabricante(produto.getNomeFabricante());
                listaFabricantes.add(fabricante);
            }

        }

        return listaFabricantes;
    }

//    public List<ProdutoDTO> buscarPorSubCategoriaPreço(Long idSubCategoriaProduto)

}
