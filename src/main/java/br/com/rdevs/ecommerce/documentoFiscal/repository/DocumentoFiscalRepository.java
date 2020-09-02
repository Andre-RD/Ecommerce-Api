package br.com.rdevs.ecommerce.documentoFiscal.repository;

import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoFiscalRepository extends JpaRepository<TbDocumentoFiscal, Long> {
}
