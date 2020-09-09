package br.com.rdevs.ecommerce.documentoFiscal.service;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEndereco;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.cadastro.repository.EnderecoRepository;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.DocumentoFiscalDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.DocumentoFiscalItemDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.PostDocumentoFiscalDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.PostDocumentoFiscalItemDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoFiscal;
import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoItem;
import br.com.rdevs.ecommerce.documentoFiscal.repository.DocumentoFiscalRepository;
import br.com.rdevs.ecommerce.estoque.model.entity.TbProdutoFilialEstoque;
import br.com.rdevs.ecommerce.estoque.repository.EstoqueRepository;
import br.com.rdevs.ecommerce.pagamentopedido.model.entity.TbPagamentoPedido;
import br.com.rdevs.ecommerce.pagamentopedido.model.entity.TbTipoPagamento;
import br.com.rdevs.ecommerce.pagamentopedido.repository.PagamentoPedidoRepository;
import br.com.rdevs.ecommerce.pagamentopedido.repository.TipoPagamentoRepository;
import br.com.rdevs.ecommerce.pedido.model.dto.PedidoDTO;
import br.com.rdevs.ecommerce.pedido.model.dto.PedidoItemDTO;
import br.com.rdevs.ecommerce.pedido.model.entity.TbPedido;
import br.com.rdevs.ecommerce.pedido.model.entity.TbPedidoItem;
import br.com.rdevs.ecommerce.pedido.model.entity.TbStatusPedido;
import br.com.rdevs.ecommerce.pedido.repository.PedidoRepository;
import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import br.com.rdevs.ecommerce.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.math.BigDecimal;
import java.util.*;

@Service
public class DocumentoFiscalService {
    @Autowired
    DocumentoFiscalRepository documentoFiscalRepository;

    @Autowired
    PagamentoPedidoRepository pagamentoPedidoRepository;

    @Autowired
    CadastroRepository cadastroRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    TipoPagamentoRepository tipoPagamentoRepository;

    @Autowired
    EstoqueRepository estoqueRepository;


    public Object listaDocumentosPorID(Long idDocumentoFiscal){
        DocumentoFiscalDTO documentoFiscalDTO = new DocumentoFiscalDTO();
        TbDocumentoFiscal documentoFiscalEntity =  documentoFiscalRepository.findByIdDocumentoFiscal(idDocumentoFiscal);
        documentoFiscalDTO.setNrCpf(documentoFiscalEntity.getTbCliente().getNrCpf());
        documentoFiscalDTO.setNrChaveAcesso(documentoFiscalEntity.getNrChaveAcesso());
        documentoFiscalDTO.setNrNF(documentoFiscalEntity.getNrNF());
        documentoFiscalDTO.setDtEmissao(documentoFiscalEntity.getDtEmissao());

        List<DocumentoFiscalItemDTO> itensDTO = new ArrayList<>();
        for (TbDocumentoItem itensEntity: documentoFiscalEntity.getItensNf()){
            DocumentoFiscalItemDTO itemDTO = new DocumentoFiscalItemDTO();

            itemDTO.setNrItemDocumento(itensEntity.getNrItemDocumento());
            itemDTO.setCdProduto(itensEntity.getProduto().getCdProduto());
            itemDTO.setQtItem(itensEntity.getQtItem());
            itemDTO.setNmProduto(itensEntity.getProduto().getNomeFantasia());
            itemDTO.setVlItemUnitario(itensEntity.getVlItem());
            Double valorRefaturado = itensEntity.getVlItem().doubleValue()*itensEntity.getQtItem();
            itemDTO.setVlTotalItem(BigDecimal.valueOf(valorRefaturado));
            itemDTO.setVlIcms(itensEntity.getVlIcms());
            itemDTO.setPcIcms(itensEntity.getPcIcms());

            itensDTO.add(itemDTO);
        }
        documentoFiscalDTO.setItensDocumento(itensDTO);


        return documentoFiscalDTO;
    }

    public List<PedidoDTO> listarPorIdCliente(Long idCliente){
        List<PedidoDTO> pedidosDTO = new ArrayList<>();
        List<TbDocumentoFiscal> documentoFiscalList = documentoFiscalRepository.findByTbClienteIdCliente(idCliente);

        for(TbDocumentoFiscal documentoFiscal: documentoFiscalList){
            PedidoDTO pedidoDTO = new PedidoDTO();

            pedidoDTO.setIdPedido(documentoFiscal.getIdDocumentoFiscal());
            pedidoDTO.setDtCompra(documentoFiscal.getDtEmissao());
            pedidoDTO.setVlFrete(BigDecimal.valueOf(0L));


            pedidoDTO.setIdCliente(idCliente);
            pedidoDTO.setDsStatusPedido("Vendido");

            List<PedidoItemDTO> pedidoItensDto = new ArrayList<>();

            Long quatidadeItens = 0L;
            for (TbDocumentoItem itemNF: documentoFiscal.getItensNf()){
                PedidoItemDTO pedidoItemDTO = new PedidoItemDTO();

                pedidoItemDTO.setIdPedido(itemNF.getDocumentoFiscal().getIdDocumentoFiscal());
                pedidoItemDTO.setDsProduto(itemNF.getProduto().getNomeFantasia());
                pedidoItemDTO.setNrItemPedido(itemNF.getNrItemDocumento());
                pedidoItemDTO.setCdProduto(itemNF.getProduto().getCdProduto());
                pedidoItemDTO.setVlPedidoItem(itemNF.getVlItem());

                if (itemNF.getQtItem()==null){
                    pedidoItemDTO.setQtProduto(1L);
                }else {
                    pedidoItemDTO.setQtProduto(itemNF.getQtItem());
                }


                quatidadeItens += pedidoItemDTO.getQtProduto();

                pedidoItensDto.add(pedidoItemDTO);
            }
            pedidoDTO.setItems(pedidoItensDto);

            if (documentoFiscal.getCdOperacao()==9){

                pedidoDTO.setTipoVenda("Comprado no Ecommerce");
            }else{
                pedidoDTO.setTipoVenda("Comprado em Loja");
            }


            pedidoDTO.setQtItensPedido(Math.toIntExact(quatidadeItens));
            pedidoDTO.setVlTotalPedido(documentoFiscal.getVlDocumentoFiscal());


            pedidosDTO.add(pedidoDTO);
        }


        return pedidosDTO;
    }


    public Object inserirItem(@RequestBody PostDocumentoFiscalDTO dfDTO){

        TbDocumentoFiscal dfEntity = new TbDocumentoFiscal();
        TbPedido pedidoEntity = new TbPedido();
        TbPagamentoPedido pagamentoPedidoEntity = new TbPagamentoPedido();
        DocumentoFiscalDTO documentoFiscalDTO = new DocumentoFiscalDTO();

        TbEndereco endereco = enderecoRepository.getOne(dfDTO.getIdEndereco());
        TbTipoPagamento tipoPagamento = tipoPagamentoRepository.getOne(dfDTO.getIdFormaPagamento());

        Calendar data = Calendar.getInstance();
        Random random = new Random();
        Long numeroAleatorio =Math.abs(random.nextLong()*100);

        TbCliente tbCliente = cadastroRepository.getOne(dfDTO.getIdCliente());
        documentoFiscalDTO.setNrCpf(tbCliente.getNrCpf());

        dfEntity.setTbCliente(tbCliente);
        pedidoEntity.setCliente(tbCliente);


        dfEntity.setCdOperacao(9l);
        dfEntity.setFlNf(1l);
        dfEntity.setCdFilial(4l);
        dfEntity.setNrChaveAcesso(numeroAleatorio);
        documentoFiscalDTO.setNrChaveAcesso(numeroAleatorio);

        dfEntity.setNrNF(2001L);
        documentoFiscalDTO.setNrNF(2001L);

        dfEntity.setDtEmissao(data.getTime());
        documentoFiscalDTO.setDtEmissao(data.getTime());
        pedidoEntity.setDtCompra(data.getTime());


        List<TbDocumentoItem> itensNF = new ArrayList<>();
        List<TbPedidoItem> itensPedido = new ArrayList<>();
        List<DocumentoFiscalItemDTO> itensNfDTOS = new ArrayList<>();

        double valor = 0d;
        Long contador = 0L;
        Long contadorItens = 0L;
        double calculoIcms = 0d;
        for (PostDocumentoFiscalItemDTO itemDTO: dfDTO.getItensDTOPost()){
            TbDocumentoItem itemNF = new TbDocumentoItem();
            TbPedidoItem itemPedido = new TbPedidoItem();
            DocumentoFiscalItemDTO itemNfDTO = new DocumentoFiscalItemDTO();

            TbProduto produto = produtoRepository.getOne(itemDTO.getCdProduto());
            itemNF.setProduto(produto);
            itemPedido.setProduto(produto);
            itemNfDTO.setCdProduto(itemDTO.getCdProduto());


            itemNF.setQtItem(itemDTO.getQtProduto());
            itemPedido.setQtProduto(itemDTO.getQtProduto());
            itemNfDTO.setQtItem(itemDTO.getQtProduto());
            itemNfDTO.setNmProduto(produto.getNomeFantasia());



            TbProdutoFilialEstoque estoque = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(produto.getCdProduto(),4L);


            //TODO validar a quantidade de itens na compra
            Long quantidadeComprada = itemDTO.getQtProduto();
            contadorItens += quantidadeComprada;


            Long estoqueNovo = estoque.getQtEstoque() - quantidadeComprada;
            estoque.setQtEstoque(estoqueNovo);
            estoqueRepository.save(estoque);

            itemNF.setDocumentoFiscal(dfEntity);
            itemPedido.setPedido(pedidoEntity);

            contador++;
            itemNF.setNrItemDocumento(contador);
            itemPedido.setNrItemPedido(contador);
            itemNfDTO.setNrItemDocumento(contador);

            Double valorRefaturado = produto.getValorUnidade().doubleValue() * quantidadeComprada;
            itemNfDTO.setVlTotalItem(BigDecimal.valueOf(valorRefaturado));

            itemNF.setVlItem(produto.getValorUnidade());
            itemNfDTO.setVlItemUnitario(produto.getValorUnidade());

            itemPedido.setVlPedidoItem(produto.getValorUnidade());

            valor += valorRefaturado;

            itemNF.setPcIcms(BigDecimal.valueOf(0.17));
            itemNfDTO.setPcIcms(BigDecimal.valueOf(0.17));

            calculoIcms = valorRefaturado*0.17;
            itemNF.setVlIcms(BigDecimal.valueOf(calculoIcms));
            itemNfDTO.setVlIcms(BigDecimal.valueOf(calculoIcms));

            itensNF.add(itemNF);
            itensPedido.add(itemPedido);
            itensNfDTOS.add(itemNfDTO);

        }

        dfEntity.setItensNf(itensNF);
        pedidoEntity.setItens(itensPedido);
        documentoFiscalDTO.setItensDocumento(itensNfDTOS);

        dfEntity.setVlDocumentoFiscal(BigDecimal.valueOf(valor));

        pedidoEntity.setVlTotalPedido(dfEntity.getVlDocumentoFiscal());

        pedidoEntity.setQtItensPedido(contadorItens.intValue());

        TbStatusPedido tbStatusPedido = new TbStatusPedido();
        tbStatusPedido.setCdStatusPedido(2L);
        pedidoEntity.setStatusPedido(tbStatusPedido);
        pedidoRepository.save(pedidoEntity);
        documentoFiscalDTO.setNrPedido(pedidoEntity.getIdPedido());

        documentoFiscalRepository.save(dfEntity);

        pagamentoPedidoEntity.setTbEndereco(endereco);
        pagamentoPedidoEntity.setNmNomeTitular(dfDTO.getNmNomeTitular());
        documentoFiscalDTO.setNmNomeTitular(dfDTO.getNmNomeTitular());
        pagamentoPedidoEntity.setNrNumeroCartao(dfDTO.getNrNumeroCartao());
        documentoFiscalDTO.setNrNumeroCartao(dfDTO.getNrNumeroCartao());
        pagamentoPedidoEntity.setTbCliente(tbCliente);
        pagamentoPedidoEntity.setTbPedidoEntity(pedidoEntity);
        pagamentoPedidoEntity.setIdPagamentoPedido(pedidoEntity.getIdPedido());
        pagamentoPedidoEntity.setDocumentoFiscal(dfEntity);
        pagamentoPedidoEntity.setTipoPagamento(tipoPagamento);
        pagamentoPedidoRepository.save(pagamentoPedidoEntity);



        return documentoFiscalDTO;
    }



}
