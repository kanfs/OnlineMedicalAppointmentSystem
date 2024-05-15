<template>
    <div class="page-container">
        <div class="personal-patient">
            <div class="header-wrapper"><div class="title">添加就诊人</div></div>
            <div>
                <div class="sub-title" style="margin-top:40px;"><div class="block"></div style="font-size:16px">就诊人信息</div>
                <div class="content-wrapper">
                    <el-form
                    :model="patient"
                    label-width="110px"
                    label-position="left"
                    ref="patientRef"
                    :rules="patientRules"
                    style="margin-left:100px; margin-top:50px;">
                        <el-form-item prop="name" label="姓名：">
                            <el-input v-model="patient.name" placeholder="请输入真实姓名" class="v-input"/>
                        </el-form-item>
                        <el-form-item prop="identifyCardNumber" label="身份证号码：" style="margin-top:40px;">
                            <el-input v-model="patient.identifyCardNumber" placeholder="请输入证件号码" class="v-input"/>
                        </el-form-item>
                        <el-form-item label="选择城市" prop="city" style="margin-top:40px;">
                            <!-- <el-text>{{ provinces[0][0].name }}</el-text> -->
                            <el-select v-model="patient.province" clearable placeholder="省份">
                                <el-option v-for="item in provinces" :key="item.code" :label="item.name" :value="item.name"></el-option>
                            </el-select>
                            <!-- <el-text>{{ cities[0].name }}</el-text> -->
                            <el-select v-model="patient.city" clearable placeholder="城市">
                                <el-option v-for="item in cities" :key="item.code" :label="item.name" :value="item.name"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="sex" label="性别：" style="margin-top:40px;">
                            <el-radio-group v-model="patient.sex">
                                <el-radio :label="true" border>男</el-radio>
                                <el-radio :label="false" border>女</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item prop="birthdate" label="出生日期：" style="margin-top:40px;">
                            <el-date-picker v-model="patient.birthdate" class="v-date-picker" type="date" placeholder="选择日期"/>
                        </el-form-item>
                        <el-form-item prop="phone" label="手机号码：" style="margin-top:40px;">
                            <el-input v-model="patient.phone" placeholder="请输入手机号码" maxlength="11" class="v-input"/>
                        </el-form-item>
                        <div class="button-wrapper" >
                            <el-button type="success" class="v-button" @click="submit" round>
                                提交
                            </el-button>
                        </div>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, getCurrentInstance, onBeforeMount, watch } from "vue";
import axios from "axios";
import Cookies from 'js-cookie';
const ctx = getCurrentInstance()!.appContext.config.globalProperties;
var userId = ref(null);
var patientId = ref(null);
export type AreaList = {
  code: string
  level: number
  name: string
  areaList: AreaList[]
}
var patient = reactive({
    name:"",
    identifyCardNumber:"",
    birthdate: new Date(),
    sex: null,
    phone:"",
    province:"",
    city:"",
});
var patientRef = ref(null);
const validatePhoneNumber = (rule: any, value: any, callback: any) => {
    const reg = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/;
    if (!reg.test(value)) {
        callback(new Error('手机号码格式错误'))
    } else {
        callback()
    }
};
const validateIdentifyCardNumber = (rule: any, value: any, callback: any) => {
    var city = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江 ", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北 ", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏 ", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外 " };
    var tip = "";
    var pass = true;
    var reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/;
    if (!reg.test(value)){
        callback(new Error('身份证号码格式错误'));
    }else if(!city[value.substr(0,2)]){
        callback(new Error('地址编码错误'));
    }else{
        //18位身份证需要验证最后一位校验位
        if (value.length == 18) {
            value = value.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            //校验位
            var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++) {
                ai = value[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if (parity[sum % 11] != value[17]) {
                callback(new Error('校验位错误'));
            }
            callback();
        }
    }
}
const patientRules = reactive({
    name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
    identifyCardNumber: [{ required: true, validator: validateIdentifyCardNumber, trigger: "blur" }],
    phone: [{ required: true, validator: validatePhoneNumber, trigger: "blur" }],
    province: [{required: true, message: "请选择省份", trigger: "blur"}],
    city: [{required: true, message: "请选择城市", trigger: "blur"}],
    sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
});
// 城市选择
var provinces = ref<AreaList[]>([]);
var cities = ref<AreaList[]>([]);
const getprovinceList = async () => {
    const res = await axios.get<AreaList[]>('/upload')
    provinces.value = res.data
}
// 获取患者信息
const getPatientInfo = async()=>{
    const res = await ctx.$axios.get("/patient/info/"+patientId.value);
    if (res.data.code !== 0){
        ctx.$message.error("不存在该名患者")
    }else{
        patient.name = res.data.name
        patient.identifyCardNumber = res.data.identifyCardNumber
        patient.birthdate = res.data.birthdate
        patient.sex = res.data.sex
        patient.phone = res.data.phone
        patient.province = res.data.province
        patient.city = res.data.city
    }
}
onBeforeMount(async()=>{
  getprovinceList();
  if (Cookies.get("token") == null){
        ctx.$message.error("请先登陆账号")
        ctx.$router.push("/home");
        
    }else{
        userId.value = Cookies.get('userId')
        if (Cookies.get("patientId") !== null){
            patientId.value = Cookies.get("patientId")
            getPatientInfo()
        }
    }
})
watch(patient, (curForm:any) => {
    for(let i in provinces.value){
        if(patient.province === provinces.value[i].name)
        {
            cities.value = provinces.value[i].areaList
        }
    }
});
const submit = async()=>{
    var res;
    if (patientId.value === null){
        res = await ctx.$axios.get("/patient/register",{params:{
            userId:userId.value, name:patient.name, identifyCardNumber:patient.identifyCardNumber, sex: patient.sex, 
            phone:patient.phone, province: patient.province, city: patient.city, 
        }})
    }else{
        res = await ctx.$axios.get("/patient/update",{params:{
            patientId:patientId.value, name:patient.name, identifyCardNumber:patient.identifyCardNumber, sex: patient.sex, 
            phone:patient.phone, province: patient.province, city: patient.city, 
        }})
    }
    
    if(res.data.code===1){
        ctx.$message.error(res.data.msg);
    }else{
        ctx.$message.success(res.data.msg);
        Cookies.set("patientId", null);
        ctx.$router.push("/patient/all");
    }
}
</script>

<style>
.page-container{
    margin-left: 0px;
    float:left ;
    width: 80%;
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
.header-wrapper .title {
    font-size: 16px;
    margin-top: 0;
}
.content-wrapper {
    margin-left: 0;
}
.title {
    letter-spacing: 1px;
    font-weight: 700;
    color: #333;
    font-size: 16px;
    font-family: '微软雅黑';
    text-align: left;
}
.content-wrapper {
    color: #999;
    font-size: 16px;
    display: table-cell; 
	vertical-align: middle; 
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
</style>