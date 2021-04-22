<template>
  <div id="loginBackground" class="login-wrapper">
    <div class="form-wrapper">
      <h1 class="login-title">登录</h1>
      <p class="login-system">奇文网盘</p>
      <!-- 登录表单 -->
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginFormRules"
        class="login-form"
        label-width="100px"
        hide-required-asterisk
      >
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-mobile-phone" placeholder="手机号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" placeholder="密码" show-password/>
        </el-form-item>
        <el-form-item class="login-btn-form-item">
          <el-button
            :disabled="loginBtnDisabled"
            class="login-btn"
            type="primary"
            @click="submitForm('loginForm')">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  // import CanvasNest from 'canvas-nest.js'
  import { login } from '@/request/user.js'

  export default {
    name: 'Login',
    components: { },
    data() {
      return {
        // 登录表单数据
        loginForm: {
          username: '13838384388',
          password: 'wjc123',
        },
        // 登录表单验证规则
        loginFormRules: {
          username: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            {
              min: 4,
              max: 20,
              message: '长度在 5 到 20 个字符',
              trigger: 'blur',
            },
          ],
        },
        isPassing: false, //  滑动解锁是否验证通过
        loginBtnDisabled: false, //  登录按钮是否禁用
      }
    },
    computed: {
      url() {
        const _url = this.$route.query.redirect //  获取路由前置守卫中 next 函数的参数，即登录后要去的页面
        return _url ? { path: _url } : { name: 'File', query: { fileType: 0, filePath: '/' } }  //  若登录之前有页面，则登录后仍然进入该页面
      },
    },
    watch: {
      //  滑动解锁验证通过时，若重新输入用户名或密码，滑动解锁恢复原样
      'loginForm.username'() {
        this.isPassing = false
      },
      'loginForm.password'() {
        this.isPassing = false
      },
    },
    created() {
      // 用户若已登录，自动跳转到首页
      if (this.$store.getters.isLogin) {
        const username = this.$store.getters.username
        this.$message({
          message: `${username} 您已登录！已跳转到首页`,
          center: true,
          type: 'success',
        })
        this.$router.replace({ name: 'Home' })
      }
      //  绘制背景图
      this.$nextTick(() => {
        // const element = document.getElementById('loginBackground')
        // new CanvasNest(element, config)
      })
    },
    methods: {
      /**
       * 滑动解锁完成 回调函数
       * @param {boolean} isPassing 解锁是否通过
       */
      updateIsPassing(isPassing) {
        if (isPassing) {
          this.loginBtnDisabled = false
        } else {
          this.loginBtnDisabled = true
        }
      },
      /**
       * 登录按钮点击事件 表单验证&用户登录
       * @param {boolean} formName 表单ref值
       */
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // 表单各项校验通过
            login(this.loginForm, true).then((res) => {
              if (res.success) {
                this.setCookies('token', res.data.user.token) //  存储登录状态
                this.$refs[formName].resetFields() //  清空表单
                this.$router.replace(this.url)  //  跳转到前一个页面或者网盘主页
              } else {
                this.$message.error('手机号或密码错误！')
                this.isPassing = false
              }
            })
          } else {
            this.$message.error('error login！')
            return false
          }
        })
      },
    },
  }
</script>
<style lang="stylus" scoped>
  .login-wrapper {
    height: 550px !important;
    min-height: 550px !important;
    padding-top: 50px;

    .form-wrapper {
      width: 375px;
      margin: 0 auto;
      text-align: center;

      .login-title {
        margin-bottom: 10px;
        font-weight: 300;
        font-size: 30px;
        color: #000;
      }

      .login-system {
        font-weight: 300;
        color: #999;
      }

      .login-form {
        width: 100%;
        margin-top: 20px;

        >>> .el-form-item__content {
          margin-left: 0 !important;
        }

        &>>> .el-input__inner {
          font-size: 16px;
        }

        .login-btn-form-item {
          .login-btn {
            width: 100%;
          }

          &>>> .el-button {
            padding: 10px 90px;
            font-size: 16px;
          }
        }
      }

      .tip {
        width: 70%;
        margin-left: 86px;
      }
    }
  }
</style>
