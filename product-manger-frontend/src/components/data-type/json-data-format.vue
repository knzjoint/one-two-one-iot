<template>

  <a-col :span="12">
    <a-form-item label="名称" :rules="[{ required: true, message: '请输入名称!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.name"></a-input>
    </a-form-item>
  </a-col>

  <a-col :span="12">
    <a-form-item label="字符长度" :rules="[{ required: true, message: '请输入字符长度!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.maxLength" type="number"></a-input>
    </a-form-item>
  </a-col>
</template>

<script>
  export default {
    name: "string-data-format",
    props: {
      dataFormat: {
        type: Object,
        default() {
          return {
            maxLength: 1034,
          }
        },
      },
    },
    watch: {
      dataFormat(newValue, oldValue) {
        this.$emit('update:dataFormat', newValue)
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>

</style>
