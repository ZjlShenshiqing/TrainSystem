<template>
<div class="job">
  <h1>火车任务管理</h1>
  <p>
    <a-button type="primary" @click="handleAdd()">
      新增
    </a-button>&nbsp;
    <a-button type="primary" @click="handleQuery()">
      刷新
    </a-button>
  </p>
  <a-table :dataSource="jobs"
           :columns="columns"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="手动执行会立即执行一次，确定执行？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleRun(record)"
          >
            <a-button type="primary" size="small">
              手动执行
            </a-button>
          </a-popconfirm>
          <a-popconfirm
              title="确定重启？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleResume(record)"
          >
            <a-button v-show="record.state === 'PAUSED' || record.state === 'ERROR'" type="primary" size="small">
              重启
            </a-button>
          </a-popconfirm>
          <a-popconfirm
              title="确定暂停？"
              ok-text="是"
              cancel-text="否"
              @confirm="handlePause(record)"
          >
            <a-button v-show="record.state === 'NORMAL' || record.state === 'BLOCKED'" type="primary" size="small">
              暂停
            </a-button>
          </a-popconfirm>
          <a-button type="primary" @click="handleEdit(record)" size="small">
            编辑
          </a-button>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record)"
          >
            <a-button type="danger" size="small">
              删除
            </a-button>
          </a-popconfirm>
        </a-space>
      </template>
    </template>
  </a-table>
  <a-modal
      title="用户"
      v-model:visible="modelVisible"
      :confirm-loading="modelLoading"
      @ok="handleModelOk"
  >
    <a-form :model="job" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="类名">
        <a-input v-model:value="job.name" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="job.description" />
      </a-form-item>
      <a-form-item label="分组">
        <a-input v-model:value="job.group" :disabled="!!job.state"/>
      </a-form-item>
      <a-form-item label="表达式">
        <a-input v-model:value="job.cronExpression" />
        <div class="ant-alert ant-alert-success">
          每5秒执行一次：0/5 * * * * ?
          <br>
          每5分钟执行一次：* 0/5 * * * ?
        </div>
      </a-form-item>
    </a-form>
  </a-modal>
</div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';
import { notification } from 'ant-design-vue';
// 定时任务
const jobs = ref([]);
// 加载效果
const loading = ref();

const columns = [{
  title: '分组',
  dataIndex: 'group',
}, {
  title: '类名',
  dataIndex: 'name',
}, {
  title: '描述',
  dataIndex: 'description',
}, {
  title: '状态',
  dataIndex: 'state',
}, {
  title: '表达式',
  dataIndex: 'cronExpression',
}, {
  title: '上次时间',
  dataIndex: 'preFireTime',
}, {
  title: '下次时间',
  dataIndex: 'nextFireTime',
}, {
  title: '操作',
  dataIndex: 'operation'
}];

// --------------------- 表单 ---------------------
const job = ref();
jobs.value = [];
const modelVisible = ref(false); // 是否可见
const modelLoading = ref(false); // 是否在加载中

/**
 * 表单提交方法
 * Created By Zhangjilin 2024/11/29
 *
 * url = "add"：默认情况下，提交请求的 URL 是 "add"，表示执行一个新增操作。
 * if (job.value.state)：检查 job.value.state 的值。job 是一个响应式对象（表单数据），state 是其中的一个属性。如果 state 为 true 或有值，那么表示是重新调度操作。
 * url = "reschedule";：如果 state 存在，说明是执行调度操作，URL 被设置为 "reschedule"，这样后端接口就会处理该类型的请求。
 */
const handleModelOk = () => {
  modelLoading.value = true;
  let url = "add";
  if (job.value.state) {
    url = "reschedule";
  }
  // 添加定时任务
  axios.post('/batchtask/admin/job/' + url, job.value).then((response) => {
    modelLoading.value = false;
    const data = response.data;
    if (data.success) {
      modelVisible.value = false;
      notification.success({description: "保存成功！"});
      handleQuery(); // 执行查询操作，刷新数据
    } else {
      notification.error({description: data.message});
    }
  })
}

/**
 * 查询方法-查询所有定时任务
 * Created By Zhangjilin 2024/11/29
 * @param param 传入的页数和size
 */
const handleQuery = () => {
  loading.value = true;
  jobs.value = [];
  axios.get('/batchtask/admin/job/query').then((response) => {
    loading.value = false;
    const data = response.data;
    if (data.success) {
      jobs.value = data.content
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 编辑方法-编辑定时任务
 * Created By Zhangjilin 2024/11/29
 */
const handleEdit = (record) => {
  modelVisible.value = true;
  job.value = Tool.copy(record);
};

/**
 * 新增方法-新增定时任务
 * Created By Zhangjilin 2024/11/29
 */
const handleAdd = () => {
  modelVisible.value = true;
  job.value = {
    group: 'DEFAULT'
  };
}

/**
 * 删除方法-删除定时任务
 * Created By Zhangjilin 2024/11/29
 */
const handleDelete = (record) => {
  axios.post('/batchtask/admin/job/delete',{
    name: record.name,
    group: record.group
  }).then((response) => {
    const data = response.data;
    if (data.success) {
      notification.success({description: "删除成功"})
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 手工执行
 * Created By Zhangjilin 2024/11/29
 */
const handleRun = (record) => {
  axios.post('batchtask/admin/job/run', record).then((response) => {
    const data = response.data;
    if (data.success) {
      notification.success({description: "手动执行成功！"})
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 暂停定时任务
 * Created By Zhangjilin 2024/11/29
 */
const handlePause = (record) => {
  axios.post('/batch/admin/job/pause', {
    name: record.name,
    group: record.group
  }).then((response) => {
    const data = response.data;
    if (data.success) {
      notification.success({description: "暂停成功！"});
      handleQuery();
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 重启定时任务
 */
const handleResume = (record) => {
  axios.post('/batchtask/admin/job/reschedule', record).then((response) => {
    modalLoading.value = false;
    const data = response.data;
    if (data.success) {
      modalVisible.value = false;
      notification.success({description: "重启成功！"});
      handleQuery();
    } else {
      notification.error({description: data.message});
    }
  });
};


/**
 * 钩子函数，初始化页面时调用
 * Created By Zhangjilin 2024/11/29
 */
onMounted(() => {
  handleQuery();
});

</script>

<style scoped>
.job {
  text-align: center;
  margin-top: 20px;
}
</style>