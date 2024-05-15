<template>
    <div>
       <div class="home page-component" :style="{background:'url('+bgimg+')',width:'90%',height:'100%',backgroundSize:'100% 100%',backgroundRepeat:'no-repeat'}">
           <div style="font-size: 18px;font-weight: bold;margin-top: 20px;text-align: center;">
               智能分诊
           </div>
           <el-row style="width: 100%; margin-top: 0px;">
               <el-card class="box-card" :style="{background:'linear-gradient(to right,#333399,#48dc92)',width:'100%',height:'100%',backgroundSize:'100% 100%',backgroundRepeat:'no-repeat'}">

               
               <el-col :span="24" :offset="4" >
                   <div class="message" >
                       <div v-for="(text, index) in textList.values" :key="index">
                           <div v-if="text.senderType == 'USER'" style=" display: flex; align-items: center; justify-content: flex-end;">
                               <!-- <span v-if="text.status == false" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                   未读
                               </span>
                               <span v-if="text.status == true" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                   已读
                                </span> -->
                               <el-tag type="warning" size="large" style=" margin-top: 10px; max-width: 80%; height: auto; white-space: normal;margin-right: 5px; ">
                                   <div style="padding:5px;font-size: 16px;height: auto;">{{ text.content }}</div>
                               </el-tag>
                               <el-image style=" width: 40px; height: 40px; border-radius: 50%; margin-top: 5px;"
                                   :src="user.headUrl == null? 'assets/image/user.png': user.headUrl" 
                                   fit="fill"/>
                               <br/>
                           </div>
                           <div v-if="text.senderType == 'DOCTOR'" style="display: flex; align-items: center">
                               <img style=" width: 40px; height: 40px; border-radius: 50%; margin-top: 5px;"
                                   src="../../assets/image/medical-robot2.png"
                                   />
                               <el-tag type="default" size="large" style=" margin-top: 10px; max-width: 80%; height: auto; white-space: normal;margin-right: 5px; ">
                                   <div style="padding:5px;font-size: 16px;height: auto;">{{ text.content }}</div>
                               </el-tag>
                               <!-- <span v-if="text.status == false" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                   未读
                               </span>
                               <span v-if="text.status == true" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                   已读
                               </span> -->
                               <br/>
                           </div>
                       </div>
                   </div>
                   <div style="margin-top: 15px; margin-left:-10%;">
                       <el-input
                           placeholder="请输入内容："
                           clearable
                           v-model="consult"
                           style="width: 842px"
                           @keyup.enter.native="submitData">
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
       var bgimg = ref(require("@/assets/image/bg-image12.png"));
       var chatbotImg = ref(require("@/assets/image/bg-image13.png"));
       onBeforeMount(async () => {
           if (Cookies.get("token") == null){
               ctx.$message.error("请先登陆账号")
               ctx.$router.push("/home");
               
           }else{
               doctorId.value = 1;
               userId.value = Cookies.get("userId");
               const res = await ctx.$axios.get("/user-text/"+doctorId.value+"/"+userId.value);
               textList.values = res.data.data;
               if (res.data.code !== 200){
                   ctx.$message.error("获取对话记录失败")
                   ctx.$router.push("/home");
               }
               const response = await ctx.$axios.get("/user/info/"+userId.value);

               user.value = response.data;
           }
       });
       const submitData = async () => {
           const res = await ctx.$axios.get("/text-ai-triage",{
               params:{
                   doctorId:doctorId.value,
                   userId:userId.value,
                   content:consult.value, 
               }
           })
           consult.value = "";
           if (res.data.code !== 200){
               ctx.$message.error("发送消息失败")
           }else{
               ctx.$message.success("发送消息成功")
               const response = await ctx.$axios.get("/user-text/"+doctorId.value+"/"+userId.value);
               textList.values = response.data.data;

           }
       }
       return {
           textList, doctor, consult, user, bgimg, chatbotImg, 
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
   width: 1000px;
   height: 100%;
   margin: 0 auto;
   padding-bottom: 40px
}

.message {
   position: relative;
   overflow: auto;
   margin-left:-5%;
   top: 5px;
   width: 100%;
   height: 600px;
   background-color:rgba(255, 255, 255, 0.5);
   box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
   padding: 5px;
}

.box-card {
   margin-top: 10px;
   height:100%;
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