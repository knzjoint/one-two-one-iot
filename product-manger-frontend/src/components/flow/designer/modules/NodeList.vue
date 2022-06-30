<template>
  <a-list :grid="{ gutter: 0, column: 1 }">
    <a-list-item v-for="node in nodeList" :key="node.type" class="ef-node-menu-li">
      <a-tooltip :title="node.nodeName" placement="right">
        <div
          class="node-item"
          draggable="true"
          @dragstart="dragNode(node.type, type)"
        >

          <!--<li v-for="subMenu in menu.children" class="ef-node-menu-li" :key="subMenu.id" :type="subMenu.type">-->
            <!--<i :class="node.icon"></i> {{subMenu.name}}-->
          <!--</li>-->
          <i :class="node.icon"></i>
          <span>{{ node.nodeName }}</span>
          <!--<a-icon :type="node.icon" />-->
        </div>
      </a-tooltip>
    </a-list-item>
  </a-list>
</template>
<script>
export default {
  name: "NodeList",
  props: {
    nodeList: {
      type: Array,
      default: () => []
    },
    type: {
      type: String,
      default: ""
    }
  },
  methods: {
    // 开始拖拽
    dragNode(type, belongTo) {
      this.$emit("setDragInfo", {
        type,
        belongTo
      });
    }
  }
};
</script>
<style scoped lang="less">

  .node-item {
    color: #565758;
    width: 150px;
    border: 1px dashed #E0E3E7;
    margin: 5px 0 5px 0;
    padding: 5px;
    border-radius: 5px;
    padding-left: 8px;
  }

</style>
