//package br.com.rdevs.ecommerce.documentoFiscal.repository;
//
//
//import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoFiscal;
//import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoItem;
//import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoItemID;
//import lombok.NoArgsConstructor;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.math.BigInteger;
//import java.util.List;
//
//@Repository
//public interface DocumentoFiscalItemRepository extends JpaRepository<TbDocumentoItem, TbDocumentoItemID> {
//
//    List<TbDocumentoItem> findByDocumentoFiscalIdDocumentoFiscal(BigInteger idDocumentoFiscal);
//
//}