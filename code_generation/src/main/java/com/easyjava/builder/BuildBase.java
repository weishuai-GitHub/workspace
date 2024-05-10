package com.easyjava.builder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;


public class BuildBase {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);
    public static void execute() {
        List<String> headInfoList =new ArrayList<>();
        //生成dateUtils
        headInfoList.add("package "+Constants.get_package_utils()+";\n");
        build(headInfoList,"DateUtils",Constants.get_utils_path());

        //生成BaseMapper
        headInfoList.clear();
        headInfoList.add("package "+Constants.get_package_mappers()+";\n");
        build(headInfoList, "BaseMapper", Constants.get_mappers_path());

        //生成SimplePage
        headInfoList.clear();
        headInfoList.add("package "+Constants.get_package_param()+";\n");
        build(headInfoList,"SimplePage",Constants.get_param_path());
        build(headInfoList, "BaseQuery", Constants.get_param_path());

        //生成VO
        headInfoList.clear();
        headInfoList.add("package "+Constants.get_package_vo()+";\n\n");
        build(headInfoList,"ResponseCodeEnum",Constants.get_vo_path());
        build(headInfoList,"ResponseVO",Constants.get_vo_path());
        headInfoList.add("import java.util.List;\n");
        headInfoList.add("import java.util.ArrayList;\n");
        build(headInfoList,"PaginationResultVO",Constants.get_vo_path());

        //生成Exception
        headInfoList.clear();
        headInfoList.add("package "+Constants.get_package_exception()+";\n\n");
        headInfoList.add("import " + Constants.get_package_vo() + ".ResponseCodeEnum;\n");
        build(headInfoList,"BussinessException",Constants.get_exception_path());

        //生成ABaseController
        headInfoList.clear();
        headInfoList.add("package "+Constants.get_package_controller()+";\n\n");
        headInfoList.add("import "+Constants.get_package_vo()+".ResponseVO;\n");
        headInfoList.add("import "+Constants.get_package_vo()+".ResponseCodeEnum;\n");
        build(headInfoList,"ABaseController",Constants.get_controller_path());
        headInfoList.add("import " + Constants.get_package_exception() + ".BussinessException;\n");
        build(headInfoList,"AGlobalExceptionHanderController",Constants.get_controller_path());
    }

    public static void build(List<String> headInfoList, String fileName,String outputPath)
    {
        File folder = new File(outputPath);
        if (!folder.exists()) {
            folder.mkdirs();  
        }
        File file = new File(outputPath,fileName+".java");

        try {
            if(!file.exists())
                file.createNewFile();
        } catch (Exception e) {
            logger.error("create file failed", e);
        }

        OutputStream out = null;
        OutputStreamWriter ouw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader ir = null;
        BufferedReader br = null;

        try {
            out = new  FileOutputStream(file);
            ouw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(ouw);

            String templatePath = BuildBase.class.getResource("/template/"+fileName+".txt").getPath();

            in = new FileInputStream(templatePath);
            ir = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(ir);
            for (String headInfo : headInfoList) {
                bw.write(headInfo);
                // bw.write("\n");
            }
            bw.write("\n");
            String line = null;
            while((line = br.readLine())!=null)
            {
                bw.write(line);
                bw.write("\n");
            }
        } catch (Exception e) {
            logger.error("create based class failed:{}",fileName, e);
        }finally
        {
            try {
                if(br!=null)
                    br.close();
                if(ir!=null)
                    ir.close();
                if(in!=null)
                    in.close();
                if(bw!=null)
                    bw.close();
                if(ouw!=null)
                    ouw.close();
                if(out!=null)
                    out.close();
            } catch (Exception e) {
                logger.error("close stream failed", e);
            }
        }
    }
}
