package br.com.rdevs.ecommerce.produto.repository;

import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProdutoPageRepository extends JpaRepository<TbProduto, BigInteger> {
    Page<TbProduto> findBySubCategoriaProdutoIdSubCategoria(BigInteger idSubCategoria, Pageable pageable);
    Page<TbProduto> findByCategoriaProdutoIdCategoriaProduto(BigInteger idCategoriaProduto, Pageable pageable);


}
