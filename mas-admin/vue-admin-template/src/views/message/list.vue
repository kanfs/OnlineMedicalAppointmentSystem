<template>
    <div :style="{background:'url('+bgimg+')',width:'100%',height:'695px',backgroundSize:'100% 100%',backgroundRepeat:'no-repeat'}">
        <el-row :gutter="20">
            <el-col :xs="24" :sm="24">
                <el-card class="box-card" :style="{background:'url('+chatbotImg+')',width:'100%',height:'100%',backgroundSize:'100% 100%',backgroundRepeat:'no-repeat'}">
                    <el-row style="width: 100%; margin-top: 10px">
                        <el-col :span="3" :offset="1">
                            <el-card class="box-card">
                                <div v-for="(user, index) in userList"  :key="index" style="margin-bottom: 5px">
                                    <el-tag :type="index == activeIndex ? '' : 'info'"
                                        style="
                                            height: 90px;
                                            width: 110px;
                                            margin-right: 15px;
                                            cursor: pointer;
                                            font-weight: bold;
                                            font-size: 13px;
                                            text-align: center;"
                                        @click="selectUser(user, index)">
                                        <el-image
                                            style=" width: 80px; height: 80px; border-radius: 50%; margin-top: 0px;"
                                            :src=" user.headUrl == null || user.headUrl == ''  ? 'https://medical-registration-system.oss-cn-beijing.aliyuncs.com/avter/defalut.jpg': user.headUrl"
                                            fit="fill"
                                        ></el-image>
                                        <br/>
                                        {{ user.name }}
                                        <span v-if="user.status == 1">：<el-tag type="danger" size="mini">未读</el-tag></span>
                                    </el-tag>
                                </div>
                            </el-card>
                        </el-col>

                        <el-col :span="16" :offset="1" >
                            <div class="message">
                                <div v-for="(text, index) in textList" :key="index">
                                    <div v-if="text.senderType == 'DOCTOR'" style=" display: flex; align-items: center; justify-content: flex-end;">
                                        <span v-if="text.status == false" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                            未读
                                        </span>
                                        <span v-if="text.status == true" style=" margin-top: 12px; padding: 5px; font-size: 10px;">
                                            已读
                                        </span>
                                        <el-tag type="success" size="large" style=" margin-top: 10px; max-width: 80%; height: auto; white-space: normal;margin-right: 5px; ">
                                            <div style="padding:5px;font-size: 16px;height: auto;">{{ text.content }}</div>
                                        </el-tag>
                                        <el-image style=" width: 40px; height: 40px; border-radius: 50%; margin-top: 5px;"
                                            :src="avatar+'?imageView2/1/w/80/h/80'" 
                                            fit="fill"/>
                                        <br/>
                                    </div>
                                    <div v-if="text.senderType == 'USER'" style="display: flex; align-items: center;">
                                        <el-image style=" width: 40px; height: 40px; border-radius: 50%; margin-top: 5px;"
                                            :src="headUrl == null? 'https://medical-registration-system.oss-cn-beijing.aliyuncs.com/avter/defalut.jpg': headUrl"
                                            fit="fill"/>
                                        <el-tag type="success" size="large" style=" margin-top: 10px; max-width: 80%; height: auto; white-space: normal;margin-right: 5px; ">
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
                                    v-model="textInfo.content"
                                    style="width: 742px">
                                </el-input>
                                <el-button type="primary" size="default" style="font-size: 16px; padding: 10px 10px;margin-left:15px; width:100px;" @click="submitData">
                                    发送
                                </el-button>
                            </div>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>

import textApi from '@/api/message/text';
import { mapGetters } from 'vuex';
import store from '@/store'

export default {
    //import 引入的组件需要注入到对象中才能使用
    data() {
        return {
            activeIndex: 0,
            userList: [],
            userId: 0,
            textList: [],
            textInfo: {
                userId: 0,
                doctorId: store.getters.doctorId,
                senderType: "DOCTOR",
                content: '',
                status: 0,
            },
            headUrl: '',
            bgimg:require('@/assets/images/web6.png'),
            chatbotImg:require('@/assets/images/web7.png'),
        }
    },
    computed: {
    ...mapGetters([
      'avatar'
    ])
  },
    watch: {},
    mounted() {
        this.findUserList()
    },
    methods: {
        // TODO 定时任务实时查询

        findUserList() {
            textApi.userList(this.textInfo.doctorId)
                .then(res => {
                    this.userList = res.data
                    if (this.userList.length > 0) {
                        this.userId = this.userList[0].id
                        this.headUrl = this.userList[0].headUrl
                        this.findTextList()
                        setInterval(() => {
                            this.findTextList()
                        }, 5000);
                    }
                })
        },
        selectUser(user, index) {
            this.activeIndex = index
            this.userId = user.id
            this.headUrl = user.headUrl
            this.findTextList()
        },
        findTextList() {
            textApi.textList(this.textInfo.doctorId, this.userId)
                .then(res => {
                    console.log(res.data)
                    this.textList = res.data
                })
        },
        submitData() {
            // 判断输入内容是否为空
            if (this.textInfo.content == null || this.textInfo.content == '') {
                this.$message({
                    message: '咨询内容不为空',
                    type: 'warning'
                });
            } else {
                this.textInfo.userId = this.userId
                this.textInfo.senderType = "DOCTOR"
                this.textInfo.status = 0
                this.textList.push(this.textInfo)

                // 发送消息
                textApi.saveText(this.textInfo)
                    .then(res => {
                        this.textInfo = {
                            userId: 0,
                            doctorId: store.getters.doctorId,
                            senderType: "DOCTOR",
                            content: '',
                            status: 0
                        }
                    })
            }
        }

    }
}
</script>

<style>
.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}

.box-card {
    width: 98%;
    margin-left: 1%;
    margin-top: 10px;
}

.header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.tinymce-container {
    line-height: 29px;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409eff;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}

.message {
    position: relative;
    overflow: auto;
    top: 10px;
    width: 100%;
    height: 520px;
    background-color:rgba(255, 255, 255, 0.5);
    
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    padding: 5px;
}
</style>
