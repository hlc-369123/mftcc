var NodeRedis = require("./main");

let redisConfig = {
  host: '127.0.0.1',
  port: 6379,
  db: 4,
  password: 'Taiji@cxy2022'
};

let filePaths = ["static/data"];

let nodeRedis = new NodeRedis(redisConfig);
nodeRedis.saveJsonFileDataToRedis(filePaths);
