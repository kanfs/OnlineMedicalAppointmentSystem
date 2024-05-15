<template>
    <div class="page-container">
        <div class="personal-patient">
            <div class="header-wrapper">
                <div class="title">就诊人管理</div>
            </div>
            <div class="content-wrapper">
                <el-card class="patient-card" shadow="always" v-for="item in patientList.values" :key="item.id">
                    <template #header>
                        <div class="clearfix">
                            <div>
                                <span class="name">{{ item.name }}</span>
                                <span> {{ item.phone }}</span>
                                <div class="detail" @click="show(item.id)">
                                    查看详情 
                                    <span class="iconfont">></span>
                                </div>
                            </div>
                        </div>
                    </template>
                    <div class="card SELF_PAY_CARD">
                        <div class="info">
                            <span class="type">身份证</span>
                            <span class="card-no" style="margin-left: 3px;">{{ item.identifyCardNumber }}</span>
                            <span class="card-view" >{{ item.sex ? "男" : "女"}}</span>
                        </div>
                        <span class="operate"></span>
                    </div>
                    <div class="card">
                        <div class="text bind-card"></div>
                    </div>
                </el-card>

                <div style="width: 700px;">
                    <div class="item-add-wrapper v-card clickable" onclick="javascript:window.location='/patient/add'">
                        <div>+ 添加就诊人</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, watch} from 'vue';
import Cookies from 'js-cookie';
import Patient from './Patient.vue';
export default{
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        var patientList = reactive([]);
        var userId = ref(null);
        const getPatientList = async() =>{
            const res = await ctx.$axios.get("/patient/list",{params:{userId:userId.value}});
            patientList.values = res.data.patientList;
        }
        const show = async(patientId)=>{
            Cookies.set("patientId", patientId, {domain:"localhost"})
            ctx.$router.push("/patient/show")
        }
        onBeforeMount(async()=>{
            if( Cookies.get("token") == null ){
                ctx.$message.error("用户信息过时，请重新登录！")
                ctx.$router.push("/home")
            }else{
                userId.value = Cookies.get("userId");
                getPatientList();
            }
        })

        return{
            patientList,
            show, 
        }
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
.patient-card {
    margin-top: 30px;
    padding: 0 30px;
    width: 100%;
    height: 130px;
}

.patient-card .el-card__header {
    padding: 16px 0
}

.patient-card .el-card__header span {
    font-size: 12px;
    color: #999
}

.patient-card .el-card__header span.name {
    font-size: 19px;
    font-weight: 500;
    color: #333;
    margin-right: 30px;
    margin-left: -420px;
}

.patient-card .el-card__header .detail {
    float: right;
    color: #999;
    cursor: pointer;
}

.patient-card .el-card__body {
    padding: 20px 0;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}

.patient-card .el-card__body .card {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    border-radius: 4px;
    padding: 0 14px;
    color: #fff;
    display: -webkit-box;
    display: -ms-flexbox;
    display: inline;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -ms-flex-line-pack: center;
    align-content: center;
    margin-right: 50px;
    cursor: auto;
    height: 35px;
    width:60%;
}

.patient-card .el-card__body .card .info {
    width: 100%;
}

.patient-card .el-card__body .card .info .type {
    margin-top: 30px;
    margin-left:-100px;
    background: #fff;
    font-size: 12px;
    padding: 4px;
    border-radius: 4px;
    height:30px;
}
.patient-card .el-card__body .card .info .card-no {
    padding: 4px;
    border-radius: 4px
}
.patient-card .el-card__body .card .info .card-view {
    padding: 4px;
    border-radius: 4px
}

.patient-card .el-card__body .card.MEDICARE_CARD {
    background: #76ced5
}

.patient-card .el-card__body .card.MEDICARE_CARD .type {
    color: #76ced5
}

.patient-card .el-card__body .card.SELF_PAY_CARD {
    margin-bottom: 20px;
    width:300px;
    background: #89c4ed;
}

.patient-card .el-card__body .card.SELF_PAY_CARD .type {
    color: #89c4ed
}

.patient-card .el-card__body .card .operate {
    width: 100px;
    text-align: right
}

.patient-card .el-card__body .card .operate img {
    width: 22px;
    -webkit-transform: translateY(2px);
    transform: translateY(2px)
}

.patient-card .el-card__body .card:nth-child(2n) {
    margin-right: 0
}

.patient-card .el-card__body .card .text {
    width: 100%;
    text-align: center;
    color: #333;
    font-size: 14px
}

.patient-card .tips {
    width: 100%;
    font-size: 14px;
    color: #ff5f5f
}

.iconfont {
    font-size: 13px
}
.item-add-wrapper {
    width: 100%;
    height: 90px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    color: #4990f1;
    font-size: 16px;
    cursor: pointer;
    margin-top:30px;
    margin-left:100px;
}
.v-card {
    position: relative;
    background: #fff;
    width: 90%;
    height:60px;
    -webkit-box-shadow: 0 4px 20px 0 rgba(0, 0, 0, .04);
    box-shadow: 0 4px 20px 0 rgba(0, 0, 0, .04);
    border-radius: 4px;
    -webkit-transition: all .3s ease;
    transition: all .3s ease
}
.v-card:hover {
    -webkit-box-shadow: 0 4px 20px 0 rgba(0, 0, 0, .1);
    box-shadow: 0 4px 20px 0 rgba(0, 0, 0, .1)
}
.clearfix{
    width: 700px;
    height: 20px;
}
</style>