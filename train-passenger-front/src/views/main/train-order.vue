<template>
  <div class="order-train">
    <span class="order-train-main">{{dailyTrainTicket.date}}</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.trainCode}}</span>次 &nbsp;
    <span class="order-train-main">{{dailyTrainTicket.start}}</span>站
    <span class="order-train-main">({{dailyTrainTicket.startTime}})</span>&nbsp;
    <span class="order-train-main">——</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.end}}</span> 站
    <span class="order-train-main">({{dailyTrainTicket.endTime}})</span>&nbsp;
  </div>
  <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{item.desc}}</span>：
        <span class="order-train-ticket-main">{{item.price}}￥</span>&nbsp;
        <span class="order-train-ticket-main">{{item.count}}</span>&nbsp;张票&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </span>
  </div>
  <a-divider></a-divider>
  {{ passengers }}
</template>

<script setup>
  import { ref } from "vue";

  const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
  console.log("下单的车次信息", dailyTrainTicket);

  const passengers = ref([]);

  const SEAT_TYPE = window.SEAT_TYPE;
  // 本车次提供的座位类型seatTypes，含票价，余票等信息，例：
  // {
  //   type: "YDZ",
  //   code: "1",
  //   desc: "一等座",
  //   count: "100",
  //   price: "50",
  // }
  // 关于SEAT_TYPE[KEY]：当知道某个具体的属性xxx时，可以用obj.xxx，当属性名是个变量时，可以使用obj[xxx]
  /**
   * Created By Zhangjilin 2024/12/7
   * 构建座位类型对象
   * 作用是根据 SEAT_TYPE 和 dailyTrainTicket 动态生成一个 seatTypes 数组，其中包含符合条件的座位类型及其信息。
   */
  const seatTypes = [];
  for (let KEY in SEAT_TYPE) {
    let key = KEY.toLowerCase();
    if (dailyTrainTicket[key] >= 0) {
      seatTypes.push({
        type: KEY,
        code: SEAT_TYPE[KEY]["code"],
        desc: SEAT_TYPE[KEY]["desc"],
        count: dailyTrainTicket[key],
        price: dailyTrainTicket[key + 'Price'],
      })
    }
  }

  /**
   * Created By Zhangjilin 2024/12/7
   * 查询所有乘客的方法
   */
  const handleQueryPassenger = () => {
    axios.get("/member/passenger/query-mine").then((response) => {
      let data = response.data;
      if (data.success) {
        passengers.value = data.content;
        passengers.value.forEach((item) => passengerOptions.value.push({
          label: item.name,
          value: item
        }))
      } else {
        notification.error({description: data.message});
      }
    });
  };

  /**
   * Created By Zhangjilin 2024/12/7
   * 钩子函数，初始化这个页面的时候就去查一下
   */

</script>

<style scoped>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}
.order-train .order-train-ticket {
  margin-top: 15px;
}
.order-train .order-train-ticket .order-train-ticket-main {
  color: red;
  font-size: 18px;
}

.order-tickets {
  margin: 10px 0;
}
.order-tickets .ant-col {
  padding: 5px 10px;
}
.order-tickets .order-tickets-header {
  background-color: cornflowerblue;
  border: solid 1px cornflowerblue;
  color: white;
  font-size: 16px;
  padding: 5px 0;
}
.order-tickets .order-tickets-row {
  border: solid 1px cornflowerblue;
  border-top: none;
  vertical-align: middle;
  line-height: 30px;
}

.order-tickets .choose-seat-item {
  margin: 5px 5px;
}
</style>