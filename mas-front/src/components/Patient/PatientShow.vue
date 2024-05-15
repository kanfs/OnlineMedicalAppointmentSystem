<template>
    <div class="page-container">
        <div class="personal-patient">
            <div class="header-wrapper">
                <div class="title">就诊人信息</div>
            </div>
            <div class="content-wrapper">
                <el-form :model="patient" label-width="110px" label-position="left" style="margin-top: 30px; margin-left:100px">
                    <el-form-item label="姓名：">
                        <div><span>{{ patient.name }}</span></div>
                    </el-form-item>
                    <el-form-item label="证件类型：">
                        <div><span>身份证</span></div>
                    </el-form-item>
                    <el-form-item label="证件号码：">
                        <div><span>{{ patient.identifyCardNumber }}</span></div>
                    </el-form-item>
                    <el-form-item label="性别：">
                        <div><span>{{ patient.sex == 1 ? "男" : "女" }}</span></div>
                    </el-form-item>
                    <el-form-item label="出生日期：">
                        <div><span>{{ patient.birthdate.toLocaleString() }}</span></div>
                    </el-form-item>
                    <el-form-item label="手机号码：">
                        <div><span>{{ patient.phone }}</span></div>
                    </el-form-item>
                    <el-form-item label="地址：">
                        <div><span>{{ patient.address }}</span></div>
                    </el-form-item>
                    <el-form-item label="状态：">
                        <div><span>认证{{ patient.status == 1 ? "通过" : "未通过" }}</span></div>
                    </el-form-item>
                    <br/>
                    <el-form-item>
                        <el-button class="patient-show-button" type="primary" @click="remove()">删除就诊人</el-button>
                        <el-button class="patient-show-button" type="white" @click="edit()">修改就诊人</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, watch} from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import Cookies from 'js-cookie';
export default{
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        var patientId = ref(null);
        var patient = reactive({
            name:"",
            identifyCardNumber:"",
            birthdate: new Date(),
            sex: null,
            phone:"",
            address:"",
        });
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
                patient.address = res.data.address
                patient.status = res.data.status
            }
        }
        const remove = async() => {
        ElMessageBox.confirm('该操作会删除患者记录。 确认继续?','Warning',
            {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
                const res = await ctx.$axios.get("/patient/delete/"+patientId.value)
                Cookies.set("patientId", null)
                if (res.data.code===0){
                    ElMessage({type: 'success',message: '删除成功',})
                } else{
                    ElMessage({type: 'error',message: res.data.msg,})
                }
                ctx.$router.push("/patient/all")
            }).catch(() => {
                ElMessage({
                    type: 'info',
                    message: 'Delete canceled',
                })
                })
        }
        const edit = async()=>{
            ctx.$router.push("/patient/add")
        }
        onBeforeMount(async()=>{
            if( Cookies.get("token") == null ){
                ctx.$message.error("用户信息过时，请重新登录！")
                ctx.$router.push("/home")
            }else if(Cookies.get("patientId") !== 0){
                patientId.value = Cookies.get("patientId");
                getPatientInfo();
            }else{
                ctx.$message.error("患者信息出错，请重新进入！")
                Cookies.set("patientId", null)
                ctx.$router.push("/patient/all")
            }
        })
        return{
            patient, remove, edit
        }
    }
}
</script>

<style>
.patient-show-button {
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    background-color: #4490f1;
    -webkit-box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .5);
    box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .5);
    border-radius: 4px;
    height: 44px;
    color: #fff;
    font-size: 16px;
    font-weight: 500;
    -webkit-transition: all .2s ease;
    transition: all .2s ease;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    cursor: pointer;
    span{
        color: black;
    }
}
</style>