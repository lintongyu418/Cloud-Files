<template>
  <div class="side-menu-wrapper">
    <!-- collapse 属性：控制菜单收缩展开 -->
    <el-menu
      :default-active="activeIndex"
      :router="true"
      :collapse="isCollapse"
      class="side-menu"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
    >
      <el-menu-item :route="{ name: 'File', query: { fileType: 0, filePath: '/' } }" index="0">
        <i class="el-icon-menu"/>
        <span slot="title">All Files</span>
      </el-menu-item>
      <el-menu-item :route="{ name: 'File', query: { fileType: 1 } }" index="1">
        <i class="el-icon-picture"/>
        <span slot="title">Pictures</span>
      </el-menu-item>
      <el-menu-item :route="{ name: 'File', query: { fileType: 2 } }" index="2">
        <i class="el-icon-document"/>
        <span slot="title">Documents</span>
      </el-menu-item>
      <el-menu-item :route="{ name: 'File', query: { fileType: 3 } }" index="3">
        <i class="el-icon-video-camera-solid"/>
        <span slot="title">Videos</span>
      </el-menu-item>
      <el-menu-item :route="{ name: 'File', query: { fileType: 4 } }" index="4">
        <i class="el-icon-headset"/>
        <span slot="title">Music</span>
      </el-menu-item>
      <el-menu-item :route="{ name: 'File', query: { fileType: 5 } }" index="5">
        <i class="el-icon-takeaway-box"/>
        <span slot="title">Others</span>
      </el-menu-item>
    </el-menu>
    <!-- 存储信息显示 -->
    <div :class="{ fold: isCollapse }" class="storage-wrapper">
      <el-progress
        :percentage="storagePercentage"
        :color="storageColor"
        :show-text="false"
        :type="isCollapse ? 'circle' : 'line'"
        :width="32"
        :stroke-width="isCollapse ? 4 : 6"
        stroke-linecap="square"
      />
      <div v-show="!isCollapse" class="text">
        <span>Space</span>
        <span>{{ storageValue | storageTrans }} / {{ storageMaxValue | storageTrans(true) }}</span>
      </div>
      <div v-show="isCollapse" class="text">
        <span>{{ storageValue | storageTrans }}</span>
      </div>
    </div>
    <!-- 展开 & 收缩分类栏 -->
    <el-tooltip :content="isCollapse ? 'expand' : 'chose'" effect="dark" placement="right">
      <div class="aside-title" @click="isCollapse ? (isCollapse = false) : (isCollapse = true)">
        <div class="top"/>
        <i v-if="isCollapse" class="icon el-icon-d-arrow-right" title="Expand"/>
        <i v-else class="icon el-icon-d-arrow-left" title="Fold"/>
        <div class="bottom"/>
      </div>
    </el-tooltip>
  </div>
</template>

<script>
export default {
  name: 'SideMenu',
  data() {
    return {
      isCollapse: false, //  控制菜单收缩展开
      storageMaxValue: Math.pow(1024, 3) * 10, //  最大存储容量，1GB
      //  自定义进度条颜色，不同占比，进度条颜色不同
      storageColor: [
        { color: '#67C23A', percentage: 50 },
        { color: '#E6A23C', percentage: 80 },
        { color: '#F56C6C', percentage: 100 },
      ],
    }
  },
  computed: {
    // 当前激活菜单的 index
    activeIndex() {
      return String(this.$route.query.fileType) //  获取当前路由参数中包含的文件类型
    },
    // 存储容量
    storageValue() {
      return this.$store.state.sideMenu.storageValue
    },
    // 存储百分比
    storagePercentage() {
      return (this.storageValue / this.storageMaxValue) * 100
    },
  },
  watch: {
    // 监听收缩状态变化，存储在sessionStorage中，保证页面刷新时仍然保存设置的状态
    isCollapse(newValue) {
      this.setCookies('isCollapse', newValue)
    },
  },
  created() {
    this.isCollapse = this.getCookies('isCollapse') === 'true' //  读取保存的状态
  },
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl';
@import '~@/assets/styles/mixins.styl';

.side-menu-wrapper {
  position: relative;
  height: calc(100vh - 61px);
  padding-right: 11px;

  .side-menu {
    // 高度设置为屏幕高度减去顶部导航栏的高度
    height: calc(100vh - 127px);
    overflow: auto;
    // 调整滚动条样式
    setScrollbar(6px, #909399, #EBEEF5);
  }

  // 对展开状态下的菜单设置宽度
  .side-menu:not(.el-menu--collapse) {
    width: 200px;
  }

  // 存储空间展示区
  .storage-wrapper {
    position: absolute;
    bottom: 0;
    left: 0;
    box-sizing: border-box;
    background: #545c64;
    width: calc(100% - 12px);
    height: 66px;
    padding: 16px;
    z-index: 2;
    color: #fff;

    .text {
      margin-top: 8px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 14px;
    }
  }

  .storage-wrapper.fold {
    padding: 0;

    >>> .el-progress--circle {
      margin: 0 auto;
      width: 32px;
      display: block;
    }

    .text {
      font-size: 12px;
      justify-content: center;
    }
  }

  // 折叠图标调整样式
  .aside-title {
    position: absolute;
    top: calc(50% - 50px);
    right: 0;
    z-index: 3;
    background: #545c64;
    color: $BorderLight;
    width: 12px;
    height: 100px;
    line-height: 100px;
    cursor: pointer;

    &:hover {
      opacity: 0.7;
    }

    .icon {
      font-size: 12px;
    }

    .top {
      position: absolute;
      top: calc(50% - 50px);
      right: 0;
      width: 0;
      height: 0;
      border-bottom: 12px solid transparent;
      border-right: 12px solid #fff;
    }

    .bottom {
      position: absolute;
      top: calc(50% + 38px);
      right: 0px;
      width: 0;
      height: 0;
      border-top: 12px solid transparent;
      border-right: 12px solid #fff;
    }
  }
}
</style>
