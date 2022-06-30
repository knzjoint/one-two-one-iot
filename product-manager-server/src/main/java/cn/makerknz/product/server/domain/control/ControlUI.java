package cn.makerknz.product.server.domain.control;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/21 7:42
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControlUI {

    List<BaseForm> controls;

}
