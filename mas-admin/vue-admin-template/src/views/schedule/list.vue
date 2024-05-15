<template>
  <div class="app-container">
    <el-container style="height: 620px">
      <el-aside width="160px" style="border: 1px silver solid">
        <!-- 部门 -->
        <el-table
          ref="singleTable"
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          style="width:100%"
          @current-change="handleCurrentChange"
          @row-click="handleRowClick"
        >
          <el-table-column
              align="center"
              prop="name"
              label="名称"
              width="120"
          />
        </el-table>
      </el-aside>
      <el-main style="padding: 0 0 0 20px;">
        <el-row style="width: 100%">
          <!-- 排班日期 分页 -->
          <el-tag v-for="(item,index) in scheduleWeekdayList.scheduleWeekdayRoList"
                  :key="index"
                  :type="index == activeIndex ? '' : 'info'"
                  style="height: 70px;margin-right:15px;cursor:pointer; font-weight: bold; font-size: 15px"
                  @click="selectDate(item.workDate, index)"
          >
            {{ item.workDate }} {{ item.weekday }}<br/>
            {{ item.signalSourceStr }}
          </el-tag>

          <!-- <el-tag v-if="scheduleWeekdayList.scheduleWeekdayRoList.length < this.queryParams.limit"
                  :type="'info'"
                  style="height: 70px;margin-right:15px;cursor:pointer; font-weight: bold; font-size: 15px"
          >
            等待系统排号<br/>
            未安排号源
          </el-tag> -->


          <!-- 分页 -->
          <el-pagination
            :current-page="queryParams.page"
            :total="this.scheduleWeekdayList.total"
            :page-size="queryParams.limit"
            class="pagination"
            layout="prev, pager, next"
            @current-change="getPage"
          >
          </el-pagination>
        </el-row>
        <el-row style="margin-top: 20px;">
          <!-- 排班日期对应的排班医生 -->
          <el-button icon="el-icon-circle-plus-outline" type="primary" size="small" style="margin-bottom: 5px" @click="addSchedule" :disabled="$hasBP('bnt.schedule.add') === false">
          新增排班
          </el-button>
          <el-button icon="el-icon-circle-plus-outline" type="success" size="small" style="margin-bottom: 5px" @click="allSchedule" :disabled="$hasBP('bnt.schedule.add') === false">
          全部排班
          </el-button>
          <el-table
            v-loading="listLoading"
            :data="scheduleList"
            border
            fit
            highlight-current-row
            size="small"
          >
            <el-table-column
              label="序号"
              width="60"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="doctorName" align="center" label="医生" width="100"></el-table-column>
              <el-table-column prop="grade" align="center" label="级别" width="100"/>
              <el-table-column prop="title" align="center" label="职称" width="100"/>
              <el-table-column prop="availableNum" align="center" label="剩余预约数" width="60"/>
              <el-table-column prop="status" align="center" label="排班状态" width="70">
              <template slot-scope="scope">
                <el-switch
                  disabled
                  v-model="scope.row.status"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  :active-value="true"
                  :inactive-value="false">
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column prop="price" align="center" label="挂号费(元)" width="80">
              <template slot-scope="scope">
                {{scope.row.price}} 元
              </template>
            </el-table-column>
            <el-table-column prop="doctorGoodat" align="center" label="擅长技能"/>
            <el-table-column  align="center" label="操作" width="100">
              <template slot-scope="scope">
                <el-button type="warning" size="mini" icon="el-icon-edit" circle title="修改状态" @click="updateSchedule(scope.row)" :disabled="$hasBP('bnt.schedule.update') === false"></el-button>
                <el-button type="danger" icon="el-icon-delete" circle size="mini" title="删除排班" @click="deleteById(scope.row.id)" :disabled="$hasBP('bnt.schedule.remove') === false"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
      </el-main>
    </el-container>

    <!-- 新增排班 -->
    <add-schedule
      ref="add"
      :dialog-visible="dialog.isVisible"
      :type="dialog.type"
      @close="addClose"
      @update="toUpdate"
      @success="addSuccess"/>

    <update-status
      ref="update"
      :dialog-visible="dialogUpdate.isVisible"
      :type="dialogUpdate.type"
      :schedule="schedule"
      @close="updateClose"
      @success="updateSuccess"/>

    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      title="调整排班"
      :type="dialogUpdate.type"
      :visible.sync="updateVisible"
      width="800px"
      top="150px"
      >
      <el-form
          ref="dataForm"
          :model="scheduleU"
          label-width="100px"
          size="small"
          style="padding-right: 40px"
      >
          <el-form-item label="医生姓名" prop="doctorName">
              <el-input v-model="scheduleU.doctorName" disabled/>
          </el-form-item>
          <el-form-item label="号源" prop="availableNum">
              <el-input v-model="scheduleU.availableNum" />
          </el-form-item>
          <el-form-item label="挂号费" prop="price">
              <el-input v-model="scheduleU.price" />
          </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button
              @click="updateVisible = false"
              size="small"
              icon="el-icon-refresh-right"
              >取 消</el-button
          >
          <el-button
              type="primary"
              icon="el-icon-check"
              @click="submitUpdate()"
              size="small"
              >确 定</el-button
          >
      </span>

    </el-dialog>
    <el-dialog title="添加/修改" :visible.sync="allScheduleVisible" width="40%">
            <el-form
                ref="dataForm"
                :model="schedule"
                label-width="100px"
                size="small"
                style="padding-right: 40px"
            >
                <el-form-item label="号源" prop="availableNum">
                    <el-input v-model="schedule.availableNum" />
                </el-form-item>
                <el-form-item label="价格" prop="price">
                    <el-input v-model="schedule.price" />
                </el-form-item>
                
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button
                    @click="allScheduleVisible = false"
                    size="small"
                    icon="el-icon-refresh-right"
                    >取 消</el-button
                >
                <el-button
                    type="primary"
                    icon="el-icon-check"
                    @click="addAllSchedule()"
                    size="small"
                    >确 定</el-button
                >
            </span>
        </el-dialog>
  </div>
</template>
<script>
import scheduleApi from '@/api/schedule/schedule';
import addSchedule from '@/views/schedule/addSchedule';
import updateStatus from '@/views/schedule/updateStatus';
import store from '@/store';
const defaultForm = {
    availableNum: 30,
    price: 50,
}
export default {
  components: {  addSchedule, updateStatus },
  data() {
    return {
      list: [],
      updateVisible:false,
      allScheduleVisible:false,
      schedule:defaultForm,
      queryParams: {
        page: 1,
        limit: 6,
        departmentId: 0
      },
      scheduleWeekdayList: [],
      activeIndex: 0,

      scheduleList: [],
      listLoading: false,
      workDate : null,

      dialog: {
        isVisible: false,
        type: 'add'
      },
      dialogUpdate: {
        isVisible: false,
        type: 'update'
      },
      schedule:{},
      scheduleU:{},
      currentRow:null,
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      // TODO 默认展开的节点，需要处理
      this.listLoading = true
      
      if ( store.getters.hospitalId == 0){
        this.$message("无法找到你负责的医院")
      }
      scheduleApi.findDepartments(store.getters.hospitalId)
        .then(response => {
          if (response.data != null) {
            this.queryParams.departmentId = response.data[0].id
          }
          this.list = response.data
          this.$refs.singleTable.setCurrentRow(this.list[0])
          this.findScheduleWeekday()
          this.listLoading = false
        })
        
      console.log()
      this.listLoading = false
    },
    handleRowClick(row, column, event) {
      this.queryParams.departmentId = row.id
      this.queryParams.page = 1
      this.activeIndex = 0
      this.scheduleWeekdayList = []
      this.scheduleList = []
      this.findScheduleWeekday()
    },
    handleCurrentChange(val) {
        this.currentRow = val;
    },
    findScheduleWeekday() {
      this.listLoading = true
      scheduleApi.scheduleWeekday(this.queryParams)
        .then(res => {
          this.scheduleWeekdayList = res.data
          if (res.data.total != 0 && this.workDate == null) {
            this.workDate = res.data.scheduleWeekdayRoList[0].workDate
            // 查询排班信息
            this.findScheduleList(res.data.scheduleWeekdayRoList[0].workDate, this.queryParams.departmentId)
            this.listLoading = false
          } else {
            this.findScheduleList(this.workDate, this.queryParams.departmentId)
            this.listLoading = false
          }
        })
      this.listLoading = false
    },
    getPage(page) {
      this.queryParams.page = page
      this.activeIndex = 0
      this.listLoading = true
      scheduleApi.scheduleWeekday(this.queryParams)
        .then(res => {
          this.scheduleWeekdayList = res.data
          if (res.data.total != 0) {
            this.workDate = res.data.scheduleWeekdayRoList[0].workDate
            // 查询排班信息
            this.findScheduleList(res.data.scheduleWeekdayRoList[0].workDate, this.queryParams.departmentId)
            this.listLoading = false
          }
        })
      this.listLoading = false
    },
    selectDate(workDate, index) {
      this.workDate = workDate
      this.activeIndex = index
      this.findScheduleList(workDate)
    },
    findScheduleList(workDate) {
      scheduleApi.scheduleList({ weekDay: this.workDate == null ? workDate : this.workDate, departmentId:this.queryParams.departmentId })
        .then(res => {
          this.scheduleList = res.data
        })
    },
    addClose() {
      this.dialog.isVisible = false
      this.findScheduleWeekday()
    },
    toUpdate(row){
      // this.dialog.isVisible = false
      // this.findScheduleWeekday()
      this.schedule = row
      this.dialogUpdate.isVisible = true
    },
    addSuccess() {
      this.findScheduleWeekday()
    },
    addSchedule() {
      // 新增排班的时候，需要判断是否是当天，当天如果是早上6:00以前，允许新增排班
      let hours = new Date().getHours()
      var date = new Date(this.workDate)
      var nowDate = new Date()
      if (hours >= 6
        && date.getFullYear() == nowDate.getFullYear()
        && date.getMonth() == nowDate.getMonth()
        && date.getDay() == nowDate.getDay()) {
        this.$message({
          message: '今天已经过了可以排班的时候了',
          type: 'warning'
        });
      } else {
        // 进行当天的日期排班
        this.dialog.type = 'add'
        this.dialog.isVisible = true
        this.$refs.add.setData(this.workDate, this.queryParams.departmentId)
      }
    },
    allSchedule(){
      this.allScheduleVisible = true
    },
    addAllSchedule(){
      this.$confirm('确定一键排班吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        let hours = new Date().getHours()
        var date = new Date(this.workDate)
        var nowDate = new Date()
        if (hours >= 6
          && date.getFullYear() == nowDate.getFullYear()
          && date.getMonth() == nowDate.getMonth()
          && date.getDay() == nowDate.getDay()) {
          this.$message({
            message: '今天已经过了可以排班的时候了',
            type: 'warning'
          });
        } else {
          // 进行当天的日期排班
          scheduleApi.scheduleAll(this.workDate, this.queryParams.departmentId, this.schedule.availableNum, this.schedule.price).then(res => {
            this.$message({
              message: '一键排班成功',
              type: 'success'
            });
            this.findScheduleWeekday()
          }).catch(() => {})
        }
      })
    },
    deleteById(id){
      this.$confirm('确定删除排班吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        scheduleApi.delete(id)
        .then(res => {
          this.$message({
            message: '排班删除成功',
            type: 'success'
          });
          this.findScheduleWeekday()
        })
      }).catch(() => {
      })
    },
    updateClose() {
      this.dialogUpdate.isVisible = false
      // this.findScheduleWeekday()
    },
    updateSuccess() {
      // this.findScheduleWeekday()
    },
    updateSchedule(row){
      this.updateVisible = true
      this.scheduleU = row
    },
    submitUpdate(){
      scheduleApi.updateSchedule(this.scheduleU)
        .then(res => {
          this.$message({
            message: '排班修改成功',
            type: 'success'
          });
          this.findScheduleWeekday()
          this.updateVisible = false
        })
    }
  }
}
</script>
<style>
.el-tree-node.is-current > .el-tree-node__content {
  background-color: #409EFF !important;
  color: white;
}

.el-checkbox__input.is-checked + .el-checkbox__label {
  color: black;
}
.el-table .success-row {
    background: #f0f9eb;
}
</style>
