<template>
<div class="dailyTrainTicket">
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"></a-date-picker>
      <station-select-view v-model="params.start" width="200px"></station-select-view>
      <station-select-view v-model="params.end" width="200px"></station-select-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-button type="primary" @click="toOrder(record)">预订</a-button>
      </template>
      <template v-else-if="column.dataIndex === 'station'">
        {{record.start}}<br/>
        {{record.end}}
      </template>
      <template v-else-if="column.dataIndex === 'time'">
        {{record.startTime}}<br/>
        {{record.endTime}}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{calDuration(record.startTime, record.endTime)}}<br/>
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          次日到达
        </div>
        <div v-else>
          当日到达
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ydz'">
        <div v-if="record.ydz >= 0">
          {{record.ydz}}<br/>
          {{record.ydzPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'edz'">
        <div v-if="record.edz >= 0">
          {{record.edz}}<br/>
          {{record.edzPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'rw'">
        <div v-if="record.rw >= 0">
          {{record.rw}}<br/>
          {{record.rwPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'yw'">
        <div v-if="record.yw >= 0">
          {{record.yw}}<br/>
          {{record.ywPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
    </template>
  </a-table>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import StationSelectView from "@/components/station-select";
import dayjs from "dayjs";
import router from "@/router";

const visible = ref(false);
let dailyTrainTicket = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  start: undefined,
  startPinyin: undefined,
  startTime: undefined,
  startIndex: undefined,
  end: undefined,
  endPinyin: undefined,
  endTime: undefined,
  endIndex: undefined,
  ydz: undefined,
  ydzPrice: undefined,
  edz: undefined,
  edzPrice: undefined,
  rw: undefined,
  rwPrice: undefined,
  yw: undefined,
  ywPrice: undefined,
  createTime: undefined,
  updateTime: undefined,
});

const dailyTrainTickets = ref([]);

const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10,
});

let loading = ref(false);

const params = ref({
  date: '',
  start: '',
  end: ''
});

const columns = [
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '车站',
    dataIndex: 'station',
  },
  {
    title: '时间',
    dataIndex: 'time',
  },
  {
    title: '历时',
    dataIndex: 'duration',
  },
  {
    title: '一等座',
    dataIndex: 'ydz',
    key: 'ydz',
  },
  {
    title: '二等座',
    dataIndex: 'edz',
    key: 'edz',
  },
  {
    title: '软卧',
    dataIndex: 'rw',
    key: 'rw',
  },
  {
    title: '硬卧',
    dataIndex: 'yw',
    key: 'yw',
  },
  {
    title: '操作',
    dataIndex: 'operation',
  },
];

/**
 * 查询方法，提交到后端接口进行校验
 * Created By Zhangjilin 2024/12/6
 * @param param
 */
const handleQuery = (param) => {
  // 查询前，先校验三个参数是否为空
  if (Tool.isEmpty(params.value.date)) {
    notification.error({description: "请选择日期!"});
    return;
  }
  if (Tool.isEmpty(params.value.start)) {
    notification.error({description: "请选择出发地"});
    return;
  }
  if (Tool.isEmpty(params.value.end)) {
    notification.error({description: "请选择目的地"});
    return;
  }
  if (!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    };
  }

  // 保存查询参数
  SessionStorage.set(SESSION_TICKET_PARAMS, params.value);

  loading.value = true;
  axios.get("/business/daily-train-ticket/query-list", {
    params: {
      page: param.page,
      size: param.size,
      date: params.value.date,
      start: params.value.start,
      end: params.value.end
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      dailyTrainTickets.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 页数发生变化时调用
 * Created By Zhangjilin 2024/12/6
 * @param page
 */
const handleTableChange = (page) => {
  // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
  pagination.value.pageSize = page.pageSize;
  handleQuery({
    page: page.current,
    size: page.pageSize
  });
};

/**
 * 通过dayjs计算起始站到到达站的时间
 * Created By Zhangjilin 2024/12/6
 */
const calDuration = (startTime, endTime) => {
  let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'seconds');
  return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
};

/**
 * 点击预订按钮，跳转到订单
 * Created By Zhangjilin 2024/12/6
 */
const toOrder = (record) => {
  dailyTrainTicket.value = Tool.copy(record);
  SessionStorage.set(SESSION_ORDER, dailyTrainTicket.value);
  router.push("/order")
}

/**
 * 钩子函数，初始化页面的时候调用
 * Created By Zhangjilin 2024/12/6
 */
onMounted(() => {
  //  "|| {}"是常用技巧，可以避免空指针异常
  params.value = SessionStorage.get(SESSION_TICKET_PARAMS) || {};
  if (Tool.isNotEmpty(params.value)) {
    handleQuery({
      page: 1,
      size: pagination.value.pageSize
    });
  }
});

</script>

<style scoped>
.dailyTrainTicket {
  text-align: center;
  margin-top: 20px;
}
</style>