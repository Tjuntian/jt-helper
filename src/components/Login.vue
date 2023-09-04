<template>
  <div class="container">
    <el-card class="box-card">
      <el-form :show-message="false" class="login-from" label-position="top" label-width="80px" :model="fromData"
        :rules="rules" ref="loginFrom">
        <div class="from-inner">
          <svg class="svg-inline--fa" aria-hidden="true" data-prefix="fas" data-icon="arrow-left" role="img"
            xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" data-fa-i2svg="">
            <path fill="currentColor"
              d="M257.5 445.1l-22.2 22.2c-9.4 9.4-24.6 9.4-33.9 0L7 273c-9.4-9.4-9.4-24.6 0-33.9L201.4 44.7c9.4-9.4 24.6-9.4 33.9 0l22.2 22.2c9.5 9.5 9.3 25-.4 34.3L136.6 216H424c13.3 0 24 10.7 24 24v32c0 13.3-10.7 24-24 24H136.6l120.5 114.8c9.8 9.3 10 24.8.4 34.3z">
            </path>
          </svg>
          <img class="img-icon" src="../assets/images/icon.png" style="width: 50px;" />
          <h4 class="heading-text">
            Welcome jt-helper,
            <br>
            login to your account.
          </h4>
          <div class="input-item">
            <el-form-item class="input-item-a" prop="account">
              <el-input placeholder="Your Account" v-model="fromData.account"></el-input>
            </el-form-item>
            <el-form-item class="input-item-a" prop="password">
              <el-input placeholder="Password" v-model="fromData.password"></el-input>
            </el-form-item>
            <el-form-item class="input-item-a" prop="checkCode">
              <div class="item-3">
                <el-input @keyup.enter.native="login()" placeholder="Verification Code"
                  v-model="fromData.checkCode"></el-input>
                <div class="img-box">
                  <img :src="fromData.checkCodeUrl" @click="getCheckCode()">
                </div>
              </div>
            </el-form-item>
          </div>
          <el-form-item>
            <el-button @click="login()" type="primary">Sign in</el-button>
          </el-form-item>
        </div>
      </el-form>
    </el-card>
    <div class="square">
      <ul>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
    </div>
    <div class="circle">
      <ul>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
    </div>
    <div id="col-c">
      <div class="col-c-bottom">
        <span class="BeiAn">©2023 JT-Helper</span>
        <span class="BeiAn" style="cursor:pointer;" @click="toWebsite">黔ICP备2023003770号-1</span>
        <div>
          <img style="width: 10px; height: 10px;" src="../assets/images/GongAn.png">
          <span class="BeiAn" style="cursor:pointer;" @click="toGongAn">贵公网安备52030302000848号</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import FingerprintJS from '@fingerprintjs/fingerprintjs'
export default {
  components: {},
  data() {
    return {
      fromData: {
        account: '',
        password: '',
        checkCode: '',
        checkCodeUrl: require('../assets/images/checkCode-no.png')
      },
      rules: {
        account: [
          { required: true, trigger: 'blur' },
          { min: 3, max: 20, trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur' },
          { min: 3, max: 20, trigger: 'blur' }
        ],
        checkCode: [
          { required: true, trigger: 'blur' },
          { min: 4, max: 4, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 设置token 单位毫秒
    setCookie(name, value, days, maxAge) {
      var expires = "";
      if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
      } else if (maxAge) {
        expires = "; max-age=" + maxAge;
      }
      document.cookie = name + "=" + (value || "") + expires + "; path=/";
    },
    // 登录
    login() {

      this.$refs['loginFrom'].validate((valid) => {
        if (valid) {
          const key = window.sessionStorage.getItem('key');
          if (key !== null) {
            this.$http.post('/anno/login', {
              account: this.fromData.account,
              password: this.fromData.password,
              key: key,
              code: this.fromData.checkCode
            }).then(res => {
              if (res.data.code !== 0) { return this.$message.error(res.data.msg) }
              this.setCookie('token', res.data.data.token, null, res.data.data.expire);
              this.$message.success('登录成功');
              this.$router.push('/helper');
              return;
            }).catch(err => {
              this.$message.error('登录出错啦!');
            })
            this.getCheckCode();
          } else {
            this.generateUniqueId().then(id => {
              window.sessionStorage.setItem('key', id);
            }).catch(err => {
              const UUID = this.getUuid();
              window.sessionStorage.setItem('key', UUID);
            })
          }
          return;
        } else {
          this.$message.error('请正确填写!')
          return false;
        }
      });
    },

    //获取验证码
    getCheckCode() {
      const key = window.sessionStorage.getItem('key');
      if (key !== null) {
        this.$http.get('/anno/captcha', {
          params: {
            key: key
          },
          responseType: 'blob'
        }).then(res => {
          if (res.status !== 200) {
            this.$message.error('网络超时,请稍后再试~');
          }
          this.fromData.checkCodeUrl = URL.createObjectURL(res.data);
        }).catch(err => {
          console.log(err);
          this.$message.error('网络超时,请稍后再试~');
        })
      } else {
        this.generateUniqueId().then(id => {
          window.sessionStorage.setItem('key', id);
        }).catch(err => {
          const UUID = this.getUuid();
          window.sessionStorage.setItem('key', UUID);
        })
      }
      return;
    },
    // 转base64
    blobToBase64(blob) {
      return new Promise((resolve, reject) => {
        const fileReader = new FileReader();
        fileReader.onload = (e) => {
          resolve(e.target.result);
        };
        //readAsDataURL
        fileReader.readAsDataURL(blob);
        fileReader.onerror = () => {
          reject(new Error('文件流异常'));
        };
      });
    },
    // 获取浏览器唯一标识
    async generateUniqueId() {
      const fp = await FingerprintJS.load();
      const result = await fp.get();
      return result.visitorId;
    },
    //生成uuid
    getUuid() {
      if (typeof crypto === 'object') {
        if (typeof crypto.randomUUID === 'function') {
          return crypto.randomUUID();
        }
        if (typeof crypto.getRandomValues === 'function' && typeof Uint8Array === 'function') {
          const callback = (c) => {
            const num = Number(c);
            return (num ^ (crypto.getRandomValues(new Uint8Array(1))[0] & (15 >> (num / 4)))).toString(16);
          };
          return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, callback);
        }
      }
      let timestamp = new Date().getTime();
      let perforNow = (typeof performance !== 'undefined' && performance.now && performance.now() * 1000) || 0;
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
        let random = Math.random() * 16;
        if (timestamp > 0) {
          random = (timestamp + random) % 16 | 0;
          timestamp = Math.floor(timestamp / 16);
        } else {
          random = (perforNow + random) % 16 | 0;
          perforNow = Math.floor(perforNow / 16);
        }
        return (c === 'x' ? random : (random & 0x3) | 0x8).toString(16);
      });
    },
    // 转跳管局官网
    toWebsite() {
      window.open(`https://beian.miit.gov.cn`);
    },
    // 转跳公安官网
    toGongAn() {
      window.open(`http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=52030302000848`);
    }
  },
  created() {
    const key = window.sessionStorage.getItem('key');
    if (key === null) {
      this.generateUniqueId().then(id => {
        window.sessionStorage.setItem('key', id);
        this.getCheckCode();
      }).catch(err => {
        const UUID = this.getUuid();
        window.sessionStorage.setItem('key', UUID);
        this.getCheckCode();
      })
    } else {
      this.getCheckCode();
    }
    return;
  }
}
</script>
<style lang="less" scoped>
#col-c {
  margin-top: auto;
  width: 100%;
  text-align: center;
  position: absolute;
  bottom: 10px;

  .col-c-bottom {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .BeiAn {
      font-size: 12px;
      color: #ffffff;
      margin-top: 6px;
    }

  }
}


ul li {
  position: absolute;
  border: 1px solid #fff;
  background-color: #fff;
  width: 30px;
  height: 30px;
  list-style: none;
  opacity: 0;
}

.square li {
  top: 40vh;
  left: 70vw;
  animation: square 10s linear infinite;

}

.square li:nth-child(2) {
  top: 80vh;
  left: 10vw;
  // 设置动画延迟时间
  animation-delay: 2s;
}

.square li:nth-child(3) {
  top: 80vh;
  left: 85vw;
  animation-delay: 4s;
}

.square li:nth-child(4) {
  top: 10vh;
  left: 75vw;
  animation-delay: 6s;
}

.square li:nth-child(5) {
  top: 30vh;
  left: 20vw;
  animation-delay: 8s;
}

.circle li {
  bottom: 0;
  left: 15vw;
  animation: circle 10s linear infinite;
}

.circle li:nth-child(2) {
  left: 25vw;
  animation-delay: 2s;
}

.circle li:nth-child(3) {
  left: 65vw;
  animation-delay: 6s;
}

.circle li:nth-child(4) {
  left: 75vw;
  animation-delay: 4s;
}

.circle li:nth-child(5) {
  left: 90vw;
  animation-delay: 8s;
}

@keyframes square {
  0% {
    transform: scale(0) rotateY(0deg);
    opacity: 1;
  }

  100% {
    transform: scale(3) rotateY(1000deg);
    opacity: 0;
  }
}

@keyframes circle {
  0% {
    transform: scale(0) rotateY(0deg);
    opacity: 1;
    bottom: 0;
    border-radius: 0;
  }

  100% {
    transform: scale(4) rotateY(1000deg);
    opacity: 0;
    bottom: 90vh;
    border-radius: 50%;
  }
}

.input-item {
  margin-top: 30px;

  .el-form-item {
    margin-bottom: 15px;
  }

}

.item-3 {
  display: flex;
  justify-content: space-around;
  align-items: center;

  .el-input {
    margin-right: 40px;
  }

  .img-box {
    height: 36px;
    width: 80px;
    padding: 10px 0 0 0;
    margin-right: 10px;
  }
}



.from-inner {
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.img-icon {
  vertical-align: middle;
  border-style: none;
  box-sizing: border-box;
  overflow-clip-margin: content-box;
  overflow: clip;
  color: #fff;
  margin-top: 50px;
}

.el-button {
  background: linear-gradient(-200deg, #17ead9, #6078ea);
  width: 100%;
  font-size: 1.09375rem;
  line-height: 1.2;
  border-radius: 0.375rem;
  font-weight: 600;
  text-align: center;
  margin-top: 30px;
  transition: 1s;
}



.el-button:hover {
  background: linear-gradient(-200deg, #6078ea, #17ead9);
}


.el-input {

  ::v-deep .el-input__inner {
    background-color: #006dea;
    color: #fff;
    border: 0.0625rem solid #006dea;
    font-size: .875rem;
    line-height: 1.6;
    background-clip: padding-box;
    border-radius: 0.375rem;
    box-shadow: none;
    border-color: #288cff !important;
  }
}

.container {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #abdcff, #0396ff);
}

.box-card {
  width: 320px;
  height: 500px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background: #288cff;
  border: 3px solid #288cff;
  overflow: hidden;

  .el-card__body {
    height: 100% !important;
  }
}

.login-from {
  inset: 3px;
  z-index: 10;
  background: #288cff;
  position: absolute;
}

.box-card::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 320px;
  height: 500px;
  background: linear-gradient(0deg, transparent, #45f3ff, #45f3ff);
  transform-origin: bottom right;
  animation: animate 6s linear infinite;
}

.box-card::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 320px;
  height: 500px;
  background: linear-gradient(0deg, transparent, #45f3ff, #45f3ff);
  transform-origin: bottom right;
  animation: animate 6s linear infinite;
  animation-delay: -3s;
}



@keyframes animate {
  0% {
    transform: rotate(0deg);

  }

  100% {
    transform: rotate(360deg);
  }

}

.svg-inline--fa {
  line-height: 1.6;
  color: #fff;
  width: 14px;
  cursor: pointer;
}

.heading-text {
  color: #fff !important;
  font-family: "Helvetica Neue";
}
</style>
