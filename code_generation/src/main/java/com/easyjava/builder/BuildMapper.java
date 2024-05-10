
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

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.get_mappers_path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(Constants.get_mappers_path() + "/" + tableInfo.getBeanName() + "Mapper.java");
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
            bw.write("package " + Constants.get_package_mappers() + ";\n\n");

            bw.write("import org.apache.ibatis.annotations.Param;\n");
            bw.write("\n\n");
            BuildComment.createComment(bw, tableInfo.getTableComment() + "Mapper");
            bw.write("public interface " + tableInfo.getBeanName() + "Mapper<T, P> extends BaseMapper<T, P> {\n");

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyList = entry.getValue();
                StringBuilder sb = new StringBuilder();
                StringBuffer methodParam = new StringBuffer();
                String tmp;
                for (FieldInfo key : keyList) {
                    methodParam.append(" @Param(\"").append(key.getPropertyName()).append("\") ")
                            .append(key.getJavaType()).append(" ").append(key.getPropertyName()).append(",");
                    tmp = key.getPropertyName().substring(0, 1).toUpperCase() + key.getPropertyName().substring(1);
                    sb.append(tmp).append("And");
                }
                if (methodParam.length() > 0) {
                    methodParam.deleteCharAt(methodParam.length() - 1);
                }
                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "查询" + tableInfo.getTableComment());
                bw.write("    T selectBy" + sb.substring(0, sb.length() - 3) + "(" + methodParam + " );\n");
                bw.write("\n");
                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "更新" + tableInfo.getTableComment());
                bw.write("    Integer updateBy" + sb.substring(0, sb.length() - 3) + "( @Param(\"bean\") T t,"
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
