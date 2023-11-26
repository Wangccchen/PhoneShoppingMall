<template>
  <div>
    <nav-header></nav-header>
    <router-view></router-view>
    <nav-footer></nav-footer>
  </div>
</template>

<script>
import NavHeader from "../components/NavHeader.vue";
import NavFooter from "./../components/NavFooter";
export default {
  name: "mall",
  components: {
    NavHeader,
    NavFooter,
  },
  data() {
    return {};
  },
};
</script>