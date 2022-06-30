package cn.makerknz.product.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * code: "031VeXll2LKx774Hm2ol23WaTs1VeXlU"
 * encryptedData: "77XFzimjje4i8p9O4oGXeQSTR5ilYl8PURjZQVTm2k15dPP7daY3LdbYNJ6C/NL79OCGMLNjPGHfSdOawd6//EOPmd96IGEHplpKizpWxzaPb3CUTieNWCBz0DFXmrOMAB/eB0LKt01C1pAbrdfPX6ohj8JtBDHeFfLbnPYXRo+Xyp1dTwfnANCQJcWY+k70J0XJZu3J3jt/L+EUdQqNtjRj89OaUyT8F3Ow7SE7UITXaPf5+VFS5Ck5qTHl/E23SJYET7afQTftLOe815MsGkHJro+ofY6bjQLF+zfigS3NMy/ycgC6fd1l63OhGqPbdHDpLy3eitgNZIORTTKqVpZVm9N5yIU8xiP9dm2zERLg96ldN2ZkSOFRY0nld+KegsnVmGv2jezOU5F1QsnnnAq/N+4lvsZ3nxVehqH3lUZMsnW2f++oPXfAki6Eh1DWq4jpglpAa7Ca2Wmj3FvBXg=="
 * iv: "uU6QGKPcVMpQwImVscX+CQ=="
 * rawData: "{"nickName":"微信用户","gender":0,"language":"","city":"","province":"","country":"","avatarUrl":"https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132"}"
 * signature: "da874d74b0cc7c65413b7252504f143d307b71da"
 * @Author: 朱康南
 * @Date: 2021/6/2/002 9:34
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReqDTO {

    private String code;

    private String encryptedData;

    private String iv;

    private String rawData;

    private String signature;

}
