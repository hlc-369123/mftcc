/**
 * 创建模块化引用的importMap
 */
function createImportMap() {
    var importMap = window.config.web_path;
    var imports = {
        "plt-js": "/" + $productName + "/static/libs/base/js/app.js",
        "single-spa": "/" + $productName + "/static/libs/singleSpa/single-spa.dev.js",
        "plt-dev": "/" + $productName + "/static/libs/designer/js/designer.js",
        "flowable-dev": "/" + $productName + "/static/libs/flowable/js/designer.js",
    };
    for (var i in importMap) {
        var fresh_link = new Date().getTime() + Math.floor(Math.random() * 1000000); //获取当前时间戳
        var name, path;
        if (!importMap[i].path || importMap[i].path.indexOf("http") == 0) {
            if ($buildType == "config-dev.js") {
                name = importMap[i].name;
                fresh_link = name == "mftcc-layout-web" ? "" : "?time=" + fresh_link;
                path = importMap[i].path + "/" + name + "/static/js/app.js" + fresh_link;
            } else if ($buildType == "config-prod.js") {
                if (typeof importMap[i] == "object") {
                    name = importMap[i].name;
                    fresh_link = name == "mftcc-layout-web" ? "" : "?time=" + fresh_link;
                    path = importMap[i].path + "/" + name + "/static/js/app.js" + fresh_link;
                } else {
                    name = importMap[i];
                    fresh_link = name == "mftcc-layout-web" ? "" : "?time=" + fresh_link;
                    path = "/" + $productName + "/web/" + name + "/static/js/app.js" + fresh_link;
                }
            }
        } else {
            name = importMap[i].name;
            path = importMap[i].path;
        }
        imports[name] = path;
    }
    var script = document.createElement('script');
    script.type = 'systemjs-importmap';
    script.textContent = JSON.stringify({
        imports: imports
    });

    document.head.appendChild(script);
}

/**
 * 判断是否是框架项目
 * @param {*} name  项目名称
 * @returns 
 */
function isLayout(name) {
    var appArr = ["mftcc-layout-web", "mftcc-base-npm"]
    if (appArr.indexOf(name) != -1) {
        return true;
    } else {
        return false;
    }
}

/**
 * 模块化引用并初始化微前端框架相关
 */
function initSingleSPA() {
    Promise.all([
        System.import('single-spa'),
        System.import('plt-js'),
    ]).then(function (res) {
        var singleSpa = res[0];
        var base = res[1];
        var loading;
        window.addEventListener('single-spa:before-routing-event', function (evt) {
            var name = evt.detail.appsByNewStatus.MOUNTED[0];
            if (name) {
                const microApp = document.getElementById("microApp");
                if (microApp != null) {
                    microApp.className = name;
                }
            }
            loading = window.ELEMENT.Loading.service({
                fullscreen: true,
                lock: true,
                text: '服务加载中',
                background: 'rgba(0, 0, 0, 0)'
            });
        });
        window.addEventListener('single-spa:routing-event', function (evt) {
            var name = evt.detail.appsByNewStatus.MOUNTED[0];
            if (name) {
                const microApp = document.getElementById("microApp");
                if (microApp != null) {
                    microApp.className = name;
                }
            }
            var href = window.location.href;
            if (isLayout(name) && href.indexOf("dev") == -1) {
                addToSingleSpa(singleSpa);
            }
            if (loading) {
                loading.close();
            }
        });
        var href = window.location.href;
        if (href.indexOf("/dev") != -1 || href.indexOf("/dev/") != -1) {
            addToSingleSpa(singleSpa, "dev");
        } else {
            addToSingleSpa(singleSpa, "layout");
        }
        singleSpa.addErrorHandler(err => {
            if (singleSpa.getAppStatus(err.appOrParcelName) === singleSpa.LOAD_ERROR) {
                System.delete(System.resolve(err.appOrParcelName));
            }
        });
        singleSpa.start();
        window.$singleSpa = singleSpa;
        Vue.prototype.$singleSpa = $singleSpa;

        /* 初始化平台组件库 */
        initPltLib(base);
    })
}

/**
 * 根据不同状态将项目初始化到singleSpa中
 * @param {*} singleSpa 
 * @param {*} state 
 */
function addToSingleSpa(singleSpa, state) {
    var importMap = window.config.web_path;
    for (var r in importMap) {
        (function () {
            var appName;
            if ($buildType == "config-dev.js") {
                appName = importMap[r].name;
            } else if ($buildType == "config-prod.js") {
                appName = importMap[r];
                if (typeof appName == "object") {
                    appName = appName.name;
                }
            }
            if (state == "dev") {
                if (!isLayout(appName)) {
                    singleSpa.registerApplication(
                        appName,
                        function () {
                            return System.import(appName);
                        },
                        function (location) {
                            return false;
                        }
                    );
                }
            } else if (state == "layout") {
                if (isLayout(appName)) {
                    singleSpa.registerApplication(
                        appName,
                        function () {
                            return System.import(appName);
                        },
                        function (location) {
                            return true;
                        }
                    );
                }
            } else {
                if (!isLayout(appName)) {
                    singleSpa.registerApplication(
                        appName,
                        function () {
                            return System.import(appName);
                        },
                        function (location) {
                            return location.pathname.indexOf(appName) != -1;
                        }
                    );
                }
            }
        })();
    }
}

/**
 * 替换配置中的浏览器页签标题和图标
 */
function setTitleIcon() {
    document.title = window.config.title || document.title;
    var link = window.config.icon;
    if (link) {
        var $favicon = document.querySelector('link[rel="icon"]');
        if ($favicon !== null) {
            $favicon.href = link;
        } else {
            $favicon = document.createElement("link");
            $favicon.rel = "icon";
            $favicon.href = link;
            document.head.appendChild($favicon);
        }
    }
}