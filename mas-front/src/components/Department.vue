<template>
    <!-- header -->
    <div class="nav-container page-component">
        <!--左侧导航 #start -->
        <div class="nav left-nav">
            <div class="nav-item selected">
                <span class="v-link selected dark">预约挂号</span>
            </div>
        </div>
        <!-- 左侧导航 #end -->
        <!-- 右侧内容 #start -->
        <div class="page-container">
            <div class="hospital-source-list">
                <div class="header-wrapper" style="justify-content: normal">
                    <span class="v-link clickable" style="cursor: pointer" @click="gotoHospital">{{ hospitalName }}</span>
                    <div class="split"></div>
                    <div>{{ departmentName }}</div>
                </div>
                <!-- 号源列表 #start -->
                <div class="mt60">
                    <div class="calendar-list-wrapper">
                        <!-- 排班日期 分页 -->
                        <el-tag v-for="(item,index) in scheduleWeekdayRoList.values"
                                :key="index"
                                :type="index == activeIndex ? '' : 'info'"
                                style="height: 70px;margin-right:15px;cursor:pointer; font-weight: bold; font-size: 15px"
                                @click="selectDate(item.workDate, index)"
                        >
                        {{ item.workDate }} {{ item.weekday }}<br/>
                        {{ item.signalSourceStr }}
                        </el-tag>

                        <el-tag v-if="isShow"
                                :type="tabShow == false ? '':'info'"
                                style="height: 70px;margin-right:15px;cursor:pointer; font-weight: bold; font-size: 15px"
                                @click="selectTime"
                        >
                        等待系统排号<br/>
                        即将放号
                        </el-tag>
                    </div>
                    <!-- 分页 -->
                    <el-pagination
                        :current-page="page"
                        :page-size="limit"
                        :total="total"
                        class="pagination"
                        layout="prev, pager, next"
                        @current-change="getPage"
                    >
                    </el-pagination>
                </div>
                <!-- 即将放号 #start-->
                <div class="countdown-wrapper mt60" v-if="tabShow == false">
                    <div class="countdonw-title">
                        {{ time }}<span class="v-link selected"> 6:00 </span>放号
                    </div>
                    <div class="countdown-text">
                        倒 计 时
                        <div><span class="number">{{ timeString }}</span></div>
                    </div>
                </div>
                <!-- 即将放号 #end-->
                <!-- 号源 #start -->
                <div class="mt60" v-if="tabShow">
                    <div class="" v-if="scheduleList.values.length > 0">
                        <div class="list-title">
                            <div class="block"></div>
                            号源
                        </div>
                        <!-- <div
                        v-for="item in scheduleList.values"
                        :key="item.id"
                        >
                            <div class="list-item">
                                <div class="item-wrapper">
                                    <div class="title-wrapper">
                                        <div class="title">{{ item.doctorName }}</div>
                                        <div class="split"></div>
                                        <div class="name" v-if="item.position == 'DOCTOR_POSITION'">普通医师</div>
                                        <div class="name" v-if="item.position == 'ATTENDING_PHYSICIAN'">主治医师</div>
                                        <div class="name" v-if="item.position == 'SUB_DOCTOR_POSITION'">副主任医师</div>
                                        <div class="name" v-if="item.position == 'CHIEF_PHYSICIAN'">主任医师</div>
                                        <div class="name" v-if="item.position == 'EXPERT_PHYSICIAN'">专家医师</div>
                                    </div>
                                    <div class="special-wrapper">{{ item.doctorIntro }}</div>
                                </div>
                                <div class="right-wrapper">
                                    <div class="fee">￥{{ item.price }}</div>
                                    <div class="button-wrapper">
                                        <div
                                        class="v-button"
                                        @click="booking(item)"
                                        :style="item.availableNum == 0 ? 'background-color: #7f828b;'
                                        : ''">
                                        <span v-if="item.availableNum > 0">剩余<span class="number">{{ item.availableNum }}</span></span>
                                        <span v-else>已约满</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div> -->
                        <el-table
                            v-loading="listLoading"
                            :data="scheduleList.values"
                            border
                            fit
                            highlight-current-row
                            size="big"
                            style="font-size: 14px; width:90%"
                        >
                            <!-- <el-table-column
                            label="序号"
                            width="60"
                            align="center"
                            >
                            <template slot-scope="scope">
                                {{ scope.$index + 1 }}
                            </template>
                            </el-table-column> -->
                            <el-table-column prop="doctorName" align="center" label="医生" width="80"></el-table-column>
                            <el-table-column prop="grade" align="center" label="级别" width="120"/>
                            <el-table-column prop="title" align="center" label="职称" width="80"/>
                            <el-table-column prop="availableNum" align="center" label="剩余预约数" width="100"/>
                            
                            <el-table-column prop="price" align="center" label="挂号费" width="80">
                                <template #default="scope">
                                    ￥{{scope.row.price}} 元
                                </template>
                            </el-table-column>
                            <el-table-column prop="doctorGoodat" align="center" label="擅长技能" width="350">
                                <template #default="scope">
                                    {{scope.row.doctorGoodat.replace(/\s+/g, '').slice(3)}} 
                                </template>
                            </el-table-column>
                            <el-table-column  align="center" label="操作" >
                                <template #default="scope">
                                    <el-button type="success" :icon="Plus" title="预约" @click="booking(scope.row)" >预约</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div class="" v-if="scheduleList.values.length == 0" style="text-align: center">
                        <el-alert
                        title="当日无号源，请查看其他号源！！！"
                        type="warning">
                        </el-alert>
                    </div>
                </div>
                <!-- 上午号源 #end -->
            </div>
        </div>
        <!-- 右侧内容 #end -->
        <el-dialog v-model="dialogVisible" title="选择就诊人" width="60%">
            <div class="content-wrapper">
                <el-card class="patient-card" style="width: 800px; cursor: pointer; height:120px;" shadow="hover" v-for="item in patientList.values" :key="item.id" @click="patientSelect(item)">
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
                    <div class="card SELF_PAY_CARD" style="cursor: pointer;">
                        <div class="info">
                            <span class="type">身份证</span>
                            <span class="card-no" style="margin-left: 3px;">{{ item.identifyCardNumber }}</span>
                            <span class="card-view" >{{ item.sex ? "男" : "女"}}</span>
                        </div>
                        <span class="operate"></span>
                    </div>
                    <div class="card" style="cursor: pointer;">
                        <div class="text bind-card"></div>
                    </div>
                </el-card>

                <div style="width: 700px;">
                    <div class="item-add-wrapper v-card clickable" onclick="javascript:window.location='/patient/add'">
                        <div>+ 添加就诊人</div>
                    </div>
                </div>
            </div>
        </el-dialog>
    </div>
    <!-- footer -->
</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, onBeforeUnmount} from 'vue';
import Cookies from 'js-cookie';
import {Check, Plus} from '@element-plus/icons-vue';
export default {
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        var userId = 0;
        var page = ref(1);
        var limit = ref(6);
        var departmentId = 0;
        var workDate = null;
        var scheduleList = reactive([]); 
        var activeIndex = ref(0);
        var total = ref(12);
        var isShow = ref(false);
        var tabShow = ref(true);
        var hospitalName = ref("");
        var hospitalId = ref(0);
        var departmentName = ref("");
        var scheduleWeekdayRoList = reactive([]);
        var timeString = ref(null);
        var time = ref("今天");
        var timer = null;
        var patientList = reactive([]);
        var dialogVisible = ref(false);
        var scheduleId = 0;
        onBeforeMount(async () => {
            if (Cookies.get("token") == null){
                    ctx.$message.error("请先登陆账号")
                    ctx.$router.push("/home");
                    
                }else{
                    departmentId = ctx.$route.params.departmentId;
                    userId = Cookies.get('userId');
                    const res = await ctx.$axios.get("/department/names/"+departmentId);
                    hospitalId.value = res.data.data.hospitalId;
                    hospitalName.value = res.data.data.hospitalName;
                    departmentName.value = res.data.data.departmentName;
                    findScheduleWeekdayList();
                }
            });
        const findScheduleWeekdayList = async()=>{
            const res = await ctx.$axios.get("/admin/schedule/weekday", {params: {page: page.value, limit: limit.value, departmentId: departmentId, }}) 
            scheduleWeekdayRoList.values = res.data.data.scheduleWeekdayRoList;
            if (res.data.data.total != 0) {
                workDate = res.data.data.scheduleWeekdayRoList[0].workDate
                // 查询排班信息
                findScheduleList(res.data.data.scheduleWeekdayRoList[0].workDate, departmentId)
            }
            isShow.value = scheduleWeekdayRoList.values.length < limit.value
        };
        const findScheduleList = async (workDate, departmentId) => {
            const res = await ctx.$axios.get("/admin/schedule/list", {params: {weekDay: workDate, departmentId: departmentId}})
            scheduleList.values = res.data.data;
        }
        const selectDate = (date, index) => {
            workDate = date
            activeIndex.value = index
            tabShow.value = true
            findScheduleList(workDate, departmentId)
        };
        const getPage = async (curPage) => {
            page.value = curPage
            activeIndex.value = 0
            findScheduleWeekdayList()
        }
        // 即将放号
        const selectTime =()=> {
            activeIndex.value = -1
            //清理定时
            if (timer != null) clearInterval(timer);
            tabShow.value = false;
            // 放号时间
            let releaseTime = new Date(
                getCurDate()
            ).getTime();
            let nowTime = new Date().getTime();
            countDown(releaseTime, nowTime);
        };
        // 预约挂号
        const booking = async (item) => {
            let date = new Date(workDate)
            let nowDate = new Date()

            // TODO 判断用户状态和用户是否认证通过

            // 判断空闲数
            if (item.availableNum === 0) {
                ctx.$message({
                message: '当前医生号源已约满，不能再预约，请更换其他医生！',
                type: 'warning'
                });
            } else if (date.getFullYear() == nowDate.getFullYear()
                && date.getMonth() == nowDate.getMonth()
                && date.getDate() == nowDate.getDate() && nowDate.getHours() > 6) {
                ctx.$message({
                message: '今日已过预约时间，不能再预约，请更换日期！',
                type: 'warning'
                });
            } else {
                // 选择患者
                //ctx.$message({message: '请选择患者！',type: 'success'});
                const res = await ctx.$axios.get('/patient/list', {params:{userId:userId}});
                patientList.values = res.data.patientList;
                scheduleId = item.id
                dialogVisible.value = true;
            }

        };
        const patientSelect = async (patient)=>{
            dialogVisible.value = false;
            const res = await ctx.$axios.get("/order/save", {params:{
                patientId: patient.id,
                scheduleId: scheduleId,
            }});
            var orderId = res.data.data;
            if(orderId == 0){
                ctx.$message({message: '该患者已经预约，请勿重复预约！',type: 'error'});
            }else{
                ctx.$router.push({ path: '/order/show/'+orderId});
            }
            
        };
        const gotoHospital = ()=>{
            console.log("gotoHospital");
            ctx.$router.push({ path: '/hospital/'+hospitalId.value});
        }


        const getCurDate = () => {
            let datetime = new Date();
            let year = datetime.getFullYear();
            let month =
                datetime.getMonth() + 1 < 10
                ? "0" + (datetime.getMonth() + 1)
                : datetime.getMonth() + 1;
            let date =
                datetime.getDate() < 10
                ? "0" + datetime.getDate()
                : datetime.getDate();
            return year + "-" + month + "-" + date;
        };

        const countDown = (releaseTime, nowTime) => {
            //计算倒计时时长
            let secondes = 0;
            if (releaseTime > nowTime) {
                time.value = "今天";
                //当前时间到放号时间的时长
                secondes = Math.floor((releaseTime - nowTime) / 1000);
            } else {
                time.value = "明天";
                //计算明天放号时间
                let releaseDate = new Date(releaseTime);
                releaseTime = new Date(
                releaseDate.setDate(releaseDate.getDate() + 1)
                ).getTime();
                //当前时间到明天放号时间的时长
                secondes = Math.floor((releaseTime - nowTime) / 1000);
            }

            //定时任务
            timer = setInterval(() => {
                secondes = secondes - 1;
                if (secondes <= 0) {
                    clearInterval(timer);
                    ctx.init();
                }
                timeString.value = convertTimeString(secondes);
            }, 1000);
        };

        const convertTimeString = (allseconds) => {
            if (allseconds <= 0) return "00:00:00";
            // 计算天数
            let days = Math.floor(allseconds / (60 * 60 * 24));
            // 小时
            let hours = Math.floor(
                (allseconds - days * 60 * 60 * 24) / (60 * 60)
            );
            // 分钟
            let minutes = Math.floor(
                (allseconds - days * 60 * 60 * 24 - hours * 60 * 60) / 60
            );
            // 秒
            let seconds =
                allseconds -
                days * 60 * 60 * 24 -
                hours * 60 * 60 -
                minutes * 60;

            //拼接时间
            let timString = "";
            if (days > 0) {
                timString = days + "天:";
            }
            return (timString +=
                hours + " 时 " + minutes + " 分 " + seconds + " 秒 ");
        };
        
        onBeforeUnmount(() => {
            clearInterval(timer)
        });
        return{
            scheduleList, total, page, limit, isShow, tabShow, time, timeString, scheduleWeekdayRoList, 
            activeIndex, departmentName, hospitalName, patientList, dialogVisible, hospitalId, 
            getPage, booking, selectDate, selectTime, patientSelect, gotoHospital, 
            Check, Plus, 
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
.v-link .clickable {
    cursor: pointer
}
.v-link.selected, .v-link:hover {
    color: #4490f1
}
.page-container {
    padding-top: 38px;
    min-height: 500px;
    width: calc(100% - 200px);
    margin-left: -50px
}

.hospital-source-list .list-item {
    width: auto;
    height: auto;
    color: #333;
    background:none;
    display: inline;
    vertical-align: top;
    font-weight: 700;
    padding: 0 ;
    line-height: inherit;
    border-radius: 4px;
    cursor: default;
    margin-top: 40px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
}
.hospital-source-list .list-item:hover{
    border:0;
    box-shadow: #4490f1;
    -webkit-box-shadow: none ;
}
.list-item {
    width: 280px;
    height: 50px;
    color: #333;
    background: #fafafa;
    display: inline-block;
    vertical-align: top;
    font-weight: 700;
    padding: 0 20px 0 30px;
    line-height: 50px;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 20px
}

.list-item.selected {
    background: #4990f1;
    color: #fff;
    -webkit-box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .5);
    box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .5);
    border: 1px solid #4990f1
}

.list-item.disabled {
    color: #ccc
}

.list-item:hover {
    border: 1px solid #4990f1;
    -webkit-box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .2);
    box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .2)
}

.list-item .right-text {
    font-weight: 400;
    float: right
}

.button-wrapper {
    width: 140px;
    margin: 0 auto
}

.title {
    letter-spacing: 1px;
    font-weight: 700;
    color: #333;
    font-size: 16px
}

.affix {
    position: fixed;
    z-index: 100
}

.mt60 {
    margin-top: 30px
}

.header-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}

.header-wrapper .title {
    margin-right: 30px
}

.split {
    border-left: 1px solid #eee;
    width: 1px;
    height: 16px;
    margin: 0 10px
}
.block {
    height: 14px;
    width: 4px;
    background: #4990f1;
    margin-right: 6px;
    border-radius: 2px
}

.calendar-list-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex
}

.calendar-list-wrapper .space {
    margin-left: 22px
}

.calendar-list-wrapper .small-space {
    margin-left: 16px
}

.pagination {
    margin-top: 30px;
    height: 42px;
    margin-left: 36%;
}

.countdown-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}

.countdown-wrapper .countdonw-title {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    color: #333;
    font-size: 16px
}

.countdown-wrapper .v-link {
    margin-left: 5px;
    margin-right: 5px
}

.countdown-wrapper .countdown-text {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    color: #999;
    font-size: 16px;
    margin-top: 20px
}

.countdown-wrapper .number {
    margin-left: 24px;
    font-weight: 700;
    font-size: 30px;
    letter-spacing: 1px;
    color: #4990f1
}

.countdown-wrapper .unit {
    font-size: 16px;
    color: #333
}

.list-item {
    margin-top: 40px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between
}

.list-item .item-wrapper {
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center
}

.list-item .item-wrapper, .list-item .title-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex
}

.list-item .title-wrapper .title {
    font-size: 14px
}

.list-item .title-wrapper .name {
    letter-spacing: 1px
}

.list-item .special-wrapper {
    width: 660px;
    margin-top: 10px;
    letter-spacing: 1px;
    color: #999
}

.list-item .right-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}

.list-item .right-wrapper .fee {
    letter-spacing: 1px;
    color: #4990f1;
    font-weight: 700;
    margin-right: 20px
}

.list-item .right-wrapper .button-wrapper {
    width: 140px
}

.list-item .right-wrapper .button-wrapper .v-button {
    font-weight: 700
}

.list-item .right-wrapper .button-wrapper .number {
    margin-top: -3px
}

.list-title {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    letter-spacing: 1px;
    color: #999
}
</style>