<template>

  <a-col :span="12">
    <a-form-item label="名称" :rules="[{ required: true, message: '请添加名称!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.name"></a-input>
    </a-form-item>
  </a-col>

  <a-col :span="12">
    <a-form-item label="打开名称" :rules="[{ required: true, message: '请输入打开名称!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.trueText"></a-input>
    </a-form-item>
  </a-col>

  <a-col :span="12">
    <a-form-item label="关闭名称" :rules="[{ required: true, message: '请输入关闭名称!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.falseText"></a-input>
    </a-form-item>
  </a-col>
</template>

<script>
  export default {
    name: "bool-data-format",
    props: {
      dataFormat: {
        type: Object,
        default() {
          return {
            trueText: '',
            falseText: '',
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
