package br.com.rdevs.ecommerce.documentoFiscal.service;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEndereco;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.cadastro.repository.EnderecoRepository;
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



    public List<TbDocumentoFiscal> listaDocumentos(){
        return documentoFiscalRepository.findAll();
    }

    public List<TbDocumentoFiscal> listarPorIdCliente(Long idCliente){
        return documentoFiscalRepository.findByTbClienteIdCliente(idCliente);
    }

    public TbPagamentoPedido inserirItem(@RequestBody PostDocumentoFiscalDTO dfDTO){
        TbDocumentoFiscal dfEntity = new TbDocumentoFiscal();
        TbPedido pedidoEntity = new TbPedido();
        TbPagamentoPedido pagamentoPedidoEntity = new TbPagamentoPedido();

        TbEndereco endereco = enderecoRepository.getOne(dfDTO.getIdEndereco());

        TbTipoPagamento tipoPagamento = tipoPagamentoRepository.getOne(dfDTO.getIdFormaPagamento());

        Calendar data = Calendar.getInstance();
        Random random = new Random();
        Long numeroAleatorio =Math.abs(random.nextLong()*100000);

        TbCliente tbCliente = cadastroRepository.getOne(dfDTO.getIdCliente());
        dfEntity.setTbCliente(tbCliente);
        pedidoEntity.setCliente(tbCliente);



        dfEntity.setCdOperacao(1l);

        dfEntity.setNrChaveAcesso(numeroAleatorio);
        dfEntity.setNrNF(2001L);
        dfEntity.setDtEmissao(data.getTime());
        pedidoEntity.setDtCompra(data.getTime());


        List<TbDocumentoItem> itensNF = new ArrayList<>();
        List<TbPedidoItem> itensPedido = new ArrayList<>();


        double valor = 0d;
        Long contador = 0L;
        double calculoIcms = 0d;
        for (PostDocumentoFiscalItemDTO itemDTO: dfDTO.getItensDTOPost()){
            TbDocumentoItem itemNF = new TbDocumentoItem();
            TbPedidoItem itemPedido = new TbPedidoItem();

            TbProduto produto = produtoRepository.getOne(itemDTO.getCdProduto());
            itemNF.setProduto(produto);
            itemPedido.setProduto(produto);

            TbProdutoFilialEstoque estoque = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(produto.getCdProduto(),4L);
            Long quantidadeComprada = 1L;

            Long estoqueNovo = estoque.getQtEstoque() - quantidadeComprada;
            estoque.setQtEstoque(estoqueNovo);
            estoqueRepository.save(estoque);

            itemNF.setDocumentoFiscal(dfEntity);
            itemPedido.setPedido(pedidoEntity);

            contador++;
            itemNF.setNrItemDocumento(contador);
            itemPedido.setNrItemPedido(contador);

            itemNF.setQtItem(1L);

            itemNF.setVlItem(produto.getValorUnidade());
            itemPedido.setVlPedidoItem(produto.getValorUnidade());

            valor += produto.getValorUnidade().doubleValue();

            itemNF.setPcIcms(BigDecimal.valueOf(0.17));

            calculoIcms = produto.getValorUnidade().doubleValue()*0.17;
            itemNF.setVlIcms(BigDecimal.valueOf(calculoIcms));

            itensNF.add(itemNF);
            itensPedido.add(itemPedido);
        }
        dfEntity.setItensNf(itensNF);
        pedidoEntity.setItens(itensPedido);

        dfEntity.setVlDocumentoFiscal(BigDecimal.valueOf(valor));

        pedidoEntity.setVlTotalPedido(dfEntity.getVlDocumentoFiscal());
        pedidoEntity.setQtItensPedido(contador.intValue());

        TbStatusPedido tbStatusPedido = new TbStatusPedido();
        tbStatusPedido.setCdStatusPedido(2L);
        pedidoEntity.setStatusPedido(tbStatusPedido);
        pedidoRepository.save(pedidoEntity);
        documentoFiscalRepository.save(dfEntity);

        pagamentoPedidoEntity.setTbEndereco(endereco);
        pagamentoPedidoEntity.setNmNomeTitular(dfDTO.getNmNomeTitular());
        pagamentoPedidoEntity.setNrNumeroCartao(dfDTO.getNrNumeroCartao());
        pagamentoPedidoEntity.setTbCliente(tbCliente);
        pagamentoPedidoEntity.setTbPedidoEntity(pedidoEntity);
        pagamentoPedidoEntity.setIdPagamentoPedido(pedidoEntity.getIdPedido());
        pagamentoPedidoEntity.setDocumentoFiscal(dfEntity);
        pagamentoPedidoEntity.setTipoPagamento(tipoPagamento);


        return pagamentoPedidoRepository.save(pagamentoPedidoEntity);
    }



}
