<template>
    <div class="app-container">
        <div class="search-div">
            <el-form label-width="70px" size="small">
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="医院名称">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.hospitalName"
                                placeholder="所属医院名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="部门名称">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.name"
                                placeholder="部门名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    
                </el-row>
                <el-row>
                    <div style="display: flex; justify-content: space-between">
                        <div>
                            <el-button
                                type="primary"
                                icon="el-icon-search"
                                size="mini"
                                @click="fetchData()"
                                :disabled="$hasBP('bnt.department.list') === false"
                                >搜索
                            </el-button>
                            <el-button
                                icon="el-icon-refresh"
                                size="mini"
                                @click="resetData"
                                >重置
                            </el-button>
                            <el-button
                                type="success"
                                icon="el-icon-plus"
                                size="mini"
                                @click="add"
                                :disabled="$hasBP('bnt.department.add') === false"
                                >添 加
                            </el-button>
                        </div>
                        <div>
                            <a
                                :disabled="
                                    $hasBP('bnt.department.export') === false
                                "
                                href="http://localhost:8888/department/system/department/excel/export"
                                target="_blank"
                            >
                                <el-button
                                    type="primary"
                                    icon="el-icon-download"
                                    size="mini"
                                    >下载模板
                                </el-button>
                            </a>
                            <a
                                :disabled="
                                    $hasBP('bnt.department.export') === false
                                "
                                href="http://localhost:8888/department/system/department/excel/export_user"
                                target="_blank"
                                style="margin-left: 10px"
                            >
                                <el-button
                                    icon="el-icon-download"
                                    size="mini"
                                    type="primary"
                                    >导出信息
                                </el-button>
                            </a>
                            <el-button
                                :disabled="
                                    $hasBP('bnt.department.import') === false
                                "
                                type="success"
                                icon="el-icon-upload"
                                size="mini"
                                style="margin-left: 10px"
                                >导入
                            </el-button>
                        </div>
                    </div>

                    <template>
                        <el-dialog
                            title="导入"
                            :visible.sync="dialogImportVisible"
                            width="400px"
                            v-loading="fullscreenLoading"
                            element-loading-text="拼命上传中"
                            element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(0, 0, 0, 0.8)"
                        >
                            <el-form label-position="right" label-width="110px">
                                <el-form-item label="文件">
                                    <el-upload
                                        :multiple="false"
                                        :on-change="onChange"
                                        :action="'http://localhost:8888/department/system/department/excel/import_department'"
                                        class="upload-demo"
                                    >
                                        <el-button size="small" type="primary"
                                            >点击上传</el-button
                                        >
                                        <div slot="tip" class="el-upload_tip">
                                            只能上传excel文件,且不超过500kb<br />
                                            <span
                                                style="
                                                    color: red;
                                                    font-weight: bold;
                                                "
                                                >上传过程中不要随意取消操作，上传完成自动会关闭弹窗！</span
                                            >
                                        </div>
                                    </el-upload>
                                </el-form-item>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogImportVisible = false">
                                    取消
                                </el-button>
                            </div>
                        </el-dialog>
                    </template>
                </el-row>
            </el-form>
        </div>

        <!-- 列表 -->
        <el-table
            v-loading="listLoading"
            :data="list"
            stripe
            border
            style="width: 100%; margin-top: 10px"
        >
        <!--  表中表 =>医生列表 -->
                <el-table-column type="expand" width="30px">
                <template slot-scope="props">
                    
                    <el-table
                        v-loading="doctorListLoading"
                        :data="props.row.doctorList"
                        stripe
                        border
                        style="width: 100%; margin-top: 10px"
                    >
                        <el-table-column label="序号" width="50" align="center" >
                            <template slot-scope="scope">
                                {{ scope.row.id }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="name"
                            label="名称"
                            width="120"
                        />
                        <el-table-column
                            align="center"
                            prop="grade"
                            label="级别"
                            width="120"
                        />
                        <el-table-column
                            align="center"
                            prop="title"
                            label="职称"
                            width="120"
                        />
                        <el-table-column
                            align="center"
                            prop="score"
                            label="患者推荐度"
                            width="60"
                        />
                        <el-table-column
                            align="center"
                            prop="goodat"
                            label="擅长"
                            width="560"
                        />
                        <el-table-column label="操作" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button
                                    type="primary"
                                    icon="el-icon-edit"
                                    size="mini"
                                    @click="edit(scope.row.id)"
                                    :disabled="$hasBP('bnt.doctor.update') === false"
                                    title="修改"
                                />

                                <el-button
                                    type="danger"
                                    icon="el-icon-delete"
                                    size="mini"
                                    @click="removeDataById(scope.row.id)"
                                    :disabled="$hasBP('bnt.doctor.remove') === false"
                                    title="删除"
                                />
                            </template>
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
        <!--  表中表 =>医生列表结束 -->
            <el-table-column label="序号" width="100" align="center">
                <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="name"
                label="名称"
                width="160"
            />
            <el-table-column
                align="center"
                prop="hospitalName"
                label="所属医院名称"
                width="200"
            />
            <el-table-column
                align="center"
                prop="intro"
                label="功能介绍"
                width="440"
            >
            </el-table-column>
            <el-table-column
                align="center"
                prop="doctorNum"
                label="医生数量"
                width="100"
            >
            </el-table-column>

            <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        type="primary"
                        icon="el-icon-edit"
                        size="mini"
                        @click="edit(scope.row.id)"
                        :disabled="$hasBP('bnt.department.update') === false"
                        title="修改"
                    />

                    <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        @click="removeDataById(scope.row.id)"
                        :disabled="$hasBP('bnt.department.remove') === false"
                        title="删除"
                    />
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页组件 -->

        <el-pagination
            @size-change="handleSizeChange"
            @current-change="fetchData"
            :current-page="page"
            :page-sizes="[5, 10, 15, 20, 30, 40, 50, 100]"
            :page-size="limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="padding: 30px 0; text-align: center"
        >
        </el-pagination>

        <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
            <el-form
                ref="dataForm"
                :model="department"
                label-width="100px"
                size="small"
                style="padding-right: 40px"
            >
                <el-form-item label="医院名称" prop="hospitalName" >
                    <el-input v-model="department.hospitalName" :disabled="department.hospitalId!=null"/>
                </el-form-item>
                <el-form-item label="科室名称" prop="name">
                    <el-input v-model="department.name" />
                </el-form-item>
                <el-form-item label="科室功能" prop="intro">
                    <el-input v-model="department.intro" />
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
                    @click="saveOrUpdate()"
                    size="small"
                    >确 定</el-button
                >
            </span>
        </el-dialog>

    </div>
</template>

<script>
import departmentApi from '@/api/information/department';
import hospitalApi from '@/api/information/hospital';
import store from '@/store'

const defaultForm = {
    id: '',
    name: '',
    hospitalName:'',
    doctorNum: 0,
    hospitalId: 0,
    intro: '',
}
export default {
    data() {
        return {
            
            listLoading: true, // 数据是否正在加载
            doctorListLoading:false,
            list: null, // banner列表
            total: 0, // 数据库中的总记录数
            page: 1, // 默认页码
            limit: 10, // 每页记录数
            searchObj: {
                hospitalId: store.getters.hospitalId,
            }, // 查询表单对象


            dialogVisible: false,
            department: defaultForm,
            saveBtnDisabled: false,

            fileList: [],

            dialogImportVisible: false,
            fullscreenLoading: false,
        }
    },

    // 生命周期函数：内存准备完毕，页面尚未渲染
    created() {
        console.log('list created......')
        this.fetchData()
    },

    // 生命周期函数：内存准备完毕，页面渲染成功
    mounted() {
        console.log('list mounted......')
    },

    methods: {
        // 加载banner列表数据
        fetchData(page = 1) {
            debugger
            this.page = page

            departmentApi.getPageList(this.page, this.limit, this.searchObj).then(
                response => {
                    this.list = response.data.records
                    this.total = response.data.total
                    // 数据加载并绑定成功
                    this.listLoading = false
                }
            )
        },

        // 重置查询表单
        resetData() {
            console.log('重置查询表单')
            this.searchObj = {}
            this.fetchData()
        },

        // 根据id删除数据
        removeDataById(id) {
            // debugger
            this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => { // promise
                // 点击确定，远程调用ajax
                return departmentApi.removeById(id)
            }).then((response) => {
                this.fetchData(this.page)
                this.$message.success(response.message || '删除成功')
            }).catch(() => {
                this.$message.info('取消删除')
            })
        },

        //弹出添加表单
        add() {
            this.dialogVisible = true
            this.department = {
                hospitalId: store.getters.hospitalId,
                hospitalName:"",
            }
            if ( this.department.hospitalId != null && this.department.hospitalId != 0){
                hospitalApi.getById(store.getters.hospitalId).then(response => {
                    this.department.hospitalName = response.data.name
                })
            }
            this.fileList = []
        },
        //编辑
        edit(id) {
            this.dialogVisible = true
            departmentApi.getById(id).then(response => {
                this.department = response.data
            })
        },

        //添加或更新
        saveOrUpdate() {
            if (!this.department.id) {
                this.save()
            } else {
                this.update()
            }
        },

        //添加
        save() {
            departmentApi.save(this.department).then(response => {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        //更新
        update() {
            departmentApi.updateById(this.department).then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        handleSizeChange(value) {
            this.limit = value
            this.fetchData()
        },

        
        
        onChange() {
            this.fullscreenLoading = true;
        }
    }
}
</script>

<style>
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
</style>
