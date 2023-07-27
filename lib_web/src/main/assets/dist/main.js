// { "framework": "Vue"} 

/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 5);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */,
/* 1 */,
/* 2 */,
/* 3 */,
/* 4 */,
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(6)
)

/* script */
__vue_exports__ = __webpack_require__(7)

/* template */
var __vue_template__ = __webpack_require__(12)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "D:\\hzbank\\weex_project\\src\\page\\main.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-62ef4d60"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__
module.exports.el = 'true'
new Vue(module.exports)


/***/ }),
/* 6 */
/***/ (function(module, exports) {

module.exports = {
  "hello-header": {
    "height": "152",
    "paddingTop": "48",
    "backgroundColor": "#ffc300",
    "flexDirection": "row",
    "verticalAlign": "center"
  },
  "header-back": {
    "width": "48",
    "height": "48",
    "marginTop": "24",
    "marginRight": "24",
    "marginBottom": "24",
    "marginLeft": "24"
  },
  "header-txt": {
    "textAlign": "center",
    "marginRight": 0,
    "marginLeft": 0,
    "fontSize": "48",
    "lineHeight": "96",
    "color": "#FFFFFF",
    "flex": 1
  },
  "hello-content": {
    "flex": 1
  },
  "hello-list": {
    "height": "1248"
  },
  "hello-footer": {
    "height": "100",
    "textAlign": "center"
  },
  "message": {
    "fontSize": "32",
    "color": "#727272",
    "textAlign": "center"
  }
}

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _item_hello = __webpack_require__(8);

var _item_hello2 = _interopRequireDefault(_item_hello);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var meta = weex.requireModule('meta'); //
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

var deviceInfo = weex.requireModule('deviceInfo');
var modal = weex.requireModule('modal');
exports.default = {
  components: {
    ItemHello: _item_hello2.default
  },
  data: function data() {
    return {
      wx: WXEnvironment,
      name: "根路由",
      logo: 'https://gw.alicdn.com/tfs/TB1yopEdgoQMeJjy1XaXXcSsFXa-640-302.png',
      icWhiteBack: "data:image/png;base64,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",
      helloList: [{ name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }, { name: "1", age: 0, sex: null }]
    };
  },
  mounted: function mounted() {
    // modal.toast({
    //   message: WXEnvironment,
    //   duration: 1
    // })
  },

  methods: {
    toStart: function toStart(v) {
      // this.$router.push("/start")
      modal.toast({
        message: v,
        duration: 1
      });
    },
    back: function back() {
      // this.$router.back()
      var conf = JSON.stringify(weex.config.env.options.androidStatusBarHeight);
      modal.toast({
        message: conf,
        duration: 1
      });
    },
    showDevice: function showDevice() {
      // 获取设备总高度
      // deviceInfo.enableFullScreenHeight(function (info) {
      //   modal.toast({
      //     message: info,
      //     duration: 3
      //   })
      // }, {})
      var conf = JSON.stringify(weex.config);
      modal.toast({
        message: conf,
        duration: 1
      });
    },
    metaInfo: function metaInfo() {}
  }
};

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(9)
)

/* script */
__vue_exports__ = __webpack_require__(10)

/* template */
var __vue_template__ = __webpack_require__(11)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "D:\\hzbank\\weex_project\\src\\components\\item_hello.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-240bf196"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__


/***/ }),
/* 9 */
/***/ (function(module, exports) {

module.exports = {
  "item-hello-container": {
    "justifyContent": "center",
    "alignItems": "center",
    "borderBottomWidth": "1",
    "borderBottomStyle": "solid",
    "borderBottomColor": "#3D3D3D"
  },
  "logo": {
    "width": "424",
    "height": "200",
    "backgroundColor": "#3D3D3D"
  },
  "greeting": {
    "textAlign": "center",
    "marginTop": "70",
    "fontSize": "50",
    "color": "#41B883"
  },
  "message": {
    "marginTop": "30",
    "marginRight": "30",
    "marginBottom": "30",
    "marginLeft": "30",
    "fontSize": "32",
    "color": "#727272"
  },
  "item-val": {
    "flexDirection": "row"
  }
}

/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
//
//
//
//
//
//
//
//
//
//
//
//
//


// 导航 多用于 pop 返回上一页
var navigator = weex.requireModule('navigator');
// 本地数据存储
var storage = weex.requireModule("storage");
// dialog ( alert,confirm,prompt ) / toast
var modal = weex.requireModule("modal");
exports.default = {
  name: "item_hello",
  props: {
    logo: String,
    hello: Object,
    back: Object
  },
  emits: ["clickHello"],
  data: function data() {
    return {
      itemKey: [],
      itemVal: []
    };
  },
  created: function created() {
    var that = this;
    Object.keys(that.hello).forEach(function (key) {
      if (that.hello[key] !== null && that.hello[key] !== undefined) {
        that.itemKey.push(key);
        that.itemVal.push(that.hello[key]);
      }
    });
  },

  methods: {
    clickTxt: function clickTxt() {
      this.$emit("clickHello", this.logo);
    },
    clickImage: function clickImage() {
      modal.toast({
        message: JSON.stringify(this.hello),
        duration: 0.5
      });
    }
  }
};

/***/ }),
/* 11 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: ["item-hello-container"]
  }, [_c('text', {
    staticClass: ["message"],
    on: {
      "click": _vm.back
    }
  }, [_vm._v("这是另一个地址了1")]), _c('image', {
    staticClass: ["logo"],
    attrs: {
      "src": _vm.logo
    },
    on: {
      "click": _vm.clickImage
    }
  }), _c('text', {
    staticClass: ["greeting"],
    on: {
      "click": _vm.clickTxt
    }
  }, [_vm._v("传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件传递给父组件")]), _vm._l((_vm.itemKey), function(item, i) {
    return _c('div', {
      key: i,
      staticClass: ["item-val"]
    }, [_c('text', [_vm._v(_vm._s(item))]), _c('div', {
      staticStyle: {
        flex: "1"
      }
    }), _c('text', [_vm._v(_vm._s(_vm.itemVal[i]))])])
  })], 2)
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ }),
/* 12 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: ["hello-container"]
  }, [_c('div', {
    staticClass: ["hello-header"]
  }, [_c('image', {
    staticClass: ["header-back"],
    attrs: {
      "src": _vm.icWhiteBack
    },
    on: {
      "click": _vm.back
    }
  }), _c('text', {
    staticClass: ["header-txt"],
    on: {
      "click": _vm.showDevice
    }
  }, [_vm._v("这就是标题")]), _c('div', {
    staticClass: ["header-back"]
  })]), _c('div', {
    staticClass: ["hello-content"]
  }, [_c('list', {
    staticClass: ["hello-list"]
  }, [_c('cell', {
    appendAsTree: true,
    attrs: {
      "append": "tree"
    }
  }, _vm._l((_vm.helloList), function(item, i) {
    return _c('ItemHello', {
      key: i,
      attrs: {
        "logo": _vm.logo,
        "hello": item
      },
      on: {
        "clickHello": _vm.toStart
      }
    })
  }))])]), _c('div', {
    staticClass: ["hello-footer"]
  }, [_c('text', {
    staticClass: ["message"]
  }, [_vm._v("现在, 开发 Weex 项目吧 !")]), _c('text', {
    staticClass: ["message"],
    on: {
      "click": _vm.toStart
    }
  }, [_vm._v(" " + _vm._s(_vm.name))])])])
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ })
/******/ ]);