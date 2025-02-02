package vip.yeee.memoo.common.excel.kit;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.memoo.base.model.exception.BizException;
import vip.yeee.memoo.base.web.utils.SpringContextUtils;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * description......
 * @author https://www.yeee.vip
 */
@Slf4j
public class EasyExcelKit {

    public static <T> List<T> syncReadExcel(MultipartFile file, Class<T> clz){
        try {
            return EasyExcel.read(file.getInputStream())
                    .head(clz)
                    .autoCloseStream(Boolean.TRUE)
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            log.error("读取文件异常", e);
            throw new BizException("读取文件异常");
        }
    }

    public static <T> List<T> syncReadExcel(InputStream inputStream, Class<T> clz){
        try {
            return EasyExcel.read(inputStream)
                    .head(clz)
                    .autoCloseStream(Boolean.TRUE)
                    .sheet()
                    .doReadSync();
        } catch (Exception e) {
            log.error("读取文件异常", e);
            throw new BizException("读取文件异常");
        }
    }

    public static <T> void export(ExcelWriter excelWriter, WriteSheet writeSheet, List<T> exportDataList) {
        try {
            excelWriter.write(exportDataList, writeSheet);
        } catch (Exception e) {
            log.error("【导出失败】 ", e);
            throw new BizException("导出失败");
        }
    }

    public static <T> void export(OutputStream out, List<T> exportDataList) {
        export(out, (Class<T>) exportDataList.get(0).getClass(), exportDataList);
    }

    public static <T> void export(OutputStream out, Class<T> clazz, List<T> exportDataList) {
        try {
            ExcelWriter excelWriter = EasyExcelKit.buildExcelWriter(out, clazz);
            WriteSheet writeSheet = EasyExcelKit.buildWriteSheet(excelWriter, 0, "工作表1").build();
            EasyExcelKit.export(excelWriter, writeSheet, exportDataList);
            excelWriter.finish();
        } catch (Exception e) {
            log.error("【导出失败】 ", e);
            throw new BizException("导出失败");
        }
    }

    public static <T> void export(OutputStream out, List<String> headList, List<T> exportDataList) {
        try {
            ExcelWriter excelWriter = EasyExcelKit.buildExcelWriter(out, headList);
            WriteSheet writeSheet = EasyExcelKit.buildWriteSheet(excelWriter, 0, "工作表1").build();
            EasyExcelKit.export(excelWriter, writeSheet, exportDataList);
            excelWriter.finish();
        } catch (Exception e) {
            log.error("【导出失败】 ", e);
            throw new BizException("导出失败");
        }
    }

    public static <T> void export2Response(List<T> exportDataList) {
        export2Response(exportDataList, (Class<T>) exportDataList.get(0).getClass());
    }

    public static <T> void export2Response(List<T> exportDataList, Class<T> clazz) {
        EasyExcelKit.export2Response(null, exportDataList, clazz, null);
    }

    public static <T> void export2Response(List<T> exportDataList, String fileName) {
        EasyExcelKit.export2Response(null, exportDataList, (Class<T>) exportDataList.get(0).getClass(), fileName);
    }

    public static <T> void export2Response(List<T> exportDataList, Class<T> clazz, String fileName) {
        EasyExcelKit.export2Response(null, exportDataList, clazz, fileName);
    }

    public static <T> void export2Response(Consumer<ExcelWriterSheetBuilder> sheetBuilderConsumer, List<T> exportDataList, Class<T> clazz, String fileName) {
        try {
            HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            LocalDateTime now = LocalDateTime.now();
            fileName = (StrUtil.isNotBlank(fileName) ? fileName : "export" + "/" + DateUtil.format(now, DatePattern.PURE_DATETIME_MS_PATTERN) + "/" + System.currentTimeMillis()) + ".xlsx";
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ExcelWriter excelWriter = EasyExcelKit.buildExcelWriter(response.getOutputStream(), clazz);
            ExcelWriterSheetBuilder sheetBuilder = EasyExcelKit.buildWriteSheet(excelWriter, 1, "工作表1");
            if (sheetBuilderConsumer != null) {
                sheetBuilderConsumer.accept(sheetBuilder);
            }
            EasyExcelKit.export(excelWriter, sheetBuilder.build(), exportDataList);
            excelWriter.finish();
        } catch (Exception e) {
            log.error("【导出失败】 ", e);
            throw new BizException("导出失败");
        }
    }

    public static <T> ExcelWriter buildExcelWriter(OutputStream out, Class<T> clazz) {
        return EasyExcelFactory
                .write(out)
                .head(clazz)
                .build();
    }

    public static <T> ExcelWriter buildExcelWriter(OutputStream out, List<String> headList) {
        return EasyExcelFactory
                .write(out)
                .head(headList.stream().map(Lists::newArrayList).collect(Collectors.toList()))
                .build();
    }

    public static ExcelWriterSheetBuilder buildWriteSheet(ExcelWriter excelWriter, Integer sheetNo, String sheetName) {
        return new ExcelWriterSheetBuilder(excelWriter)
                .sheetNo(sheetNo)
                .sheetName(sheetName)
//                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
        ;
    }
}
