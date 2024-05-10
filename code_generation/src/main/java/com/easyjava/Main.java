package com.easyjava;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;
import com.easyjava.bean.TableInfo;
import com.easyjava.builder.BuildBase;
import com.easyjava.builder.BuildController;
import com.easyjava.builder.BuildMapXml;
import com.easyjava.builder.BuildMapper;
import com.easyjava.builder.BuildPo;
import com.easyjava.builder.BuildQuery;
import com.easyjava.builder.BuildServce;
import com.easyjava.builder.BuildServceImpl;
import com.easyjava.builder.BuildTable;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        List<TableInfo> tableInfos = BuildTable.getTables();
        BuildBase.execute();
        for (TableInfo tableInfo : tableInfos) {
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMapper.execute(tableInfo);
            BuildMapXml.execute(tableInfo);
            BuildServce.execute(tableInfo);
            BuildServceImpl.execute(tableInfo);
            BuildController.execute(tableInfo);
        }
        
        logger.info(Constants.get_utils_path());
        // logger.info(Constants.get_po_path());
        // logger.info(Constants.get_param_path());
    }
}