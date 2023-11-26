<template>
  <div class="footer">
    <div class="foot-logo">
      <p>数码商城</p>
    </div>
    <div class="foot-link">
      <a href="https://github.com/Wangccchen" target="_blank">github主页</a>
      <a href="https://github.com/Wangccchen" target="_blank"
        >计科一班 王琛 开发</a
      >
      <a href="https://github.com/Wangccchen" target="_blank"
        >网络应用开发大作业</a
      >
    </div>
    <div class="Copyright">
      Copyright ©2023
      <span>Wangccchen</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "nav-footer",
};
</script>

<style lang="scss">
.footer {
  height: 240px;
  background-color: #333333;
  color: #b0b0b0;
  position: absolute;
  border-top: 3px solid #409eff;
  width: 100%;
  text-align: center;
  font-size: 16px;
  .foot-logo {
    margin-top: 50px;

    img {
      width: 53px;
      height: 36px;
      margin-bottom: 20px;
    }
    p {
      margin-bottom: 40px;
    }
  }
  .foot-link {
    a {
      color: #b0b0b0;
      margin-right: 20px;
      display: inline-block;
      height: 16px;
      line-height: 16px;
      position: relative;
      &:after {
        content: "";
        position: absolute;
        top: 1px;
        margin-left: 10px;
        border-right: 1px solid #b0b0b0;
        height: 15px;
      }
      &:last-child:after {
        display: none;
      }
    }
    margin-bottom: 10px;
  }
  .Copyright {
    span {
      color: #b0b0b0;
    }
    a {
      color: #b0b0b0;
      font-size: 16px;
      margin-left: 5px;
    }
  }
}
</style>