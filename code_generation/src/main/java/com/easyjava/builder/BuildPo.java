
package com.easyjava.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.DateUtils;

public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo  tableInfo) {
        File folder = new File(Constants.get_po_path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(Constants.get_po_path() + "/" + tableInfo.getBeanName() + ".java");
        try {
            if(!file.exists())
                file.createNewFile();
        } catch (Exception e) {
            logger.error("create file failed", e);
        }
        OutputStream out = null;
        OutputStreamWriter ouw = null;
        BufferedWriter bw = null;
        try {
            out = new  FileOutputStream(file);
            ouw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(ouw);
            bw.write("package " + Constants.get_package_po() + ";\n\n");

            bw.write("import java.io.Serializable;\n");
            if(tableInfo.isHasBigDecimal())
                bw.write("import java.math.BigDecimal;\n");
            if(tableInfo.isHasDate() || tableInfo.isHasDateTime())
            {
                bw.write("import java.util.Date;\n");
                bw.write("import " + Constants.get_package_utils() + ".DateUtils;\n");
                bw.write(Constants.DATE_SERIALIZATION_CLASSES+";\n");
                bw.write(Constants.DATE_DESERIALIZATION_CLASSES+";\n");
                
            }
            for (FieldInfo filedInfo : tableInfo.getColumnInfo()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELDS, filedInfo.getPropertyName())) {
                    bw.write(Constants.IGNORE_BEAN_TOJSON_CLASSES+";\n");
                    break;
                }
            }    
            
            bw.write("\n\n");
            BuildComment.createComment(bw,tableInfo.getTableComment());
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {\n");
            
            for (FieldInfo filedInfo : tableInfo.getColumnInfo()) {
                BuildComment.createFieldComment(bw,filedInfo.getComment());

                if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, filedInfo.getSqlType())){
                    bw.write("\t" + String.format(Constants.DATE_SERIALIZATION_EXPRESSION, DateUtils.yyyy_MM_dd_HH_mm_ss));
                    bw.write("\n");
                    bw.write("\t" + String.format(Constants.DATE_DESERIALIZATION_EXPRESSION, DateUtils.yyyy_MM_dd_HH_mm_ss));
                    bw.write("\n");
                }

                if(ArrayUtils.contains(Constants.SQL_DATA_TYPES, filedInfo.getSqlType())){
                    bw.write("\t" + String.format(Constants.DATE_SERIALIZATION_EXPRESSION, DateUtils.yyyy_MM_dd));
                    bw.write("\n");
                    bw.write("\t" + String.format(Constants.DATE_DESERIALIZATION_EXPRESSION, DateUtils.yyyy_MM_dd));
                    bw.write("\n");
                }
                
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELDS, filedInfo.getPropertyName())) {
                    bw.write("\t" + Constants.IGNORE_BEAN_TOJSON_EXPRESSION);
                    bw.write("\n");
                }

                bw.write("    private " + filedInfo.getJavaType() + " " + filedInfo.getPropertyName() + ";\n\n");
            }
            
            for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
                BuildSetandGet.createSet(bw,filedInfo.getJavaType(),filedInfo.getPropertyName());
                bw.write("\n");
                BuildSetandGet.createGet(bw,filedInfo.getJavaType(),filedInfo.getPropertyName());
                bw.write("\n");
            }

            // 生成toString方法
            bw.write("    @Override\n");
            bw.write("    public String toString() {\n");
            bw.write("        return \"" + tableInfo.getBeanName() + " {\" +\n");
            for (FieldInfo filedInfo : tableInfo.getColumnInfo()) {
                if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, filedInfo.getSqlType())){
                    bw.write("                \" \\\"" + filedInfo.getComment() + "\\\": \" + DateUtils.format("+filedInfo.getPropertyName()+", DateUtils.yyyy_MM_dd_HH_mm_ss) + \",\"+\n");
                }
                else if(ArrayUtils.contains(Constants.SQL_DATA_TYPES, filedInfo.getSqlType())){
                    bw.write("                \" \\\"" + filedInfo.getComment() + "\\\": \" + DateUtils.format("+filedInfo.getPropertyName()+", DateUtils.yyyy_MM_dd) + \",\"+\n");
                }
                else bw.write("                \" \\\"" + filedInfo.getComment() + "\\\": \" + " + (filedInfo.getPropertyName()==null? "null":filedInfo.getPropertyName()) + " + \",\"+\n");
            }
            bw.write("                \"}\";\n");
            bw.write("    }\n");
            bw.write("}\n");
            bw.flush();
        } catch (Exception e) {
            logger.error("write file failed", e);
        }finally{
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
