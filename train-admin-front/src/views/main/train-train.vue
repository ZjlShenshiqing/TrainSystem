<template>
  <div class="welcome">
    <h1>车次管理</h1>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <!-- 增加loading可以防止用户不断的点击提交 -->
    <a-table :dataSource="trains"
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
    <a-modal v-model:visible="visible" title="车次" @ok="handleOk"
             ok-text="确认" cancel-text="取消">
      <a-form :model="train" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
        <a-form-item label="车次编号">
          <a-input v-model:value="train.code" :disabled="!!train.id"/>
        </a-form-item>
        <a-form-item label="车次类型">
          <a-select v-model:value="train.type">
            <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.code" :value="item.code">
              {{item.desc}}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="始发站">
          <station-select-view v-model="train.start"></station-select-view>
        </a-form-item>
        <a-form-item label="始发站拼音">
          <a-input v-model:value="train.startPinyin" />
        </a-form-item>
        <a-form-item label="出发时间">
          <!-- 时间控件，专门用来填入时间 -->
          <a-time-picker v-model:value="train.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
        </a-form-item>
        <a-form-item label="终点站">
          <station-select-view v-model="train.end"></station-select-view>
        </a-form-item>
        <a-form-item label="终点站拼音">
          <a-input v-model:value="train.endPinyin" />
        </a-form-item>
        <a-form-item label="到站时间">
          <a-time-picker v-model:value="train.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
// onMonted: 这个方法是Vue生命周期的钩子函数
import { ref, onMounted, watch } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";

// emum.js 的引入
const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;

// 车次列表，初始化为空数组
const trains = ref([]);

// 当前操作的车次，用于模态框
const train = ref({
  id: undefined,
  code: undefined,
  type: undefined,
  start: undefined,
  startPinyin: undefined,
  startTime: undefined,
  end: undefined,
  endPinyin: undefined,
  endTime: undefined,
  createTime: undefined,
  updateTime: undefined,
});

// 模态框的显示状态
const visible = ref(false);

// 新增状态：显示模态框
const onAdd = () => {
  // 新增时，将车次的所有值清空，这样新增的模态框不会显示任何的数据
  train.value = {};
  visible.value = true;
};

// 修改状态
const onEdit = (record) => {
  /*
  * 需要解决的问题：模态框输入会导致表格的数据也会跟着变化
  * 解决方案：复制一个新的对象，copy一下
  * */
  train.value = window.Tool.copy(record)
  visible.value = true;
};

// 分页的三个属性名是完全固定的
const pagination = ref({
  total: 0, // 列表的总数
  current: 1, // 当前的页码
  pageSize: 8 // 每页条数
})
let loading = ref(false);

// 显示在前端的框
const columns = [
  {
    title: '车次编号',
    dataIndex: 'code',
    key: 'code',
  },
  {
    title: '车次类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '始发站',
    dataIndex: 'start',
    key: 'start',
  },
  {
    title: '始发站拼音',
    dataIndex: 'startPinyin',
    key: 'startPinyin',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '终点站',
    dataIndex: 'end',
    key: 'end',
  },
  {
    title: '终点站拼音',
    dataIndex: 'endPinyin',
    key: 'endPinyin',
  },
  {
    title: '到站时间',
    dataIndex: 'endTime',
    key: 'endTime',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
];

/**
 * 查询方法
 * Created By Zhangjilin 2024/11/22
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
  axios.get("/business/admin/train/query-list", {
    params: {
      page: param.page,
      size: param.size
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      trains.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 删除车次信息
 * Created By Zhangjilin 2024/11/22
 * @param record
 */
const onDelete = (record) => {
  axios.delete("/business/admin/train/delete/" + record.id).then((response) => {
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
 * 保存车次信息
 * Created By Zhangjilin 2024/11/22
 */
const handleOk = () => {
  axios.post("/business/admin/train/save", station.value).then((response) => {
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
 * Created By Zhangjilin 2024/11/22
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