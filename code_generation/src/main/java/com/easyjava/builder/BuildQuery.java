
package com.easyjava.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;

public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo  tableInfo) {
        File folder = new File(Constants.get_param_path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(Constants.get_param_path() + "/" + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAM + ".java");
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
            bw.write("package " + Constants.get_package_param() + ";\n\n");

            if(tableInfo.isHasBigDecimal())
                bw.write("import java.math.BigDecimal;\n"); 
            
            bw.write("\n\n");
            BuildComment.createComment(bw,tableInfo.getTableComment());
            bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAM + " extends BaseQuery {\n");
            List<FieldInfo> extendInfo = new ArrayList<>();
            for (FieldInfo filedInfo : tableInfo.getColumnInfo()) {
                BuildComment.createFieldComment(bw,filedInfo.getComment());
                
                //如果是Sting类型，添加模糊查询字段
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType())) {
                    bw.write("    private " + filedInfo.getJavaType() + " " + filedInfo.getPropertyName() + ";\n\n");
                    bw.write("    private String " + filedInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_FUZZY + ";\n\n");
                    FieldInfo filedInfoFuzzy = new FieldInfo();
                    filedInfoFuzzy.setJavaType(filedInfo.getJavaType());
                    filedInfoFuzzy.setPropertyName(filedInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_FUZZY);
                    filedInfoFuzzy.setSqlType(filedInfo.getSqlType());
                    filedInfoFuzzy.setFieldName(filedInfo.getFieldName());
                    extendInfo.add(filedInfoFuzzy);
                }

                //如果是时间类型，添加时间范围查询字段
                else if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, filedInfo.getSqlType())||
                    ArrayUtils.contains(Constants.SQL_DATA_TYPES, filedInfo.getSqlType())) {
                    bw.write("    private String " + filedInfo.getPropertyName() + ";\n\n");
                    bw.write("    private String " + filedInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_START + ";\n\n");
                    bw.write("    private String " + filedInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_END + ";\n\n");
                    FieldInfo start = new FieldInfo();
                    start.setJavaType(filedInfo.getJavaType());
                    start.setPropertyName(filedInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_START);
                    start.setSqlType(filedInfo.getSqlType());
                    start.setFieldName(filedInfo.getFieldName());
                    extendInfo.add(start);
                    FieldInfo end = new FieldInfo();
                    end.setJavaType(filedInfo.getJavaType());
                    end.setPropertyName(filedInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_END);
                    end.setSqlType(filedInfo.getSqlType());
                    end.setFieldName(filedInfo.getFieldName());
                    extendInfo.add(end);
                }
                else {
                    bw.write("    private " + filedInfo.getJavaType() + " " + filedInfo.getPropertyName() + ";\n\n");
                }
            }
            
            //生成get和set方法
            for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
                if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, filedInfo.getSqlType())||
                    ArrayUtils.contains(Constants.SQL_DATA_TYPES, filedInfo.getSqlType()))
                {
                    BuildSetandGet.createSet(bw,"String",filedInfo.getPropertyName());
                    bw.write("\n");
                    BuildSetandGet.createGet(bw,"String",filedInfo.getPropertyName());
                    bw.write("\n");
                }
                else{
                    BuildSetandGet.createSet(bw,filedInfo.getJavaType(),filedInfo.getPropertyName());
                    bw.write("\n");
                    BuildSetandGet.createGet(bw,filedInfo.getJavaType(),filedInfo.getPropertyName());
                    bw.write("\n");
                }
                
            }
            for(FieldInfo filedInfo : extendInfo) {
                if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, filedInfo.getSqlType())||
                    ArrayUtils.contains(Constants.SQL_DATA_TYPES, filedInfo.getSqlType()))
                {
                    BuildSetandGet.createSet(bw,"String",filedInfo.getPropertyName());
                    bw.write("\n");
                    BuildSetandGet.createGet(bw,"String",filedInfo.getPropertyName());
                    bw.write("\n");
                }
                else{
                    BuildSetandGet.createSet(bw,filedInfo.getJavaType(),filedInfo.getPropertyName());
                    bw.write("\n");
                    BuildSetandGet.createGet(bw,filedInfo.getJavaType(),filedInfo.getPropertyName());
                    bw.write("\n");
                }
            }
            tableInfo.setExtendInfo(extendInfo);

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
