package org.geelato.web.yida.rest;

import com.aliyun.dingtalkyida_1_0.models.BatchGetFormDataByIdListResponse;
import com.aliyun.dingtalkyida_1_0.models.BatchGetFormDataByIdListResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.checkerframework.checker.units.qual.A;
import org.geelato.core.orm.Dao;
import org.geelato.web.platform.m.flow.service.ListService;
import org.geelato.web.platform.m.excel.entity.PlaceholderMeta;
import org.geelato.web.platform.m.excel.service.ExcelWriter;
import org.geelato.web.yida.service.InstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "/api/yida/export")
public class ExportExcelRestController {

    private Logger logger = LoggerFactory.getLogger(ExportExcelRestController.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

    @Autowired
    @Qualifier("primaryDao")
    private Dao dao;

    // c:\\a\\b\\c
    @Value(value = "${geelato.file.root.path}")
    private String fileRootPath;

    @Value(value = "${app.systemToken}")
    private String systemToken;

    @Autowired
    private InstanceService instanceService;
    @Autowired
    private ExcelWriter excelWriter;

    @Autowired
    private ListService listService;


    /**
     * 导出对账单
     *
     * @param req
     * @param response
     * @param appType
     * @param formUuid
     * @param formInstId
     * @param userId
     */
    @RequestMapping(value = "/excel/{appType}/{formUuid}/{formInstId}/{userId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void exportBill(HttpServletRequest req, HttpServletResponse response, @PathVariable String appType, @PathVariable String formUuid, @PathVariable String formInstId, @PathVariable String userId) {
        try {
            // 获取表单数据
            BatchGetFormDataByIdListResponse batchGetFormDataByIdListResponse = instanceService.queryByIds(systemToken, formUuid, Arrays.asList(formInstId.split(",")), true, userId, appType);
            List<BatchGetFormDataByIdListResponseBody.BatchGetFormDataByIdListResponseBodyResult> resultList = batchGetFormDataByIdListResponse.getBody().getResult();

            // 获取模板文件
            String templateName = "对账单按订单导出";
            String templateExt = "xls";
            String templateFilePath = fileRootPath + "\\template\\" + templateName + "." + templateExt;

            FileInputStream templateFileInputStream = new FileInputStream(templateFilePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(templateFileInputStream);
            POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
            HSSFWorkbook templateWorkbook = new HSSFWorkbook(fileSystem);

            // 如果多组数据写在一个Sheet中
            writeSheet(templateWorkbook, resultList);
            // 如果多组数据写在多个Sheet中
            // writeSheets(templateWorkbook, resultList);
            String billNo = resultList.get(0).getFormData().get("textField_lygt7ed").toString();
            // 生成目录、导出文件
            Date date = new Date();
            String exportFileName = "对账单_" + billNo + "_" + date.getTime();
            String directory = fileRootPath + "\\export\\" + sdf.format(date);
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) {
                directoryFile.mkdirs();
            }
            File exportFile = new File(directory + "\\" + exportFileName + "." + templateExt);
            OutputStream outputStream = new FileOutputStream(exportFile);
            templateWorkbook.removeSheetAt(0);
            templateWorkbook.write(outputStream);
            outputStream.close();

            bufferedInputStream.close();

            // 将导出的文件写入输入流，用于直接输出到浏览器端
            FileInputStream fileInputStream = new FileInputStream(exportFile);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，网络传输URL编码，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(exportFileName + "." + templateExt, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + exportFile.length());
            OutputStream outputStream2 = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream2.write(buffer);
            outputStream2.flush();

        } catch (Exception e) {
            logger.error("基于Yida的表单信息导出Excel出错。", e);
        }

    }

    /**
     * 多组数据写在一个Sheet中，对于指定需要迭代的行，将多组数据的拼接形成多行列表
     *
     * @param templateWorkbook
     * @param resultList       多组数据
     */
    private void writeSheet(HSSFWorkbook templateWorkbook, List<BatchGetFormDataByIdListResponseBody.BatchGetFormDataByIdListResponseBodyResult> resultList) {
        HSSFSheet placeholderMetaSheet = templateWorkbook.getSheetAt(0);
        Map<String, PlaceholderMeta> placeholderMetaMap = excelWriter.readPlaceholderMeta(placeholderMetaSheet);
        HSSFSheet sheet = templateWorkbook.getSheetAt(1);

        ArrayList groupSumLists = new ArrayList();
        List<Map> valueMapList = new ArrayList<>();
        for (BatchGetFormDataByIdListResponseBody.BatchGetFormDataByIdListResponseBodyResult result : resultList) {
            Map valueMap = result.getFormData();
            // 应收明细表
            ArrayList list = (ArrayList) valueMap.get("tableField_lb0jkkfa");
            // 币种 selectField_lb0jkkfd
            // 小计 numberField_lb0jkkfg
            ArrayList groupSumList = listService.sum(list, "numberField_lb0jkkfg", "selectField_lb0jkkfd");

            groupSumLists.addAll(groupSumList);

            // 货物明细表
            ArrayList goodsList = (ArrayList) valueMap.get("tableField_lb0bpav4");
            String goodsNames = listService.concatOne(goodsList, "textField_lb0bpav5", ",", true);

            valueMap.put("goodsNames", goodsNames);
            valueMapList.add(valueMap);
        }

        ArrayList groupSumList = listService.sum(groupSumLists, "numberField_lb0jkkfg", "selectField_lb0jkkfd");
        // 取多项数据中的其中一项作为非列表单元格的变量数据（因为多项数据中非列表数据的值应是一致的，这里只取第一项数据）
        Map valueMap = resultList.get(0).getFormData();
        valueMap.put("groupSumList", groupSumList);

        excelWriter.writeSheet(sheet, placeholderMetaMap, valueMapList, valueMap);
    }

    /**
     * 多组数据写在多个Sheet中
     *
     * @param templateWorkbook
     * @param resultList       多组数据
     */
    private void writeSheets(HSSFWorkbook templateWorkbook, List<BatchGetFormDataByIdListResponseBody.BatchGetFormDataByIdListResponseBodyResult> resultList) {
        for (int i = 1; i < resultList.size(); i++) {
            templateWorkbook.cloneSheet(1);
        }

        HSSFSheet placeholderMetaSheet = templateWorkbook.getSheetAt(0);
        Map<String, PlaceholderMeta> placeholderMetaMap = excelWriter.readPlaceholderMeta(placeholderMetaSheet);
        for (int i = 0; i < resultList.size(); i++) {
            HSSFSheet sheet = templateWorkbook.getSheetAt(i + 1);
            BatchGetFormDataByIdListResponseBody.BatchGetFormDataByIdListResponseBodyResult result = resultList.get(i);
            Map valueMap = result.getFormData();
            // 应收明细表
            ArrayList list = (ArrayList) valueMap.get("tableField_lb0jkkfa");
            // 币种 selectField_lb0jkkfd
            // 小计 numberField_lb0jkkfg
            ArrayList groupSumList = listService.sum(list, "numberField_lb0jkkfg", "selectField_lb0jkkfd");
            valueMap.put("groupSumList", groupSumList);

            // 货物明细表
            ArrayList goodsList = (ArrayList) valueMap.get("tableField_lb0bpav4");
            String goodsNames = listService.concatOne(goodsList, "textField_lb0bpav5", ",", true);

            valueMap.put("goodsNames", goodsNames);
            excelWriter.writeSheet(sheet, placeholderMetaMap, valueMap);
        }
    }

}
