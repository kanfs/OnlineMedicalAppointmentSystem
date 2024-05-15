<template>
    <div class="home page-component">
        <el-carousel indicator-position="outside" autoplay="true" interval="2000">
            <el-carousel-item>
                <img src="../assets/image/web7.png" alt=""/>
            </el-carousel-item>
            <el-carousel-item>
                <img src="../assets/image/web4.png" alt=""/>
            </el-carousel-item>
            <el-carousel-item>
                <img src="../assets/image/web5.png" alt=""/>
            </el-carousel-item>
        </el-carousel>
        <!-- bottom -->
        <div class="bottom">
            <div class="left">
                <div class="home-filter-wrapper">
                    <div class="nav-container page-component">
                        <!--左侧导航 #start -->
                        <div class="nav left-nav">
                            <div class="nav-item selected">
                                <span class="v-link selected dark">医院详情</span>
                            </div>
                            <!-- <div class="nav-item">
                                <span class="v-link clickable dark" @click="appointmentNotice">预约须知</span>
                            </div> -->
                            <div class="nav-item">
                                <span class="v-link clickable dark" @click="gotoReservation">预约挂号</span>
                            </div>
                            <div class="nav-item">
                                <span class="v-link clickable dark" @click="gotoConsultation">病情咨询</span>
                            </div>
                        </div>
                        <!-- 左侧导航 #end -->

                        <!--  右侧内容  #start  -->
                        <div class="page-container">
                            <div class="hospital-detail">
                                <div class="common-header">
                                    <div class="title-wrapper">
                                        <span class="hospital-title">{{hospital.name}}</span>
                                        <div class="icon-wrapper">
                                            <el-icon><CaretTop /></el-icon>{{hospital.style}}
                                            <el-icon><CollectionTag/></el-icon>{{hospital.level}}
                                            <el-icon><Finished /></el-icon>{{hospital.type}}
                                            
                                        </div>
                                    </div>
                                </div>
                                <div class="info-wrapper" style=" width: 79%; padding-left: 0px; padding-top: 0px; flex-direction: row;">
                                    <img
                                        src="../assets/image/hospital-image3.png"
                                        :alt="hospital.name"
                                        style="width: 150px; height: 150px ;margin-left: -20px; margin-top: -20px;"
                                    />
                                    <div>
                                        <div class="icon-text-wrapper">
                                            <el-icon><Position /></el-icon>
                                            <span class="text"style="font-size: 15px">
                                                <p>{{ hospital.route }}</p> 
                                            </span>
                                            <span class="iconfont right-icon"></span>
                                        </div>
                                    </div>
                                    <div class="content-wrapper" style="margin-top: -30px; ">
                                        <div>&nbsp;</div>
                                        
                                        <div class="common-header">
                                            <div class="title-wrapper" style="font-size: 15px">
                                                <span>医院编号：{{ hospital.id }}</span>
                                            </div>
                                        </div>

                                        <div>&nbsp;</div>
                                        <div class="common-header">
                                            <div class="title-wrapper" style="font-size: 15px">
                                                <span>医院地址：</span>
                                            </div>
                                        </div>
                                        <div class="common-header">
                                            <div class="title-wrapper" style="font-size: 15px">
                                                <span style="text-align:left;">{{ hospital.address }}</span>
                                            </div>
                                        </div>

                                        <div>&nbsp;</div>
                                        <div class="common-header">
                                            <div class="title-wrapper" style="font-size: 15px">
                                                <span>医院电话：</span>
                                            </div>
                                        </div>
                                        <div class="common-header">
                                            <div class="title-wrapper" style="font-size: 15px">
                                                <span style="text-align:left;">{{ hospital.telephone }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                

                                <div class="title mt40">医院介绍</div>

                                <div class="detail-content mt40" style="width: 79%">
                                    <p v-html="hospital.intro"></p>
                                </div>
                            </div>
                        </div>
                        <!--  右侧内容  #end  -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { reactive, ref, onBeforeMount, getCurrentInstance, onBeforeUpdate } from 'vue';
import Cookies from 'js-cookie';
export default {
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        var hospitalId = ref(0);
        var hospital = reactive({ });
        onBeforeMount(async () => {
            if (Cookies.get("token") !== null){
                hospitalId.value = ctx.$route.params.hospitalId;
                const res = await ctx.$axios.get("admin/information/hospital/get/"+hospitalId.value)
                if (res.data.code !== 200){
                    ctx.$message.error("不存在对应医院")
                    ctx.$router.push("/home");
                }
                hospital.id = res.data.data.id;
                hospital.name = res.data.data.name;
                hospital.province = res.data.data.province;
                hospital.city = res.data.data.city;
                hospital.style = res.data.data.style;
                hospital.level = res.data.data.level;
                hospital.type = res.data.data.type;
                hospital.address = res.data.data.address.substring(3);
                hospital.departmentNum = res.data.data.departmentNum;
                hospital.doctorNum = res.data.data.doctorNum;
                hospital.telephone = res.data.data.telephone.substring(3);
                hospital.intro = res.data.data.intro;
            }else{
                ctx.$message.error("请先登陆账号")
                ctx.$router.push("/home");
            }
        });
        onBeforeUpdate(async () => {
            if (Cookies.get("token") == null){
                ctx.$message.error("请先登陆账号")
                ctx.$router.push("/home");
                
            }else{
                hospitalId.value = ctx.$route.params.hospitalId;
                const res = await ctx.$axios.get("admin/information/hospital/get/"+hospitalId.value)
                if (res.data.code !== 200){
                    ctx.$message.error("不存在对应医院")
                    ctx.$router.push("/home");
                }
                hospital.id = res.data.data.id;
                hospital.name = res.data.data.name;
                hospital.province = res.data.data.province;
                hospital.city = res.data.data.city;
                hospital.style = res.data.data.style;
                hospital.level = res.data.data.level;
                hospital.type = res.data.data.type;
                hospital.address = res.data.data.address.substring(3);
                hospital.departmentNum = res.data.data.departmentNum;
                hospital.doctorNum = res.data.data.doctorNum;
                hospital.telephone = res.data.data.telephone.substring(3);
                hospital.intro = res.data.data.intro;
            }
        });
        const gotoReservation = () => {
            ctx.$router.push({path: "/reservation/"+hospitalId.value})
        };
        const gotoConsultation = () => {
            ctx.$router.push({path: "/consultation/"+hospitalId.value})
        }
        return {
            hospital,
            gotoReservation, gotoConsultation, 
        }
    }
}
</script>

<style scoped>
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
    margin-left: 20px
}
.hospital-detail .info-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    margin-top: 30px;

}

.hospital-detail .info-wrapper .space {
    margin-bottom: 10px
}

.hospital-detail .info-wrapper .content-wrapper {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    margin-left: 30px;
}

.hospital-detail .info-wrapper .hospital-img {
    height: 80px;
    width: 80px;
    border-radius: 40px
}

.hospital-detail .detail-content {
    line-height: 22px;
    letter-spacing: 1px;
    font-size: 14px
}

.hospital-detail .detail-content p {
    text-indent: 2em;
    margin: 0
}

.hospital-detail .detail-content h4 {
    color: #333;
    margin-bottom: 0
}

.hospital-detail .mt40 {
    margin-top: 20px
}

.common-header {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between
}

.common-header .title-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}

.common-header .title-wrapper .hospital-title {
    color: #333;
    font-size: 20px;
    font-weight: 700;
    margin-right: 15px
}

.common-header .favicon .iconfont {
    font-size: 14px
}

.icon-text-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    color: #333;
    letter-spacing: 1px
}

.icon-text-wrapper .prefix-icon {
    margin-top: 3px;
    margin-right: 10px;
    font-size: 14px
}

.icon-text-wrapper .right-icon {
    margin-top: 3px;
    margin-left: 12px;
    font-size: 14px;
    color: #4490f1;
    cursor: pointer
}

.icon-text-wrapper .text p {
    margin: 0
}

.icon-text-wrapper .text ol {
    margin: 0;
    padding-left: 20px
}
</style>