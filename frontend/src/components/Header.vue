<template>
  <div class="header-wrapper">
    <img :src="logoUrl" class="logo" @click="$router.push({ name: 'Home' })">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" router>
      <el-menu-item :route="{ name: 'Home' }" index="Home">Home</el-menu-item>
      <el-menu-item :route="{ name: 'File', query: { fileType: 0, filePath: '/' } }" index="File">Space</el-menu-item>
      <el-menu-item :route="{ name: 'myShare', query: { filePath: '/' } }" index="MyShare">Share</el-menu-item>
      <!-- 为了和其他菜单样式保持一致，请一定要添加类名 el-menu-item -->
      <div v-show="isLogin" class="el-menu-item exit" @click="exitButton()">
        Exit
      </div>
      <div v-show="isLogin" class="el-menu-item username">
        <i class="el-icon-user-solid"/>
        {{ username }}
      </div>
      <el-menu-item v-show="!isLogin" :route="{ name: 'Login' }" class="login" index="Login">
        Login
      </el-menu-item>
      <!-- 开发环境 -->
      <el-menu-item v-show="!isLogin" :route="{ name: 'Register' }" class="register" index="Register">
        Register
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: 'Header',
    data() {
      return {
        logoUrl: require('@/assets/images/common/logo_header.png'),
      }
    },
    computed: {
      ...mapGetters(['isLogin', 'username']),
      // 当前激活菜单的 index
      activeIndex() {
        return this.$route.name || 'Home' //  获取当前路由名称
      },
    },
    methods: {
      /**
       * 退出登录
       * @description 清除 cookie 存放的 token 和 viewDomain 并跳转到登录页面
       */
      exitButton() {
        this.$message.success('Logged out！')
        this.$store.dispatch('getUserInfo').then(() => {
          this.removeCookies('viewDomain')
          this.removeCookies('token')
          this.$router.push({path: '/login'})
        })
      },
    },
  }
</script>

<style lang="stylus" scoped>
  @import '~@/assets/styles/varibles.styl';

  .header-wrapper {
    width: 100%;
    box-shadow: $tabBoxShadow;
    display: flex;

    .logo {
      margin: 14px 24px 0 24px;
      display: inline-block;
      height: 40px;
      cursor: pointer;
    }

    >>> .el-menu--horizontal {
      .el-menu-item:not(.is-disabled):hover {
        border-bottom-color: $Primary !important;
        background: $tabBackColor;
      }
    }

    .el-menu-demo {
      flex: 1;

      .headerLogo {
        color: $Primary;
        font-size: 60px;
        opacity: 1;
        cursor: default;

        a {
          display: block;
        }
      }

      .login, .register, .username, .exit {
        float: right;
      }
    }
  }
</style>
