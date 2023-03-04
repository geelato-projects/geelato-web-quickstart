package org.geelato.web.yida.service;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenService {

    private Logger logger = LoggerFactory.getLogger(TokenService.class);
    private GetAccessTokenResponse accessTokenResponse;
    private Date newAccessTokenTime;

    @Value(value = "${app.appKey}")
    private String appKey;

    @Value(value = "${app.appSecret}")
    private String appSecret;

    /**
     * 使用 Token 初始化账号Client
     *
     * @return Client
     * @throws Exception
     */
    public Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    /**
     * 首次获取之后，半个小时内取上次的Token，半小时后，从服务端获取最新的token
     *
     * accessToken的有效期为7200秒（2小时），有效期内重复获取会返回相同结果并自动续期，过期后获取会返回新的accessToken。
     * 开发者需要缓存accessToken，用于后续接口的调用。因为每个应用的accessToken是彼此独立的，所以进行缓存时需要区分应用来进行存储。
     * 不能频繁调用gettoken接口，否则会受到频率拦截。
     *
     * @param appKey    DingTalk中的企业内部应用appKey,非宜搭的
     * @param appSecret DingTalk中的企业内部应用appSecret,非宜搭的
     * @return
     * @throws Exception
     */
    public GetAccessTokenResponse getAccessTokenResponse(String appKey, String appSecret) throws Exception {
        if (accessTokenResponse != null && newAccessTokenTime != null) {
            // 半个小时内取上次的Token，半小时后，从服务端获取最新的token
            Date date = new Date();
            int diffSeconds = (int) ((date.getTime() - newAccessTokenTime.getTime()) / 1000);
            if (diffSeconds < 1800) {
                return accessTokenResponse;
            }
        }
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest().setAppKey(appKey).setAppSecret(appSecret);
        accessTokenResponse = createClient().getAccessToken(getAccessTokenRequest);
        newAccessTokenTime = new Date();
        return accessTokenResponse;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() throws Exception {
        return getAccessTokenResponse(appKey, appSecret).getBody().getAccessToken();
    }

}