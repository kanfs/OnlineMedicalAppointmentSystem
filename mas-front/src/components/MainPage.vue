/* eslint-disable */
<template>
    <section class="bg-gray">
        <section class="choose-section">
            <!-- 欢迎部分 -->
            <div class="welcome-content">
                <div class="main-title">
                    <span class="main-title-website-name">O M A S</span>
                    <span class="main-title-welcome-message">预约挂号服务{{  }}</span>
                </div>
                <div class="sub-title">
                    <span style="color:aqua">全国{{ hospitalNum }}所医院   {{ doctorNum }}位医生可挂号    竭诚为您服务！</span>
                </div>
            </div>
            <!-- 选择部分 -->
            <div class="choose-content">
                <div class="choose-area" >
                    <div class="label-content">省份:</div>
                    <el-radio-group class="radio-content" v-model="province">
                        <el-radio v-for="item in provinces.value" class="radio-item" :label="item"></el-radio>
                    </el-radio-group>
                </div>
                <div class="choose-area" v-if="province != '全国' && province !=''">
                    <div class="label-content">城市:</div>
                    <el-radio-group class="radio-content" v-model="city">
                        <el-radio v-for="item in cities.value" class="radio-item" :label="item"></el-radio>
                    </el-radio-group>
                </div>
                <div class="choose-hospital">
                    <div class="label-content">医院:</div>
                    <el-radio-group class="radio-content" v-model="hospital">
                        <el-radio v-for="item in hospitals.values" class="radio-item" :label="item"></el-radio>
                    </el-radio-group>
                    <el-button class="hospital-search" :icon="Search" @click="searchHospitalVisible = true">搜索其他医院</el-button>
                </div>
                <div class="choose-area">
                    <div class="label-content">科室:</div>
                    <el-radio-group class="radio-content" v-model="department">
                        <el-radio v-for="item in departments.values" class="radio-item" :label="item"></el-radio>
                    </el-radio-group>
                </div>
                <div class="choose-area">
                    <div class="label-content">疾病:</div>
                    <div class="disease"><el-input class="disease-input" type="text" v-model="disease" placeholder="请输入疾病名称" @change="getMatchedDoctors"></el-input></div>
                </div>
            </div>
        </section>
        <!-- 医生部分 -->
        <section class="doctor-section">
            <!-- 日期选择 -->
            <div class="date-area-content">
                <el-radio-group  v-model="date">
                    <div class="no-limited-time"><el-radio :label="noLimitDate"><div class="unchoose-time">{{ '不 限\n日 期' }}</div></el-radio></div>
                    <el-radio v-for="item in dates"  :label="item.date" class="date-radio" >
                        <div class="date-item" :style="{ background: date === item.date ? '#fd87ab' : '#fdfdfd' }">
                            <div class="date-label">{{ (item.month)+"月"+(item.date)+"日" }}</div>
                            <div class="date-value">{{ item.day }}</div>
                        </div>
                    </el-radio>
                </el-radio-group>
            </div>
            <!-- 医生列表 -->
            <div class="doctor-list-content" v-loading="loading">
                <div class="split-line"></div>
                <div>
                    <div class="doctor-item"  v-for="item in doctors.values" :key="item.id">
                        <img alt src="../assets/image/doctor.png" class="avatar">
                        <div class="doctor-detail">
                            <div class="doctor-basic">
                                <div class="doctor-name">{{ item.doctorName }}</div>
                                <div v-if="item.grade !== '暂无'" class="doctor-grade">{{ item.grade }}</div>
                                <div v-if="item.title !== '暂无'" class="doctor-title">{{ item.title }}</div>
                            </div>
                            <div class="doctor-ground">
                                <span>{{ item.hospitalName }}</span>
                                <span style="margin-left: 10px;">{{ item.departmentName }}</span>
                            </div>
                            <div class="doctor-special">{{ item.goodat.trim() }}</div>
                            <div class="doctor-score">
                                <div class="evaluate-label">
                                    推荐度
                                    <span class="evaluate-text">{{ item.score }}</span>
                                </div>
                            </div>
                        </div>
                        <div class="action-btn">
                            <el-button-group>
                            <el-button type="primary" @click="appoint(item.departmentId)">预约</el-button>
                            <el-button type="success" @click="consult(item.id)">咨询</el-button>
                            </el-button-group>
                        </div>
                        <!-- <div class="action-btn">
                            <el-button type="primary" @click="appoint(item.departmentId)">立即预约</el-button>
                        </div>
                        <div class="action-btn">
                            <el-button type="success" @click="appoint(item.departmentId)">立即咨询</el-button>
                        </div> -->
                    </div>
                    <div class="split-line"></div>
                </div>
            </div>
            <!-- 分页符 -->
            <el-pagination
            v-model:current-page="curPage"
            v-model:page-size="pageSize"
            layout="prev, pager, next, jumper"
            :total="matchedDoctorNum"
            @current-change="handleCurPageChange()"
            style = "justify-content: center"
            />
        </section>
        
    </section>
    <el-dialog v-model="searchHospitalVisible" :show-close="false">
        <template #title>
            <div class="dialog-header">
                <div class="title-icon"></div>
                <div class="title-text">搜索其他医院</div>
                <el-button type="danger" @click="searchHospitalVisible = false">
                    <el-icon class="el-icon--left"><CircleCloseFilled /></el-icon>
                    关闭
                </el-button>
            </div>
        </template>
        <div style="padding:20px 24px 0 30px;margin-top:-30px;">
            <el-input v-model="vagueHospitalName" type="text" autocomplete="true" placeholder="请输入医院名称" >
                <template #append>
                    <el-button @click="searchHospitalByName" :icon="Search" >搜医院</el-button>
                </template>
            </el-input>
            <section class="hospital-section"> 
                <el-table :data="pagedHospitals.values.slice((curTablePage-1)*tablePageSize, curTablePage*tablePageSize)" @row-click="selectHospitalName">
                    <el-table-column prop="name" label="医院名称"></el-table-column>
                </el-table>
                <el-pagination
                v-model:current-page="curTablePage"
                v-model:page-size="tablePageSize"
                layout="prev, pager, next, jumper"
                :total="matchedHospitalNum"
                @current-change="handleCurTablePageChange()"
                style = "justify-content: center">
                </el-pagination>
            </section>
        </div>       
    </el-dialog>
</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, watch} from 'vue';
import {Search, CircleCloseFilled} from '@element-plus/icons-vue';
export default{
    setup() {
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        const searchHospitalVisible = ref(false);
        var hospitalNum = ref(3647);
        var doctorNum = ref(28239);
        // var provinces = reactive(["全国", "北京", "上海", "河北", "广东", "江苏", "陕西", "山东", "重庆", "天津", "四川", "辽宁", "湖北", "湖南", "浙江", "山西", "河南", "福建", "黑龙江", "安徽", "海南", "云南", "甘肃", "江西", "广西", "新疆", "青海", "吉林", "宁夏", "贵州", "内蒙古", "西藏"]);
        var provinces = reactive(["全国", "北京", "上海", "河北", "广东", "江苏", "陕西", "山东", "重庆", "天津", "四川", "辽宁", "湖北", "湖南", "浙江", "山西", "河南", "福建", "黑龙江", "安徽", "海南", "云南", "甘肃", "江西", "广西", "新疆", "青海", "吉林", "宁夏", "贵州", "内蒙古", "西藏"]);
        var cities = reactive([""]);
        var areaData = reactive([]);
        var hospitals = reactive([""]);
        var pagedHospitals = reactive([""]);
        var departments = reactive([""]);
        var doctors = reactive([])
        // {id : Number,name : String,grade : String,title : String,score : Number,goodat : String,}
        var diseases = reactive([]);
        var daysOfWeekInChinese = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
        var date = ref(-1);
        var noLimitDate = ref(-1);
        var dates = reactive([]);
        var currentDate = new Date();
        for (var i = 0; i <= 6; i++) {
            var curDay = new Date();
            curDay.setDate(currentDate.getDate() + i);
            var curDayOfMonth = curDay.getMonth()+1;
            var curDayOfDate = curDay.getDate()
            var curDayOfWeek = curDay.getDay();
            var curDayOfWeekChinese = daysOfWeekInChinese[curDayOfWeek];
            dates.push({month:curDayOfMonth, date:curDayOfDate, day:curDayOfWeekChinese})
        }
        var vagueHospitalName = ref("")
        var province = ref("");
        var city = ref("");
        var hospital = ref("");
        var department = ref("");
        var disease = ref("");
        var curPage = ref(1);
        var pageSize = ref(20);
        var matchedDoctorNum = ref(doctorNum.value);
        var curTablePage = ref(1);
        var tablePageSize = ref(6);
        var matchedHospitalNum = ref(hospitalNum.value);
        var loading = ref(true);
        const getprovinceList = async () => {
            const res = await ctx.$axios.get('/upload')
            areaData.value = res.data
            var tmp = [];
            for (var i = 0; i < areaData.value.length; i++) {
                tmp.push(areaData.value[i].name)
            }
            tmp.unshift("全国");
            provinces.value = tmp;
            // console.log(provinces.value)
        }
        const getCityName = async (province)=>{
            var tmp = [];
            for (var i = 0; i < areaData.value.length; i++) {
                if (province.value == areaData.value[i].name){
                    for (var j = 0; j < areaData.value[i].areaList.length; j++){
                        tmp.push(areaData.value[i].areaList[j].name)
                    }
                    tmp.unshift("全省");
                    cities.value = tmp;
                    break;
                }
            }
        }
        const getBaicInfo = async ()=>{
            const res = await ctx.$axios.get("hospital/basic-info");
            hospitalNum.value = res.data.hospitalNum
            doctorNum.value = res.data.doctorNum
        };
        const getHospitalName = async () =>{
            const res = await ctx.$axios.get('hospital/names',{params:{province:province.value, city:city.value}});
            hospitals.values = res.data;
            pagedHospitals.values = res.data.map(name => ({ name }));
            hospitals.values.unshift("不限");
        };
        const searchHospitalByName = async () =>{
            const res = await ctx.$axios.get("hospital/vague-name",{
                params:{
                    hospitalName:vagueHospitalName.value,
                }});
            pagedHospitals.values = res.data.map(name => ({ name }));
            matchedHospitalNum.value = res.data.length;
        };
        const getDepartmentName = async () =>{
            const res = await ctx.$axios.get("department/names",{params:{hospital:hospital.value}});
            departments.values = res.data;
            if( departments.values.length == 0 ) departments.values.unshift("不限","内科","外科","妇产科","儿科","眼科","耳鼻喉科","神经科","心理科","康复科","急诊科","放射科","遗传科","麻醉科");
            else departments.values.unshift("不限");
        };
        const getMatchedDoctors = async () =>{
            loading.value = true;
            const res = await ctx.$axios.get("doctor/matched-doctors",{
                params:{
                    province:province.value,
                    city:city.value,
                    hospitalName:hospital.value,
                    departmentName:department.value,
                    disease:disease.value,
                    firstLimit:((curPage.value-1)*pageSize.value),
                    pageSize:pageSize.value,
                }});
            doctors.values = res.data.doctorList;
            matchedDoctorNum.value = res.data.count;
            loading.value = false;
        };
        const appoint = async (id) =>{
            ctx.$router.push({path:'/department/'+id});
        };
        const consult = async (id) =>{
            ctx.$router.push({path:'/consultation/doctor/'+id});
        };
        const handleCurPageChange = async () =>{
            getMatchedDoctors();
        };
        const handleCurTablePageChange = async () =>{
            // console.log(curTablePage.value);
        };
        const selectHospitalName = async (row) =>{
            hospitals.values.pop()
            hospitals.values.push(row.name);
            hospital.value = row.name;
            searchHospitalVisible.value = false;
        };
        onBeforeMount(async()=>{
            getBaicInfo();
            getHospitalName();
            getDepartmentName();
            getMatchedDoctors();
            getprovinceList();
        });
        watch(province, (curProvince)=>{
            city.value = "";
            getCityName(province);
            hospital.value = "";
            getHospitalName();
            department.value = "";
            getDepartmentName();
            getMatchedDoctors();
            curPage.value = 1;
        });
        watch(city, (curCity)=>{
            hospital.value = "";
            getHospitalName();
            department.value = "";
            getDepartmentName();
            getMatchedDoctors();
            curPage.value = 1;
        });
        watch(hospital, (curHospital)=>{
            department.value = "";
            getDepartmentName();
            getMatchedDoctors();
            curPage.value = 1;
        });
        watch(department, (curDepartment)=>{
            getMatchedDoctors();
            curPage.value = 1;
        });
        return {
            hospitalNum, doctorNum, searchHospitalVisible, searchHospitalByName,
            city, province, hospital, department, disease, date, noLimitDate, vagueHospitalName, curPage, pageSize, matchedDoctorNum, curTablePage, tablePageSize, matchedHospitalNum, 
            cities, provinces, hospitals, departments, diseases, dates, pagedHospitals, 
            getMatchedDoctors, doctors, loading, 
            consult, appoint, handleCurPageChange, handleCurTablePageChange, selectHospitalName, 
            Search,
        }
    },
}

</script>

<style scoped>
.bg-gray{
    background:#f6f6f6;
    padding-bottom:136px;
}
.choose-section{
    margin: auto;
    width:1200px;
    height: 600px auto;
    background:#ffffff;
    -webkit-box-shadow:0 1px 6px 0 rgba(0,0,0,.03),0 0 2px 0 rgba(0,0,0,.04);
    -moz-box-shadow:0 1px 6px 0 rgba(0,0,0,.03),0 0 2px 0 rgba(0,0,0,.04);
    box-shadow:0 1px 6px 0 rgba(0,0,0,.03),0 0 2px 0 rgba(0,0,0,.04);
}
.welcome-content{
    background-image:url(../assets/image/icon-banner.jpg);
    width:1200px;
    height:210px;
    -webkit-background-size:cover;
    -moz-background-size:cover;
    -o-background-size:cover;
    background-size:cover;
    background-repeat:no-repeat;
    background-position:50%;
    margin-bottom: 20px;
}
.main-title{
    padding:33px 293px 0 300px;
    line-height:65px;
    font-family: '仿宋', 'STFangsong', sans-serif;
    height:65px;
    font-size:46px;
    font-weight:700;
    letter-spacing:2px;
}
.main-title .main-title-website-name{margin-right:20px;color:#46a0f0}
.main-title .main-title-welcome-message{margin-left:80px;color:#f5f219}
.sub-title{
    margin-top:20px;
    text-align:center;
    height:50px;
    line-height:50px;
    font-size:26px;
    color:#2ad956;
    margin-left:23%;
}
.choose-content{
    width:1118px;
    display:-webkit-box;
    display:-webkit-flex;
    display:-moz-box;
    display:-ms-flexbox;
    display:flex;
    -webkit-flex-wrap:wrap;
    -ms-flex-wrap:wrap;
    flex-wrap:wrap;
}
.choose-area{
    width: 1200px;
    display:-webkit-box;
    display:-webkit-flex;
    display:-moz-box;
    display:-ms-flexbox;
    display:flex;
}
.choose-hospital{
    width: 1100px;
    display:-webkit-box;
    display:-webkit-flex;
    display:-moz-box;
    display:-ms-flexbox;
    display:flex;
    .hospital-search{
        width:150px;
        height:26px;
        line-height:26px;
        padding:0;
        -webkit-border-radius:4px;
        -moz-border-radius:4px;
        border-radius:4px;
        border:1px solid #055ad7;
        color:#055ad7;
        font-size:14px;
    }
}
.label-content{
    font-size:16px;
    margin-left:10px;
    font-weight:700;
    width:50px;
    line-height:22px;
    color:#000;
    padding-top:6px;
    margin-right:8px;
}
.radio-content{
    width:1200px;
    display:-webkit-box;
    display:-webkit-flex;
    display:-moz-box;
    display:-ms-flexbox;
    display:flex;
    -webkit-flex-wrap:wrap;
    -ms-flex-wrap:wrap;
    flex-wrap:wrap;
}
.radio-content .radio-item{
    border: #a4d7b1;
    font-size:24px;
    color:#48482f;
    margin-right:5px;
    line-height:40px;
    cursor:pointer;
    margin-bottom:10px;
}
.el-radio__input{
   display: none !important;
 }
.disease{
    width:160px;
    height:20px;
    font-weight:400;
    line-height:10px;
    margin-left:5px;
    margin-bottom: 10px;
}
.disease .disease-input{
    outline:none;
    border-radius: 3px;
    height:26px;
    width:160px;
}
.disease .disease-input .el-input__wrapper {
    width: 100%;
    background-color: rgb(233, 233, 227) ; 
}
.disease input::-webkit-input-placeholder{
    color:#161a23;
    font-size:13px
}
.doctor-section{
    width:1200px;
    background:#fff;
    -webkit-box-shadow:0 1px 6px 0 rgba(0,0,0,.03),0 0 2px 0 rgba(0,0,0,.04);
    -moz-box-shadow:0 1px 6px 0 rgba(0,0,0,.03),0 0 2px 0 rgba(0,0,0,.04);
    box-shadow:0 1px 6px 0 rgba(0,0,0,.03),0 0 2px 0 rgba(0,0,0,.04);
    min-height:200px;
    margin:16px auto 0;
}
.date-area-content{
    width:1150px;
    height:120px;
    padding:30px 30px 30px 20px;
}
.no-limited-time{
    margin-left:0px;
    margin-right:50px;
    font-size:48px;
    width:84px;
    height:60px;
    -webkit-border-radius:8px;
    -moz-border-radius:8px;
    border-radius:8px;
    border:1px solid #eaeef5;
    text-align:center;
    font-weight:700;
    color:#194dc7;
    cursor:pointer;
}
.unchoose-time{
    margin-left: -10px;
    margin-top: -20px;
    text-overflow: ellipsis;
    white-space: pre-line;
    vertical-align: middle;
    display: inline-block;
    font-size: 20px;
    line-height: 30px;
    user-select:none;
}
.date-radio .date-item{
    width:84px;
    height:60px;
    -webkit-border-radius:8px;
    -moz-border-radius:8px;
    border-radius:6px;
    border:1px solid #7287ab;
    margin-right:20px;
    cursor:pointer;
    background: #fdfdfd;
    .date-label{
        vertical-align: middle;
        height:30px;
        background:transparent;
        margin-top: 0px;
        margin-left: 0px;
        font-size:16px;
        color:#486dc2;
        font-weight:700;
        line-height:30px;
        text-align:center;
    }
    .date-value{
        margin-left: -5px;
        margin-top: -3px;
        line-height:30px;
        font-size:16px;
        font-weight:600;
        text-align:center;
        color:#2c7e48;
    }
}
.doctor-list-content{
    min-height:300px;
    margin-top:-40px;
    .doctor-item{
        display: flex;
        padding:20px 30px;
        .avatar{
            width:60px;
            height:60px;
            -webkit-border-radius:6px;
            -moz-border-radius:6px;
            border-radius:6px;
            border:1px solid #ccc;
        }
        .doctor-detail{
            margin-left:16px;
            width:885px;
            .doctor-basic{
                display:flex;
                .doctor-name{
                    font-size:20px;
                    line-height:28px;
                    font-weight:700;
                    color:#000;
                    max-width:500px;
                    cursor:pointer;
                }
                .doctor-title{
                    font-size:14px;
                    height:20px;
                    color:#475163;
                    line-height:20px;
                    margin-left:10px;
                }
                .doctor-grade{
                    font-size:14px;
                    height:20px;
                    color:#475163;
                    line-height:20px;
                    margin-left:10px;
                }
            }
            .doctor-ground{
                display:flex;
                margin-top:5px;
                height:20px;
                line-height:20px;
                color:#475163;
                font-size:14px;
            }
            .doctor-special{
                display:flex;
                margin-top:6px;
                line-height:20px;
                font-size:14px;
                color:#475163;
                max-height:41px;
                overflow:hidden;
                position:relative;
                text-align: left; /* 让文本在容器中左对齐 */
                justify-content: flex-start;
            }
            .doctor-score{
                margin-top:7px;
                display:-webkit-box;
                display:-webkit-flex;
                display:-moz-box;
                display:-ms-flexbox;
                display:flex;
                .evaluate-label{
                    height:20px;
                    font-size:14px;
                    color:#000;
                    line-height:20px;
                    .evaluate-text{
                        font-size:16px;
                        line-height:22px;
                        font-weight:700;
                        color:#ff4d52;
                        margin-left:6px;
                    }
                }
            }
        }
        .action-btn{
                width:140px;height:60px;line-height:44px;
                -webkit-border-radius:4px;-moz-border-radius:4px;border-radius:4px;
                background:transparent;
                text-align:center;
                color:#fff;
                margin-top:32px;margin-left:30px;
                cursor:pointer;
            }
    }
}
.split-line{width:1141px;height:1px;background:#e4e6ef;margin-left:29px}
.split-line-white{background:#fff}












.dialog-header{
    height:80px;
    display:flex;
    -webkit-box-align:center;
    -webkit-align-items:center;
    -moz-box-align:center;
    -ms-flex-align:center;
    align-items:center;
    border-bottom:1px solid #dcdcdc;
    .title-icon{
        margin-left:22px;
        width:4px;
        height:24px;
        background:#055ad7;
    }
    .title-text{
        width: 700px;
        margin-left:10px;
        font-size:16px;
        font-weight:700;
        color:#323232;
    }
}
.hospital-section[data-v-61491c19]{
    margin-top:30px;
    display:flex;
    -webkit-flex-wrap:wrap;
    -ms-flex-wrap:wrap;
    flex-wrap:wrap;
    .main-item{
        display:inline-block;
        font-size:14px;
        color:#333;
        padding:3px 7px;
        margin-right:6px;
        line-height:20px;
        cursor:pointer;
        margin-bottom:10px;
        user-select:none;
    }
}
::v-deep .el-radio__input {
  display: none ;
}
</style>