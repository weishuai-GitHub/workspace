package com.easyjava.builder;
import java.io.BufferedWriter;

public class BuildSetandGet {
    
    public static void createSet(BufferedWriter wr,String type,String name) throws Exception {
        wr.write("    public void set"+name.substring(0,1).toUpperCase()+name.substring(1)+"("+type+" "+name+") {\n");
        wr.write("        this."+name+" = "+name+";\n");
        wr.write("    }\n");
    }
    
    public static void createGet(BufferedWriter wr,String type,String name) throws Exception {
        wr.write("    public "+type+" get"+name.substring(0,1).toUpperCase()+name.substring(1)+"() {\n");
        wr.write("        return this."+name+";\n");
        wr.write("    }\n");
    }
}
