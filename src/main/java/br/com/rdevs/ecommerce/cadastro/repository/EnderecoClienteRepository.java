package br.com.rdevs.ecommerce.cadastro.repository;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbEndereco;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEnderecoCliente;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEnderecoClientePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoClienteRepository extends JpaRepository<TbEnderecoCliente, TbEnderecoClientePK> {

    List<TbEnderecoCliente> findByIdCliente (Long idCliente);
    TbEnderecoCliente findByIdClienteAndIdEndereco (Long idCliente,Long idEndereco);
    List<TbEnderecoCliente> findByIdEndereco (Long idEndereco);

}
