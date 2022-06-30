<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="login-container">
    <a-row>
      <a-col :xs="0" :md="0" :sm="12" :lg="14" :xl="16"></a-col>
      <a-col :xs="24" :sm="24" :md="12" :lg="10" :xl="6">
        <div v-if="registerShow" class="login-container-form">
          <div class="login-container-hello">注册</div>
          <div class="login-container-title">欢迎来到 {{ title }}</div>
          <a-form :model="registerForm">
            <a-form-item>
              <a-input v-model:value="registerForm.phone" placeholder="手机号">
                <template v-slot:prefix>
                  <UserOutlined style="color: rgba(0, 0, 0, 0.25)" />
                </template>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-input
                v-model:value="registerForm.password"
                type="password"
                placeholder="Password"
              >
                <template v-slot:prefix>
                  <LockOutlined style="color: rgba(0, 0, 0, 0.25)" />
                </template>
              </a-input>
            </a-form-item>

            <a-form-item>
              <a-row>
                <a-col span="12">
                  <a-input v-model:value="registerForm.code" placeholder="验证码">
                    <template v-slot:prefix>
                      <PicCenterOutlined style="color: rgba(0, 0, 0, 0.25)" />
                    </template>
                  </a-input>
                </a-col>
                <a-col span="12">
                  <a-button @click="requestCode" :disabled="secondsToGo !== 60">
                    {{ secondsToGo === 60 ? '验证码' : secondsToGo + 's' }}
                  </a-button>
                </a-col>
              </a-row>
            </a-form-item>

            <a-form-item>
              <a-button
                @click="registerHandler"
                type="primary"
                html-type="submit"
                :disabled="
                  registerForm.username === '' || registerForm.password === ''
                "
                :loading="registerLoading"
              >
                注册
              </a-button>
            </a-form-item>
          </a-form>
          <Button type="text" shape="round" @click="registerShowHandler">登录</Button>
        </div>
        <div v-else class="login-container-form">
          <div class="login-container-hello">Hello!</div>
          <div class="login-container-title">欢迎来到 {{ title }}</div>
          <a-form :model="form" @submit="handleSubmit" @submit.prevent>
            <a-form-item>
              <a-input v-model:value="form.phone" placeholder="手机号">
                <template v-slot:prefix>
                  <UserOutlined style="color: rgba(0, 0, 0, 0.25)" />
                </template>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-input
                v-model:value="form.password"
                type="password"
                placeholder="Password"
              >
                <template v-slot:prefix>
                  <LockOutlined style="color: rgba(0, 0, 0, 0.25)" />
                </template>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-button
                type="primary"
                html-type="submit"
                :disabled="form.username === '' || form.password === ''"
              >
                登录
              </a-button>
            </a-form-item>
          </a-form>
          <Button type="text" shape="round" @click="registerShowHandler">注册</Button>
        </div>
      </a-col>
    </a-row>
    <!--<div class="login-container-tips">羿歌信息开发</div>-->
  </div>
</template>
<script>
  import { dependencies, devDependencies } from '*/package.json'
  import { mapActions, mapGetters } from 'vuex'
  import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
  import { registerCode, register } from '@/api/user'

  export default {
    name: 'Login',
    components: {
      UserOutlined,
      LockOutlined,
    },
    data() {
      return {
        form: {
          phone: '',
          password: '',
        },
        registerForm: {
          phone: '',
          password: '',
          code: '',
        },
        secondsToGo: 60,
        registerShow: false,
        redirect: undefined,
        dependencies: dependencies,
        devDependencies: devDependencies,
        registerLoading: false
      }
    },
    computed: {
      ...mapGetters({
        logo: 'settings/logo',
        title: 'settings/title',
      }),
    },
    watch: {
      $route: {
        handler(route) {
          this.redirect = (route.query && route.query.redirect) || '/'
        },
        immediate: true,
      },
    },
    mounted() {
      // this.form.phone = 'admin'
      // this.form.password = '123456'
      /*  setTimeout(() => {
        this.handleSubmit()
      }, 3000) */
    },
    methods: {
      ...mapActions({
        login: 'user/login',
      }),
      countDown() {
        this.interval = setInterval(() => {
          clearInterval(this.interval)
          this.secondsToGo -= 1
          if (0 === this.secondsToGo) {
            this.secondsToGo = 60
          } else {
            this.countDown()
          }
        }, 1000)
      },
      registerHandler() {
        this.registerLoading = true
        register(this.registerForm).then((res) => {
          this.$message.success('注册成功,请跳转到登录界面!')
        }).finally(() => {
          this.registerLoading = false
        })
      },
      registerShowHandler() {
        this.registerShow = !this.registerShow
      },
      requestCode() {
        registerCode(this.registerForm).then((res) => {
          // console.log(res)
        })
        this.secondsToGo = 59
        this.countDown()
      },
      handleRoute() {
        return this.redirect === '/404' || this.redirect === '/403'
          ? '/'
          : this.redirect
      },
      async handleSubmit() {
        await this.login(this.form)
        await this.$router.push(this.handleRoute())
      },
    },
  }
</script>
<style lang="less">
  .login-container {
    width: 100%;
    height: 100vh;
    background: url('~@/assets/login_images/login_background.png');
    background-size: cover;
    &-form {
      width: calc(100% - 40px);
      height: 420px;
      padding: 4vh;
      margin-top: calc((100vh - 400px) / 2);
      margin-right: 20px;
      margin-left: 20px;
      background: url('~@/assets/login_images/login_form.png');
      background-size: 100% 100%;
      border-radius: 10px;
      box-shadow: 0 2px 8px 0 rgba(7, 17, 27, 0.06);
    }
    &-hello {
      font-size: 32px;
      color: #fff;
    }
    &-title {
      margin-bottom: 30px;
      font-size: 20px;
      color: #fff;
    }
    &-tips {
      position: fixed;
      bottom: @vab-margin;
      width: 100%;
      height: 40px;
      color: rgba(255, 255, 255, 0.856);
      text-align: center;
    }
    .ant-col {
      width: 100%;
      /*padding: 0 0 0 10px;*/
    }
    .ant-input {
      height: 35px;
    }
    .ant-btn {
      width: 100%;
      height: 45px;
      border-radius: 99px;
    }
  }
</style>
