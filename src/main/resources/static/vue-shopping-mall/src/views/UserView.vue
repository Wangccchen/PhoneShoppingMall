<template>
  <div id="pdView">
    <!--按钮-->
    <el-row>
      <el-button type="danger" size="medium" @click="deleteByIds">
        - 批量删除</el-button
      >
      <el-button @click="toggleSelection()">取消选择</el-button>
      <el-button
        type="primary"
        size="medium"
        @click="
          dialogFormVisible = true;
          singleProd = {
            userID: 0,
            username: '',
            password: '',
            email: '',
            address: '',
            phoneNumber: '',
            flag: 0,
          };
        "
        >+ 新增用户</el-button
      >
    </el-row>
    <!-- 表格显示 -->
    <el-table
      ref="multipleTable"
      :data="tableData"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column
        prop="userID"
        label="用户ID"
        width="80"
      ></el-table-column>
      <el-table-column
        prop="username"
        label="用户名"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="password"
        label="密码"
        width="200"
      ></el-table-column>
      <el-table-column
        prop="email"
        label="邮箱地址"
        width="300"
      ></el-table-column>
      <el-table-column
        prop="address"
        label="收货地址"
        width="300"
      ></el-table-column>
      <el-table-column
        prop="phoneNumber"
        label="手机号"
        width="300"
      ></el-table-column>
      <el-table-column
        prop="flag"
        label="是否为管理员"
        width="100"
      ></el-table-column>
      <!-- 操作按钮显示 -->
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="update(scope.row.userID)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="small"
            @click="deleteById(scope.row.userID)"
            >删除</el-button
          >
        </template>
      </el-table-column>
      <!-- 对话框显示 -->
      <el-dialog
        title="用户信息"
        :visible.sync="dialogFormVisible"
        :append-to-body="true"
      >
        <el-form :model="singleProd">
          <el-form-item label="用户名字" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.username"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.password"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="邮箱" :label-width="formLabelWidth">
            <el-input v-model="singleProd.email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="收货地址" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.address"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="手机号" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.phoneNumber"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="权限" :label-width="formLabelWidth">
            <el-input v-model="singleProd.flag" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="add">确 定</el-button>
        </div>
      </el-dialog>
    </el-table>
    <!-- 分页栏显示 -->
    <!-- Pagination分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :background="background"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="10"
      :current-page="currentPage"
      layout="sizes,prev, pager, next,jumper,total"
      :total="totalCount"
    >
    </el-pagination>
  </div>
</template>

<script>
import axios from "axios";
//import { page, add, update, deleteById, selectById } from "@/api/prod";
export default {
  data() {
    return {
      background: true,
      //每页显示的条数
      pageSize: 10,
      //当前页码
      currentPage: 1,
      //条数总数
      totalCount: 100,
      // 添加数据对话框是否展示的标记
      dialogVisible: false,
      //存储返回的产品列表
      tableData: [],
      //存储单个user
      singleProd: {
        userID: 0,
        username: "",
        password: "",
        email: "",
        address: "",
        phoneNumber: "",
        flag: 0,
      },
      //表单相关数据
      dialogFormVisible: false,
      formLabelWidth: "120px",
      //被选中的列表
      multipleSelection: [],
      // 被选中的id数组
      selectedIds: [],
    };
  },
  mounted() {
    this.page(); //当页面加载完成后，发送异步请求，获取数据
    axios
      .get("http://localhost:8080/users", {
        withCredentials: true,
      })
      .then((resp) => {
        console.log(resp.data.data);
        this.tableData = resp.data.data.users;
      });
  },
  methods: {
    //更新被选中的列表
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //取消选择
    toggleSelection() {
      this.$refs.multipleTable.clearSelection();
    },
    // 查询分页数据
    page() {
      axios
        .get(
          "http://localhost:8080/users?currentPage=" +
            this.currentPage +
            "&pageSize=" +
            this.pageSize
        )
        .then((res) => {
          this.totalCount = res.data.data.totalItems;
          this.tableData = res.data.data.users;
        });
    },
    //分页
    handleSizeChange(val) {
      // 重新设置每页显示的条数
      this.pageSize = val;
      this.page();
    },

    handleCurrentChange(val) {
      // 重新设置当前页码
      this.currentPage = val;
      this.page();
    },

    // 添加数据
    add() {
      if (this.singleProd.userID) {
        //修改
        axios
          .put("http://localhost:8080/users", this.singleProd)
          .then((resp) => {
            console.log("要修改写入的内容：", this.singleProd);
            if (resp.data.code == 1) {
              this.dialogFormVisible = false;
              this.page();
              this.$message({ message: "恭喜你，保存成功", type: "success" });
              this.singleProd = {
                userID: 0,
                username: "",
                password: "",
                email: "",
                address: "",
                phoneNumber: "",
                flag: 0,
              };
            } else {
              this.$message.error(resp.data.msg);
            }
          });
      } else {
        //添加
        axios
          .post("http://localhost:8080/users", this.singleProd)
          .then((resp) => {
            console.log(this.singleProd);
            if (resp.data.code == 1) {
              this.dialogFormVisible = false;
              this.page();
              this.$message({ message: "恭喜你，保存成功", type: "success" });
              this.singleProd = {
                userID: 0,
                username: "",
                password: "",
                email: "",
                address: "",
                phoneNumber: "",
                flag: 0,
              };
            } else {
              this.$message.error(resp.data.msg);
            }
          });
      }
    },

    //删除信息
    deleteById(id) {
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          axios
            .delete("http://localhost:8080/users/delete/" + id)
            .then((resp) => {
              if (resp.data.code == 1) {
                //删除成功
                this.$message.success("恭喜你，删除成功");
                this.page();
              } else {
                this.$message.error(resp.data.msg);
              }
            });
        })
        .catch(() => {
          //用户点击取消按钮
          this.$message.info("已取消删除");
        });
    },
    //批量删除
    deleteByIds() {
      // 弹出确认提示框
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //点击确认按钮
          for (let i = 0; i < this.multipleSelection.length; i++) {
            this.selectedIds[i] = this.multipleSelection[i].productID;
          }

          axios
            .post("http://localhost:8080/users/deleteBatch", this.selectedIds)
            .then((resp) => {
              if (resp.data.code == 1) {
                //删除成功
                this.$message.success("恭喜你，删除成功");
                this.page();
              } else {
                this.$message.error(resp.data.msg);
              }
            });
        })
        .catch(() => {
          //用户点击取消按钮
          this.$message.info("已取消删除");
        });
    },
    //编辑界面
    update(id) {
      //显示窗口
      this.dialogFormVisible = true;
      console.log(id);
      //先获得要修改的东西
      axios.get("http://localhost:8080/users/" + id).then((res) => {
        if (res.data.code == 1) {
          //console.log(res.data.data);
          this.singleProd = res.data.data;
          console.log(this.singleProd);
        }
      });
    },
  },
};
</script>

<style scoped>
#title {
  background-color: #dcdfe6;
  font-family: "微软雅黑";
  color: #303133;
  font-size: 25px;
  line-height: 60px;
}
</style>