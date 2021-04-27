<template>
  <div class="breadcrumb-wrapper">
    <div class="title">Current Position：</div>
    <el-breadcrumb v-if="fileType && !['Share', 'myShare'].includes($route.name)" separator="/">
      <el-breadcrumb-item>{{ fileTypeMap[fileType] }}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-breadcrumb v-else separator="/">
      <el-breadcrumb-item v-for="(item, index) in breadCrumbList" :key="index" :to="getRouteQuery(item)">{{
        item.name
      }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
export default {
  name: 'BreadCrumb',
  props: {
    // 文件类型
    fileType: {
      required: true,
      type: Number,
    },
  },
  data() {
    return {
      fileTypeMap: {
        1: 'All Pictures',
        2: 'All Documents',
        3: 'All Videos',
        4: 'All Musics',
        5: 'All Others',
        6: '回收站',
      },
    }
  },
  computed: {
    /**
     * 面包屑导航栏数组
     */
    breadCrumbList: {
      get() {
        const filePath = this.$route.query.filePath
        const filePathList = filePath ? filePath.split('/') : []
        const res = [] //  返回结果数组
        const _path = [] //  存放祖先路径
        for (let i = 0; i < filePathList.length; i++) {
          if (filePathList[i]) {
            _path.push(filePathList[i] + '/')
            res.push({
              path: _path.join(''),
              name: filePathList[i],
            })
          } else if (i === 0) {
            //  根目录
            filePathList[i] = '/'
            _path.push(filePathList[i])
            res.push({
              path: '/',
              name: this.$route.meta.breadCrumbName,
            })
          }
        }
        return res
      },
      set() {
        return []
      },
    },
  },
  methods: {
    // 获取文件参数
    getRouteQuery(item) {
      const routeName = this.$route.name
      if (routeName === 'Share') {
        // 当前是查看他人分享列表的页面
        return { query: { filePath: item.path } }
      } else if (routeName === 'myShare') {
        // 当前是我的已分享列表页面
        return {
          query: {
            filePath: item.path,
            shareBatchNum: item.path === '/' ? undefined : this.$route.query.shareBatchNum,  //  当查看的是根目录，批次号置空
          },
        }
      } else {
        // 网盘页面
        return { query: { filePath: item.path, fileType: 0 } }
      }
    },
  },
}
</script>

<style lang="stylus" scoped>
.breadcrumb-wrapper {
  padding: 0;
  height: 30px;
  line-height: 30px;
  display: flex;

  .title, >>> .el-breadcrumb {
    height: 30px;
    line-height: 30px;
  }
}
</style>
