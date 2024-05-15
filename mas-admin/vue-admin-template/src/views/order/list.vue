<template>
    <div class="app-container">
        <div class="search-div">
            <el-form label-width="70px" size="small">
                <el-row>
                    <el-col :span="6">
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
                                v-model="searchObj.departmentName"
                                placeholder="所属部门名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="医生姓名">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.doctorName"
                                placeholder="医生姓名"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="患者姓名">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.patientName"
                                placeholder="患者姓名"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="预约日期">
                            <el-date-picker
                            v-model="searchObj.workDate"
                            type="date"
                            placeholder="选择日期"
                            :picker-options="pickerOptions">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="状态" prop="status">
                            <el-select v-model="searchObj.status" placeholder="订单状态">
                                <el-option
                                v-for="item in statuses"
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
                                :disabled="$hasBP('bnt.order.list') === false"
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
                                :disabled="$hasBP('bnt.order.add') === false"
                                >添 加
                            </el-button>
                        </div>
                        <div>
                            <a
                                :disabled="
                                    $hasBP('bnt.order.export') === false
                                "
                                href="http://localhost:8888/order/system/order/excel/export"
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
                                    $hasBP('bnt.order.export') === false
                                "
                                href="http://localhost:8888/order/system/order/excel/export_user"
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
                                    $hasBP('bnt.order.import') === false
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
                                        :action="'http://localhost:8888/order/system/order/excel/import_order'"
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
            <el-table-column label="序号" width="50" align="center">
                <template slot-scope="scope">
                    {{ (searchObj.page - 1) * searchObj.limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="hospitalName"
                label="医院名称"
                width="140"
            />
            <el-table-column
                align="center"
                prop="departmentName"
                label="部门名称"
                width="120"
            />
            <el-table-column
                align="center"
                prop="doctorName"
                label="医生名称"
                width="120"
            />
            <el-table-column
                align="center"
                prop="patientName"
                label="患者名称"
                width="120"
            />
            <el-table-column
                align="center"
                prop="workDate"
                label="预约日期"
                width="100"
            >
                <template slot-scope="scope">
                    {{ new Date(scope.row.workDate).toLocaleDateString() }}
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="number"
                label="号序"
                width="60"
            />
            <el-table-column
                align="center"
                prop="状态"
                label="status"
                width="100"
            >
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.status === 0" type="info">未支付</el-tag>
                    <el-tag v-if="scope.row.status === 1" type="success">已支付</el-tag>
                    <el-tag v-if="scope.row.status === 2" type="danger">已取消</el-tag>
                    <el-tag v-if="scope.row.status === 3" type="warning">已过期</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="price"
                label="价格"
                width="100"
            />
            <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        type="primary"
                        icon="el-icon-edit"
                        size="mini"
                        @click="edit(scope.row.id)"
                        :disabled="$hasBP('bnt.order.update') === false"
                        title="编辑"
                    />

                    <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        @click="removeDataById(scope.row.id)"
                        :disabled="$hasBP('bnt.order.remove') === false"
                        title="删除"
                    />
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页组件 -->

        <el-pagination
            @size-change="handleSizeChange"
            @current-change="fetchData"
            :current-page="searchObj.page"
            :page-sizes="[5, 10, 15, 20, 30, 40, 50, 100]"
            :page-size="searchObj.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="padding: 30px 0; text-align: center"
        >
        </el-pagination>

        <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
            <el-form
                ref="dataForm"
                :model="order"
                label-width="100px"
                size="small"
                style="padding-right: 40px"
            >
                <el-form-item label="医生名称" prop="doctorName">
                    <el-input v-model="order.doctorName" />
                </el-form-item>
                <el-form-item label="患者名称" prop="patientName">
                    <el-input v-model="order.orderName" />
                </el-form-item>
                <el-form-item label="患者证件号" prop="identifyCardNumber">
                    <el-input v-model="order.identifyCardNumber" />
                </el-form-item>
                <el-form-item label="号序" prop="number">
                    <el-input v-model="order.number" />
                </el-form-item>
                <el-form-item label="预约日期" prop="workDate">
                    <el-date-picker
                    v-model="order.workDate"
                    type="date"
                    placeholder="选择日期"
                    :picker-options="pickerOptions">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select v-model="order.status" placeholder="状态">
                        <el-option
                        v-for="item in statuses"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
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
import orderApi from '@/api/order/order';
import store from '@/store';
const defaultForm = {
    id: '',
    patientName: '',
    identifyCardNumber:'',
    doctorName: '',
    workDate: new Date(),
    status: 0,
    number: 0,
    price: 0,
}
export default {
    data() {
        return {
            // 主任医师 副主任医师 主治医师 主任技师 暂无 心理咨询师 医师 副主任医师  副主任技师 主管技师 高级按摩师 技师 副主任药师 主任药师 主任检验师 主任康复师 主任护师 副主任检验师 主管检验师 副主任护师 主管康复师 药师 营养师 副主任检验医师 检验技师 主任检验医师 护师 心理治疗师 主管药师 医士 康复师 主管护师 副主任康复师 护士 主治检验医师 验光技师 高级验光技师 主诊医师 检验医师 住院医师 初级验光员 技士 高级验光员 康复士 药士 助产士 乡村医生 检验技士 中级验光员
            
            listLoading: true, // 数据是否正在加载
            list: null, // banner列表
            total: 0, // 数据库中的总记录数
            searchObj: {
                page: 1, // 默认页码
                limit: 10, // 每页记录数
                hospitalId: store.getters.hospitalId,
                doctorId: store.getters.doctorId,
            }, // 查询表单对象

            statuses : [{value:'0',label:'未支付'}, {value:'1',label:'已支付'}, {value:'2',label:'已取消'}, {value:'3',label:'已过期'},],

            dialogVisible: false,
            order: defaultForm,
            saveBtnDisabled: false,

            fileList: [],

            dialogImportVisible: false,
            fullscreenLoading: false,

            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
                shortcuts: [{
                    text: '今天',
                    onClick(picker) {
                    picker.$emit('pick', new Date());
                    }
                }, {
                    text: '昨天',
                    onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24);
                    picker.$emit('pick', date);
                    }
                }, {
                    text: '一周前',
                    onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                    picker.$emit('pick', date);
                    }
                }]
            },
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
        fetchData()
    },

    methods: {
        // 加载banner列表数据
        fetchData(page = 1) {
            debugger
            this.searchObj.page = page

            orderApi.getPageList(this.searchObj).then(
                response => {
                    this.list = response.data
                    this.total = response.data.length
                    // 数据加载并绑定成功
                    this.listLoading = false
                    console.log(response.data)
                }
            )
        },

        // 重置查询表单
        resetData() {
            console.log('重置查询表单')
            this.searchObj = {
                page: 1, // 默认页码
                limit: 10, // 每页记录数
                hospitalId: store.getters.hospitalId,
                doctorId: store.getters.doctorId,
            }
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
                return orderApi.delete(id)
            }).then((response) => {
                this.fetchData(this.searchObj.page)
                this.$message.success(response.message || '删除成功')
            }).catch(() => {
                this.$message.info('取消删除')
            })
        },

        //弹出添加表单
        add() {
            this.dialogVisible = true
            this.order = {
                hospitalId: store.getters.hospitalId,
            }
            this.fileList = []
        },
        //编辑
        edit(id) {
            this.dialogVisible = true
            orderApi.getById(id).then(response => {
                this.order = response.data
            })
        },

        //添加或更新
        saveOrUpdate() {
            if (!this.order.id) {
                this.save()
            } else {
                this.update()
            }
        },

        //添加
        save() {
            orderApi.save(this.order).then(response => {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.fetchData(this.searchObj.page)
            })
        },

        //更新
        update() {
            orderApi.updateById(this.order).then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogVisible = false
                this.fetchData(this.searchObj.page)
            })
        },

        handleSizeChange(value) {
            this.searchObj.limit = value
            this.fetchData()
        },

        
        
        onChange() {
            this.fullscreenLoading = true;
        }
    },
}
</script>

<style scoped>
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

