<template>
  <div class="welcome">
    <h1>常用旅客</h1>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="showModal">新增</a-button>
      </a-space>
    </p>
    <!-- 增加loading可以防止用户不断的点击提交 -->
    <a-table :data-source="passengers"
             :columns="columns"
             :pagintypation="pagination"
             @change="handleTableChange"
             :loading="loading"/>
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

// 使用 reactive 包装对象，确保 passenger 是响应式的
const passenger = reactive({
  id: null,
  name: '',
  idCard: '',
  type: '1',
  createTime: null,
  updateTime: null
});

// 模态框的显示状态
const visible = ref(false);

// 显示模态框
const showModal = () => {
  visible.value = true;
};

// 提交表单数据
const handleOk = () => {
  console.log('提交的数据：', passenger);
  axios
      .post('/member/passenger/save', passenger)
      .then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '保存成功！' });
          visible.value = false;
          // 刷新列表，刷新一下当前的列表
          handleQuery({
            page: pagination.current,
            size: pagination.pageSize
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
const pagination = reactive({
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
]);

const handleQuery = (param) => {
  // 不带参数的时候，初始化查第一页
  if(!param) {
    param = {
      page: 1,
      size: pagination.pageSize
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
      pagination.current = param.page;
      pagination.total = data.content.total;
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
    size: pagination.pageSize
  })
})
</script>

<style scoped>
.welcome {
  text-align: center;
  margin-top: 20px;
}
</style>