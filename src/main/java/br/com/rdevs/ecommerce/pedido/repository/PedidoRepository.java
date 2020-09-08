package br.com.rdevs.ecommerce.pedido.repository;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.pedido.model.entity.TbPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<TbPedido, Long> {

     List<TbPedido> findByClienteIdCliente(Long idCliente);

     TbPedido findByIdPedido(Long idPedido);

}
