package br.com.rdevs.ecommerce.produto.repository;

import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<TbProduto, Long> {
    List<TbProduto> findByNomeFantasia(String nomeFantasia);
    List<TbProduto> findByNomeFabricante(String nomeFabricante);
    List<TbProduto> findByCategoriaProdutoIdCategoriaProduto(Long idCategoriaProduto);
    List<TbProduto> findByCategoriaProdutoIdCategoriaProdutoAndSubCategoriaProdutoIdSubCategoria(Long idCategoriaProduto, Long idSubCategoria);
    List<TbProduto> findBySubCategoriaProdutoIdSubCategoria(Long idSubCategoria);
    TbProduto findByCdProduto(Long cdProduto);

}
