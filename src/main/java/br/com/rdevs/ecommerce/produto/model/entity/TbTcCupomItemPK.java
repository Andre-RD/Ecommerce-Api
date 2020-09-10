package br.com.rdevs.ecommerce.produto.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TbTcCupomItemPK implements Serializable {
    private Long idCupomItem;

    private Long idCupom;

}
