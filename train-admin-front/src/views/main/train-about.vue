<script setup>
</script>

<template>
  <div class="about-container">
    <div class="about-header">
      <h1>关于我们</h1>
      <p>12306是中国铁路客户服务中心，为用户提供便捷的购票、查询和服务功能。</p>
    </div>
    <div class="about-content">
      <h2>我们的服务</h2>
      <ul>
        <li>快速购票：支持全国各地火车票的查询与购买。</li>
        <li>行程管理：方便用户查看和管理已订购车票的信息。</li>
        <li>便捷支付：支持多种支付方式，让购票更加轻松。</li>
        <li>智能客服：提供在线答疑服务，快速解决用户问题。</li>
      </ul>
      <h2>联系我们</h2>
      <p>
        如果您有任何问题，请通过以下方式联系我们：
      </p>
      <ul>
        <li>客服电话：12306</li>
        <li>官方邮箱：service@12306.cn</li>
        <li>官方网站：<a href="https://www.12306.cn" target="_blank">12306官方网站</a></li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.about-container {
  font-family: Arial, sans-serif;
  padding: 20px;
  color: #333;
}

.about-header {
  text-align: center;
  margin-bottom: 20px;
}

.about-header h1 {
  font-size: 32px;
  color: #1e87f0;
}

.about-header p {
  font-size: 18px;
  color: #555;
}

.about-content {
  margin-top: 20px;
}

.about-content h2 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #333;
}

.about-content ul {
  list-style-type: disc;
  margin-left: 20px;
  color: #555;
}

.about-content ul li {
  font-size: 16px;
  margin-bottom: 5px;
}

.about-content a {
  color: #1e87f0;
  text-decoration: none;
}

.about-content a:hover {
  text-decoration: underline;
}
</style>