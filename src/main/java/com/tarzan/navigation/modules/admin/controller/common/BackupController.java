package com.tarzan.navigation.modules.admin.controller.common;

import com.tarzan.navigation.common.props.CmsProperties;
import com.tarzan.navigation.modules.admin.vo.DbBackupVO;
import com.tarzan.navigation.modules.admin.vo.base.PageResultVo;
import com.tarzan.navigation.modules.admin.vo.base.ResponseVo;
import com.tarzan.navigation.utils.DbBackupTools;
import com.tarzan.navigation.utils.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
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
    private final CmsProperties cmsProperties;

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
        File file=new File(cmsProperties.getBackupDir());
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
    public ResponseVo deleteRole(String fileName) {
        String backupDir= StringUtils.appendIfMissing(cmsProperties.getBackupDir(), File.separator);
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
        if ( dbTools.rollback(fileName)) {
            return ResultUtil.success("数据恢复成功");
        } else {
            return ResultUtil.error("数据恢复失败");
        }
    }

}
