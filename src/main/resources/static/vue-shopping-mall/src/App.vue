<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
// import ProductView from "./views/ProductView.vue";
import HomePage from "./views/HomePage.vue";
export default {
  components: { HomePage },
  data() {
    return {};
  },
};
</script>

<style>
</style>
