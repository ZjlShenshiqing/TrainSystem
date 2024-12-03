<template>
  <div class="welcome">
    <h1>火车座位管理</h1>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainSeats"
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
// onMonted: 这个方法是Vue生命周期的钩子函数
import { reactive, ref, onMounted } from 'vue';
import { notification } from 'ant-design-vue';
import axios from 'axios';
import TrainSelectView from "@/components/train-select.vue";

const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;

// 站点列表，初始化为空数组
const trainSeats = ref([]);

// 当前操作的站点，用于模态框
let trainSeat = ref({
  id: undefined,
  trainCode: undefined,
  carriageIndex: undefined,
  row: undefined,
  col: undefined,
  seatType: undefined,
  carriageSeatIndex: undefined,
  createTime: undefined,
  updateTime: undefined,
});

// 模态框的显示状态
const visible = ref(false);

// 新增状态：显示模态框
const onAdd = () => {
  // 新增时，将车次的所有值清空，这样新增的模态框不会显示任何的数据
  trainSeat.value = {};
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
  trainSeat.value = window.Tool.copy(record)
  visible.value = true;
};

// 分页的三个属性名是完全固定的
const pagination = ref({
  total: 0, // 列表的总数
  current: 1, // 当前的页码
  pageSize: 8 // 每页条数
})

let loading = ref(false);

// 筛选车次作为查询条件
let params = ref({
  trainCode: null
});

const columns = [
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '厢序',
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
    title: '同车厢座序',
    dataIndex: 'carriageSeatIndex',
    key: 'carriageSeatIndex',
  },
];

/**
 * 查询方法
 * Created By Zhangjilin 2024/11/24
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
  axios.get("/business/admin/trainSeat/query-list", {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      trainSeats.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 删除数据方法
 * Created By Zhangjilin 2024/11/24
 * @param record
 */
const onDelete = (record) => {
  axios.delete("/business/admin/trainSeat/delete/" + record.id).then((response) => {
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
 * 保存车次座位信息
 * Created By Zhangjilin 2024/11/24
 */
const handleOk = () => {
  axios.post("/business/admin/trainSeat/save", trainSeat.value).then((response) => {
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
 * Created By Zhangjilin 2024/11/24
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