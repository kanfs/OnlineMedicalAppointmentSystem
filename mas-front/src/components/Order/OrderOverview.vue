<template>
    <div class="page-container">
      <div class="personal-order">
        <div class="title">挂号订单</div>
        <el-form :inline="true">
          <el-form-item label="就诊人：">
            <el-select
              v-model="patientId"
              placeholder="请选择就诊人"
              class="v-select patient-select"
              style="width: 260px"
              clearable
            >
              <el-option
                align="center"
                v-for="item in patients"
                :key="item.id"
                :label="item.name +'【' +item.identifyCardNumber +'】' "
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="订单状态：" style="margin-left: 80px">
            <el-select
              v-model="orderStatus"
              placeholder="全部"
              class="v-select patient-select"
              style="width: 260px"
              clearable
            >
              <el-option
                align="center"
                v-for="(item, index) in statuses"
                :key="index"
                :label="item"
                :value="index"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="default"
              class="
                                search-button
                                v-link
                                highlight
                                clickable
                                selected
                            "
              @click="fetchData()">
              查询
            </el-button>
          </el-form-item>
        </el-form>
        <div class="table-wrapper table">
          <el-table :data="orders" stripe style="width: 100%">
            <el-table-column align="center" label="就诊日期" width="120">
              <template #default="scope">
                {{ new Date(scope.row.workDate).toLocaleDateString() }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="hospitalName" label="医院" width="100"/>
            <el-table-column align="center" prop="departmentName" label="科室" width="100"/>
            <!-- <el-table-column align="center" prop="patientName" :show-overflow-tooltip="true" label="门诊"/>  -->
            <el-table-column align="center" prop="doctorName" label="医生"/>
            <!-- <el-table-column align="center" prop="position" label="职称"/>  -->
            <el-table-column align="center" prop="price" label="费用"/>
            <el-table-column align="center" prop="patientName" :show-overflow-tooltip="true" label="就诊人"/>
            <el-table-column align="center" prop="number" label="号序"/>
            <el-table-column align="center" prop="status" label="订单状态">
              <template #default="scope">
                <el-tag v-if="scope.row.status == 0">待支付</el-tag>
                <el-tag v-if="scope.row.status == 1" type="success">已支付</el-tag>
                <el-tag v-if="scope.row.status == 2" type="warning">已取消</el-tag>
                <el-tag v-if="scope.row.status == 3" type="danger">已结束</el-tag>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template #default="scope">
                        <el-button type="primary" :icon="View" circle size="mini" title="详情" @click="show(scope.row.id)"></el-button>
                        <el-button v-if="scope.row.status == 3 || scope.row.status == 2" type="danger" :icon="Delete" circle size="mini" title="删除" @click="deleteOrder(scope.row.id)"></el-button>
                </template>
            </el-table-column> 
          </el-table>
        </div>
        <!-- 分页 -->
        <el-pagination
          class="pagination"
          layout="prev, pager, next"
          :current-page="page"
          :total="total"
          :page-size="limit"
          @current-change="fetchData"
        >
        </el-pagination>
      </div>
    </div>
</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, onBeforeUnmount} from 'vue';
import Cookies from 'js-cookie';
import { View, Delete } from '@element-plus/icons-vue';
export default {
    
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        var orders = ref([]);
        var patients = ref([]);
        var statuses = ref(['未支付', '已支付', '已取消', '废弃']);
        var page = ref(1); 
        var limit = ref(10);
        var total = ref(10);
        var patientId = ref(null);
        var orderStatus = ref(null);
        var userId = 0;
        onBeforeMount(async ()=>{
            if (Cookies.get("token") == null){
                    ctx.$message.error("请先登陆账号")
                    ctx.$router.push("/home");
                }else{
                  
                    userId = Cookies.get('userId');
                    fetchData();
                    getPatients();
                }
        });
        const fetchData = async()=>{
            const res = await ctx.$axios.get("/order/list",{params:{userId:userId, page:page.value, limit:limit.value, patientId:patientId.value, status:orderStatus.value}});
            orders.value = res.data.data;
            total.value = res.data.length;
        }
        const getPatients = async()=>{
            const res = await ctx.$axios.get("/patient/list",{params:{userId:userId}});
            patients.value = res.data.patientList;
        }
        const show = (id) =>{
            ctx.$router.push({path:'/order/show/'+id});
        }
        const deleteOrder = async (id) =>{
            const res = await ctx.$axios.get("/order/delete/"+id);
            if (res.data.code === 200){
                ctx.$message.success("删除成功");
                fetchData();
            }else{
                ctx.$message.error("删除失败");
            }
        }
        return {
            orders, page, limit, patients, statuses, total, patientId, orderStatus, 
            show, fetchData, deleteOrder, 
            View, Delete, 
        }
    }   
}
</script>

<style scoped>
.pagination {
    margin-top: 30px;
    height: 42px;
    margin-left: 36%;
}
</style>