(function($){
    $(document).ready(function(){
        // 侧栏菜单初始状态设置
        if(theme.minNav !== '1')trigger_resizable();
        // 主题状态
        switch_mode(false);
        // 搜索模块
        intoSearch();
        // 粘性页脚
        stickFooter();
        // 网址块提示 
        if(isPC()){ $('[data-toggle="tooltip"]').tooltip({trigger: 'hover'}); }else{ $('.qr-img[data-toggle="tooltip"]').tooltip({trigger: 'hover'}); }
        // 初始化tab滑块
        intoSlider();
        // 初始化theiaStickySidebar
        if($('.sidebar-tools')[0])
            $('.sidebar-tools').theiaStickySidebar({
                additionalMarginTop: 90,
                additionalMarginBottom: 20
            });
        // 初始化游客自定义数据
        if(theme.isCustomize === '1'){
            intoSites(false);
        }
        // 初始化TOP按钮和菜单栏状态
        if ($(window).scrollTop() >= 50) {
            $('#go-to-up').fadeIn(200);
            $('.big-header-banner').addClass('header-bg');
        }
        // 全屏加载
        if($('#loading')[0]){
            var siteWelcome = $('#loading');
            siteWelcome.addClass('close');
            setTimeout(function() {
                siteWelcome.remove();
            }, 600);
        }
        // 首页标签切换
        if(theme.isHome === '1'){
            setTimeout(function () {
                if($('a.smooth[href="'+window.location.hash+'"]')[0]){
                    $('a.smooth[href="'+window.location.hash+'"]').click();
                }
                else if(window.location.hash != ''){
                    $("html, body").animate({
                        scrollTop: $(window.location.hash).offset().top - 90
                    }, {
                        duration: 500,
                        easing: "swing"
                    });
                }
            }, 300);
        }
        initSidebarNav($(".sidebar-item.top-menu"));

        $('.io-ajax-auto').each(function() {
            var _this = $(this);
            var url = _this.attr('href');
            if (!url) {
                url = _this.data('href');
            }
            $.get(url, null, function (data, status) {
                _this.html(data);
            });
            return false;
        });
 /*       if (GetQueryVal('iopay')) {
            if (!window.load_io_pay) {
                window.load_io_pay = true;
                $.getScript(theme.uri+"/iopay/assets/js/pay.js",function() {
                    weixin_auto_send();
                });
            }
        }*/
    });

    function initSidebarNav(container) {
        if(!container[0]) return;
        var dropdownToggle = $('<i class="iconfont icon-arrow-r-m sidebar-more text-sm"></i>');
        container.find(".menu-item-has-children>a").after(dropdownToggle);//after append
    }
    $(document).on('click','a.smooth',function(ev) {
        var _this=$(this);
        ev.preventDefault();
        if($('#sidebar').hasClass('show') && !_this.hasClass('change-href')){
            $('#sidebar').modal('toggle');
        }
        if(_this.attr("href").substr(0, 1) == "#" && $(_this.attr("href"))[0]){
            $("html, body").animate({
                scrollTop: $(_this.attr("href")).offset().top - 90
            }, {
                duration: 500,
                easing: "swing"
            });
        }
        if(_this.hasClass('go-search-btn')){
            $('#search-text').focus();
        }
        if(!_this.hasClass('change-href')){
            var menu =  $("a"+_this.attr("href"));
            if(menu[0]){
                menu.click();
                toTarget(menu.closest('ul'),true,true);
            }
        }
    });
    $(document).on('click','a.smooth-n',function(ev) {
        ev.preventDefault();
        $("html, body").animate({
            scrollTop: $($(this).attr("href")).offset().top - 90
        }, {
            duration: 500,
            easing: "swing"
        });
    });
    $(".panel-body.single img:not(.unfancybox)").each(function(i) {
        var _this = $(this);
        if (!_this.hasClass('wp-smiley') && !this.parentNode.href) {
            if(theme.lazyload == '1')
                _this.wrap("<a class='js' href='" + _this.data('src') + "' data-fancybox='fancybox' data-caption='" + this.alt + "'></a>");
            else
                _this.wrap("<a class='js' href='" + this.src + "' data-fancybox='fancybox' data-caption='" + this.alt + "'></a>");
        }
    });
    // Enable/Disable Resizable Event
    var wid = 0;
    $(window).resize(function() {
        clearTimeout(wid);
        wid = setTimeout(go_resize, 200);
    });
    function go_resize() {
        stickFooter();
        trigger_resizable();
        refreshChart();
    }
    // count-a数字动画
    $('.count-a').each(function () {
        var _this = $(this);
        _this.prop('Counter', 0).animate({
            Counter: _this.text()
        }, {
            duration: 1000,
            easing: 'swing',
            step: function (now) {
                _this.text(Math.ceil(now));
            }
        });
    });
    $(document).on('click', "a[target!='_blank']:not(.qrcode-signin)", function() {
        var _this = $(this);
        if( isPC() && theme.loading=='1' && _this.attr('href') && _this.attr('href').indexOf("#") != 0 && _this.attr('href').indexOf("java") != 0 && !_this.data('fancybox')  && !_this.data('commentid') && !_this.hasClass('nofx') ){
            var load = $('<div id="load-loading"></div>');
            $("body").prepend(load);
            load.animate({opacity:'1'},200,'swing').delay(2000).hide(300,function(){ load.remove() });
        }
    });
    // 点赞
    $(document).on('click', ".btn-like", function() {
        var t = $(this);
        if(t.data('action') == "post_like"){
            if (t.hasClass('liked')) {
                showAlert(JSON.parse('{"status":1,"msg":"'+localize.liked+'"}'));
            } else {
                var icop = t.children('.flex-column');
                t.addClass('liked');
                $.ajax({
                    type : 'POST',
                    url : theme.ajaxurl,
                    data : {
                        action: t.data('action'),
                        post_id: t.data("id"),
                        ticket: t.data("ticket")
                    },
                    success : function( data ){
                        var $am = $('<i class="iconfont icon-heart" style="color: #f12345;transform: scale(1) translateY(0);position: absolute;transition: .6s;opacity: 1;"></i>');
                        icop.prepend($am);
                        showAlert(JSON.parse('{"status":1,"msg":"'+localize.like+'"}'));
                        $('.like-count').html(data);
                        $am.addClass('home-like-hide');
                    },
                    error:function(){
                        showAlert(JSON.parse('{"status":4,"msg":"'+localize.networkerror+'"}'));
                    }
                });
            }
        }else{
            if (t.hasClass('disabled'))
                return false;
            var _delete = 0;
            var id = t.data("id");
            if (t.hasClass('liked')) {
                _delete = 1;
            }
            t.addClass('disabled');
            $.ajax({
                type : 'POST',
                url : theme.ajaxurl,
                data : {
                    action: t.data("action"),
                    post_id: t.data("id"),
                    post_type: t.data("post_type"),
                    delete: _delete,
                    ticket: t.data("ticket")
                },
                success : function( data ){
                    t.removeClass('disabled');
                    if(data.status==1){
                        $('.star-count-'+id).html(data.count);
                        if(_delete==1){
                            t.removeClass('liked');
                            t.find('.star-ico').removeClass('icon-collection').addClass('icon-collection-line');
                        }
                        else{
                            t.addClass('liked');
                            t.find('.star-ico').removeClass('icon-collection-line').addClass('icon-collection');
                        }
                        ioPopupTips(data.status, data.msg);
                        return false;
                    }
                    ioPopupTips(data.status, data.msg);
                },
                error:function(){
                    t.removeClass('disabled');
                    ioPopupTips(4, localize.networkerror );
                }
            });

        }
        return false;
    });
    // 卡片点赞
    $(document).on('click', '.home-like', function() {
        var _this = $(this);
        if (_this.hasClass('liked')) {
            showAlert(JSON.parse('{"status":3,"msg":"'+localize.liked+'"}'));
        } else {
            var id = _this.data("id");
            _this.addClass('liked');
            $.ajax({
                type : 'POST',
                url : theme.ajaxurl,
                data : {
                    action: "post_like",
                    post_id: id
                },
                success : function( data ){
                    var $am = $('<i class="iconfont icon-heart" style="color: #f12345;transform: scale(1) translateY(0);position: absolute;transition: .6s;opacity: 1;"></i>');
                    _this.prepend($am);
                    showAlert(JSON.parse('{"status":1,"msg":"'+localize.like+'"}'));
                    $(".home-like-"+id).html(data);
                    $am.addClass('home-like-hide');
                },
                error:function(){
                    showAlert(JSON.parse('{"status":4,"msg":"'+localize.networkerror+'"}'));
                }
            });
        }
        return false;
    });
    //未开启详情页计算访客方法
    $(document).on('click', 'a.is-views[data-id]', function() {
        $.ajax({
            type:"GET",
            url:theme.ajaxurl,
            data:{
                action:'io_postviews',
                postviews_id:$(this).data('id'),
            },
            cache:false,
        });
    });

    //夜间模式
    $(document).on('click', '.switch-dark-mode', function(event) {
        event.preventDefault();
        $('html').toggleClass('io-black-mode '+theme.defaultclass);
        switch_mode(true);
        $("#"+ $('.switch-dark-mode').attr('aria-describedby')).remove();
    });
    function switch_mode(set_cookie){
        var body = $('html');
        var switcher = $(".switch-dark-mode");
        var modeico = $(".mode-ico");
        var _tinymce_body = $("#post_content_ifr").contents().find('body');
        if(body.hasClass('io-black-mode')){
            _tinymce_body.addClass('io-black-mode');
            if(set_cookie) setCookie('io_night_mode',0,30);
            if(switcher.attr("data-original-title"))
                switcher.attr("data-original-title", localize.lightMode );
            else
                switcher.attr("title",localize.lightMode);
            modeico.removeClass("icon-night").addClass("icon-light");
            chartTheme = 'dark';
            setChartTheme();
        }
        else{
            _tinymce_body.removeClass('io-black-mode');
            if(set_cookie) setCookie('io_night_mode',1,30);
            if(switcher.attr("data-original-title"))
                switcher.attr("data-original-title",localize.nightMode);
            else
                switcher.attr("title",localize.nightMode);
            modeico.removeClass("icon-light").addClass("icon-night");
            chartTheme = '';
            setChartTheme();
        }
    }
    function themeChange() {
        try {
            var night = getCookie('io_night_mode');
            if (night === "0" || (!night && window.matchMedia("(prefers-color-scheme: dark)").matches)) {
                document.documentElement.classList.add("io-black-mode");
                document.documentElement.classList.remove(theme.defaultclass);
            } else {
                document.documentElement.classList.remove("io-black-mode");
                document.documentElement.classList.add(theme.defaultclass);
            }
            switch_mode(false);
        } catch (_) {}
    }
    const mediaQueryListDark = window.matchMedia("(prefers-color-scheme: dark)");
    mediaQueryListDark.addEventListener("change", themeChange);

    //返回顶部
    $(window).scroll(function () {
        if ($(this).scrollTop() >= 50) {
            $('#go-to-up').fadeIn(200);
            $('.big-header-banner').addClass('header-bg');
        } else {
            $('#go-to-up').fadeOut(200);
            $('.big-header-banner').removeClass('header-bg');
        }
    });
    $('.go-up').click(function () {
        $('body,html').animate({
            scrollTop: 0
        }, 500);
        return false;
    });

    //滑块菜单
    $('.slider_menu li:not(.anchor)').hover(function() {
        $(this).addClass("hover"),
            toTarget($(this).parent(),true,true)
    }, function() {
        $(this).removeClass("hover") ;
        var menu = $(this).parent('ul') ;
        window.setTimeout(function() {
            toTarget(menu,true,true)
        }, 50)
    });
    $.fn.tabToCenter = function (o) {
        var _this = $(this);

        if (!_this.length) return;
        var _active = o;
        if (!_active.length) return;
        var _this_w = _this.innerWidth();
        var _active_w = _active.innerWidth();
        var _this_scrollLeft = _this.scrollLeft();

        var _active_left = _active.position().left;

        var _x_dist = ~~((_this_scrollLeft + _active_left) - (_this_w / 2) + (_active_w / 2));
        _x_dist = _x_dist > 0 ? _x_dist : 0;
        _this.animate({
            scrollLeft: _x_dist
        }, 300)
    }
    $(document).on('click', '.tab-auto-scrollbar a', function (e) {
        $(this).closest('.tab-auto-scrollbar').tabToCenter($(this).parent('li'));
    });
    function intoSlider() {
        $(".slider_menu[sliderTab]").each(function() {
            var _this = $(this);
            if(!_this.hasClass('into')){
                var menu = _this.children("ul");
                menu.prepend('<li class="anchor" style="position:absolute;width:0;height:28px"></li>');
                var target = menu.find('.active').parent();
                if(0 < target.length){
                    menu.children(".anchor").css({
                        left: target.position().left + target.scrollLeft() + "px",
                        width: target.outerWidth() + "px",
                        height: target.height() + "px",
                        opacity: "1"
                    })
                }
                _this.addClass('into');
            }
        })
        if(is_function('Swiper')){
            var swiper_post = new Swiper(".swiper-post-module", {
                autoplay: {
                    disableOnInteraction: false,
                },
                lazy: {
                    loadPrevNext: true,
                },
                slidesPerView: 1,
                loop: true,
                pagination: {
                    el: ".swiper-pagination",
                    clickable: true,
                },
                navigation: {
                    nextEl: ".swiper-button-next",
                    prevEl: ".swiper-button-prev",
                }
            });
            var swiper_widgets = new Swiper(".swiper-widgets", {
                autoplay: {
                    disableOnInteraction: false,
                    delay: 5000,
                },
                effect : 'fade',
                thumbs: {
                    swiper: {
                        el: '.swiper-widgets-thumbs',
                        slidesPerView: "auto",
                        freeMode: true,
                        centerInsufficientSlides: true,
                    },
                    autoScrollOffset: 1,
                },
                on:{
                    init:function(swiper){
                        var slide=this.slides.eq(0);
                        slide.addClass('anim-slide');
                    },
                    transitionStart: function(){
                        for(var i=0;i<this.slides.length;i++){
                            var slide=this.slides.eq(i);
                            slide.removeClass('anim-slide');
                        }
                    },
                    transitionEnd: function(){
                        var slide=this.slides.eq(this.activeIndex);
                        slide.addClass('anim-slide');
                    },
                }
            });
            var swiper_term_content = new Swiper(".swiper-term-content", {
                nested:true,
                slidesPerView: "auto",
                freeMode: true,
                mousewheel: true,
                watchSlidesProgress: true,
                resistanceRatio: false
            });
        }
    }
    //粘性页脚
    function stickFooter() {
        var main_footer = $('.main-footer');
        main_footer.attr('style', '');
        if(main_footer.hasClass('footer-stick'))
        {
            var win_height          = jQuery(window).height(),
                footer_height       = main_footer.outerHeight(true),
                main_content_height = main_footer.position().top + footer_height ;
            if(win_height > main_content_height - parseInt(main_footer.css('marginTop'), 10))
            {
                main_footer.css({
                    marginTop: win_height - main_content_height
                });
            }
        }
    }


    $('#sidebar-switch').on('click',function(){
        $('body').removeClass('mini-sidebar');
        //$('.sidebar-nav .change-href').attr('href','javascript:;');
    });

    // Trigger Resizable Function
    let isMin = false,
        isMobileMin = false;
    function trigger_resizable(  ) {
        if( (theme.minNav == '1' && !isMin && 767.98<$(window).width() )||(!isMin && 767.98<$(window).width() && $(window).width()<1024) ){
            //$('#mini-button').removeAttr('checked');
            $('#mini-button').prop('checked', false);
            trigger_lsm_mini();
            isMin = true;
            if(isMobileMin){
                $('body').addClass('mini-sidebar');
                //$('.sidebar-nav .change-href').each(function(){$(this).attr('href',$(this).data('change'))});
                isMobileMin = false;
            }
        }
        else if( ( theme.minNav != '1')&&((isMin && $(window).width()>=1024) || ( isMobileMin && !isMin && $(window).width()>=1024 ) ) ){
            $('#mini-button').prop('checked', true);
            trigger_lsm_mini();
            isMin = false;
            if(isMobileMin){
                isMobileMin = false;
            }
        }
        else if($(window).width() < 767.98 && $('body').hasClass('mini-sidebar')){
            $('body').removeClass('mini-sidebar');
            //$('.sidebar-nav .change-href').attr('href','javascript:;');
            isMobileMin = true;
            isMin = false;
        }
    }
    // sidebar-menu-inner收缩展开
    $(document).on('click','.sidebar-menu-inner .sidebar-more,.sidebar-menu-inner a',function(){//.sidebar-menu-inner a //.has-sub a  
        showMenu($(this));
    });
    function showMenu(_this){
        if (!$('body').hasClass('mini-sidebar')) {//菜单栏没有最小化
            _this.parent("li").siblings("li").removeClass('sidebar-show').children('ul').slideUp(200);
            if (_this.nextAll('ul').css('display') == "none") { //展开
                //展开未展开
                // $('.sidebar-item').children('ul').slideUp(300);
                _this.nextAll('ul').slideDown(200);
                _this.parent('li').addClass('sidebar-show').siblings('li').removeClass('sidebar-show');
            }else{ //收缩
                //收缩已展开
                _this.nextAll('ul').slideUp(200);
                //$('.sidebar-item.sidebar-show').removeClass('sidebar-show');
                _this.parent('li').removeClass('sidebar-show');
            }
        }
    }
    //菜单栏最小化
    $('#mini-button').on('click',function(){
        if(!$('.sidebar-nav').hasClass())
            $('body').addClass('animate-nav');
        trigger_lsm_mini();
    });
    function trigger_lsm_mini(){
        if ($('.header-mini-btn input[type="checkbox"]').prop("checked")) {
            $('body').removeClass('mini-sidebar');
            $('.sidebar-menu ul ul').css("display", "none");
        }else{
            $('.sidebar-item.sidebar-show').removeClass('sidebar-show');
            $('.sidebar-menu ul').removeAttr('style');
            $('body').addClass('mini-sidebar');
        }
    }
    //显示2级悬浮菜单
    $(document).on('mouseenter','.mini-sidebar .sidebar-nav .sidebar-menu ul:first>li,.mini-sidebar .sidebar-nav .flex-bottom ul:first>li',function(){
        var offset = 0;
        var _this = $(this);
        if(_this.parents('.flex-bottom').length!=0)
            offset = -4;
        var sidebar_second = $(".sidebar-popup.second");
        if(sidebar_second.length == 0){
            ($("body").append("<div class='second sidebar-popup sidebar-menu-inner text-sm'><div></div></div>"))
            sidebar_second = $(".sidebar-popup.second");
        }
        $(".sidebar-popup.second>div").html(_this.html());
        sidebar_second.show();
        var top = _this.offset().top - $(window).scrollTop() + offset;
        var d = $(window).height() - $(".sidebar-popup.second>div").height();
        if(d - top <= 0 ){
            top  = d >= 0 ?  d - 8 : 0;
        }
        sidebar_second.stop().animate({"top":top}, 50);
        _this.one('mouseleave', function() {
            if($('body').hasClass('mini-sidebar') && $('.sidebar-scroll').innerHeight() > $('.sidebar-menu').innerHeight()){
                $('.mini-sidebar .sidebar-nav .sidebar-menu').one('mouseleave', function() {sidebar_second.hide();})
            }else{
                sidebar_second.hide();
            }
        });
    });
    $(document).on('mouseenter','.mini-sidebar .sidebar-nav .slimScrollBar,.second.sidebar-popup',function(){
        var sidebar_second = $(".sidebar-popup.second");
        sidebar_second.show();
        $(this).one('mouseleave', function() {
            sidebar_second.hide();
        });
    });

    $(document).on('click', '.ajax-cm-home .ajax-cm', function(event) {
        event.preventDefault();
        var t = $(this);
        var id = t.data('id');
        var box = $(t.attr('href')).children('.site-list');
        if( box.children('.url-card').length==0 ){
            t.addClass('disabled');
            $.ajax({
                url: theme.ajaxurl,
                type: 'POST',
                dataType: 'html',
                data : {
                    action: t.data('action'),
                    term_id: id,
                },
                cache: true,
            })
                .done(function(response) {
                    if (response.trim()) {
                        var url = $(response);
                        box.html(url);
                        if(isPC()) url.find('[data-toggle="tooltip"]').tooltip({ trigger: 'hover' });
                    } else {
                    }
                    t.removeClass('disabled');
                })
                .fail(function() {
                    t.removeClass('disabled');
                })
        }
    });

    //首页 热门/随机/最新 切换
/*    $(document).on('click', '.ajax-home-hot-list:not(.load)', function(event) {
        event.preventDefault();
        var t = $(this);
        var box = $(t.attr('href')).children('.ajax-list-body');
        t.addClass('disabled');
        $.ajax({
            url: theme.ajaxurl,
            type: 'POST',
            dataType: 'html',
            data : $.extend({
                action: t.data('action'),
            }, t.data('datas')),
            cache: true,
        })
            .done(function(response) {
                if (response.trim()) {
                    var url = $(response);
                    box.html(url);
                    if(isPC()) url.find('[data-toggle="tooltip"]').tooltip({ trigger: 'hover' });
                    t.addClass('load');
                }
                t.removeClass('disabled');
            })
            .fail(function() {
                t.removeClass('disabled');
            })
    });*/

    $(document).on('click', 'a.tab-widget-link:not(.load)',function(event) {
        event.preventDefault();
        var t = $(this);
        t.addClass('disabled');
        $.ajax({
            url: theme.ajaxurl,
            type: 'POST',
            dataType: 'html',
            data : $.extend({
                action: t.data('action'),
            }, t.data('datas')),
        })
            .done(function(response){
                $(t.attr('href') + ' .widget-item' ).html(response);
                t.addClass('load');
                t.removeClass('disabled');
            })
            .fail(function() {
                t.removeClass('disabled');
            })
    });

    // 非ajax加载时切换选项卡
    $(document).on('click','a.tab-noajax',function(ev) {
        var _this = $(this);
        var url = _this.data('link');
        if(url)
            _this.parents('.d-flex.flex-fill.flex-tab').children('.btn-move.tab-move').show().attr('href', url);
        else
            _this.parents('.d-flex.flex-fill.flex-tab').children('.btn-move.tab-move').hide();
    });
    //登录
    $(document).on('click', '.login-btn-action', function () {
        window.location.href = theme.loginurl;
        window.location.reload;
    });
    //首页tab模式请求内容
    $(document).on('click', '.ajax-list-home a:not(.load)', function(event) {
        event.preventDefault();
        loadHomeAjax( $(this), $($(this).attr('href')).children('.ajax-list-body'),$(this).parents('li').data() );
    });
    function loadHomeAjax(t, body,data){
        t.addClass('disabled');
        $.ajax({
            url: theme.ajaxurl,
            type: 'GET',
            dataType: 'html',
            data : data,
            cache: true,
        })
            .done(function(response) {
                if (response.trim()) {
                    body.html(response);
                    if(isPC()) $('.ajax-url [data-toggle="tooltip"]').tooltip({ trigger: 'hover' });
                    t.addClass('load');
                }
                t.removeClass('disabled');
            })
            .fail(function() {
                t.removeClass('disabled');
            })
    }

    // 自定义模块-----------------
    $(".add-link-form").on("submit", function() {
        var siteName = $(".site-add-name").val(),
            siteUrl = $(".site-add-url").val();
        addSiteList({
            id: +new Date,
            name: siteName,
            url: siteUrl
        });
        this.reset();
        this.querySelector("input").focus();
        $(this).find(".btn-close-fm").click();
    });
    var isEdit = false;
    $('.customize-menu .btn-edit').click(function () {
        if(isEdit){
            $('.url-card .remove-site,#add-site').hide();
            $('.url-card .remove-site,.add-custom-site').hide();
            $('.url-card .remove-cm-site').hide();
            $('.customize-sites').removeClass('edit');
            ioSortable();
            $('.customize-menu .btn-edit').html(localize.editBtn);
        }else{
            $('.url-card .remove-site,#add-site').show();
            $('.url-card .remove-site,.add-custom-site').show();
            $('.url-card .remove-cm-site').show();
            $('.customize-sites').addClass('edit');
            ioSortable();
            $('.customize-menu .btn-edit').html(localize.okBtn);
        }
        isEdit = !isEdit;
    });
    function addSiteList(site){
        var sites = getItem("myLinks");
        //判断是否重复
        for (var i = 0; i < sites.length; i++) {
            if(sites[i].url==site.url)
            {
                showAlert(JSON.parse('{"status":4,"msg":"'+localize.urlExist+'"}'));
                return;
            }
        }
        sites.unshift(site);
        addSite(site,false,false);
        setItem(sites,"myLinks");
    }
    function addSite(site,isLive,isHeader) {
        if(!isLive) $('.customize_nothing').remove();
        else $('.customize_nothing_click').remove();
        var url_f,matches = site.url.match(/^(?:https?:\/\/)?((?:[-A-Za-z0-9]+\.)+[A-Za-z]{2,6})/);
        if (!matches || matches.length < 2) url_f=site.url;
        else {
            url_f=matches[0];
            if(theme.urlformat == '1')
                url_f = matches[1];
        }
        var newSite = $('<div class="url-card  col-6 '+theme.classColumns+' col-xxl-10a">'+
            '<div class="url-body mini"><a href="'+site.url+'" target="_blank" class="card new-site mb-3 site-'+site.id+'" data-id="'+site.id+'" data-url="'+site.url+'" data-toggle="tooltip" data-placement="bottom" title="'+site.name+'" rel="external nofollow">'+
            '<div class="card-body" style="padding:0.4rem 0.5rem;">'+
            '<div class="url-content d-flex align-items-center">'+
            '<div class="url-img rounded-circle mr-2 d-flex align-items-center justify-content-center">'+
            '<img src="' + theme.icourl + url_f + theme.icopng + '">'+
            '</div>'+
            '<div class="url-info flex-fill">'+
            '<div class="text-sm overflowClip_1">'+
            '<strong>'+site.name+'</strong>'+
            '</div>'+
            '</div>'+
            '</div>'+
            '</div>'+
            '</a></div>' +
            '<a href="javascript:;" class="text-center remove-site" data-id="'+site.id+'" style="display: none"><i class="iconfont icon-close-circle"></i></a>'+
            '</div>');
        if(isLive){
            if(isHeader)
                $(".my-click-list").prepend(newSite);
            else
                $(".my-click-list").append(newSite);
            newSite.children('.remove-site').on("click",removeLiveSite);
        } else {
            $("#add-site").before(newSite);
            newSite.children('.remove-site').on("click",removeSite);
        }
        if(isEdit)
            newSite.children('.remove-site').show();
        if(isPC()) $('.new-site[data-toggle="tooltip"]').tooltip({ trigger: 'hover' });
    }
    function getItem(key) {
        var a = window.localStorage.getItem(key);
        return a ? a = JSON.parse(a) : [];
    }
    function setItem(sites,key) {
        window.localStorage.setItem(key, JSON.stringify(sites));
    }
    function intoSites(isLive) {
        var sites = getItem( isLive ? "livelists" : "myLinks" );
        if(sites.length && !isLive && !$("#add-site")[0]){
            $(".customize_nothing.custom-site").children(".nothing").html('<a href="javascript:;" class="add-new-custom-site" data-action="add_custom_urls" data-term_name="我的导航" data-urls="'+Base64.encode(JSON.stringify( sites ))+'" >您已登录，检测到您的设备上有数据，点击<strong style="color:#db2323">同步到服务器</strong>。</a>');
            return;
        }
        if (sites.length) {
            for (var i = 0; i < sites.length; i++) {
                addSite(sites[i],isLive,false);
            }
        }
    }
    function removeSite() {
        var id = $(this).data("id"),
            sites = getItem("myLinks");
        for (var i = 0; i < sites.length; i++){
            if ( parseInt(sites[i].id) === parseInt(id)) {
                sites.splice(i, 1);
                break;
            }
        }
        setItem(sites,"myLinks");
        $(this).parent().remove();
    }
    function removeLiveSite() {
        var id = $(this).data("id"),
            sites = getItem("livelists");
        for (var i = 0; i < sites.length; i++){
            if ( parseInt(sites[i].id) === parseInt(id)) {
                sites.splice(i, 1);
                break;
            }
        }
        setItem(sites,"livelists");
        $(this).parent().remove();
    }
    $(document).on('click', '.add-new-custom-site', function(event) {
        var t = $(this);
        $.ajax({
            url: theme.ajaxurl,
            type: 'POST',
            dataType: 'json',
            data : t.data(),
        })
            .done(function(response) {
                showAlert(response);
            })
            .fail(function() {
                showAlert(JSON.parse('{"status":4,"msg":"'+localize.networkerror+'"}'));
            })
    });
    $(".add-custom-site-form").on("submit", function() {
        var t = $(this);
        var tt = this;
        var url = t.find("input[name=url]").val();
        var name = t.find("input[name=url_name]").val();
        var term_id = t.find('input:radio:checked').val();
        var term_name = t.find('input[name=term_name]').val();
        if(term_name=='' && term_id==undefined){
            showAlert(JSON.parse('{"status":3,"msg":"'+localize.selectCategory+'"}'));
            return false;
        }
        $.ajax({
            url: theme.ajaxurl,
            type: 'POST',
            dataType: 'json',
            data : t.serialize()+"&action=add_custom_url",
        })
            .done(function(response) {
                if(response.status !=1){
                    showAlert(response);
                    return;
                }
                var url_f,matches = url.match(/^(?:https?:\/\/)?((?:[-A-Za-z0-9]+\.)+[A-Za-z]{2,6})/);
                if (!matches || matches.length < 2) url_f=url;
                else {
                    url_f=matches[0];
                    if(theme.urlformat == '1')
                        url_f = matches[1];
                }
                var id = response.id;
                var newSite = $('<div id="url-'+id+'" class="url-card sortable col-6 '+theme.classColumns+' col-xxl-10a">'+
                    '<div class="url-body mini"><a href="'+url+'" target="_blank" class="card new-site mb-3 site-'+id+'" data-id="'+id+'" data-url="'+url+'" data-toggle="tooltip" data-placement="bottom" title="'+name+'" rel="external nofollow">'+
                    '<div class="card-body" style="padding:0.4rem 0.5rem;">'+
                    '<div class="url-content d-flex align-items-center">'+
                    '<div class="url-img rounded-circle mr-2 d-flex align-items-center justify-content-center">'+
                    '<img src="' + theme.icourl + url_f + theme.icopng + '">'+
                    '</div>'+
                    '<div class="url-info flex-fill">'+
                    '<div class="text-sm overflowClip_1">'+
                    '<strong>'+name+'</strong>'+
                    '</div>'+
                    '</div>'+
                    '</div>'+
                    '</div>'+
                    '</a></div>' +
                    '<a href="javascript:;" class="text-center remove-cm-site" data-action="delete_custom_url" data-id="'+id+'"><i class="iconfont icon-close-circle"></i></a>'+
                    '</div>');
                $(".add-custom-site[data-term_id="+term_id+"]").before(newSite);
                tt.reset();
                tt.querySelector("input").focus();
                t.find(".btn-close-fm").click();
                showAlert(JSON.parse('{"status":1,"msg":"'+localize.addSuccess+'"}'));
            })
            .fail(function() {
                showAlert(JSON.parse('{"status":4,"msg":"'+localize.networkerror+'"}'));
                return;
            })
    });
    $(document).on("click",'.url-card .remove-cm-site', function(event){
        var t = $(this);
        t.addClass('disabled');
        $.ajax({
            url: theme.ajaxurl,
            type: 'POST',
            dataType: 'json',
            data : t.data(),
        })
            .done(function(response) {
                if(response.status == 1){
                    t.parent().remove();
                }
                t.removeClass('disabled');
                showAlert(response);
            })
            .fail(function() {
                t.removeClass('disabled');
                showAlert(JSON.parse('{"status":4,"msg":"'+localize.networkerror+'"}'));
            })
    });
    function ioSortable() {
        if($('.customize-sites').hasClass('edit')){
            if(isPC()) $('.customize-sites .new-site[data-toggle="tooltip"]').tooltip('disable');
            //$('.customize-sites').find('a').attr('href','javascript:void(0)');
            $('.customize-sites .site-list').sortable({
                items: '.sortable',
                containment: ".main-content",
                //'placeholder': "ui-state-highlight",
                update : function(e, ui) {
                    $('.customize-sites .site-list').sortable('disable');
                    var term_id = $(this).data('term_id');
                    var order   = $(this).sortable('serialize');


                    var queryData = { "action": "update_custom_url_order", "term_id" : term_id, "order" : order };
                    $.ajax({
                        url: theme.ajaxurl,
                        type: 'POST',
                        data: queryData,
                        cache: false,
                        dataType: "json",
                        success: function(data){
                            if(data.status != 1){
                                showAlert(data);
                            }
                            $('.customize-sites .site-list').sortable('enable');
                        },
                        error: function(html){
                            $('.customize-sites .site-list').sortable('enable');
                            showAlert(JSON.parse('{"status":4,"msg":"'+localize.networkerror+'"}'));
                        }
                    });

                }
            });
        }else{
            if(isPC()) $('.customize-sites .new-site[data-toggle="tooltip"]').tooltip('enable');
            //$('.customize-sites').find('a').attr('href',$(this).data('url'));
            $( ".customize-sites .site-list" ).sortable( "destroy" );
        }

    }

    $("input[name=term_name]").focus(function(){
        var this_input = $("input[name=term_id]");
        this_input.prop('checked', false);
    });
    $('.form_custom_term_id').on("click", function(event){
        $("input[name=term_name]").val("");
    });
    $(document).on('click', '.url-card a.card', function(event) {
        var _this = $(this);
        var site = {
            id: _this.data("id"),
            name: _this.find("strong").html(),
            url: _this.data("url")
        };
        if(site.url==="")
            return;
        var liveList = getItem("livelists");
        var isNew = true;
        for (var i = 0; i < liveList.length; i++){
            if (liveList[i].name === site.name) {
                isNew = false;
            }
        }
        if(isNew){
            var maxSite = theme.customizemax;
            if(liveList.length > maxSite-1){
                $(".my-click-list .site-"+liveList[maxSite-1].id).parent().remove();
                liveList.splice(maxSite-1, 1);
            }
            addSite(site,true,true);
            liveList.unshift(site);
            setItem(liveList,"livelists");
        }
    });
    // 搜索模块 -----------------------
    function intoSearch() {
        var page = $('.s-type-list.big label').data('page');
        var searchlist = window.localStorage.getItem("searchlist_"+page);
        var searchlistmenu = window.localStorage.getItem("searchlistmenu_"+page);
        if(searchlist){
            var type_checked = $(".hide-type-list input#"+searchlist);
            type_checked.prop('checked', true);
            window.setTimeout(function() {
                type_checked.closest('.tab-auto-scrollbar').tabToCenter(type_checked.parent('li'));
            }, 100);
            $(".hide-type-list input#m_"+searchlist).prop('checked', true);
        }
        if(searchlistmenu){
            $('.s-type-list.big label').removeClass('active');
            $(".s-type-list [data-id="+searchlistmenu+"]").addClass('active');
        }
        toTarget($(".s-type-list.big"),false,false);
        $('.big.tab-auto-scrollbar').tabToCenter($('.big.tab-auto-scrollbar label.active'));
        $('.hide-type-list .s-current').removeClass("s-current");
        $('.hide-type-list input:radio[name="type"]:checked').parents(".search-group").addClass("s-current");
        $('.hide-type-list input:radio[name="type2"]:checked').parents(".search-group").addClass("s-current");
        $(".super-search-fm").attr("action",$('.hide-type-list input:radio:checked').val());
        $(".search-key").attr("placeholder",$('.hide-type-list input:radio:checked').data("placeholder"));
        if(searchlist=='type-zhannei' || searchlist=='type-big-zhannei'){
            $(".search-key").attr("zhannei","true");
        }
    }
    $(document).on('click', '.s-type-list label', function(event) {
        //event.preventDefault();
        var _this = $(this);
        $('.s-type-list.big label').removeClass('active');
        _this.addClass('active');
        window.localStorage.setItem("searchlistmenu_"+_this.data("page"), _this.data("id"));
        var parent = _this.parents(".s-search");
        parent.find('.search-group').removeClass("s-current");
        parent.find('#'+_this.attr("for")).parents(".search-group").addClass("s-current");
        _this.closest('.tab-auto-scrollbar').tabToCenter(_this);
        toTarget(_this.parents(".s-type-list"),false,false);
    });
    $('.hide-type-list .search-group input').on('click', function() {
        var _this = $(this);
        var parent = _this.parents(".s-search");
        window.localStorage.setItem("searchlist_"+_this.data("page"), _this.attr("id").replace("m_",""));
        parent.children(".super-search-fm").attr("action",_this.val());
        parent.find(".search-key").attr("placeholder",_this.data("placeholder"));

        if(_this.attr('id')=="type-zhannei" || _this.attr('id')=="type-big-zhannei" || _this.attr('id')=="m_type-zhannei")
            parent.find(".search-key").attr("zhannei","true");
        else
            parent.find(".search-key").attr("zhannei","");
        _this.closest('.tab-auto-scrollbar').tabToCenter(_this.parent());
        parent.find(".search-key").select();
        parent.find(".search-key").focus();
    });
    $(document).on("submit", ".super-search-fm", function() {
        var _this = $(this);
        var key = encodeURIComponent(_this.find(".search-key").val());
        if(key == "")
            return false;
        else{
            var search_url = _this.attr("action");
            if(search_url.indexOf("%s%") != -1){
                window.open( search_url.replace("%s%",key) );
            }else{
                window.open( search_url + key);
            }
            return false;
        }
    });
    function getSmartTipsGoogle(value,parents) {
        $.ajax({
            type: "GET",
            url: "//suggestqueries.google.com/complete/search?client=firefox&callback=iowenHot",
            async: true,
            data: { q: value },
            dataType: "jsonp",
            jsonp: "callback",
            success: function(res) {
                var list = parents.children(".search-smart-tips");
                list.children("ul").text("");
                tipsList = res[1].length;
                if (tipsList) {
                    for (var i = 0; i < tipsList; i++) {
                        list.children("ul").append("<li>" + res[1][i] + "</li>");
                        list.find("li").eq(i).click(function() {
                            var keyword = $(this).html();
                            parents.find(".smart-tips.search-key").val(keyword);
                            parents.children(".super-search-fm").submit();
                            list.slideUp(200);
                        });
                    };
                    list.slideDown(200);
                } else {
                    list.slideUp(200);
                }
            },
            error: function(res) {
                tipsList = 0;
            }
        })
    }
    function getSmartTipsBaidu(value,parents) {
        $.ajax({
            type: "GET",
            //url: "//sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su?cb=iowenHot",
            url: "//suggestion.baidu.com/su?p=3&cb=iowenHot",
            async: true,
            data: { wd: value },
            dataType: "jsonp",
            jsonp: "cb",
            success: function(res) {
                var list = parents.children(".search-smart-tips");
                list.children("ul").text("");
                tipsList = res.s.length;
                if (tipsList) {
                    for (var i = 0; i < tipsList; i++) {
                        list.children("ul").append("<li>" + res.s[i] + "</li>");
                        list.find("li").eq(i).click(function() {
                            var keyword = $(this).html();
                            parents.find(".smart-tips.search-key").val(keyword);
                            parents.children(".super-search-fm").submit();
                            list.slideUp(200);
                        });
                    };
                    list.slideDown(200);
                } else {
                    list.slideUp(200);
                }
            },
            error: function(res) {
                tipsList = 0;
            }
        })
    }
    var listIndex = -1;
    var parent;
    var tipsList = 0;
    var isZhannei = false;
    $('.smart-tips.search-key').off().on({
        compositionstart: function() {
            $(this).attr('data-status', false);
        },
        compositionend: function() {
            $(this).attr('data-status', true);
        },
        blur: function() {
            parent = '';
            $(".search-smart-tips").delay(150).slideUp(200);
        },
        focus: function() {
            var _this = $(this);
            isZhannei = _this.attr('zhannei')!='';
            parent = _this.parents('#search');
            if (_this.attr('data-status') == 'true' && _this.val() && !isZhannei) {
                switch(theme.hotWords) {
                    case "baidu":
                        getSmartTipsBaidu(_this.val(),parent);
                        break;
                    case "google":
                        getSmartTipsGoogle(_this.val(),parent);
                        break;
                    default:
                }
            }
        },
        keyup: function(e) {
            var _this = $(this);
            isZhannei = _this.attr('zhannei')!='';
            parent = _this.parents('#search');
            if (_this.attr('data-status') == 'true' && _this.val()) {
                if (e.keyCode == 38 || e.keyCode == 40 || isZhannei) {
                    return;
                }
                switch(theme.hotWords) {
                    case "baidu":
                        getSmartTipsBaidu(_this.val(),parent);
                        break;
                    case "google":
                        getSmartTipsGoogle(_this.val(),parent);
                        break;
                    default:
                }
                listIndex = -1;
            } else {
                $(".search-smart-tips").slideUp(200);
            }
        },
        keydown: function(e) {
            var _this = $(this);
            if(_this.attr('zhannei')!='')
                return;
            parent = _this.parents('#search');
            if (e.keyCode === 40) {
                listIndex === (tipsList - 1) ? listIndex = 0 : listIndex++;
                parent.find(".search-smart-tips ul li").eq(listIndex).addClass("current").siblings().removeClass("current");
                var hotValue = parent.find(".search-smart-tips ul li").eq(listIndex).html();
                parent.find(".smart-tips.search-key").val(hotValue);
            }
            if (e.keyCode === 38) {
                if (e.preventDefault) {
                    e.preventDefault();
                }
                if (e.returnValue) {
                    e.returnValue = false;
                }
                listIndex === 0 || listIndex === -1 ? listIndex = (tipsList - 1) : listIndex--;
                parent.find(".search-smart-tips ul li").eq(listIndex).addClass("current").siblings().removeClass("current");
                var hotValue = parent.find(".search-smart-tips ul li").eq(listIndex).html();
                parent.find(".smart-tips.search-key").val(hotValue);
            }
        }
    });
    $('.nav-login-user.dropdown----').hover(function(){
        var _this = $(this);
        if(!_this.hasClass('show'))
            _this.children('a').click();
    },function(){
        //$(this).removeClass('show');
        //$(this).children('a').attr('aria-expanded',false);
        //$(this).children('.dropdown-menu').removeClass('show');
    });
    $('#add-new-sites-modal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var modal = $(this);
        modal.find('[name="term_id"]').val(  button.data('terms_id') );
        modal.find('[name="url"]').val(  button.data('new_url') );
        modal.find('[name="url_name"]').val('');
        modal.find('[name="url_summary"]').removeClass('is-invalid').val('');
        button.data('new_url','');
        var _url = modal.find('[name="url"]').val();
        if(_url!=''){
            getUrlInfo(_url,modal);
            urlStartValue = _url;
        }
    });
    var urlStartValue = '';
    $('#modal-new-url').on('blur',function(){
        var t = $(this);
        if(t.val()!=''){
            if(isURL(t.val())){
                if(urlStartValue!=t.val()){
                    urlStartValue = t.val();
                    getUrlInfo(t.val(),$('.add_new_sites_modal'));
                }
            }else{
                showAlert(JSON.parse('{"status":4,"msg":"URL 无效！"}'));
            }
        }
    });
    $('#modal-new-url-summary').on('blur',function(){
        var t = $(this);
        if(t.val()!=''){
            t.removeClass('is-invalid');
        }
    });
/*    function getUrlInfo(_url,modal){
        $('#modal-new-url-ico').show();
        $.post("//apiv2.iotheme.cn/webinfo/get.php", { url: _url ,key: theme.apikey },function(data,status){
            if(data.code==0){
                $('#modal-new-url-ico').hide();
                $("#modal-new-url-summary").addClass('is-invalid');
            }
            else{
                $('#modal-new-url-ico').hide();
                if(data.site_title=="" && data.site_description==""){
                    $("#modal-new-url-summary").addClass('is-invalid');
                }else{
                    modal.find('[name="url_name"]').val(data.site_title);
                    modal.find('[name="url_summary"]').val(data.site_description);
                }
            }
        }).fail(function () {
            $('#modal-new-url-ico').hide();
            $(".refre_msg").html('<i class="iconfont icon-tishi"></i>'+localize.timeout).show(200).delay(4000).hide(200);
        });
    }*/
    $(document).on('click','a.sidebar-rand-post',function(){
        load_rand_post($(this).data());
    });

    //复制
    $(document).on('click', "[data-clipboard-text]", function (e) {
        var _this = $(this);
        var text = _this.data('clipboard-text');
        if (_this.hasClass('down_count')) {
            $.ajax({
                type: "POST",
                url: theme.ajaxurl,
                data: _this.data(),
                success: function (n) {
                    $('.down-count-text').html(n);
                }
            });
        }
        copyText(text, function () {
            alert(localize.extractionCode);
        }, function () {
        }, this);
    });
    loadFunc(function() {
        if($('a.sidebar-rand-post')[0]){
            $('a.sidebar-rand-post').click();
        }
    });

    $(document).on('click', '.password-show-btn', function () {
        var _this = $(this);
        var _ico = _this.find('.iconfont');
        var _input = _this.siblings('input');
        if (_this.data('show') == "0") {
            _ico.removeClass("icon-chakan-line");
            _ico.addClass("icon-hide-line");
            _input.attr('type', 'text');
            _this.data('show',1);
        } else {
            _ico.removeClass("icon-hide-line");
            _ico.addClass("icon-chakan-line");
            _input.attr('type', 'password');
            _this.data('show',0);
        }
    });
    $('.count-tips input[type="text"]').off().on({
        compositionstart: function () {
            $(this).attr('data-status', false);
        },
        compositionend: function () {
            $(this).attr('data-status', true);
            change_input(this);
        },
        input: function () {
            change_input(this);
        }
    });

    $(document).on('click', '[data-for]', function () {
        var _this = $(this);
        var _tt;
        var _for = _this.data('for');
        var _f = _this.parents('form');
        var _v = _this.data('value');
        var _group = $(_this.parents('[for-group]')[0]);
        if (_group.length) {
            _group.find('[data-for="' + _for + '"]').removeClass('active');
        } else {
            _this.siblings().removeClass('active');
        }

        _this.addClass('active');
        _tt = _this.html();
        _f.find("input[name='" + _for + "']").val(_v).trigger('change');

        _f.find("span[name='" + _for + "']").html(_tt);
    })


    $('.only-submit #submit').click(function() {
        var _this = $(this);
        var _form = _this.closest('form');
        captcha_ajax(_this, '', function (n) {
            if (n.status == 1) {
                _form[0].reset();
                _form.find('.image-captcha').click();
            }
        });
        return false;
    })
    $(document).on('click',"#wp_login_form #submit",function() {
        var _this = $(this);
        captcha_ajax(_this, '', function(m) {
            if(m.status == 1){
                if(!m.goto){
                    window.location.reload();
                }
            }
        });
        return false;
    });
    $(document).on('click',".open-login",function() {
        var _this = $(this);
        if ($('#user_agreement')[0] && !$('#user_agreement').is(':checked')) {
            ioPopupTips(2, localize.userAgreement);
            return false;
        }
    });
    $(document).on('click','.user-reset-password',function(){
        var _this = $(this);
        var url = _this.attr('href');
        var content = _this.closest('.modal-content');
        content.css({
            'height': content.outerHeight()
        }).animate({
            'height': '220px'
        },200);
        content.find('.io-modal-content').html('');
        content.find('.loading-anim').fadeIn(200);
        $.get(url, null, function (data, status) {
            content.find('.io-modal-content').html(data).slideDown(200, function () {
                content.find('.loading-anim').fadeOut(200);
                var height = $(this).outerHeight();
                content.animate({
                    'height': height,
                }, 200, 'swing', function () {
                    content.css({
                        'height': '',
                        'overflow': '',
                        'transition': ''
                    })
                });
            });
            $('[captcha-type]')[0] && CaptchaInit();
        });
        return false;
    });
    $('.user-bind-modal').on('click',function(){
        var t = $(this);
        var url = t.attr('href');
        var modal = ioModal(t);
        $.get(url, null, function (data, status) {
            modal.find('.io-modal-content').html(data).slideDown(200, function () {
                modal.find('.loading-anim').fadeOut(200);
                var height = $(this).outerHeight();
                var content = modal.find('.modal-content');
                content.animate({
                    'height': height,
                }, 200, 'swing', function () {
                    content.css({
                        'height': '',
                        'overflow': '',
                        'transition': ''
                    })
                });
            });
            $('[captcha-type]')[0] && CaptchaInit();
        });
        return false;
    });
    $(document).on("click",".user-bind-from .btn-submit",function() {
        var _this = $(this);
        var content = _this.closest('.modal-content');
        captcha_ajax(_this, '', function (n) {
            if (n.html) {
                _this.closest('.io-modal-content').html(n.html).slideDown(200, function () {
                    var height = $(this).outerHeight();
                    content.animate({
                        'height': height,
                    }, 200, 'swing', function () {
                        content.css({
                            'height': '',
                            'overflow': '',
                            'transition': ''
                        })
                    });
                });
                $('[captcha-type]')[0] && CaptchaInit();
            }
        });
        return false;
    });
    $(document).on("click", ".io-ajax-modal-get", function () {
        var t = $(this);
        var url = t.attr('href');
        if (!url) {
            url = t.data('href');
        }
        var modal = ioModal(t);
        $.get(url, null, function (data, status) {
            modal.find('.io-modal-content').html(data).slideDown(200, function () {
                modal.find('.loading-anim').fadeOut(200);
                var height = $(this).outerHeight();
                var content = modal.find('.modal-content');
                content.animate({
                    'height': height,
                }, 200, 'swing', function () {
                    content.css({
                        'height': '',
                        'overflow': '',
                        'transition': ''
                    })
                });
            });
            $('.modal .dependency-box').dependency();
            if ($('.initiate-pay')[0] && !window.load_io_pay) {
                window.load_io_pay = true;
                $.getScript(theme.uri+"/iopay/assets/js/pay.js");
            }
        });
        return false;
    });
    $(document).on("click", ".modal .io-ajax-price-get", function () {
        var t = $(this);
        var url = t.attr('href');
        if (!url) {
            url = t.data('href');
        }
        var b = t.parent();
        if (b.hasClass('disabled') || t.attr('disabled')) {
            return false;
        }
        b.children().attr('disabled', false);
        t.attr('disabled', true);
        b.addClass('disabled');
        var p = t.closest('form');
        var content = p.find(t.data('target'));
        var loading = '<div class="d-flex align-items-center justify-content-center bg-o-muted position-absolute io-radius h-100 w-100"><i class="iconfont icon-loading icon-spin icon-2x"></i></div>';
        content.append(loading);
        $.get(url, null, function (data, status) {
            var _t = $(data);
            content.html(_t);
            _t[0].click();
            b.removeClass('disabled');
        });
        return false;
    });
    $(document).on("input", ".get-ajax-custom-product-val", debounce(function () {
        var t = $(this);
        url = t.data('href');
        if (t.hasClass('disabled')) {
            return false;
        }
        t.addClass('disabled');
        var p = t.closest('form');
        var content = p.find(t.data('target'));
        var hh = '<i class="iconfont icon-point"></i>';
        var loading = '<i class="iconfont icon-loading icon-spin"></i>';
        content.html(loading);
        $.get(url, p.serializeObject(), function (data, status) {
            if (data.msg) {
                alert.status = data.status?data.status:(data.error ? 4 : 1);
                alert.msg = data.msg;
                showAlert(alert);
                content.html(hh);
            } else {
                content.html(data);
            }
            t.removeClass('disabled');
        });
        return false;
    },800));
    $(document).on("click", ".io-ajax-modal", function () {
        var t   = $(this);
        var modal = ioModal(t);
        $.ajax({
            type : 'POST',
            url : theme.ajaxurl,
            data : t.data(),
            success : function( data ){
                modal.find('.io-modal-content').html(data).slideDown(200, function () {
                    modal.find('.loading-anim').fadeOut(200);
                    var height = $(this).outerHeight();
                    var content = modal.find('.modal-content');
                    content.animate({
                        'height': height,
                    }, 200, 'swing', function () {
                        content.css({
                            'height': '',
                            'overflow': '',
                            'transition': ''
                        })
                    });
                });
            },
            error: function () {
                modal.modal('hide');
                showAlert(JSON.parse('{"status":4,"msg":"'+localize.networkerror+'"}'));
            }
        });
        return false;
    });
    $(document).on("input propertychange","#user_email",function(){
        if($(this).val().length > 4)
            $(".verification").slideDown();
    });
    $(document).on("click", ".btn-token", function() {
        var t = $(this);
        var p = t.closest('form');
        if (t.attr('disabled')) {
            return false;
        }
        var email = p.find('#user_email');
        if (!email[0]) {
            email = p.find('.mm_mail');
        }
        countdown = 60;
        var btn = p.find(".btn-token");
        var _text = t.html();
        var token_submit = function () {
            captcha_ajax(t, '', function (n) {
                email.attr("readonly", "readonly");
                if (n.status == 1) {
                    settime();
                } else {
                    email.removeAttr("readonly");
                }
            });
            return false;
        }
        var settime = function () {
            if (countdown > 0) {
                btn.html(countdown + localize.reSend).attr('disabled', true);
                countdown--;
                setTimeout(settime, 1000);
            } else {
                btn.html(_text).attr('disabled', false);
                countdown = 60
            }
        }
        token_submit();
    });

    $.fn.dependency = function () {
        function checkBoolean(v) {
            switch (v) {
                case true:
                case 'true':
                case 1:
                case '1':
                    v = true;
                    break;

                case null:
                case false:
                case 'false':
                case 0:
                case '0':
                    v = false;
                    break;
            }
            return v;
        };

        function evalCondition(condition, val1, val2) {
            if (condition == '==') {
                return checkBoolean(val1) == checkBoolean(val2);
            } else if (condition == '!=') {
                return checkBoolean(val1) != checkBoolean(val2);
            } else if (condition == '>=') {
                return Number(val2) >= Number(val1);
            } else if (condition == '<=') {
                return Number(val2) <= Number(val1);
            } else if (condition == '>') {
                return Number(val2) > Number(val1);
            } else if (condition == '<') {
                return Number(val2) < Number(val1);
            } else if (condition == 'any') {
                if ($.isArray(val2)) {
                    for (var i = val2.length - 1; i >= 0; i--) {
                        if ($.inArray(val2[i], val1.split(',')) !== -1) {
                            return true;
                        }
                    }
                } else {
                    if ($.inArray(val2, val1.split(',')) !== -1) {
                        return true;
                    }
                }
            } else if (condition == 'not-any') {
                if ($.isArray(val2)) {
                    for (var i = val2.length - 1; i >= 0; i--) {
                        if ($.inArray(val2[i], val1.split(',')) == -1) {
                            return true;
                        }
                    }
                } else {
                    if ($.inArray(val2, val1.split(',')) == -1) {
                        return true;
                    }
                }
            }
            return false;
        };

        return this.each(function () {
            var $this = $(this),
                $fields = $this.find('[data-controller]');
            if ($fields.length) {
                var is_on = 'is-on';
                $fields.each(function () {
                    var $field = $(this);
                    if ($field.attr(is_on)) return;
                    var controllers = $field.attr(is_on, true).data('controller').split('|'),
                        conditions = $field.data('condition').split('|'),
                        values = $field.data('value').toString().split('|');
                    $.each(controllers, function (index, depend_id) {
                        var value = values[index] || '',
                            condition = conditions[index] || conditions[0] || '==';
                        $this.on('change', "[name='" + depend_id + "']", function (elem) {
                            var $elem = $(this);
                            var _type = $elem.attr('type');
                            var val2 = (_type == 'checkbox') ? $elem.is(':checked') : $elem.val();
                            var is_show = evalCondition(condition, value, val2);

                            $field.trigger('controller.change', is_show);
                            if (is_show) {
                                $field.show()
                            } else {
                                $field.hide()
                            }
                        });
                    });
                });
            }
        });
    };
    $('.dependency-box').dependency();
})(jQuery);
function change_input(_this) {
    if ($(_this).attr('data-status') == 'true' && $(_this).val().length <= $(_this).parent().attr('data-max')) {
        $(_this).parent().attr('data-min', $(_this).val().length);
    } else if ($(_this).attr('data-status') == 'true') {
        $(_this).val($(_this).val().substring(0, $(_this).parent().attr('data-max') - 1));
    }
}
function load_rand_post(datas){
    var load = loadingShow(datas.id, false);
    $.ajax({
        url: theme.ajaxurl,
        type: 'POST',
        dataType: 'html',
        data : {
            action: datas.action,
            data:datas
        },
    })
        .done(function(response) {
            $(datas.id+' .ajax-panel').html(response);
            loadingHid(load);
        })
        .fail(function() {
            loadingHid(load);
        })
}
function isURL(URL){
    var str=URL;
    var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
    var objExp=new RegExp(Expression);
    if(objExp.test(str)==true){
        return true;
    }else{
        return false;
    }
}
function isPC() {
    let u = navigator.userAgent;
    let Agents = ["Android", "iPhone", "webOS", "BlackBerry", "SymbianOS", "Windows Phone", "iPad", "iPod"];
    let flag = true;
    for (let i = 0; i < Agents.length; i++) {
        if (u.indexOf(Agents[i]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}
function chack_name(str){
    var pattern = RegExp(/[( )(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\*)(\()(\))(\+)(\=)(\[)(\])(\{)(\})(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)(\<)(\>)(\»)(\«)(\“)(\”)(\?)(\)]+/);
    if (pattern.test(str)){
        return true;
    }
    return false;
}
function errorAlert(msg) {
    showAlert(JSON.parse('{"status":4,"msg":"'+msg+'"}'))
}
function successAlert(msg) {
    showAlert(JSON.parse('{"status":2,"msg":"'+msg+'"}'))
}
function showAlert(data) {
    var alert,ico;
    switch(data.status) {
        case 0:
            title = localize.successAlert;
            alert='primary';
            ico='icon-loading icon-spin';
            break;
        case 1:
            title = localize.successAlert;
            alert='success';
            ico='icon-adopt';
            break;
        case 2:
            title = localize.infoAlert;
            alert='info';
            ico='icon-tishi';
            break;
        case 3:
            title = localize.warningAlert;
            alert='warning';
            ico='icon-warning';
            break;
        case 4:
            title = localize.errorAlert;
            alert='danger';
            ico='icon-close-circle';
            break;
        default:
    }
    var msg = data.msg;
    if(!$('#alert_placeholder')[0]){
        $('body').append('<div id="alert_placeholder" class="alert-system"></div>')
    };
    var $html = $('<div class="alert-body text-sm io-alert-'+alert+' alert alert-'+alert+' d-flex py-2 align-items-center"><i class="iconfont '+ico+' text-lg mr-2"></i><span class="mr-2">'+msg+'</span></div>');
    removeAlert();
    $('#alert_placeholder').append( $html );
    if(alert == 'primary'){
        $html.slideDown().addClass('show');
    } else {
        $html.slideDown().addClass('show');
        setTimeout(function () {
            removeAlert($html);
        }, 3500);
    }
}
function removeAlert(e) {
    if (!e) {
        e = $('.io-alert-primary');
    }
    if (e[0]) {
        e.removeClass('show');
        setTimeout(function () {
            e.remove();
        }, 300);
    }
}
function toTarget(menu, padding, isMult) {
    var slider = menu.children(".anchor");
    var target = menu.children(".hover").first() ;
    var scroll = menu.closest('.tab-auto-scrollbar');
    if (target && 0 < target.length){
    }
    else{
        if(isMult){
            target = menu.find('.active').parent();
        }else{
            target = menu.find('.active');
        }
    }
    if(0 < target.length){
        if(padding)
            slider.css({
                left: target.position().left + scroll.scrollLeft() + "px",
                width: target.outerWidth() + "px",
                opacity: "1"
            });
        else
            slider.css({
                left: target.position().left + scroll.scrollLeft() + (target.outerWidth()/4) + "px",
                width: target.outerWidth()/2 + "px",
                opacity: "1"
            });
    }
    else{
        slider.css({
            opacity: "0"
        })
    }
}
var ioadindex = 0;
function loadingShow(parent = "body",only=true){
    if(only && $('.load-loading')[0]){
        ioadindex ++;
        return $('.load-loading');
    }
    var load = $('<div class="load-loading" style="display:none"><div class="bg"></div><div class="rounded-lg bg-light" style="z-index:1"><div class="spinner-border m-4" role="status"><span class="sr-only">Loading...</span></div></div></div>');
    $(parent).prepend(load);
    load.fadeIn(200);
    return load;
}
function loadingHid(load){
    if(ioadindex>0)
        ioadindex--;
    else{
        ioadindex = 0;
        load.fadeOut(300,function(){ load.remove() });
    }
}
function ioPopupTips(type, msg, callBack) {
    var ico = '';
    switch(type) {
        case 1:
            ico='icon-adopt';
            break;
        case 2:
            ico='icon-tishi';
            break;
        case 3:
            ico='icon-warning';
            break;
        case 4:
            ico='icon-close-circle';
            break;
        default:
    }
    var c = type==1 ? 'tips-success' : 'tips-error';
    var html = '<section class="io-bomb '+c+' io-bomb-sm io-bomb-open">'+
        '<div class="io-bomb-overlay"></div>'+
        '<div class="io-bomb-body text-center">'+
        '<div class="io-bomb-content bg-white px-5"><i class="iconfont '+ico+' icon-8x"></i>'+
        '<p class="text-md mt-3">'+msg+'</p>'+
        '</div>'+
        '</div>'+
        '</section>';
    var tips = $(html);
    $('body').addClass('modal-open').append(tips);
    if(hasScrollbar())$('body').css("padding-right",getScrollbarWidth());
    setTimeout(function(){
        $('body').removeClass('modal-open');
        if(hasScrollbar())$('body').css("padding-right",'');
        if ($.isFunction(callBack)) callBack(true);
        tips.removeClass('io-bomb-open').addClass('io-bomb-close');
        setTimeout(function(){
            tips.removeClass('io-bomb-close');
            setTimeout(function(){
                tips.remove();
            }, 200);
        },400);
    },2000);
}
function ioPopup(type, html, maskStyle, btnCallBack) {
    var maskStyle = maskStyle ? 'style="' + maskStyle + '"' : '';
    var size = '';
    if( type == 'big' ){
        size = 'io-bomb-lg';
    }else if( type == 'no-padding' ){
        size = 'io-bomb-nopd';
    }else if( type == 'cover' ){
        size = 'io-bomb-cover io-bomb-nopd';
    }else if( type == 'full' ){
        size = 'io-bomb-xl';
    }else if( type == 'small' ){
        size = 'io-bomb-sm';
    }else if( type == 'confirm' ){
        size = 'io-bomb-md';
    }else if( type == 'pay' ){
        size = 'io-bomb-sm io-bomb-nopd';
    }
    var template = '\
	<div class="io-bomb ' + size + ' io-bomb-open">\
		<div class="io-bomb-overlay" ' + maskStyle + '></div>\
		<div class="io-bomb-body text-center">\
			<div class="io-bomb-content bg-white">\
				'+html+'\
			</div>\
			<div class="btn-close-bomb mt-2">\
                <i class="iconfont icon-close-circle"></i>\
            </div>\
		</div>\
	</div>\
	';
    var popup = $(template);
    $('body').addClass('modal-open').append(popup);
    if(hasScrollbar())$('body').css("padding-right",getScrollbarWidth());
    var close = function(){
        $('body').removeClass('modal-open');
        if(hasScrollbar())$('body').css("padding-right",'');
        $(popup).removeClass('io-bomb-open').addClass('io-bomb-close');
        setTimeout(function(){
            $(popup).removeClass('io-bomb-close');
            setTimeout(function(){
                popup.remove();
            }, 200);
        },600);
    };
    $(popup).on('click touchstart', '.btn-close-bomb i, .io-bomb-overlay', function(event) {
        event.preventDefault();
        if ($.isFunction(btnCallBack)) btnCallBack(true);
        close();
    });
    return popup;
}
function ioConfirm(title, message, btnCallBack) {
    var template = '\
	<div class="io-bomb io-bomb-confirm io-bomb-open">\
		<div class="io-bomb-overlay"></div>\
		<div class="io-bomb-body">\
			<div class="io-bomb-content bg-white text-sm">\
                <div class="io-bomb-header fx-yellow modal-header-bg text-center p-3">\
                    <i class="iconfont icon-tishi icon-2x"></i>\
                    <div class="text-md mt-1">'+title+'</div>\
                </div>\
				<div class="m-4">'+message+'</div>\
                <div class="text-center mb-4">\
                    <button class="btn vc-red btn-shadow mx-2 px-5" onclick="_onclick(true);">'+localize.okBtn+'</button>\
                    <button class="btn vc-l-yellow btn-outline mx-2 px-5" onclick="_onclick(false);">'+localize.cancelBtn+'</button>\
                </div>\
			</div>\
		</div>\
	</div>\
	';
    var popup = $(template);
    $('body').addClass('modal-open').append(popup);
    if(hasScrollbar())$('body').css("padding-right",getScrollbarWidth());
    _onclick = function (r) {
        close();
        if ($.isFunction(btnCallBack)) btnCallBack(r ,$(this));
    };
    var close = function(){
        $('body').removeClass('modal-open');
        if(hasScrollbar())$('body').css("padding-right",'');
        $(popup).removeClass('io-bomb-open').addClass('io-bomb-close');
        setTimeout(function(){
            $(popup).removeClass('io-bomb-close');
            setTimeout(function(){
                popup.remove();
            }, 200);
        },600);
    };
    return popup;
}
function debounce(callback, delay, immediate) {
    var timeout;
    return function () {
        var context = this,
            args = arguments;
        var later = function () {
            timeout = null;
            if (!immediate) {
                callback.apply(context, args);
            }
        };
        var callNow = (immediate && !timeout);
        clearTimeout(timeout);
        timeout = setTimeout(later, delay);
        if (callNow) {
            callback.apply(context, args);
        }
    };
}
function ioModal(_this) {
    var size = _this.data('modal_size') || 'modal-medium';
    var type = _this.data('modal_type') || 'modal-suspend';
    var id = 'refresh_modal'+type;
    var modal_html = '<div class="modal fade" id="' + id + '" tabindex="-1" role="dialog" aria-hidden="false">\
    <div class="modal-dialog '+ size +' modal-dialog-centered" role="document">\
    <div class="modal-content '+ type +'">\
    </div>\
    </div>\
    </div>\
    </div>';
    var loading = '<div class="io-modal-content"></div><div class="loading-anim io-radius bg-blur-20"><div class="d-flex align-items-center justify-content-center h-100"><i class="iconfont icon-loading icon-spin icon-2x"></i></div></div>';

    var modal = $('#'+id);
    if (!modal[0]) {
        $('body').append(modal_html);
        modal = $('#'+id);
    }
    modal.find('.modal-content').html(loading).css({
        'height': '220px',
        'overflow': 'hidden'
    });
    modal.modal('show');
    return modal;
}

function GetQueryVal(key) {
    var url = window.parent.location.search;
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        if (str.indexOf("#" != -1)) {
            str = str.substr(0);
        }
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            if (strs[i].indexOf(key) != -1) {
                return strs[i].split("=")[1];
            }
        }
    }
    return null;
}

var chartTheme ='';
var domChart = document.getElementById("chart-container");
var ioChart;
var chartOption;
function setChartTheme(){
    if (chartOption && typeof chartOption === 'object') {
        ioChart.dispose();
        ioChart = echarts.init(domChart, chartTheme);
        ioChart.setOption(chartOption);
    }
}
function refreshChart(){
    if (chartOption && typeof chartOption === 'object') {
        ioChart.resize();
    }
}
function hasScrollbar() {
    return document.body.scrollHeight > (window.innerHeight || document.documentElement.clientHeight)?'11':'22';
}
function getScrollbarWidth() {
    var noScroll, scroll, oDiv = document.createElement("DIV");
    oDiv.style.cssText = "position:absolute; top:-1000px; width:100px; height:100px; overflow:hidden;";
    noScroll = document.body.appendChild(oDiv).clientWidth;
    oDiv.style.overflowY = "scroll";
    scroll = oDiv.clientWidth;
    document.body.removeChild(oDiv);
    return noScroll-scroll;
}
function setCookie(cname,cvalue,exdays){
    var expires = '';
    if(exdays!=''){
        var d = new Date();
        d.setTime(d.getTime()+(exdays*24*60*60*1000));
        expires = "expires="+d.toGMTString();
    }
    document.cookie = cname+"="+cvalue+"; "+expires+"; path=/";
}
function getCookie(cname){
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name)==0) { return c.substring(name.length,c.length); }
    }
    return "";
}
function is_function(functionName){
    try {
        if (typeof eval(functionName) === "function") {
            return true;
        } else {
            return false;
        }
    } catch (e) {
    }
    return false;
}

/**
 * 处理人机验证表单
 * @param {*} _this
 * @param {*} data
 * @param {*} success
 * @returns
 */
function captcha_ajax(_this, data='', success='') {
    if (_this.attr('disabled')) {
        return false;
    }
    if (!data) {
        var form = _this.closest('form');
        data = form.serializeObject();
    }
    var _action = _this.data('action')
    if (_action) {
        data.action = _action;
    }
    if (data.captcha_type && window.captcha && !window.captcha.ticket) {
        CaptchaOpen(_this, data.captcha_type);
        return false;
    }

    if (window.captcha) {
        data.captcha = JSON.parse(JSON.stringify(window.captcha));
        data.captcha._this && delete(data.captcha._this);
        window.captcha = {};
    }

    var alert = {};
    alert.status = 0;
    alert.msg = localize.loading;
    showAlert(alert);

    var _text = _this.html();
    _this.attr('disabled', true).html('<i class="iconfont icon-loading icon-spin mr-2"></i>'+localize.wait);

    $.ajax({
        url: theme.ajaxurl,
        type: 'POST',
        dataType: 'json',
        data : data,
    }).done(function(n){
        if (n.msg) {
            alert.status = n.status;
            alert.msg = n.msg;
            showAlert(alert);
        } else {
            removeAlert();
        }
        _this.attr('disabled', false).html(_text);
        $.isFunction(success) && success(n, _this, data);
        if (n.goto) {
            window.location.href = n.goto;
            window.location.reload;
        } else if (n.reload) {
            window.location.reload();
        }
    }).fail(function (n) {
        n = n.responseJSON;
        if (n && n.msg) {
            alert.status = n.status;
            alert.msg = n.msg;
            showAlert(alert);
        } else {
            alert.status = 4;
            alert.msg = localize.networkerror;
            showAlert(alert);
        }
        _this.attr('disabled', false).html(_text);
    })
}

function copyText(text, success, error, _this) {
    // 数字没有 .length 不能执行selectText 需要转化成字符串
    var textString = text.toString();
    var input = document.querySelector('#copy-input');
    if (!input) {
        input = document.createElement('input');
        input.id = "copy-input";
        input.readOnly = "readOnly"; // 防止ios聚焦触发键盘事件
        input.style.position = "fixed";
        input.style.left = "-2000px";
        input.style.zIndex = "-1000";
        _this.parentNode.appendChild(input)
    }

    input.value = textString;
    // ios必须先选中文字且不支持 input.select();
    selectText(input, 0, textString.length);
    if (document.execCommand('copy')) {
        $.isFunction(success) && success();
    } else {
        $.isFunction(error) && error();
    }
    input.blur();

    // input自带的select()方法在苹果端无法进行选择，所以需要自己去写一个类似的方法
    // 选择文本。createTextRange(setSelectionRange)是input方法
    function selectText(textbox, startIndex, stopIndex) {
        if (textbox.createTextRange) { //ie
            var range = textbox.createTextRange();
            range.collapse(true);
            range.moveStart('character', startIndex); //起始光标
            range.moveEnd('character', stopIndex - startIndex); //结束光标
            range.select(); //不兼容苹果
        } else { //firefox/chrome
            textbox.setSelectionRange(startIndex, stopIndex);
            textbox.select();
        }
    }
}

/**
 *
 * @returns
 */
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
