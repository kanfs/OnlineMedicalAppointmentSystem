<template>
    <div class="app-container">
        <div class="search-div">
            <el-form label-width="70px" size="small">
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="名称">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.keyword"
                                placeholder="名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="5">
                        <el-form-item label="性质" prop="style">
                            <el-select v-model="searchObj.style" placeholder="医院性质">
                                <el-option
                                v-for="item in styles"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="5">
                        <el-form-item label="等级" prop="level">
                            <el-select v-model="searchObj.level" placeholder="医院等级">
                                <el-option
                                v-for="item in levels"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="5">
                        <el-form-item label="类型" prop="type">
                            <el-select v-model="searchObj.type" placeholder="医院类型">
                                <el-option
                                v-for="item in types"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
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
                                :disabled="$hasBP('bnt.hospital.list') === false"
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
                                :disabled="$hasBP('bnt.hospital.add') === false"
                                >添 加
                            </el-button>
                        </div>
                        <div>
                            <a
                                :disabled="
                                    $hasBP('bnt.hospital.export') === false
                                "
                                href="http://localhost:8160/hospital/system/hospital/excel/export_user_template"
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
                                    $hasBP('bnt.hospital.export') === false
                                "
                                href="http://localhost:8160/hospital/system/hospital/excel/export_user"
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
                                    $hasBP('bnt.hospital.import') === false
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
                                        :action="'http://localhost:8888/hospital/system/hospital/excel/import_hospital'"
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
            <!--  表中表 =>部门列表 -->
            <el-table-column type="expand" width="30px">
                <template slot-scope="props">
                    
                    <el-table
                        v-loading="departmentListLoading"
                        :data="props.row.departmentList"
                        stripe
                        border
                        style="width: 100%; margin-top: 10px"
                    >
                        <el-table-column label="序号" width="150" align="center">
                            <template slot-scope="scope">
                                {{ scope.row.id }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="name"
                            label="名称"
                            width="140"
                        />
                        <el-table-column
                            align="center"
                            prop="intro"
                            label="功能介绍"
                            width="540"
                        />
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
                                    @click="editDepartment(scope.row.id)"
                                    :disabled="$hasBP('bnt.department.update') === false"
                                    title="修改"
                                />

                                <el-button
                                    type="danger"
                                    icon="el-icon-delete"
                                    size="mini"
                                    @click="removeDepartmentById(scope.row.id)"
                                    :disabled="$hasBP('bnt.department.remove') === false"
                                    title="删除"
                                />
                            </template>
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
        <!--  表中表 =>部门列表结束 -->
            <el-table-column label="序号" width="50" align="center">
                <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="name"
                label="名称"
                width="110"
            />
            <el-table-column
                align="center"
                prop="style"
                label="性质"
                width="50"
            />
            <el-table-column
                align="center"
                prop="level"
                label="等级"
                width="50"
            >
            </el-table-column>
            <el-table-column
                align="center"
                prop="type"
                width="50"
                label="类型"
            />
            <el-table-column
                align="center"
                prop="address"
                width="100"
                label="地址"
            />
            <el-table-column
                align="center"
                prop="telephone"
                width="100"
                label="联系电话"
            />
            <el-table-column
                align="center"
                prop="intro"
                width="660"
                label="介绍"
            >
                <template slot-scope="scope">
                    <tooltip :message="scope.row.intro"></tooltip>
                </template>
            </el-table-column>
            

            <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        type="success"
                        icon="el-icon-plus"
                        size="mini"
                        @click="addDepartment(scope.row.id)"
                        :disabled="$hasBP('bnt.department.add') === false"
                        title="添加"
                    />

                    <el-button
                        type="primary"
                        icon="el-icon-edit"
                        size="mini"
                        @click="edit(scope.row.id)"
                        :disabled="$hasBP('bnt.hospital.update') === false"
                        title="修改"
                    />

                    <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        @click="removeDataById(scope.row.id)"
                        :disabled="$hasBP('bnt.hospital.remove') === false"
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
                :model="hospital"
                label-width="100px"
                size="small"
                style="padding-right: 40px"
            >
                <el-form-item label="名称" prop="name">
                    <el-input v-model="hospital.name" />
                </el-form-item>
                <el-form-item label="性质" prop="style">
                    <el-select v-model="hospital.style" placeholder="医院性质">
                        <el-option
                        v-for="item in styles"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="等级" prop="level">
                    <el-select v-model="hospital.level" placeholder="医院等级">
                        <el-option
                        v-for="item in levels"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-select v-model="hospital.type" placeholder="医院类型">
                        <el-option
                        v-for="item in types"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="地址" prop="address">
                    <el-input v-model="hospital.address" />
                </el-form-item>
                <el-form-item label="联系电话" prop="telephone">
                    <el-input v-model="hospital.telephone" />
                </el-form-item>
                <el-form-item label="医院简介" prop="intro">
                    <el-input v-model="hospital.intro" />
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
        <el-dialog title="添加/修改" :visible.sync="dialogDepartmentVisible" width="40%">
            <el-form
                ref="dataForm"
                :model="department"
                label-width="100px"
                size="small"
                style="padding-right: 40px"
            >
                <el-form-item label="名称" prop="name">
                    <el-input v-model="department.name" />
                </el-form-item>
                <el-form-item label="功能介绍" prop="intro">
                    <el-input v-model="department.intro" />
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button
                    @click="dialogDepartmentVisible = false"
                    size="small"
                    icon="el-icon-refresh-right"
                    >取 消</el-button
                >
                <el-button
                    type="primary"
                    icon="el-icon-check"
                    @click="saveOrUpdateDepartment()"
                    size="small"
                    >确 定</el-button
                >
            </span>
        </el-dialog>
    </div>
</template>

<script>
import hospitalApi from '@/api/information/hospital';
import departmentApi from '@/api/information/department';
import store from '@/store';
import tooltip from "@/views/information/hospital/tooltip.vue"

const defaultForm = {
    id: '',
    name: '',
    style: '',
    level: '',
    type: '',
    address:'',
    departmentNum:0,
    doctorNum:0,
    telephone:'',
    intro:'',
    departmentList:[],
}
const defaultDepartmentForm = {
    id: '',
    name: '',
    intro: "",
}
export default {
    components: {
		tooltip
	},
    data() {
        return {
            styles :[{
                value: '公立',
                label: '公立',
            },{
                value:'民营',
                label:'民营',
            }],
            // 三甲 三级 二级 二甲 未定级 一级 一甲
            levels:[{
                value:'三甲',
                label:'三甲',
            },{
                value:'三级',
                label:'三级',
            },{
                value:'二级',
                label:'二级',
            },{
                value:'二甲',
                label:'二甲',
            },{
                value:'未定级',
                label:'未定级',
            },{
                value:'一级',
                label:'一级',
            },{
                value:'一甲',
                label:'一甲',
            }],
            // 综合医院 传染病医院 中医医院 中西医结合医院 儿童医院 心血管病医院 精神病医院 其它医院 肛肠医院 口腔医院 妇幼保健院 妇产医院 肿瘤医院 整形外科医院 胸科医院 门诊部 耳鼻喉科医院 眼科医院 皮肤病医院 骨科医院 康复医院 职业病医院 社区卫生服务中心 卫生院 血液病医院 民族医院 疾病预防控制中心 急救中心 诊所 美容医院
            types:[{
               value:'综合医院',
               label:'综合医院', 
            },{
                value:'传染病医院',
                label:'传染病医院',
            },{
                value:'中医医院',
                label:'中医医院',
            },{
                value:'中西医结合医院',
                label:'中西医结合医院',
            },{
                value:'儿童医院',
                label:'儿童医院',
            },{
                value:'心血管病医院',
                label:'心血管病医院',
            },{
                value:'精神病医院',
                label:'精神病医院',
            },{
                value:'其它医院',
                label:'其它医院',
            },{
                value:'肛肠医院',
                label:'肛肠医院',
            },{
                value:'口腔医院',
                label:'口腔医院',
            },{
                value:'妇幼保健院',
                label:'妇幼保健院',
            },{
                value:'妇产医院',
                label:'妇产医院',
            },{
                value:'肿瘤医院',
                label:'肿瘤医院',
            },{
                value:'整形外科医院',
                label:'整形外科医院',
            },{
                value:'胸科医院',
                label:'胸科医院',
            },{
                value:'门诊部',
                label:'门诊部',
            },{
                value:'耳鼻喉科医院',
                label:'耳鼻喉科医院',
            },{
                value:'眼科医院',
                label:'眼科医院',
            },{
                value:'皮肤病医院',
                label:'皮肤病医院',
            },{
                value:'骨科医院',
                label:'骨科医院',
            },{
                value:'康复医院',
                label:'康复医院',
            },{
                value:'职业病医院',
                label:'职业病医院',
            },{
                value:'社区卫生服务中心',
                label:'社区卫生服务中心',
            },{
                value:'卫生院',
                label:'卫生院',
            },{
                value:'血液病医院',
                label:'血液病医院',
            },{
                value:'民族医院',
                label:'民族医院',
            },{
                value:'疾病预防控制中心',
                label:'疾病预防控制中心',
            },{
                value:'急救中心',
                label:'急救中心',
            },{
                value:'诊所',
                label:'诊所',
            },{
                value:'美容医院',
                label:'美容医院',
            }],
            
            listLoading: true, // 数据是否正在加载
            departmentListLoading: false, // 部门数据是否正在加载
            list: null, // banner列表
            total: 0, // 数据库中的总记录数
            page: 1, // 默认页码
            limit: 10, // 每页记录数
            searchObj: {
                hospitalId: store.getters.hospitalId, // 医院id
            }, // 查询表单对象


            dialogVisible: false,
            dialogDepartmentVisible:false, 
            hospital: defaultForm,
            department: defaultDepartmentForm, 
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

            hospitalApi.getPageList(this.page, this.limit, this.searchObj).then(
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
                return hospitalApi.removeById(id)
            }).then((response) => {
                this.fetchData(this.page)
                this.$message.success(response.message || '删除成功')
            }).catch(() => {
                this.$message.info('取消删除')
            })
        },

        // 根据id删除部门
        removeDepartmentById(id) {
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
            this.hospital = {}
            this.fileList = []
        },
        //弹出添加部门表单
        addDepartment(hospitalId) {
            this.dialogDepartmentVisible = true
            this.department = {
                hospitalId: hospitalId,
            }
        },
        //编辑
        edit(id) {
            this.dialogVisible = true
            hospitalApi.getById(id).then(response => {
                this.hospital = response.data
            })
        },
        //编辑部门
        editDepartment(id) {
            this.dialogDepartmentVisible = true
            departmentApi.getById(id).then(response => {
                this.department = response.data
            })
        },

        //添加或更新
        saveOrUpdate() {
            if (!this.hospital.id) {
                this.save()
            } else {
                this.update()
            }
        },

        //添加或更新部门
        saveOrUpdateDepartment() {
            if (!this.department.id) {
                this.saveDepartment()
            } else {
                this.updateDepartment()
            }
        },

        //添加
        save() {
            hospitalApi.save(this.hospital).then(response => {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        //更新
        update() {
            hospitalApi.updateById(this.hospital).then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        //添加
        saveDepartment() {
            departmentApi.save(this.department).then(response => {
                this.$message.success('操作成功')
                this.dialogDepartmentVisible = false
                this.fetchData(this.page)
            })
        },

        //更新
        updateDepartment() {
            departmentApi.updateById(this.department).then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogDepartmentVisible = false
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
