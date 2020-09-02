package br.com.rdevs.ecommerce.pedido.service;
//Classe para inserir as regras de negócio e metodos a serem inseridos na classe Controller
import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.pedido.model.dto.PedidoDTO;
import br.com.rdevs.ecommerce.pedido.model.dto.PedidoItemDTO;
import br.com.rdevs.ecommerce.pedido.model.entity.TbPedido;
import br.com.rdevs.ecommerce.pedido.model.entity.TbPedidoItem;
import br.com.rdevs.ecommerce.pedido.model.entity.TbStatusPedido;
import br.com.rdevs.ecommerce.pedido.repository.PedidoRepository;
import br.com.rdevs.ecommerce.pedido.service.bo.TbPedidoBO;
import br.com.rdevs.ecommerce.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TbPedidoService {
    //implementar inclusão, exclusão e consulta
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CadastroRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TbPedidoBO pedidoBO;


    @PersistenceContext
    private EntityManager em;

    //Método de buscar Pedidos pela id do cliente
    public List<PedidoDTO> buscarPedidoPorIdCliente(Long idCliente) {
        List<PedidoDTO> pedidosDTO = new ArrayList<>();
        List<TbPedido> pedidos = pedidoRepository.findByClienteIdCliente(idCliente);

        for(TbPedido tbPedido : pedidos){
            PedidoDTO pedidoDTO = pedidoBO.parseToDTO(tbPedido);
            pedidoDTO.setIdCliente(idCliente);

            List<PedidoItemDTO> pedidoItensDto = new ArrayList<>();

            for (TbPedidoItem pedidoItem : tbPedido.getItens()){
                PedidoItemDTO pedidoItemDTO = new PedidoItemDTO();
                pedidoItemDTO.setIdPedido(tbPedido.getIdPedido());
                pedidoItemDTO.setDsProduto(pedidoItem.getProduto().getNomeFantasia());
                pedidoItemDTO.setNrItemPedido(pedidoItem.getNrItemPedido());
                pedidoItemDTO.setCdProduto(pedidoItem.getProduto().getCdProduto());
                pedidoItemDTO.setVlPedidoItem(pedidoItem.getVlPedidoItem());

                pedidoItensDto.add(pedidoItemDTO);
            }
            pedidoDTO.setItems(pedidoItensDto);

            pedidosDTO.add(pedidoDTO);
        }


        return pedidosDTO;
    }

    //Método de inserir Pedidos
    public TbPedido inserirPedido(PedidoDTO dto) throws Exception{
        TbPedido pedidoEntity = pedidoBO.parseToEntity(dto,null);
        TbCliente cliente = new TbCliente();
        cliente.setIdCliente(dto.getIdCliente());
        pedidoEntity.setCliente(cliente);

        List<TbPedidoItem> itemsEntity = new ArrayList<>();

        double valor = 0d;
        Long quantidade = 0l;
        for(PedidoItemDTO itemDTO : dto.getItems()){
            TbPedidoItem itemEntity = new TbPedidoItem();
            itemEntity.setNrItemPedido(itemDTO.getNrItemPedido());
            itemEntity.setProduto(produtoRepository.getOne(itemDTO.getCdProduto()));
            itemEntity.setVlPedidoItem(itemEntity.getProduto().getValorUnidade());

            valor += itemEntity.getVlPedidoItem().doubleValue();
            quantidade++;
            itemEntity.setNrItemPedido(quantidade);
            itemEntity.setPedido(pedidoEntity);
            itemsEntity.add(itemEntity);
        }
        pedidoEntity.setVlTotalPedido(BigDecimal.valueOf(valor));
        pedidoEntity.setQtItensPedido(quantidade.intValue());
        pedidoEntity.setItens(itemsEntity);

        TbStatusPedido tbStatusPedido = new TbStatusPedido();
        tbStatusPedido.setCdStatusPedido(2L);
        pedidoEntity.setStatusPedido(tbStatusPedido);

        return pedidoRepository.save(pedidoEntity);
        }

    }





