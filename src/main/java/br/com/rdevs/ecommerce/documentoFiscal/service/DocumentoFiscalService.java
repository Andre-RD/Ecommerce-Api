package br.com.rdevs.ecommerce.documentoFiscal.service;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCartaoCredito;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.model.entity.TbEndereco;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.cadastro.repository.CartaoRepository;
import br.com.rdevs.ecommerce.cadastro.repository.EnderecoRepository;
import br.com.rdevs.ecommerce.cadastro.service.ClienteService;
import br.com.rdevs.ecommerce.cadastro.service.bo.CartaoCreditoBO;
import br.com.rdevs.ecommerce.documentoFiscal.model.CBD.ConsultaBanco;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.DocumentoFiscalDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.DocumentoFiscalItemDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.PostDocumentoFiscalDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.dto.PostDocumentoFiscalItemDTO;
import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoFiscal;
import br.com.rdevs.ecommerce.documentoFiscal.model.entity.TbDocumentoItem;
//import br.com.rdevs.ecommerce.documentoFiscal.repository.DocumentoFiscalItemRepository;
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
import br.com.rdevs.ecommerce.produto.model.dto.ProdutoDTO;
import br.com.rdevs.ecommerce.produto.model.entity.TbProduto;
import br.com.rdevs.ecommerce.produto.model.entity.TbTcCupom;
import br.com.rdevs.ecommerce.produto.model.entity.TbTcCupomItem;
import br.com.rdevs.ecommerce.produto.repository.CupomItemRepository;
import br.com.rdevs.ecommerce.produto.repository.CupomRepository;
import br.com.rdevs.ecommerce.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    CupomItemRepository cupomItemRepository;

    @Autowired
    ConsultaBanco consultaBanco;

//    @Autowired
//    DocumentoFiscalItemRepository documentoFiscalItemRepository;

    @Autowired
    CupomRepository cupomRepository;

    @Autowired
    TipoPagamentoRepository tipoPagamentoRepository;

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    CartaoRepository cartaoRepository;

    @Autowired
    ClienteService clienteService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    CartaoCreditoBO cartaoCreditoBO;

    @PersistenceContext
    private EntityManager manager;


    public TbDocumentoFiscal listaDocumentosPorID(BigInteger idDocumentoFiscal){
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


        return documentoFiscalEntity;
    }

    public List<PedidoDTO> listarPorIdCliente(BigInteger idCliente){
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
                    pedidoItemDTO.setQtProduto(1);
                }else {
                    pedidoItemDTO.setQtProduto(itemNF.getQtItem());
                }


                quatidadeItens += pedidoItemDTO.getQtProduto();

                pedidoItensDto.add(pedidoItemDTO);
            }
            pedidoDTO.setItems(pedidoItensDto);

            if (documentoFiscal.getCdOperacao()==BigInteger.valueOf(9)){

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

    public DocumentoFiscalDTO inserirItem(@RequestBody PostDocumentoFiscalDTO dfDTO){

        TbDocumentoFiscal dfEntity = new TbDocumentoFiscal();
        TbPedido pedidoEntity = new TbPedido();
        TbPagamentoPedido pagamentoPedidoEntity = new TbPagamentoPedido();
        DocumentoFiscalDTO documentoFiscalDTO = new DocumentoFiscalDTO();

        TbTcCupom tcCupom = cupomRepository.findByClienteIdCliente(dfDTO.getIdCliente());

        if(dfDTO.getNmNomeTitular() == "" || dfDTO.getNrNumeroCartao() == ""){
            dfDTO.setNmNomeTitular(null);
            dfDTO.setNrNumeroCartao(null);
        }

        TbEndereco endereco = enderecoRepository.getOne(dfDTO.getIdEndereco());
        documentoFiscalDTO.setEndereco(endereco);
        TbTipoPagamento tipoPagamento = tipoPagamentoRepository.getOne(dfDTO.getIdFormaPagamento());
        documentoFiscalDTO.setFormaPagamento(tipoPagamento.getDsTipoPagamento());
        documentoFiscalDTO.setIdFormaPagamento(tipoPagamento.getIdTipoPagamento());

        Date data = new Date();
        Random random = new Random();
        Long numeroAleatorio =Math.abs(random.nextLong()*100);

        TbCliente tbCliente = cadastroRepository.getOne(dfDTO.getIdCliente());
        documentoFiscalDTO.setNrCpf(tbCliente.getNrCpf());

        documentoFiscalDTO.setDsEmail(tbCliente.getDsEmail());
        dfEntity.setTbCliente(tbCliente);
        pedidoEntity.setCliente(tbCliente);


        dfEntity.setCdOperacao(BigInteger.valueOf(9l));
        dfEntity.setFlNf(1l);
        dfEntity.setCdFilial(BigInteger.valueOf(4l));
        dfEntity.setNrChaveAcesso(numeroAleatorio);
        documentoFiscalDTO.setNrChaveAcesso(numeroAleatorio);

        dfEntity.setNrNF(2001L);
        documentoFiscalDTO.setNrNF(2001L);

        dfEntity.setDtEmissao(data);
        documentoFiscalDTO.setDtEmissao(data);
        pedidoEntity.setDtCompra(data);


        List<TbDocumentoItem> itensNF = new ArrayList<>();
        List<TbPedidoItem> itensPedido = new ArrayList<>();
        List<DocumentoFiscalItemDTO> itensNfDTOS = new ArrayList<>();

        double valor = 0d;
        Integer contador = 0;
        Long contadorItens = 0L;
        double calculoIcms = 0d;


        for (PostDocumentoFiscalItemDTO itemDTO: dfDTO.getItensDTOPost()){
            Double pcDensconto = 1D;
            Double valorConvertido = null;
            TbDocumentoItem itemNF = new TbDocumentoItem();
            TbPedidoItem itemPedido = new TbPedidoItem();
            DocumentoFiscalItemDTO itemNfDTO = new DocumentoFiscalItemDTO();

            TbProduto produto = produtoRepository.getOne(itemDTO.getCdProduto());

            TbTcCupomItem tcCupomItem = cupomItemRepository.findByTcCupomClienteIdClienteAndProdutoCpCdProduto(dfDTO.getIdCliente(),itemDTO.getCdProduto());
            itemNF.setProduto(produto);
            itemPedido.setProduto(produto);
            itemNfDTO.setCdProduto(itemDTO.getCdProduto());


            itemNF.setQtItem(itemDTO.getQtProduto());
            itemPedido.setQtProduto(itemDTO.getQtProduto());
            itemNfDTO.setQtItem(itemDTO.getQtProduto());
            itemNfDTO.setNmProduto(produto.getNomeFantasia());



            TbProdutoFilialEstoque estoque = estoqueRepository.findByProdutoFilialCdProdutoAndCdFilial(produto.getCdProduto(), BigInteger.valueOf(4L));

            Integer quantidadeComprada = itemDTO.getQtProduto();
            contadorItens += quantidadeComprada;


            Integer estoqueNovo = estoque.getQtEstoque() - quantidadeComprada;
            estoque.setQtEstoque(estoqueNovo);
            estoqueRepository.save(estoque);

            itemNF.setDocumentoFiscal(dfEntity);
            itemPedido.setPedido(pedidoEntity);

            contador++;
            itemNF.setNrItemDocumento(BigInteger.valueOf(contador));
            itemPedido.setNrItemPedido(BigInteger.valueOf(contador));
            itemNfDTO.setNrItemDocumento(BigInteger.valueOf(contador));



            if (tcCupom==null||tcCupomItem==null) {
                pcDensconto -= tbCliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
            }else if(tcCupomItem.getPcDesconto().doubleValue()<tbCliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue()){
                pcDensconto -= tbCliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
            }else if(tcCupomItem.getPcDesconto().doubleValue()>tbCliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue()){
                pcDensconto -= tcCupomItem.getPcDesconto().doubleValue();
            }else {
                pcDensconto -= tbCliente.getCategoriaCliente().getPcDescontoEcommerce().doubleValue();
            }

            valorConvertido = produto.getValorUnidade().doubleValue()*pcDensconto;

            Double valorRefaturado = valorConvertido * quantidadeComprada;

            itemNfDTO.setVlTotalItem(BigDecimal.valueOf(valorRefaturado).setScale(2, RoundingMode.HALF_EVEN));
            itemNF.setVlItem(BigDecimal.valueOf(valorConvertido).setScale(2, RoundingMode.HALF_EVEN));
            itemNfDTO.setVlItemUnitario(BigDecimal.valueOf(valorConvertido).setScale(2, RoundingMode.HALF_EVEN));
            itemPedido.setVlPedidoItem(BigDecimal.valueOf(valorConvertido).setScale(2, RoundingMode.HALF_EVEN));



            valor += valorRefaturado;

            itemNF.setPcIcms(BigDecimal.valueOf(0.17));
            itemNfDTO.setPcIcms(BigDecimal.valueOf(0.17));

            calculoIcms = valorRefaturado*0.17;
            itemNF.setVlIcms(BigDecimal.valueOf(calculoIcms));
            itemNfDTO.setVlIcms(BigDecimal.valueOf(calculoIcms).setScale(2, RoundingMode.HALF_EVEN));

            itensNF.add(itemNF);
            itensPedido.add(itemPedido);
            itensNfDTOS.add(itemNfDTO);

        }

        dfEntity.setItensNf(itensNF);
        pedidoEntity.setItens(itensPedido);
        documentoFiscalDTO.setItensDocumento(itensNfDTOS);

        dfEntity.setVlDocumentoFiscal(BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_EVEN));
        documentoFiscalDTO.setValorTotalNota(BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_EVEN));
        documentoFiscalDTO.setQtItens(contadorItens);

        pedidoEntity.setVlTotalPedido(dfEntity.getVlDocumentoFiscal());

        pedidoEntity.setQtItensPedido(contadorItens.intValue());

        TbStatusPedido tbStatusPedido = new TbStatusPedido();
        tbStatusPedido.setCdStatusPedido(BigInteger.valueOf(2L));
        pedidoEntity.setStatusPedido(tbStatusPedido);



        documentoFiscalRepository.save(dfEntity);

        pedidoRepository.save(pedidoEntity);

        documentoFiscalDTO.setNrPedido(pedidoEntity.getIdPedido());

        documentoFiscalDTO.setIdNF(dfEntity.getIdDocumentoFiscal());

        pagamentoPedidoEntity.setTbEndereco(endereco);
        if (dfDTO.getNrNumeroCartao()!=null) {


            String cartaoConvertido = dfDTO.getNrNumeroCartao();
            List<TbCartaoCredito> cartaoCredito = cartaoRepository.findByClienteCartaoIdCliente(dfDTO.getIdCliente());

            if (cartaoConvertido.contains("*")) {
                for (TbCartaoCredito i : cartaoCredito) {
                    byte[] decodedBytes = Base64.getDecoder().decode(i.getNrNumeroCartao());
                    String decodedString = new String(decodedBytes);

                    cartaoConvertido = decodedString;
                    String ultimosDigitos = "****.****.****." + decodedString.substring(decodedString.length() - 4);
                    if (dfDTO.getIdFormaPagamento()==BigInteger.valueOf(1)) {
                        pagamentoPedidoEntity.setNmNomeTitular(dfDTO.getNmNomeTitular());
                        documentoFiscalDTO.setNmNomeTitular(dfDTO.getNmNomeTitular());
                        pagamentoPedidoEntity.setNrNumeroCartao(Base64.getEncoder().encodeToString(cartaoConvertido.getBytes()));
                        documentoFiscalDTO.setNrNumeroCartao(ultimosDigitos);
                    }
                }
            } else {
                String ultimosDigitos = "****.****.****." + cartaoConvertido.substring(cartaoConvertido.length() - 4);
                if (dfDTO.getIdFormaPagamento()==BigInteger.valueOf(1)) {
                    pagamentoPedidoEntity.setNmNomeTitular(dfDTO.getNmNomeTitular());
                    documentoFiscalDTO.setNmNomeTitular(dfDTO.getNmNomeTitular());
                    pagamentoPedidoEntity.setNrNumeroCartao(Base64.getEncoder().encodeToString(cartaoConvertido.getBytes()));
                    documentoFiscalDTO.setNrNumeroCartao(ultimosDigitos);
                }
            }
        }


        pagamentoPedidoEntity.setTbCliente(tbCliente);
        pagamentoPedidoEntity.setTbPedidoEntity(pedidoEntity);
        pagamentoPedidoEntity.setIdPagamentoPedido(pedidoEntity.getIdPedido());
        pagamentoPedidoEntity.setDocumentoFiscal(dfEntity);
        pagamentoPedidoEntity.setTipoPagamento(tipoPagamento);
        pagamentoPedidoRepository.save(pagamentoPedidoEntity);


        return documentoFiscalDTO;
    }


    public void enviarNota (@RequestBody DocumentoFiscalDTO documentoFiscalDTO){
        String emailDestino = documentoFiscalDTO.getDsEmail();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailDestino);
        email.setSubject("Raia Drogasil SA Ecommerce");

        String corpoEmail = "\n          Produtos:\n";
        String opcaoPagamento = "";
        String enderecoEntrega= "";


        if(documentoFiscalDTO.getIdFormaPagamento()==BigInteger.valueOf(1)){
            opcaoPagamento+="\n          Opção de Pagamento \n" +
                    "Forma de Pagamento: " +documentoFiscalDTO.getFormaPagamento()+ " \n"+
                    "Nome Do Titular: " +documentoFiscalDTO.getNmNomeTitular()+ " \n"+
                    "Numero do Cartão: "+documentoFiscalDTO.getNrNumeroCartao()+ " \n";
        }else if (documentoFiscalDTO.getIdFormaPagamento()==BigInteger.valueOf(2)){
            opcaoPagamento+="\n Opção de Pagamento \n" +
                    "Forma de Pagamento: " +documentoFiscalDTO.getFormaPagamento()+ " \n";
        }else {
            opcaoPagamento+="\n Opção de Pagamento \n" +
                    "Forma de Pagamento: " +documentoFiscalDTO.getFormaPagamento()+ " \n";
        }

        enderecoEntrega+="\n          Endereço De Entrega: \n" +
                "Logradouro: " +documentoFiscalDTO.getEndereco().getDsEndereco()+ " \n" +
                "Numero:     " +documentoFiscalDTO.getEndereco().getNrEndereco()+ " \n" +
                "CEP:        " +documentoFiscalDTO.getEndereco().getNrCep()+ " \n" +
                "Bairro:     " +documentoFiscalDTO.getEndereco().getDsBairro() + " \n"+
                "Cidade:     " +documentoFiscalDTO.getEndereco().getDsCidade()+" - "+documentoFiscalDTO.getEndereco().getSgEstado()+" \n";

        Double valorTotal = 0d;
        for (DocumentoFiscalItemDTO itensVendidoDTO : documentoFiscalDTO.getItensDocumento() ) {
            valorTotal = 0d;
            valorTotal = itensVendidoDTO.getQtItem()*itensVendidoDTO.getVlItemUnitario().doubleValue();
            corpoEmail +=
                            " "+ itensVendidoDTO.getNrItemDocumento() + "º " +
                            " "+ itensVendidoDTO.getQtItem()+ " X " +                                                            // Quantidade do produto
                            itensVendidoDTO.getNmProduto() + " \n"+                                                  // Descrição do Produto
                            " Valor Unitario : R$ " + itensVendidoDTO.getVlItemUnitario()+ "\n"+
                            " Valor Total :    R$ " +(BigDecimal.valueOf(valorTotal).setScale(2, RoundingMode.HALF_EVEN))+" \n\n";
        }

        email.setText("RD Gente que cuida de Gente \nAqui está o seu cupom!\n"+                                              // Título do E-mail
                opcaoPagamento+
                enderecoEntrega+
                 corpoEmail +                                                                                              // Corpo do E-mail, ta declarado aqui em cima
                "\n\nTotal Compra R$ " + documentoFiscalDTO.getValorTotalNota());                                                                                            // Total da compra toda

        mailSender.send(email);                                                                                             // Enviar
    }


    public Object ultimaNota(BigInteger idCliente){
        Map<BigInteger, DocumentoFiscalDTO> map = new HashMap<>();
        Query query = manager.createNativeQuery("SELECT \n" +
                "tc.ID_CLIENTE,\n" +//0
                "tc.NM_CLIENTE,\n" +//1
                "tc.NR_CPF,\n" +//2
                "tc.DS_EMAIL,\n" +//3
                "tpp.ID_PEDIDO,\n" +//4
                "tpp.ID_FORMA_PAGAMENTO,\n" +//5
                "tpp.ID_TIPO_PAGAMENTO,\n" +//6
                "tpp.NR_NUMERO_CARTAO,\n" +//7
                "tpp.NM_NOME_TITULAR,\n" +//8
                "te.ID_ENDERECO,\n" +//9
                "te.DS_ENDERECO,\n" +//10
                "te.NR_ENDERECO,\n" +//11
                "te.NR_CEP,\n" +//12
                "te.DS_BAIRRO,\n" +//13
                "te.DS_CIDADE,\n" +//14
                "te.SG_ESTADO,\n" +//15
                "te.NM_COMPLEMENTO,\n" +//16
                "tdf.NR_CHAVE_ACESSO,\n" +//17
                "tdf.NR_NF,\n" +//18
                "tdf.NR_SERIE,\n" +//19
                "tdf.DT_EMISSAO,\n" +//20
                "tdf.VL_DOCUMENTO_FISCAL,\n" +//21
                "tdf.ID_DOCUMENTO_FISCAL\n" +//22
                "\n" +
                "FROM TB_PAGAMENTO_PEDIDO tpp ,TB_ENDERECO te ,TB_CLIENTE tc ,TB_DOCUMENTO_FISCAL tdf  \n" +
                "WHERE te.ID_ENDERECO = tpp.ID_ENDERECO\n" +
                "AND tpp.ID_CLIENTE = tc.ID_CLIENTE \n" +
                "AND tpp.ID_DOCUMENTO_FISCAL = tdf.ID_DOCUMENTO_FISCAL \n" +
                "AND tc.ID_CLIENTE ="+idCliente+" \n" +
                "ORDER BY ID_DOCUMENTO_FISCAL DESC LIMIT 1 ;");

        List<Object []> listEntity = query.getResultList();
        Object [] dfo = listEntity.get(0);
        Long contadorItens = 0L;
//        for (Object [] dfo: listEntity){
            DocumentoFiscalDTO documentoFiscalDTO = new DocumentoFiscalDTO();

            documentoFiscalDTO.setNrCpf((String) dfo[2]);
            documentoFiscalDTO.setDsEmail((String) dfo[3]);
            documentoFiscalDTO.setIdNF((BigInteger) dfo[22]);
            documentoFiscalDTO.setDtEmissao((Date) dfo[20]);
            documentoFiscalDTO.setNrPedido((BigInteger) dfo[4]);


            documentoFiscalDTO.setNmNomeTitular((String) dfo[8]);
            documentoFiscalDTO.setValorTotalNota((BigDecimal) dfo[21]);
            TbTipoPagamento tbTipoPagamento = consultaBanco.tipoPagamento((BigInteger) dfo[6]);
            documentoFiscalDTO.setFormaPagamento(tbTipoPagamento.getDsTipoPagamento());
            documentoFiscalDTO.setIdFormaPagamento(tbTipoPagamento.getIdTipoPagamento());
            TbEndereco endereco = consultaBanco.endereco((BigInteger) dfo[9]);
            documentoFiscalDTO.setEndereco(endereco);
            if (tbTipoPagamento.getIdTipoPagamento().equals(BigInteger.valueOf(1))) {
                byte[] decodedBytes = Base64.getDecoder().decode((String) dfo[7]);
                String decodedString = new String(decodedBytes);
                String ultimosDigitos = "****.****.****." + decodedString.substring(decodedString.length() - 4);
                documentoFiscalDTO.setNrNumeroCartao(ultimosDigitos);
            }else {
                documentoFiscalDTO.setNrNumeroCartao((String) dfo[7]);
            }

            List<DocumentoFiscalItemDTO> listDTO = new ArrayList<>();

            List<TbDocumentoItem> listEntity2 = consultaBanco.itensNF((BigInteger) dfo[22]);
            for (TbDocumentoItem itens: listEntity2){
                contadorItens++;
                Double valorTotal = 0d;
                valorTotal = itens.getQtItem()* itens.getVlItem().doubleValue();
                DocumentoFiscalItemDTO documentoFiscalItemDTO = new DocumentoFiscalItemDTO();
                documentoFiscalItemDTO.setCdProduto(itens.getProduto().getCdProduto());
                documentoFiscalItemDTO.setNmProduto(itens.getProduto().getNomeFantasia());
                documentoFiscalItemDTO.setNrItemDocumento(itens.getNrItemDocumento());
                documentoFiscalItemDTO.setVlItemUnitario(itens.getVlItem());
                documentoFiscalItemDTO.setVlIcms(itens.getVlIcms());
                documentoFiscalItemDTO.setQtItem(itens.getQtItem());
                documentoFiscalItemDTO.setPcIcms(itens.getPcIcms());
                documentoFiscalItemDTO.setVlTotalItem(BigDecimal.valueOf(valorTotal).setScale(2, RoundingMode.HALF_EVEN));

                listDTO.add(documentoFiscalItemDTO);
            }

            documentoFiscalDTO.setQtItens(contadorItens);
            documentoFiscalDTO.setItensDocumento(listDTO);
            map.put(documentoFiscalDTO.getIdNF(), documentoFiscalDTO);


        return documentoFiscalDTO;
    }

    public TbTipoPagamento tipoPagamento(BigInteger idTipoPagamento){
        TbTipoPagamento tbTipoPagamento = new TbTipoPagamento();
        Query query = manager.createNativeQuery("SELECT \n" +
                "ttp.ID_TIPO_PAGAMENTO,\n" +
                "ttp.DS_TIPO_PAGAMENTO_ECOM\n" +
                "FROM TB_TIPO_PAGAMENTO ttp\n" +
                "WHERE ID_TIPO_PAGAMENTO = "+idTipoPagamento+" ");
        List<Object[]> ListEntity = query.getResultList();
        Object[] entity = ListEntity.get(0);

        return tbTipoPagamento;
    }


}
