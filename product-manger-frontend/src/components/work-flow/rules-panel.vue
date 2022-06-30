<template>

  <a-list size="small" :bordered="false" :data-source="engineRules">
    <template #renderItem="{ item }">
      <a-list-item v-if="item.type === 'startNode'">
        如果
      </a-list-item>
      <a-list-item v-else-if="item.type === 'endNode'">
        执行: {{item.data.name}} 设置值为 {{item.condition.value}}
      </a-list-item>
      <a-list-item v-else-if="item.type === 'nodeNode'">
        &nbsp &nbsp {{item.data.name}} {{relationalOperatorSelect[item.condition.conditionType]}} {{item.condition.value}}
      </a-list-item>
    </template>
    <!--<template #header>-->
      <!--<div>Header</div>-->
    <!--</template>-->
  </a-list>
</template>

<script>
  // {{relationalOperatorSelect[item.condition.conditionType]}}
  // {{item.condition.value}}

  import { RecycleScroller } from 'vue-virtual-scroller'

  const relationalOperatorSelect = {
    EQ: '相等',
    NE: '不相等',
    LT: '小于',
    GT: '大于',
    LE: '小于或等于',
    GE: '大于或等于',
  }

  export default {
    name: "rulesPanel",
    components: {
      RecycleScroller,
    },
    props: {
      engineRules: {
        type: Array,
        default() {
          return []
        },
      },
    },
    data() {
      return {
        relationalOperatorSelect,
      }
    }
  }
</script>

<style rel="stylesheet/stylus" scoped>

  .ant-list-item {
    color: white;
  }

</style>
