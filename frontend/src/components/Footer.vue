<template>
  <div class="footer-wrapper">
    <div class="copy-right-wrapper">
      <img :src="logoUrl" :alt="$store.getters.imgAlt + 'footerLogo'" class="logo" >
      <div class="copy-right">
        <span>Final Project for CS6650 | </span>
        <span class="tip-website">
          Please use Chrome for better experience
        </span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Footer',
  data() {
    return {
      logoUrl: require('@/assets/images/common/logo_footer.png'),
      joinList: [
        {
          class: 'qq',
          logo: 'icon-qq',
          img: require('@/assets/images/footer/QQImg.png'),
          title: 'QQ群',
        },
        {
          class: 'wechat',
          logo: 'icon-weixin-copy',
          img: require('@/assets/images/footer/wechatImg.png'),
          title: '微信公众号',
        },
        {
          class: 'gitee',
          logo: 'icon-mayun',
          img: require('@/assets/images/footer/giteeImg.png'),
          title: '码云 开源社区',
        },
      ],
    }
  },
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl';

.footer-wrapper {
  /*margin-top: 20px;*/
  display: flex;
  height 80px;

  .copy-right-wrapper {
    width: 100%;
    color: $BorderLight;
    background: linear-gradient(to right, $Primary, #66b1ff);
    padding: 16px 0 16px 2vw;
    font-size: 14px;
    color: $BorderLight;
    display: flex;
    align-items: center;

    .logo {
      width: 60px;
      display: block;
    }

    .split {
      margin: 0 8px;
    }

    .link {
      &:hover {
        text-decoration: underline;
      }
    }

    .tip-website {
      padding-top: 8px;
    }
  }

}
</style>
