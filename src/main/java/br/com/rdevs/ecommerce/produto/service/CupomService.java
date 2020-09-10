package br.com.rdevs.ecommerce.produto.service;

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
import br.com.rdevs.ecommerce.produto.service.bo.ProdutoBo;
import br.com.rdevs.ecommerce.produto.service.bo.ProdutoImagemBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CupomService {
    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private CupomItemRepository cupomItemRepository;

    @Autowired
    private ProdutoBo produtoBo;

    @Autowired
    private ProdutoImagemBo produtoImagemBo;

    @Autowired
    private EstoqueRepository estoqueRepository;

    public Object buscarCuponsIdCliente(Long idCliente){
        TbTcCupom tcCupomEntity = cupomRepository.findByClienteIdCliente(idCliente);
        TcCupomDTO tcCupomDTO = new TcCupomDTO();
        tcCupomDTO.setNmCliente(tcCupomEntity.getCliente().getNmCliente());
        tcCupomDTO.setIdCupom(tcCupomEntity.getIdCupom());
        tcCupomDTO.setDtFinalCupom(tcCupomEntity.getDtFinalCupom());

        List<TcCupomItemDTO> tcCupomItensDTO = new ArrayList<>();

        for (TbTcCupomItem itemEntity: tcCupomEntity.getItensCupom()){
            TcCupomItemDTO itemCupomDTO = new TcCupomItemDTO();

            itemCupomDTO.setIdTcCupom(itemEntity.getTcCupom().getIdCupom());
            itemCupomDTO.setIdCupomItem(itemEntity.getIdCupomItem());
            itemCupomDTO.setPcDesconto(itemEntity.getPcDesconto());

            TbProduto prod = itemEntity.getProdutoCp();
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

            itemCupomDTO.setProdutoCp(dto);


            tcCupomItensDTO.add(itemCupomDTO);
        }
        tcCupomDTO.setItensCupom(tcCupomItensDTO);




        return tcCupomDTO;
    }

    public Object buscarPorClienteEProduto(Long idCliente, Long cdProduto){
        return cupomItemRepository.findByTcCupomClienteIdClienteAndProdutoCpCdProduto(idCliente,cdProduto);
    }

}
