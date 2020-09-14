package br.com.rdevs.ecommerce.produto.service;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.estoque.model.dto.EstoqueProdutoDTO;
import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import br.com.rdevs.ecommerce.estoque.repository.EstoqueRepository;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private EntityManager em;

    public List<ListaFabricantes> fabricantesPorSubCategoria(String idSubcategoria) {
        List<ListaFabricantes> listaFabricantes = new ArrayList<>();
        List<TbProduto> produtosConvert = new ArrayList<>();
        if (idSubcategoria.matches("[0-9]+")){
            Long produto = Long.parseLong(idSubcategoria);
            produtosConvert = produtoRepository.findBySubCategoriaProdutoIdSubCategoria(produto);
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


    public List<ProdutoDTO> buscarPorNome(String nomeFantasia) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        List<TbProduto> listaEntity = produtoRepository.findByNomeFantasiaContaining(nomeFantasia);

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


                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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

    public List<ProdutoDTO> buscarPorCdProduto(Long cdProduto, Long idCliente) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        if (idCliente == null) {
            idCliente = 1l;
        }
        TbCliente cliente = cadastroRepository.findByIdCliente(idCliente);
        TbTcCupom tcCupom = cupomRepository.findByClienteIdCliente(idCliente);
        TbTcCupomItem tcCupomItem = cupomItemRepository.findByTcCupomClienteIdClienteAndProdutoCpCdProduto(idCliente, cdProduto);
        TbProduto prod = produtoRepository.findByCdProduto(cdProduto);

        Double pcDensconto = 1D;
        Double valorConvertido = null;

        if (prod.getDsProduto() != null) {
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

//                List<TbProdutoFilialEstoque> listEstoques = prod.getProdutosEstoqueEntity();

            TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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


                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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

    public List<ProdutoDTO> buscarPorCategoriaPage(Long idCategoriaProduto, Long page) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        Pageable firstPageWithTwoElements = PageRequest.of(Math.toIntExact(page), 6);
        Page<TbProduto> listaEntity = pageRepository.findByCategoriaProdutoIdCategoriaProduto(idCategoriaProduto, firstPageWithTwoElements);
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

                    TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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

    public List<ProdutoDTO> buscarPorCategoria(Long idCategoriaProduto) {
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

                    TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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

    public List<ProdutoDTO> buscarPorSubCategoriaPage(Long idSubCategoriaProduto, Long page) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        Pageable firstPageWithTwoElements = PageRequest.of(Math.toIntExact(page), 6);

        Page<TbProduto> listaEntity = pageRepository.findBySubCategoriaProdutoIdSubCategoria(idSubCategoriaProduto, firstPageWithTwoElements);

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

                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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

    public List<ProdutoDTO> buscarPorSubCategoria(Long idSubCategoriaProduto, Long idCliente) {
        List<ProdutoDTO> listaDTO = new ArrayList<>();
        if (idCliente == null) {
            idCliente = 1l;
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

                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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

    public List<ProdutoDTO2> buscarPorSubCategoria2(Long idSubCategoriaProduto, Long idCliente) {
        List<ProdutoDTO2> listaDTO = new ArrayList<>();
        if (idCliente == null) {
            idCliente = 1l;
        }
        TbCliente cliente = cadastroRepository.findByIdCliente(idCliente);
        TbTcCupom tcCupom = cupomRepository.findByClienteIdCliente(idCliente);
        List<TbProduto> listaEntity = produtoRepository.findBySubCategoriaProdutoIdSubCategoria(idSubCategoriaProduto);


        for (TbProduto prod : listaEntity) {
            Double pcDensconto = 1D;
            Double valorConvertido = null;
            if (prod.getDsProduto() != null) {

                TbTcCupomItem tcCupomItem = cupomItemRepository.findByTcCupomClienteIdClienteAndProdutoCpCdProduto(idCliente, prod.getCdProduto());
                ProdutoDTO2 dto = new ProdutoDTO2();
                dto.setCdProduto(prod.getCdProduto());
                dto.setIdStatusProduto(prod.getIdStatusProduto());
                dto.setNomeFantasia(prod.getNomeFantasia());
                dto.setNomeFabricante(prod.getNomeFabricante());
                dto.setDsProduto(prod.getDsProduto());
                dto.setValorUnidade(prod.getValorUnidade());


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

                dto.setSubCategoriaProduto(subCategoriaProduto);

                dto.setDsUrl(prod.getImagens().get(0).getDsUrl());


                TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(prod.getCdProduto(), 4L);
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
        Long[] produtosPromo = {76L, 69L, 46L, 51L, 80L, 82L, 79L, 70L};
        List<ProdutoDTO> produtosDTOS = new ArrayList<>();

        for (Long cdProduto : produtosPromo) {
            TbProduto produtosEntity = produtoRepository.findByCdProduto(cdProduto);
            ProdutoDTO produtoDTO = produtoBo.parseToDTO(produtosEntity);


            CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
            SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();

            subCategoriaProduto.setIdSubCategoria(produtosEntity.getSubCategoriaProduto().getIdSubCategoria());
            subCategoriaProduto.setDsSubCategoria(produtosEntity.getSubCategoriaProduto().getDsSubCategoria());
            catdto.setIdCategoriaProduto(produtosEntity.getCategoriaProduto().getIdCategoriaProduto());
            catdto.setDsCategoriaProduto(produtosEntity.getCategoriaProduto().getDsCategoriaProduto());

            produtoDTO.setSubCategoriaProduto(subCategoriaProduto);
            produtoDTO.setCategoriaProduto(catdto);

            List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
            for (TbProdutoImagem produtoImagemEntity : produtosEntity.getImagens()) {
                ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);

                imagemsProdutodto.add(imagemDTO);
            }
            produtoDTO.setImagens(imagemsProdutodto);

            TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(produtosEntity.getCdProduto(), 4L);
            EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
            estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
            estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
            estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
            produtoDTO.setEstoques(estoqueProdutoDTO);

            produtosDTOS.add(produtoDTO);
        }

        return produtosDTOS;
    }

    public List<ProdutoDTO> produtosDestaquesSemana() {
        Long[] produtosPromo = {80L, 81L, 67L, 69L, 80L, 82L, 79L, 55L};
        List<ProdutoDTO> produtosDTOS = new ArrayList<>();

        for (Long cdProduto : produtosPromo) {
            TbProduto produtosEntity = produtoRepository.findByCdProduto(cdProduto);
            ProdutoDTO produtoDTO = produtoBo.parseToDTO(produtosEntity);


            CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
            SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();

            subCategoriaProduto.setIdSubCategoria(produtosEntity.getSubCategoriaProduto().getIdSubCategoria());
            subCategoriaProduto.setDsSubCategoria(produtosEntity.getSubCategoriaProduto().getDsSubCategoria());
            catdto.setIdCategoriaProduto(produtosEntity.getCategoriaProduto().getIdCategoriaProduto());
            catdto.setDsCategoriaProduto(produtosEntity.getCategoriaProduto().getDsCategoriaProduto());

            produtoDTO.setSubCategoriaProduto(subCategoriaProduto);
            produtoDTO.setCategoriaProduto(catdto);

            List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
            for (TbProdutoImagem produtoImagemEntity : produtosEntity.getImagens()) {
                ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);

                imagemsProdutodto.add(imagemDTO);
            }
            produtoDTO.setImagens(imagemsProdutodto);

            TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(produtosEntity.getCdProduto(), 4L);
            EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
            estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
            estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
            estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
            produtoDTO.setEstoques(estoqueProdutoDTO);

            produtosDTOS.add(produtoDTO);
        }

        return produtosDTOS;
    }

    public List<ProdutoDTO> produtosPopulares() {
        Long[] produtosPromo = {77L, 80L, 89L, 51L, 48L, 49L, 50L, 71L};
        List<ProdutoDTO> produtosDTOS = new ArrayList<>();

        for (Long cdProduto : produtosPromo) {
            TbProduto produtosEntity = produtoRepository.findByCdProduto(cdProduto);
            ProdutoDTO produtoDTO = produtoBo.parseToDTO(produtosEntity);


            CategoriaProdutoDTO catdto = new CategoriaProdutoDTO();
            SubCategoriaProdutoDTO subCategoriaProduto = new SubCategoriaProdutoDTO();

            subCategoriaProduto.setIdSubCategoria(produtosEntity.getSubCategoriaProduto().getIdSubCategoria());
            subCategoriaProduto.setDsSubCategoria(produtosEntity.getSubCategoriaProduto().getDsSubCategoria());
            catdto.setIdCategoriaProduto(produtosEntity.getCategoriaProduto().getIdCategoriaProduto());
            catdto.setDsCategoriaProduto(produtosEntity.getCategoriaProduto().getDsCategoriaProduto());

            produtoDTO.setSubCategoriaProduto(subCategoriaProduto);
            produtoDTO.setCategoriaProduto(catdto);

            List<ProdutoImagemDTO> imagemsProdutodto = new ArrayList<>();
            for (TbProdutoImagem produtoImagemEntity : produtosEntity.getImagens()) {
                ProdutoImagemDTO imagemDTO = produtoImagemBo.parseToDTO(produtoImagemEntity);

                imagemsProdutodto.add(imagemDTO);
            }
            produtoDTO.setImagens(imagemsProdutodto);

            TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(produtosEntity.getCdProduto(), 4L);
            EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
            estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
            estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
            estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
            produtoDTO.setEstoques(estoqueProdutoDTO);

            produtosDTOS.add(produtoDTO);
        }

        return produtosDTOS;
    }


    public List<ProdutoDTO2> produtosDestaquesSemana2() {
        Long[] produtosPromo = {80L, 81L, 67L, 69L, 80L, 82L, 79L, 55L};
        List<ProdutoDTO2> produtosDTOS = new ArrayList<>();

        for (Long cdProduto : produtosPromo) {
            TbProduto produtoEntity = produtoRepository.findByCdProduto(cdProduto);
            ProdutoDTO2 produtoDTO = new ProdutoDTO2();

            produtoDTO.setCdProduto(produtoEntity.getCdProduto());
            produtoDTO.setIdStatusProduto(produtoEntity.getIdStatusProduto());
            produtoDTO.setNomeFantasia(produtoEntity.getNomeFantasia());
            produtoDTO.setNomeFabricante(produtoEntity.getNomeFabricante());
            produtoDTO.setDsProduto(produtoEntity.getDsProduto());
            produtoDTO.setValorUnidade(produtoEntity.getValorUnidade());

            TbProdutoImagem produtoImagemEntity = produtoEntity.getImagens().get(0);
            produtoDTO.setDsUrl(produtoImagemEntity.getDsUrl());



            TbProdutoFilialEstoque produtoEstoqueEntity = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(produtoEntity.getCdProduto(), 4L);
            EstoqueProdutoDTO estoqueProdutoDTO = new EstoqueProdutoDTO();
            estoqueProdutoDTO.setCdFilial(produtoEstoqueEntity.getCdFilial());
            estoqueProdutoDTO.setQtEstoque(produtoEstoqueEntity.getQtEstoque());
            estoqueProdutoDTO.setQtEmpenho(produtoEstoqueEntity.getQtEmpenho());
            produtoDTO.setEstoques(estoqueProdutoDTO);

            produtosDTOS.add(produtoDTO);
        }

        return produtosDTOS;
    }


}
