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

public class BuildServce {
    private static final Logger logger = LoggerFactory.getLogger(BuildServce.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.get_service_path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + "Service";
        File file = new File(Constants.get_service_path() + "/" + className + ".java");
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
            bw.write("package " + Constants.get_package_service() + ";\n\n");
            bw.write("import java.util.List;\n\n");
            bw.write("import " + Constants.get_package_po() + "." + tableInfo.getBeanName() + ";\n");
            bw.write("import " + Constants.get_package_param() + "." + tableInfo.getBeanParaName() + ";\n");
            bw.write("import " + Constants.get_package_vo() + ".PaginationResultVO;\n\n");

            BuildComment.createComment(bw, tableInfo.getTableComment() +"Service");
            bw.write("public interface " + className + " {\n\n");
            BuildComment.createFieldComment(bw, "根据条件查询列表");
            bw.write("    List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParaName()
                    + " query);\n\n");
            BuildComment.createFieldComment(bw, "根据条件查询数量");
            bw.write("    Integer findCountByParam(" + tableInfo.getBeanParaName() + " query);\n\n");
            BuildComment.createFieldComment(bw, "分页查询");
            bw.write("    PaginationResultVO<" + tableInfo.getBeanName() + "> findCountByPage("
                    + tableInfo.getBeanParaName() + " query);\n\n");
            BuildComment.createFieldComment(bw, "新增");
            bw.write("    Integer add(" + tableInfo.getBeanName() + " bean);\n\n");
            BuildComment.createFieldComment(bw, "批量新增");
            bw.write("    Integer addBatch(List<" + tableInfo.getBeanName() + "> beans);\n\n");
            BuildComment.createFieldComment(bw, "批量新增或更新");
            bw.write("    Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> beans);\n\n");
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyList = entry.getValue();
                StringBuilder sb = new StringBuilder();
                StringBuffer methodParam = new StringBuffer();
                String tmp;
                for (FieldInfo key : keyList) {
                    methodParam.append(key.getJavaType()).append(" ").append(key.getPropertyName()).append(",");
                    tmp = key.getPropertyName().substring(0, 1).toUpperCase() + key.getPropertyName().substring(1);
                    sb.append(tmp).append("And");
                }
                if (methodParam.length() > 0) {
                    methodParam.deleteCharAt(methodParam.length() - 1);
                }
                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "查询" + tableInfo.getTableComment());
                bw.write("    " + tableInfo.getBeanName() + " getBy" + sb.substring(0, sb.length() - 3) + "("
                        + methodParam + " );\n");
                bw.write("\n");
                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "更新" + tableInfo.getTableComment());
                bw.write("    Integer updateBy" + sb.substring(0, sb.length() - 3) + "( " + tableInfo.getBeanName() + " t,"
                        + methodParam + " );\n");
                bw.write("\n");
                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "删除" + tableInfo.getTableComment());
                bw.write("    Integer deleteBy" + sb.substring(0, sb.length() - 3) + "(" + methodParam + " );\n");
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
