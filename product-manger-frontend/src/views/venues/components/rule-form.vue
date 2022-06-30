<template>

  <a-col :span="12">
    <a-form-item label="名称" :rules="[{ required: true, message: '请添加名称!', trigger: 'blur' },]">
      <a-input v-model:value="dataFormat.name"></a-input>
    </a-form-item>
  </a-col>

</template>

<script>
  export default {
    name: "rule-form",
    props: {
      ruleForm: {
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
        this.$emit('update:ruleForm', newValue)
      }
    },
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>

</style>
