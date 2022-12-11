/*Core*/
var Core = (function () {
    var core = {};
    var coreOptions;
    /*Core的参数对象*/
    coreOptions = {
        tableOptions: {
            id: "",
            url: "",
            columns: [],
            uniqueId: "id",//每一行的唯一标识，一般为主键列
            method: "post",//请求方式（*）
            undefinedText: "-", /*为undefiend时显示的字*/
            striped: false, //是否显示行间隔色
            queryParams: queryInitParams,
            responseHandler: responseHandler,
            toolbar: '',        //工具按钮用哪个容器
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 20, 50, 999],
            contentType: "application/x-www-form-urlencoded",//用post请求，这个是必须条件，必须加上，get可以不用，亲测
            dataType: "json",
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: false, //是否启用排序
            sortOrder: "asc", //排序方式
            sortName: "", //排序字段
            queryParamsType: "",
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            showColumns: false, //是否显示所有的列
            showRefresh: false, //是否显示刷新按钮
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true, //是否启用点击选中行
            strictSearch: true,
            showToggle: false, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            showExport: false, //是否显示导出
            exportDataType: "basic", //basic', 'all', 'selected'.
            escape: true,//html转意
            onLoadSuccess: tableLoadSuccess,
            theadClasses: "thead-light",
        }
    };

    /**
     * 发送ajax post请求
     * @param url
     * @param dataToPost
     * @param d
     * @param e
     * @param type
     * @param contentType
     * @param async
     */
    core.postAjax = function (url, dataToPost, d, e, type, contentType, async) {
        url = (ctx + url).replace('//', '/');
        $.ajax({
            url: url,
            cache: false,
            async: async === undefined ? true : async,
            data: dataToPost,
            type: type === undefined ? "POST" : type,
            contentType: contentType === undefined ? 'application/x-www-form-urlencoded; charset=UTF-8' : contentType,
            success: function (data) {
                if (typeof d == "function") {
                    d(data);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                if (typeof e == "function") {
                    e(xhr, textStatus, errorThrown)
                }
                if (xhr.status === 403) {
                    layer.msg("您没有权限访问，请联系管理员！")
                } else if (xhr.status === 500) {
                    layer.msg("服务器内部错误！")
                } else if (xhr.status === 404) {
                    layer.msg("您访问的内容不存在！")
                } else {
                    layer.msg("服务器未知错误！")
                }
            }
        });
    };

    core.download = function (data, strFileName, strMimeType) {
         return download(data, strFileName, strMimeType);
    }

    /**
     * jQuery.load()加载页面
     * @param id
     * @param url
     * @param d
     */
    core.load = function (id, url, d) {
        url = (ctx + url).replace('//', '/');
        $(id).html("");
        $(id).load(url, function (response, status, xhr) {
            if (typeof d == "function" && status === "success") {
                d();
            }
            switch (xhr.status) {
                case 403:
                    layer.msg("您没有权限访问！");
                    break;
                case 404:
                    layer.msg("对应的页面不存在！");
                    break;
            }
        })
    }

    /**
     * 消息提示
     * @param t
     * @param m
     */
    core.showMessage = function (t, m) {
        if (t == 1) {
            $(".prompt-success .prompt-text").text(m);
            $(".prompt-success").show();
            $(".prompt-success .alert-success").css("opacity", "1");
            setTimeout(function () {
                $(".prompt-success .alert-success").css("opacity", "0");
                $(".prompt-success").hide();
            }, 3000);
        } else if (t = 2) {
            $(".prompt-error .prompt-text").text(m);
            $(".prompt-error").show();
            $(".prompt-error .alert-danger").css("opacity", "1");
            setTimeout(function () {
                $(".prompt-error .alert-danger").css("opacity", "0");
                $(".prompt-error").hide();
            }, 5000);
        }
    };
    /**
     * bootstrap-table表格
     * @param options
     * @param success
     */
    core.initTable = function (options, success) {
        var tableOptions = $.extend({}, coreOptions.tableOptions, options);
        tableOptions.url = (ctx + tableOptions.url).replace('//', '/');
        $(tableOptions.id).bootstrapTable({
            url: tableOptions.url, //请求后台的URL（*）
            contentType: tableOptions.contentType, //用post请求，这个是必须条件，必须加上，get可以不用，亲测
            dataType: tableOptions.dataType,
            method: tableOptions.method, //请求方式（*）
            //            toolbar: '#toolbar',        //工具按钮用哪个容器
            undefinedText: tableOptions.undefinedText, /*为undefiend时显示的字*/
            striped: tableOptions.striped, //是否显示行间隔色
            theadClasses: tableOptions.theadClasses,//这里设置表头样式
            cache: tableOptions.cache, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: tableOptions.pagination, //是否显示分页（*）
            sortable: tableOptions.sortable, //是否启用排序
            sortOrder: tableOptions.sortOrder, //排序方式
            sortName: tableOptions.sortName, //排序方式
            toolbar: tableOptions.toolbar,
            //            search: true,             //是否使用客户端搜索
            queryParams: tableOptions.queryParams,//传递参数（*）
            responseHandler: tableOptions.responseHandler,
            queryParamsType: tableOptions.queryParamsType,
            sidePagination: tableOptions.sidePagination, //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: tableOptions.pageNumber, //初始化加载第一页，默认第一页
            pageSize: tableOptions.pageSize, //每页的记录行数（*）
            pageList: tableOptions.pageList, //可供选择的每页的行数（*）
            showColumns: tableOptions.showColumns, //是否显示所有的列
            showRefresh: tableOptions.showRefresh, //是否显示刷新按钮
            minimumCountColumns: tableOptions.minimumCountColumns, //最少允许的列数
            clickToSelect: tableOptions.clickToSelect, //是否启用点击选中行
            strictSearch: tableOptions.strictSearch,
            //            height: 460,            //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            showToggle: tableOptions.showToggle, //是否显示详细视图和列表视图的切换按钮
            uniqueId: tableOptions.uniqueId, //每一行的唯一标识，一般为主键列
            cardView: tableOptions.cardView, //是否显示详细视图
            detailView: tableOptions.detailView, //是否显示父子表
            showExport: tableOptions.showExport, //是否显示导出
            exportDataType: tableOptions.exportDataType, //basic', 'all', 'selected'.
            escape: tableOptions.escape,//html转义
            //            align: "center",
            columns: tableOptions.columns,//表格列
            onLoadSuccess: tableOptions.onLoadSuccess,
            onPostBody: function () {
                //改变复选框样式
                $(tableOptions.id).find("input:checkbox").each(function (i) {
                    var $check = $(this);
                    if ($check.attr("id") && $check.next("label")) {
                        return;
                    }
                    var name = $check.attr("name");
                    var id = name + "-" + i;
                    var $label = (i == 0 ? $('<label for="' + id + '"></label>') : $('<label></label>'));
                    $check.attr("id", id).parent().addClass("tz-checkbox").append($label);
                });
                $(tableOptions.id).find("input:radio").each(function (i) {
                    var $check = $(this);
                    if ($check.attr("id") && $check.next("label")) {
                        return;
                    }
                    var name = $check.attr("name");
                    var id = name + "-" + i;
                    var $label = (i == 0 ? $('<label for="' + id + '"></label>') : (tableOptions.clickToSelect == true ? $('<label></label>') : $('<label for="' + id + '"></label>')));
                    $check.attr("id", id).parent().addClass("tz-radio").append($label);
                });
                if ($.isFunction(options.onPostBody)) {
                    options.onPostBody();
                }
            }
        });
    }


    /*刷新表格 ：flag-是否跳转到当前页。默认首页*/
    core.refreshTable = function (id, flag) {
        if (flag) {
            $(id).bootstrapTable("refresh");
        } else {
            $(id).bootstrapTable("refresh", {"pageNumber": 1});
        }
    }

    /*根据data选中数据*/
    core.checkTableBy = function (id, data) {
        $(id).bootstrapTable("checkBy", data)
    }

    /*根据uniqueId获取所选列*/
    core.getRowByUniqueId = function (id, val) {
        return $(id).bootstrapTable("getRowByUniqueId", val);
    }
    core.selectSingleData = function (id) {
        var selectContent = $(id).bootstrapTable('getSelections');
        if (typeof (selectContent) == 'undefined' || selectContent == "") {
            layer.msg("请先选择一条数据!");
            return false;
        } else if (selectContent.length > 1) {
            layer.msg("只能选择一条数据!");
            return false;
        } else {
            var selectData = selectContent[0];
            return selectData;
        }
    }

    core.selectMutiData = function (id) {
        var checkedRows = $(id).bootstrapTable('getSelections');
        if (checkedRows.length == 0) {
            layer.msg("请先选择一条数据！");
            return false;
        } else {
            return checkedRows;
        }
    }

    /*更新某一列的值 index-行索引，field-字段名，value-值*/
    core.updateCell = function (id, index, field, value) {
        var updateCellOptions = {
            index: index,
            field: field,
            value: value
        }
        return $(id).bootstrapTable("updateCell", updateCellOptions);
    }

    /*禁用button*/
    core.mask = function (e) {
        $(e).attr('disabled', "true");//添加disabled属性
    }
    /*启用button*/
    core.unmask = function (e) {
        $(e).removeAttr('disabled');//添加disabled属性
    }

    /*询问框*/
    core.confirm = function (content, d) {
        layer.confirm(content, {
            icon: 3,
            title: "系统提示",
            btn: ['确认', '取消'],
            btnclass: ['btn btn-primary', 'btn btn-danger'],
        }, function (index) {
            layer.close(index);
            d(true);
        });
    }

    //date类型到字符串
    core.formatterDateTime = function (date) {
        var datetime = date.getFullYear()
            + "-"// "年"
            + ((date.getMonth() + 1) >= 10 ? (date.getMonth() + 1)
                : "0" + (date.getMonth() + 1))
            + "-"// "月"
            + (date.getDate() < 10 ? "0" + date.getDate() : date
                .getDate())
            + " "
            + (date.getHours() < 10 ? "0" + date.getHours() : date
                .getHours())
            + ":"
            + (date.getMinutes() < 10 ? "0" + date.getMinutes()
                : date.getMinutes())
            + ":"
            + (date.getSeconds() < 10 ? "0" + date.getSeconds()
                : date.getSeconds());
        return datetime;
    }

    //long类型转时间字符串
    core.longMsTimeConvertToDateTime = function (time) {
        var myDate = new Date(time);
        return this.formatterDateTime(myDate);
    }

    /*日期+*/
    core.addDate = function (date, days) {
        if (days == undefined || days == '') {
            days = 1;
        }
        var date = new Date(date);
        date.setDate(date.getDate() + days);
        var month = date.getMonth() + 1;
        var day = date.getDate();
        return date.getFullYear() + '-' + getFormatDate(month) + '-' + getFormatDate(day);
    }

    function getFormatDate(arg) {
        if (arg == undefined || arg == '') {
            return '';
        }
        var re = arg + '';
        if (re.length < 2) {
            re = '0' + re;
        }
        return re;
    }

    /*是否是数组*/
    core.isArray = function (s) {
        return s instanceof Array;
    }

    core.clearForm = function (id) {

        var objId = document.getElementById(id);
        if (objId == undefined) {
            return;
        }
        for (var i = 0; i < objId.elements.length; i++) {
            if (objId.elements[i].type == "text") {
                objId.elements[i].value = "";
            } else if (objId.elements[i].type == "password") {
                objId.elements[i].value = "";
            } else if (objId.elements[i].type == "radio") {
                objId.elements[i].checked = false;
            } else if (objId.elements[i].type == "checkbox") {
                objId.elements[i].checked = false;
            } else if (objId.elements[i].type == "select-one") {
                objId.elements[i].options[0].selected = true;
            } else if (objId.elements[i].type == "select-multiple") {
                for (var j = 0; j < objId.elements[i].options.length; j++) {
                    objId.elements[i].options[j].selected = false;
                }
            } else if (objId.elements[i].type == "textarea") {
                objId.elements[i].value = "";
            }
        }
    }

    /*清除表单错误提示*/
    core.clearError = function (id) {
        $(id).find(".warning,.valid,.promimg").remove();
        $(id).find(".error").removeClass("error");
        $(id).find(".prombtn").removeClass("prombtn");
        $(id).find(".prominput").removeClass("prominput");
    }
    /*保留两位小数*/
    core.numberTwo = function (num) {
        if (isNaN(num) || num == "") {
            return num;
        } else {
            if (isNaN(parseFloat(num).toFixed(2))) {
                return num;
            } else {
                return parseFloat(num).toFixed(2);
            }
        }
    }
    /*数字千分话并保留两位小数*/
    core.numToTwo = function (num) {
        try {
            num = this.numberTwo(num).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
        } finally {
            return num;
        }
    }


    // 判断是否为json对象
    core.isJsonObject = function (obj) {
        var isjson = typeof (obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
        return isjson;
    }

    core.setCookie = function (cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toGMTString();
        document.cookie = cname + "=" + encodeURI(cvalue) + "; " + expires + ";path=/";
    }
    core.getCookie = function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i].trim();
            if (c.indexOf(name) == 0) {
                return decodeURI(c.substring(name.length, c.length));
            }
        }
        return "";
    }

    core.getQqInfo = function (qq, d) {
        $.ajax({  /* 使用ajax请求 */
            type: "get",  /* 请求方式为GET */
            url: "http://r.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + qq,  /* 发送请求的地址 */
            dataType: "jsonp",   /* 返回JSONP格式 */
            scriptCharset: 'gbk',
            jsonp: "callback",    /* 重写回调函数名 */
            jsonpCallback: "portraitCallBack",  /* 指定回调函数名 */
            success: function (json) {  /* 请求成功输出 */
                console.log(json)
                if (json[qq] == undefined || json[qq][6].trim() == "") {
                    layer.msg("qq信息不存在！")
                    return;
                }
                var qqInfo = {qq: "", nickname: "", avatar: ""};
                for (var key in json) {
                    qqInfo.qq = key;
                }
                qqInfo.nickname = json[qq][6];
                qqInfo.avatar = json[qq][0];
                console.log(qqInfo)
                if (typeof d == "function") {
                    d(qqInfo);
                }
            },
            error: function () {  /* 请求失败输出 */
                layer.msg('获取QQ信息失败');
            }
        });
    }

    core.parseBool = function parseBool(b) {
        return !(/^(false|0)$/i).test(b) && !!b;
    }


    return core;
})(Core, window);
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds()
// 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;

};

function queryInitParams(params) {
    //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
    return {
        pageNumber: params.pageNumber, // 页码
        pageSize: params.pageSize // 分页大小
    };
}

function responseHandler(data) {
    return data;
}

function tableLoadSuccess(data) {
}