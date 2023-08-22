(function($) {
    var admin = {
        initialised: false,
        version: 1,
        mobile: false,
        collapseScreenSize: 991,
        sideBarSize: 1199,
        init: function() {
            if (!this.initialised) {
                this.initialised = true
            } else {
                return
            }
            this.userToggle();
            this.sideBarToggle();
            this.sideMenu();
            this.sideBarHover();
            this.searchToggle();
            this.rightSlide();
            this.tooltipHover();
            this.counter();
            this.nubberSpin();
            this.singleSlide();
            this.productRemove();
            this.Quantity();
            this.PriceRange();
            this.Product_thumb_slider();
            this.counter_number();
            this.loader()
        },
        loader: function() {
            jQuery(window).on("load", function() {
                $(".loader").fadeOut();
                $(".spinner").delay(500).fadeOut("slow")
            })
        },
        counter_number: function() {
            if ($(".counter-text").length > 0) {
                $(".counter-text").appear(function() {
                    $(".count-no1").countTo()
                })
            }
        },
        Product_thumb_slider: function() {
            if ($(".int-thumb-slider").length > 0) {
                var galleryThumbs = new Swiper(".gallery-thumbs", {
                    spaceBetween: 10,
                    slidesPerView: 4,
                    freeMode: true,
                    watchSlidesVisibility: true,
                    watchSlidesProgress: true,
                });
                var galleryTop = new Swiper(".gallery-top", {
                    spaceBetween: 10,
                    thumbs: {
                        swiper: galleryThumbs
                    }
                })
            }
        },
        PriceRange: function() {
            if ($(".range-slider").length > 0) {
                $(function() {
                    $("#slider-range").slider({
                        range: true,
                        min: 12,
                        max: 2000,
                        values: [541, 1402],
                        slide: function(event, ui) {
                            $("#amount").text("$" + ui.values[0] + " - $" + ui.values[1])
                        }
                    });
                    $("#amount").text("$" + $("#slider-range").slider("values", 0) + " - $" + $("#slider-range").slider("values", 1))
                })
            }
        },
        Quantity: function() {
            var quantity = 0;
            $(".quantity-plus").on("click", function(e) {
                e.preventDefault();
                var quantity = parseInt($(this).siblings(".quantity").val());
                $(this).siblings(".quantity").val(quantity + 1)
            });
            $(".quantity-minus").on("click", function(e) {
                e.preventDefault();
                var quantity = parseInt($(this).siblings(".quantity").val());
                if (quantity > 0) {
                    $(this).siblings(".quantity").val(quantity - 1)
                }
            })
        },
        userToggle: function() {
            var count = 0;
            $(".user-info").on("click", function() {
                if ($(window).width() <= admin.collapseScreenSize) {
                    if (count == "0") {
                        $(".user-info-box").addClass("show");
                        count++
                    } else {
                        $(".user-info-box").removeClass("show");
                        count--
                    }
                }
            });
            $(".user-info-box, .user-info").on("click", function(e) {
                if ($(window).width() <= admin.collapseScreenSize) {
                    event.stopPropagation()
                }
            });
            $("body").on("click", function() {
                if ($(window).width() <= admin.collapseScreenSize) {
                    if (count == "1") {
                        $(".user-info-box").removeClass("show");
                        count--
                    }
                }
            })
        },
        sideBarToggle: function() {
            $(".toggle-btn").on("click", function(e) {
                e.stopPropagation();
                $("body").toggleClass("mini-sidebar");
                $(this).toggleClass("checked")
            });
            $(".sidebar-wrapper").on("click", function(event) {
                event.stopPropagation()
            })
        },
        sideMenu: function() {
            $(".side-menu-wrap ul li").has(".sub-menu").addClass("has-sub-menu");
            $.sidebarMenu = function(menu) {
                var animationSpeed = 300,
                    subMenuSelector = ".sub-menu";
                $(menu).on("click", "li a", function(e) {
                    var $this = $(this);
                    var checkElement = $this.next();
                    if (checkElement.is(subMenuSelector) && checkElement.is(":visible")) {
                        checkElement.slideUp(animationSpeed, function() {});
                    } else {
                        var parent = $this.parents("ul").first();
                        var parent_li = $this.parent("li");
                        if ((checkElement.is(subMenuSelector)) && (!checkElement.is(":visible"))) {
                            checkElement.slideDown(animationSpeed, function() {})
                        }else{
                            parent.parent().parent().find("li.active").removeClass("active");
                            parent.parent().addClass("active");
                            parent_li.addClass("active");
                        }
                    }
                    if (checkElement.is(subMenuSelector)) {
                        e.preventDefault()
                    }
                })
            };
            $.sidebarMenu($(".main-menu"));
            $(function() {
                for (var a = window.location, counting = $(".main-menu a").filter(function() {
                    return this.href == a
                }).addClass("active").parent().addClass("active");;) {
                    if (!counting.is("li")) {
                        break
                    }
                    counting = counting.parent().addClass("in").parent().addClass("active")
                }
            })
        },
        sideBarHover: function() {
            if ($(window).width() >= admin.sideBarSize) {
                $(".main-menu").hover(function() {
                    $("body").addClass("sidebar-hover")
                }, function() {
                    $("body").removeClass("sidebar-hover")
                })
            }
        },
        searchToggle: function() {
            $(".search-toggle").on("click", function() {
                $(".serch-wrapper").addClass("show-search")
            });
            $(".search-close, .main-content").on("click", function() {
                $(".serch-wrapper").removeClass("show-search")
            })
        },
        rightSlide: function() {
            $(".setting-info").on("click", function(e) {
                e.stopPropagation();
                $("body").toggleClass("open-setting")
            });
            $("body, .close-btn").on("click", function() {
                $("body").removeClass("open-setting")
            });
            $(".slide-setting-box").on("click", function(event) {
                event.stopPropagation()
            })
        },
        tooltipHover: function() {
            if ($(".toltiped").length > 0) {
                $(".toltiped").tooltip()
            }
            if ($(".toltiped-right").length > 0) {
                $(".toltiped-right").tooltip({
                    "placement": "right",
                })
            }
        },
        productRemove: function() {
            $(".remove-product").on("click", function() {
                $(this).closest(".product-thumb-wrap").parent().remove()
            })
        },
        counter: function() {
            if ($(".counter-holder").length > 0) {
                var a = 0;
                $(window).scroll(function() {
                    var topScroll = $(".counter-holder").offset().top - window.innerHeight;
                    if (a == 0 && $(window).scrollTop() > topScroll) {
                        $(".count-no").each(function() {
                            var $this = $(this),
                                countTo = $this.attr("data-count");
                            $({
                                countNum: $this.text()
                            }).animate({
                                countNum: countTo
                            }, {
                                duration: 5000,
                                easing: "swing",
                                step: function() {
                                    $this.text(Math.floor(this.countNum))
                                },
                                complete: function() {
                                    $this.text(this.countNum)
                                }
                            })
                        });
                        a = 1
                    }
                })
            }
        },
        nubberSpin: function() {
            if ($(".number-spin").length > 0) {
                $(".number-increase").on("click", function() {
                    if ($(this).prev().val() < 50000) {
                        $(this).prev().val(+$(this).prev().val() + 1)
                    }
                });
                $(".number-decrease").on("click", function() {
                    if ($(this).next().val() > 1) {
                        if ($(this).next().val() > 1) {
                            $(this).next().val(+$(this).next().val() - 1)
                        }
                    }
                })
            }
        },
        singleSlide: function() {
            if ($(".swiper-container.s1").length > 0) {
                var slingleSlideSwiper = new Swiper(".swiper-container.s1", {
                    autoHeight: false,
                    autoplay: false,
                    loop: true,
                    spaceBetween: 0,
                    centeredSlides: false,
                    speed: 1500,
                    autoplay: {
                        delay: 1000,
                    },
                    navigation: {
                        nextEl: ".swiperButtonNext",
                        prevEl: ".swiperButtonPrev",
                    },
                })
            }
        },
    };
    admin.init()
})(jQuery);
$(document).ready(function() {
    $(window).on("load", function() {
        var colorcode = document.cookie;
        if (colorcode != "") {
            var cname = "colorcssfile";
            var name = cname + "=";
            var cssname = "";
            var ca = document.cookie.split(";");
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == " ") {
                    c = c.substring(1)
                }
                if (c.indexOf(name) != -1) {
                    cssname = c.substring(name.length, c.length)
                }
            }
            if (cssname != "") {
                var new_style = "assets/css/" + cssname + ".css";
                $("#theme-change").attr("href", new_style)
            }
        }
    });
    $(".colorchange").on("click", function() {
        var name = $(this).attr("id");
        var new_style = "assets/css/" + name + ".css";
        $("#theme-change").attr("href", new_style)
    });
    $("#style-switcher .bottom a.settings").on("click", function(e) {
        e.preventDefault();
        var div = $("#style-switcher");
        if (div.css("right") === "-133px") {
            $("#style-switcher").animate({
                right: "0px"
            })
        } else {
            $("#style-switcher").animate({
                right: "-133px"
            })
        }
    })
});