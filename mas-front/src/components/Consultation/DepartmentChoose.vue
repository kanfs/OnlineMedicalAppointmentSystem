<template>
    <div class="home page-component">
        <el-carousel indicator-position="outside">
            <el-carousel-item>
                <img src="../../assets/image/web7.png" alt=""/>
            </el-carousel-item>
            <el-carousel-item>
                <img src="../../assets/image/web4.png" alt=""/>
            </el-carousel-item>
            <el-carousel-item>
                <img src="../../assets/image/web5.png" alt=""/>
            </el-carousel-item>
        </el-carousel>
        <!-- bottom -->
        <div class="bottom">
            <div class="left">
                <div class="home-filter-wrapper">
                    <div class="nav-container page-component">
                        <!--左侧导航 #start -->
                        <div class="nav left-nav">
                            <div class="nav-item">
                                <span class="v-link clickable dark" @click="gotoHospital">医院详情</span>
                            </div>
                            <!-- <div class="nav-item">
                                <span class="v-link clickable dark" @click="appointmentNotice">预约须知</span>
                            </div> -->
                            <div class="nav-item">
                                <span class="v-link clickable dark" @click="gotoReservation">预约挂号</span>
                            </div>
                            <div class="nav-item  selected">
                                <span class="v-link clickable dark" >病情咨询</span>
                            </div>
                        </div>
                        <!-- 左侧导航 #end -->

                        <!--  右侧内容  #start  -->
                        <div class="page-container">
                            <div class="hospital-home">
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
                                <div class="title select-title">选择咨询科室医生</div>
                                <div>
                                    <el-collapse  v-model="activeNames" @change="handleChange" accordion>
                                        <el-collapse-item  :title="item.name" :name="item.id" v-for="(item, index) in departmentList.values" >
                                            <template  #title>
                                                <div style="display: flex;flex-direction: row;width: 100%;height:50px;" >
                                                    <!-- 有department1~8.png八张图片 在<img/>中轮流使用 -->
                                                    <img src="../../assets/image/department1.png" style="width:50px;height:50px" v-if="departmentList.values[index].id % 7 == 1"/>
                                                    <img src="../../assets/image/department2.png" style="width:50px;height:50px" v-if="departmentList.values[index].id % 7 == 2"/>
                                                    <img src="../../assets/image/department3.png" style="width:50px;height:50px" v-if="departmentList.values[index].id % 7 == 3"/>
                                                    <img src="../../assets/image/department4.png" style="width:50px;height:50px" v-if="departmentList.values[index].id % 7 == 4"/>
                                                    <img src="../../assets/image/department5.png" style="width:50px;height:50px" v-if="departmentList.values[index].id % 7 == 5"/>
                                                    <img src="../../assets/image/department6.png" style="width:50px;height:50px" v-if="departmentList.values[index].id % 7 == 6"/>
                                                    <img src="../../assets/image/department7.png" style="width:50px;height:50px" v-if="departmentList.values[index].id % 7 == 0"/>
                                                    <div style="margin-left:40px;cursor: pointer;font-size:18px;" 
                                                    :style="{ color: departmentList.values[index].id === hoverDepartmentId ? '#1994db' : 'black' }"
                                                    @mouseover="hoverDepartmentId = departmentList.values[index].id"
                                                    @mouseout="hoverDepartmentId = null"
                                                    @click="selectDepartment(departmentList.values[index].id)"
                                                    >{{departmentList.values[index].name}}</div>
                                                </div>
                                            </template>
                                            <template #default="scope">
                                                <el-descriptions :column="5" v-for="(item, index) in doctorList.values" v-if="isloading==false" style="margin-top: 28px;width: 100%;" border>
                                                    <el-descriptions-item :align="center" :width="200">
                                                        <template #label >
                                                            <div style="text-align: center;">
                                                                <el-icon style="size:6px"><user/></el-icon>
                                                                姓名
                                                            </div>
                                                        </template>
                                                        <template #default >
                                                            <div>{{ item.name }}</div>
                                                        </template>
                                                    </el-descriptions-item>
                                                    <el-descriptions-item :align="center" :width="200">
                                                        <template #label >
                                                            <div style="text-align: center;">
                                                                <el-icon style="size:6px"><medal/></el-icon>
                                                                级别
                                                            </div>
                                                        </template>
                                                        <template #default >
                                                            <div>{{ item.grade }}</div>
                                                        </template>
                                                    </el-descriptions-item>
                                                    <el-descriptions-item :align="center" :width="200">
                                                        <template #label >
                                                            <div style="text-align: center;">
                                                                <el-icon style="size:6px"><coin/></el-icon>
                                                                职称
                                                            </div>
                                                        </template>
                                                        <template #default >
                                                            <div>{{ item.title }}</div>
                                                        </template>
                                                    </el-descriptions-item>
                                                    <el-descriptions-item :align="center" :width="200">
                                                        <template #label >
                                                            <div style="text-align: center;width:100px;">
                                                                <el-icon style="size:6px"><star/></el-icon>
                                                                患者推荐度
                                                            </div>
                                                        </template>
                                                        <template #default >
                                                            <div>{{ item.score }}</div>
                                                        </template>
                                                    </el-descriptions-item>
                                                    <el-descriptions-item >
                                                        <template #label />
                                                        <el-button type="primary" @click="consultDoctor(item.id)">咨询</el-button>
                                                    </el-descriptions-item>
                                                    <el-descriptions-item label="擅长" :align="center" label-align="right">{{ item.goodat.trim().slice(3) }}</el-descriptions-item>
                                                </el-descriptions>
                                                <el-skeleton v-if="isloading" :rows="5"/>
                                            </template>
                                        </el-collapse-item>
                                    </el-collapse>
                                </div>
                                
                                
                            </div>
                        </div>
                        <el-backtop :right="100" :bottom="100" />
                        <!--  右侧内容  #end  -->
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, watch} from 'vue';
import Cookies from 'js-cookie';
export default {
    setup() {
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        const hospitalId = ref(0);
        const hospital = reactive({});
        var isloading = ref(true);
        var departmentList = reactive([]);
        var hoverDepartmentId=ref(null);
        var doctorList = reactive([]);
        onBeforeMount(async () => {
            if (Cookies.get("token") == null){
                ctx.$message.error("请先登陆账号")
                ctx.$router.push("/home");
            }else{
                hospitalId.value = ctx.$route.params.hospitalId;
                const res = await ctx.$axios.get("/admin/information/hospital/get/"+hospitalId.value)
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
                const response = await ctx.$axios.get("/admin/schedule/departments/"+hospitalId.value)
                departmentList.values = response.data.data;
            }
        });
        const selectDepartment =(departmentId) => {
            ctx.$router.push("/department/"+departmentId);
            return ;
        }
        const gotoHospital = () =>{
            ctx.$router.push("/hospital/"+hospitalId.value);
            return ;
        };
        const gotoReservation = () => {
            ctx.$router.push({path: "/reservation/"+hospitalId.value})
        };
        const handleChange = async (value) =>{
            if(value!=''){
                isloading.value = true;
                const response = await ctx.$axios.get("/department/doctor/"+value);
                doctorList.values = response.data.data;
                isloading.value = false;
            }
           
        }
        const consultDoctor = async(doctorId)=>{
            if(doctorId!=''){
                ctx.$router.push({path: "/consultation/doctor/"+doctorId})
            }
        }
        return{
            hospital, departmentList, hoverDepartmentId, doctorList, isloading, 
            selectDepartment, gotoHospital, gotoReservation, handleChange, consultDoctor, 
            
        }
    },
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
    background:rgb(67, 125, 202)1;
}
.v-link {
    color: #666666;
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
    margin-left: -20px
}
.hospital-home .info-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    margin-top: 30px;
    padding-top: 0;
    padding-left: 0;
    width: 100%;
    flex-direction:inherit;

}

.hospital-detail .info-wrapper .space {
    margin-bottom: 10px
}

.hospital-detail .info-wrapper .content-wrapper {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    margin-left: 60px;
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

.hospital-home .info-wrapper .content-wrapper .line {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    margin-top: 8px;
    font-size: 12px;
    margin-left:30px;
}

.hospital-home .info-wrapper .content-wrapper .line .label {
    color: #999
}

.hospital-home .rule-wrapper {
    margin-top: 8px;
    font-size: 12px;
    color: #999;
    text-align: left;
    line-height: 20px;
    min-height: 20px;
    padding: 0;
    background: none;
}

.hospital-home .rule-wrapper ol {
    margin: 0;
    padding-left: 13px
}

.hospital-home .info-wrapper .hospital-img {
    height: 80px;
    width: 80px;
    border-radius: 40px
}

.title {
    letter-spacing: 1px;
    font-weight: 700;
    color: #333;
    font-size: 16px
}

.hospital-home .select-title {
    padding-top: 40px;
    padding-bottom: 20px;
    background: #fff;
    position: -webkit-sticky;
    position: sticky;
    top: 0
}
.collapse-title {
  flex: 0 1 54%;
  order: 1;
}
.el-collapse {
    width:800px;
    
}
::v-deep .el-collapse-item__header{
    
    height:60px;
}
::v-deep .el-collapse-item__content {
  padding-bottom: 0;
  
}

.el-collapse-item__arrow,
.el-tabs__nav {
  transform: rotate(90deg);
}
::v-deep .el-collapse-item__arrow.is-active {
  transform: rotate(90deg);
}
.cell-item {
  display: flex;
  align-items: center;
  width:200px
}
.split-line{width:941px;height:1px;background:#e4e6ef;margin-top: 10px;}
</style>