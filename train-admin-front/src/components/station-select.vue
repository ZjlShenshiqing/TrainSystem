<template>
  <a-select v-model:value="name" show-search allowClear
            :filterOption="filterNameCodeOption"
            @change="onChange" placeholder="请选择车站"
            :style="'width: ' + localWidth">
    <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :label="item.name + item.namePinyin + item.namePy">
      {{item.name}} {{item.namePinyin}} ~ {{item.namePy}}
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
const name = ref(); // 当前选择的车次
const stations = ref([]); // 车次列表
const localWidth = ref(props.width); // 组件宽度

/**
 * 动态监听name,并传递给子组件
 * Created By Zhangjilin 2024/11/24
 */
watch(
    () => props.modelValue,
    (newValue) => {
      console.log('props.modelValue', newValue);
      name.value = newValue;
    },
    { immediate: true }
);

/**
 * 查询所有车站，用车站下拉框
 * Created By Zhangjilin 2024/11/24
 * @returns {Promise<void>}
 */
/**
 * 查询所有的车站，用于车站下拉框
 */
const queryAllName = () => {
  axios.get("/business/admin/station/query-all").then((response) => {
    let data = response.data;
    if (data.success) {
      stations.value = data.content;
    } else {
      notification.error({description: data.message});
    }
  });
};

/**
 * 下拉框筛选（搜索）
 * Created By Zhangjilin 2024/11/24
 * @param input
 * @param option
 * @returns {boolean}
 */
// 车次下拉框的筛选逻辑
const filterNameCodeOption = (input, option) => {
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
  const station = stations.value.find((item) => item.code === value) || {};
  emit('change', station); // 传递完整的车次信息给父组件
};

// 页面加载完成后查询车次
onMounted(queryAllName);
</script>