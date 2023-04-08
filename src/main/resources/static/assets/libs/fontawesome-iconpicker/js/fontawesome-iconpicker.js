/*!
 * Font Awesome Icon Picker
 * https://farbelous.github.io/fontawesome-iconpicker/
 *
 * @author Javi Aguilar, itsjavi.com
 * @license MIT License
 * @see https://github.com/farbelous/fontawesome-iconpicker/blob/master/LICENSE
 */


! function(e) {
    "function" == typeof define && define.amd ? define(["jquery"], e) : e(jQuery)
}(function(j) {
    j.ui = j.ui || {};
    j.ui.version = "1.12.1";
    ! function() {
        var r, y = Math.max,
            x = Math.abs,
            s = /left|center|right/,
            i = /top|center|bottom/,
            c = /[\+\-]\d+(\.[\d]+)?%?/,
            f = /^\w+/,
            l = /%$/,
            o = j.fn.pos;

        function q(e, a, t) {
            return [parseFloat(e[0]) * (l.test(e[0]) ? a / 100 : 1), parseFloat(e[1]) * (l.test(e[1]) ? t / 100 : 1)]
        }

        function C(e, a) {
            return parseInt(j.css(e, a), 10) || 0
        }
        j.pos = {
            scrollbarWidth: function() {
                if (void 0 !== r) return r;
                var e, a, t = j("<div style='display:block;position:absolute;width:50px;height:50px;overflow:hidden;'><div style='height:100px;width:auto;'></div></div>"),
                    s = t.children()[0];
                return j("body").append(t), e = s.offsetWidth, t.css("overflow", "scroll"), e === (a = s.offsetWidth) && (a = t[0].clientWidth), t.remove(), r = e - a
            },
            getScrollInfo: function(e) {
                var a = e.isWindow || e.isDocument ? "" : e.element.css("overflow-x"),
                    t = e.isWindow || e.isDocument ? "" : e.element.css("overflow-y"),
                    s = "scroll" === a || "auto" === a && e.width < e.element[0].scrollWidth;
                return {
                    width: "scroll" === t || "auto" === t && e.height < e.element[0].scrollHeight ? j.pos.scrollbarWidth() : 0,
                    height: s ? j.pos.scrollbarWidth() : 0
                }
            },
            getWithinInfo: function(e) {
                var a = j(e || window),
                    t = j.isWindow(a[0]),
                    s = !!a[0] && 9 === a[0].nodeType;
                return {
                    element: a,
                    isWindow: t,
                    isDocument: s,
                    offset: !t && !s ? j(e).offset() : {
                        left: 0,
                        top: 0
                    },
                    scrollLeft: a.scrollLeft(),
                    scrollTop: a.scrollTop(),
                    width: a.outerWidth(),
                    height: a.outerHeight()
                }
            }
        }, j.fn.pos = function(h) {
            if (!h || !h.of) return o.apply(this, arguments);
            h = j.extend({}, h);
            var m, p, d, T, u, e, a, t, g = j(h.of),
                b = j.pos.getWithinInfo(h.within),
                k = j.pos.getScrollInfo(b),
                w = (h.collision || "flip").split(" "),
                v = {};
            return e = 9 === (t = (a = g)[0]).nodeType ? {
                width: a.width(),
                height: a.height(),
                offset: {
                    top: 0,
                    left: 0
                }
            } : j.isWindow(t) ? {
                width: a.width(),
                height: a.height(),
                offset: {
                    top: a.scrollTop(),
                    left: a.scrollLeft()
                }
            } : t.preventDefault ? {
                width: 0,
                height: 0,
                offset: {
                    top: t.pageY,
                    left: t.pageX
                }
            } : {
                width: a.outerWidth(),
                height: a.outerHeight(),
                offset: a.offset()
            }, g[0].preventDefault && (h.at = "left top"), p = e.width, d = e.height, T = e.offset, u = j.extend({}, T), j.each(["my", "at"], function() {
                var e, a, t = (h[this] || "").split(" ");
                1 === t.length && (t = s.test(t[0]) ? t.concat(["center"]) : i.test(t[0]) ? ["center"].concat(t) : ["center", "center"]), t[0] = s.test(t[0]) ? t[0] : "center", t[1] = i.test(t[1]) ? t[1] : "center", e = c.exec(t[0]), a = c.exec(t[1]), v[this] = [e ? e[0] : 0, a ? a[0] : 0], h[this] = [f.exec(t[0])[0], f.exec(t[1])[0]]
            }), 1 === w.length && (w[1] = w[0]), "right" === h.at[0] ? u.left += p : "center" === h.at[0] && (u.left += p / 2), "bottom" === h.at[1] ? u.top += d : "center" === h.at[1] && (u.top += d / 2), m = q(v.at, p, d), u.left += m[0], u.top += m[1], this.each(function() {
                var t, e, c = j(this),
                    f = c.outerWidth(),
                    l = c.outerHeight(),
                    a = C(this, "marginLeft"),
                    s = C(this, "marginTop"),
                    r = f + a + C(this, "marginRight") + k.width,
                    i = l + s + C(this, "marginBottom") + k.height,
                    o = j.extend({}, u),
                    n = q(v.my, c.outerWidth(), c.outerHeight());
                "right" === h.my[0] ? o.left -= f : "center" === h.my[0] && (o.left -= f / 2), "bottom" === h.my[1] ? o.top -= l : "center" === h.my[1] && (o.top -= l / 2), o.left += n[0], o.top += n[1], t = {
                    marginLeft: a,
                    marginTop: s
                }, j.each(["left", "top"], function(e, a) {
                    j.ui.pos[w[e]] && j.ui.pos[w[e]][a](o, {
                        targetWidth: p,
                        targetHeight: d,
                        elemWidth: f,
                        elemHeight: l,
                        collisionPosition: t,
                        collisionWidth: r,
                        collisionHeight: i,
                        offset: [m[0] + n[0], m[1] + n[1]],
                        my: h.my,
                        at: h.at,
                        within: b,
                        elem: c
                    })
                }), h.using && (e = function(e) {
                    var a = T.left - o.left,
                        t = a + p - f,
                        s = T.top - o.top,
                        r = s + d - l,
                        i = {
                            target: {
                                element: g,
                                left: T.left,
                                top: T.top,
                                width: p,
                                height: d
                            },
                            element: {
                                element: c,
                                left: o.left,
                                top: o.top,
                                width: f,
                                height: l
                            },
                            horizontal: t < 0 ? "left" : 0 < a ? "right" : "center",
                            vertical: r < 0 ? "top" : 0 < s ? "bottom" : "middle"
                        };
                    p < f && x(a + t) < p && (i.horizontal = "center"), d < l && x(s + r) < d && (i.vertical = "middle"), y(x(a), x(t)) > y(x(s), x(r)) ? i.important = "horizontal" : i.important = "vertical", h.using.call(this, e, i)
                }), c.offset(j.extend(o, {
                    using: e
                }))
            })
        }, j.ui.pos = {
            _trigger: function(e, a, t, s) {
                a.elem && a.elem.trigger({
                    type: t,
                    position: e,
                    positionData: a,
                    triggered: s
                })
            },
            fit: {
                left: function(e, a) {
                    j.ui.pos._trigger(e, a, "posCollide", "fitLeft");
                    var t, s = a.within,
                        r = s.isWindow ? s.scrollLeft : s.offset.left,
                        i = s.width,
                        c = e.left - a.collisionPosition.marginLeft,
                        f = r - c,
                        l = c + a.collisionWidth - i - r;
                    a.collisionWidth > i ? 0 < f && l <= 0 ? (t = e.left + f + a.collisionWidth - i - r, e.left += f - t) : e.left = 0 < l && f <= 0 ? r : l < f ? r + i - a.collisionWidth : r : 0 < f ? e.left += f : 0 < l ? e.left -= l : e.left = y(e.left - c, e.left), j.ui.pos._trigger(e, a, "posCollided", "fitLeft")
                },
                top: function(e, a) {
                    j.ui.pos._trigger(e, a, "posCollide", "fitTop");
                    var t, s = a.within,
                        r = s.isWindow ? s.scrollTop : s.offset.top,
                        i = a.within.height,
                        c = e.top - a.collisionPosition.marginTop,
                        f = r - c,
                        l = c + a.collisionHeight - i - r;
                    a.collisionHeight > i ? 0 < f && l <= 0 ? (t = e.top + f + a.collisionHeight - i - r, e.top += f - t) : e.top = 0 < l && f <= 0 ? r : l < f ? r + i - a.collisionHeight : r : 0 < f ? e.top += f : 0 < l ? e.top -= l : e.top = y(e.top - c, e.top), j.ui.pos._trigger(e, a, "posCollided", "fitTop")
                }
            },
            flip: {
                left: function(e, a) {
                    j.ui.pos._trigger(e, a, "posCollide", "flipLeft");
                    var t, s, r = a.within,
                        i = r.offset.left + r.scrollLeft,
                        c = r.width,
                        f = r.isWindow ? r.scrollLeft : r.offset.left,
                        l = e.left - a.collisionPosition.marginLeft,
                        o = l - f,
                        n = l + a.collisionWidth - c - f,
                        h = "left" === a.my[0] ? -a.elemWidth : "right" === a.my[0] ? a.elemWidth : 0,
                        m = "left" === a.at[0] ? a.targetWidth : "right" === a.at[0] ? -a.targetWidth : 0,
                        p = -2 * a.offset[0];
                    o < 0 ? ((t = e.left + h + m + p + a.collisionWidth - c - i) < 0 || t < x(o)) && (e.left += h + m + p) : 0 < n && (0 < (s = e.left - a.collisionPosition.marginLeft + h + m + p - f) || x(s) < n) && (e.left += h + m + p), j.ui.pos._trigger(e, a, "posCollided", "flipLeft")
                },
                top: function(e, a) {
                    j.ui.pos._trigger(e, a, "posCollide", "flipTop");
                    var t, s, r = a.within,
                        i = r.offset.top + r.scrollTop,
                        c = r.height,
                        f = r.isWindow ? r.scrollTop : r.offset.top,
                        l = e.top - a.collisionPosition.marginTop,
                        o = l - f,
                        n = l + a.collisionHeight - c - f,
                        h = "top" === a.my[1] ? -a.elemHeight : "bottom" === a.my[1] ? a.elemHeight : 0,
                        m = "top" === a.at[1] ? a.targetHeight : "bottom" === a.at[1] ? -a.targetHeight : 0,
                        p = -2 * a.offset[1];
                    o < 0 ? ((s = e.top + h + m + p + a.collisionHeight - c - i) < 0 || s < x(o)) && (e.top += h + m + p) : 0 < n && (0 < (t = e.top - a.collisionPosition.marginTop + h + m + p - f) || x(t) < n) && (e.top += h + m + p), j.ui.pos._trigger(e, a, "posCollided", "flipTop")
                }
            },
            flipfit: {
                left: function() {
                    j.ui.pos.flip.left.apply(this, arguments), j.ui.pos.fit.left.apply(this, arguments)
                },
                top: function() {
                    j.ui.pos.flip.top.apply(this, arguments), j.ui.pos.fit.top.apply(this, arguments)
                }
            }
        },
            function() {
                var e, a, t, s, r, i = document.getElementsByTagName("body")[0],
                    c = document.createElement("div");
                for (r in e = document.createElement(i ? "div" : "body"), t = {
                    visibility: "hidden",
                    width: 0,
                    height: 0,
                    border: 0,
                    margin: 0,
                    background: "none"
                }, i && j.extend(t, {
                    position: "absolute",
                    left: "-1000px",
                    top: "-1000px"
                }), t) e.style[r] = t[r];
                e.appendChild(c), (a = i || document.documentElement).insertBefore(e, a.firstChild), c.style.cssText = "position: absolute; left: 10.7432222px;", s = j(c).offset().left, j.support.offsetFractions = 10 < s && s < 11, e.innerHTML = "", a.removeChild(e)
            }()
    }();
    j.ui.position
}),
    function(e) {
        "use strict";
        "function" == typeof define && define.amd ? define(["jquery"], e) : window.jQuery && !window.jQuery.fn.iconpicker && e(window.jQuery)
    }(function(l) {
        "use strict";
        var t = function(e) {
                return !1 === e || "" === e || null == e
            },
            s = function(e) {
                return 0 < l(e).length
            },
            r = function(e) {
                return "string" == typeof e || e instanceof String
            },
            i = function(e, a) {
                return -1 !== l.inArray(e, a)
            },
            c = function(e, a) {
                this._id = c._idCounter++, this.element = l(e).addClass("iconpicker-element"), this._trigger("iconpickerCreate", {
                    iconpickerValue: this.iconpickerValue
                }), this.options = l.extend({}, c.defaultOptions, this.element.data(), a), this.options.templates = l.extend({}, c.defaultOptions.templates, this.options.templates), this.options.originalPlacement = this.options.placement, this.container = !!s(this.options.container) && l(this.options.container), !1 === this.container && (this.element.is(".dropdown-toggle") ? this.container = l("~ .dropdown-menu:first", this.element) : this.container = this.element.is("input,textarea,button,.btn") ? this.element.parent() : this.element), this.container.addClass("iconpicker-container"), this.isDropdownMenu() && (this.options.placement = "inline"), this.input = !!this.element.is("input,textarea") && this.element.addClass("iconpicker-input"), !1 === this.input && (this.input = this.container.find(this.options.input), this.input.is("input,textarea") || (this.input = !1)), this.component = this.isDropdownMenu() ? this.container.parent().find(this.options.component) : this.container.find(this.options.component), 0 === this.component.length ? this.component = !1 : this.component.find("i").addClass("iconpicker-component"), this._createPopover(), this._createIconpicker(), 0 === this.getAcceptButton().length && (this.options.mustAccept = !1), this.isInputGroup() ? this.container.parent().append(this.popover) : this.container.append(this.popover), this._bindElementEvents(), this._bindWindowEvents(), this.update(this.options.selected), this.isInline() && this.show(), this._trigger("iconpickerCreated", {
                    iconpickerValue: this.iconpickerValue
                })
            };
        c._idCounter = 0, c.defaultOptions = {
            title: !1,
            selected: !1,
            defaultValue: !1,
            placement: "bottom",
            collision: "none",
            animation: !0,
            hideOnSelect: !1,
            showFooter: !1,
            searchInFooter: !1,
            mustAccept: !1,
            selectedCustomClass: "bg-primary",
            icons: [],
            fullClassFormatter: function(e) {
                return e
            },
            input: "input,.iconpicker-input",
            inputSearch: !1,
            container: !1,
            component: ".input-group-addon,.iconpicker-component",
            templates: {
                popover: '<div class="iconpicker-popover popover"><div class="arrow"></div><div class="popover-title"></div><div class="popover-content"></div></div>',
                footer: '<div class="popover-footer"></div>',
                buttons: '<button class="iconpicker-btn iconpicker-btn-cancel btn btn-default btn-sm">Cancel</button> <button class="iconpicker-btn iconpicker-btn-accept btn btn-primary btn-sm">Accept</button>',
                search: '<input type="search" class="form-control iconpicker-search" placeholder="Type to filter" />',
                iconpicker: '<div class="iconpicker"><div class="iconpicker-items"></div></div>',
                iconpickerItem: '<a role="button" href="javascript:;" class="iconpicker-item"><i></i></a>'
            }
        }, c.batch = function(e, a) {
            var t = Array.prototype.slice.call(arguments, 2);
            return l(e).each(function() {
                var e = l(this).data("iconpicker");
                e && e[a].apply(e, t)
            })
        }, c.prototype = {
            constructor: c,
            options: {},
            _id: 0,
            _trigger: function(e, a) {
                a = a || {}, this.element.trigger(l.extend({
                    type: e,
                    iconpickerInstance: this
                }, a))
            },
            _createPopover: function() {
                this.popover = l(this.options.templates.popover);
                var e = this.popover.find(".popover-title");
                if (this.options.title && e.append(l('<div class="popover-title-text">' + this.options.title + "</div>")), this.hasSeparatedSearchInput() && !this.options.searchInFooter ? e.append(this.options.templates.search) : this.options.title || e.remove(), this.options.showFooter && !t(this.options.templates.footer)) {
                    var a = l(this.options.templates.footer);
                    this.hasSeparatedSearchInput() && this.options.searchInFooter && a.append(l(this.options.templates.search)), t(this.options.templates.buttons) || a.append(l(this.options.templates.buttons)), this.popover.append(a)
                }
                return !0 === this.options.animation && this.popover.addClass("fade"), this.popover
            },
            _createIconpicker: function() {
                var t = this;
                this.iconpicker = l(this.options.templates.iconpicker);
                var e = function(e) {
                        var a = l(this);
                        a.is("i") && (a = a.parent()), t._trigger("iconpickerSelect", {
                            iconpickerItem: a,
                            iconpickerValue: t.iconpickerValue
                        }), !1 === t.options.mustAccept ? (t.update(a.data("iconpickerValue")), t._trigger("iconpickerSelected", {
                            iconpickerItem: this,
                            iconpickerValue: t.iconpickerValue
                        })) : t.update(a.data("iconpickerValue"), !0), t.options.hideOnSelect && !1 === t.options.mustAccept && t.hide()
                    },
                    a = l(this.options.templates.iconpickerItem),
                    s = [];
                for (var r in this.options.icons)
                    if ("string" == typeof this.options.icons[r].title) {
                        var i = a.clone();
                        if (i.find("i").addClass(this.options.fullClassFormatter(this.options.icons[r].title)), i.data("iconpickerValue", this.options.icons[r].title).on("click.iconpicker", e), i.attr("title", "." + this.options.icons[r].title), 0 < this.options.icons[r].searchTerms.length) {
                            for (var c = "", f = 0; f < this.options.icons[r].searchTerms.length; f++) c = c + this.options.icons[r].searchTerms[f] + " ";
                            i.attr("data-search-terms", c)
                        }
                        s.push(i)
                    } return this.iconpicker.find(".iconpicker-items").append(s), this.popover.find(".popover-content").append(this.iconpicker), this.iconpicker
            },
            _isEventInsideIconpicker: function(e) {
                var a = l(e.target);
                return !((!a.hasClass("iconpicker-element") || a.hasClass("iconpicker-element") && !a.is(this.element)) && 0 === a.parents(".iconpicker-popover").length)
            },
            _bindElementEvents: function() {
                var a = this;
                this.getSearchInput().on("keyup.iconpicker", function() {
                    a.filter(l(this).val().toLowerCase())
                }), this.getAcceptButton().on("click.iconpicker", function() {
                    var e = a.iconpicker.find(".iconpicker-selected").get(0);
                    a.update(a.iconpickerValue), a._trigger("iconpickerSelected", {
                        iconpickerItem: e,
                        iconpickerValue: a.iconpickerValue
                    }), a.isInline() || a.hide()
                }), this.getCancelButton().on("click.iconpicker", function() {
                    a.isInline() || a.hide()
                }), this.element.on("focus.iconpicker", function(e) {
                    a.show(), e.stopPropagation()
                }), this.hasComponent() && this.component.on("click.iconpicker", function() {
                    a.toggle()
                }), this.hasInput() && this.input.on("keyup.iconpicker", function(e) {
                    i(e.keyCode, [38, 40, 37, 39, 16, 17, 18, 9, 8, 91, 93, 20, 46, 186, 190, 46, 78, 188, 44, 86]) ? a._updateFormGroupStatus(!1 !== a.getValid(this.value)) : a.update(), !0 === a.options.inputSearch && a.filter(l(this).val().toLowerCase())
                })
            },
            _bindWindowEvents: function() {
                var e = l(window.document),
                    a = this,
                    t = ".iconpicker.inst" + this._id;
                l(window).on("resize.iconpicker" + t + " orientationchange.iconpicker" + t, function(e) {
                    a.popover.hasClass("in") && a.updatePlacement()
                }), a.isInline() || e.on("mouseup" + t, function(e) {
                    a._isEventInsideIconpicker(e) || a.isInline() || a.hide()
                })
            },
            _unbindElementEvents: function() {
                this.popover.off(".iconpicker"), this.element.off(".iconpicker"), this.hasInput() && this.input.off(".iconpicker"), this.hasComponent() && this.component.off(".iconpicker"), this.hasContainer() && this.container.off(".iconpicker")
            },
            _unbindWindowEvents: function() {
                l(window).off(".iconpicker.inst" + this._id), l(window.document).off(".iconpicker.inst" + this._id)
            },
            updatePlacement: function(e, a) {
                e = e || this.options.placement, this.options.placement = e, a = !0 === (a = a || this.options.collision) ? "flip" : a;
                var t = {
                    at: "right bottom",
                    my: "right top",
                    of: this.hasInput() && !this.isInputGroup() ? this.input : this.container,
                    collision: !0 === a ? "flip" : a,
                    within: window
                };
                if (this.popover.removeClass("inline topLeftCorner topLeft top topRight topRightCorner rightTop right rightBottom bottomRight bottomRightCorner bottom bottomLeft bottomLeftCorner leftBottom left leftTop"), "object" == typeof e) return this.popover.pos(l.extend({}, t, e));
                switch (e) {
                    case "inline":
                        t = !1;
                        break;
                    case "topLeftCorner":
                        t.my = "right bottom", t.at = "left top";
                        break;
                    case "topLeft":
                        t.my = "left bottom", t.at = "left top";
                        break;
                    case "top":
                        t.my = "center bottom", t.at = "center top";
                        break;
                    case "topRight":
                        t.my = "right bottom", t.at = "right top";
                        break;
                    case "topRightCorner":
                        t.my = "left bottom", t.at = "right top";
                        break;
                    case "rightTop":
                        t.my = "left bottom", t.at = "right center";
                        break;
                    case "right":
                        t.my = "left center", t.at = "right center";
                        break;
                    case "rightBottom":
                        t.my = "left top", t.at = "right center";
                        break;
                    case "bottomRightCorner":
                        t.my = "left top", t.at = "right bottom";
                        break;
                    case "bottomRight":
                        t.my = "right top", t.at = "right bottom";
                        break;
                    case "bottom":
                        t.my = "center top", t.at = "center bottom";
                        break;
                    case "bottomLeft":
                        t.my = "left top", t.at = "left bottom";
                        break;
                    case "bottomLeftCorner":
                        t.my = "right top", t.at = "left bottom";
                        break;
                    case "leftBottom":
                        t.my = "right top", t.at = "left center";
                        break;
                    case "left":
                        t.my = "right center", t.at = "left center";
                        break;
                    case "leftTop":
                        t.my = "right bottom", t.at = "left center";
                        break;
                    default:
                        return !1
                }
                return this.popover.css({
                    display: "inline" === this.options.placement ? "" : "block"
                }), !1 !== t ? this.popover.pos(t).css("maxWidth", l(window).width() - this.container.offset().left - 5) : this.popover.css({
                    top: "auto",
                    right: "auto",
                    bottom: "auto",
                    left: "auto",
                    maxWidth: "none"
                }), this.popover.addClass(this.options.placement), !0
            },
            _updateComponents: function() {
                if (this.iconpicker.find(".iconpicker-item.iconpicker-selected").removeClass("iconpicker-selected " + this.options.selectedCustomClass), this.iconpickerValue && this.iconpicker.find("." + this.options.fullClassFormatter(this.iconpickerValue).replace(/ /g, ".")).parent().addClass("iconpicker-selected " + this.options.selectedCustomClass), this.hasComponent()) {
                    var e = this.component.find("i");
                    0 < e.length ? e.attr("class", this.options.fullClassFormatter(this.iconpickerValue)) : this.component.html(this.getHtml())
                }
            },
            _updateFormGroupStatus: function(e) {
                return !!this.hasInput() && (!1 !== e ? this.input.parents(".form-group:first").removeClass("has-error") : this.input.parents(".form-group:first").addClass("has-error"), !0)
            },
            getValid: function(e) {
                r(e) || (e = "");
                var a = "" === e;
                e = l.trim(e);
                for (var t = !1, s = 0; s < this.options.icons.length; s++)
                    if (this.options.icons[s].title === e) {
                        t = !0;
                        break
                    } return !(!t && !a) && e
            },
            setValue: function(e) {
                var a = this.getValid(e);
                this.hide();
                return !1 !== a ? (this.iconpickerValue = a, this._trigger("iconpickerSetValue", {
                    iconpickerValue: a
                }), this.iconpickerValue) : (this._trigger("iconpickerInvalid", {
                    iconpickerValue: e
                }), !1)
            },
            getHtml: function() {
                return '<i class="' + this.options.fullClassFormatter(this.iconpickerValue) + '"></i>'
            },
            setSourceValue: function(e) {
                return !1 !== (e = this.setValue(e)) && "" !== e && (this.hasInput() ? this.input.val(this.iconpickerValue) : this.element.data("iconpickerValue", this.iconpickerValue), this._trigger("iconpickerSetSourceValue", {
                    iconpickerValue: e
                })), e
            },
            getSourceValue: function(e) {
                var a = e = e || this.options.defaultValue;
                return void 0 !== (a = this.hasInput() ? this.input.val() : this.element.data("iconpickerValue")) && "" !== a && null !== a && !1 !== a || (a = e), a
            },
            hasInput: function() {
                return !1 !== this.input
            },
            isInputSearch: function() {
                return this.hasInput() && !0 === this.options.inputSearch
            },
            isInputGroup: function() {
                return this.container.is(".input-group")
            },
            isDropdownMenu: function() {
                return this.container.is(".dropdown-menu")
            },
            hasSeparatedSearchInput: function() {
                return !1 !== this.options.templates.search && !this.isInputSearch()
            },
            hasComponent: function() {
                return !1 !== this.component
            },
            hasContainer: function() {
                return !1 !== this.container
            },
            getAcceptButton: function() {
                return this.popover.find(".iconpicker-btn-accept")
            },
            getCancelButton: function() {
                return this.popover.find(".iconpicker-btn-cancel")
            },
            getSearchInput: function() {
                return this.popover.find(".iconpicker-search")
            },
            filter: function(s) {
                if (t(s)) return this.iconpicker.find(".iconpicker-item").show(), l(!1);
                var r = [];
                return this.iconpicker.find(".iconpicker-item").each(function() {
                    var e = l(this),
                        a = e.attr("title").toLowerCase();
                    a = a + " " + (e.attr("data-search-terms") ? e.attr("data-search-terms").toLowerCase() : "");
                    var t = !1;
                    try {
                        t = new RegExp("(^|\\W)" + s, "g")
                    } catch (e) {
                        t = !1
                    }!1 !== t && a.match(t) ? (r.push(e), e.show()) : e.hide()
                }), r
            },
            show: function() {
                if (this.popover.hasClass("in")) return !1;
                l.iconpicker.batch(l(".iconpicker-popover.in:not(.inline)").not(this.popover), "hide"), this._trigger("iconpickerShow", {
                    iconpickerValue: this.iconpickerValue
                }), this.updatePlacement(), this.popover.addClass("in"), setTimeout(l.proxy(function() {
                    this.popover.css("display", this.isInline() ? "" : "block"), this._trigger("iconpickerShown", {
                        iconpickerValue: this.iconpickerValue
                    })
                }, this), this.options.animation ? 300 : 1)
            },
            hide: function() {
                if (!this.popover.hasClass("in")) return !1;
                this._trigger("iconpickerHide", {
                    iconpickerValue: this.iconpickerValue
                }), this.popover.removeClass("in"), setTimeout(l.proxy(function() {
                    this.popover.css("display", "none"), this.getSearchInput().val(""), this.filter(""), this._trigger("iconpickerHidden", {
                        iconpickerValue: this.iconpickerValue
                    })
                }, this), this.options.animation ? 300 : 1)
            },
            toggle: function() {
                this.popover.is(":visible") ? this.hide() : this.show(!0)
            },
            update: function(e, a) {
                return e = e || this.getSourceValue(this.iconpickerValue), this._trigger("iconpickerUpdate", {
                    iconpickerValue: this.iconpickerValue
                }), !0 === a ? e = this.setValue(e) : (e = this.setSourceValue(e), this._updateFormGroupStatus(!1 !== e)), !1 !== e && this._updateComponents(), this._trigger("iconpickerUpdated", {
                    iconpickerValue: this.iconpickerValue
                }), e
            },
            destroy: function() {
                this._trigger("iconpickerDestroy", {
                    iconpickerValue: this.iconpickerValue
                }), this.element.removeData("iconpicker").removeData("iconpickerValue").removeClass("iconpicker-element"), this._unbindElementEvents(), this._unbindWindowEvents(), l(this.popover).remove(), this._trigger("iconpickerDestroyed", {
                    iconpickerValue: this.iconpickerValue
                })
            },
            disable: function() {
                return !!this.hasInput() && (this.input.prop("disabled", !0), !0)
            },
            enable: function() {
                return !!this.hasInput() && (this.input.prop("disabled", !1), !0)
            },
            isDisabled: function() {
                return !!this.hasInput() && !0 === this.input.prop("disabled")
            },
            isInline: function() {
                return "inline" === this.options.placement || this.popover.hasClass("inline")
            }
        }, l.iconpicker = c, l.fn.iconpicker = function(a) {
            return this.each(function() {
                var e = l(this);
                e.data("iconpicker") || e.data("iconpicker", new c(this, "object" == typeof a ? a : {}))
            })
        }, c.defaultOptions = l.extend(c.defaultOptions, {
            icons: [
                {title: "linecons-music", searchTerms: []},
                {title: "linecons-search", searchTerms: []},
                {title: "linecons-mail", searchTerms: []},
                {title: "linecons-heart", searchTerms: []},
                {title: "linecons-star", searchTerms: []},
                {title: "linecons-user", searchTerms: []},
                {title: "linecons-videocam", searchTerms: []},
                {title: "linecons-camera", searchTerms: []},
                {title: "linecons-photo", searchTerms: []},
                {title: "linecons-attach", searchTerms: []},
                {title: "linecons-lock", searchTerms: []},
                {title: "linecons-eye", searchTerms: []},
                {title: "linecons-tag", searchTerms: []},
                {title: "linecons-thumbs-up", searchTerms: []},
                {title: "linecons-pencil", searchTerms: []},
                {title: "linecons-comment", searchTerms: []},
                {title: "linecons-location", searchTerms: []},
                {title: "linecons-cup", searchTerms: []},
                {title: "linecons-trash", searchTerms: []},
                {title: "linecons-doc", searchTerms: []},
                {title: "linecons-note", searchTerms: []},
                {title: "linecons-cog", searchTerms: []},
                {title: "linecons-params", searchTerms: []},
                {title: "linecons-calendar", searchTerms: []},
                {title: "linecons-sound", searchTerms: []},
                {title: "linecons-clock", searchTerms: []},
                {title: "linecons-lightbulb", searchTerms: []},
                {title: "linecons-tv", searchTerms: []},
                {title: "linecons-desktop", searchTerms: []},
                {title: "linecons-mobile", searchTerms: []},
                {title: "linecons-cd", searchTerms: []},
                {title: "linecons-inbox", searchTerms: []},
                {title: "linecons-globe", searchTerms: []},
                {title: "linecons-cloud", searchTerms: []},
                {title: "linecons-paper-plane", searchTerms: []},
                {title: "linecons-fire", searchTerms: []},
                {title: "linecons-graduation-cap", searchTerms: []},
                {title: "linecons-megaphone", searchTerms: []},
                {title: "linecons-database", searchTerms: []},
                {title: "linecons-key", searchTerms: []},
                {title: "linecons-beaker", searchTerms: []},
                {title: "linecons-truck", searchTerms: []},
                {title: "linecons-money", searchTerms: []},
                {title: "linecons-food", searchTerms: []},
                {title: "linecons-shop", searchTerms: []},
                {title: "linecons-diamond", searchTerms: []},
                {title: "linecons-t-shirt", searchTerms: []},
                {title: "linecons-wallet", searchTerms: []},
            ]
        })
    });