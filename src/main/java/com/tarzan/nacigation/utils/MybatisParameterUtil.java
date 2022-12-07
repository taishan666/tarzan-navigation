package com.tarzan.nacigation.utils;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author tarzan
 **/

public class MybatisParameterUtil {

    final static int paramNums=999;


    public static <T, F> void cutInParameter(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.in(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().in(column, objects);
                    }
                });
            }
        }else{
            wrapper.eq(column, null); 
        }
    }

    public static <T, F> void cutNotInParameter(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.notIn(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().notIn(column, objects);
                    }
                });
            }
        }
    }


    public static <T, F> void cutInParameter(LambdaQueryChainWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.in(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().in(column, objects);
                    }
                });
            }
        }
    }

    public static <T, F> void cutNotInParameter(LambdaQueryChainWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.notIn(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().notIn(column, objects);
                    }
                });
            }
        }
    }

    public static <T, F> void cutInParameter(LambdaUpdateWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.in(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().in(column, objects);
                    }
                });
            }
        }
    }

    public static <T, F> void cutNotInParameter(LambdaUpdateWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.notIn(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().notIn(column, objects);
                    }
                });
            }
        }
    }


    public static <T, F> void cutInParameter(LambdaUpdateChainWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.in(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().in(column, objects);
                    }
                });
            }
        }
    }

    public static <T, F> void cutNotInParameter(LambdaUpdateChainWrapper<T> wrapper, SFunction<T, ?> column, Collection<F> coll)  {
        List<List<F>> newList = splitList(coll, paramNums);
        if (CollectionUtils.isNotEmpty(newList)) {
            if(newList.size() ==1){
                wrapper.notIn(column, newList.get(0));
            }else{
                wrapper.and(i -> {
                    i.in(column, newList.get(0));
                    newList.remove(0);
                    for (List<F> objects : newList) {
                        i.or().notIn(column, objects);
                    }
                });
            }
        }
    }


    public static <F> List<List<F>> splitList(Collection<F> coll, int rows) {
        List<F> list=new ArrayList<>(coll);
        List<List<F>> newList = new ArrayList<>();
        int num = list.size();
        int i = 0;
        while (num > rows) {
            int end=i+rows;
            newList.add(list.subList(i, end));
            i = end;
            num = num - rows;
        }
        if (num > 0) {
            int end=i+num;
            newList.add(list.subList(i, end));
        }
        return newList;
    }
}

