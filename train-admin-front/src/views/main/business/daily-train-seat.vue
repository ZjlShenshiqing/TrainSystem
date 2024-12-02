<template>
  <div class="dailyTrainSeat">
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainSeats"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <template v-else-if="column.dataIndex === 'col'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.col && item.type === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/train-select";

const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;

const visible = ref(false);

let dailyTrainSeat = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  carriageIndex: undefined,
  row: undefined,
  col: undefined,
  seatType: undefined,
  carriageSeatIndex: undefined,
  sell: undefined,
  createTime: undefined,
  updateTime: undefined,
});

const dailyTrainSeats = ref([]);

/**
 * 分页属性
 * Created By Zhangjilin 2024/12/2
 * @type {Ref<UnwrapRef<{total: number, current: number, pageSize: number}>, UnwrapRef<{total: number, current: number, pageSize: number}> | {total: number, current: number, pageSize: number}>}
 */
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10,
});

let loading = ref(false);

/**
 * 查询参数
 * Created By Zhangjilin 2024/12/2
 * @type {Ref<UnwrapRef<{trainCode: null}>, UnwrapRef<{trainCode: null}> | {trainCode: null}>}
 */
let params = ref({
  trainCode: null
});

const columns = [
  {
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '箱序',
    dataIndex: 'carriageIndex',
    key: 'carriageIndex',
  },
  {
    title: '排号',
    dataIndex: 'row',
    key: 'row',
  },
  {
    title: '列号',
    dataIndex: 'col',
    key: 'col',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '同车箱座序',
    dataIndex: 'carriageSeatIndex',
    key: 'carriageSeatIndex',
  },
  {
    title: '售卖情况',
    dataIndex: 'sell',
    key: 'sell',
  },
];

/**
 * 查询方法
 * Created By Zhangjilin 2024/12/2
 * @param param
 */
const handleQuery = (param) => {
  if (!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get("/business/admin/daily-train-seat/query-list", {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      dailyTrainSeats.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 页数发生变化的时候执行
 * Created By Zhangjilin 2024/12/2
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
 * 钩子函数，页面初始化的时候执行
 * Created By Zhangjilin 2024/12/2
 */
onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  });
});
</script>

<style scoped>
.dailyTrainSeat {
  text-align: center;
  margin-top: 20px;
}
</style>