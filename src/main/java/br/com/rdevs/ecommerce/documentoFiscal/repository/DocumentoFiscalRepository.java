package br.com.rdevs.ecommerce.documentoFiscal.repository;

import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoFiscalRepository extends JpaRepository<TbDocumentoFiscal, Long> {

    List<TbDocumentoFiscal> findByTbClienteIdCliente(Long idCliente);
    TbDocumentoFiscal findByIdDocumentoFiscal(Long idDocumentoFiscal);

}
