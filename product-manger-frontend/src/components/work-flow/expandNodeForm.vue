<template>
  <div>
    <a-form :rules="rules" ref="dataForm" :model="tempItem" label-width="80px">
      <a-form-item label="步骤名称" name="stepName">
        <a-input size="small" v-model:value="tempItem.stepName" style="width: 200px"></a-input>
      </a-form-item>
      <a-form-item label="分支" name="stepName">
        <template v-for="(ruleItem,index) in tempItem.ruleGroupList">
          <div class="expand-item">
            <span class="expand-title">分支{{index+1}} </span>
            <span class="expand-content">
              <a-input size="small" v-model:value="ruleItem.name" placeholder="placeholder"></a-input>
            </span>
            <vab-icon icon="delete-bin-6-line" @click="handleRemoveRuleGroup(index)"></vab-icon>
            <!--<span title="删除分支" class="el-icon-btn el-icon-remove"-->
                  <!--@click="handleRemoveRuleGroup(index)"></span>-->
          </div>
        </template>
        <div title="创建分支">
          <vab-icon @click="handleCreateRuleGroup" icon="add-fill"></vab-icon>
        </div>
        <!--<div title="创建分支" class="el-icon-btn rule-content-tips el-icon-circle-plus"-->
             <!--@click="handleCreateRuleGroup"></div>-->
      </a-form-item>
    </a-form>

  </div>
</template>

<script>
  import {clone} from "./utils";
  import VabIcon from '@/layout/vab-icon'

  function getTempItem() {
    return {
      stepName: '条件分组',
      ruleGroupList: [{
        name: ''
      }]
    };
  }

  export default {
    name: "ExpandNodeForm",
    components: {
      VabIcon,
    },
    props: {
      editItem: {
        type: Object,
      }
    },
    data() {
      return {
        tempItem: getTempItem(),
        rules: {
          stepName: [{required: true, trigger: 'blur', message: '请填写名称'}],
        }
      };
    },

    created() {
      if (this.editItem && this.editItem.stepName) {
        let tempItem = clone(this.editItem);
        this.updateData(tempItem);
      }
    },

    methods: {

      updateData(tempItem) {
        this.tempItem = tempItem;
      },

      validateFormData() {
        return new Promise((resolve, reject) => {
          // this.$refs['dataForm'].validate((valid) => {
          //     if (!valid) {
          //         reject();
          //         return;
          //     }
          //     resolve();
          // });
          resolve();
        });
      },

      formData() {
        let tempItem = clone(this.tempItem);
        return tempItem;
      },

      handleCreateRuleGroup() {
        this.tempItem.ruleGroupList.push({
          name: ''
        });
      },

      handleRemoveRuleGroup(index) {
        this.tempItem.ruleGroupList.splice(index, 1);

        if (this.tempItem.ruleGroupList.length === 0) {
          this.handleCreateRuleGroup();
        }
      },

      _message(msg, type) {
        this.$message({
          type: type || 'error',
          message: msg
        });
      },
    }

  };
</script>

<style lang="scss" scoped>


  .expand-item {
    border: 1px solid #e7e7e7;
    border-radius: 4px;
    position: relative;
    margin-bottom: 5px;

    span {
      display: inline-block;
    }

    .expand-title {
      position: absolute;
      font-size: 12px;
      display: flex;
      align-items: center;
      justify-items: center;
      width: 33px;
      height: 100%;
      color: #fff;
      background: #87cefa;
      border: 1px solid #87cefa;
    }

    .expand-content {
      margin-left: 38px;
      max-width: 340px;
      vertical-align: middle;
      line-height: 22px;
    }
  }
</style>
