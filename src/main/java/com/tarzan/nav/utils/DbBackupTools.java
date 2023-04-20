package com.tarzan.nav.utils;

import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import com.tarzan.nav.common.props.TarzanProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 数据库数据备份
 *
 * @author tarzan Liu
 * @date 2021/7/20 19:44
 */
@Slf4j
@Component
@Data
public class DbBackupTools {

    @Resource
    private  JdbcTemplate jdbcTemplate;
    @Resource
    private TarzanProperties tarzanProperties;
    /** 备份文件前缀 */
    private  final static String FILE_PREFIX="backupSql_";
    /** 时间格式 */
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final static List<String> EXCLUDE_TABLES= new ArrayList<>();

    static {
        EXCLUDE_TABLES.add("SYS_LOG_ERROR");
        EXCLUDE_TABLES.add("SYS_LOGIN_LOG");
        EXCLUDE_TABLES.add("SYS_USER");
    }

    /**
     * 获取所有表名称
     */
    private  List<String> tableNames() {
        List<String> tableNames= new ArrayList<>();
        try {
            Connection getConnection= Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            DatabaseMetaData metaData = getConnection.getMetaData();
            ResultSet rs = metaData.getTables(getConnection.getCatalog(), null, null, new String[] { "TABLE" });
            while (rs.next()) {
                String tableName=rs.getString("TABLE_NAME");
                if(tableName.startsWith("SYS_")||tableName.startsWith("BIZ_")){
                    if(!EXCLUDE_TABLES.contains(tableName)){
                        tableNames.add(tableName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNames;
    }


    /**
     * 数据还原
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized  boolean rollback(String fileName) {
        long stat=System.currentTimeMillis();
        List<String> list=new ArrayList<>();
        //清空所有表数据
        tableNames().forEach(t-> list.add("TRUNCATE TABLE "+t));
        jdbcTemplate.batchUpdate(list.toArray(new String[0]));
        list.clear();
        FileInputStream out=null;
        InputStreamReader reader=null;
        BufferedReader in=null;
        try {
            String backupDir= StringUtils.appendIfMissing(tarzanProperties.getBackupDir(), File.separator);
            out = new FileInputStream(backupDir+fileName);
            reader = new InputStreamReader(out, StandardCharsets.UTF_8);
            in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                 list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        list.forEach(e-> jdbcTemplate.batchUpdate(e));
        log.info("恢复数据耗时 "+(System.currentTimeMillis()-stat)+" ms");
        return true;
    }


    /**
     * 数据备份
     */
    public synchronized boolean backSql() {
        long stat=System.currentTimeMillis();
        try {
            File dir = new File(tarzanProperties.getBackupDir());
            dir.mkdirs();
            String path = dir.getPath() + File.separator+ FILE_PREFIX+System.currentTimeMillis()+".sql" ;
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            tableNames().forEach(t->{
               StringBuilder sb=new StringBuilder();
                List<Map<String, Object>> list=jdbcTemplate.queryForList("SELECT * FROM "+t);
                list.forEach(e->{
                    sb.append("INSERT INTO ").append(t).append(" VALUES (");
                    e.forEach((k,v)->{
                        if(v instanceof String){
                            String str=(String) v;
                            if (str.contains(LineSeparator.Unix)) {
                                str = str.replaceAll(LineSeparator.Unix, "\\\\n");
                            }
                            if (str.contains(LineSeparator.Macintosh)) {
                                str = str.replaceAll(LineSeparator.Macintosh, "\\\\r");
                            }
                            if (str.contains("'")) {
                                str = str.replaceAll("'", "''");
                            }
                            sb.append("'").append(str).append("'").append(",");
                        }else if(v instanceof Date){
                            sb.append("'").append(DATE_FORMAT.format(v)).append("'").append(",");
                        }else{
                            sb.append(v).append(",");
                        }
                    });
                    sb.append("); \n");
                });
                String sql= sb.toString().replace(",);",");");
                try{
                FileOutputStream out = new FileOutputStream(file, true); //如果追加方式用true
                out.write(sql.getBytes(StandardCharsets.UTF_8));//注意需要转换对应的字符集
                out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        log.info("备份文件耗时 "+(System.currentTimeMillis()-stat)+" ms");
        return true;
    }


}
