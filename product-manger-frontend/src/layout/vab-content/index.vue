<template>
  <a-layout-content class="vab-content">
    <!--此处不能加key-->
    <router-view v-slot="{ Component }">
      <transition mode="out-in" name="fade-transform">
        <!--<keep-alive :include="cachedViews">-->
          <component :is="Component"/>
        <!--</keep-alive>-->
      </transition>
    </router-view>
  </a-layout-content>
</template>

<script>
export default {
  name: 'VabContent',
  computed: {
    cachedViews() {
      return this.$store.state.settings.cachedViews
    },
  },
  watch: {
    $route: {
      handler() {
        if ('mobile' === this.device) {
          this.$store.dispatch('settings/foldSideBar')
        }
      },
      immediate: true,
    },
  },
}
</script>

<style lang="less">
  .fade-transform-leave-active,
  .fade-transform-enter-active {
    transition: all 0.2s;
  }

  .fade-transform-enter {
    opacity: 0;
    transform: translateX(-30px);
  }

  .fade-transform-leave-to {
    opacity: 0;
    transform: translateX(30px);
  }

  .vab-content {
    min-height: calc(
        100vh - @vab-header-height - @vab-padding - @vab-padding - @vab-padding -
        @vab-padding
    ) !important;
    padding: @vab-padding;
    margin: @vab-margin;
    background: #fff;

  .error-container {
    height: calc(
        100vh - @vab-header-height - @vab-padding - @vab-padding - @vab-padding -
        @vab-padding - @vab-padding - @vab-margin
    ) !important;
  }

  }
</style>
