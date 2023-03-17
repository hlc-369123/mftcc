var fs = require("fs")

class AutoRouter {
    constructor() {
        this.root = "src/views";
        this.autoFilePath = "src/router/auto.js"
    }
    auto() {
        if (fs.existsSync(this.autoFilePath)) {
            fs.unlinkSync(this.autoFilePath);
        }
        this.autoStr = "let router = ["
        this.readDirSync(this.root, "")
        var endStr = "];export default router;"
        if (this.autoStr.endsWith(",")) {
            this.autoStr = this.autoStr.substring(0, this.autoStr.length - 1);
        }
        this.autoStr += endStr;
        fs.writeFile(this.autoFilePath, this.autoStr, function (err) {
            if (err) {
                return console.log(err);
            }
        });
    }
    readDirSync(path, upPath) {
        var _self = this;
        var pa = fs.readdirSync(path);
        pa.forEach(function (ele, index) {
            var info = fs.statSync(path + "/" + ele)
            if (info.isDirectory()) {
                let tempUpPath = upPath + "/" + ele;
                _self.readDirSync(path + "/" + ele, tempUpPath);
            } else {
                if (/.vue$/.test(ele)) {
                    var fileName = ele.substring(0, ele.length - 4);
                    var routerPath = upPath + "/" + fileName;
                    var filePath = _self.root.replace("src", "@") + routerPath;
                    var str = "{path: '" + routerPath + "', component: resolve => require.ensure([], () => resolve(require('" + filePath + "')), '" + fileName + "')},";
                    _self.autoStr += str;
                }
            }
        })
    }
}




module.exports = AutoRouter;