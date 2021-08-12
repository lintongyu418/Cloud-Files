<template>
  <!-- 文件平铺 -->
  <div class="file-grid-wrapper">
    <ul
      v-loading="loading"
      class="file-list"
      element-loading-text="loading files ..."
      @click.self="rightMenu.isShow = false"
      @scroll="rightMenu.isShow = false"
    >
      <li
        v-for="(item, index) in fileListSorted"
        :key="index"
        :title="item | fileNameComplete"
        :style="`width: ${gridSize + 40}px; `"
        class="file-item"
        @click="handleFileNameClick(item, index, fileListSorted)"
        @contextmenu.prevent="rightClickFile(item, index, $event)"
      >
        <img :src="setFileImg(item)" :style="`width: ${gridSize}px; height: ${gridSize}px;`" class="file-img" >
        <div class="file-name">{{ item | fileNameComplete }}</div>
        <div
          v-show="batchOperate"
          :class="{ checked: item.checked }"
          class="file-checked-wrapper"
          @click.stop.self="item.checked = !item.checked"
        >
          <el-checkbox
            v-model="item.checked"
            class="file-checked"
            @click.stop="item.checked = !item.checked"
          />
        </div>
      </li>
    </ul>
    <transition name="el-fade-in-linear">
      <div v-show="rightMenu.isShow" :style="`top: ${rightMenu.top}px; left: ${rightMenu.left}px;`" class="right-menu">
        <el-button
          type="info"
          size="small"
          plain
          @click.native="handleDeleteFileBtnClick(selectedFile)"
        >Delete</el-button
        >
        <el-button
          v-if="fileType !== 6"
          type="info"
          size="small"
          plain
          @click.native="handleMoveFileBtnClick(selectedFile)"
        >Move</el-button
        >
        <el-button
          v-if="fileType !== 6"
          type="info"
          size="small"
          plain
          @click.native="handleRenameFileBtnClick(selectedFile)"
        >Rename</el-button
        >
        <el-button
          v-if="fileType !== 6"
          type="info"
          size="small"
          plain
          @click.native="rightMenu.isShow = false"
        >
          <a
            :href="getDownloadFilePath(selectedFile)"
            :download="selectedFile.fileName + '.' + selectedFile.extendName"
            target="_blank"
            style="display: block; color: inherit"
          >Download</a
          >
        </el-button>
        <el-button
          v-if="fileType !== 6 && (selectedFile.extendName == 'zip' || selectedFile.extendName == 'rar')"
          type="info"
          size="small"
          plain
          @click.native="handleUnzipFileBtnClick(selectedFile)"
        >UnZip</el-button
        >
      </div>
    </transition>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import 'element-ui/lib/theme-chalk/base.css'

export default {
  name: 'FileGrid',
  props: {
    // 文件类型
    fileType: {
      required: true,
      type: Number,
    },
    // 文件路径
    filePath: {
      required: true,
      type: String,
    },
    fileList:{
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: () => false,
    },
    batchOperate: {
      type: Boolean,
      default: () => false,
    },
  },
  data() {
    return {
      fileListSorted: [],
      //  可以识别的文件类型
      fileImgTypeList: [
        'png',
        'jpg',
        'jpeg',
        'docx',
        'doc',
        'ppt',
        'pptx',
        'xls',
        'xlsx',
        'avi',
        'mp4',
        'css',
        'csv',
        'chm',
        'rar',
        'zip',
        'dmg',
        'mp3',
        'open',
        'pdf',
        'rtf',
        'txt',
        'oa',
        'js',
        'html',
        'img',
        'sql',
        'jar',
        'svg',
        'gif',
        'json',
        'exe',
      ],
      //  文件图片Map映射
      fileImgMap: {
        dir: require('@/assets/images/file/dir.png'),
        chm: require('@/assets/images/file/file_chm.png'),
        css: require('@/assets/images/file/file_css.png'),
        csv: require('@/assets/images/file/file_csv.png'),
        png: require('@/assets/images/file/file_pic.png'),
        jpg: require('@/assets/images/file/file_pic.png'),
        jpeg: require('@/assets/images/file/file_pic.png'),
        docx: require('@/assets/images/file/file_word.png'),
        doc: require('@/assets/images/file/file_word.png'),
        ppt: require('@/assets/images/file/file_ppt.png'),
        pptx: require('@/assets/images/file/file_ppt.png'),
        xls: require('@/assets/images/file/file_excel.png'),
        xlsx: require('@/assets/images/file/file_excel.png'),
        mp4: require('@/assets/images/file/file_video.png'),
        avi: require('@/assets/images/file/file_avi.png'),
        rar: require('@/assets/images/file/file_rar.png'),
        zip: require('@/assets/images/file/file_zip.png'),
        dmg: require('@/assets/images/file/file_dmg.png'),
        mp3: require('@/assets/images/file/file_music.png'),
        open: require('@/assets/images/file/file_open.png'),
        pdf: require('@/assets/images/file/file_pdf.png'),
        rtf: require('@/assets/images/file/file_rtf.png'),
        txt: require('@/assets/images/file/file_txt.png'),
        oa: require('@/assets/images/file/file_oa.png'),
        unknown: require('@/assets/images/file/file_unknown.png'),
        js: require('@/assets/images/file/file_js.png'),
        html: require('@/assets/images/file/file_html.png'),
        img: require('@/assets/images/file/file_img.png'),
        sql: require('@/assets/images/file/file_sql.png'),
        jar: require('@/assets/images/file/file_jar.png'),
        svg: require('@/assets/images/file/file_svg.png'),
        json: require('@/assets/images/file/file_json.png'),
        exe: require('@/assets/images/file/file_exe.png'),
      },
      downloadFilePath: '',
      viewFilePath: '',
      // 右键菜单
      rightMenu: {
        isShow: false,
        top: 0,
        left: 0,
      },
      selectedFile: {},
      // 音频预览
      audioObj: {
        src: '',
      },
    }
  },
  computed: {
    /**
     * selectedColumnList: 列显隐
     * fileModel: 文件查看模式 0列表模式 1网格模式
     *  */
    ...mapGetters(['selectedColumnList', 'fileModel']),
    //  判断当前路径下是否有普通文件
    isIncludeNormalFile() {
      return this.fileList.map((data) => data.isDir).includes(0)
    },
    //  判断当前路径下是否有压缩文件
    isIncludeZipRarFile() {
      return (
        this.fileList.map((data) => data.extendName).includes('zip') ||
        this.fileList.map((data) => data.extendName).includes('rar')
      )
    },
    // 批量操作模式 - 被选中的文件
    selectedFileList() {
      const res = this.fileListSorted.filter((item) => item.checked)
      return res
    },


    // 图标大小
    gridSize() {
      return 60
    },
  },
  watch: {
    // 文件平铺模式 排序-文件夹在前
    fileList(newValue) {
      this.fileListSorted = [...newValue]
        .sort((pre, next) => {
          return next.isDir - pre.isDir
        })
        .map((item) => {
          return {
            ...item,
            checked: false,
          }
        })
    },
    // 批量操作模式 - 被选中的文件
    selectedFileList(newValue) {
      this.$emit('setSelectionFile', newValue)
    },
  },
  methods: {
    /**
     * 文件鼠标右键事件
     * @param {object} item 文件信息
     * @param {number} index 文件索引
     * @param {object} mouseEvent 鼠标事件信息
     */
    rightClickFile(item, index, mouseEvent) {
      if (!this.batchOperate) {
        this.rightMenu.isShow = false
        setTimeout(() => {
          this.selectedFile = item
          this.rightMenu.top = mouseEvent.clientY - 64
          this.rightMenu.left = mouseEvent.clientX + 18
          this.rightMenu.isShow = true
        }, 100)
      }
    },
    /**
     * 根据文件扩展名设置文件图片
     * @param {object} row 文件信息
     */
    setFileImg(row) {
      if (!row.extendName) {
        //  文件夹
        return this.fileImgMap.dir
      } else if (!this.fileImgTypeList.includes(row.extendName)) {
        //  无法识别文件类型的文件
        return this.fileImgMap.unknown
      } else if (this.fileType !== 6 && ['jpg', 'png', 'jpeg', 'gif'].includes(row.extendName)) {
        // 图片类型，直接显示缩略图
        return this.getImgMinPath(row)
      } else {
        //  可以识别文件类型的文件
        return this.fileImgMap[row.extendName]
      }
    },

    /**
     * 文件名点击事件
     * @description 若当前点击的为文件夹，则进入文件夹内部；若是文件，则进行相应的预览。
     * @param {object} row 文件信息
     * @param {number} activeIndex 文件索引
     * @param {[]} fileList 文件列表
     */
    handleFileNameClick(row, activeIndex, fileList) {
      this.rightMenu.isShow = false
      //  若是目录则进入目录
      if (row.isDir) {
        this.$router.push({
          query: {
            filePath: row.filePath + row.fileName + '/',
            fileType: 0,
          },
        })
      } else {
        //  若是文件，则进行相应的预览
        //  若当前点击项是图片
        const PIC = ['png', 'jpg', 'jpeg', 'gif', 'svg']
        if (PIC.includes(row.extendName)) {
          if (this.fileType === 1) {
            //  图片分类下 - 大图查看
            const data = {
              imgPreviewVisible: true,
              imgPreviewList: fileList.map((item) => {
                return {
                  fileUrl: this.getViewFilePath(item),
                  downloadLink: this.getDownloadFilePath(item),
                  fileName: item.fileName,
                  extendName: item.extendName,
                }
              }),
              activeIndex: activeIndex,
            }
            this.$store.commit('setImgPreviewData', data)
          } else {
            //  非图片分类下 - 大图查看
            const data = {
              imgPreviewVisible: true,
              imgPreviewList: [
                {
                  fileUrl: this.getViewFilePath(row),
                  downloadLink: this.getDownloadFilePath(row),
                  fileName: row.fileName,
                  extendName: row.extendName,
                },
              ],
              activeIndex: 0,
            }
            this.$store.commit('setImgPreviewData', data)
          }
        }
        //  若当前点击项是可以使用office在线预览的
        if (['ppt', 'pptx', 'doc', 'docx', 'xls', 'xlsx'].includes(row.extendName)) {
          window.open(this.getFileOnlineViewPathByOffice(row), '_blank')
        }
        //  若当前点击项是代码或文本文件
        const CODE = ['html', 'js', 'css', 'json', 'c', 'java', 'txt', 'pdf']
        if (CODE.includes(row.extendName)) {
          window.open(this.getViewFilePath(row), '_blank')
        }
        //  若当前点击项是视频mp4格式
        const VIDEO = ['mp4']
        if (VIDEO.includes(row.extendName)) {
          if (this.fileType === 3) {
            // 视频分类下 加载播放列表
            const data = {
              videoPreviewVisible: true,
              videoPreviewList: fileList.map((item) => {
                return {
                  ...item,
                  fileUrl: this.getViewFilePath(item),
                  downloadLink: this.getDownloadFilePath(item),
                  fileName: item.fileName,
                  extendName: item.extendName,
                }
              }),
              activeIndex: activeIndex,
            }
            this.$store.commit('setVideoPreviewData', data)
          } else {
            // 非视频分类下 - 单个视频预览
            const data = {
              videoPreviewVisible: true,
              videoPreviewList: [
                {
                  ...row,
                  fileUrl: this.getViewFilePath(row),
                  downloadLink: this.getDownloadFilePath(row),
                  fileName: row.fileName,
                  extendName: row.extendName,
                },
              ],
              activeIndex: 0,
            }
            this.$store.commit('setVideoPreviewData', data)
          }
        }
        //  若当前点击项是音频mp3格式
        const AUDIO = ['mp3']
        if (AUDIO.includes(row.extendName)) {
          if (this.audioObj.src !== this.getViewFilePath(row)) {
            this.$notify.closeAll()
            this.audioObj.src = this.getViewFilePath(row)
            this.$notify({
              title: `${row.fileName}.${row.extendName}`,
              dangerouslyUseHTMLString: true,
              message: `<audio class="audio-preview" src="${
                this.audioObj.src
              }" controls autoplay style="padding-right: 16px; margin-top: 16px;"></audio>`,
              duration: 0, //  不自动关闭
              offset: 100,
              onClose: () => {
                this.audioObj.src = ''
              },
            })
          }
        }
      }
    },
    /**
     * 移动按钮点击事件
     * @description 向父组件传递当前操作的文件信息，并打开“移动文件对话框”
     * @param {object} file 文件信息
     */
    handleMoveFileBtnClick(file) {
      this.rightMenu.isShow = false
      this.$emit('setOperationFile', file)
      //  第一个参数: 是否批量移动；第二个参数：打开/关闭移动文件模态框
      this.$emit('setMoveFileDialogData', false, true)
    },
    /**
     * 解压缩按钮点击事件
     * @description 调用解压缩文件接口，并展示新的文件列表
     * @param {object} fileInfo 文件信息
     */
    handleUnzipFileBtnClick(fileInfo) {
    },
    /**
     * 删除按钮点击事件
     * @description 区分 删除到回收站中 | 在回收站中彻底删除，打开确认对话框
     * @param {object} fileInfo 文件信息
     */
    handleDeleteFileBtnClick(fileInfo) {
      this.rightMenu.isShow = false
      if (this.fileType === 6) {
        //  回收站里 - 彻底删除
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.confirmDeleteFile(fileInfo, true)
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      } else {
        //  非回收站
        this.$confirm('删除后可在回收站查看, 是否继续删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.confirmDeleteFile(fileInfo, false)
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      }
    },
    /**
     * 删除文件确认对话框 | 确定按钮点击事件
     * @description 区分 删除到回收站中 | 在回收站中彻底删除，调用相应的删除文件接口
     * @param {object} fileInfo 文件信息
     * @param {boolean} type 文件类型，true 在回收站中彻底删除 false 删除到回收站
     */
    confirmDeleteFile(fileInfo, type) {
    },
    /**
     * 文件重命名按钮点击事件
     * @description 打开确认对话框让用户输入新的文件名
     * @param {object} fileInfo 文件信息
     */
    handleRenameFileBtnClick(fileInfo) {
      this.rightMenu.isShow = false
      var fileName = fileInfo.fileName
      this.$prompt('Please enter a new name', 'Hint', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        inputValue: fileName,
        inputPattern: /\S/, //  文件名不能为空
        inputErrorMessage: 'new name',
        closeOnClickModal: false,
      })
        .then(({ value }) => {
          fileInfo.oldFileName = fileInfo.fileName
          fileInfo.fileName = value
          this.confirmRenameFile(fileInfo)
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: 'Cancel',
          })
        })
    },
    /**
     * 文件重命名对话框 | 确定按钮点击事件
     * @description 调用文件重命名接口
     * @param {object} fileInfo 文件信息
     */
    confirmRenameFile(fileInfo) {
    },
  },
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl';
@import '~@/assets/styles/mixins.styl';

.file-grid-wrapper {
  border-top: 1px solid $BorderBase;

  .file-list {
    height: calc(100vh - 206px);
    overflow-y: auto;
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    align-content: flex-start;
    setScrollbar(10px);
    list-style none;
    margin 0;
    padding 0;

    .file-item {
      margin: 0 16px 16px 0;
      position: relative;
      padding: 8px;
      text-align: center;
      cursor: pointer;

      &:hover {
        background: $tabBackColor;

        .file-name {
          font-weight: 550;
        }
      }

      .file-name {
        margin-top: 8px;
        height: 44px;
        line-height: 22px;
        font-size: 12px;
        word-break: break-all;
        setEllipsis(2);
      }

      .file-checked-wrapper {
        position: absolute;
        top: 0;
        left: 0;
        z-index: 2;
        background: rgba(245, 247, 250, 0.5);
        width: 100%;
        height: 100%;

        .file-checked {
          position: absolute;
          top: 16px;
          left: 24px;
        }
      }

      .file-checked-wrapper.checked {
        background: rgba(245, 247, 250, 0);
      }
    }
  }

  .right-menu {
    position: fixed;
    display: flex;
    flex-direction: column;

    >>> .el-button {
      margin: 0;
      border-radius: 0;
    }
  }
}
</style>
