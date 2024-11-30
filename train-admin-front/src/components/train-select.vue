<template>
  <a-select v-model:value="trainCode" show-search allowClear
            :filterOption="filterTrainCodeOption"
            @change="onChange" placeholder="请选择车次"
            :style="'width: ' + localWidth">
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code" :label="item.code + item.start + item.end">
      {{item.code}} {{item.start}} ~ {{item.end}}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import { notification } from 'ant-design-vue';

/**
 * 父组件 → 子组件：`props`
 * 子组件 → 父组件：`emit`
 */
const props = defineProps({
  modelValue: String, // 父组件传入的值
  width: {
    type: String,
    default: '100%',
  },
});

const emit = defineEmits(['update:modelValue', 'change']); // 定义组件的事件

/**
 * 响应式参数
 */
const trainCode = ref(); // 当前选择的车次
const trains = ref([]); // 车次列表
const localWidth = ref(props.width); // 组件宽度

/**
 * 动态监听trainCode,并传递给子组件
 * Created By Zhangjilin 2024/11/24
 */
watch(
    () => props.modelValue,
    (newValue) => {
      console.log('props.modelValue', newValue);
      trainCode.value = newValue;
    },
    { immediate: true }
);

/**
 * 查询所有车次，用于车次下拉框
 * Created By Zhangjilin 2024/11/24
 * @returns {Promise<void>}
 */
const queryAllTrain = async () => {
  const SESSION_ALL_TRAIN = 'SESSION_ALL_TRAIN'; // 会话存储的 key
  const cachedTrains = SessionStorage.get(SESSION_ALL_TRAIN);

  // 先读取缓存
  if (cachedTrains) {
    console.log('queryAllTrain 读取缓存');
    trains.value = cachedTrains;
    return;
  }

  // 查询所有车次
  try {
    const response = await axios.get('/business/admin/train/query-all');
    const data = response.data;
    if (data.success) {
      trains.value = data.content;
      console.log('queryAllTrain 保存缓存');
      SessionStorage.set(SESSION_ALL_TRAIN, trains.value);
    } else {
      notification.error({ description: data.message });
    }
  } catch (error) {
    console.error('查询车次失败:', error);
    notification.error({ description: '车次查询失败，请稍后再试！' });
  }
};

/**
 * 下拉框筛选（搜索）
 * Created By Zhangjilin 2024/11/24
 * @param input
 * @param option
 * @returns {boolean}
 */
// 车次下拉框的筛选逻辑
const filterTrainCodeOption = (input, option) => {
  console.log('筛选输入:', input, '选项:', option);
  return option.label.toLowerCase().includes(input.toLowerCase());
};

/**
 * 将当前组件的值响应给父组件
 * Created By Zhangjilin 2024/11/24
 * @param value
 */
const onChange = (value) => {
  emit('update:modelValue', value); // 通知父组件更新值
  const train = trains.value.find((item) => item.code === value) || {};
  emit('change', train); // 传递完整的车次信息给父组件
};

// 页面加载完成后查询车次
onMounted(queryAllTrain);
</script>

