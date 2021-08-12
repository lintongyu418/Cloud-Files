<template>
  <div id="app">
    <Header v-if="isHeaderShow" id="headWrapper"/>
    <router-view class="mainContent"/>
    <Footer v-if="isFooterShow"/>
    <global-uploader v-if="isFileAboutShow"/>
    <!-- 图片预览 -->
    <ImgPreview v-if="isFileAboutShow" />
    <VideoPreview v-if="isFileAboutShow" />
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import Footer from '@/components/Footer.vue'
  import globalUploader from '@/components/common/GlobalUploader.vue'
  import ImgPreview from '@/components/common/ImgPreview'
  import VideoPreview from '@/components/common/VideoPreview'

  export default {
    name: 'App',
    components: {
      Header,
      Footer,
      globalUploader,
      ImgPreview,
      VideoPreview,
    },
    computed: {
      //  头部是否显示
      isHeaderShow() {
        const routerNameList = ['Error_401', 'Error_404', 'Error_500']
        return !routerNameList.includes(this.$route.name)
      },
      //  底部是否显示
      isFooterShow() {
        const routerNameList = ['File', 'Share', 'myShare', 'Error_401', 'Error_404', 'Error_500']
        return !routerNameList.includes(this.$route.name)
      },
      // 网盘页面文件上传/预览相关组件是否显示
      isFileAboutShow() {
        const routerNameList = ['Login', 'Register']
        return !routerNameList.includes(this.$route.name)
      },
    },
  }
</script>
<style lang="stylus" scoped>
  @import '~@/assets/styles/varibles.styl';

  #app {
    height: 100%;
    overflow-x: hidden;
    -webkit-text-size-adjust: none;
    overflow-y: auto;

    >>> .back-top {
      background-color: $Success;
      color: #fff;
      z-index: 3;
    }

    .mainContent {
      flex: 1;
      width: 90%;
      min-height: calc(100vh - 70px - 80px);
      margin: 0 auto;
      display: flex;
    }
  }
</style>
