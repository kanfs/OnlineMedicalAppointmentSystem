<template>
     <div>
        <div class="home page-component" :style="{background:'url('+bgimg+')',width:'100%',height:'100%',backgroundSize:'100% 100%',backgroundRepeat:'no-repeat'}">
            <div style="font-size: 18px;font-weight: bold;margin-top: 20px;text-align: center;">
                医师咨询
            </div>
            <el-row style="width: 100%; margin-top: 0px;">
                <el-card class="box-card" :style="{background:'linear-gradient(to right,#333399,#48dc92)',width:'100%',height:'100%',backgroundSize:'100% 100%',backgroundRepeat:'no-repeat'}">

                <el-col :span="20" :offset="4">
                    <el-descriptions :column="4" style="margin-top: 28px;width:100%;background-color: rgba(255, 255, 255, 0.7);" >
                        <el-descriptions-item :align="center" :width="240">
                            <template #label >
                                    <el-icon style="size:6px"><user/></el-icon>
                                    姓名
                            </template>
                            {{ doctor.name }}
                        </el-descriptions-item>
                        <el-descriptions-item :align="center" :width="240" >
                            <template #label >
                                    <el-icon style="size:6px"><medal/></el-icon>
                                    级别
                            </template>
                            <template #default >
                                {{ doctor.grade }}
                            </template>
                        </el-descriptions-item>
                        <el-descriptions-item :align="center" :width="240">
                            <template #label >
                                    <el-icon style="size:6px"><coin/></el-icon>
                                    职称
                            </template>
                            <template #default >
                                {{ doctor.title }}
                            </template>
                        </el-descriptions-item>
                        <el-descriptions-item :align="center" :width="247">
                            <template #label >
                                    <el-icon style="size:6px"><star/></el-icon>
                                    患者推荐度
                            </template>
                            <template #default >
                                {{ doctor.score }}
                            </template>
                        </el-descriptions-item>
                        <!-- <el-descriptions-item >
                            <template #label />
                            <el-button type="primary" @click="consultDoctor(item.id)">咨询</el-button>
                        </el-descriptions-item> -->
                        <el-descriptions-item label="" :align="center" label-align="right">{{ doctor.goodat }}</el-descriptions-item>
                    </el-descriptions>
                </el-col>
                <el-col :span="20" :offset="4" >
                    <div class="message" >
                        <div v-for="(text, index) in textList.values" :key="index">
                            <div v-if="text.senderType == 'USER'" style=" display: flex; align-items: center; justify-content: flex-end;">
                                <span v-if="text.status == false" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                    未读
                                </span>
                                <span v-if="text.status == true" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                    已读
                                    </span>
                                <el-tag type="warning" size="large" style=" margin-top: 10px; max-width: 80%; height: auto; white-space: normal;margin-right: 5px; ">
                                    <div style="padding:5px;font-size: 16px;height: auto;">{{ text.content }}</div>
                                </el-tag>
                                <el-image style=" width: 40px; height: 40px; border-radius: 50%; margin-top: 5px;"
                                    :src="user.headUrl == null? 'https://medical-registration-system.oss-cn-beijing.aliyuncs.com/avter/defalut.jpg': user.headUrl" 
                                    fit="fill"/>
                                <br/>
                            </div>
                            <div v-if="text.senderType == 'DOCTOR'" style="display: flex; align-items: center">
                                <img style=" width: 40px; height: 40px; border-radius: 50%; margin-top: 5px;"
                                    src="../../assets/image/doctor.png"
                                    />
                                <el-tag type="default" size="large" style=" margin-top: 10px; max-width: 80%; height: auto; white-space: normal;margin-right: 5px; ">
                                    <div style="padding:5px;font-size: 16px;height: auto;">{{ text.content }}</div>
                                </el-tag>
                                <span v-if="text.status == false" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                    未读
                                </span>
                                <span v-if="text.status == true" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                    已读
                                </span>
                                <br/>
                            </div>
                        </div>
                    </div>
                    <div style="margin-top: 15px">
                        <el-input
                            placeholder="请输入内容："
                            clearable
                            v-model="consult"
                            style="width: 842px">
                        </el-input>
                        <el-button type="primary" size="default" style="font-size: 16px; padding: 10px 10px;margin-left:15px; width:100px;" @click="submitData">
                            发送
                        </el-button>
                    </div>
                </el-col>
                </el-card>
            </el-row>
        </div>
    </div>
</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, watch} from 'vue';
import Cookies from 'js-cookie';
export default{
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        const doctorId = ref(0);
        const userId = ref(0);
        var textList = reactive([]);
        var doctor = ref({});
        var user = ref({});
        var consult = ref("");
        var bgimg = ref(require("@/assets/image/web6.png"));
        // var chatbotImg = ref(require("@/assets/image/web7.png"));
        onBeforeMount(async () => {
            if (Cookies.get("token") == null){
                ctx.$message.error("请先登陆账号")
                ctx.$router.push("/home");
                
            }else{
                doctorId.value = ctx.$route.params.doctorId;
                userId.value = Cookies.get("userId");
                const res = await ctx.$axios.get("/user-text/"+doctorId.value+"/"+userId.value);
                textList.values = res.data.data;
                if (res.data.code !== 200){
                    ctx.$message.error("获取对话记录失败")
                    ctx.$router.push("/home");
                }
                const response1 = await ctx.$axios.get("/doctor/info/"+doctorId.value);
                doctor.value = response1.data.doctor;
                const response2 = await ctx.$axios.get("/user/info/"+userId.value);

                user.value = response2.data;
            }
        });
        const submitData = async () => {
            const res = await ctx.$axios.get("/text-doctor",{
                params:{
                    doctorId:doctorId.value,
                    userId:userId.value,
                    content:consult.value, 
                }
            })
            if (res.data.code !== 200){
                ctx.$message.error("发送消息失败")
            }else{
                ctx.$message.success("发送消息成功")
                const response = await ctx.$axios.get("/user-text/"+doctorId.value+"/"+userId.value);
                textList.values = response.data.data;
            }
        }
        return {
            textList, doctor, consult, user, bgimg, // chatbotImg, 
            submitData, 
        }
    }
}
</script>

<style scoped>
.home {
    background: #fff;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column
}
.page-component {
    width: 1200px;
    min-width: 1200px;
    height: 100%;
    margin: 0 auto;
    padding-bottom: 40px
}

.message {
    position: relative;
    overflow: auto;
    top: 5px;
    width: 100%;
    height: 520px;
    background-color:rgba(255, 255, 255, 0.5);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    padding: 5px;
}

.box-card {
    width: 98%;
    margin-top: 10px;
}

:deep(.el-descriptions__body){
    background-color: transparent;
}
:deep(.el-descriptions__table){
    background-color: transparent;
}
:deep(.el-descriptions__cell){
    background-color: transparent;
}
:deep(.el-descriptions__label){
    background-color: transparent;
}
:deep(td){
    background-color: transparent;
}
</style>