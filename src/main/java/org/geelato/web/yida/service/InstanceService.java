package org.geelato.web.yida.service;

import com.aliyun.dingtalkyida_1_0.Client;
import com.aliyun.dingtalkyida_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import org.geelato.core.orm.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class InstanceService {

    private Logger logger = LoggerFactory.getLogger(InstanceService.class);
    @Autowired
    private TokenService tokenService;

    @Autowired
    @Qualifier("primaryDao")
    private Dao dao;

    public Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    public BatchGetFormDataByIdListResponse queryByIds(String systemToken, String formUuid, List idList, Boolean needFormInstanceValue, String userId, String appType) throws Exception {

        BatchGetFormDataByIdListHeaders batchGetFormDataByIdListHeaders = new BatchGetFormDataByIdListHeaders();
        batchGetFormDataByIdListHeaders.xAcsDingtalkAccessToken = tokenService.getToken();
        BatchGetFormDataByIdListRequest batchGetFormDataByIdListRequest = new BatchGetFormDataByIdListRequest()
                .setSystemToken(systemToken)
                .setFormUuid(formUuid)
                .setFormInstanceIdList(idList)
                .setNeedFormInstanceValue(false)
                .setUserId(userId)
                .setAppType(appType);
        Client client = createClient();
        try {
            return client.batchGetFormDataByIdListWithOptions(batchGetFormDataByIdListRequest, batchGetFormDataByIdListHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            logger.error("通过实例ID批量获取实例信息出错。", err);
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            logger.error("通过实例ID批量获取实例信息出错。", err);
        }
        return null;
    }

    public SearchFormDatasResponse search(String systemToken, String appType, String formUuid, String userId, String searchFieldJson) throws Exception {
        Client client = createClient();
        SearchFormDatasHeaders searchFormDatasHeaders = new SearchFormDatasHeaders();
        searchFormDatasHeaders.xAcsDingtalkAccessToken = tokenService.getToken();
        SearchFormDatasRequest searchFormDatasRequest = new SearchFormDatasRequest();
        searchFormDatasRequest.setAppType(appType);
        searchFormDatasRequest.setUserId(userId);
        searchFormDatasRequest.setSystemToken(systemToken);
        searchFormDatasRequest.setFormUuid(formUuid);
        searchFormDatasRequest.setSearchFieldJson(searchFieldJson);
        try {
            return client.searchFormDatasWithOptions(searchFormDatasRequest, searchFormDatasHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        }
        return null;
    }

    public void saveInstance(Map valueMap) {

    }
}
