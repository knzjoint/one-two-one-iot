<template>
  <div class="index-container">
    <el-amap  :zoom="zoom" :center="center"></el-amap>
    <version-information />
  </div>
</template>

<script>
  import VersionInformation from './components/VersionInformation'

  export default {
    name: 'Index',
    components: { VersionInformation },
    data() {
      return {
        center: [121.329402, 31.228667],
        zoom: 17,
      }
    },
  }
</script>
