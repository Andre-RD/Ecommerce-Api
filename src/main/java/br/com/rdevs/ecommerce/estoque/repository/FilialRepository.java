package br.com.rdevs.ecommerce.estoque.repository;


import br.com.rdevs.ecommerce.estoque.model.entity.TbFilial;
import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilialRepository extends JpaRepository<TbFilial, Long> {

    List<TbFilial> findByCdFilial (Long cdFilial);


//

    List<TbFilial> findByCdFilialAndEstoqueProdutoFilialCdProduto (Long cdFilial, Long cdProduto);

}
