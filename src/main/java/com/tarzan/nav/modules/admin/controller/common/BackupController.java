package com.tarzan.nav.modules.admin.controller.common;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.props.TarzanProperties;
import com.tarzan.nav.modules.admin.vo.DbBackupVO;
import com.tarzan.nav.modules.admin.vo.base.PageResultVo;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.utils.DbBackupTools;
import com.tarzan.nav.utils.FileUtil;
import com.tarzan.nav.utils.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 * 数据备份
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年10月11日
 */
@Controller
@RequestMapping("/backup")
@Slf4j
@AllArgsConstructor
public class BackupController {

    private final DbBackupTools dbTools;
    private final TarzanProperties tarzanProperties;
    private final EhCacheCacheManager cacheCacheManager;

    /**
     * 备份数据库
     */
    @GetMapping("/upload")
    public String importSQL(){
        return CoreConst.ADMIN_PREFIX + "backup/form";
    }

    @ResponseBody
    @PostMapping("/upload")
    public ResponseVo upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        String backupDir= StringUtils.appendIfMissing(tarzanProperties.getBackupDir(), File.separator);
        File file=new File(backupDir+multipartFile.getOriginalFilename());
        try {
            FileUtil.copy(multipartFile.getBytes(), file);
        } catch (IOException e) {
            return  ResultUtil.error();
        }
        return ResultUtil.success();
    }

    /**
     * 备份数据库
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseVo backupSQL(){
        if (dbTools.backSql()) {
            return ResultUtil.success("数据备份成功");
        } else {
            return ResultUtil.error("数据备份失败");
        }
    }

    /**
     * 备份文件列表
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo backupList(Integer pageNumber, Integer pageSize){
        File file=new File(tarzanProperties.getBackupDir());
        File[] files= file.listFiles();
        if(files==null){
            return ResultUtil.table(null,null);
        }
        List<DbBackupVO> list= new ArrayList<>(15);
        Arrays.asList(files).forEach(e->{
            DbBackupVO vo=new DbBackupVO();
            vo.setFileName(e.getName());
            vo.setSize(e.length());
            vo.setCreateTime(new Date(e.lastModified()));
            list.add(vo);
        });
        Collections.reverse(list);
        int endIndex = Math.min(pageNumber * pageSize, list.size());
        return ResultUtil.table(list.subList((pageNumber - 1) * pageSize, endIndex), (long) list.size());
    }

    /**
     * 删除备份
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(String fileName) {
        String backupDir= StringUtils.appendIfMissing(tarzanProperties.getBackupDir(), File.separator);
        File file=new File(backupDir+fileName);
        if (file.exists()&&file.delete()) {
            return ResultUtil.success("删除备份成功");
        } else {
            return ResultUtil.error("删除备份失败");
        }
    }

    /**
     * 还原备份
     */
    @PostMapping("rollback")
    @ResponseBody
    public ResponseVo rollback(String fileName){
        if (dbTools.rollback(fileName)) {
            cacheCacheManager.getCache("website").clear();
            cacheCacheManager.getCache("category").clear();
            cacheCacheManager.getCache("link").clear();
            cacheCacheManager.getCache("dashboard").clear();
            return ResultUtil.success("数据恢复成功");
        } else {
            return ResultUtil.error("数据恢复失败");
        }
    }

    /**
     * 下载
     */
    @PostMapping("/download")
    public void download(String fileName, HttpServletResponse response) throws IOException {
        String backupDir= StringUtils.appendIfMissing(tarzanProperties.getBackupDir(), File.separator);
        FileInputStream fis=new FileInputStream(backupDir+fileName);
        response.addHeader("Content-Disposition", "attachment;filename="+fileName+";"+"filename*=utf-8''"+fileName);
        FileUtil.copy(fis, response.getOutputStream());
    }

}
