package br.com.rdevs.ecommerce.produto.repository;

import br.com.rdevs.ecommerce.produto.model.entity.TbTcCupomItem;
import br.com.rdevs.ecommerce.produto.model.entity.TbTcCupomItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomItemRepository extends JpaRepository<TbTcCupomItem, Long> {

    TbTcCupomItem findByTcCupomClienteIdClienteAndProdutoCpCdProduto(Long idCliente, Long cdProduto);

}
