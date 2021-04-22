<template>
  <div class="file-wrapper">
    <el-container class="el-container">
      <el-aside width="auto">
        <AsideMenu/>
      </el-aside>
      <el-container>
        <el-main class="el-main">
          <FileList />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import AsideMenu from './left/AsideMenu'
  import FileList from "@/views/file/right/FileList";

  export default {
    name: 'File',
    components: {
      AsideMenu,
      FileList,
    },
  }
</script>

<style lang="stylus" scoped>
  @import '~@/assets/styles/varibles.styl';
  @import '~@/assets/styles/mixins.styl';

  .file-wrapper {
    width: 100% !important;
  }

  .el-container {
    .el-aside {
      height: calc(100vh - 61px);
      overflow: hidden;
    }
  }

  .el-main {
    padding: 0 16px !important;
    overflow: hidden;
  }
</style>
