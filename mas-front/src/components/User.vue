<template>
    <div class="nav-container page-component">
        <!--左侧导航 #start -->
        <div class="left-nav">
            <div class="nav-item">
                <span class="v-link clickable dark" onclick="javascript:window.location='/order'">挂号订单</span>
            </div>
            <div class="nav-item">
                <span class="v-link clickable dark" onclick="javascript:window.location='/patient'">就诊人管理</span>
            </div>
            <div class="nav-item selected">
                <span class="v-link clickable dark" onclick="javascript:window.location='/user'">用户信息</span>
            </div>
        </div>
        <!-- 左侧导航 #end -->
        <!-- 右侧内容 #start -->
        <div class="page-container" style="align-items: center;">
            <div>
                <div >
                    <div class="sub-title"><div class="block"></div>用户信息</div>
                    <div class="content-wrapper">
                        <el-form :model="userInfo" label-width="110px" label-position="left" ref="userRef" :rules="userFormRules" style="margin-left: 100px;margin-top:20px;">
                            <el-form-item prop="emailAddress" label="账号：" style="margin-top:30px;">
                                <el-input v-model="userInfo.emailAddress" disabled class="v-input"/>
                            </el-form-item>
                            <el-form-item prop="name" label="姓名：" style="margin-top:30px;">
                                <el-input v-model="userInfo.name" placeholder="请输入真实姓名" class="v-input"/>
                            </el-form-item>
                            <el-form-item prop="password" label="新密码：" style="margin-top:30px;">
                                <el-input type="password" v-model="userInfo.password" placeholder="请输入新密码" class="v-input" show-password/>
                            </el-form-item>
                            <el-form-item prop="headUrl" label="用户头像：" style="margin-top:30px;">
                                <div class="upload-wrapper">
                                    <div class="avatar-uploader">
                                        <el-upload class="avatar-uploader" 
                                        action="http://localhost:8888/mas/admin/file/img/avter"
                                        :show-file-list="false" 
                                        :on-success="onUploadSuccess"
                                        :before-upload="beforeAvatarUpload"
                                        list-type="picture">
                                            <div class="upload-inner-wrapper">
                                                <img v-if="userInfo.headUrl" :src="userInfo.headUrl" class="avatar" style="width: 100px; height: 100px;"/>
                                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                                                <div v-if="!userInfo.headUrl" class="text">上传头像</div>
                                            </div>
                                        </el-upload>
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item prop="phoneNumber" label="手机号码：" style="margin-top:30px;">
                                <el-input v-model="userInfo.phoneNumber" placeholder="请输入手机号码" maxlength="11" class="v-input"/>
                            </el-form-item>
                            <div class="button-wrapper">
                                <el-button type="success" class="v-button" @click="confirmVisible = true" round>
                                    提交
                                </el-button>
                            </div>
                        </el-form>
                    </div>
                </div>
            </div>
        </div>
        <!-- 右侧内容 #end -->
        <!-- 确认对话框 -->
        <el-dialog v-model="confirmVisible" width="30%">
            <el-row style="font-family: '楷体';margin-left: 10%; font-size:0.8cm">你确认要进行修改吗？</el-row>
            <el-row style="font-family: '楷体';margin-left: 10%; font-size:0.8cm;margin-top: 30px">请输入你的旧密码验证身份</el-row>
            <el-input v-model="password" placeholder="输入你的旧密码" style="width: 90%;height: 40px; margin-top:30px" type="password" show-password></el-input>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="confirmVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirm">确认</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import {ref, reactive, onBeforeMount, getCurrentInstance, watch} from 'vue';
import Cookies from 'js-cookie';
import axios from 'axios';
export default{
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        var userId = ref(0);
        const userRef = ref(null);
        const userInfo = reactive({
            emailAddress : "",
            name: "",
            password : "",
            phoneNumber: "",
            headUrl: "",
        });
        const validateEmailAddress = (rule, value, callback) => {
            var emailRegExp = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
            var emailRegExp1 = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
            if ((!emailRegExp.test(value) && value != '') || (!emailRegExp1.test(value) && value != '')) {
            callback(new Error('请输入有效邮箱格式！'));
            } else {
            callback();
            }
        };
        const validatePassword = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else if (value.length > 15 || value.length <6) {
                callback(new Error("密码长度应在6-15位间!"));
            } else {
                callback();
            }
        };
        const validatePhoneNumber = (rule, value, callback) => {
            const reg = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/;
            if (!reg.test(value)) {
                callback(new Error('手机号码格式错误'));
            } else {
                callback();
            }
        };
        const userFormRules = reactive({
            emailAddress: [{ required: true, validator: validateEmailAddress, trigger: "blur" }],
            password: [{ required: true, validator: validatePassword, trigger: "blur" }],
            name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
            phoneNumber: [{ required: true, validator: validatePhoneNumber, trigger: "blur" }],
        });
        onBeforeMount(async()=>{
            if (Cookies.get("token") == null){
                ctx.$message.error("请先登陆账号")
                ctx.$router.push("/home");
                
            }else{
                userId.value = Cookies.get('userId')
                const res = await ctx.$axios.get("/user/info/"+userId.value)
                userInfo.emailAddress = res.data.emailAddress
                userInfo.phoneNumber = res.data.phoneNumber
                userInfo.name = res.data.name
                userInfo.headUrl = res.data.headUrl
            }
        });
        var confirmVisible = ref(false);
        var password = ref("");
        const onUploadSuccess = (response, file) => {
                if (response.code !== 200) {
                ctx.$message.error("上传失败");
                return;
            }
            // 填充上传文件列表
            userInfo.headUrl = response.data;
        };
        const beforeAvatarUpload = (file) => {
            const isJPG = file.type === 'image/jpeg'
            const isLt2M = file.size / 1024 / 1024 < 2

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!')
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!')
            }
            return isJPG && isLt2M
        };
        const confirm = async () =>{
            const res = await ctx.$axios.get("/user/update", 
                                {params:{userId:userId.value, password:password.value, headUrl:userInfo.headUrl,
                                        phoneNumber:userInfo.phoneNumber, newPassword:userInfo.password, name:userInfo.name}});
            if( res.data.code!== 0 ) ctx.$message.error( res.data.msg )
            else ctx.$message.success("修改成功")
            password.value = ""
            confirmVisible.value = false;
        }
        return{
            userRef, userInfo, userFormRules, confirmVisible, password, 
            confirm, onUploadSuccess, beforeAvatarUpload, 
        }
    }
}
</script>

<style>
.nav-container {
    padding-top: 38px;
    min-height: 500px;
    width: calc(100% - 200px);
    margin-left: 200px;
    margin-top: -60px;
}
.left-nav {
    position: -webkit-sticky;
    position: sticky;
    top: 14px;
    float: left
}
.nav-item {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    width: 200px;
    height: 70px;
    position: relative;
    padding-left: 20px
}

.nav-item.selected {
    font-weight: 700;
    font-size: 16px
}

.nav-item.selected:before {
    content: "";
    position: absolute;
    left: 0;
    width: 10px;
    height: 10px;
    z-index: 1;
    background: #4490f1
}
.v-link {
    color: #666;
    -webkit-transition: all .2s ease;
    transition: all .2s ease;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    letter-spacing: 1px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}
.v-link.dark {
    color: #333
}
.v-link.clickable {
    cursor: pointer
}
.v-link.selected, .v-link:hover {
    color: #4490f1
}
.page-container {
    padding-top: 38px;
    min-height: 500px;
    width: calc(100% - 200px);
    margin-left: 200px
}
.sub-title {
    letter-spacing: 1px;
    color: #999;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}
.upload-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex
}
.avatar-uploader{
    border:solid;
    border-color: aqua;
	border-radius:10px 10px 10px 10px;  /*圆弧边框*/
}
.upload-inner-wrapper {
    width: 128px;
    height: 100px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    color: #4990f1
}
.upload-inner-wrapper .icon {
    font-size: 36px
}
.upload-inner-wrapper .text {
    font-size: 14px;
    margin-top: 5px
}


.header-wrapper .title {
  font-size: 16px;
  margin-top: 0;
}
.sub-title {
    letter-spacing: 1px;
    color: #999;
    margin-top: 60px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}
.block {
    height: 14px;
    width: 4px;
    background: #4990f1;
    margin-right: 6px;
    border-radius: 2px
}

.content-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    margin-left: 140px;
    margin-top: 20px
}
.sub-title {
  margin-top: 0;
}


.button-wrapper {
  margin-top: 30px;
}
.v-button {
    background-color: rgb(239, 246, 36);
    width:30%;
    height: 40px;
    align-items: center;
    border-radius: 5px;
    cursor: pointer;
}
.dialog-input{
    margin-top: 30px;
}
.v-input {
    .el-input__wrapper{
        background-color: #baafaf;
        box-shadow: none;
        height:0px;
        margin-top:20px;
    }
    .el-input__inner{
        height: 40px;
        line-height: 40px;
        font-size: 16px;
        width: 200px;
        margin-bottom: 30px;
    }
    .el-input__suffix{
        margin-bottom: 30px;
    }
}

</style>