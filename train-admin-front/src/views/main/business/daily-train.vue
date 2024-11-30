<template>
<div class="dailyTrain">
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      <train-select-view v-model="params.code" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
      <a-button type="danger" @click="onClickGenDaily">手动生成车次信息</a-button>
    </a-space>
  </p>
  <!-- 表格 -->
  <a-table :dataSource="dailyTrains"
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
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in TRAIN_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.type">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <!-- 手动生成每日车次 -->
  <a-modal v-model:visible="visible" title="每日车次" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrain" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrain.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select-view v-model="dailyTrain.code" @change="onChangeCode"></train-select-view>
      </a-form-item>
      <a-form-item label="车次类型">
        <a-select v-model:value="dailyTrain.type">
          <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="始发站">
        <a-input v-model:value="dailyTrain.start" />
      </a-form-item>
      <a-form-item label="始发站拼音">
        <a-input v-model:value="dailyTrain.startPinyin" />
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="dailyTrain.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
      <a-form-item label="终点站">
        <a-input v-model:value="dailyTrain.end" />
      </a-form-item>
      <a-form-item label="终点站拼音">
        <a-input v-model:value="dailyTrain.endPinyin" />
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="dailyTrain.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
    </a-form>
  </a-modal>
  <!-- 通过日期生成每日车次 -->
  <a-modal v-model:visible="genDailyVisible" title="生成车次" @ok="handleGenDailyOk"
           :confirm-loading="genDailyLoading" ok-text="确认" cancel-text="取消">
    <a-form :model="genDaily" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="genDaily.date" placeholder="请选择日期"/>
      </a-form-item>
    </a-form>
  </a-modal>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/train-select";
import dayjs from 'dayjs';

const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;
const visible = ref(false);
let loading = ref(false);

let params = ref({
  code: null
});

const genDaily = ref({
  date: null
});

const genDailyVisible = ref(false);
const genDailyLoading = ref(false);

let dailyTrain = ref({
  id: undefined,
  date: undefined,
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

const dailyTrains = ref([]);

// 分页属性 这是初始的属性
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10,
});

const columns = [
  {
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
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
 * 添加方法
 * Created By Zhangjilin 2024/11/30
 */
const onAdd = () => {
  // 置空
  dailyTrain.value = {};
  visible.value = true;
}

/**
 * 编辑
 * Created By Zhangjilin 2024/11/30
 */
const onEdit = (record) => {
  dailyTrain.value = window.Tool.copy(record);
  visible.value = true;
}

/**
 * 删除
 * Created By Zhangjilin 2024/11/30
 */
const onDelete = (record) => {
  axios.delete("/business/admin/daily-train/delete/" + record.id).then((response) => {
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
 * 保存
 * Created By Zhangjilin 2024/11/30
 */
const handleOk = () => {
  axios.post("/business/admin/daily-train/save", dailyTrain.value).then((response) => {
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
}

/**
 * 查询方法
 * Created By Zhangjilin 2024/11/30
 */
const handleQuery = (param) => {
  if (!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get("/business/admin/daily-train/query-list", {
    params: {
      page: param.page,
      size: param.size,
      code: params.value.code,
      date: params.value.date
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      dailyTrains.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 分页变化时的方法
 * Created By Zhangjilin 2024/11/30
 */
const handleTableChange = (pagination) => {
  handleQuery({
    page: pagination.current,
    size: pagination.pageSize
  });
}

/**
 * 点击每日生成车次数据的时候调用的函数
 * Created By Zhangjilin 2024/11/30
 */
const onClickGenDaily = () => {
  genDailyVisible.value = true;
};

/**
 * 处理车次选择变化
 * Created By Zhangjilin 2024/11/30
 */
const onChangeCode = (train) => {

}

/**
 * 生成每日车次，按这个就会自动生成
 * Created By Zhangjilin 2024/11/30
 */
const handleGenDetailOk = () => {

}

/**
 * 钩子函数，第一次进入到页面的到时候加载该函数
 * Created By Zhangjilin 2024/11/30
 */
onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  });
});

</script>

<style scoped>
.dailyTrain {
  text-align: center;
  margin-top: 20px;
}
</style>