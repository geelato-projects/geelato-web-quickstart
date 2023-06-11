package org.geelato.web.yida.rest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkyida_1_0.models.BatchGetFormDataByIdListResponse;
import com.aliyun.dingtalkyida_1_0.models.BatchGetFormDataByIdListResponseBody;import com.aliyun.dingtalkyida_1_0.models.SearchFormDatasResponse;
import com.aliyun.dingtalkyida_1_0.models.SearchFormDatasResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import org.geelato.core.api.ApiResult;
import org.geelato.core.orm.Dao;
import org.geelato.web.yida.service.InstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "/api/yida/instance")
public class InstanceRestController {

    private Logger logger = LoggerFactory.getLogger(InstanceRestController.class);

    @Autowired
    @Qualifier("primaryDao")
    private Dao dao;

    @Value(value = "${app.systemToken}")
    private String systemToken;

    @Value(value = "${app.adminId}")
    private String userId;
    @Autowired
    private InstanceService instanceService;

    /**
     * 代理YiDa的实例查询
     * 依据实例id获取一个实例数据
     * @param appType
     * @param formUuid
     * @param formInstId
     */
    @RequestMapping(value = "/queryOne/{appType}/{formUuid}/{formInstId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ApiResult queryOneByPathVar(@PathVariable String appType, @PathVariable String formUuid, @PathVariable String formInstId) {
        ApiResult apiResult = new ApiResult();
        try {
            // 获取表单数据
            BatchGetFormDataByIdListResponse batchGetFormDataByIdListResponse = instanceService.queryByIds(systemToken, formUuid, Arrays.asList(formInstId.split(",")), true, userId, appType);
            List<BatchGetFormDataByIdListResponseBody.BatchGetFormDataByIdListResponseBodyResult> resultList = batchGetFormDataByIdListResponse.getBody().getResult();
            if(resultList.size()>0){
                apiResult.setData(resultList.get(0).getFormData());
            }
        } catch (Exception e) {
            logger.error("基于Yida的表单实例信息查询出错（queryOne）。", e);
            logger.info("appType:{0},formUuid:{1},formInstId:{2},userId:{3}", appType, formUuid, formInstId, userId);
            apiResult.error();
        }
        return apiResult;
    }

    /**
     * 获取实体一个属性的值
     *
     * @param appType
     * @param formUuid
     * @param key      查询实例的一个属性名称
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryOneValueByFields/{appType}/{formUuid}", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResult queryOneValueByFields(@PathVariable String appType, @PathVariable String formUuid, @RequestParam String key, HttpServletRequest request) {
        ApiResult apiResult = new ApiResult();
        String searchFieldJson = "";
        JSONObject json = new JSONObject();
        try {
            request.getParameterMap().forEach((name, value) -> {
                if ("key".equals(name)) {
                } else {
                    json.put(name, value[0]);
                }
            });
            searchFieldJson = json.toJSONString();
            // 获取表单数据
            SearchFormDatasResponse searchFormDatasResponse = instanceService.search(systemToken, appType, formUuid, userId, searchFieldJson);
            List<SearchFormDatasResponseBody.SearchFormDatasResponseBodyData> resultList = searchFormDatasResponse.getBody().getData();
            if (resultList.size() > 0) {
                apiResult.setData(resultList.get(0).getFormData().get(key));
            }
        } catch (Exception e) {
            logger.error("基于Yida的表单实例信息查询出错（queryOneValueByFields）。", e);
            logger.info("appType:{0},formUuid:{1},userId:{2},searchFieldJson:{3},key:{4}", appType, formUuid, userId, searchFieldJson, key);
            apiResult.error();
        }
        return apiResult;
    }

    /**
     * 代理YiDa的实例查询
     * 依据实例id获取一个实例数据
     * @param appType
     * @param formUuid
     * @param formInstId
     */
    @RequestMapping(value = "/queryOne", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ApiResult queryOneByRequestParam(@RequestParam String appType, @RequestParam String formUuid, @RequestParam String formInstId) {
        ApiResult apiResult = new ApiResult();
        try {
            // 获取表单数据
            BatchGetFormDataByIdListResponse batchGetFormDataByIdListResponse = instanceService.queryByIds(systemToken, formUuid, Arrays.asList(formInstId.split(",")), true, userId, appType);
            List<BatchGetFormDataByIdListResponseBody.BatchGetFormDataByIdListResponseBodyResult> resultList = batchGetFormDataByIdListResponse.getBody().getResult();
            if(resultList.size()>0){
                apiResult.setData(resultList.get(0).getFormData());
            }
        } catch (Exception e) {
            logger.error("基于Yida的表单实例信息查询出错（queryOne）。", e);
            logger.info("appType:{0},formUuid:{1},formInstId:{2},userId:{3}", appType, formUuid, formInstId, userId);
            apiResult.error();
        }
        return apiResult;
    }
}
