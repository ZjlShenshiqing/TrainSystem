<template>
  <div class="welcome">
    <h1>火车经停站管理</h1>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <!-- 增加loading可以防止用户不断的点击提交 -->
    <a-table :dataSource="trainStations"
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
    <a-modal v-model:visible="visible" title="火车车站" @ok="handleOk"
             ok-text="确认" cancel-text="取消">
      <a-form :model="trainStation" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
        <a-form-item label="车次编号">
          <train-select-view v-model="trainStation.trainCode"></train-select-view>
        </a-form-item>
        <a-form-item label="站序">
          <a-input v-model:value="trainStation.index" />
          <span style="color: red">重要：第1站是0，对显示销售图有影响</span>
        </a-form-item>
        <a-form-item label="站名">
          <station-select-view v-model="trainStation.name"></station-select-view>
        </a-form-item>
        <a-form-item label="站名拼音">
          <a-input v-model:value="trainStation.namePinyin" disabled/>
        </a-form-item>
        <a-form-item label="进站时间">
          <a-time-picker v-model:value="trainStation.inTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
        </a-form-item>
        <a-form-item label="出站时间">
          <a-time-picker v-model:value="trainStation.outTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
        </a-form-item>
        <a-form-item label="停站时长">
          <a-time-picker v-model:value="trainStation.stopTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
        </a-form-item>
        <a-form-item label="里程（公里）">
          <a-input v-model:value="trainStation.km" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
// onMonted: 这个方法是Vue生命周期的钩子函数
import { reactive, ref, onMounted, watch} from 'vue';
import { notification } from 'ant-design-vue';
import axios from 'axios';
import {pinyin} from "pinyin-pro";

// 站点列表，初始化为空数组
const trainStations = ref([]);

// 当前操作的站点，用于模态框
let trainStation = ref({
  id: undefined,
  trainCode: undefined,
  index: undefined,
  name: undefined,
  namePinyin: undefined,
  inTime: undefined,
  outTime: undefined,
  stopTime: undefined,
  km: undefined,
  createTime: undefined,
  updateTime: undefined,
});

// 模态框的显示状态
const visible = ref(false);

// 新增状态：显示模态框
const onAdd = () => {
  // 新增时，将车次的所有值清空，这样新增的模态框不会显示任何的数据
  trainStation.value = {};
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
  trainStation.value = window.Tool.copy(record)
  visible.value = true;
};

// 分页的三个属性名是完全固定的
const pagination = ref({
  total: 0, // 列表的总数
  current: 1, // 当前的页码
  pageSize: 8 // 每页条数
})

let loading = ref(false);

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
    title: '站序',
    dataIndex: 'index',
    key: 'index',
  },
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
    title: '进站时间',
    dataIndex: 'inTime',
    key: 'inTime',
  },
  {
    title: '出站时间',
    dataIndex: 'outTime',
    key: 'outTime',
  },
  {
    title: '停站时长',
    dataIndex: 'stopTime',
    key: 'stopTime',
  },
  {
    title: '里程（公里）',
    dataIndex: 'km',
    key: 'km',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
];

/**
 * 查询方法
 * Created By Zhangjilin 2024/11/23
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
  axios.get("/business/admin/trainStation/query-list", {
    params: {
      page: param.page,
      size: param.size
    }
  }).then((response) => {
    loading.value = false;
    let data = response.data;
    if (data.success) {
      trainStations.value = data.content.list;
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
 * Created By Zhangjilin 2024/11/23
 * @param record
 */
const onDelete = (record) => {
  axios.delete("/business/admin/trainStation/delete/" + record.id).then((response) => {
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
 * 保存车次经停站信息
 * Created By Zhangjilin 2024/11/23
 */
const handleOk = () => {
  axios.post("/business/admin/trainStation/save", trainStation.value).then((response) => {
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
 * 当用户输入或修改站名名称时，自动生成拼音（全拼）和拼音首字母
 * Created By Zhangjilin 2024/11/24
 */
watch(() => trainStation.value.name, ()=>{
  if (Tool.isNotEmpty(trainStation.value.name)) {
    trainStation.value.namePinyin = pinyin(trainStation.value.name, { toneType: 'none'}).replaceAll(" ", "");
  } else {
    trainStation.value.namePinyin = "";
  }
}, {immediate: true});

/**
 * 钩子函数，初始化页面时调用
 * Created By Zhangjilin 2024/11/23
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