<template>
    <el-container class="home-page-container">
        <el-header>
            <div class="login-bar">
                <ul class="top-wrap">
                    <li class="top-name">
                        <a class="top-logo" href="http://localhost:8080/home">在线医疗预约平台</a>
                    </li>
                    <li class="top-name">
                        <a href="http://localhost:8080/home" class="top-hovor">首页</a>
                    </li>
                    <!-- <li class="top-name">
                        <a href="http://localhost:8080/home/doctor-list.html" class="top-hovor">找医生</a>
                    </li> -->
                    <!-- <li class="top-name">
                        <a href="http://localhost:8080/home/hospital-list.html" class="top-hovor">找医院</a>
                    </li> -->
                    <li class="top-name" style="margin-left:40px;">
                        <a href="http://localhost:8080/AITriage" class="top-hovor">智能分诊</a>
                    </li>
                    <li class="top-name" style="margin-left:40px;">
                        <a href="http://localhost:8080/AIConsult" class="top-hovor">医学咨询</a>
                    </li>
                    <!-- <li class="top-name">
                        <a href="http://localhost:8080/home/mainpage" class="top-hovor">挂号</a>
                    </li> -->
                    <li class="top-search" style="margin-left:40px;">
                        <div class="top-search-content">
                            <div class="top-search-input">
                                <!-- <el-input class="input" type="text" v-model="searchContent" placeholder="请输入医院名称">
                                    <template #append>
                                        <div class="right-submit"><el-button type="primary" :icon="Search" @click="search()">搜索</el-button></div>
                                        
                                    </template>
                                </el-input> -->
                                <el-autocomplete
                                    v-model="searchContent"
                                    :fetch-suggestions="querySearchAsync"
                                    clearable
                                    placeholder="请输入医院名称"
                                    @select="handleSelect"
                                    style="width: 300px;"
                                />
                            </div>
                        </div>
                    </li>
                    <!-- 未登录 -->
                    <li class="top-login-start" v-if="!logined">
                        <a href="http://localhost:8080/login">登录</a>
                        <span class="line"> | </span>
                        <a href="http://localhost:8080/register" class="register">注册</a>
                    </li>
                    <!-- 登录 -->
                    <li class="top-patient" v-else>
                        <a :href="['http://localhost:8080/user/contact']" class="user-message">
                            <img src="../assets/image/contact.png" alt="" class="message-icon">
                            <span class="message-text">联系客服</span>
                        </a>
                        <a :href="['http://localhost:8080/user']" class="patient-user">
                            <img src="../assets/image/user.png" alt="" class="user-icon">
                            <span class="user-text">个人中心</span>
                        </a>
                        <a :href="['http://localhost:8080/login']" class="work-message" @click="exit">
                            <img src="../assets/image/exit.png" alt="" class="work-icon">
                            <span class="work-text">退出登录</span>
                        </a>
                    </li>
                    <li class="top-contact">
                    <!-- <a href="http://localhost:8080/contact" rel="nofollow">
                        <img src="../assets/image/message.png" alt="消息队列" class="contact-icon">
                        <span class="contact-text">联系客服</span>
                    </a> -->
                        <el-tooltip v-if="userId != ''" effect="white" content="系统通知" placement="bottom-start">
                            <el-badge v-if="uncheckedMessageCount > 0" style="margin-top: -5px; margin-right: 10px" :value="uncheckedMessageCount">
                                <el-button :icon="Message" @click="showDrawer" circle></el-button>
                            </el-badge>
                            <el-badge v-else style="margin-top: -5px; margin-right: 10px">
                                <el-button :icon="Message" @click="showDrawer" circle></el-button>
                            </el-badge>
                        </el-tooltip>
                    </li>
                </ul>
            </div>
            <el-drawer title="系统通知" v-model="drawer" direction="rtl" :append-to-body="true" >
                    
                    <ul style="overflow: auto">
                        <li v-for="message in messageList.values">
                            <el-link :underline=false type="primary" style="font-weight: bold" @click="checkViewed(message)">
                                {{ message.title }}
                                <div v-if="message.isSee === false"
                                    style="background: red; width: 5px; height: 5px; border-radius: 50%;
                                    float: right; margin-top: 5px; margin-left: 6px;">
                                </div>
                            </el-link>
                            <br/>
                            <div style="margin-top: 10px; margin-bottom: 5px">
                                <i style="font-size: 10px; color: darkgrey; content: '\e71f' ;margin-right: 5px">{{ message.creationTime }}</i>
                                    <el-tag v-if="message.isSee === false" type="warning" size="small">待查看</el-tag>
                                    <el-tag v-if="message.isSee === true" size="small">已查看</el-tag>
                            </div>
                        </li>
                    </ul>

                <!-- 分页组件 -->
                <el-pagination
                v-model:current-page="page"
                :page-size="10"
                layout="prev, pager, next, jumper"
                :total="messageCount"
                @current-change="handleCurPageChange()"
                style = "justify-content: center"
                />

                <el-drawer title="系统消息通知查看" :append-to-body="true" v-model="innerDrawer">
                    <el-card>
                        <div slot="header" class="header">
                            <el-row :gutter="20">
                                <el-col :xs="24" :sm="24" style="font-weight: bold"> {{ innerMessage.values.title }}</el-col>
                            </el-row>
                        </div>
                        <div>
                            <span>{{ innerMessage.values.content }}</span>
                            <div style="margin-top: 100px; margin-bottom: 5px">
                                <i style="font-size: 10px; color: darkgrey; content: '\e71f'">{{ innerMessage.values.creationTime }}</i>
                            </div>
                        </div>
                    </el-card>
                </el-drawer>
            </el-drawer>
        </el-header>
        <el-main><router-view/></el-main>
        <el-footer>
            <el-text type="info" >在线医疗预约平台  联系邮箱:xxxxx@xxx</el-text>
            <div class="footer-container">
                <div class="wrapper">
                    <div>
                        <span class="record">京ICP备13018369号</span>
                        <span class="phone">电话挂号010-56253825</span>
                    </div>
                    <div class="right">
                        <span class="v-link clickable"> 联系我们 </span>
                        <span class="v-link clickable"> 合作伙伴 </span>
                        <span class="v-link clickable"> 用户协议 </span>
                        <span class="v-link clickable"> 隐私协议 </span>
                    </div>
                </div>
            </div>
        </el-footer>
    </el-container>
</template>

<script>
import Cookies from 'js-cookie';
import axios from "axios";
import {Search, Message} from '@element-plus/icons-vue';
import {ref, reactive, getCurrentInstance, onBeforeMount, onMounted} from 'vue';
export default {
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        const searchContent = ref();
        var logined = ref(false);
        var userId = ref(0);
        var page = ref(1);
        var drawer = ref(false);
        var innerDrawer = ref(false);
        var messageCount = ref(0);
        var uncheckedMessageCount = ref(0);
        var messageList = reactive([])
        var innerMessage = reactive({});
        var hospitalId = 0;
        
        onBeforeMount(async()=>{
            var token = Cookies.get("token");
            if (token==null){
                logined.value = false;
            }else{
                logined.value = true;
                userId.value = Cookies.get('userId');
                findMessageList();
            }
        });
        
        // const search = async () => {
        //     if(searchContent.value === ''){
        //         ctx.$message({ message: '请输入搜索内容', type: 'warning'});
        //     }else{
        //         const res = await axios.get("/search", {params:{searchContent: searchContent.value}});
        //     }
        // }
        const querySearchAsync = async (queryString, cb) => {
            var results = []
            if (queryString === '' ) {
                cb(results)
            }else{
                const res = await ctx.$axios.get("/hospital/vague-name",{params:{hospitalName:queryString,}});
                const list = res.data;
                results = Array.from(list).map(item => { return {value: item};});
                cb(results)
            }
        }
        const handleSelect = async (item) =>{
            const res = await ctx.$axios.get("hospital/name",{params:{name:item.value,}});
            if(res.status !== 200){
                ctx.$message({ message: '获取医院信息失败', type: 'error'});
            }else{
                // 如果当前不在医院详情页，则跳转 否则刷新当前页面
                if (hospitalId !== res.data){
                    hospitalId = res.data;
                    ctx.$router.push('/hospital/'+hospitalId);
                    // location.reload();
                }else{
                    // location.reload();
                }

            }
        }

        const showDrawer = async () =>{
            drawer.value = true;
            findMessageList()
        }
        const findMessageList = async ()=>{
            const res  = await axios.get("/message-list", {params:{userId: userId.value, page: page.value}});
            messageList.values = res.data.messageList
            uncheckedMessageCount.value = 0
            for( const message in res.data.messageList) {
                messageList.values[message].creationTime = res.data.messageList[message].creationTime.replace("T", " ")
                if (res.data.messageList[message].isSee === false) uncheckedMessageCount.value += 1
            }
            messageCount.value = res.data.messageCount
        }
        const checkViewed = async(message) => {
            const response = await axios.get("/view-message" , {params:{id: message.id}});
            findMessageList()
            innerMessage.values = message;
            innerDrawer.value = true;
        }
        const handleCurPageChange = async () =>{
            const res  = await axios.get("/message-list", {params:{userId: userId.value, page: page.value}});
            messageList.values = res.data.messageList
            messageCount.value = res.data.messageCount
        }
        const exit = async () => {
            logined.value = false;
            Cookies.remove("token");
            Cookies.remove("userId");
            Cookies.remove("name");
            window.location.href = "http://localhost:8080/login";
        }
        return {
            userId, searchContent, logined, drawer, innerDrawer, messageList, messageCount, page, innerMessage, uncheckedMessageCount, 
            exit, showDrawer, handleCurPageChange, checkViewed, querySearchAsync, handleSelect, 
            Search, Message
        };
    }
}
</script>

<style scoped>
li {
    list-style-type: none;
}
a{
    text-decoration: none;
}
.login-bar{
    height:58px;
    background:#fff;
    -webkit-box-shadow:0 2px 3px 0 rgba(0,0,0,.04),0 0 1px 0 rgba(0,0,0,.04);
    -moz-box-shadow:0 2px 3px 0 rgba(0,0,0,.04),0 0 1px 0 rgba(0,0,0,.04);
    box-shadow:0 2px 3px 0 rgba(0,0,0,.04),0 0 1px 0 rgba(0,0,0,.04);
    margin:0 auto
}
.top-wrap{
    display:flex;
    -webkit-box-align:center;
    -webkit-align-items:center;
    -moz-box-align:center;
    -ms-flex-align:center;
    align-items:center;
    width:1300px;
    height:100%;
    margin:0 auto;
    position:relative;
    top:0
}
.top-name{
    padding:0 15px;
    position:relative
}
.top-name a{
    font-size:16px;
    font-weight:400;
    color:#8591a6;
    line-height:17px;
    border-bottom: none;
}
.top-name .top-hover{
    font-size:16px;
    font-weight:500;
    color:#8591a6
}
.top-name .top-hover:hover{
    font-size:16px;
    font-weight:500;
    color:#000
}
.top-name .active{color:#000}
.top-name .active:after{
    content:"";display:
    inline-block;width:90%;
    height:6px;
    background:#055ad7;
    position:absolute;
    left:0;
    right:0;
    bottom:-21px;
    margin:0 auto
}
.top-name:first-child{padding-left:0}
.top-name .top-logo{
    width:140px;
    height:45px;
    background:no-repeat;
    background-image:url(../assets/image/omas-logo.jpg);
    -webkit-background-size:140px 45px;
    -moz-background-size:140px 45px;
    -o-background-size:140px 45px;
    background-size:140px 45px;
    display:inline-block;
    color:rgba(0,0,0,0)
}
.top-search{
    margin:-15px 10px;
    width:360px;
    height:45px;
    background:none;
    -webkit-border-radius:18px;
    -moz-border-radius:18px;
    border-radius:18px;
}
.top-search-content{
    margin:5px -20px;
    display:flex;
    -webkit-box-align:center;
    -webkit-align-items:center;
    -moz-box-align:center;
    -ms-flex-align:center;
    align-items:center;
    -webkit-box-pack:justify;
    -webkit-justify-content:space-between;
    -moz-box-pack:justify;
    -ms-flex-pack:justify;
    justify-content:space-between;
    line-height:45px
}
.top-search-input{
    width:280px;
    height:22px;
    font-weight:400;
    line-height:22px;
    margin-left:20px
}
.top-search-input .input{
    outline:none;
    border-radius: 3px;
    width:100%;
}
.top-search-input .input .el-input__wrapper {
    width: 100%;
    background-color: rgb(233, 233, 227) ; 
}
.top-search-input input::-webkit-input-placeholder{
    color:#161a23;
    font-size:14px
}
.right-submit{
    width:50px;
    height: 32px;
    outline:none;
    border:none;
    background:transparent;
    -moz-border-radius:14px;
    border-radius:14px;
    margin:0px;
    text-align:center;
    color:#4b12db;
    font-size:16px;
    line-height:28px;
    cursor:pointer
}
.top-login-start{margin-left:20px}
.top-login-start a{font-size:16px;color:#055ad7}
.top-login-start .line{padding:0 5px;color:#055ad7}
.top-contact{
    position:absolute;
    top:50%;
    right:0;
    text-align:center;
    width:200px;
    -webkit-transform:translateY(-50%);
    -moz-transform:translateY(-50%);
    -ms-transform:translateY(-50%);
    -o-transform:translateY(-50%);
    transform:translateY(-50%);
}
.top-contact .contact-icon{
    width:28px;
    height:28px;
}
.top-contact .contact-text{
    padding-top:2px;
    display:block;
    font-size:12px;
    color:#314b82;
}
.top-patient{
    text-align:center;
}
.top-patient .point{
    width:4px;
    height:4px;
    background:rgb(120, 134, 144);
    display:inline-block;
    -webkit-border-radius:50px;
    -moz-border-radius:50px;
    border-radius:50px;
    position:absolute;
    right:-1px;
    top:4px;
}
.top-patient .show{display:block}
.top-patient .message-icon{
    width:20px;
    height:20px;
    padding-top:5px;
}
.top-patient .user-icon{
    width:20px;
    height:20px;
    padding-top:5px;
}
.top-patient .work-icon{
    width:20px;
    height:20px;
    padding-top:5px;
}
.top-patient .user-message{margin:0 -20px;float:left;}
.top-patient .work-message{margin:0 -20px;float:left;}
.top-patient .patient-user{margin:0 60px;float:left;}
.top-patient .message-text{display:block;font-size:12px;color:#314b82;padding-top:5px}
.top-patient .work-text{display:block;font-size:12px;color:#314b82;padding-top:5px}
.top-patient .user-text{display:block;font-size:12px;color:#314b82;padding-top:5px}
.top-contact .contact-icon{width:28px;height:28px}
.top-contact .contact-text{padding-top:2px;display:block;font-size:12px;color:#314b82}

.footer-container {
    font-size: 12px;
    background: #fafafa;
    height: 50px;
    line-height: 50px;
    min-width: 1200px
}

.footer-container .wrapper {
    width: 1200px;
    min-width: 1200px;
    margin: 0 auto;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
    -ms-flex-direction: row;
    flex-direction: row;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between
}

.footer-container .wrapper .record {
    color: #bbb
}

.footer-container .wrapper .phone {
    margin-left: 42px
}

.footer-container .wrapper .right {
    width: 341px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between
}
.v-link.clickable {
    cursor: pointer
}

</style>