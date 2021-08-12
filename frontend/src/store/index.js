import Vue from "vue";
import Vuex from "vuex";

import config from '@/config'

import user from './modules/user'  //  用户模块
import sideMenu from "@/store/modules/sideMenu";
import fileList from "@/views/file/right/FileList";
import imgPreview from "@/store/modules/imgPreview";
import videoPreview from "@/store/modules/videoPreview";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //
  },
  getters: {
    // 登录状态
    isLogin: (state) => state.user.isLogin,
    // 用户姓名
    username: (state) => state.user.userInfo.username,
    // 用户ID
    userId: (state) => state.user.userInfo.userId,
    // 表格显示列
    selectedColumnList: (state) =>
      state.fileList.selectedColumnList === undefined
        ? config.allColumnList
        : state.fileList.selectedColumnList.split(","),
    // 文件查看模式
    fileModel: (state) => {
      return state.fileList.fileModel === undefined ? 0 : Number(state.fileList.fileModel)
    },
    // 网格模式 & 时间线模式下 文件图标大小
    gridSize: (state) => state.fileList.gridSize,
  },
  mutations: {
    //
  },
  actions: {
    //
  },
  modules: {
    user,
    sideMenu,
    fileList,
    imgPreview,
    videoPreview,
  },
})
