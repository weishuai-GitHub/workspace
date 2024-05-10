
package com.easyjava.builder;

import java.io.BufferedWriter;
import java.util.Date;

import com.easyjava.bean.Constants;
import com.easyjava.utils.DateUtils;

// 生成注释
public class BuildComment {
    // 生成类注释
    public static void createComment(BufferedWriter wr, String comment) throws Exception {
        wr.write("/**\n");
        wr.write(" * @Description: " + (comment == null ? "" : comment) + "\n");
        wr.write(" *\n");
        wr.write(" * @author: " + Constants.AUTHOR + "\n");
        wr.write(" * @date: " + DateUtils.format(new Date(), DateUtils._yyyy_MM_dd) + "\n");
        wr.write(" */\n");
    }

    // 生成字段注释
    public static void createFieldComment(BufferedWriter wr, String comment) throws Exception {
        wr.write("    /**\n");
        wr.write("     * " + (comment == null ? "" : comment) + "\n");
        wr.write("     */\n");
    }

    // 生成方法注释(暂不需要)
    public static void createMethodComment(BufferedWriter wr, String comment) {
        System.out.println("createMethodComment");
    }
}
