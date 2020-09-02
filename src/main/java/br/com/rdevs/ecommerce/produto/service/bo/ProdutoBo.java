package br.com.rdevs.ecommerce.produto.service.bo;

import br.com.rdevs.ecommerce.cadastro.model.dto.ClienteDTO;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.produto.model.dto.ProdutoDTO;
import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdutoBo {

    public ProdutoDTO parseToDTO(TbProduto produtoEntity){
        ProdutoDTO produtoDTO= new ProdutoDTO();

        produtoDTO.setCdProduto(produtoEntity.getCdProduto());
        produtoDTO.setIdStatusProduto(produtoEntity.getIdStatusProduto());
        produtoDTO.setNomeFantasia(produtoEntity.getNomeFantasia());
        produtoDTO.setNomeFabricante(produtoEntity.getNomeFabricante());
        produtoDTO.setDsProduto(produtoEntity.getDsProduto());
        produtoDTO.setValorUnidade(produtoEntity.getValorUnidade());

        return produtoDTO;
    }

    public TbProduto parseToEntity(ProdutoDTO produtoDTO,TbProduto produtoEntity){

        if(produtoEntity == null)
            produtoEntity = new TbProduto();
        if(produtoDTO == null)
            return produtoEntity;

        produtoEntity = new TbProduto();

        produtoEntity.setCdProduto(produtoDTO.getCdProduto());
        produtoEntity.setIdStatusProduto(produtoDTO.getIdStatusProduto());
        produtoEntity.setNomeFantasia(produtoDTO.getNomeFantasia());
        produtoEntity.setNomeFabricante(produtoDTO.getNomeFabricante());
        produtoEntity.setDsProduto(produtoDTO.getDsProduto());
        produtoEntity.setValorUnidade(produtoDTO.getValorUnidade());

        return produtoEntity;
    }
}
