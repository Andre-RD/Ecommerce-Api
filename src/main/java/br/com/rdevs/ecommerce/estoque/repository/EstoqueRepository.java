package br.com.rdevs.ecommerce.estoque.repository;



import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<TbProdutoFilialEstoque, Long> {
  List<TbProdutoFilialEstoque> findByProdutoFilialCdProduto(Long cdProduto);

  TbProdutoFilialEstoque findByCdFilial(Long cdFilial);

  TbProdutoFilialEstoque findByProdutoFilialCdProdutoAndCdFilial(Long cdProduto,Long cdFilial);

}
