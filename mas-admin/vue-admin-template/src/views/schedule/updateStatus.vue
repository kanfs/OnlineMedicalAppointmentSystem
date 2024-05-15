<template>
  <el-dialog
    :close-on-click-modal="false"
    :close-on-press-escape="true"
    title="排班调整"
    :type="type"
    :visible.sync="isVisible"
    width="800px"
    top="150px"
  >
    <el-form
        ref="dataForm"
        :model="schedule"
        label-width="100px"
        size="small"
        style="padding-right: 40px"
    >
        <el-form-item label="医生姓名" prop="doctorName">
            <el-input v-model="schedule.doctorName" disabled/>
        </el-form-item>
        <el-form-item label="号源" prop="availableNum">
            <el-input v-model="schedule.availableNum" />
        </el-form-item>
        <el-form-item label="挂号费" prop="price">
            <el-input v-model="schedule.price" />
        </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button
            @click="dialogVisible = false"
            size="small"
            icon="el-icon-refresh-right"
            >取 消</el-button
        >
        <el-button
            type="primary"
            icon="el-icon-check"
            @click="submitData()"
            size="small"
            >确 定</el-button
        >
    </span>

  </el-dialog>
</template>
<script>

import scheduleApi from '@/api/schedule/schedule'
//import noticeApi from "@/api/message/notice";

export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'update'
    },
    schedule:{},
  },
  data() {
    //这里存放数据
    return {
      
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
  created(){

  },
  //监控 data 中的数据变化
  watch: {},
  //方法集合
  methods: {

    close() {
      this.$emit('close')
    },
    reset() {
      
    },
    //提交表单数据
    submitData() {
      this.$emit('close')
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
