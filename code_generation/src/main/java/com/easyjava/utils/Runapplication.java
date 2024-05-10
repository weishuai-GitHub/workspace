package com.easyjava.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;
import com.easyjava.bean.TableInfo;
import com.easyjava.builder.BuildBase;
import com.easyjava.builder.BuildPo;
import com.easyjava.builder.BuildQuery;
import com.easyjava.builder.BuildTable;

public class Runapplication {
    private static final Logger logger = LoggerFactory.getLogger(Runapplication.class);
    public static void main(String[] args) {
        List<TableInfo> tableInfos = BuildTable.getTables();
        for (TableInfo tableInfo : tableInfos) {
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            // System.out.println(JsonUtils.toJson(tableInfo));
        }
        BuildBase.execute();
        logger.info(Constants.get_utils_path());
        // logger.info(Constants.get_po_path());
        // logger.info(Constants.get_param_path());
    }
}
