<template>
  <div class="welcome">
    <h1>站点管理</h1>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <!-- 增加loading可以防止用户不断的点击提交 -->
    <a-table :dataSource="stations"
             :columns="columns"
             :pagination="pagination"
             @change="handleTableChange"
             :loading="loading">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                @confirm="onDelete(record)"
                ok-text="确认" cancel-text="取消">
              <a style="color: red">删除</a>
            </a-popconfirm>
            <a @click="onEdit(record)">编辑</a>
          </a-space>
        </template>
      </template>
    </a-table>
    <!-- 模态框 -->
    <a-modal v-model:visible="visible" title="车站" @ok="handleOk"
             ok-text="确认" cancel-text="取消">
      <a-form :model="station" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
        <a-form-item label="站名">
          <a-input v-model:value="station.name" />
        </a-form-item>
        <a-form-item label="站名拼音">
          <a-input v-model:value="station.namePinyin" disabled/>
        </a-form-item>
        <a-form-item label="拼音首字母">
          <a-input v-model:value="station.namePy" disabled/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
// onMonted: 这个方法是Vue生命周期的钩子函数
import { reactive, ref, onMounted } from 'vue';
import { notification } from 'ant-design-vue';
import axios from 'axios';

// emum.js 的引入
const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;

// 使用 ref
let station = ref({
  id: null,
  name: '',
  namePinyin: '',
  namePy: '1',
  createTime: null,
  updateTime: null,
});

// 模态框的显示状态
const visible = ref(false);

// 新增状态：显示模态框
const onAdd = () => {
  // 新增时，将乘客的所有值清空，这样新增的模态框不会显示任何的数据
  passenger.value = {};
  visible.value = true;
};

// 修改状态
const onEdit = (record) => {
  /*
  * 需要解决的问题：模态框输入会导致表格的数据也会跟着变化
  * passenger.value = record; 也就是这一行代码需要改进
  *
  * 解决方案：复制一个新的对象，copy一下
  * */
  passenger.value = window.Tool.copy(record)
  visible.value = true;
};

const passengers = ref([]);
// 分页的三个属性名是完全固定的
const pagination = ref({
  total: 0, // 列表的总数
  current: 1, // 当前的页码
  pageSize: 2 // 每页条数
})
let loading = ref(false);
const columns = [
  {
    title: '站名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '站名拼音',
    dataIndex: 'namePinyin',
    key: 'namePinyin',
  },
  {
    title: '站名拼音首字母',
    dataIndex: 'namePy',
    key: 'namePy',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
];

/**
 * 查询方法
 * Created By Zhangjilin 2024/11/21
 * @param param 传入的页数和size
 */
const handleQuery = (param) => {
  if (!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get("/business/admin/station/query-list", {
    params: {
      page: param.page,
      size: param.size
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      stations.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

const onDelete = (record) => {
  axios.delete("/business/station/delete/" + record.id).then((response) => {
    const data = response.data;
    if (data.success) {
      notification.success({description: "删除成功！"});
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      });
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 保存站点信息
 * Created By Zhangjilin 2024/11/21
 */
const handleOk = () => {
  axios.post("/business/admin/station/save", station.value).then((response) => {
    let data = response.data;
    if (data.success) {
      notification.success({description: "保存成功！"});
      visible.value = false;
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    } else {
      notification.error({description: data.message});
    }
  });
};

const handleTableChange = (pagination) => {
  handleQuery({
    page: pagination.current, // 当前点击页
    size: pagination.pageSize
  })
}

/**
 * 钩子函数，初始化页面时调用
 * Created By Zhangjilin 2024/11/21
 */
onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  });
});
</script>

<style scoped>
.welcome {
  text-align: center;
  margin-top: 20px;
}
</style>