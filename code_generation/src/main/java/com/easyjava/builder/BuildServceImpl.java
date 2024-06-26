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

public class BuildServceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildServce.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.get_service_impl_path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + "ServiceImpl";
        File file = new File(Constants.get_service_impl_path() + "/" + className + ".java");
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
            bw.write("package " + Constants.get_package_service_impl() + ";\n\n");
            bw.write("import java.util.List;\n\n");
            bw.write("import " + Constants.get_package_po() + "." + tableInfo.getBeanName() + ";\n");
            bw.write("import " + Constants.get_package_param() + "." + tableInfo.getBeanParaName() + ";\n");
            bw.write("import " + Constants.get_package_param() + ".SimplePage;\n");
            bw.write("import " + Constants.get_package_vo() + ".PaginationResultVO;\n");
            bw.write("import " + Constants.get_package_service() + "." + tableInfo.getBeanName() + "Service;\n");
            bw.write("import " + Constants.get_package_mappers() + "." + tableInfo.getBeanName()
                    + Constants.SUFFIX_MAPPERS + ";\n\n");
            bw.write("import org.springframework.stereotype.Service;\n");
            bw.write("import javax.annotation.Resource;\n\n");
            BuildComment.createComment(bw, tableInfo.getTableComment() + "Service实现类");
            bw.write("@Service(\"" + tableInfo.getBeanName().substring(0, 1).toLowerCase()
                    + tableInfo.getBeanName().substring(1) + "Service\")\n");
            bw.write("public class " + className + " implements " + tableInfo.getBeanName() + "Service {\n\n");

            bw.write("\t@Resource\n");
            String mapperName = tableInfo.getBeanName().substring(0, 1).toLowerCase()
                    + tableInfo.getBeanName().substring(1) + Constants.SUFFIX_MAPPERS;
            bw.write("\tprivate " + tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + "<" + tableInfo.getBeanName()
                    + "," + tableInfo.getBeanParaName() + "> "
                    + mapperName + ";\n\n");

            BuildComment.createFieldComment(bw, "根据条件查询列表");
            bw.write("\t@Override\n");
            bw.write("    public List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParaName()
                    + " query){\n");
            bw.write("        return this." + mapperName + ".selectList(query);\n");
            bw.write("    }\n\n");

            BuildComment.createFieldComment(bw, "根据条件查询数量");
            bw.write("\t@Override\n");
            bw.write("    public Integer findCountByParam(" + tableInfo.getBeanParaName() + " query){\n");
            bw.write("        return this." + mapperName + ".selectCount(query);\n");
            bw.write("    }\n\n");

            BuildComment.createFieldComment(bw, "分页查询");
            bw.write("\t@Override\n");
            bw.write("    public PaginationResultVO<" + tableInfo.getBeanName() + "> findCountByPage("
                    + tableInfo.getBeanParaName() + " query){\n");
            bw.write("\t\tInteger count = this.findCountByParam(query);\n");
            bw.write("\t\tint pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();\n");
            bw.write("\t\tSimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);\n");
            bw.write("\t\tquery.setSimplePage(simplePage);\n");
            bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.findListByParam(query);\n");
            bw.write("\t\tPaginationResultVO<" + tableInfo.getBeanName()
                    + "> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);\n");
            bw.write("        return result;\n");
            bw.write("    }\n\n");

            BuildComment.createFieldComment(bw, "新增");
            bw.write("\t@Override\n");
            bw.write("    public Integer add(" + tableInfo.getBeanName() + " bean){\n");
            bw.write("        return this." + mapperName + ".insert(bean);\n");
            bw.write("    }\n\n");

            BuildComment.createFieldComment(bw, "批量新增");
            bw.write("\t@Override\n");
            bw.write("    public Integer addBatch(List<" + tableInfo.getBeanName() + "> beans){\n");
            bw.write("\t\treturn this." + mapperName + ".insertBatch(beans);\n");
            bw.write("    }\n\n");

            BuildComment.createFieldComment(bw, "批量新增或更新");
            bw.write("\t@Override\n");
            bw.write("    public Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> beans){\n");
            bw.write("\t\treturn this." + mapperName + ".insertOrUpdateBatch(beans);\n");
            bw.write("    }\n\n");
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyList = entry.getValue();
                StringBuilder sb = new StringBuilder();
                StringBuffer methodParam = new StringBuffer();
                StringBuffer param = new StringBuffer();
                String tmp;
                for (FieldInfo key : keyList) {
                    methodParam.append(key.getJavaType()).append(" ").append(key.getPropertyName()).append(",");
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
                bw.write("\t@Override\n");
                bw.write("    public " + tableInfo.getBeanName() + " getBy" + sb.substring(0, sb.length() - 3) + "("
                        + methodParam + " ){\n");
                bw.write("        return this." + mapperName + ".selectBy" + sb.substring(0, sb.length() - 3) + "("
                        + param + ");\n");
                bw.write("    }\n");
                bw.write("\n");

                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "更新" + tableInfo.getTableComment());
                bw.write("\t@Override\n");
                bw.write("    public Integer updateBy" + sb.substring(0, sb.length() - 3) + "( "
                        + tableInfo.getBeanName() + " t,"
                        + methodParam + " ){\n");
                bw.write("        return this." + mapperName + ".updateBy" + sb.substring(0, sb.length() - 3) + "(t,"
                        + param + ");\n");
                bw.write("    }\n");
                bw.write("\n");

                BuildComment.createFieldComment(bw,
                        "根据" + sb.substring(0, sb.length() - 3) + "删除" + tableInfo.getTableComment());
                bw.write("\t@Override\n");
                bw.write(
                        "    public Integer deleteBy" + sb.substring(0, sb.length() - 3) + "(" + methodParam + " ){\n");
                bw.write("        return this." + mapperName + ".deleteBy" + sb.substring(0, sb.length() - 3) + "("
                        + param + ");\n");
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
