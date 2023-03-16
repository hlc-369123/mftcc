/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.components.design.utils;

import cn.mftcc.common.logger.MFLogger;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class FileHelper {

    public static final Logger log = LoggerFactory.getLogger(FileHelper.class);


    public static JSONArray readDir(String path, String type, String condition){
        JSONArray r = new JSONArray();
        File dir = new File(path);
        if(!dir.exists() || !dir.isDirectory()){
            return r;
        }
        String id = "";
        String name = "";
        switch (type){
            case "form":
                id = "formId";
                name = "formName";
                break;
            case "table":
                id = "tableId";
                name = "tableName";
                break;
            case "layout":
                id = "layoutId";
                name = "layoutName";
                break;
            default:
                break;
        }
        File[] fs = dir.listFiles();

        File[] files = new File[]{};
        for(File file : fs){
            File[] jsons = file.listFiles(f -> {
                if(condition == null) {
                    return true;
                }
                return f.getName().toLowerCase().contains(condition.toLowerCase());
            });
            if(jsons != null){
                files = (File[]) ArrayUtils.addAll(files, jsons);
            }
        }

        Arrays.sort(files, new CompratorByLastModified());
        if(files.length > 10){
            files = Arrays.copyOf(files, 10);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(File f : files){
            long ms = f.lastModified();
            Date d = new Date(ms);
            JSONObject item = new JSONObject();
            String content = readFile(f);
            JSONObject json = JSONObject.parseObject(content);
            item.put("id", json.getString(id));
            item.put("name", json.getString(name));
            item.put("type", f.getParentFile().getName());
            item.put("fileName", f.getName());
            item.put("time", sdf.format(d));
            r.add(item);
        }
        return r;
    }

    /**
     * 获取文件
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        return readFile(new File(fileName));
    }


    /**
     * 获取文件
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        StringBuffer sb = new StringBuffer();
        try (
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(fis,"UTF-8"); //最后的"GBK"根据文件属性而定，如果不行，改成"UTF-8"试试
                BufferedReader br = new BufferedReader(reader);
                ) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            MFLogger.error(e.getMessage(),e);
        }
        return sb.toString();
    }

    /**
     * 创建文件
     *
     * @param fileName 文件路径+名称+文件类型
     * @param filecontent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static boolean writeFile(String fileName, String filecontent) {
        return writeFile(new File(fileName), filecontent);
    }

    /**
     * 创建文件
     *
     * @param file 文件
     * @param filecontent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static boolean writeFile(File file, String filecontent) {
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if(!file.createNewFile()){
                    return false;
                }
            }
            writeFileContent(file.getAbsolutePath(), filecontent);
            return true;
        } catch (Exception e) {
            MFLogger.error(e.getMessage(),e);
        }
        return false;
    }

    /**
     * 向文件中写入内容
     *
     * @param filepath 文件路径与名称
     * @param newstr 写入的内容
     * @throws IOException
     */
    private static void writeFileContent(String filepath, String newstr) throws IOException {
        try (
                FileOutputStream writerStream = new FileOutputStream(filepath);
                BufferedWriter utput = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
                ) {
            utput.write(newstr);
        } catch (Exception e) {
            MFLogger.error(e.getMessage(),e);
        }
    }

    public static boolean delFile(String path) {
        try{
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                if(!file.delete()){
                    return false;
                }
            } else if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    delFile(f.getAbsolutePath());
                }
                if(!file.delete()){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            log.error("删除文件失败: " + path);
            return false;
        }
    }

    static class CompratorByLastModified implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0) {
                return -1;//倒序正序控制
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;//倒序正序控制
            }
        }
    }
}
