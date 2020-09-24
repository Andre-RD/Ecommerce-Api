package br.com.rdevs.ecommerce.cadastro.repository;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface Cadastro2Repository extends JpaRepository<TbCliente, BigInteger> {

    TbCliente findByNrCpf(String nrCpf);

    TbCliente findByIdCliente(BigInteger idCliente);

    //TbCliente findByDsEmail(String dsEmail);

    TbCliente findByDsEmail(String dsEmail);


}
