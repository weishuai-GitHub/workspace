package com.easyjava.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.get_controller_path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + "Controller";
        File file = new File(Constants.get_controller_path() + "/" + className + ".java");
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (Exception e) {
            logger.error("create file failed", e);
        }
        OutputStream out = null;
        OutputStreamWriter ouw = null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(file);
            ouw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(ouw);
            bw.write("package " + Constants.get_package_controller() + ";\n\n");
            bw.write("import java.util.List;\n\n");
            bw.write("import " + Constants.get_package_po() + "." + tableInfo.getBeanName() + ";\n");
            bw.write("import " + Constants.get_package_param() + "." + tableInfo.getBeanParaName() + ";\n");
            bw.write("import " + Constants.get_package_vo() + ".ResponseVO;\n");
            bw.write("import " + Constants.get_package_service() + "." + tableInfo.getBeanName() + "Service;\n\n");

            bw.write("import org.springframework.web.bind.annotation.RequestBody;\n");
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
            bw.write("import org.springframework.web.bind.annotation.RequestParam;\n");
            bw.write("import org.springframework.web.bind.annotation.RestController;\n");
            bw.write("import javax.annotation.Resource;\n\n");
            BuildComment.createComment(bw, tableInfo.getTableComment() + "Controller");
            // bw.write("@RestController(\""+ className.substring(0,1).toLowerCase()
            // +className.substring(1) +"\")\n");
            bw.write("@RestController\n");
            bw.write("@RequestMapping(\"/" + tableInfo.getBeanName().substring(0, 1).toLowerCase()
                    + tableInfo.getBeanName().substring(1) + "\")\n");
            bw.write("public class " + className + " extends ABaseController {\n\n");

            bw.write("\t@Resource\n");
            String serviceName = tableInfo.getBeanName() + "Service";
            String serviceImplName = serviceName.substring(0, 1).toLowerCase() + serviceName.substring(1);
            bw.write("\tprivate " + serviceName + " " + serviceImplName + ";\n\n");
            BuildComment.createFieldComment(bw, "通用查询");
            bw.write("\t@SuppressWarnings(\"rawtypes\")\n");
            bw.write("\t@RequestMapping(\"loadDataList\")\n");
            bw.write("\tpublic ResponseVO loadDataList(" + tableInfo.getBeanParaName() + " query){\n");
            bw.write("\t\treturn getSuccessResponseVO( this." + serviceImplName + ".findCountByPage(query));\n");
            bw.write("\t}\n\n");

            BuildComment.createFieldComment(bw, "新增");
            bw.write("\t@SuppressWarnings(\"rawtypes\")\n");
            bw.write("\t@RequestMapping(\"add\")\n");
            bw.write("    public ResponseVO add(" + tableInfo.getBeanName() + " bean){\n");
            bw.write("        return getSuccessResponseVO( this." + serviceImplName + ".add(bean));\n");
            bw.write("    }\n\n");

            BuildComment.createFieldComment(bw, "批量新增");
            bw.write("\t@SuppressWarnings(\"rawtypes\")\n");
            bw.write("\t@RequestMapping(\"addBatch\")\n");
            bw.write("    public ResponseVO addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> beans){\n");
            bw.write("\t\treturn getSuccessResponseVO( this." + serviceImplName + ".addBatch(beans));\n");
            bw.write("    }\n\n");

            BuildComment.createFieldComment(bw, "批量新增或更新");
            bw.write("\t@SuppressWarnings(\"rawtypes\")\n");
            bw.write("\t@RequestMapping(\"addOrUpdateBatch\")\n");
            bw.write("    public ResponseVO addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName()
                    + "> beans){\n");
            bw.write("\t\treturn getSuccessResponseVO( this." + serviceImplName + ".addOrUpdateBatch(beans));\n");
            bw.write("    }\n\n");
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyList = entry.getValue();
                StringBuilder sb = new StringBuilder();
                StringBuffer methodParam = new StringBuffer();
                StringBuffer param = new StringBuffer();
                String tmp;
                for (FieldInfo key : keyList) {
                    methodParam.append("@RequestParam(\"" + key.getPropertyName() + "\") " + key.getJavaType())
                            .append(" ").append(key.getPropertyName()).append(",");
                    tmp = key.getPropertyName().substring(0, 1).toUpperCase() + key.getPropertyName().substring(1);
                    sb.append(tmp).append("And");
                    param.append(key.getPropertyName()).append(",");
                }
                if (methodParam.length() > 0) {
                    methodParam.deleteCharAt(methodParam.length() - 1);
                    param.deleteCharAt(param.length() - 1);
                }
                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "查询" + tableInfo.getTableComment());
                bw.write("\t@SuppressWarnings(\"rawtypes\")\n");
                bw.write("\t@RequestMapping(\"" + "getBy" + sb.substring(0, sb.length() - 3) + "\")\n");
                bw.write("    public ResponseVO getBy" + sb.substring(0, sb.length() - 3) + "("
                        + methodParam + " ){\n");
                bw.write("        return getSuccessResponseVO(this." + serviceImplName + ".getBy"
                        + sb.substring(0, sb.length() - 3) + "(" + param + "));\n");
                bw.write("    }\n");
                bw.write("\n");

                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "更新" + tableInfo.getTableComment());
                bw.write("\t@SuppressWarnings(\"rawtypes\")\n");
                bw.write("\t@RequestMapping(\"" + "updateBy" + sb.substring(0, sb.length() - 3) + "\")\n");
                bw.write("    public ResponseVO updateBy" + sb.substring(0, sb.length() - 3) + "( @RequestBody "
                        + tableInfo.getBeanName() + " t,"
                        + methodParam + " ){\n");
                bw.write("        return getSuccessResponseVO( this." + serviceImplName + ".updateBy"
                        + sb.substring(0, sb.length() - 3) + "(t," + param + "));\n");
                bw.write("    }\n");
                bw.write("\n");

                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "删除" + tableInfo.getTableComment());
                bw.write("\t@SuppressWarnings(\"rawtypes\")\n");
                bw.write("\t@RequestMapping(\"" + "deleteBy" + sb.substring(0, sb.length() - 3) + "\")\n");
                bw.write("    public ResponseVO deleteBy" + sb.substring(0, sb.length() - 3) + "(" + methodParam
                        + " ){\n");
                bw.write("        return getSuccessResponseVO( this." + serviceImplName + ".deleteBy"
                        + sb.substring(0, sb.length() - 3) + "(" + param + "));\n");
                bw.write("    }\n");
                bw.write("\n");
            }
            bw.write("}\n");
            bw.flush();
        } catch (Exception e) {
            logger.error("write file failed", e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (ouw != null) {
                    ouw.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                logger.error("close stream failed", e2);
            }
        }
    }
}
