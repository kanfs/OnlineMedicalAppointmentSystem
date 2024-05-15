<template>
    <div class="register-container">
        <div class="register-box">
            <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerFormRules"
            label-width="150px"
            class="register-form"
            status-icon>
                <el-row><el-text size="large" class="text">用户注册</el-text></el-row>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="registerForm.name" placeholder=""></el-input>
                </el-form-item>
                <el-form-item label="电话号码" prop="phoneNumber">
                    <el-input v-model="registerForm.phoneNumber" placeholder="Please input your phone number"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="registerForm.password" placeholder="Please input password" type="password" show-password></el-input>
                </el-form-item>
                <el-form-item label="密码确认" prop="checkPass">
                    <el-input v-model="registerForm.checkPass" placeholder="Please input password again" type="password" show-password></el-input>
                </el-form-item>
                <!-- <CityChooser provinces="areaData" :cities="cities" id="cityChooser"/>  -->
                <el-form-item label="选择城市" prop="city">
                    <!-- <el-text>{{ provinces[0][0].name }}</el-text> -->
                    <el-select v-model="registerForm.province" clearable placeholder="省份">
                        <el-option v-for="item in provinces" :key="item.code" :label="item.name" :value="item.name"></el-option>
                    </el-select>
                    <!-- <el-text>{{ cities[0].name }}</el-text> -->
                    <el-select v-model="registerForm.city" clearable placeholder="城市">
                        <el-option v-for="item in cities" :key="item.code" :label="item.name" :value="item.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="性别" prop="sex" style="margin-top:18px;">
                    <el-radio-group v-model="registerForm.sex" style="margin-top: 20px;">
                        <el-radio label="男" border>男</el-radio>
                        <el-radio label="女" border>女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="registerForm.email" placeholder="please input your email"></el-input>
                </el-form-item>
                <el-form-item label="验证码" prop="testCode">
                    <el-input class="input" type="text" v-model="registerForm.testCode" placeholder="please input your testCode">
                        <template #append>
                            <div class="test-code-wrapper"><el-button type="primary" @click="sendCode()">发送验证码</el-button></div>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item style="margin-top: 40px;">
                    <el-button type="info" @click="resetRegisterForm()" :icon="Refresh" round>重置</el-button>
                    <el-button type="success" @click="register()" :icon="Check" round>提交注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { Plus, Refresh, Check } from '@element-plus/icons-vue';
import { ref, reactive, getCurrentInstance, watch, onBeforeMount } from "vue";
import type { FormInstance, FormRules } from 'element-plus';
import Cookies from 'js-cookie';
import axios from 'axios';
interface Form {
    name: String,
    phoneNumber: String,
    password: String,
    checkPass: String,
    province: String,
    city: String,
    sex: String,
    email: String,
    testCode: String,
};
export type AreaList = {
  code: string
  level: number
  name: string
  areaList: AreaList[]
}

const ctx = getCurrentInstance()!.appContext.config.globalProperties;
const validateOriginPassword = (rule: any, value: any, callback: any) => {
    if (value === '') {
        callback(new Error('请输入密码'))
    } else if (value.length > 15 || value.length <6) {
        callback(new Error("密码长度应在6-15位间!"))
    } else {
        callback()
  }
};
const validatePassword = (rule: any, value: any, callback: any) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== registerForm.password) {
        callback(new Error("两次输入的密码不一致!"))
    } else {
        callback()
  }
};
const validatePhoneNumber = (rule: any, value: any, callback: any) => {
    const reg = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/;
    if (!reg.test(value)) {
        callback(new Error('手机号码格式错误'))
    } else {
        callback()
    }
};
const validateEmail = (rule: any, value: any, callback: any) => {
    var emailRegExp = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
    var emailRegExp1 = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
    if ((!emailRegExp.test(value) && value != '') || (!emailRegExp1.test(value) && value != '')) {
        callback(new Error('请输入有效邮箱格式！'));
    } else {
        callback();
    }
};
const validateTestCode = (rule: any, value: any, callback: any) => {
    if (value === '') {
        callback(new Error('请输入验证码'))
    } else if (value.length > 15 || value.length <6) {
        callback(new Error("验证码长度应为4位!"))
    } else {
        callback()
  }
};

const registerForm = reactive<Form>({
    name: "",
    phoneNumber: "",
    password: "",
    checkPass: "",
    province: "",
    city: "",
    sex: "",
    email: "",
    testCode: ""
});
const registerFormRef = ref();
// 注册验证表单
const registerFormRules = reactive<FormRules>({
    name: [{required: true, message: "请输入姓名", trigger: "blur"}],
    phoneNumber: [{ required: true, validator: validatePhoneNumber, trigger: "blur" }],
    password: [{ required: true, validator: validateOriginPassword, trigger: "blur" }],
    checkPass: [{ required:true, validator: validatePassword, trigger: "blur"}],
    province: [{required: true, message: "请选择省份", trigger: "blur"}],
    city: [{required: true, message: "请选择城市", trigger: "blur"}],
    sex: [{required: true, message: "请选择性别", trigger: "blur"}],
    email:[{required: true, validator: validateEmail, trigger: "blur"}],
    testCode: [{required: true, validator: validateTestCode, trigger: "blur"}],
});
// 城市选择
var provinces = ref<AreaList[]>([]);
var cities = ref<AreaList[]>([]);
const getprovinceList = async () => {
    const res = await axios.get<AreaList[]>('/upload')
    provinces.value = res.data
}
onBeforeMount(async()=>{
  getprovinceList()
})

watch(registerForm, (curRegisterForm:any) => {
    for(let i in provinces.value){
        if(registerForm.province === provinces.value[i].name)
        {
            cities.value = provinces.value[i].areaList
        }
    }
});
// 发送验证码
const sendCode = async () => {
    if (registerForm.email === "") return ctx.$message.error("请输入邮箱");
    const res  = await axios.post("/testCode", {
        email: registerForm.email
    });
    if (res.data.code === 1) return ctx.$message.error(res.data.msg);
};
// 注册 向后台发送post请求
const register = async () => {
    registerFormRef.value.validate(async (valid: any) => {
        if ( !valid ) return ;
        const  res = await axios.post("/register", {
            name: registerForm.name,
            phoneNumber: registerForm.phoneNumber,
            password: registerForm.password,
            province: registerForm.province,
            city: registerForm.city,
            sex: registerForm.sex,
            email: registerForm.email,
            testCode: registerForm.testCode
        });
        if (res.data.code !== 0) return ctx.$message.error("注册失败");
        ctx.$message.success("注册成功");
        ctx.$router.push("/login");
    });
};
// 重置
const resetRegisterForm = () => {
    registerFormRef.value.resetFields();
};
</script>

<style lang="less" scoped>
.register-container {
  background-image: linear-gradient(to right, #24be55, #a6c1ee);
  height: 100%;
  width:100%;
}
.register-box {
    background-color: #c6d49c;
    width: 700px;
    height: 780px;
    border-radius: 15px;
    padding: 0 50px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
}
.text{
    margin-left: 33%;
    margin-top: -70px;
    margin-bottom: 10px;
    font-size: 40px;
    font-family:"楷体";
    color:#e4f497;
    padding:20px;
}
.register-form {
    position: absolute;
    top: 10%;
    left: 0%;
    bottom: 20px;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
    text-align: -webkit-center;
}
:deep(.el-form-item__label){
    margin-top: 20px;
    margin-left: 10%;
    font-size: 20px;
    font-family:"楷体";
}
.el-input{
    margin-top: 20px;
    width: 80%;
    height: 35px;
    font-size: 20px;
    font-family:"仿宋";
}
.el-select {
    margin-top: 20px;
    width: 40%;
    height: 40px;
    display: inline-block;
    position: relative;
    line-height: 32px;
}
:deep(.el-input__inner){
    width:100%;
    height:100%;
    font-size:20px;
    font-family:"仿宋";
}
.el-button.is-round{
    width: 130px;
    height: 40px;
    margin-left:70px;
    :deep(span) {
        color:#fff;
        font-size:20px;
        letter-spacing:1px;
    }
}

</style>