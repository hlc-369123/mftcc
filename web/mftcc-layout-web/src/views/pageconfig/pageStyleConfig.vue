<template>
  <div class="pageBox">
    <div class="pageConfin">
      <span style="font-size: 26px;">页面样式配置</span>
      <el-button type="primary" @click="submit">保存</el-button>
    </div>

    <el-form ref="form" :model="formData" label-width="160px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="系统名称" class="item">
            <el-input style="width: 96%;" v-model="formData.title"></el-input>
          </el-form-item>
        </el-col>
        <!--        <el-col :span="12">-->
        <!--          <el-form-item label="系统logo位置">-->
        <!--            <el-input v-model="formData.logoSite"></el-input>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="系统名称颜色">
            <el-color-picker v-model="formData.titleColor"></el-color-picker>
            <el-input v-model="formData.titleColor"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="登录按钮背景色">
            <el-color-picker v-model="formData.buttonColor"></el-color-picker>
            <el-input v-model="formData.buttonColor"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="输入框文字颜色">
            <el-color-picker v-model="formData.inputTextColor"></el-color-picker>
            <el-input v-model="formData.inputTextColor"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="输入区域背景颜色">
            <el-color-picker v-model="formData.inputAreaColor"></el-color-picker>
            <el-input v-model="formData.inputAreaColor"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="输入框背景色">
            <el-color-picker v-model="formData.inputColor"></el-color-picker>
            <el-input v-model="formData.inputColor"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="登录按钮文字颜色">
            <el-color-picker v-model="formData.buttonTextColor"></el-color-picker>
            <el-input v-model="formData.buttonTextColor"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="登录页背景图">
            <el-upload
              :class="{hide:hideUpload}"
              action="vartaie"
              list-type="picture-card"
              :file-list="photoList"
              :accept="'image/*'"
              :on-change="(file,fileList) => {getFile(file,fileList, '登录页背景图')}"
              :http-request="uploading"
              :on-preview="(file) => {handlePictureCardPreview(file, '登录页背景图')}"
              :on-remove="(file,fileList) => {handleRemove(file,fileList, '登录页背景图')}">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="formData.loginBackImg">
            </el-dialog>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="登陆页面logo">
            <el-upload
              :class="{hide:hideUploadLogin}"
              action="vartaie"
              list-type="picture-card"
              :file-list="loginLogoList"
              :accept="'image/*'"
              :on-change="(file,fileList) => {getFile(file,fileList, '登陆页面logo')}"
              :http-request="uploading"
              :on-preview="(file) => {handlePictureCardPreview(file, '登陆页面logo')}"
              :on-remove="(file,fileList) => {handleRemove(file,fileList, '登陆页面logo')}">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible4">
              <img width="100%" :src="formData.loginLogo" alt="">
            </el-dialog>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="系统logo">
            <el-upload
              :class="{hide:hideUploadSys}"
              action="vartaie"
              list-type="picture-card"
              :file-list="sysLogoList"
              :accept="'image/*'"
              :on-change="(file,fileList) => {getFile(file,fileList, '系统logo')}"
              :http-request="uploading"
              :on-preview="(file) => {handlePictureCardPreview(file, '系统logo')}"
              :on-remove="(file,fileList) => {handleRemove(file,fileList, '系统logo')}">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible3">
              <img width="100%" :src="formData.sysLogo" alt="">
            </el-dialog>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="宣传语">
            <el-upload
              :class="{hide:hideUploadXu}"
              action="vartaie"
              list-type="picture-card"
              :file-list="sylontList"
              :accept="'image/*'"
              :on-change="(file,fileList) => {getFile(file,fileList, '宣传语')}"
              :http-request="uploading"
              :on-preview="(file) => {handlePictureCardPreview(file, '宣传语')}"
              :on-remove="(file,fileList) => {handleRemove(file,fileList, '宣传语')}">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible2">
              <img width="100%" :src="formData.slogan" alt="">
            </el-dialog>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="加载动画">
            <el-upload
              :class="{hide:hideUploadAni}"
              action="vartaie"
              list-type="picture-card"
              :file-list="loadingList"
              :accept="'image/*'"
              :on-change="(file,fileList) => {getFile(file,fileList, '加载动画')}"
              :http-request="uploading"
              :on-preview="(file) => {handlePictureCardPreview(file, '加载动画')}"
              :on-remove="(file,fileList) => {handleRemove(file,fileList, '加载动画')}">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible1">
              <img width="100%" :src="formData.loadingImg" alt="">
            </el-dialog>
          </el-form-item>
        </el-col>
      </el-row>
<!--      <el-form-item style="text-align: center; margin-top: 20px;">-->
<!--        <el-button type="primary" @click="submit">保存</el-button>-->
<!--        <el-button>取消</el-button>-->
<!--      </el-form-item>-->
    </el-form>
  </div>
</template>

<script>
  import api from '@/api/pageconfig/pageconfig'

  export default {
    data() {
      return {
        hideUpload: false,
        hideUploadLogin: false,
        hideUploadSys: false,
        hideUploadXu: false,
        hideUploadAni: false,
        hideUploadAll: false,
        vartaie: "111",
        dialogImageUrl: '',
        dialogVisible: false,
        dialogVisible1: false,
        dialogVisible2: false,
        dialogVisible3: false,
        dialogVisible4: false,
        photoList:[],
        loginLogoList:[],
        sysLogoList:[],
        sylontList:[],
        loadingList:[],
        formData: {
          title: '',
          titleColor: '',
          logoSite: '',
          inputAreaColor: '',
          inputColor: '',
          inputTextColor: '',
          inputIconColor: '',
          buttonColor: '',
          buttonTextColor: '',
          loginBackImg: '',
          loadingImg:'',
          loginLogo: '',
          slogan: '',
          allLogo: '',
          sysLogo: '',
        }
      }
    },
    created() {
      let data = {};
      data['companyId'] = window.$productName;
      api.getList(data, res => {
        if (res.data.length > 0) {
          this.formData = res.data[0];
          if (this.formData.loginBackImg){
            let data = {};
            data.url = this.formData.loginBackImg;
            this.photoList.push(data)
          };
          if (this.formData.sysLogo){
            let data = {};
            data.url = this.formData.sysLogo;
            this.sysLogoList.push(data)
          }
          if (this.formData.loginLogo){
            let data = {};
            data.url = this.formData.loginLogo;
            this.loginLogoList.push(data)
          }
          if (this.formData.slogan){
            let data = {};
            data.url = this.formData.slogan;
            this.sylontList.push(data)
          }
          if (this.formData.loadingImg){
            let data = {};
            data.url = this.formData.loadingImg;
            this.loadingList.push(data)
          }
        }
      })
    },
    methods: {
      submit() {
        this.formData['companyId'] = window.$productName;
        api.insert(this.formData, res => {
          if (res.code == 0) {
            this.$message({
              type: 'success',
              message: '保存成功'
            })
          }
        })
      },
      uploading(file) {
        //禁止默认上传
      },
      getFile(file, fileList, type) {
        this.getBase64(file.raw).then(res => {
          if ("登录页背景图" == type) {
            this.formData.loginBackImg = res;
            this.hideUpload = fileList.length >= 1;
          } else if ("登陆页面logo" == type) {
            this.formData.loginLogo = res;
            this.hideUploadLogin = fileList.length >= 1;
          } else if ("系统logo" == type) {
            this.formData.sysLogo = res;
            this.hideUploadSys = fileList.length >= 1;
          } else if ("宣传语" == type) {
            this.formData.slogan = res;
            this.hideUploadXu = fileList.length >= 1;
          } else if ("加载动画" == type) {
            this.formData.loadingImg = res;
            this.hideUploadAni = fileList.length >= 1;
          }
        });
      },
      //移除
      handleRemove(file, fileList, type) {
        if ("登录页背景图" == type) {
          this.hideUpload = fileList.length >= 1;
        } else if ("登陆页面logo" == type) {
          this.hideUploadLogin = fileList.length >= 1;
        } else if ("系统logo" == type) {
          this.hideUploadSys = fileList.length >= 1;
        } else if ("宣传语" == type) {
          this.hideUploadXu = fileList.length >= 1;
        } else if ("加载动画" == type) {
          this.hideUploadAni = fileList.length >= 1;
        } else if ("综合logo" == type) {
          this.hideUploadAll = fileList.length >= 1;
        }
      },
      //预览
      handlePictureCardPreview(file,type) {
        if (file.raw){
          this.getBase64(file.raw).then(res => {
            this.dialogVisible = true;
            this.dialogImageUrl = res
          });
        }else if (type == '加载动画'){
          this.dialogVisible1 = true;
        }else if (type == '宣传语'){
          this.dialogVisible2 = true;
        }else if(type == '系统logo'){
          this.dialogVisible3 = true;
        }else if(type == '登陆页面logo'){
          this.dialogVisible4 = true;
        }else if(type == '登录页背景图'){
          this.dialogVisible = true;
        }
      },
      //图片转base64
      getBase64(file) {
        return new Promise(function (resolve, reject) {
          let reader = new FileReader();
          let imgResult = "";
          reader.readAsDataURL(file);
          reader.onload = function () {
            imgResult = reader.result;
          };
          reader.onerror = function (error) {
            reject(error);
          };
          reader.onloadend = function () {
            resolve(imgResult);
          };
        });
      },
    }
  }
</script>
<style>

  .hide .el-upload--picture-card {
    display: none;
  }

  .pageBox {
    width: 88%;
    margin-left: 6%;
    margin-top: 3%
  }

  .pageBox .el-input {
    position: relative;
    font-size: 14px;
    display: inline-block;
    width: 70%;
    bottom: 15px;
  }
  .pageConfin{
    position: relative;
    bottom: 5%;
    right: 3%;
  }
  .pageConfin .el-button{
    float: right;
    margin-right: 1%;
  }
  .item .el-form-item__label{
    position: relative;
    bottom: 14px;
  }
</style>

