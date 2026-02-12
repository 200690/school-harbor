<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h4>关于我们</h4>
          <p>校园港湾是专为在校大学生设计的综合服务平台，提供校园兼职和二手交易服务。</p>
        </div>
        <div class="footer-section">
          <h4>快速链接</h4>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/part-time">兼职</router-link></li>
            <li><router-link to="/second-hand">二手交易</router-link></li>
            <li><router-link to="/user/user/center">个人中心</router-link></li>
            <li><router-link to="/about">关于我们</router-link></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>联系我们</h4>
          <p>邮箱: zhanglonglong@createrna.com</p>
          <p>电话: 15737349990</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© {{ new Date().getFullYear() }} 校园港湾. 保留所有权利.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter'
}
</script>
