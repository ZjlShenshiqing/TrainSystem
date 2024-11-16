<template>
  <div class="welcome">
    <h1>常用旅客</h1>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <!-- 增加loading可以防止用户不断的点击提交 -->
    <a-table :data-source="passengers"
             :columns="columns"
             :pagintypation="pagination"
             @change="handleTableChange"
             :loading="loading">
    <!--

      增加额外的列，这边是需要增加一个编辑的按钮
      自定义表格单元格的内容。#bodyCell 是插槽的名字

      { column, record }：插槽传递的参数，其中：
          column：表示当前单元格所在的列配置对象。
          record：表示当前行的数据对象。

      v-if="column.dataIndex === 'operation'"：判断当前列是否为 operation（操作列）。如果是，则显示自定义内容。
      <a-space>：Ant Design Vue 中的 a-space 组件，用于在多个元素之间增加间距。

    -->
        <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'operation'">
              <a-space>
                <!--
                 @click="onEdit(record)"：点击事件绑定了 onEdit 方法，并传入当前行的数据对象 record。
                  当用户点击 "编辑" 按钮时，会调用 onEdit(record) 方法，
                  并将当前行的数据对象 record 作为参数传递进去。
                 -->
                <a @click="onEdit(record)">编辑</a>
              </a-space>
            </template>
        </template>
    </a-table>
    <!-- 模态框 -->
    <a-modal v-model:visible="visible" title="乘车人" @ok="handleOk" ok-text="确认" cancel-text="取消">
      <a-form :model="passenger" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
        <a-form-item label="姓名">
          <a-input v-model:value="passenger.name" placeholder="请输入姓名" />
        </a-form-item>
        <a-form-item label="身份证">
          <a-input v-model:value="passenger.idCard" placeholbillder="请输入身份证号" />
        </a-form-item>
        <a-form-item label="类型">
          <a-select v-model:value="passenger.type" placeholder="请选择类型">
            <a-select-option value="1">成人</a-select-option>
            <a-select-option value="2">儿童</a-select-option>
            <a-select-option value="3">学生</a-select-option>
          </a-select>
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

// 使用 ref
let passenger = ref({
  id: null,
  name: '',
  idCard: '',
  type: '1',
  createTime: null,
  updateTime: null
});

// 模态框的显示状态
const visible = ref(false);

// 新增状态：显示模态框
const onAdd = () => {
  visible.value = true;
};

// 修改状态
const onEdit = (record) => {
  passenger.value = record;
  visible.value = true;
};

// 提交表单数据
const handleOk = () => {
  console.log('提交的数据：', passenger);
  axios
      .post('/member/passenger/save', passenger.value)
      .then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '保存成功！' });
          visible.value = false;
          // 刷新列表，刷新一下当前的列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        } else {
          notification.error({ description: data.message });
        }
      })
      .catch((error) => {
        console.error('请求出错：', error);
        notification.error({ description: '保存失败，请重试！' });
      });
};

const passengers = ref([]);
// 分页的三个属性名是完全固定的
const pagination = ref({
  total: 0, // 列表的总数
  current: 1, // 当前的页码
  pageSize: 2 // 每页条数
})
let loading = ref(false);
const columns = ref([
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '身份证号',
    dataIndex: 'idCard',
    key: 'idCard',
  },
  {
    title: '旅客类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
]);

const handleQuery = (param) => {
  // 不带参数的时候，初始化查第一页
  if(!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    };
  }
  // 显示loading的效果
  loading.value = true;
  axios.get("/member/passenger/query-list" , {
    // 需要带上分页参数 注意vue的get请求就是这样写的
    params: {
      page: param.page,
      size: param.size
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      passengers.value = data.content.list;
      // 设置分页控件的值,设置当前的页码
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message})
    }
  })
}

const handleTableChange = (pagination) => {
  handleQuery({
    page: pagination.current, // 当前点击页
    size: pagination.pageSize
  })
}

onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  })
})
</script>

<style scoped>
.welcome {
  text-align: center;
  margin-top: 20px;
}
</style>