let routers = require.context(`@`, true, /\.vue$/).keys();
let spaData = {};
routers.forEach((item) => {
  const paths = item.match(/[a-zA-Z]+/g); //paths中存储了一个目录，二级目录，文件名
  let index = "components".length + 2;
  let vuePath;

  if (paths[0] != "components") {
    return false;
  }
  vuePath = item.slice(index, item.length - 4);
  // if(!spaData[paths[2]]){
  //     spaData[paths[2]] = {};
  // }
  // spaData[paths[2]][paths[3]] = require("./"+item.slice(2)).default;
  let component = require("./" + item.slice(2));
  spaData[vuePath] = component.default;
});
export default spaData;
