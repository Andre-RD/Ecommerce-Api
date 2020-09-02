package br.com.rdevs.ecommerce.cadastro.repository;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbEndereco;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEnderecoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoClienteRepository extends JpaRepository<TbEnderecoCliente,Long> {

    List<TbEnderecoCliente> findByIdCliente (Long idCliente);
    List<TbEnderecoCliente> findByIdEndereco(Long idEndereco);

}
