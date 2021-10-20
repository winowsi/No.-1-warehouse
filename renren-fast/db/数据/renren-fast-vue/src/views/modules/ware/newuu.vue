<template>
  <div>
      <h1>NI</h1>
  </div>
</template>

<script>
export default {
  name: '',
  data() {
    return {};
  },
  computed: {},//计算属性类似data概念
  watch: {},//监控data中的数据变化
  methods: {},
  created() {},//生命周期创建完成(可以访问当前this实例)
  mounted() {},//生命周期挂在完成(可以访问Dom元素)
  beforeCreate() {},//生命周期-创建之前
  beforeUpdate() {},//生命周期-更新之前
  updated() {},//生命周期-更新之后
  beforeDestroy() {},//生命周期-销毁之前
  destroyed() {},//生命周期-销毁完成
  activated(){}//如果页面有keep-alive缓存功能这个函数会触发
};
</script>

<style scoped >

</style>