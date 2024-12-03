<template class="dailyTrainCarriage">
  <div class="dailyTrainCarriage">
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainCarriages"
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
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="每日车箱" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrainCarriage" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainCarriage.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select-view v-model="dailyTrainCarriage.trainCode" width="200px"></train-select-view>
      </a-form-item>
      <a-form-item label="箱序">
        <a-input v-model:value="dailyTrainCarriage.index" />
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="dailyTrainCarriage.seatType">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <!--<a-form-item label="座位数">-->
      <!--  <a-input v-model:value="dailyTrainCarriage.seatCount" />-->
      <!--</a-form-item>-->
      <a-form-item label="排数">
        <a-input v-model:value="dailyTrainCarriage.rowCount" />
      </a-form-item>
      <!--<a-form-item label="列数">-->
      <!--  <a-input v-model:value="dailyTrainCarriage.colCount" />-->
      <!--</a-form-item>-->
    </a-form>
  </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/train-select";

const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
const visible = ref(false);

let dailyTrainCarriage = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  index: undefined,
  seatType: undefined,
  seatCount: undefined,
  rowCount: undefined,
  colCount: undefined,
  createTime: undefined,
  updateTime: undefined,
});

const dailyTrainCarriages = ref([]);

let loading = ref(false);

/**
 * 初始分页属性
 * Created By Zhangjilin 2024/12/2
 * @type {Ref<UnwrapRef<{total: number, current: number, pageSize: number}>, UnwrapRef<{total: number, current: number, pageSize: number}> | {total: number, current: number, pageSize: number}>}
 */
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10,
});

let params = ref({
  trainCode: null,
  date: null
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
    dataIndex: 'index',
    key: 'index',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '座位数',
    dataIndex: 'seatCount',
    key: 'seatCount',
  },
  {
    title: '排数',
    dataIndex: 'rowCount',
    key: 'rowCount',
  },
  {
    title: '列数',
    dataIndex: 'colCount',
    key: 'colCount',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
];

/**
 * 添加-模态框方法
 * Created By Zhangjilin 2024/12/2
 */
const onAdd = () => {
  dailyTrainCarriage.value = {};
  visible.value = true;
};

/**
 * 修改-模态框方法
 * Created By Zhangjilin 2024/12/2
 */
const onEdit = (record) => {
  dailyTrainCarriage.value = window.Tool.copy(record);
  visible.value = true;
};

/**
 * 删除-模态框方法
 * Created By Zhangjilin 2024/12/2
 */
const onDelete = (record) => {
  axios.delete("/business/admin/daily-train-carriage/delete/" + record.id).then((response) => {
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
 * 保存-模态框方法
 * Created By Zhangjilin 2024/12/2
 */
const handleOk = () => {
  axios.post("/business/admin/daily-train-carriage/save", dailyTrainCarriage.value).then((response) => {
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

/**
 * 查询-模态框方法
 * Created By Zhangjilin 2024/12/2
 */
const handleQuery = (param) => {
  if (!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get("/business/admin/daily-train-carriage/query-list", {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode,
      date: params.value.date
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      dailyTrainCarriages.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 页数发生变化时执行的代码
 * Created By Zhangjilin 2024/12/2
 */
const handleTableChange = (pagination) => {
  // console.log("看看自带的分页参数都有啥：" + pagination);
  handleQuery({
    page: pagination.current,
    size: pagination.pageSize
  });
};

/**
 * 钩子函数，初始化页面的时候调用
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
.dailyTrainCarriage {
  text-align: center;
  margin-top: 20px;
}
</style>