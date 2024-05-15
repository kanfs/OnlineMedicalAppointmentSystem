<template>
  <div class="dashboard-container">
    <div>Welcome!</div>
    <img :src="ewizardClap" class="pan-img">
  </div>
</template>

<script>

export default {
  name: 'Dashboard',
  data() {
    return {
      ewizardClap: 'https://wpimg.wallstcn.com/007ef517-bafd-4066-aae4-6883632d9646',
      dialogVisible: true
    }
  },
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.pan-img {
  display: block;
  margin: 0 auto;
  width: 80%;
}
</style>
