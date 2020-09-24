package br.com.rdevs.ecommerce.pagamentopedido.controller;

import br.com.rdevs.ecommerce.pagamentopedido.service.TipoPagamentoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TipoPagamentoController {

    @Autowired
    private TipoPagamentoService service;

    @ApiOperation(value = "Buscartodas as notas")
    @GetMapping("/tipopagamento")
    public ResponseEntity<Object> buscarTodas(){
        return ResponseEntity.ok().body(service.buscarTodas());
    }


//    @PostMapping("/tipopagamento")
//    public ResponseEntity<Object> salvarTipoPagamento(@RequestBody TipoPagamentoDTO tipoPagamentoDTO){
//        ResultData resultData = null;
//        if(tipoPagamentoDTO.getIdTipoPagamento() == null)
//            resultData = new ResultData(HttpStatus.BAD_REQUEST.value(),  "Campo: idTipoPagamento não informado!");
//        else if(tipoPagamentoDTO.getIdTipoPagamento() == null)
//            resultData = new ResultData(HttpStatus.BAD_REQUEST.value(),"Campo: idTipoPagamento não informado!");
//
//        if(resultData != null)
//            return ResponseEntity.badRequest().body(resultData);
//        else{
//            try{
//                TbTipoPagamento tipoPagamento = service.inserir(tipoPagamentoDTO);
//                resultData = new ResultData<TbTipoPagamento>(HttpStatus.OK.value(),
//                return ResponseEntity.status(HttpStatus.CREATED).body(resultData);
//            }catch(Exception e){
//                resultData = new ResultData(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocorreu um erro ao registrar Tipo de Pagamento", e.getMessage());
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR. value()).body(resultData);
//            }
//        }
//    }


}
