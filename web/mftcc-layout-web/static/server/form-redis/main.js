var redis = require('redis');
var fs = require('fs');
var nodePath = require('path');

class NodeDemo {
  constructor(option) {
    this.client = redis.createClient(option);
    this.client.on('error', (err) => {
      throw err;
    })
  }

  /**
   * 保存文件里的JSON数据到Redis里
   */
  saveJsonFileDataToRedis () {
    if (arguments.length == 0) {
      throw '至少需要一个参数'
    }
    let _self = this;
    console.log('\x1B[32m%s\x1B[0m', '开始向Redis内写入数据！')
    for (let i = 0; i < arguments.length; i++) {
      // 将参数转换为数组
      _self
        .strConvertArr(arguments[i])
        .then(async (paths) => {
          for (let f = 0; f < paths.length; f++) {
            var filesList = [];
            try {
              _self.readFileList(paths[f], [".json"], filesList)
            } catch {
              console.error('\x1B[31m%s\x1B[0m', paths[f] + " 读取失败，请检查目标是否存在")
              continue;
            }
            console.log('\x1B[32m%s\x1B[0m', '读取 ' + paths[f] + ' 成功，' + '开始保存，请稍后...')
            await _self
              .readFileByJsonStr(filesList)
              .then(async (jsonMap) => {
                for (var [key, value] of jsonMap) {
                  await _self
                    .setPromise(key, JSON.stringify(JSON.parse(value)))
                    .then((key) => {
                      // console.log("保存文件：" + key + " 成功");
                    })
                    .catch((err) => {
                      console.error('\x1B[31m%s\x1B[0m', key + " 保存失败；失败原因: " + err)
                    })
                }
              })
              .catch((e) => {
                throw e;
              })
              .finally(() => {
                console.log('\x1B[32m%s\x1B[0m', '保存 ' + paths[f] + ' 完毕！')
              })
          }
        })
        .catch((e) => {
          throw e;
        })
        .finally(() => {
          console.log('\x1B[32m%s\x1B[0m', '执行结束，断开redis连接！')
          this.client.quit();
        })
    }
  }

  /**
   * 验证参数是否为数组，如果不是数组，则统一转换为数组处理
   * @param {*} arg 
   */
  strConvertArr (arg) {
    return new Promise((resolve, reject) => {
      if (Object.prototype.toString.call(arg) == '[object Array]') {
        resolve(arg);
      }

      if (Object.prototype.toString.call(arg) == '[object String]') {
        resolve([arg]);
      }
      reject();
    })
  }

  /**
   * 读取目录下的文件
   * @param {*} dir 
   * @param {*} suffixArr 
   * @param {*} filesList 
   */
  readFileList (dir, suffixArr, filesList = []) {
    let _self = this;
    let stat = fs.statSync(dir);
    if (stat.isDirectory()) {
      const files = fs.readdirSync(dir);
      files.forEach((item) => {
        let fullPath = nodePath.join(dir, item);
        const stat = fs.statSync(fullPath);
        if (stat.isDirectory()) {
          _self.readFileList(fullPath, suffixArr, filesList); //递归读取文件
        } else {
          if (suffixArr.includes(nodePath.extname(fullPath))) {
            filesList.push(fullPath);
          }
        }
      });
    } else {
      if (suffixArr.includes(nodePath.extname(dir))) {
        filesList.push(dir);
      }
    }
  }

  /**
   * 根据路径地址，读取JSON字符串
   * @param {*} filePaths 文件路径地址（多个）
   */
  readFileByJsonStr (filePaths) {
    let _self = this;
    return new Promise(async (resolve, reject) => {
      let jsonMap = new Map();
      for (let f = 0; f < filePaths.length; f++) {
        await _self.readFile(filePaths[f]).then((person) => {
          let key = filePaths[f].substring(filePaths[f].lastIndexOf(nodePath.sep, filePaths[f].lastIndexOf(nodePath.sep) - 1) + 1, filePaths[f].lastIndexOf(nodePath.extname(filePaths[f])))
          jsonMap.set(key, person);
        })
      }
      if (jsonMap.size == 0) {
        reject("未读取到数据");
      } else {
        resolve(jsonMap);
      }
    })
  }

  readFile (filePath) {
    return new Promise((resolve, reject) => {
      fs.readFile(filePath, (err, data) => {
        if (err) {
          reject(err);
        }
        let person = data.toString();
        resolve(person)
      })
    })
  }

  setPromise (key, value) {
    let _self = this;
    return new Promise((resolve, reject) => {
      _self.client.set(key, value, (error) => {
        if (error) {
          reject(error)
        } else {
          resolve(key);
        }
      })
    })
  }
}

module.exports = NodeDemo;