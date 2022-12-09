package com.tarzan.navigation.utils;

import com.tarzan.navigation.common.constant.CoreConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AppInstallTools {


    @Resource
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.url}")
    private String url;

    private final static String h2Driver="jdbc:h2:";

    private final static String mysqlDriver="jdbc:mysql:";


    public void  install(){
        if (url.contains(h2Driver)) {
            installSQL("schema-h2.sql");
        }
        if (url.contains(mysqlDriver)) {
            installSQL("schema-mysql.sql");
        }

    }

    private  void  installSQL(String fileName){
        if(tableNum()<=CoreConst.TABLE_NUM){
            log.info("创建数据库表开始");
            InputStream dbIos = this.getClass().getResourceAsStream("/db/"+ fileName);
            try {
                InputStreamReader reader = new InputStreamReader(dbIos, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(reader);
                String txt = FileCopyUtils.copyToString(in);
                jdbcTemplate.execute(txt);
                log.info("创建数据库表完毕");
            } catch (IOException e) {
                e.printStackTrace();
            }
            initData();
        }else{
            CoreConst.IS_INSTALLED.set(true);
            if(userNum()!=0){
                CoreConst.IS_REGISTERED.set(true);
            }
        }
    }

    private void  initData(){
        log.info("初始化数据开始");
        InputStream dbIos = this.getClass().getResourceAsStream("/db/data.sql");
        try {
            InputStreamReader reader = new InputStreamReader(dbIos, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String txt = FileCopyUtils.copyToString(in);
            jdbcTemplate.execute(txt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("初始化数据完毕");
    }

    /**
     * 获取所有表数量
     */
    private  int tableNum() {
        List<String> tableNames= new ArrayList<>();
        try {
            Connection getConnection=jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = getConnection.getMetaData();
            ResultSet rs = metaData.getTables(getConnection.getCatalog(), null, null, new String[] { "TABLE" });
            while (rs.next()) {
                String tableName=rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNames.size();
    }

    /**
     * 获取用户数
     */
    private  int userNum() {
        int users =  this.jdbcTemplate.queryForObject("SELECT count(*) AS num FROM sys_user",
                    (rs, rowNum) -> rs.getInt("num"));
          return users;
    }


}
