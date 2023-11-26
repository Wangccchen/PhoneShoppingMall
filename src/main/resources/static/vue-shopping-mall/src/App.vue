<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
// import ProductView from "./views/ProductView.vue";
import HomePage from "./views/BackHomePage.vue";
export default {
  components: { HomePage },
  data() {
    return {};
  },
};
</script>

<style>
</style>
