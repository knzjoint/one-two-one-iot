<template>
  <div>
    <a-form :rules="rules" ref="dataForm" :model="tempItem" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
      <a-form-item label="名称" name="stepName">
        <a-input size="small" v-model:value="tempItem.stepName" style="width: 200px"></a-input>
      </a-form-item>

      <a-form-item label="值类型">
        <a-select v-model:value="tempItem.type" disabled>
          <a-select-option v-for="(item, index) in dataTypeSelect"
                           :value="item.value"
                           :key="index">
            {{ item.label }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <!--<a-form-item label="值控制描述">-->
        <!--<div>{{itemData.things_desc}}</div>-->
      <!--</a-form-item>-->


      <a-form-item label="值" name="value" v-if="tempItem.type === 'INT'">
        <a-input-number v-model:value="tempItem.value" :min="1" :max="100" />
      </a-form-item>

      <a-form-item label="值" name="value" v-else-if="tempItem.type === 'DECIMAL'">
        <a-input-number v-model:value="tempItem.value" :min="0" :max="100" :step="0.1" />
      </a-form-item>

      <a-form-item label="值" name="value" v-else-if="tempItem.type === 'STRING'">
        <a-input size="small" v-model:value="tempItem.value" style="width: 200px"></a-input>
      </a-form-item>

      <a-form-item label="值" name="value" v-else-if="tempItem.type === 'BOOL'">
        <a-switch checked-children="开" un-checked-children="关" v-model:checked="tempItem.value" />
      </a-form-item>


    </a-form>
  </div>
</template>

<script>

  import {clone} from './utils'

  const dataTypeSelect = [
    {
      value: 'INT',
      label: '整数',
    },
    {
      value: 'DECIMAL',
      label: '浮点',
    },
    {
      value: 'STRING',
      label: '字符',
    },
    {
      value: 'BOOL',
      label: '布尔',
    },
    {
      value: 'JSON',
      label: 'JSON',
    },
  ]

  function _getTempItem() {
    return {
      stepName: '新建',
    }
  }

  export default {
    name: "EndNodeForm",
    props: {
      editItem: {
        type: Object,
      },
      itemData: {
        type: Object,
      }
    },
    data() {
      return {
        tempItem: {
          stepName: '节点',
          type: 'INT',
          value: '100',
          conditionType: 'EMPTY',
        },
        rules: {
          stepName: [{required: true, trigger: 'blur', message: '请填写名称'}],
        },
        dataTypeSelect,
      }
    },
    created() {
      if (this.editItem && this.editItem.stepName) {
        this.tempItem = clone(this.editItem);
      }

      if (this.itemData && this.itemData.name) {
        this.tempItem.stepName = this.itemData.name
        this.tempItem.type = this.itemData.thingsType
      }
    },

    methods: {
      //
      validateFormData() {
        return new Promise((resolve, reject) => {

          // this.$refs['dataForm'].validate((valid) => {
          //     if (!valid) {
          //         reject();
          //         return;
          //     }
          //     resolve();
          // })
          resolve();
        });
      },

      formData() {
        let tempItem = clone(this.tempItem);
        return tempItem
      }
    }
  }
</script>

<style scoped>

</style>
