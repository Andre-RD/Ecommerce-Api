package br.com.rdevs.ecommerce.produto.repository;

import br.com.rdevs.ecommerce.produto.model.entity.TbTcCupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomRepository extends JpaRepository<TbTcCupom , Long> {
    TbTcCupom findByClienteIdCliente (Long idCliente);
}
