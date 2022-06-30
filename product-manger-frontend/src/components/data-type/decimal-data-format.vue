<template>

  <a-col :span="12">
    <a-form-item label="名称" :rules="[{ required: true, message: '请输入名称!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.name"></a-input>
    </a-form-item>
  </a-col>

  <a-col :span="12">
    <a-form-item label="最小值" :rules="[{ required: true, message: '请输入最小值!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.min"></a-input>
    </a-form-item>
  </a-col>

  <a-col :span="12">
    <a-form-item label="最大值" :rules="[{ required: true, message: '请输入最大值!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.max"></a-input>
    </a-form-item>
  </a-col>

  <a-col :span="12">
    <a-form-item label="步长" :rules="[{ required: true, message: '请输入步长!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.step"></a-input>
    </a-form-item>
  </a-col>
</template>

<script>
  export default {
    name: "decimal-data-format",
    props: {
      dataFormat: {
        type: Object,
        default() {
          return {
            min: 0.0,
            max: 10000.0,
            step: 0.1,
          }
        },
      },
    },
    watch: {
      dataFormat(newValue, oldValue) {
        this.$emit('update:dataFormat', newValue)
      }
    },
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>

</style>
