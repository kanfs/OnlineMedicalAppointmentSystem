<template>
  <el-dialog
    :close-on-click-modal="false"
    :close-on-press-escape="true"
    title="新增排班"
    :type="type"
    :visible.sync="isVisible"
    width="800px"
    top="150px"
  >

    <el-table
      v-loading="listLoading"
      :data="doctorInfo"
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
      <el-table-column prop="availableNum" align="center" label="号量" width="60"/>
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
      <el-table-column prop="doctorGoodat" align="center" label="擅长技能" type="expand">
        <template slot-scope="scope">
          {{scope.row.doctorGoodat}}
        </template>
      </el-table-column>
      <el-table-column  align="center" label="操作" width="100">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" circle title="修改" @click="editData(scope.row)"></el-button>
          <el-button type="success" size="mini" icon="el-icon-plus" circle title="添加" @click="submitData(scope.row)"></el-button>
        </template>
      </el-table-column>
    </el-table>

    <div slot="footer" class="dialog-footer">
      <el-button plain type="warning" @click="isVisible = false">取消</el-button>
    </div>
    <el-dialog>
      <!-- <el-form :model="schedule" label-width="120px">
        
      </el-form> -->
    </el-dialog>
  </el-dialog>
</template>
<script>

import scheduleApi from '@/api/schedule/schedule'

export default {
  components: {},
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'add'
    }
  },
  data() {
    //这里存放数据
    return {
      doctorInfo: [],
      listLoading: false,
      workDate: null,
      departmentId: null,
      updateDialogVisible: false,
    }
  },
  //计算属性 类似于 data 概念
  computed: {
    isVisible: {
      get() {
        return this.dialogVisible
      },
      set() {
        this.close()
        this.reset()
      }
    },
    title() {
      return this.type === 'add'
        ? '添加'
        : '修改'
    }
  },
  //监控 data 中的数据变化
  watch: {},
  //方法集合
  methods: {
    setData(workDate, departmentId) {
      this.workDate = workDate
      this.departmentId = departmentId
      this.listLoading = true
      scheduleApi.getFreeDoctorList({weekDay: workDate, departmentId: departmentId})
      .then(res => {
        this.doctorInfo = res.data
        this.listLoading = false
      })
      this.listLoading = false
    },
    close() {
      this.doctorInfo = []
      this.$emit('close')
    },
    reset() {
      // 先清除校验，再清除表单，不然有奇怪的bug
      this.doctorInfo = []
    },
    //修改表单数据
    editData(row){
      this.$emit('update', row)
    },
    //提交表单数据
    submitData(row) {
      this.$confirm('确定对该医生进行排班吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.workDate = this.workDate
        scheduleApi.save(row).then(res => {
          this.$message({
            message: '排班成功',
            type: 'success'
          });
          this.$emit('close')
          this.listLoading = true
          scheduleApi.getFreeDoctorList({weekDay: this.workDate, departmentId: this.departmentId})
            .then(res => {
              this.doctorInfo = res.data
              this.listLoading = false
            })
          this.listLoading = false
        })
      }).catch(() => {
      })
    },
  },
  //生命周期 - 创建完成（可以访问当前 this 实例）
  created() {

  },
  //生命周期 - 挂载完成（可以访问 DOM 元素）
  mounted() {

  }
}
</script>
<style lang="scss" scoped>
//@import url(); 引入公共 css 类
</style>
