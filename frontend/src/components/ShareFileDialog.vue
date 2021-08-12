<template>
  <div>
    <!-- 分享对话框 -->
    <el-dialog
      :visible.sync="dialogShareFile.visible"
      :close-on-click-modal="false"
      title="Share File"
      width="550px"
      @close="handleShareFileDialogCancel('shareFileForm')"
    >
      <el-form
        v-show="!dialogShareFile.success"
        ref="shareFileForm"
        :model="form"
        :rules="rules"
        class="share-file-form"
        label-suffix="："
        label-width="130px"
      >
        <el-form-item label="Valid until" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            :editable="false"
            :clearable="false"
            :picker-options="pickerOptions"
            type="datetime"
            placeholder="Choose date"
            align="right"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="Verification code?" prop="shareType">
          <el-radio-group v-model="form.shareType">
            <el-radio :label="1">Yes</el-radio>
            <el-radio :label="0">No</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <el-form
        v-if="dialogShareFile.success"
        ref="shareSuccessForm"
        :model="dialogShareFile.shareData"
        class="share-success-form"
        label-suffix="："
        label-width="90px"
      >
        <div class="success-tip">
          <i class="el-icon-success"/>
          <span class="text">Successfully created a share link!</span>
        </div>
        <el-form-item label="share link" prop="shareBatchNum">
          <el-input
            :value="getShareLink(dialogShareFile.shareData.shareBatchNum)"
            :readonly="true"
            type="textarea"
            autosize
          />
        </el-form-item>
        <el-form-item v-if="dialogShareFile.shareData.extractionCode" label="提取码" prop="extractionCode">
          <el-input v-model="dialogShareFile.shareData.extractionCode" :readonly="true"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          v-if="dialogShareFile.success"
          type="primary"
          @click="copyShareLink(dialogShareFile.shareData.shareBatchNum, dialogShareFile.shareData.extractionCode)"
        >Copy share link</el-button
        >
        <template v-else>
          <el-button @click="handleShareFileDialogCancel('shareFileForm')">Cancel</el-button>
          <el-button
            :loading="dialogShareFile.loading"
            type="primary"
            @click="handleShareFileDialogOk('shareFileForm')"
          >Confirm</el-button
          >
        </template>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ShareFileDialog',
  props: {
    dialogShareFile: {
      required: true,
      type: Object,
    },
  },
  data() {
    return {
      // 分享文件对话框数据
      form: {
        // endTime: (new Date()).getTime() + 3600 * 1000 * 24,
        endTime: '',
        shareType: 0,
      },
      rules: {
        endTime: [{ required: true, message: 'Please choose the end date', trigger: 'blur' }],
      },
      loading: false,
      pickerOptions: {
        shortcuts: [
          {
            text: 'Today',
            onClick(picker) {
              picker.$emit('pick', new Date())
            },
          },
          {
            text: 'One day',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24)
              picker.$emit('pick', date)
            },
          },
          {
            text: 'One Week',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
              picker.$emit('pick', date)
            },
          },
          {
            text: 'A month',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 30)
              picker.$emit('pick', date)
            },
          },
        ],
      },
    }
  },
  methods: {
    /**
     * 分享文件对话框 | 取消按钮点击事件
     * @description 关闭对话框，重置表单
     * @param {string} formName 表单ref值
     */
    handleShareFileDialogCancel(formName) {
      this.$emit('setDialogShareFileData', false)
      this.$refs[formName].resetFields()
    },
    /**
     * 分享文件对话框 | 确定按钮点击事件
     * @description 校验表单，校验通过后调用分享文件接口
     * @param {string} formName 表单ref值
     */
    handleShareFileDialogOk(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$emit('setDialogShareFileData', true, this.form)
        } else {
          return false
        }
      })
    },
  },
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl';

.success-tip {
  margin-bottom: 16px;
  text-align: center;
  color: $Success;

  .el-icon-success {
    margin-right: 8px;
    height: 20px;
    line-height: 20px;
  }
}
</style>
