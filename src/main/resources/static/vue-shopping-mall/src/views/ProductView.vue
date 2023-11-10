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
            description: '',
            imageURL: '',
            price: '',
            productID: '',
            productName: '',
            salesVolume: 0,
            stockQuantity: 0,
          };
        "
        >+ 上架产品</el-button
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
        prop="productID"
        label="产品ID"
        width="80"
      ></el-table-column>
      <el-table-column
        prop="productName"
        label="产品名字"
        width="180"
      ></el-table-column>
      <el-table-column
        prop="price"
        label="销售价"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="stockQuantity"
        label="库存量"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="salesVolume"
        label="销量"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="description"
        label="产品简介"
        width="500"
      ></el-table-column>
      <!-- 操作按钮显示 -->
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="update(scope.row.productID)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="small"
            @click="deleteById(scope.row.productID)"
            >删除</el-button
          >
        </template>
      </el-table-column>
      <!-- 对话框显示 -->
      <el-dialog
        title="产品信息"
        :visible.sync="dialogFormVisible"
        :append-to-body="true"
      >
        <el-form :model="singleProd">
          <el-form-item label="产品名字" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.productName"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品价格" :label-width="formLabelWidth">
            <el-input v-model="singleProd.price" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="产品描述" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.description"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品库存" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.stockQuantity"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品销量" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.salesVolume"
              autocomplete="off"
            ></el-input>
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
      //存储单个product
      singleProd: {
        description: "",
        imageURL: "",
        price: "",
        productID: "",
        productName: "",
        salesVolume: 0,
        stockQuantity: 0,
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
      .get("http://localhost:8080/products", {
        withCredentials: true,
      })
      .then((resp) => {
        console.log(resp.data.data);
        this.tableData = resp.data.data.products;
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
          "http://localhost:8080/products?currentPage=" +
            this.currentPage +
            "&pageSize=" +
            this.pageSize
        )
        .then((res) => {
          this.totalCount = res.data.data.totalItems;
          this.tableData = res.data.data.products;
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
      if (this.singleProd.productID) {
        //修改
        axios
          .put("http://localhost:8080/products", this.singleProd)
          .then((resp) => {
            console.log(this.singleProd);
            if (resp.data.code == 1) {
              this.dialogFormVisible = false;
              this.page();
              this.$message({ message: "恭喜你，保存成功", type: "success" });
              this.singleProd = {
                description: "",
                imageURL: "",
                price: "",
                productID: "",
                productName: "",
                salesVolume: 0,
                stockQuantity: 0,
              };
            } else {
              this.$message.error(resp.data.msg);
            }
          });
      } else {
        //添加
        axios
          .post("http://localhost:8080/products", this.singleProd)
          .then((resp) => {
            console.log(this.singleProd);
            if (resp.data.code == 1) {
              this.dialogFormVisible = false;
              this.page();
              this.$message({ message: "恭喜你，保存成功", type: "success" });
              this.singleProd = {
                description: "",
                imageURL: "",
                price: "",
                productID: "",
                productName: "",
                salesVolume: 0,
                stockQuantity: 0,
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
            .delete("http://localhost:8080/products/delete/" + id)
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
            .post(
              "http://localhost:8080/products/deleteBatch",
              this.selectedIds
            )
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
      //发送修改请求
      axios.get("http://localhost:8080/products/" + id).then((res) => {
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
