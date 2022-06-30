package cn.makerknz.product.server.emqx;

import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import io.github.gcdd1993.emqx.sdk.http.api.EmqxOperationApi;
import io.github.gcdd1993.emqx.sdk.http.model.request.PublishRequest;
import io.github.gcdd1993.emqx.sdk.http.model.response.EmqxResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 20:26
 */

@Service
public class EmqxAdminService {

    @Autowired
    private EmqxOperationApi emqxOperationApi;

    /**
     * 将消息发送到设备
     * @param clientId
     * @param topic
     * @param payload
     */
    public void sendMsgToDevice(String clientId, String topic, String payload) {

        PublishRequest publishRequest = PublishRequest.builder()
                .clientId(clientId)
                .topic(topic)
                .payload(payload)
                .encoding("plain")
                .qos(1)
                .retain(false)
                .build();

        try {
            Response<EmqxResponseDto<Void>> response = emqxOperationApi.publish(publishRequest).execute();
            if (response.code() == HttpStatus.OK.value()) {
                EmqxResponseDto<Void> body = response.body();
                if (body.getCode() != 0) {
                    // EMQX请示错误
                    throw new BusinessException(ExceptionEnum.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

}
